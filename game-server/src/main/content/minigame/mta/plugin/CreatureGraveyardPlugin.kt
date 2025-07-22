package content.minigame.mta.plugin

import core.api.*
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Music
import org.rs.consts.Sounds

/**
 * Represents the Creature Graveyard.
 */
class CreatureGraveyardPlugin :
    MTAZone("Creature Graveyard", arrayOf(Item(Items.ANIMALS_BONES_6904), Item(Items.ANIMALS_BONES_6905), Item(Items.ANIMALS_BONES_6906), Item(Items.ANIMALS_BONES_6907), Item(Items.BANANA_1963), Item(Items.PEACH_6883)),) {

    override fun update(player: Player?) {
        val player = player ?: return
        val type = type ?: return
        val points = getVarbit(player, type.varbit)
        sendString(player, points.toString(), type.overlay.id, 12)
    }

    override fun leave(entity: Entity, logout: Boolean): Boolean {
        if (entity is Player) PLAYERS.remove(entity)
        return super.leave(entity, logout)
    }

    override fun enter(entity: Entity): Boolean {
        if (entity is Player && PLAYERS.add(entity)) {
            if (!PULSE.isRunning) {
                PULSE.restart()
                PULSE.start()
                Pulser.submit(PULSE)
            }
            entity.musicPlayer.takeIf { !it.hasUnlocked(Music.ROLL_THE_BONES_533) }?.unlock(Music.ROLL_THE_BONES_533)
        }
        return super.enter(entity)
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (e is Player) {
            when (target.id) {
                org.rs.consts.Scenery.FOOD_CHUTE_10735 -> deposit(e)
                in org.rs.consts.Scenery.BONES_10725..org.rs.consts.Scenery.BONES_10728 -> BoneType.forObject(target.id)
                    ?.grab(e, target.asScenery())

                else -> return super.interact(e, target, option)
            }
            return true
        }
        return super.interact(e, target, option)
    }

    override fun death(e: Entity, killer: Entity): Boolean {
        if (e is Player) {
            val player = e.asPlayer() ?: return super.death(e, killer)
            val varbitId = MTAType.GRAVEYARD.varbit
            val points = getVarbit(player, varbitId)
            val toRemove = points.coerceAtMost(10)
            if (toRemove > 0) {
                setVarbit(player, varbitId, points - toRemove)
                sendMessage(player, "You lost $toRemove Pizazz Points upon death!")
            }
        }
        return super.death(e, killer)
    }

    private fun deposit(player: Player) {
        if (!inInventory(player, BANANA) && !inInventory(player, PEACH)) {
            sendDialogue(player, "You don't have any bananas or peaches to deposit.")
            return
        }

        val items = arrayOf(BANANA, PEACH)
        var totalAmount =
            player.getAttribute("grave-amt", 0) + items.sumOf { item ->
                amountInInventory(player, item).also { removeItem(player, Item(item, it)) }
            }

        if (totalAmount >= 16) {
            totalAmount = (totalAmount - 16).coerceAtLeast(0)
            val rune = RandomFunction.getRandomElement(RUNES)
            val runeName = rune.name.lowercase()
            player.inventory.add(rune, player)
            incrementPoints(player, MTAType.GRAVEYARD.ordinal, 1)
            rewardXP(player, Skills.MAGIC, 50.0)
            playAudio(player, Sounds.MTA_DEPOSIT_FRUIT_1663)
            sendDialogueLines(
                player,
                "Congratulations - you've been awarded a${
                    if (StringUtils.isPlusN(
                            runeName,
                        )
                    ) {
                        "n"
                    } else {
                        ""
                    }
                } $runeName and extra",
                "magic XP.",
            )
        }

        setAttribute(player, "grave-amt", totalAmount)
    }

    override fun configure() {
        PULSE.stop()
        register(ZoneBorders(3333, 9610, 3390, 9663, 1, true))
    }

    enum class BoneType(val objectsId: Int, val item: Item) {
        FIRST(org.rs.consts.Scenery.BONES_10725, Item(Items.ANIMALS_BONES_6904)),
        SECOND(org.rs.consts.Scenery.BONES_10726, Item(Items.ANIMALS_BONES_6905)),
        THIRD(org.rs.consts.Scenery.BONES_10727, Item(Items.ANIMALS_BONES_6906)),
        FOURTH(org.rs.consts.Scenery.BONES_10728, Item(Items.ANIMALS_BONES_6907)),
        ;

        fun grab(
            player: Player,
            scenery: Scenery,
        ) {
            if (!hasSpaceFor(player, item)) {
                sendMessage(player, "You have no free space!")
                return
            }
            lock(player, 1)
            addItem(player, item.id)
            animate(player, Animations.HUMAN_BURYING_BONES_827)
            playAudio(player, Sounds.MTA_DEPOSIT_BONE_1660) // ?
            var life = scenery.attributes.getAttribute("life", 4) - 1
            if (life < 1) {
                life = 4
                val nextType = values().getOrNull(ordinal + 1) ?: FIRST
                SceneryBuilder.replace(scenery, scenery.transform(nextType.objectsId))
            }
            scenery.attributes.setAttribute("life", life)
        }

        companion object {
            fun forItem(item: Item): BoneType? = values().find { it.item.id == item.id }

            fun forObject(scenery: Int): BoneType? = values().find { it.objectsId == scenery }
        }
    }

    companion object {
        val ZONE = CreatureGraveyardPlugin()
        private val PLAYERS = mutableListOf<Player>()
        private val RUNES = arrayOf(Item(Items.NATURE_RUNE_561), Item(Items.WATER_RUNE_555), Item(Items.EARTH_RUNE_557))
        private const val BANANA = Items.BANANA_1963
        private const val PEACH = Items.PEACH_6883
        private val GFX_POS =
            arrayOf(
                Location.create(3370, 9634, 1),
                Location.create(3359, 9634, 1),
                Location.create(3360, 9643, 1),
                Location.create(3363, 9643, 1),
                Location.create(3363, 9633, 1),
                Location.create(3367, 9637, 1),
                Location.create(3366, 9642, 1),
                Location.create(3380, 9647, 1),
                Location.create(3375, 9648, 1),
                Location.create(3370, 9649, 1),
                Location.create(3372, 9644, 1),
                Location.create(3379, 9646, 1),
                Location.create(3381, 9651, 1),
                Location.create(3379, 9656, 1),
                Location.create(3373, 9658, 1),
                Location.create(3353, 9648, 1),
                Location.create(3356, 9653, 1),
                Location.create(3346, 9657, 1),
                Location.create(3347, 9655, 1),
                Location.create(3354, 9634, 1),
                Location.create(3352, 9631, 1),
                Location.create(3355, 9627, 1),
                Location.create(3345, 9631, 1),
                Location.create(3345, 9627, 1),
                Location.create(3347, 9623, 1),
                Location.create(3351, 9621, 1),
                Location.create(3368, 9624, 1),
                Location.create(3368, 9631, 1),
                Location.create(3375, 9630, 1),
                Location.create(3375, 9635, 1),
                Location.create(3371, 9621, 1),
                Location.create(3377, 9621, 1),
                Location.create(3381, 9624, 1),
                Location.create(3382, 9629, 1),
                Location.create(3371, 9635, 1),
                Location.create(3365, 9634, 1),
                Location.create(3361, 9641, 1),
                Location.create(3363, 9644, 1),
            )

        private val PULSE: Pulse =
            object : Pulse(6) {
                override fun pulse(): Boolean {
                    if (PLAYERS.isEmpty()) {
                        return true
                    }
                    val locations = GFX_POS.filter { RandomFunction.random(12) < 8 }.toMutableList()
                    PLAYERS.filter(Player::isActive).forEach { player ->
                        locations.forEach { loc ->
                            player.packetDispatch.sendPositionedGraphics(Graphics.create(520), loc)
                        }
                        playAudio(player, Sounds.MTA_BONEFALL_1661)
                        if (player.dialogueInterpreter.dialogue == null) {
                            player.impactHandler.manualHit(player, 2, ImpactHandler.HitsplatType.NORMAL)
                        }
                    }

                    delay = RandomFunction.random(6, 12)
                    return false
                }
            }
    }
}
