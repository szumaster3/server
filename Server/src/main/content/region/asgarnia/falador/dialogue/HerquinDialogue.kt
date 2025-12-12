package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Herquin dialogue.
 */
class HerquinDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> showTopics(
                Topic("Do you wish to trade?", 1),
                Topic("Sorry, I don't want to talk to you, actually.", 3),
            )
            1 -> npc(FaceAnim.FRIENDLY, "Why, yes, this is a jewel shop after all.").also { stage++ }
            2 -> {
                end()
                openNpcShop(player!!, NPCs.HERQUIN_584)
            }
            3 -> npc(FaceAnim.ROLLING_EYES, "Huh, charming.").also { stage = END_DIALOGUE }
        }
    }
}
