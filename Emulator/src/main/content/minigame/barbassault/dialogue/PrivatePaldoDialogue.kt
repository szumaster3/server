package content.minigame.barbassault.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PrivatePaldoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hi, soldier.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "Why hello there! Have you heard about Primalmoose?").also { stage++ }
            1 -> playerl(FaceAnim.HALF_GUILTY, "What's a primal moose?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "A barbarian! Word is he's been smashing through waves of Penance faster than anyone we've ever seen.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "That's why we've decided to battle harder set of waves to challenge the veterans.",
                ).also { stage++ }

            4 -> playerl(FaceAnim.HALF_GUILTY, "Well, I suppose that's a good idea.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "You should try them yourself. Put your skills to the test against the Penance King!",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PRIVATE_PALDO_5031)
    }
}
