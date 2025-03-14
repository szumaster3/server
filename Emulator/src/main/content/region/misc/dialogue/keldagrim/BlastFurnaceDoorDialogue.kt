package content.region.misc.dialogue.keldagrim

import content.minigame.blastfurnace.BlastFurnace
import core.api.removeItem
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

class BlastFurnaceDoorDialogue(
    val fee: Int,
) : DialogueFile() {
    var init = true

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                sendDialogue(
                    player!!,
                    "You must be Smithing Level 60 or higher in order to enter the Blast Furnace",
                ).also {
                    stage++
                }
            1 ->
                sendDialogue(
                    player!!,
                    "However, you may enter for 10 minutes if you pay the entrance fee.<br>($fee gp)",
                ).also {
                    stage++
                }
            2 -> options("Yes", "No").also { stage++ }
            3 ->
                when (buttonID) {
                    1 -> {
                        if (removeItem(player!!, Item(Items.COINS_995, fee))) {
                            BlastFurnace.enter(player!!, true)
                        } else {
                            sendDialogue(player!!, "You don't have enough gold to cover the entrance fee!")
                        }
                        stage = END_DIALOGUE
                    }
                    2 -> sendDialogue(player!!, "Then get out of here!").also { stage = END_DIALOGUE }
                }
        }
    }
}
