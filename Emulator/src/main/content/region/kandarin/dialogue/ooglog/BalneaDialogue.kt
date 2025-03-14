package content.region.kandarin.dialogue.ooglog

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BalneaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi there!").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm ever so busy at the moment; please come back after the grand opening.",
                ).also {
                    stage++
                }
            2 -> playerl(FaceAnim.HALF_ASKING, "What grand reopening?").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "I'm sorry, I really can't spare the time to talk to you.").also { stage++ }
            4 -> playerl(FaceAnim.THINKING, "Uh, sure.").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BALNEA_7047)
    }
}
