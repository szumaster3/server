package content.region.asgarnia.quest.troll.dialogue

import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Quests

class DunstanDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.TROLL_STRONGHOLD)) {
            in 1..10 -> {
                when (stage) {
                    START_DIALOGUE ->
                        npcl(FaceAnim.FRIENDLY, "Have you managed to rescue Godric yet?").also { stage++ }

                    1 -> playerl(FaceAnim.FRIENDLY, "Not yet.").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Please hurry! Who knows what they will do to him? Is there anything I can do in the meantime?",
                        ).also {
                            stage++
                        }

                    3 ->
                        showTopics(
                            Topic(FaceAnim.THINKING, "Is it OK if I use your anvil?", 10),
                            Topic(FaceAnim.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                        )

                    10 -> npcl(FaceAnim.FRIENDLY, "So you're a smithy are you?").also { stage++ }
                    11 -> playerl(FaceAnim.FRIENDLY, "I dabble.").also { stage++ }
                    12 -> npcl(FaceAnim.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
                    13 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage++ }
                    14 -> npcl(FaceAnim.FRIENDLY, "Anything else before I get on with my work?").also { stage = 3 }
                }
            }

            11 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Has Godric returned home?").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "He is safe and sound, thanks to you my friend!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I'm glad to hear it.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I have very little to offer you by way of thanks, but perhaps you will accept these family heirlooms. They were found by my great-great-grandfather, but we still don't have any idea what they do.",
                        ).also {
                            stage++
                        }

                    4 -> {
                        end()
                        finishQuest(player!!, Quests.TROLL_STRONGHOLD)
                    }
                }
            }
        }
    }
}
