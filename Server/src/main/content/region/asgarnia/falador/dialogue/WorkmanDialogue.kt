package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Workman (Falador) dialogue.
 */
class FaladorWorkmanDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Hiya.").also { stage++ }
            1 -> npc(FaceAnim.ANNOYED, "What do you want? I've got work to do!").also { stage++ }
            2 -> player(FaceAnim.ASKING, "Can you teach me anything?").also { stage++ }
            3 -> npcl(FaceAnim.ANNOYED, "No - I've got one lousy apprentice already, and that's quite enough hassle! Go away!").also { stage = END_DIALOGUE }
        }
    }
}
