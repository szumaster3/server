package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Lakki Dwarf dialogue.
 */
class LakkiTheDeliveryDwarfDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hello!").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "I'm sorry, I can't talk right now.").also { stage = END_DIALOGUE }
        }
    }
}
