package content.region.asgarnia.quest.death.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import org.rs.consts.Quests

class EohricDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.DEATH_PLATEAU)) {
            in 5..9 -> {
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FaceAnim.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 ->
                        showTopics(
                            Topic(FaceAnim.THINKING, "I'm looking for the guard that was on the last night.", 10),
                            Topic(FaceAnim.FRIENDLY, "Do you know of another way up Death Plateau?", 20),
                            Topic(FaceAnim.FRIENDLY, "No, I'm just looking around.", END_DIALOGUE),
                        )

                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "There was only one guard on last night. Harold. He's a nice lad, if a little dim.",
                        ).also { stage++ }

                    11 -> player(FaceAnim.FRIENDLY, "Do you know where he is staying?").also { stage++ }
                    12 -> npc(FaceAnim.FRIENDLY, "Harold is staying at the Toad and Chicken.").also { stage++ }
                    13 ->
                        player(FaceAnim.FRIENDLY, "Thanks!").also {
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 10)
                            content.region.asgarnia.quest.death.dialogue.HaroldDialogueFile.Companion
                                .resetNpc(player!!)
                            stage = END_DIALOGUE
                        }

                    20 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "No, sorry. I wouldn't want to go north-east from here, it's very rocky and barren.",
                        ).also { stage = END_DIALOGUE }
                }
            }

            10 -> {
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FaceAnim.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 ->
                        showTopics(
                            Topic(FaceAnim.THINKING, "Where is the guard that was on last night staying?", 10),
                            Topic(FaceAnim.FRIENDLY, "Do you know of another way up Death Plateau?", 20),
                            Topic(FaceAnim.FRIENDLY, "No, I'm just looking around.", END_DIALOGUE),
                        )

                    10 ->
                        npc(FaceAnim.FRIENDLY, "Harold is staying at the Toad and Chicken.").also {
                            stage = END_DIALOGUE
                        }

                    20 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "No, sorry. I wouldn't want to go north-east from here, it's very rocky and barren.",
                        ).also { stage = END_DIALOGUE }
                }
            }

            11 -> {
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FaceAnim.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "I found Harold but he won't talk to me!.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.THINKING,
                            "Hmm. Harold has got in trouble a few over his drinking and gambling. Perhaps he'd open up after a drink?",
                        ).also { stage++ }

                    4 ->
                        playerl(FaceAnim.FRIENDLY, "Thanks, I'll try that!").also {
                            setQuestStage(player!!, Quests.DEATH_PLATEAU, 12)
                            stage = END_DIALOGUE
                        }
                }
            }

            12 -> {
                when (stage) {
                    0 -> player(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FaceAnim.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 -> playerl(FaceAnim.ASKING, "You said Harold had a weakness?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.THINKING,
                            "Yes, if you buy Harold a beer he might talk to you. I also know he has a weakness for gambling. Hope that helps!",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.FRIENDLY, "Thanks for the help!").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
