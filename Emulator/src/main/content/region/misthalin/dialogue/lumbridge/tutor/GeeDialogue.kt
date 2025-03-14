package content.region.misthalin.dialogue.lumbridge.tutor

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GeeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello there, can I help you?").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What's up?", "Are there any quests I can do here?", "Can I buy your stick?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What's up?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Do you know of any quests I can do?").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Can I buy your stick?").also { stage = 30 }
                }
            10 -> npc(FaceAnim.HALF_GUILTY, "I assume the sky is up..").also { stage++ }
            11 -> npc(FaceAnim.HALF_GUILTY, "You assume?").also { stage++ }
            12 ->
                npc(FaceAnim.HALF_GUILTY, "Yeah, unfortunately I don't seem to be able to look up.").also {
                    stage =
                        END_DIALOGUE
                }
            20 -> npc(FaceAnim.HALF_GUILTY, "Nope, sorry.").also { stage = END_DIALOGUE }
            30 ->
                npc(
                    FaceAnim.FURIOUS,
                    "It's not a stick! I'll have you know it's a very powerful",
                    "staff!",
                ).also { stage++ }
            31 -> player(FaceAnim.HALF_GUILTY, "Really? Show me what it can do!").also { stage++ }
            32 -> npc(FaceAnim.HALF_GUILTY, "Um..It's a bit low on power at the moment..").also { stage++ }
            33 -> player(FaceAnim.HALF_GUILTY, "It's a stick isn't it?").also { stage++ }
            34 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "...Ok it's a stick.. But only while I save up for a staff.",
                    "Zaff in Varrock square sells them in his shop.",
                ).also {
                    stage++
                }
            35 -> player(FaceAnim.HALF_GUILTY, "Well good luck with that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GEE_2237)
    }
}
