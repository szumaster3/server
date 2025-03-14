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
class DonovanDialogue(
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
                    0 -> npcl(FaceAnim.NEUTRAL, "How can I help?").also { stage++ }
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
                                        stage =
                                            4
                                    }
                                3 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Did you hear any suspicious noises at all?").also {
                                        stage =
                                            5
                                    }
                            }
                        } else {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage =
                                            4
                                    }
                                3 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Did you hear any suspicious noises at all?").also {
                                        stage =
                                            5
                                    }
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
                            "Oh... I really couldn't say. I wouldn't really want to point any fingers at anybody. If I had to make a guess I'd have to say it was probably Bob though. I saw him arguing with Lord Sinclair about some missing silverware from the Kitchen. It was a very heated argument.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Me? I was sound asleep here in the servants Quarters. It's very hard work as a handyman around here. There's always something to do!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    5 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "No. Can I go yet? Your face irritates me.",
                        ).also { stage = END_DIALOGUE }
                    6 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well, I do know Frank bought some poison recently to clean the family crest that's outside.",
                        ).also {
                            stage++
                        }
                    7 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "It's very old and rusty, and I couldn't clean it myself, so he said he would buy some cleaner and clean it himself. He probably just got some from that Poison Salesman who came to the door the other day...",
                        ).also {
                            stage++
                        }
                    8 -> npcl(FaceAnim.NEUTRAL, "You'd really have to ask him though.").also { stage++ }
                    9 -> {
                        end()
                        setQuestStage(player!!, Quests.MURDER_MYSTERY, 4)
                    }
                }
            100 ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.FRIENDLY, "Thank you for all your help in solving the murder.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONOVAN_THE_FAMILY_HANDYMAN_806)
    }
}
