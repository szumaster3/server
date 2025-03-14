package content.global.handlers.item.withitem

import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class HaySackListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.HAY_SACK_6057, Items.BRONZE_SPEAR_1237) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendMessage(player, "You stick the bronze spear through the hay sack.")
                replaceSlot(player, used.asItem().slot, Item(Items.HAY_SACK_6058))
            }
            return@onUseWith true
        }
    }
}
