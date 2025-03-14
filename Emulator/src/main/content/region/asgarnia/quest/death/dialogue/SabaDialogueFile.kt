package content.region.asgarnia.quest.death.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import org.rs.consts.Quests

class SabaDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.DEATH_PLATEAU)) {
            19 ->
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npc(FaceAnim.ANNOYED, "What?!").also { stage++ }
                    2 ->
                        showTopics(
                            Topic(FaceAnim.THINKING, "I'm looking for the guard that was on the last night.", 10),
                            Topic(FaceAnim.THINKING, "Do you know of another way up Death Plateau?", 20),
                            Topic(FaceAnim.HALF_GUILTY, "Nothing, sorry!", END_DIALOGUE),
                        )

                    10 -> npcl(FaceAnim.ANNOYED, "Who?!").also { stage++ }
                    11 -> npcl(FaceAnim.ANNOYED, "Buzz off!").also { stage = END_DIALOGUE }
                    20 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Why would I want to go up there? I just want to be left in peace!",
                        ).also { stage++ }

                    21 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "It used to be just humans trampling past my cave and making a racket. Now there's those blasted trolls too! Not only do they stink and argue with each other loudly but they are always fighting the humans.",
                        ).also { stage++ }

                    22 -> npcl(FaceAnim.ANNOYED, "I just want to be left in peace!").also { stage++ }
                    23 -> playerl(FaceAnim.FRIENDLY, "Ah... I might be able to help you.").also { stage++ }
                    24 -> npcl(FaceAnim.ANNOYED, "How?!").also { stage++ }
                    25 ->
                        playerl(
                            FaceAnim.HALF_THINKING,
                            "I'm trying to help the...er...humans to reclaim back Death Plateau. If you help me then at least you'd be rid of the trolls.",
                        ).also { stage++ }

                    26 -> npcl(FaceAnim.ANNOYED, "Hmph.").also { stage++ }
                    27 -> npcl(FaceAnim.ANNOYED, "Let me see...").also { stage++ }
                    28 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I've only been up Death Plateau once to complain about the noise but those pesky trolls started throwing rocks at me!",
                        ).also { stage++ }

                    29 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Before the trolls came there used to be a nettlesome Sherpa that took humans exploring or something equally stupid. Perhaps he'd know another way.",
                        ).also { stage++ }

                    30 -> playerl(FaceAnim.FRIENDLY, "Where does this Sherpa live?").also { stage++ }
                    31 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "I don't know but it can't be far as he used to be around all the time!",
                        ).also {
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 20)
                            stage = END_DIALOGUE
                        }
                }

            20 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                    1 -> npcl(FaceAnim.ANNOYED, "Have you got rid of those pesky trolls yet?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Where did you say this Sherpa was?").also { stage++ }
                    3 ->
                        npcl(FaceAnim.ANNOYED, "I dunno but he must live around here somewhere!").also {
                            stage = END_DIALOGUE
                        }
                }

            in 21..26 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Have you got rid of those pesky trolls yet?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I'm working on it!").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Grr!").also { stage = END_DIALOGUE }
                }
        }
    }
}
