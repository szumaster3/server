package content.minigame.blastfurnace.dialogue

import content.minigame.blastfurnace.plugin.BlastFurnace
import core.api.removeItem
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

/**
 * Represents the Blast furnace door dialogue.
 */
class BlastFurnaceDoorDialogue(val fee: Int) : DialogueFile() {

    var init = true

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendDialogue(player!!, "You must be Smithing Level 60 or higher in order to enter the Blast Furnace").also { stage++ }
            1 -> sendDialogue(player!!, "However, you may enter for 10 minutes if you pay the entrance fee.<br>($fee gp)").also { stage++ }
            2 -> options("Yes", "No").also { stage++ }
            3 -> when (buttonID) {
                1 -> if (removeItem(player!!, Item(Items.COINS_995, fee))) {
                    end()
                    BlastFurnace.enter(player!!, true)
                } else {
                    end()
                    sendDialogue(player!!, "You don't have enough gold to cover the entrance fee!")
                }

                2 -> sendDialogue(player!!, "Then get out of here!").also { stage = END_DIALOGUE }
            }
        }
    }
}
