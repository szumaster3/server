package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Jeff dialogue.
 */
class JeffDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Tell me, is the guard still watching us?").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Why would you care if there's a guard watching you?").also { stage++ }
            2 -> npc(FaceAnim.HALF_GUILTY, "Oh, forget it.").also { stage = END_DIALOGUE }
        }
    }
}
