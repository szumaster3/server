package content.region.misthalin.dialogue.dorgeshuun

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GourmetDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Ooh, a surface-dweller! Have you got any exotic surface foods to sell?",
                ).also {
                    stage++
                }
            1 -> options("Not at the moment.", "Yes!", "What kind of foods do you like?").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.NEUTRAL, "Not at the moment.").also { stage++ }
                    2 -> playerl(FaceAnim.HAPPY, "Yes!").also { stage = 4 }
                    3 -> playerl(FaceAnim.ASKING, "What kind of foods do you like?").also { stage = 5 }
                }
            3 ->
                npcl(FaceAnim.OLD_NORMAL, "A pity. Dorgeshuun food seems so dull now I've tasted surface food.").also {
                    stage =
                        END_DIALOGUE
                }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Splendid! Show me what you've got!").also { stage = END_DIALOGUE }
            5 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "All kinds! I don't want to keep eating the same food, even if I like it. I want to try new tastes!",
                ).also {
                    stage++
                }
            6 ->
                npcl(FaceAnim.OLD_NORMAL, "Why not try something on me and see if I like it?").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOURMET_5788, NPCs.GOURMET_5789, NPCs.GOURMET_5790, NPCs.GOURMET_5791)
    }
}
