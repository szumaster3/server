package content.region.asgarnia.dialogue.falador

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FaladorWomenParkDialogue(
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
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Greetings! Have you come to gaze in rapture at the",
                    "natural beauty of Falador's parkland?",
                ).also {
                    stage++
                }
            1 -> player(FaceAnim.HALF_GUILTY, "Um, yes, very nice. Lots of.... trees and stuff.").also { stage++ }
            2 -> npc(FaceAnim.HALF_GUILTY, "Trees! I do so love trees! And flowers! And squirrels.").also { stage++ }
            3 -> player(FaceAnim.HALF_GUILTY, "Sorry, I have a strange urge to be somewhere else.").also { stage++ }
            4 -> npc(FaceAnim.HALF_GUILTY, "Come back to me soon and we can talk again about trees!").also { stage++ }
            5 -> player(FaceAnim.HALF_GUILTY, "...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WOMAN_3226)
    }
}
