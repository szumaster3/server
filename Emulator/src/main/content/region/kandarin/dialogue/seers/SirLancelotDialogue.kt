package content.region.kandarin.dialogue.seers

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SirLancelotDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val STAGE_FULL_OF_YOURSELF = 2
    val STAGE_GET_MERLIN_OUT = 11

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Greetings! I am Sir Lancelot, the greatest Knight in the land! What do you want?",
                    ).also {
                        if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 10 ||
                            getQuestStage(player, Quests.MERLINS_CRYSTAL) >= 30
                        ) {
                            stage = 10
                        } else if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 20) {
                            stage = 20
                        } else if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 0) {
                            stage++
                        }
                    }

                1 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "You're a little full of yourself aren't you?", STAGE_FULL_OF_YOURSELF),
                        Topic(FaceAnim.NEUTRAL, "I seek a quest!", 5),
                    )

                STAGE_FULL_OF_YOURSELF ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I have every right to be proud of myself.",
                    ).also { stage++ }

                3 -> npcl(FaceAnim.NEUTRAL, "My prowess in battle is world renowned!").also { stage = END_DIALOGUE }

                5 -> npcl(FaceAnim.NEUTRAL, "Leave questing to the professionals.").also { stage++ }
                6 -> npcl(FaceAnim.NEUTRAL, "Such as myself.").also { stage = END_DIALOGUE }

                10 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "I want to get Merlin out of the crystal.", STAGE_GET_MERLIN_OUT),
                        Topic(FaceAnim.NEUTRAL, "You're a little full of yourself aren't you?", STAGE_FULL_OF_YOURSELF),
                    )

                STAGE_GET_MERLIN_OUT ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Well, if the Knights of the Round Table can't manage it, I can't see how a commoner like you could succeed where we have failed.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }

                20 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "I want to get Merlin out of the crystal.", STAGE_GET_MERLIN_OUT),
                        Topic(FaceAnim.NEUTRAL, "You're a little full of yourself aren't you?", STAGE_FULL_OF_YOURSELF),
                        Topic(FaceAnim.NEUTRAL, "Any ideas on how to get into Morgan Le Faye's stronghold?", 21),
                    )

                21 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "That stronghold is built in a strong defensive position.",
                    ).also { stage++ }
                22 -> npcl(FaceAnim.NEUTRAL, "It's on a big rock sticking out into the sea.").also { stage++ }
                23 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "There are two ways in that I know of, the large heavy front doors, and the sea entrance, only penetrable by boat.",
                    ).also {
                        stage++
                    }

                24 -> {
                    npcl(FaceAnim.NEUTRAL, "They take all their deliveries by boat.").also {
                        if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 20) {
                            setQuestStage(player!!, Quests.MERLINS_CRYSTAL, 30)
                        }
                        stage = END_DIALOGUE
                    }
                }
            }
        } else {
            when (stage) {
                0 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Greetings! I am Sir Lancelot, the greatest Knight in the land! What do you want?",
                    ).also {
                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 ||
                            isQuestComplete(player!!, Quests.HOLY_GRAIL)
                        ) {
                            stage = 1
                        } else {
                            stage = 10
                        }
                    }

                1 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Hmmm. I heard you freed Merlin. Either you're better than you look or you got lucky. I think the latter.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                10 -> playerl(FaceAnim.FRIENDLY, "I am questing for the Holy Grail.").also { stage++ }
                11 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "The Grail? Ha! Frankly, little man, you're not in that league.",
                    ).also { stage++ }
                12 -> playerl(FaceAnim.FRIENDLY, "Why do you say that?").also { stage++ }
                13 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "You got lucky with freeing Merlin but there's no way a puny wannabe like you is going to find the Holy Grail where so many others have failed.",
                    ).also {
                        stage++
                    }
                14 -> playerl(FaceAnim.ANGRY, "We'll see about that.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirLancelotDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_LANCELOT_239)
    }
}
