package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DonalDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "What do you want?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.THINKING, "Just wondering if you were still here.").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "Of course I'm still here.").also { stage++ }
            2 -> npc(FaceAnim.OLD_DISTRESSED, "I'm not going near that crack in the wall again.").also { stage++ }
            3 ->
                npc(
                    FaceAnim.OLD_DISTRESSED,
                    "Rock falls and so on are fine, ",
                    "but sea monsters in caves - never!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DonalDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONAL_3938)
    }
}
