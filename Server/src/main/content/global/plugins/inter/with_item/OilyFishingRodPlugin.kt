package content.global.plugins.inter.with_item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class OilyFishingRodPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating oily fishing rod.
         */

        onUseWith(IntType.ITEM, Items.BLAMISH_OIL_1582, Items.FISHING_ROD_307) { player, used, with ->
            val oil = used.asItem()
            val rod = with.asItem()

            if (!inInventory(player, oil.id) || !inInventory(player, rod.id)) {
                sendMessage(player, "You need both the oil and the fishing rod to do this.")
                return@onUseWith true
            }

            val rodSlot = rod.slot
            val oilSlot = oil.slot

            if (removeItem(player, oil) && removeItem(player, rod)) {
                replaceSlot(player, rodSlot, Item(Items.OILY_FISHING_ROD_1585, 1))
                replaceSlot(player, oilSlot, Item(Items.VIAL_229, 1))
                sendMessage(player, "You rub the oil into the fishing rod.")
            }

            return@onUseWith true
        }
    }
}
