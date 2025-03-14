package content.global.handlers.item.withitem

import core.api.removeItem
import core.api.replaceSlot
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class DesertDisguiseListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.KARIDIAN_HEADPIECE_4591, Items.FAKE_BEARD_4593) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                replaceSlot(player, used.asItem().slot, Item(Items.DESERT_DISGUISE_4611, 1))
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
