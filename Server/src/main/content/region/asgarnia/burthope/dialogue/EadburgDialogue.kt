package content.region.asgarnia.burthope.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Eadburg dialogue.
 */
class EadburgDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_WORRIED,"Hello there. What's in the pot?").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY,"The stew for the servants' main meal.").also { stage++ }
            2 -> playerl(FaceAnim.HALF_WORRIED,"Can I have some?").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY,"Well not unless you become a servant. There's a war on, you know, so food is scarce.").also { stage++ }
            4 -> playerl(FaceAnim.HALF_WORRIED,"Good point. I should probably leave you to it.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY,"Bye then.").also { stage = END_DIALOGUE }
        }
    }
}
