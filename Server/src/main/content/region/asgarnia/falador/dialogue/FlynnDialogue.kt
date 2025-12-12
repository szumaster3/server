package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Flynn dialogue.
 */
class FlynnDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HAPPY, "Hello. Do you want to buy or sell any maces?").also { stage++ }
            1 -> showTopics(
                Topic("Well, I'll have a look, at least.", 2),
                Topic("No, thanks.", END_DIALOGUE)
            )
            2 -> {
                end()
                openNpcShop(player!!, NPCs.FLYNN_580)
            }
        }
    }
}
