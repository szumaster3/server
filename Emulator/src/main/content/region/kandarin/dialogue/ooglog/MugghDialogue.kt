package content.region.kandarin.dialogue.ooglog

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MugghDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.CHILD_FRIENDLY, "Hey, what you doing here? We not open yet.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Just having a nosey, really.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You bring dat nose back here when we open for business. I fix you up good.",
                ).also {
                    stage++
                }
            2 -> playerl(FaceAnim.FRIENDLY, "Fix me up?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Yeah, me give you facial. Try to make your ugly face look bit nicer.",
                ).also {
                    stage++
                }
            4 -> playerl(FaceAnim.FRIENDLY, "Charming.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return MugghDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MUGGH_7062)
    }
}
