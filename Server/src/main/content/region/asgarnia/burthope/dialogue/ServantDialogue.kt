package content.region.asgarnia.burthope.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Represents the Servant dialogue.
 */
class ServantDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
            1 -> npcl(FaceAnim.HALF_GUILTY, "Hi").also { stage++ }
            2 -> npcl(FaceAnim.HALF_GUILTY, "Look, I'd better not talk. I'll get in trouble.").also { stage++ }
            3 -> npcl(FaceAnim.HALF_GUILTY, "If you want someone to show you round the castle ask Eohric, the Head Servant.").also { stage = END_DIALOGUE }
        }
    }
}
