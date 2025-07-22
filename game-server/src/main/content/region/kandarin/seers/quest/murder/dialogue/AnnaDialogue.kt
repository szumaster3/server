package content.region.kandarin.seers.quest.murder.dialogue

import content.region.kandarin.seers.quest.murder.plugin.MurderMysteryUtils
import core.api.inInventory
import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.setQuestStage
import core.api.sendItemDialogue
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AnnaDialogue(player: Player? = null) : Dialogue(player) {
    
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
        val threadColor = MurderMysteryUtils.getGuiltyColor(player)
        val questStage = getQuestStage(player!!, Quests.MURDER_MYSTERY)
        when (questStage) {
            in 1..4 ->
                when (stage) {
                    0 -> npcl(FaceAnim.ANNOYED, "Oh really? What do you want to know then?").also { stage++ }
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
                                            6
                                    }
                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 7 }
                            }
                        } else {
                            when (buttonID) {
                                1 -> playerl(FaceAnim.SUSPICIOUS, "Who do you think is responsible?").also { stage++ }
                                2 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Where were you when the murder happened?").also {
                                        stage =
                                            6
                                    }
                                3 -> playerl(FaceAnim.SUSPICIOUS, "Do you recognise this thread?").also { stage = 7 }
                                4 ->
                                    playerl(FaceAnim.SUSPICIOUS, "Why'd you buy poison the other day?").also {
                                        stage =
                                            9
                                    }
                            }
                        }

                    3 -> npcl(FaceAnim.ANNOYED, "It was clearly an intruder.").also { stage++ }
                    4 -> playerl(FaceAnim.SUSPICIOUS, "Well, I don't think it was.").also { stage++ }
                    5 -> npcl(FaceAnim.ANNOYED, "It was one of our lazy servants then.").also { stage = END_DIALOGUE }
                    6 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "In the library. No one else was there so you'll just have to take my word for it.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    7 -> {
                        if (inInventory(player!!, Items.CRIMINALS_THREAD_1809)) {
                            sendItemDialogue(
                                player!!,
                                Items.CRIMINALS_THREAD_1809,
                                "You show Anna the thread from the study.",
                            ).also { stage++ }
                        } else {
                            npcl(FaceAnim.ANNOYED, "Not really, no. Thread is fairly common.").also {
                                stage =
                                    END_DIALOGUE
                            }
                        }
                    }

                    8 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "It's some $threadColor thread. It's not exactly uncommon is it? My trousers are made of the same material.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    9 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "That useless Gardener Stanford has let his compost heap fester. It's an eyesore to the garden! So I bought some poison from a travelling salesman so that I could kill off some of the wildlife living in it.",
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

    override fun getIds(): IntArray = intArrayOf(NPCs.ANNA_814)
}
