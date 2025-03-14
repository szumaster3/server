package content.region.kandarin.quest.murder.dialogue

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
class ElizabethDialogue(
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
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questStage = getQuestStage(player!!, Quests.MURDER_MYSTERY)
        when (questStage) {
            in 1..4 ->
                when (stage) {
                    0 -> npcl(FaceAnim.NEUTRAL, "What's so important you need to bother me with then?").also { stage++ }
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
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage =
                                            4
                                    }
                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 7 }
                            }
                        } else {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage =
                                            4
                                    }
                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 7 }
                                4 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Why'd you buy poison the other day?").also {
                                        stage =
                                            8
                                    }
                            }
                        }

                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Could have been anyone. The old man was an idiot. He's been asking for it for years.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 -> npcl(FaceAnim.NEUTRAL, "I was out.").also { stage++ }
                    5 -> playerl(FaceAnim.ASKING, "Care to be any more specific?").also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Not really. I don't have to justify myself to the likes of you, you know. I know the King personally you know. Now are we finished here?",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    7 ->
                        npcl(
                            FaceAnim.THINKING,
                            "It's some thread. You're not very good at this whole investigation thing are you?",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    8 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "There was a nest of mosquitos under the fountain in the garden, which I killed with poison the other day. You can see for yourself if you're capable of managing that, which I somehow doubt.",
                        ).also {
                            stage++
                        }
                    9 -> playerl(FaceAnim.NEUTRAL, "I hate mosquitos!").also { stage++ }
                    10 -> npcl(FaceAnim.NEUTRAL, "Doesn't everyone?").also { stage++ }
                    11 -> {
                        end()
                        setQuestStage(player!!, Quests.MURDER_MYSTERY, 4)
                    }
                }

            100 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Apparently you aren't as stupid as you look.",
                        ).also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELIZABETH_818)
    }
}
