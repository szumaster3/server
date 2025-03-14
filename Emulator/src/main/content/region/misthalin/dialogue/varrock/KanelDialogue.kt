package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class KanelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.CHILD_SUSPICIOUS, "Hel-lo?").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Right. Goodbye.").also { stage++ }
            2 -> npc(FaceAnim.CHILD_NORMAL, "Bye?").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return KanelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KANEL_784)
    }
}
