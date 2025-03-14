package content.global.dialogue

import core.ServerConstants
import core.api.openInterface
import core.api.sendDialogue
import core.api.sendItemDialogue
import core.api.sendString
import core.game.dialogue.DialogueFile
import core.game.node.item.Item
import core.tools.START_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items

class BankHelpDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            START_DIALOGUE -> options("Check Bank Value", "Banking Assistance", "Close").also { stage++ }
            1 ->
                when (buttonID) {
                    1 ->
                        player?.let { it ->
                            end()
                            val wealth = it.bank.wealth
                            if (wealth > 0) {
                                val word = if (wealth != 1) "coins" else "coin"
                                val itemMap =
                                    mapOf(
                                        2 to Items.COINS_8890,
                                        3 to Items.COINS_8891,
                                        4 to Items.COINS_8892,
                                        5 to Items.COINS_8893,
                                        25 to Items.COINS_8894,
                                        100 to Items.COINS_8895,
                                        250 to Items.COINS_8896,
                                        1000 to Items.COINS_8897,
                                        10000 to Items.COINS_8898,
                                        Int.MAX_VALUE to Items.COINS_8899,
                                    )
                                return sendItemDialogue(
                                    it,
                                    itemMap.entries.first { wealth < it.key }.let { Item(it.value, wealth) },
                                    "Your bank is worth <col=a52929>$wealth</col> $word.",
                                )
                            } else {
                                sendDialogue(it, "You have no valuables in your bank.")
                            }
                        }

                    2 ->
                        player?.let {
                            end()
                            it.bank.close()
                            openInterface(it, Components.BANK_V2_HELP_767).also {
                                if (player!!.interfaceManager.isOpened) {
                                    sendString(
                                        player!!,
                                        "Bank of ${ServerConstants.SERVER_NAME} - Help",
                                        Components.BANK_V2_HELP_767,
                                        40,
                                    )
                                }
                            }
                        }

                    3 -> end()
                }
        }
    }
}
