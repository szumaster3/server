package content.global.handlers.item.withitem

import core.api.addItem
import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class OilyFishingRodListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.BLAMISH_OIL_1582, Items.FISHING_ROD_307) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                replaceSlot(player, with.asItem().slot, Item(Items.OILY_FISHING_ROD_1585, 1))
                addItem(player, Items.VIAL_229)
                sendMessage(player, "You rub the oil into the fishing rod.")
            }
            return@onUseWith true
        }
    }
}
