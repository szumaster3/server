package content.region.asgarnia.port_sarim.dialogue

import core.api.anyInEquipment
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import shared.consts.Items

/**
 * Represents the Bellemorde cat dialogue.
 */
class CatBellemordeDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player("Hello puss.").also { stage++ }
            1 -> {
                val hasCatSpeakAmulet = anyInEquipment(player!!, Items.CATSPEAK_AMULET_4677, Items.CATSPEAK_AMULETE_6544)
                if (!hasCatSpeakAmulet) {
                    npc(FaceAnim.CHILD_FRIENDLY, "Hiss!").also { stage = END_DIALOGUE }
                } else {
                    npc(FaceAnim.CHILD_FRIENDLY, "Hello human.").also { stage++ }
                }
            }
            2 -> player(FaceAnim.HALF_ASKING, "Would you like a fish?").also { stage++ }
            3 -> npc(FaceAnim.CHILD_FRIENDLY, "I don't want your fish. I hunt and eat what I", "need by myself.").also { stage = END_DIALOGUE }
        }
    }
}
