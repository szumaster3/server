package content.region.asgarnia.port_sarim.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Grum dialogue.
 */
class GrumDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HAPPY, "Would you like to buy or sell some gold jewellery?").also { stage++ }
            1 -> showTopics(
                Topic("Yes, please.", 2),
                Topic("No, I'm not that rich.", 3),
            )
            2 -> {
                end()
                openNpcShop(player!!, NPCs.GRUM_556)
            }
            3 -> npc(FaceAnim.ANNOYED, "Get out, then! We don't want any riff-raff in here.").also { stage = END_DIALOGUE }
        }
    }
}
