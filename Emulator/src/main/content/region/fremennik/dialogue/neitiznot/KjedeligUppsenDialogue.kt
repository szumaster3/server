package content.region.fremennik.dialogue.neitiznot

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class KjedeligUppsenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.NEUTRAL, "I'm guarding the king, I cannot speak.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.NEUTRAL, "Sorry.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KjedeligUppsenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KJEDELIG_UPPSEN_5518)
    }
}
