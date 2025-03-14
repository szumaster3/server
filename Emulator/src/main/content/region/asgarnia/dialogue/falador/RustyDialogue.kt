package content.region.asgarnia.dialogue.falador

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RustyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hiya. Are you carrying anything valuable?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Why are you asking?").also { stage++ }
            1 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Um... It's a quiz. I'm asking everyone I meet if they're",
                    "carrying anything valuable.",
                ).also {
                    stage++
                }
            2 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "What would you do if I said I had loads of expensive items",
                    "with me?",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Ooh, do you? It's been ages since anyone said they'd got",
                    "anything worth stealing.",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.HALF_GUILTY, "'Anything worth stealing'?").also { stage++ }
            5 -> npc(FaceAnim.HALF_GUILTY, "Um... Not that I'd dream of stealing anything!").also { stage++ }
            6 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Well, I'll say I'm not carrying anything valuable at all.",
                ).also { stage++ }
            7 -> npc(FaceAnim.HALF_GUILTY, "Oh, what a shame.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RUSTY_3239)
    }
}
