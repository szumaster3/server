package content.region.karamja.quest.mm.dialogue

import core.game.dialogue.DialogueFile

class LumoDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> playerl("Hello?")
            1 -> npcl("Hello - Who are you?")
            2 -> playerl("Hello?")
            3 -> npcl("Hello - Who are you?")
            4 -> playerl("Hello?")
            5 -> npcl("Hello - Who are you?")
            6 -> playerl("Hello?")
            7 -> npcl("Hello - Who are you?")
            8 -> playerl("Hello?")
            9 -> npcl("Hello - Who are you?")
            10 -> playerl("Hello?")
            11 -> npcl("Hello - Who are you?")
            12 -> playerl("Hello?")
            13 -> npcl("Hello - Who are you?")
            14 -> playerl("Hello?")
            15 -> npcl("Hello - Who are you?")
            16 -> playerl("Hello?")
            17 -> npcl("Hello - Who are you?")
            18 -> playerl("Hello?")
            19 -> npcl("Hello - Who are you?")
            20 -> playerl("Hello?")
            21 -> npcl("Hello - Who are you?")
            22 -> playerl("Hello?")
            23 -> npcl("Hello - Who are you?")
            24 -> playerl("Hello?")
            25 -> npcl("Hello - Who are you?")
            26 -> playerl("Hello?")
            27 -> npcl("Hello - Who are you?")
            30 -> end()
        }
    }
}
