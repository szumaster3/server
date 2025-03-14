package content.region.asgarnia.handlers

import content.region.asgarnia.dialogue.rimmington.CustomsSergeantDialogue
import core.api.openDialogue
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class RimmingtonListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CUSTOMS_SERGEANT_31459, IntType.SCENERY, "talk-to") { player, _ ->
            if (player.location.x >= 2963) {
                openDialogue(player, CustomsSergeantDialogue())
            }
            return@on true
        }

        on(Scenery.DOOR_1534, IntType.SCENERY, "close", "open") { player, node ->
            if (node.location.x == 2950 && node.location.y == 3207) {
                sendMessage(player, "The doors appear to be stuck.")
                return@on false
            }
            DoorActionHandler.handleDoor(player, node.asScenery().wrapper)
            return@on true
        }
    }
}
