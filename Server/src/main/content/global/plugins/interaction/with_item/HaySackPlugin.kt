package content.global.plugins.interaction.with_item

import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class HaySackPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles using hay sack on bronze spear.
         */

        onUseWith(IntType.ITEM, Items.HAY_SACK_6057, Items.BRONZE_SPEAR_1237) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendMessage(player, "You stick the bronze spear through the hay sack.")
                replaceSlot(player, used.asItem().slot, Item(Items.HAY_SACK_6058))
            }
            return@onUseWith true
        }
    }
}
