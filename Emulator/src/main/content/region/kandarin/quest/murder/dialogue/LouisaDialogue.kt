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
class LouisaDialogue(
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
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)) {
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
                                            10
                                    }
                            }
                        }

                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Elizabeth. Her father confronted her about her constant petty thieving, and was devastated to find she had stolen a silver needle which had meant a lot to him. You could hear their argument from Lumbridge!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I was right here with Hobbes and Mary. You can't suspect me surely!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    5 -> npcl(FaceAnim.ASKING, "Suspicious? What do you mean suspicious?").also { stage++ }
                    6 ->
                        playerl(
                            FaceAnim.ASKING,
                            "Any sounds of a struggle with an intruder for example?",
                        ).also { stage++ }
                    7 -> npcl(FaceAnim.ASKING, "No, I'm sure I don't recall any such thing.").also { stage++ }
                    8 -> playerl(FaceAnim.ASKING, "How about the guard dog barking at an intruder?").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.HALF_WORRIED,
                            "No, I didn't. If you don't have anything else to ask can You go and leave me alone now? I have a lot of cooking to do for this evening.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    10 ->
                        npcl(
                            FaceAnim.HALF_WORRIED,
                            "I told Carol to buy some from that strange poison salesman and clean the drains before they began to smell any worse.",
                        ).also {
                            stage++
                        }
                    11 ->
                        npcl(
                            FaceAnim.HALF_WORRIED,
                            "She was the one who blocked them in the first place with a load of beans that she bought for some reason.",
                        ).also {
                            stage++
                        }
                    12 ->
                        npcl(
                            FaceAnim.HALF_WORRIED,
                            "There were far too many to eat, and they were almost rotten when she bought them anyway! You'd really have to ask her though.",
                        ).also {
                            stage =
                                END_DIALOGUE
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
        return intArrayOf(NPCs.LOUISA_809)
    }
}
