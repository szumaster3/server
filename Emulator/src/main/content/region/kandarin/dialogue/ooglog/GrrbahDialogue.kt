package content.region.kandarin.dialogue.ooglog

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GrrbahDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.CHILD_NORMAL, "Hi, human.").also { stage++ }
            1 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
            2 -> npcl(FaceAnim.CHILD_NORMAL, "How does it feel to be so puny wee small, human?").also { stage++ }
            3 -> playerl(FaceAnim.THINKING, "Oh, I dunno. How does it feel to be so incredibly dense?").also { stage++ }
            4 -> npcl(FaceAnim.CHILD_NORMAL, "Uhh... what dat s'pposed to mean?").also { stage++ }
            5 -> playerl(FaceAnim.NEUTRAL, "Never mind.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GrrbahDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRRBAH_7073)
    }
}
