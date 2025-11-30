package content.global.skill.slayer

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import shared.consts.Animations
import shared.consts.Scenery

class SlayerTowerPlugin : InteractionListener {

    companion object {
        private val SLAYER_DOOR_IDS = intArrayOf(Scenery.DOOR_4490, Scenery.DOOR_4487, Scenery.DOOR_4491, Scenery.DOOR_4492)
        private val SLAYER_DOOR_FIRST_FLOOR_IDS = intArrayOf(Scenery.DOOR_10527,Scenery.DOOR_10528)
        private val SPIKEY_CHAIN_IDS = intArrayOf(Scenery.SPIKEY_CHAIN_9319,Scenery.SPIKEY_CHAIN_9320)
    }

    enum class GargoyleStatues(val x: Int, val y: Int, val z: Int) {
        STATUE_1(3426, 3534, 0),
        STATUE_2(3430, 3534, 0);

        fun get(): core.game.node.scenery.Scenery? = getScenery(x, y, z)
    }

    override fun defineListeners() {
        on(SLAYER_DOOR_IDS, IntType.SCENERY, "open", "close") { player, node ->
            if (node.id in listOf(Scenery.DOOR_4487, Scenery.DOOR_4490, Scenery.DOOR_4491, Scenery.DOOR_4492)) {
                DoorActionHandler.handleDoor(player, node.asScenery())
                val option = getUsedOption(player)
                val anim = when(option) {
                    "open" ->  Animations.GARGOYLE_STATUE_OPEN_1533 to 2717
                    "close" -> Animations.GARGOYLE_STATUE_CLOSE_1532 to 2718
                    else -> null
                }
                anim?.let { (animation, sound) ->
                    GargoyleStatues.values().forEach { statue ->
                        statue.get()?.let { animateScenery(it, animation) }
                    }
                    playGlobalAudio(player.location, sound)
                }
            }
            return@on true
        }


        on(SLAYER_DOOR_FIRST_FLOOR_IDS, IntType.SCENERY, "open", "close") { player, node ->
            DoorActionHandler.handleDoor(player, node.asScenery())
            return@on true
        }

        on(SPIKEY_CHAIN_IDS, IntType.SCENERY, "climb-up", "climb-down") { player, node ->
            val level = if (player.location.z == 0) 61 else 71
            if (node.id == Scenery.SPIKEY_CHAIN_9319 && getStatLevel(player, Skills.AGILITY) < level) {
                sendMessage(player, "You need an agility level of at least $level in order to do this.")
            }
            return@on true
        }
    }
}
