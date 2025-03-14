package content.region.kandarin.quest.grail.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FisherKingDialogue : DialogueFile() {
    var STAGE_SEEK_GRAIL = 10
    var STAGE_LOOK_WELL = 20
    var STAGE_HOW_KNOW = 2
    var STAGE_LOOK_AROUND = 5

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.THE_FISHER_KING_220)

        when (stage) {
            0 -> {
                if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 50) {
                    npcl(FaceAnim.HAPPY, "You missed all the excitement!")
                    stage = 30
                } else {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Ah! You got inside at last! You spent all that time fumbling around outside, I thought you'd never make it here.",
                    )
                    stage++
                }
            }

            1 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "How did you know what I have been doing?", STAGE_HOW_KNOW),
                    Topic(FaceAnim.NEUTRAL, "I seek the Holy Grail.", STAGE_SEEK_GRAIL),
                    Topic(FaceAnim.NEUTRAL, "You don't look too well.", STAGE_LOOK_WELL),
                )

            STAGE_HOW_KNOW ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Oh, I can see what is happening in my realm. I have sent clues to help you get here, such as the fisherman, and the crone.",
                ).also { stage++ }

            3 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "I seek the Holy Grail.", STAGE_SEEK_GRAIL),
                    Topic(FaceAnim.NEUTRAL, "You don't look too well.", STAGE_LOOK_WELL),
                    Topic(FaceAnim.NEUTRAL, "Do you mind if I have a look around?", STAGE_LOOK_AROUND),
                )

            STAGE_LOOK_AROUND ->
                npcl(FaceAnim.NEUTRAL, "No, not at all. Please, be my guest.").also {
                    stage = END_DIALOGUE
                }

            STAGE_SEEK_GRAIL ->
                npcl(
                    FaceAnim.HAPPY,
                    "Ah excellent. A knight come to seek the Holy Grail. Maybe our land can be restored to its former glory.",
                ).also { stage++ }

            11 ->
                npcl(
                    FaceAnim.HAPPY,
                    "At the moment the Grail cannot be removed from the castle. Legend has it a questing knight will one day work out how to restore our land; then he will claim the Grail as his prize.",
                ).also { stage++ }

            12 -> playerl(FaceAnim.NEUTRAL, "Any ideas how I can restore the land?").also { stage++ }
            13 -> npcl(FaceAnim.SAD, "None at all.").also { stage++ }
            14 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "You don't look too well.", STAGE_LOOK_WELL),
                    Topic(FaceAnim.NEUTRAL, "Do you mind if I have a look around?", STAGE_LOOK_AROUND),
                )

            STAGE_LOOK_WELL -> npcl(FaceAnim.SAD, "Nope, I don't feel so good either.").also { stage++ }
            21 ->
                npcl(
                    FaceAnim.SAD,
                    "I fear my life is running short... Alas, my son and heir is not here. I am waiting for my son to return to this castle.",
                ).also { stage++ }

            22 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "If you could find my son, that would be a great weight off my shoulders.",
                ).also { stage++ }

            23 -> playerl(FaceAnim.NEUTRAL, "Who is your son?").also { stage++ }
            24 -> npcl(FaceAnim.NEUTRAL, "He is known as Percival.").also { stage++ }
            25 -> npcl(FaceAnim.NEUTRAL, "I believe he is a knight of the round table.").also { stage++ }
            26 ->
                playerl(FaceAnim.NEUTRAL, "I shall go and see if I can find him.").also {
                    stage = END_DIALOGUE
                    if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 30) {
                        setQuestStage(player!!, Quests.HOLY_GRAIL, 40)
                    }
                }

            30 ->
                npcl(
                    FaceAnim.HAPPY,
                    "I got here and agreed to take over duties as king here, then before my eyes the most miraculous changes occured here... grass and trees were growing outside before our very eyes!",
                ).also { stage++ }

            31 -> npcl(FaceAnim.HAPPY, "Thank you very much for showing me the way home.").also { stage = END_DIALOGUE }
        }
    }
}
