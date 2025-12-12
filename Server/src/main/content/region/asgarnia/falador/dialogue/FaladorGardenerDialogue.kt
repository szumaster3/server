package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Falador Gardener dialogue.
 */
class FaladorGardenerDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.HAPPY, "Hello.").also { stage++ }
            1 -> npc(FaceAnim.HALF_GUILTY, "Oi'm busy. If tha' wants owt, tha' can go find Wyson.", "He's ta boss 'round here. And,", "KEEP YE' TRAMPIN' FEET OFF MA'FLOWERS!").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "Right...").also { stage = END_DIALOGUE }
        }
    }
}
