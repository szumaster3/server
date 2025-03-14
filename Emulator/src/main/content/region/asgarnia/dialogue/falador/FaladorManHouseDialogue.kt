package content.region.asgarnia.dialogue.falador

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FaladorManHouseDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "What are you doing in my house?").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "I was just exploring.").also { stage++ }
            2 -> npc(FaceAnim.HALF_GUILTY, "You're exploring my house?").also { stage++ }
            3 -> player(FaceAnim.HALF_GUILTY, "You don't mind, do you?").also { stage++ }
            4 -> npc(FaceAnim.HALF_GUILTY, "But... why are you exploring in my house?").also { stage++ }
            5 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Oh, I don't know. I just wandered in, saw you and thought",
                    "it'd be fun to speak to you.",
                ).also {
                    stage++
                }
            6 -> npc(FaceAnim.HALF_GUILTY, "... you are very strange...").also { stage++ }
            7 -> player(FaceAnim.HALF_GUILTY, "Perhaps I should go now.").also { stage++ }
            8 -> npc(FaceAnim.HALF_GUILTY, "Yes, please go away now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAN_3223)
    }
}
