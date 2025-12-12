package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Falador Man house dialogue.
 */
class FaladorManDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hello.").also { stage++ }
            1 -> npc(FaceAnim.HALF_GUILTY, "What are you doing in my house?").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "I was just exploring.").also { stage++ }
            3 -> npc(FaceAnim.HALF_GUILTY, "You're exploring my house?").also { stage++ }
            4 -> player(FaceAnim.HALF_GUILTY, "You don't mind, do you?").also { stage++ }
            5 -> npc(FaceAnim.HALF_GUILTY, "But... why are you exploring in my house?").also { stage++ }
            6 -> player(FaceAnim.HALF_GUILTY, "Oh, I don't know. I just wandered in, saw you and thought", "it'd be fun to speak to you.").also { stage++ }
            7 -> npc(FaceAnim.HALF_GUILTY, "... you are very strange...").also { stage++ }
            8 -> player(FaceAnim.HALF_GUILTY, "Perhaps I should go now.").also { stage++ }
            9 -> npc(FaceAnim.HALF_GUILTY, "Yes, please go away now.").also { stage = END_DIALOGUE }
        }
    }
}
