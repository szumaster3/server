package content.region.karamja.quest.mm.dialogue

import core.game.dialogue.DialogueFile

class KrukDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> playerl("Hello?").also { stage++ }
            1 -> npcl("What brings you up here, monkey?").also { stage++ }
            3 ->
                playerl(
                    "I have come to seek audience with Awowogei. I am told I need your permission to do so.?",
                ).also { stage++ }
            4 -> npcl("That's right - you do. What business have you on our island?").also { stage++ }
            5 -> playerl("I am envoy from the monkeys of Karamja. I have come to propose an alliance.").also { stage++ }
            6 ->
                npcl("I see. Very well, you look genuine enough. Follow me.").also {
                    stage++
                }
        }
    }
}
