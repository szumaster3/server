package content.region.misthalin.dialogue.lumbridge.tutor

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DonieDialogue(
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
            0 -> options("Where am I?", "How are you today?", "Your shoe lace is untied.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Where am I?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "How are you today?").also { stage = 4 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Your shoe lace is untied.").also { stage = 10 }
                }
            2 -> npc(FaceAnim.HALF_GUILTY, "This is the town of Lumbridge my friend.").also { stage++ }
            3 -> options("Where am I?", "How are you today?", "Your shoe lace is untied.").also { stage = 1 }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Aye, not too bad thank you. Lovely weather in",
                    settings!!.name + " this fine day.",
                ).also { stage++ }
            5 -> player(FaceAnim.HALF_GUILTY, "Weather?").also { stage++ }
            6 -> npc(FaceAnim.HALF_GUILTY, "Yes weather, you know.").also { stage++ }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The state or condition of the atmosphere at a time and",
                    "place, with respect to variables such as temperature,",
                    "moisture, wind velocity, and barometric pressure.",
                ).also {
                    stage++
                }
            8 -> player(FaceAnim.HALF_GUILTY, "...").also { stage++ }
            9 -> npc(FaceAnim.HALF_GUILTY, "Not just a pretty face eh? Ha ha ha.").also { stage = END_DIALOGUE }
            10 -> npc(FaceAnim.HALF_GUILTY, "No it's not!").also { stage++ }
            11 -> player(FaceAnim.HALF_GUILTY, "No you're right. I have nothing to back that up.").also { stage++ }
            12 -> npc(FaceAnim.HALF_GUILTY, "Fool! Leave me alone!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DonieDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONIE_2238)
    }
}
