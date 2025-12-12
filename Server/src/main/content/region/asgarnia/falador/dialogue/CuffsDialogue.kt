package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

class CuffsDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hello. nice day for a walk, isn't it?").also { stage++ }
            1 -> npc(FaceAnim.HALF_GUILTY, "A walk? Oh, yes, that's what we're doing.", "We're just out here for a walk.").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "I'm glad you're just out here for a walk. A more suspicious", "person would think you were waiting here to attack weak-", "looking travellers.").also { stage++ }
            3 -> npc(FaceAnim.HALF_GUILTY, "Nope, we'd never do anything like that.", "Just a band of innocent walkers, that's us.").also { stage++ }
            4 -> player(FaceAnim.HALF_GUILTY, "Alright, have a nice walk.").also { stage = END_DIALOGUE }
        }
    }
}