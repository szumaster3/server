package content.global.plugins.interaction.with_item

import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class FungicideSprayPlugin : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles reloading the Fungicide spray.
         * It can only be reloaded when fully empty (charge 0).
         */
        val sprayIds = (Items.FUNGICIDE_SPRAY_10_7421..Items.FUNGICIDE_SPRAY_0_7431).toList()

        onUseWith(IntType.ITEM, Items.FUNGICIDE_7432, *sprayIds.toIntArray()) { player, used, with ->
            if (with.id != Items.FUNGICIDE_SPRAY_0_7431) {
                sendMessage(player, "You don't need to reload right now.")
                return@onUseWith true
            }

            if (removeItem(player, used.asItem())) {
                replaceSlot(player, with.asItem().slot, Item(Items.FUNGICIDE_SPRAY_10_7421))
                sendMessage(player, "You refill the fungicide spray.")
                return@onUseWith true
            }

            return@onUseWith false
        }
    }
}
