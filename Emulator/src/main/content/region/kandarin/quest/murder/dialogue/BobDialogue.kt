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
class BobDialogue(
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
        val threadColor = MurderMysteryUtils.getGuiltyColor(player)
        val questStage = getQuestStage(player!!, Quests.MURDER_MYSTERY)
        when (questStage) {
            in 1..4 ->
                when (stage) {
                    0 -> npcl(FaceAnim.HALF_GUILTY, "I suppose I had better talk to you then.").also { stage++ }
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
                            FaceAnim.ANGRY,
                            "I don't really care as long as no one thinks it's me. Maybe it was that strange poison seller who headed towards the seers village.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    4 -> npcl(FaceAnim.NEUTRAL, "I was walking by myself in the garden.").also { stage++ }
                    5 -> playerl(FaceAnim.SUSPICIOUS, "And can anyone vouch for that?").also { stage++ }
                    6 -> npcl(FaceAnim.ANNOYED, "No. But I was.").also { stage = END_DIALOGUE }
                    7 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "It's some $threadColor thread. I suppose you think that's some kind of clue? It looks like the material my trousers are made of.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    8 -> npcl(FaceAnim.HALF_GUILTY, "What's it to you anyway?").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.ANGRY,
                            "If you absolutely must know, we had a problem with the beehive in the garden, and as all of our servants are so pathetically useless, I decided I would deal with it myself. So I did.",
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
                        npcl(FaceAnim.HALF_GUILTY, "Apparently you aren't as stupid as you look.").also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BOB_815)
}
