package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Cassie dialogue.
 */
class CassieDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(FaceAnim.HAPPY, "I buy and sell shields; do you want to trade?").also { stage++ }
            1 -> showTopics(
                Topic("Yes, please.", 2),
                Topic("No, thanks.", END_DIALOGUE)
            )
            2 -> {
                end()
                openNpcShop(player!!, NPCs.CASSIE_577)
            }
        }
    }
}
