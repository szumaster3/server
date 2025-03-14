package content.region.misthalin.dialogue.edgeville

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HariDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Who are you?", "Can you teach me about canoeing?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> npc(FaceAnim.HALF_GUILTY, "My name is Hari.").also { stage = 10 }
                    2 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It's really quite simple to make. Just walk down to that",
                            "tree on the bank and chop it down.",
                        ).also {
                            stage =
                                18
                        }
                }
            10 -> player(FaceAnim.HALF_GUILTY, "And what are you going here Hari?").also { stage++ }
            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Like most people who come to Edgeville, I am here to seek",
                    "adventure in the Wilderness.",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I found a secret underground river that will take me quite",
                    "a long way north.",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.HALF_GUILTY, "Underground river? Where does it come out?").also { stage++ }
            14 -> npc(FaceAnim.HALF_GUILTY, "It comes out in a pond located deep in Wilderness.").also { stage++ }
            15 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I had to find a very special type of canoe to get me up",
                    "the river though, would you like to know more?",
                ).also {
                    stage++
                }
            16 -> options("Yes", "No").also { stage++ }
            17 ->
                when (buttonId) {
                    1 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It's really quite simple to make. Just walk down to that",
                            "tree on the bank and chop it down.",
                        ).also {
                            stage++
                        }
                    2 -> end()
                }
            18 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When you have done that you can shape the log further",
                    "with your axe to make a canoe.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return HariDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HARI_3330)
    }
}
