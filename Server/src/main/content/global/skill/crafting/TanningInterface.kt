package content.global.skill.crafting

import core.api.sendInputDialogue
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import shared.consts.Components

class TanningInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        on(Components.TANNER_324) { player, _, opcode, buttonID, _, _ ->
            val type = TanningProduct.forId(buttonID) ?: return@on true

            when (opcode) {
                124, 199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        TanningProduct.tan(player, value as Int, type)
                    }
                }

                else -> {
                    val amount = when (opcode) {
                        155 -> 1
                        196 -> 5
                        234 -> player.inventory.getAmount(Item(type.item, 1))
                        else -> 0
                    }
                    if (amount > 0) TanningProduct.tan(player, amount, type)
                }
            }

            return@on true
        }
    }
}