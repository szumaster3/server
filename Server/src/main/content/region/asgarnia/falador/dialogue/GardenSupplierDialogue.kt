package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Falador Garden Supplier dialogue.
 */
class GardenSupplierDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc("Hello, I sell many plants. Would you like", "to see what I have?").also { stage++ }
            1 -> showTopics(
                Topic("Yes, please!", 2),
                Topic("No, thanks.", END_DIALOGUE),
                title = "Select one"
            )
            2 -> {
                end()
                openNpcShop(player!!, NPCs.GARDEN_SUPPLIER_4251)
            }
        }
    }
}
