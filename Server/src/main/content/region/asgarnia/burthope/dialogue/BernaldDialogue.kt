package content.region.asgarnia.burthope.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Represents the Bernald dialogue.
 */
class BernaldDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.WORRIED, "Do you know anything about grapevine diseases?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "No, I'm afraid I don't.").also { stage++ }
            2 -> npcl(FaceAnim.GUILTY, "Oh, that's a shame. I hope I find someone soon, otherwise I could lose all of this year's crop.").also { stage = END_DIALOGUE }
        }
    }
}