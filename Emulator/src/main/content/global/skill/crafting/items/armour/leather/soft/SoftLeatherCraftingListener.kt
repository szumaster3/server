package content.global.skill.crafting.items.armour.leather.soft

import core.api.amountInInventory
import core.api.sendInputDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items

class SoftLeatherCraftingListener :
    InteractionListener,
    InterfaceListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.LEATHER_1741, Items.NEEDLE_1733) { player, _, _ ->
            SoftLeather.openCraftingInterface(player)
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.LEATHER_CRAFTING_154) { player, _, opcode, buttonID, _, _ ->
            var amount = 0
            val soft = SoftLeather.forButton(buttonID) ?: return@on true
            when (opcode) {
                155 -> amount = 1
                196 -> amount = 5
                124 -> amount = amountInInventory(player, Items.LEATHER_1741)
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        submitIndividualPulse(
                            player,
                            SoftLeatherCraftingPulse(player, Item(Items.LEATHER_1741), soft, value as Int),
                        )
                    }
                    return@on true
                }
            }
            submitIndividualPulse(player, SoftLeatherCraftingPulse(player, null, soft, amount))
            return@on true
        }
    }
}
