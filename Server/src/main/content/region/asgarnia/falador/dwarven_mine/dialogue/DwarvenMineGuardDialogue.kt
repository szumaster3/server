package content.region.asgarnia.falador.dwarven_mine.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Dwarven Mine Guard dialogue.
 */
class DwarvenMineGuardDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_ANGRY1, "Don't distract me while I'm on duty! This mine has to be protected!").also { stage++ }
            2 -> player(FaceAnim.HALF_ASKING, "What's going to attack a mine?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_ANGRY1, "Goblins! They wander everywhere, attacking anyone they think is small enough to be an easy victim. We need more cannons to fight them off properly.").also { stage++ }
            4 -> player(FaceAnim.HALF_THINKING, "Well, I've done my bit to help with that.").also { stage++ }
            5 -> npcl(FaceAnim.OLD_ANGRY1, "Yes, I heard. Now please let me get on with my guard duties.").also { stage++ }
            6 -> player(FaceAnim.HALF_GUILTY, "Alright, I'll leave you alone now.").also { stage = END_DIALOGUE }
        }
    }
}
