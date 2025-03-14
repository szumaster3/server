package content.global.skill.herblore

import core.api.addItem
import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class CoconutListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.COCONUT_5974, Items.HAMMER_2347) { player, used, _ ->
            val itemSlot = used.asItem().slot
            if (removeItem(player, used.asItem())) {
                replaceSlot(player, itemSlot, Item(used.id + 2))
                sendMessage(player, "You crush the coconut with a hammer.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.COCONUT_5976, Items.VIAL_229) { player, used, with ->
            val itemSlot = with.asItem().slot
            if (removeItem(player, used.asItem())) {
                replaceSlot(player, itemSlot, Item(Items.COCONUT_MILK_5935))
                addItem(player, used.id + 2)
                sendMessage(player, "You overturn the coconut into a vial.")
            }
            return@onUseWith true
        }
    }
}
