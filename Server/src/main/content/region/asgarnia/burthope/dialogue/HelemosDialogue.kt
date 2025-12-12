package content.region.asgarnia.burthope.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE

/**
 * Represents the Helemos dialogue.
 */
class HelemosDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Greetings. Welcome to the Heroes' Guild.").also { stage++ }
            1 -> showTopics(
                Topic("So do you sell anything here?", 2),
                Topic("So what can I do here?", 4)
            )
            2 -> npcl(FaceAnim.HAPPY, "Why yes! We DO run an exclusive shop for our members!").also { stage++ }
            3 -> end().also { openNpcShop(player!!, npc!!.id) }
            4 -> npcl(FaceAnim.HAPPY, "Look around... there are all sorts of things to keep our guild members entertained!").also { stage = END_DIALOGUE }
        }
    }
}
