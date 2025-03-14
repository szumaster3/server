package content.region.misthalin.quest.fluffs.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PhilopDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.HALF_GUILTY, "Hello, what's your name?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.CHILD_NORMAL, "Gwwrr!").also { stage++ }
            1 -> playerl(FaceAnim.HALF_GUILTY, "Err, hello there. What's that you have there?").also { stage++ }
            2 -> npc(FaceAnim.CHILD_NORMAL, "Gwwwrrr! Dwa-gon Gwwwwrrrr!").also { stage++ }
            3 -> playerl(FaceAnim.CHILD_NORMAL, "Enjoy playing with your dragon, then.").also { stage++ }
            4 -> npc(FaceAnim.CHILD_NORMAL, "Gwwwrrr!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return PhilopDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PHILOP_782)
    }
}
