package content.region.asgarnia.burthope.dialogue

import core.api.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import core.tools.START_DIALOGUE
import shared.consts.Quests

/**
 * Represents the Burthope dialogues.
 */
class BurthopeNPCDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        val random = RandomFunction.random(1, 3)
        if (isQuestComplete(player!!, Quests.DEATH_PLATEAU)) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage = random }
                1 -> npcl(FaceAnim.HAPPY, "I heard about what you did, thank you!").also { stage = END_DIALOGUE }
                2 -> npcl(FaceAnim.HAPPY, "Thank you so much!").also { stage = END_DIALOGUE }
                3 -> npcl(FaceAnim.HAPPY, "Surely we are safe now!").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage = random }
                1 -> npcl(FaceAnim.FRIENDLY, "Hello stranger.").also { stage = END_DIALOGUE }
                2 -> npcl(FaceAnim.FRIENDLY, "Hi!").also { stage = END_DIALOGUE }
                3 -> npcl(FaceAnim.FRIENDLY, "Welcome to Burthorpe!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }
            }
        }
    }
}