package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FullangrDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "Good day, sir.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What are you doing down here?", "Good day.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "What are you doing down here?").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "Good day.").also { stage = END_DIALOGUE }
                }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "I'm working on the digging, of course.",
                    "It's a small excavation, so only two of us ",
                    "can work on it at a time.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FullangrDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FULLANGR_3934)
    }
}
