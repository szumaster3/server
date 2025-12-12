package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Wayne dialogue.
 */
class WayneDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HAPPY, "Welcome to Wayne's Chains. Do you wanna buy or", "sell some chain mail?").also { stage++ }
            1 -> showTopics(
                Topic("Yes please.", 2),
                Topic("No, thanks.", END_DIALOGUE)
            )
            2 -> {
                end()
                openNpcShop(player!!, NPCs.WAYNE_581)
            }
        }
    }
}
