package content.region.karamja.dialogue.brimhaven

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BrimhavenBartenderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Yohoho me hearty what would you like to drink?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Nothing, thank you.", "A pint of Grog please.", "A bottle of rum please.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Nothing, thank you.").also { stage = END_DIALOGUE }
                    2 -> player(FaceAnim.HAPPY, "A pint of Grog please..").also { stage++ }
                    3 -> player(FaceAnim.HAPPY, "A bottle of rum please.").also { stage = 4 }
                }

            2 -> npc(FaceAnim.FRIENDLY, "One grog coming right up, that'll be three coins.").also { stage++ }
            3 -> {
                if (!removeItem(player, Item(Items.COINS_995, 3))) {
                    end()
                    player("Sorry, I don't seem to have enough coins.")
                } else {
                    end()
                    addItem(player, Items.GROG_1915)
                    sendMessage(player, "You buy a pint of Grog.")
                }
            }

            4 -> npc(FaceAnim.FRIENDLY, "That'll be 27 coins.").also { stage++ }
            5 -> {
                if (!removeItem(player, Item(Items.COINS_995, 27))) {
                    end()
                    player("Sorry, I don't seem to have enough coins.")
                } else {
                    end()
                    addItem(player, Items.RUM_8940)
                    sendMessage(player, "You buy a bottle of rum.")
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BrimhavenBartenderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_735)
    }
}
