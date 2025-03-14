package content.region.misthalin.dialogue.varrock

import content.global.handlers.iface.ge.ExchangeItemSets
import content.global.handlers.iface.ge.StockMarket
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.ge.ExchangeHistory.Companion.getInstance
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GrandExchangeClerkDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        player(FaceAnim.HALF_GUILTY, "Hi there.")
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> npc("Good day to you, sir, How can I help?").also { stage++ }
            2 ->
                options(
                    "I want to access the Grand Exchange, please.",
                    "I want to collect my items.",
                    "Can I see a history of my offers?",
                    "Can you help me with item sets?",
                    "I'm fine, actually.",
                ).also { stage++ }

            3 ->
                when (buttonId) {
                    1 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "I want to access the Grand Exchange, please.",
                        ).also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "I want to collect my items.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Can I see history of my offers?").also { stage = 30 }
                    4 -> playerl(FaceAnim.HALF_GUILTY, "Can you help me with item sets?").also { stage = 40 }
                    5 -> player(FaceAnim.HALF_GUILTY, "I'm fine actually.").also { stage = 50 }
                }

            10 -> npc(FaceAnim.HALF_GUILTY, "Only too happy to help you, sir.").also { stage++ }
            11 -> {
                end()
                StockMarket.openFor(player!!)
            }

            20 -> npc(FaceAnim.HALF_GUILTY, "As you wish, sir.").also { stage++ }
            21 -> {
                end()
                getInstance(player).openCollectionBox()
            }

            30 -> npc(FaceAnim.HALF_GUILTY, "If that is your wish.").also { stage++ }
            31 -> {
                end()
                getInstance(player).openHistoryLog(player!!)
            }

            40 -> npc(FaceAnim.HALF_GUILTY, "It would be my pleasure, sir.").also { stage++ }
            41 -> {
                end()
                ExchangeItemSets.openFor(player!!)
            }

            50 -> npc(FaceAnim.HALF_GUILTY, "If you say so, sir.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GrandExchangeClerkDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.GRAND_EXCHANGE_CLERK_6528,
            NPCs.GRAND_EXCHANGE_CLERK_6529,
            NPCs.GRAND_EXCHANGE_CLERK_6530,
            NPCs.GRAND_EXCHANGE_CLERK_6531,
        )
    }
}
