package content.global.skill.herblore

import core.api.addItemOrDrop
import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class CoconutPlugin : InteractionListener {

    override fun defineListeners() {

        onUseWith(IntType.ITEM, Items.COCONUT_5974, Items.HAMMER_2347) { player, used, _ ->
            val itemSlot = used.asItem().slot
            if (removeItem(player, Item(used.id, 1))) {
                replaceSlot(player, itemSlot, Item(Items.COCONUT_SHELL_5978, 1))
                sendMessage(player, "You crush the coconut with a hammer.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.COCONUT_5976, Items.VIAL_229) { player, used, with ->
            val itemSlot = with.asItem().slot
            if (removeItem(player, Item(used.id, 1))) {
                replaceSlot(player, itemSlot, Item(Items.COCONUT_MILK_5935, 1))
                addItemOrDrop(player, Items.COCONUT_SHELL_5978, 1)
                sendMessage(player, "You overturn the coconut into a vial.")
            }
            return@onUseWith true
        }
    }
}
