package content.region.asgarnia.burthope.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Represents the Wistan dialogue.
 */
class WistanDialogue: DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Welcome to Burthorpe Supplies. Your last shop before heading north into the mountains!").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Would you like to buy something?").also { stage++ }
            3 -> options("Yes, please.", "No, thanks.").also { stage++ }
            4 -> when (buttonID) {
                1 -> playerl(FaceAnim.FRIENDLY, "Yes, please.").also { stage = 5 }
                2 -> playerl(FaceAnim.FRIENDLY, "No, thanks.").also { stage = END_DIALOGUE }
            }
            5 -> {
                end()
                openNpcShop(player!!, npc!!.id)
            }
        }
    }
}
