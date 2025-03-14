package content.region.kandarin.dialogue.ooglog

import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

class OgressBankerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.CHILD_NORMAL, "...").also { stage++ }
            1 -> playerl(FaceAnim.NEUTRAL, "Excuse me, can I get some service here, please?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.CHILD_EVIL_LAUGH,
                    "GRAAAAAH! You go away, human! Me too busy with training to talk to puny thing like you.",
                ).also {
                    stage++
                }
            3 ->
                sendNPCDialogue(
                    player,
                    NPCs.BALNEA_7047,
                    "I do apologise, sir. We're temporarily unable to meet your banking needs.",
                    FaceAnim.FRIENDLY,
                ).also {
                    stage++
                }
            4 ->
                sendNPCDialogue(
                    player,
                    NPCs.BALNEA_7047,
                    "We'll be open as soon as we realize our customer experience goals " +
                        "and can guarantee the high standards of service that you expect from all " +
                        "branches of the Bank of Gielinor.",
                    FaceAnim.FRIENDLY,
                ).also { stage++ }
            5 -> playerl(FaceAnim.THINKING, "What did you just say to me?").also { stage++ }
            6 ->
                sendNPCDialogue(
                    player,
                    NPCs.BALNEA_7047,
                    "We're closed until I can teach these wretched creatures some manners.",
                    FaceAnim.FRIENDLY,
                ).also {
                    stage++
                }
            7 -> playerl(FaceAnim.NEUTRAL, "Ah, right. Good luck with that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OgressBankerDialogue(player)
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.OGRESS_BANKER_7049,
            NPCs.OGRESS_BANKER_7050,
        )
}
