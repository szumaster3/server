package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DerrikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Good day, Sir. Can I help you with anything?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Can I use your anvil?", "Nothing, thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Can I use your anvil?").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "Nothing, thanks.").also { stage = END_DIALOGUE }
                }

            2 -> npc(FaceAnim.NEUTRAL, "You may.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DerrikDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DERRIK_1376)
    }
}
