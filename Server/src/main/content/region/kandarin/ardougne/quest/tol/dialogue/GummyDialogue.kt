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
 * Represents the Gummy dialogue.
 */
@Initializable
class GummyDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.TOWER_OF_LIFE)) {
            in 0..1 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "What are you up to?").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Rock, Paper, Scissors. Can't you tell?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "It was more of a rhetorical question.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Rhetora-what?").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Shouldn't you be working?").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "We are. It's team building.").also { stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Why, hello there!").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "What do you want?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Clothing.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Nope. Need mine.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Ah, come on. You know you want to give me free stuff.").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Stop bothering me, can't you see I'm busy?").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Yes, VERY busy I'm sure.").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "Okay, okay. The other day I was drying my clothes on a line down by the shore. A storm hit and some of my clothing went missing.").also { stage++ }
                8 -> playerl(FaceAnim.FRIENDLY, "Likely story. Sure you weren't skinny dipping?").also { stage++ }
                9 -> npcl(FaceAnim.FRIENDLY, "Just go look and leave me be! Search around the tower and you may find them.").also { stage = END_DIALOGUE }
            }

            3 -> if (getAttribute(player, GameAttributes.TOL_TOWER_ACCESS, 0) == 1) {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "You winning?").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "It's a draw at the moment. First it was best of three, then it was best of five.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "And now?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Best of a hundred and one.").also { stage = END_DIALOGUE }
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GUMMY_5591)
}
