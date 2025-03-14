package content.region.misc.handlers.tutorial

import core.api.closeAllInterfaces
import core.game.dialogue.DialogueFile

class WelcomeMessage : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> {
                player!!.dialogueInterpreter.sendDialogues(
                    "Welcome to Lumbridge! To get more help, simply click on the",
                    "Lumbridge Guide or one of the Tutors - these can be found by",
                    "looking for the question mark icon on your minimap. If you find you",
                    "are lost at any time, look for a signpost or use the Lumbridge Home",
                    "Teleport spell.",
                )
                stage++
            }

            1 -> {
                end()
                closeAllInterfaces(player!!)
            }
        }
    }
}
