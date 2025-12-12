package content.region.asgarnia.port_sarim.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Thaki The Delivery Dwarf dialogue.
 */
class ThakiTheDeliveryDwarfDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Arrr!").also { stage++ }
            1 -> player(FaceAnim.LAUGH, "Hi, little fellow.").also { stage++ }
            2 -> npc(FaceAnim.OLD_ANGRY1, "What did you just say to me!?").also { stage++ }
            3 -> player(FaceAnim.GUILTY, "Arrr! nothing, nothing at all..").also { stage = END_DIALOGUE }
        }
    }
}
