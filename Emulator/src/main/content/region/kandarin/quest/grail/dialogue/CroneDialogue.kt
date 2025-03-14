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

class CroneDialogue(
    val forced: Boolean,
) : DialogueFile() {
    var STAGE_SIX_HEADS = 5
    val STAGE_FISHER_KING = 10
    val STAGE_WHISTLE = 20
    val STAGE_SEARCHING = 30
    val STAGE_REALM_CROSSING = 40

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CRONE_217)

        if (forced) {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Wait!").also { stage++ }
                1 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Did you say the Grail? You are a Grail knight, yes? Well you'd better hurry. A Fisher King is in pain.",
                    ).also {
                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 20) {
                            setQuestStage(player!!, Quests.HOLY_GRAIL, 30)
                        }
                        stage++
                    }

                2 -> playerl(FaceAnim.ANGRY, "Well I would, but I don't know where I am going!").also { stage++ }
                3 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Go to where the six heads face, blow the whistle and away you go!",
                    ).also { stage++ }

                4 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "What are the six heads?", STAGE_SIX_HEADS),
                        Topic(FaceAnim.NEUTRAL, "What's a Fisher King?", STAGE_FISHER_KING),
                        Topic(FaceAnim.NEUTRAL, "Ok, I will go searching.", STAGE_SEARCHING),
                        Topic(FaceAnim.NEUTRAL, "What do you mean by the whistle?", STAGE_WHISTLE),
                    )

                STAGE_SIX_HEADS ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "The six stone heads have appeared just recently in the world. They all face the point of realm crossing. Find where two of the heads face,",
                    ).also {
                        stage++
                    }

                6 -> npcl(FaceAnim.NEUTRAL, "and you should be able to pinpoint where it is.").also { stage++ }
                7 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "What's a Fisher King?", STAGE_FISHER_KING),
                        Topic(FaceAnim.NEUTRAL, "Ok, I will go searching.", STAGE_SEARCHING),
                        Topic(FaceAnim.NEUTRAL, "What do you mean by the whistle?", STAGE_WHISTLE),
                        Topic(FaceAnim.NEUTRAL, "The point of realm crossing?", STAGE_REALM_CROSSING),
                    )

                STAGE_FISHER_KING ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "The Fisher King is the owner and slave of the Grail...",
                    ).also { stage++ }

                11 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "What are the six heads?", STAGE_SIX_HEADS),
                        Topic(FaceAnim.NEUTRAL, "Ok, I will go searching..", STAGE_SEARCHING),
                        Topic(FaceAnim.NEUTRAL, "What do you mean by the whistle?", STAGE_WHISTLE),
                    )

                STAGE_WHISTLE ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "You don't know about the whistles yet? The whistles are easy.",
                    ).also {
                        stage++
                    }

                21 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "You will need one to get to and from the Fisher King's realm. They reside in a haunted manor house in Misthalin, though you may not perceive them unless you carry something from the realm of the Fisher",
                    ).also {
                        stage++
                    }

                22 -> npcl(FaceAnim.NEUTRAL, "King...").also { stage++ }
                23 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "What are the six heads?", STAGE_SIX_HEADS),
                        Topic(FaceAnim.NEUTRAL, "What's a Fisher King?", STAGE_FISHER_KING),
                        Topic(FaceAnim.NEUTRAL, "Ok, I will go searching.", STAGE_SEARCHING),
                    )

                STAGE_SEARCHING -> npcl(FaceAnim.NEUTRAL, "Good luck with that.").also { stage = END_DIALOGUE }

                STAGE_REALM_CROSSING ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "The realm of the Fisher King is not quite of this reality. It is of a reality very close to ours though... Where it is easiest to cross, THAT is a point of realm crossing.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Hello deary.").also { stage++ }
                1 -> playerl(FaceAnim.NEUTRAL, "Um... hello.").also { stage = END_DIALOGUE }
            }
        }
    }
}
