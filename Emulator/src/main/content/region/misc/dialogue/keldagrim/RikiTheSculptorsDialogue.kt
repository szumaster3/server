package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RikiTheSculptorsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "I'm glad I don't have to talk to you anymore!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Hrm.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RikiTheSculptorsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.RIKI_THE_SCULPTORS_MODEL_2143,
            NPCs.RIKI_THE_SCULPTORS_MODEL_2144,
            NPCs.RIKI_THE_SCULPTORS_MODEL_2145,
            NPCs.RIKI_THE_SCULPTORS_MODEL_2146,
            NPCs.RIKI_THE_SCULPTORS_MODEL_2147,
            NPCs.RIKI_THE_SCULPTORS_MODEL_2148,
            NPCs.RIKI_THE_SCULPTORS_MODEL_2149,
            NPCs.RIKI_THE_SCULPTORS_MODEL_2150,
        )
    }
}
