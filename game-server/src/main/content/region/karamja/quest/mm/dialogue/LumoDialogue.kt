package content.region.karamja.quest.mm.dialogue

import core.game.dialogue.DialogueFile

class LumoDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int, ) {
        when (stage) {
            0 -> playerl("Hello?").also { stage++ }
            1 -> npcl("Hello - Who are you?").also { stage = 0 }
            30 -> end()
        }
    }
}
