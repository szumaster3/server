package content.global.handlers.item.withitem

import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.api.toIntArray
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class FungicideSprayListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(
            IntType.ITEM,
            Items.FUNGICIDE_7432,
            *(Items.FUNGICIDE_SPRAY_10_7421..Items.FUNGICIDE_SPRAY_0_7431).toIntArray(),
        ) { player, used, with ->
            if (with.id in Items.FUNGICIDE_SPRAY_10_7421..Items.FUNGICIDE_SPRAY_1_7430) {
                sendMessage(player, "You don't need reload right now.")
                return@onUseWith false
            }
            if (removeItem(player, used.asItem())) {
                replaceSlot(player, with.asItem().slot, Item(Items.FUNGICIDE_SPRAY_10_7421))
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
