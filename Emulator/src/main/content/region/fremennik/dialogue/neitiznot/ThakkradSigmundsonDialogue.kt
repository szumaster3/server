package content.region.fremennik.dialogue.neitiznot

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ThakkradSigmundsonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npcl(FaceAnim.FRIENDLY, "Greetings! I can cure your Yak Hides if you'd like!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Good to know!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ThakkradSigmundsonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THAKKRAD_SIGMUNDSON_5506)
    }
}
