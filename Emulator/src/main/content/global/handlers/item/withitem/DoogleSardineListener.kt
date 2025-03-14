package content.global.handlers.item.withitem

import core.api.addItemOrDrop
import core.api.removeItem
import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class DoogleSardineListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.RAW_SARDINE_327, Items.DOOGLE_LEAVES_1573) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendDialogue(player, "You rub the doogle leaves over the sardine.")
                addItemOrDrop(player, Items.DOOGLE_SARDINE_1552)
            }
            return@onUseWith true
        }
    }
}
