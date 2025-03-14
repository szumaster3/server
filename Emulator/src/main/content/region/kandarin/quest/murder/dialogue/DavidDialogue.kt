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
class DavidDialogue(
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
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)) {
            in 1..4 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "And? Make this quick. I have better things to do than be interrogated by halfwits all day.",
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
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage = 4
                                    }

                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 5 }
                            }
                        } else {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage = 4
                                    }

                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 5 }
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
                            "I don't really know or care. Frankly, the old man deserved to die. There was a suspicious red headed man who came to the house the other day selling poison now I think about it. Last I saw he was headed towards the tavern in the Seers village.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "That is none of your business. Are we finished now, or are you just going to stand there irritating me with your idiotic questions all day?",
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
                        playerl(
                            FaceAnim.SUSPICIOUS,
                            "So you didn't hear any sounds of a struggle or any barking from the guard dog next to his study window?",
                        ).also {
                            stage++
                        }
                    7 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Now you mention it, no. It is odd I didn't hear anything like that. But I do sleep very soundly as I said and wouldn't necessarily have heard it if there was any such noise.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    8 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "There was a nest of spiders upstairs between the two servants' quarters. Obviously I had to kill them before our pathetic servants whined at my father some more.",
                        ).also {
                            stage++
                        }
                    9 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Honestly, it's like they expect to be treated like royalty! If I had my way I would fire the whole workshy lot of them!",
                        ).also {
                            stage++
                        }
                    10 -> {
                        end()
                        setQuestStage(player!!, Quests.MURDER_MYSTERY, 4)
                    }
                }

            100 ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.FRIENDLY, "Apparently you aren't as stupid as you look.").also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAVID_817)
    }
}
