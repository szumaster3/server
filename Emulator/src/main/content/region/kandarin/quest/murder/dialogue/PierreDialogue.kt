package content.region.kandarin.quest.murder.dialogue

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PierreDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.MURDER_MYSTERY)) {
            playerl(FaceAnim.FRIENDLY, "I'm here to help the guards with their investigation.")
        } else {
            sendMessage(player!!, "He is ignoring you.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questStage = getQuestStage(player!!, Quests.MURDER_MYSTERY)
        when (questStage) {
            in 1..4 ->
                when (stage) {
                    0 -> npcl(FaceAnim.FRIENDLY, "How can I help?").also { stage++ }
                    1 ->
                        if (getQuestStage(player, Quests.MURDER_MYSTERY) == 3) {
                            options(
                                "Who do you think is responsible?",
                                "Where were you when the murder happened?",
                                "Did you hear any suspicious noises at all?",
                                "Why'd you buy poison the other day?",
                            ).also { stage++ }
                        } else {
                            options(
                                "Who do you think is responsible?",
                                "Where were you when the murder happened?",
                                "Did you hear any suspicious noises at all?",
                            ).also { stage++ }
                        }

                    2 ->
                        if (getQuestStage(player, Quests.MURDER_MYSTERY) == 3) {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage = 4
                                    }

                                3 ->
                                    playerl(
                                        FaceAnim.SUSPICIOUS,
                                        "Did you hear any suspicious noises at all?",
                                    ).also { stage = 5 }
                            }
                        } else {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage = 4
                                    }

                                3 ->
                                    playerl(
                                        FaceAnim.SUSPICIOUS,
                                        "Did you hear any suspicious noises at all?",
                                    ).also { stage = 5 }

                                4 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Why'd you buy poison the other day?").also {
                                        stage =
                                            10
                                    }
                            }
                        }

                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Honestly? I think it was Carol. I saw her in a huge argument with Lord Sinclair in the library the other day. It was something to do with stolen books. She definitely seemed upset enough to have done it afterwards.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I was in town at the inn. When I got back the house was swarming with guards who told me what had happened. Sorry.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    5 -> npcl(FaceAnim.NEUTRAL, "Well, like what?").also { stage++ }
                    6 -> playerl(FaceAnim.ASKING, "Any sounds of a struggle with Lord Sinclair?").also { stage++ }
                    7 -> npcl(FaceAnim.NEUTRAL, "No, I don't remember hearing anything like that.").also { stage++ }
                    8 -> playerl(FaceAnim.ASKING, "How about the guard dog barking?").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I hear him bark all the time. It's one of his favourite things to do. I can't say I did the night of the murder though as I wasn't close enough to hear either way.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    10 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well, I know David said that he was going to do something about the spiders' nest that's between the two servants' quarters upstairs.",
                        ).also {
                            stage++
                        }
                    11 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "He made a big deal about it to Mary the Maid, calling her useless and incompetent. I felt quite sorry for her actually. You'd really have to ask him though.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            100 ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.FRIENDLY, "Thank you for all your help in solving the murder.").also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PIERRE_807)
    }
}
