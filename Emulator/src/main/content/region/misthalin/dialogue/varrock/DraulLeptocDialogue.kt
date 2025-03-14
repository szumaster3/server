package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DraulLeptocDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HALF_GUILTY,
            "What are you doing in my house..why the",
            "impertinence...the sheer cheek...how dare you violate my",
            "personal lodgings....",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "I...I was just looking around...").also { stage++ }
            1 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well get out! Get out....this is my house....and don't go",
                    "near my daughter Juliet...she's grounded in her room",
                    "to keep her away from that good for nothing Romeo.",
                ).also { stage++ }

            2 -> player(FaceAnim.HALF_GUILTY, "Yes....sir....").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DraulLeptocDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DRAUL_LEPTOC_3324)
    }
}
