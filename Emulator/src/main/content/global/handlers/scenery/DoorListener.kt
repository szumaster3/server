package content.global.handlers.scenery

import core.api.replaceScenery
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class DoorListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.DOOR_1531, IntType.SCENERY, "close") { player, node ->
            if (node.location == Location.create(2598, 3404, 0)) {
                sendMessage(player, "The door appears to be stuck.")
                return@on true
            }
            DoorActionHandler.handleDoor(player, node.asScenery())
            return@on true
        }

        on(Scenery.DOOR_4250, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), node.id + 1, -1, node.direction, node.location)
            return@on true
        }
    }
}
