package content.region.kandarin.dialogue.ooglog

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SeegudDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.NEUTRAL, "Hello, there! Nice day, isn't it?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Hmph, it an okay day. Not so sure is nice day. Bit sticky, bit hot. Makes my bones itch.",
                ).also {
                    stage++
                }
            2 -> playerl(FaceAnim.NEUTRAL, "Makes your bones...itch? How does that work?").also { stage++ }
            3 -> npcl(FaceAnim.CHILD_NORMAL, "When you get old, you understand.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SeegudDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SEEGUD_7052)
    }
}
