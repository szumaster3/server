package content.region.kandarin.quest.murder.dialogue

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.sendDialogue
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
class MaryDialogue(
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
                                            6
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
                                            6
                                    }
                                4 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Why'd you buy poison the other day?").also {
                                        stage =
                                            11
                                    }
                            }
                        }

                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Oh I don't know... Frank was acting kind of funny... After that big argument him and the Lord had the other day by the beehive... so I guess maybe him... but it's really scary to think someone here might have been responsible. I actually hope it was a burglar...",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.HALF_CRYING,
                            "I was with Hobbes and Louisa in the Kitchen helping to prepare Lord Sinclair's meal, and then when I took it to his study... I saw... oh, it was horrible... he was...",
                        ).also {
                            stage++
                        }
                    5 ->
                        sendDialogue(
                            player!!,
                            "She seems to be on the verge of crying. You decide not to push her anymore for details.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    6 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I don't really remember hearing anything out of the ordinary.",
                        ).also { stage++ }
                    7 -> playerl(FaceAnim.ASKING, "No sounds of a struggle then?").also { stage++ }
                    8 -> npcl(FaceAnim.NEUTRAL, "No, I don't remember hearing anything like that.").also { stage++ }
                    9 -> playerl(FaceAnim.ASKING, "How about the guard dog barking?").also { stage++ }
                    10 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Oh that horrible dog is always barking at noting but I don't think I did...",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    11 ->
                        npcl(
                            FaceAnim.HALF_WORRIED,
                            "I overheard Anna saying to Stanford that if he didn't do something about the state of his compost heap, she was going to.",
                        ).also {
                            stage++
                        }
                    12 ->
                        npcl(
                            FaceAnim.HALF_WORRIED,
                            "She really doesn't get on well with Stanford. I really have no idea why. You'd really have to ask her though.",
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
        return intArrayOf(NPCs.MARY_810)
    }
}
