package content.global.handlers.item.withitem

import core.api.removeItem
import core.api.replaceSlot
import core.api.sendItemDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class HangoverCureListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.SNAPE_GRASS_231, Items.CHOCOLATEY_MILK_1977) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendItemDialogue(player, Items.HANGOVER_CURE_1504, "You mix the snape grass into the bucket.")
                replaceSlot(player, with.asItem().slot, Item(Items.HANGOVER_CURE_1504, 1))
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
