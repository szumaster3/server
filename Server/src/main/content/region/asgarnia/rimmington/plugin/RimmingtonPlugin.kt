package content.region.asgarnia.rimmington.plugin

import content.region.asgarnia.rimmington.dialogue.CustomsSergeantDialogue
import core.api.openDialogue
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

class RimmingtonPlugin : InteractionListener {

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

        /*
         * on(Scenery.WARDROBE_33963,IntType.SCENERY, "open") { _, node ->
         *     val rand = (Scenery.WARDROBE_35133..Scenery.WARDROBE_35135).random()
         *     replaceScenery(node.asScenery(), node.id, rand)
         *     return@on true
         * }
         */
    }
}
