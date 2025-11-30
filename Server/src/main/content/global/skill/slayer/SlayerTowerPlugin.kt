package content.global.skill.slayer

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

class SlayerTowerPlugin : InteractionListener {

    companion object {
        private val SLAYER_DOOR_IDS = intArrayOf(Scenery.DOOR_4490, Scenery.DOOR_4487, Scenery.DOOR_4492)
    }

    enum class GargoyleStatues(val x: Int, val y: Int, val z: Int) {
        STATUE_1(3426, 3534, 0),
        STATUE_2(3430, 3534, 0);

        fun get(): core.game.node.scenery.Scenery? = getScenery(x, y, z)
    }

    override fun defineListeners() {
        on(SLAYER_DOOR_IDS, IntType.SCENERY, "open", "close") { player, node ->
            when (node.id) {
                Scenery.DOOR_4490, Scenery.DOOR_4487 -> {
                    DoorActionHandler.handleDoor(player, node.asScenery())
                    GargoyleStatues.values().forEach { statue ->
                        statue.get()?.let {
                            val anim = if (getUsedOption(player) == "open") 1533 else 1532
                            animateScenery(it, anim)
                        }
                    }
                }
            }
            return@on true
        }
    }
}
