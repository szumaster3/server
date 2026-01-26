package content.global.plugins.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class CapAndGogglesOptionPlugin : InteractionListener {

    companion object {
        private const val capAndGoggles = Items.CAP_AND_GOGGLES_9946
        private const val bomberCap = Items.BOMBER_CAP_9945
        private const val gnomeGoggles = Items.GNOME_GOGGLES_9472
    }

    override fun defineListeners() {

        /*
         * Handles split.
         */

        on(capAndGoggles, IntType.ITEM, "split") { player, node ->
            if (freeSlots(player) < 2) {
                sendDialogue(player, "You don't have enough inventory space for that.")
                return@on true
            }

            val item = node.asItem() ?: return@on true
            val slot = item.slot

            if (removeItem(player, item)) {
                replaceSlot(player, slot, Item(bomberCap, 1))
                addItem(player, gnomeGoggles, 1)
            }
            return@on true
        }
    }
}
