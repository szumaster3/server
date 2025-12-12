package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

class DrorkarDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hello, how are you?").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "Packages, packages and more!").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "Ugh.. Okay, have a good day.").also { stage = END_DIALOGUE }
        }
    }
}
