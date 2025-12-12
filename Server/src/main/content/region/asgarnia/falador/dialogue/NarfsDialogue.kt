package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Narfs dialogue.
 */
class NarfsDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl(FaceAnim.LAUGH, "That's a funny name you've got.").also { stage++ }
            1 -> npc(FaceAnim.ASKING, "'Narf'? You think that's funny?", "At least I Don't call myself '" + player?.username + "' ", "Where did you get a name like that?").also { stage++ }
            2 -> player(FaceAnim.FRIENDLY, "It seemed like a good idea at the time!").also { stage++ }
            3 -> npc(FaceAnim.ANGRY, "Bah!").also { stage = END_DIALOGUE }
        }
    }
}
