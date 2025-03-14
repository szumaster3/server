package content.region.karamja.dialogue.brimhaven

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LubufuDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Watch where you're going, young whippersnapper!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("I wasn't going anywhere...", "What's a whippersnapper?", "Who are you?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I wasn't going anywhere...").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "What's a whippersnapper?").also { stage = 3 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Who are you?").also { stage = 4 }
                }
            2 -> npc(FaceAnim.HALF_GUILTY, "Well then go away from here!").also { stage = END_DIALOGUE }
            3 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It's a whip. Which snaps. Like me. Now leave!",
                ).also { stage = END_DIALOGUE }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I am Lubufu - the only fisherman who knows the secret",
                    "of the Karambwan!",
                ).also {
                    stage++
                }
            5 -> player(FaceAnim.HALF_GUILTY, "What's a Karambwan?").also { stage++ }
            6 -> npc(FaceAnim.HALF_GUILTY, "What a foolish question! Now leave!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LubufuDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUBUFU_1171)
    }
}
