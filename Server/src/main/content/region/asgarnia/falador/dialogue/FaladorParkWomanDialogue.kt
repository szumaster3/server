package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Falador Women Park dialogue.
 */
class FaladorParkWomanDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.HAPPY, "Hello.").also { stage++ }
            1 -> npc(FaceAnim.HAPPY, "Greetings! Have you come to gaze in rapture at the", "natural beauty of Falador's parkland?").also { stage++ }
            2 -> player(FaceAnim.STRUGGLE, "Um, yes, very nice. Lots of.... trees and stuff.").also { stage++ }
            3 -> npc(FaceAnim.HAPPY, "Trees! I do so love trees! And flowers! And squirrels.").also { stage++ }
            4 -> player(FaceAnim.THINKING, "Sorry, I have a strange urge to be somewhere else.").also { stage++ }
            5 -> npc(FaceAnim.HAPPY, "Come back to me soon and we can talk again about trees!").also { stage++ }
            6 -> player(FaceAnim.NEUTRAL, "...").also { stage = END_DIALOGUE }
        }
    }
}
