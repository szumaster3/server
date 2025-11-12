package content.global.plugins.scenery

import core.api.replaceScenery
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.Scenery

class DoorPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles fremennik doors interaction.
         */

        on(Scenery.DOOR_1531, IntType.SCENERY, "close") { player, node ->
            if (node.location == Location.create(2598, 3404, 0)) {
                sendMessage(player, "The door appears to be stuck.")
                return@on true
            }
            DoorActionHandler.handleDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles fremennik doors (curtains) interaction.
         */

        on(Scenery.DOOR_4250, IntType.SCENERY, "open") { _, node ->
            when (node.direction) {
                Direction.NORTH -> {
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH_WEST, node.location)
                }
                Direction.EAST -> {
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH, node.location)
                }
                else -> {
                    replaceScenery(node.asScenery(), node.id + 1, -1, node.direction, node.location)
                }
            }
            return@on true
        }
    }
}
