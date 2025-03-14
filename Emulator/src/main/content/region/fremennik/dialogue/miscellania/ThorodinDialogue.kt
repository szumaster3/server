package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ThorodinDialogue(
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
                    "We're extending the cave so more people can live in it.",
                    "These Miscellanians aren't so bad.",
                    "They appreciate the benefits of living underground.",
                ).also {
                    stage++
                }
            3 -> player(FaceAnim.ASKING, "...such as?").also { stage++ }
            4 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Not getting rained on, for example.",
                    "Did you do anything about that monster Donal",
                    "was talking about?",
                ).also {
                    stage++
                }
            5 -> player(FaceAnim.FRIENDLY, "It's been taken care of.").also { stage++ }
            6 ->
                npc(FaceAnim.OLD_HAPPY, "Glad to hear it.", "Now we can get on with excavating.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ThorodinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THORODIN_3936)
    }
}
