package content.region.kandarin.ardougne.quest.tol.dialogue

import content.data.GameAttributes
import core.api.getAttribute
import core.api.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents Transmute dialogue.
 */
@Initializable
class TransmuteDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.TOWER_OF_LIFE)) {
            in 0..2 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi there. What is this place?").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Ah, this be the Tower of Life! Beautiful, isn't it?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "I prefer ordinary houses.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Pah, you have no taste.").also { stage = END_DIALOGUE }
            }
            3 -> if (getAttribute(player, GameAttributes.TOL_TOWER_ACCESS, 0) == 1) {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "I'm a builder!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "I question why you are so pleased about this.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Well, I think it's a positive step in my career as a courageous adventurer.").also { stage = END_DIALOGUE }
                }
            } else {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "I must say, the inside of the tower is really something to behold.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Why, thanks. You'll find various pieces of machinery to fix in there. You can no doubt find the materials in there, too.").also { stage = END_DIALOGUE }
                }
            }
            6 -> if (getAttribute(player, GameAttributes.TOL_CUTSCENE, false)) {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "I can't believe what you guys have done.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Marvellous.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "No. Scary!").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "That too.").also { stage = END_DIALOGUE }
                }
            }
            8 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Well, I've sat down and had a good chat with the Homunculus.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "And?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "You'll soon find out!").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Huh?").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TRANSMUTE_THE_ALCHEMIST_5585)
}
