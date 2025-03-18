package content.minigame.mta.handlers

import content.minigame.mta.handlers.room.TelekineticTheatre
import core.api.*
import core.game.global.action.PickupHandler.canTake
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellBlocks.isBlocked
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Plugin
import org.rs.consts.Sounds

class TelekineticGrabSpell :
    MagicSpell(
        SpellBookManager.SpellBook.MODERN,
        33,
        43.0,
        ANIMATION,
        START_Graphics,
        SOUND,
        arrayOf(Item(Runes.AIR_RUNE.id), Item(Runes.LAW_RUNE.id, 1)),
    ) {
    override fun newInstance(arg: SpellType?): Plugin<SpellType?> {
        SpellBookManager.SpellBook.MODERN.register(SPELL_ID, this)
        return this
    }

    override fun cast(
        entity: Entity,
        target: Node,
    ): Boolean {
        if (target !is GroundItem) return false
        if (!canCast(entity, target)) {
            return false
        }
        entity.lock(2)
        visualize(entity, target)
        Pulser.submit(getGrabPulse(entity, target))
        return true
    }

    override fun visualize(
        entity: Entity,
        target: Node,
    ) {
        super.visualize(entity, target)
        entity.faceLocation(target.location)
        getProjectile(entity, target as GroundItem).send()
    }

    private fun getGrabPulse(
        entity: Entity,
        ground: GroundItem,
    ): Pulse {
        return object : Pulse(getDelay(ground.location.getDistance(entity.location)), entity) {
            override fun pulse(): Boolean {
                val player = if (entity is Player) entity else null
                val g = GroundItemManager.get(ground.id, ground.location, player!!)
                if (g == null) {
                    sendMessage(player, "Too late!")
                    return true
                }
                if (ground.id == 6561) {
                    openDialogue(player, 2692, findNPC(2692)!!, true)
                    return true
                }
                if (player == null) {
                    return true
                }
                val teleZone = inZone(player, "Telekinetic Theatre") && g.id == 6888
                if (player != null) {
                    if (g == null || !g.isActive) {
                        sendMessage(player, "Too late!")
                        return true
                    }
                    playAudio(player, Sounds.VULNERABILITY_IMPACT_3008)
                    if (!teleZone) {
                        player.inventory.add(Item(g.id, g.amount, g.charge))
                    } else {
                        val zone: TelekineticTheatre = TelekineticTheatre.getZone(player)
                        zone.moveStatue()
                        player.lock(delay)
                    }
                    sendGraphics(END_Graphics, ground.location)
                }
                if (!teleZone) {
                    GroundItemManager.destroy(g)
                }
                return true
            }
        }
    }

    fun canCast(
        entity: Entity,
        item: GroundItem?,
    ): Boolean {
        if (entity.locks.isInteractionLocked || entity.locks.isComponentLocked) {
            return false
        }

        if (entity is Player) {
            val player = entity
            if (!player.inventory.hasSpaceFor(item as Item?)) {
                sendMessage(player, "You don't have enough inventory space.")
                return false
            }
            if (!canTake(player, item!!, 1)) {
                return false
            }
        }
        if (isBlocked(SPELL_ID, (item as Node?)!!)) {
            sendDialogue(entity.asPlayer(), "You can't do that.")
            return false
        }
        return super.meetsRequirements(entity, true, true)
    }

    fun getProjectile(
        entity: Entity,
        item: GroundItem,
    ): Projectile =
        Projectile.create(
            entity.location,
            item.location,
            PROJECTILE_ID,
            START_HEIGHT,
            END_HEIGHT,
            START_DELAY,
            Projectile.getSpeed(entity, item.location),
            ANGLE,
            11,
        )

    fun getDelay(distance: Double): Int = (2 + distance * 0.5).toInt()

    companion object {
        private val ANIMATION = Animation(2310)
        private val START_Graphics = Graphics(org.rs.consts.Graphics.BOUNTY_LOCATE_CAST_142, 88, 15)
        private val END_Graphics = Graphics(org.rs.consts.Graphics.BOUNTY_LOCATE_IMPACT_144)
        private val SOUND = Audio(Sounds.TELEGRAB_HIT_3007, 10, 1)
        private const val PROJECTILE_ID = org.rs.consts.Graphics.BOUNTY_LOCATE_PROJECTILE_143
        private const val START_HEIGHT = 40
        private const val END_HEIGHT = 0
        private const val START_DELAY = 41
        private const val ANGLE = 5
        const val SPELL_ID = 19
    }
}
