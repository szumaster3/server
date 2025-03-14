package content.region.fremennik.dialogue.jatizso

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class EricDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "Spare us a few coppers mister")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        playerl(FaceAnim.ANGRY, "NO!").also { stage = END_DIALOGUE }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return EricDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ERIC_5499)
    }
}
