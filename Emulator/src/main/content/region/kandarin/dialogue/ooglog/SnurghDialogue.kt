package content.region.kandarin.dialogue.ooglog

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SnurghDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Outta de way, human. Dis place not open yet!",
                ).also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "That's not very friendly.").also { stage++ }
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Me said OUTTA DE WAY! You no can sleep here!").also { stage++ }
            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Alright, alright! Keep your hat on! I never said I wanted to.",
                ).also { stage++ }
            4 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SnurghDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SNURGH_7057)
    }
}
