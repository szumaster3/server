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
class FrankDialogue(
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
                    0 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Good for you. Now what do you want? ...And can you spare me any money? I'm a little short...",
                        ).also {
                            stage++
                        }
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
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage = 3 }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage =
                                            4
                                    }
                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 5 }
                            }
                        } else {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage = 3 }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage =
                                            4
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
                            "I don't know. You don't know how long it takes an inheritance to come through do you? I could really use that money pretty soon...",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I don't know, somewhere around here probably. Could you spare me a few coins? I'll be able to pay you back double tomorrow it's just there's this poker night tonight in town...",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    5 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "It looks like thread to me, but I'm not exactly an expert. Is it worth something? Can I have it? Actually, can you spare me a few gold?",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    6 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Would you like to buy some? I'm kind of strapped for cash right now, I'll sell it to you cheap, it's hardly been used at all.",
                        ).also {
                            stage++
                        }
                    7 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "I just used a bit to clean that family crest outside up a bit. Do you think I can get much money For the family crest, actually? It's cleaned up a bit now.",
                        ).also {
                            stage++
                        }
                    8 -> {
                        end()
                        setQuestStage(player!!, Quests.MURDER_MYSTERY, 4)
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
        return intArrayOf(NPCs.FRANK_819)
    }
}
