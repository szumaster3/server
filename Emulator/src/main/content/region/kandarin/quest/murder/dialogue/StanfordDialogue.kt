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
class StanfordDialogue(
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
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage = 3 }
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
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage = 3 }
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
                            "It was Anna. She is seriously unbalanced. She trashed the garden once then tried to blame it on me! I bet it was her. It's just the kind of thing she'd do! She really hates me and was arguing with Lord Sinclair about trashing the garden a few days ago.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Right here, by my little shed. It's very cosy to sit and think in.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    5 -> npcl(FaceAnim.NEUTRAL, "Not that I remember.").also { stage++ }
                    6 ->
                        playerl(
                            FaceAnim.ASKING,
                            "So no sounds of a struggle between Lord Sinclair and an intruder?",
                        ).also { stage++ }
                    7 -> npcl(FaceAnim.NEUTRAL, "Not to the best of my recollection.").also { stage++ }
                    8 -> playerl(FaceAnim.ASKING, "How about the guard dog barking?").also { stage++ }
                    9 -> npcl(FaceAnim.NEUTRAL, "Not that I can recall.").also { stage = END_DIALOGUE }
                    10 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well, Bob mentioned to me the other day he wanted to get rid of the bees in that hive over there. I think I saw him buying poison from that poison salesman the other day.",
                        ).also {
                            stage++
                        }
                    11 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I assume it was to sort out those bees. You'd really have to ask him though.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STANFORD_811)
    }
}
