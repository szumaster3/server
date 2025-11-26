package content.global.plugins.interaction.with_item

import core.api.addItemOrDrop
import core.api.removeItem
import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

class SeasonedSardinePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating the doogle sardine (Gertrude's cat quest).
         */

        onUseWith(IntType.ITEM, Items.RAW_SARDINE_327, Items.DOOGLE_LEAVES_1573) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendDialogue(player, "You rub the doogle leaves over the sardine.")
                addItemOrDrop(player, Items.DOOGLE_SARDINE_1552, 1)
            }
            return@onUseWith true
        }
    }
}
