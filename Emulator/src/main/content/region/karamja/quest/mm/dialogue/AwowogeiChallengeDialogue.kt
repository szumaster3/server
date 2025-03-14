package content.region.karamja.quest.mm.dialogue

import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import org.rs.consts.Quests

class AwowogeiChallengeDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> npcl("Have you brought with you a captive?").also { stage++ }
            1 -> playerl("Yes, I have.").also { stage++ }
            2 -> npcl("Well done!").also { stage++ }
            3 ->
                npcl(
                    "You have shown yourself to be very resourceful. You have managed to complete an extremely long journey remarkably quickly.",
                ).also {
                    stage++
                }
            4 -> playerl("Thank you.").also { stage++ }
            5 ->
                npcl(
                    "You are clearly well acquired with the ways of this world. We will talk more on this later.",
                ).also {
                    stage++
                }
            6 -> npcl("In the meantime, feel free to remain as long as you like on my island.").also { stage++ }
            7 -> playerl("What about the proposed alliance, Awowogei?.").also { stage++ }
            8 ->
                npcl(
                    "I must think upon it some more and discuss the matter with my advisers. We will contact you when we are ready.",
                ).also {
                    stage = 99
                }

            99 -> {
                end()
                setQuestStage(player!!, Quests.MONKEY_MADNESS, 46)
            }
        }
    }
}
