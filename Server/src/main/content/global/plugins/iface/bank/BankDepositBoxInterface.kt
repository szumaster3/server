package content.global.plugins.iface.bank

import core.api.*
import core.game.component.Component
import core.game.dialogue.InputType
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import shared.consts.Animations
import shared.consts.Components

class BankDepositBoxInterface : InterfaceListener {
    companion object {
        private const val BUTTON_DEPOSIT_BOB = 13
        private const val OP_AMOUNT_ONE = 155
        private const val OP_AMOUNT_FIVE = 196
        private const val OP_AMOUNT_TEN = 124
        private const val OP_AMOUNT_ALL = 199
        private const val OP_AMOUNT_X = 234
        private const val OP_EXAMINE = 9

        private fun transferX(player: Player, slot: Int, withdraw: Boolean, after: (() -> Unit)? = null) {
            sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                val number = Integer.parseInt(value.toString())

                if (withdraw) {
                    player.bank.takeItem(slot, number)
                } else {
                    player.bank.addItem(slot, number)
                }

                player.bank.updateLastAmountX(number)
                after?.let { it() }
            }
        }
    }

    private fun handleDepositBoxMenu(player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int): Boolean {
        val item = player.inventory.get(slot) ?: return true

        if (opcode == OP_EXAMINE) {
            sendMessage(player, item.definition.examine)
            return true
        }

        runWorldTask {
            when (opcode) {
                OP_AMOUNT_ONE -> player.bank.addItem(slot, 1)
                OP_AMOUNT_FIVE -> player.bank.addItem(slot, 5)
                OP_AMOUNT_TEN -> player.bank.addItem(slot, 10)
                OP_AMOUNT_X -> transferX(player, slot, false, player.bank::refreshDepositBoxInterface)
                OP_AMOUNT_ALL -> player.bank.addItem(slot, player.inventory.getAmount(item))
                else -> player.debug("Unknown deposit box menu opcode $opcode")
            }
            player.bank.refreshDepositBoxInterface()
            animate(player, Animations.HUMAN_BANK_DEPOSIT_BOX_834)
        }

        return true
    }

    override fun defineInterfaceListeners() {
        on(Components.BANK_DEPOSIT_BOX_11, ::handleDepositBoxMenu)
        on(Components.BANK_DEPOSIT_BOX_11, BUTTON_DEPOSIT_BOB) { player, _, _, _, _, _ ->
            dumpBeastOfBurden(player)
            return@on true
        }
    }
}
