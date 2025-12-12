package content.region.asgarnia.burthope.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Represents the Unferth dialogue.
 */
class UnferthDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        /*
         * when (stage) {
         * START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi Unferth. How are you doing?").also { stage++ }
         * 1 -> npcl(FaceAnim.FRIENDLY, "It's just not the same without Bob around.").also { stage++ }
         * 2 -> playerl(FaceAnim.FRIENDLY, "I'm so sorry Unferth.").also { stage++ }
         * 3 -> npcl(FaceAnim.FRIENDLY, "Gertrude asked me if I'd like one of her new kittens. I don't think I'm ready for that yet.").also { stage++ }
         * 4 -> playerl(FaceAnim.FRIENDLY, "Give it time. Things will get better, I promise.").also { stage++ }
         * 5 -> npcl(FaceAnim.FRIENDLY, "Thanks ${player.name}.").also { stage = END_DIALOGUE }
         * }
         */

        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.GUILTY, "Hello.").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "What's wrong?").also { stage++ }
            2 -> npcl(FaceAnim.GUILTY, "It's fine. Nothing for you to worry about.").also { stage = END_DIALOGUE }
        }
    }
}
