package content.region.kandarin.dialogue.ooglog

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SnarrlDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.CHILD_NORMAL, "Hi, human.").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "Hi, ogre.").also { stage++ }
            2 -> options("How are you today?", "Can you tell me about this salt-water spring?").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "How are you today, little ogre?").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Can you tell me about this copper-coloured pool?",
                        ).also { stage = 8 }
                }
            4 -> npcl(FaceAnim.CHILD_NORMAL, "Me wanna go visit Fycie 'n Bugs!").also { stage++ }
            5 -> playerl(FaceAnim.HALF_ASKING, "Yes, they're both delightful individuals.").also { stage++ }
            6 -> npcl(FaceAnim.CHILD_NORMAL, "Will you take me to see dem, human?").also { stage++ }
            7 ->
                playerl(FaceAnim.HALF_ASKING, "Didn't your mother ever teach you not to talk to strangers?").also {
                    stage =
                        END_DIALOGUE
                }
            8 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Yeah, sure, human! De water flows fast from underground. When you bathe here, it make you flow fast overground for long, long time.",
                ).also {
                    stage++
                }
            9 -> playerl(FaceAnim.HALF_ASKING, "Flow fast?").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You know, flow on your feetsies. Fast-like. Quick, like a bunny!",
                ).also { stage++ }
            11 -> playerl(FaceAnim.HALF_ASKING, "Are you talking about running?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Yeah, run, flow - same thing. Have bath here, it not matter how heavy you are, how much you carry - you can flow long time!",
                ).also {
                    stage++
                }
            13 -> playerl(FaceAnim.HALF_ASKING, "Thanks, that's good to know.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SnarrlDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SNARRL_7069, NPCs.CHOMP_7074)
    }
}
