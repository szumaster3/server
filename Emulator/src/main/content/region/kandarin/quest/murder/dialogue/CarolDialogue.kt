package content.region.kandarin.quest.murder.dialogue

import content.region.kandarin.quest.murder.handlers.MurderMysteryUtils
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
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
class CarolDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.MURDER_MYSTERY)) {
            playerl(FaceAnim.FRIENDLY, "I'm here to help the guards with their investigation.")
        } else {
            sendMessage(player!!, "She is ignoring you.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val threadColor = MurderMysteryUtils.getGuiltyColor(player)
        val questStage = getQuestStage(player!!, Quests.MURDER_MYSTERY)
        when (questStage) {
            in 1..4 ->
                when (stage) {
                    0 -> npcl(FaceAnim.ANNOYED, "Well, ask what you want to know then.").also { stage++ }
                    1 ->
                        if (getQuestStage(player, Quests.MURDER_MYSTERY) == 3) {
                            options(
                                "Who do you think is responsible?",
                                "Where were you when the murder happened?",
                                "Do you recognise this thread?",
                                "Why'd you buy poison the other day?",
                            ).also { stage++ }
                        } else {
                            options(
                                "Who do you think is responsible?",
                                "Where were you when the murder happened?",
                                "Do you recognise this thread?",
                            ).also { stage++ }
                        }

                    2 ->
                        if (getQuestStage(player, Quests.MURDER_MYSTERY) == 3) {
                            when (buttonId) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage = 4
                                    }

                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 5 }
                            }
                        } else {
                            when (buttonId) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage = 4
                                    }

                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 5 }
                                4 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Why'd you buy poison the other day?").also {
                                        stage =
                                            6
                                    }
                            }
                        }

                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I don't know. I think it's very convenient that you have arrived here so soon after it happened. Maybe it was you.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Why? Are you accusing me of something? You seem to have a very high opinion of yourself. I was in my room if you must know, alone.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    5 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "It's some $threadColor thread... it kind of looks like the Same material as my trousers. But obviously it's not.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    6 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "I don't see what on earth it has to do with you, but the drain outside was blocked, and as nobody else here has the intelligence to even unblock a simple drain I felt I had to do it myself.",
                        ).also {
                            stage++
                        }

                    7 -> {
                        end()
                        setQuestStage(player!!, Quests.MURDER_MYSTERY, 4)
                    }
                }

            100 ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.NEUTRAL, "Apparently you aren't as stupid as you look.").also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAROL_816)
    }
}
