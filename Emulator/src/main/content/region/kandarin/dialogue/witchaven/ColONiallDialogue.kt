package content.region.kandarin.dialogue.witchaven

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ColONiallDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Hello. What can I do for you?").also { stage++ }
            1 -> player(FaceAnim.FRIENDLY, "Oh, I'm just wondering what you're doing.").also { stage++ }
            2 -> npc(FaceAnim.FRIENDLY, "A spot of fishing.").also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "That doesn't look much like a fishing rod.").also { stage++ }
            4 -> npc(FaceAnim.FRIENDLY, "That my friend, depends on what you're fishing for.").also { stage++ }
            5 -> player(FaceAnim.FRIENDLY, "And what would that be?").also { stage++ }
            6 -> npcl(FaceAnim.FRIENDLY, "A little of this, a little of that; the usual things.").also { stage++ }
            7 -> player(FaceAnim.FRIENDLY, "Have you caught much?").also { stage++ }
            8 -> npc(FaceAnim.FRIENDLY, "The odd bite here and there. Hmm.").also { stage++ }
            9 -> player(FaceAnim.FRIENDLY, "What?").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You look like a capable lad. Tell you what, when you've got a bit more experience under your belt, get yourself over to Falador.",
                ).also {
                    stage++
                }
            11 -> player(FaceAnim.FRIENDLY, "What's in Falador?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "An old friend of mine. You'll find him sitting on a bench in Falador Park. See what he can do for you.",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.FRIENDLY, "What's his name? Who should I say sent me?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "None of that matters if you can find him and if you're ready my name isn't necessary.",
                ).also {
                    stage++
                }
            15 -> player(FaceAnim.FRIENDLY, "Oh right. I'll get going then. Goodbye.").also { stage++ }
            16 -> npc(FaceAnim.FRIENDLY, "Goodbye and good luck.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ColONiallDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COL_ONIALL_4872)
    }
}
