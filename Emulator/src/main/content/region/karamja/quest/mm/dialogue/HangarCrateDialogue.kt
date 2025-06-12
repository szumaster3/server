package content.region.karamja.quest.mm.dialogue

import core.api.addItemOrDrop
import core.api.sendDialogueOptions
import core.api.sendItemDialogue
import core.api.setTitle
import core.game.dialogue.DialogueFile
import org.rs.consts.Items

class HangarCrateDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    Items.SPARE_CONTROLS_4002,
                    "This crate is full of ... spare controls!",
                ).also { stage++ }

            1 -> {
                setTitle(player!!, 2)
                sendDialogueOptions(player!!, "Do you wish to take one?", "Yes", "No").also { stage++ }
            }

            2 ->
                when (buttonID) {
                    1 -> addItemOrDrop(player!!, Items.SPARE_CONTROLS_4002).also { end() }
                    2 -> end()
                }
        }
    }
}
