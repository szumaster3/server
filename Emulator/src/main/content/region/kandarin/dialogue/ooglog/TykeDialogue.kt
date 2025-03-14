package content.region.kandarin.dialogue.ooglog

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TykeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.CHILD_NORMAL, "Hi, human.").also { stage++ }
            1 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Hey, human. What did you bring me?").also { stage++ }
            3 ->
                playerl(
                    FaceAnim.THINKING,
                    "Hmm, let me think carefully about this. Oh, yes. I remember, now! Absolutely nothing.",
                ).also {
                    stage++
                }
            4 -> npcl(FaceAnim.CHILD_NORMAL, "Aw, shucks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TykeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TYKE_7068)
    }
}
