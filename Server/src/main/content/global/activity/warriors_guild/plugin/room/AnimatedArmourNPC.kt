package content.global.activity.warriors_guild.plugin.room

import core.api.clearLogoutListener
import core.api.removeAttribute
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items

class AnimatedArmourNPC internal constructor(private val player: Player, location: Location?, private val set: AnimatedRoomPlugin.ArmourSet) : NPC(set.npcId, location) {

    private var running = false
    private var clearTime = 0

    override fun init() {
        super.init()
        animate(Animation.create(Animations.DEAD_FACE_DOWN_THEN_GETTING_UP_4166))
        sendChat("I'M ALIVE!")
        properties.combatPulse.attack(player)
        HintIconManager.registerHintIcon(player, this)
    }

    override fun clear() {
        super.clear()
        player.hintIconManager.clear()
    }

    override fun handleTickActions() {
        if (running) {
            if (!properties.combatPulse.isAttacking && !walkingQueue.isMoving) {
                clearTime++
                if (clearTime >= 50) {
                    clear()
                    removeAttribute(player, "animated_set")
                    return
                }
            }
        } else {
            clearTime = 0
        }

        if (!running && !properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(player)
        }

        super.handleTickActions()
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        if (entity !== player) {
            if (entity is Player) {
                sendMessage(entity, "This isn't your armour to attack.")
            }
            return false
        }
        return super.isAttackable(entity, style, message)
    }

    override fun finalizeDeath(killer: Entity?) {
        clear()
        clearLogoutListener(player, "animation-room")
        removeAttribute(player,"animated_set")

        if (killer == null) {
            return
        }

        var takenPiece = false
        val canTake = RandomFunction.random(180) == 1
        val takeIndex = if (canTake) RandomFunction.random(set.pieces.size) else -1

        set.pieces.forEachIndexed { index, piece ->
            if (index == takeIndex && !takenPiece) {
                takenPiece = true
                sendMessage(player, "Your armour was destroyed in the fight.")
            } else {
                GroundItemManager.create(Item(piece), location, player)
            }
        }

        GroundItemManager.create(Item(Items.WARRIOR_GUILD_TOKEN_8851, set.tokenAmount), location, player)
    }
}
