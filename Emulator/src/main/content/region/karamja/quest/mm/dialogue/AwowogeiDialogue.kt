package content.region.karamja.quest.mm.dialogue

import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import org.rs.consts.NPCs
import org.rs.consts.Quests

class AwowogeiDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> playerl("Greetings, Awowogei.").also { stage++ }
            1 -> npcl("Greetings, visitor. What brings you on my island?").also { stage++ }
            2 -> playerl("I am envoy from the monkeys of Karamja. we wish to propose an alliance.").also { stage++ }
            3 ->
                npcl(
                    "I see. Ours is strong and mighty lineage of monkey, visitor. We clearly do not need your offer of an alliance.",
                ).also {
                    stage++
                }
            4 ->
                playerl(
                    "I am envoy from the monkeys of Karamja. we wish to propose an alliance.Awowogei, please consider my offer carefully.",
                ).also {
                    stage++
                }
            5 ->
                playerl(
                    "We offer strength in numbers and our island would serve as a northern platform for defence. All that we ask for in return is peace.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    NPCs.UWOGO_1449,
                    FaceAnim.ANGRY,
                    "I don't believe him, Awowogei. Never trust a northern monkey.",
                ).also { stage++ }

            7 -> npcl("What is your opinion, Murowoi?").also { stage++ }
            8 -> npcl(NPCs.MURUWOI_1450, FaceAnim.NEUTRAL, "I think he is trustworthy, sir.").also { stage++ }
            9 ->
                npcl(
                    "I have to admit, I have always regarded your kind as our inferior cousins, visitor.",
                ).also { stage++ }
            10 -> npcl("However, I am well aware that you man have a few things to offer.").also { stage++ }
            11 ->
                npcl(
                    NPCs.UWOGO_1449,
                    FaceAnim.ANGRY,
                    "Don't listen to ${if (player!!.isMale) "him" else "her"} Awowogei!",
                ).also { stage++ }

            12 -> npcl("Be silent, Uwogo.").also { stage++ }
            13 ->
                npcl(
                    "I have heard your kind are exceptionally resourceful. I wish to put this reputation on the test.",
                ).also {
                    stage++
                }
            14 -> npcl("You must be well aware your kind are hunted and trapped almost everywhere.").also { stage++ }
            15 ->
                npcl(
                    "In particular you may have heard of such activities in a city known to the humans as Ardougne.",
                ).also {
                    stage++
                }
            16 ->
                npcl(
                    "There are several of your kind kept captive there. I challenge you to free one and return it to me",
                ).also {
                    stage++
                }
            17 -> playerl("How am I meant to free one of them?").also { stage++ }
            18 -> npcl("This is for you to decide, visitor.").also { stage++ }
            19 -> playerl("Very well. I will be back later, with one of the captives.").also { stage++ }
            99 -> {
                end()
                setQuestStage(player!!, Quests.MONKEY_MADNESS, 33)
            }
        }
    }
}
