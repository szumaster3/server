package content.region.desert.dialogue.pollnivneach

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BlackJackSellerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npcl(FaceAnim.HALF_GUILTY, "Hello. Could I interest you in a blackjack?")
        return true
    }

    override fun handle(
        intefaceId: Int,
        objectId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Ah, it's you again Player. Tell me how are the blackjack sales going?",
                ).also {
                    stage++
                }
            1 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Not too bad, Ali was well impressed by the reaction and demand they had initially but he's a little concerned about how the sales have dropped off of recent.",
                ).also {
                    stage++
                }
            2 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Maybe you could produce a different range of products?",
                ).also { stage++ }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I'm not too sure, I'm quite happy to stick with what I've got unless, I had a good reason to change.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BlackJackSellerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLACKJACK_SELLER_2548)
    }
}
