package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Ambassador Spanfipple dialogue.
 */
class AmbassadorSpanfippleDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "It's all very white round here, isn't it?").also { stage++ }
            1 -> player(FaceAnim.THINKING, "Well, it is the White Knights' Castle.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "I think it would all look better in pink. At least then I wouldn't be squinting all the time.").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Yes, but then they'd have to become the Pink Knights. I think they'd have problems recruiting then.").also { stage++ }
            4 -> npc(FaceAnim.OLD_DEFAULT, "You're probably right. Maybe brown, then.").also { stage++ }
            5 -> player(FaceAnim.HALF_THINKING, "I think that may be worse...").also { stage++ }
            6 -> npc(FaceAnim.OLD_ANGRY1, "Bah, humans have no sense of style...").also { stage = END_DIALOGUE }
        }
    }
}
