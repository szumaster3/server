package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Drogo dwarf dialogue.
 */
class DrogoDwarfDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "'Ello. Welcome to my Mining shop, friend.").also { stage++ }
            1 -> showTopics(
                Topic("Do you want to trade?", 4),
                Topic("Hello, shorty.", 3),
                Topic("Why don't you ever restock ores and bars?", 2)
            )
            2 -> npc(FaceAnim.OLD_DEFAULT, "The only ores and bars I sell are those sold to me.").also { stage = END_DIALOGUE }
            3 -> npc(FaceAnim.OLD_ANGRY1, "I may be short, but at least I've got manners.").also { stage = END_DIALOGUE }
            4 -> {
                end()
                openNpcShop(player!!, NPCs.DROGO_DWARF_579)
            }
        }
    }
}
