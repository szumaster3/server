package content.global.plugins.interaction.with_item

import core.api.removeItem
import core.api.replaceSlot
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class DesertDisguisePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating the Desert Disguise.
         */

        onUseWith(IntType.ITEM, Items.KARIDIAN_HEADPIECE_4591, Items.FAKE_BEARD_4593) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                replaceSlot(player, used.asItem().slot, Item(Items.DESERT_DISGUISE_4611, 1))
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
