package content.region.asgarnia.dialogue.portsarim

import core.api.*
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
class PortSarimBartenderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Good day to you!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HAPPY, "Hello there!").also { stage++ }
            1 ->
                sendDialogueOptions(
                    player,
                    "Choose an option:",
                    "Could I buy a beer, please.",
                    "Bye, then.",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "Could I buy a beer, please?").also { stage++ }
                    2 -> {
                        player(FaceAnim.FRIENDLY, "Bye, then.").also { stage = 6 }
                        stage = 20
                    }
                }

            3 -> npc(FaceAnim.FRIENDLY, "Sure, that will be two gold coins, please.").also { stage++ }
            4 -> player(FaceAnim.FRIENDLY, "Okay, here you go.").also { stage++ }
            5 -> {
                end()
                if (!removeItem(player, Item(Items.COINS_995, 2))) {
                    sendMessage(player, "You need 2 gold coins to buy beer.")
                } else {
                    sendDialogue(player, "You buy a pint of beer.")
                    addItemOrDrop(player, Items.BEER_1917, 1)
                }
            }

            6 -> npc(FaceAnim.FRIENDLY, "Come back soon!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_734)
    }
}
