package content.region.kandarin.baxtorian

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.Scenery

class OttoGrottoListeners : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles search the bed in Otto's cabin to find a barbarian rod.
         */

        on(Scenery.BARBARIAN_BED_25268, IntType.SCENERY, "search") { player, _ ->
            if (
                getAttribute(player, BarbarianTraining.FISHING_START, false) &&
                !inInventory(player, Items.BARBARIAN_ROD_11323) &&
                freeSlots(player) > 0
            ) {
                sendMessage(player, "You find a heavy fishing rod under the bed and take it.")
                addItem(player, Items.BARBARIAN_ROD_11323, 1)
            } else {
                sendMessage(player, "You don't find anything that interests you.")
            }
            return@on true
        }
    }
}
