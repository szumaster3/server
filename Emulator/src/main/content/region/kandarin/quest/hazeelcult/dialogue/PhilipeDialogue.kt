package content.region.kandarin.quest.hazeelcult.dialogue

import content.region.kandarin.quest.hazeelcult.handlers.HazeelCultListener
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PhilipeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questName = "Hazeel Cult"
        val questStage = getQuestStage(player!!, questName)

        when {
            (questStage == 0) ->
                when (stage) {
                    0 -> npcl(FaceAnim.FRIENDLY, "Hello, how are you today?").also { stage++ }
                    1 -> playerl(FaceAnim.FRIENDLY, "Good thank you. And yourself?").also { stage++ }
                    2 -> npcl(FaceAnim.FRIENDLY, "Fine and dandy.").also { stage = END_DIALOGUE }
                }

            (questStage in 1..2) ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Mommy said you're here to kill all the nasty people that keep breaking in.",
                        ).also {
                            stage++
                        }
                    1 -> playerl(FaceAnim.FRIENDLY, "Something like that.").also { stage++ }
                    2 -> npcl(FaceAnim.FRIENDLY, "Can I watch?").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "No!").also { stage = END_DIALOGUE }
                }

            (questStage == 3) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, HazeelCultListener.MAHJARRAT, true) &&
                            !getAttribute(player, HazeelCultListener.CARNILLEAN, true)
                        ) {
                            playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                        } else {
                            sendMessage(
                                player,
                                "They aren't interested in talking to you.",
                            ).also { stage = END_DIALOGUE }
                        }
                    }
                    1 ->
                        npcl(
                            FaceAnim.SAD,
                            "Someone killed Scruffy. I liked Scruffy. He never told me off.",
                        ).also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Yeah... it's a real shame.").also { stage++ }
                    3 -> npcl(FaceAnim.SAD, "I want my mommy.").also { stage = END_DIALOGUE }
                }

            (questStage == 100) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, HazeelCultListener.MAHJARRAT, true) &&
                            !getAttribute(player, HazeelCultListener.CARNILLEAN, true)
                        ) {
                            playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage = 1 }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Hello youngster.").also { stage = 4 }
                        }
                    }
                    1 -> npcl(FaceAnim.NEUTRAL, "What have you brought me? I want some more toys!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I'm afraid I don't have any toys.").also { stage++ }
                    3 -> npcl(FaceAnim.ANNOYED, "Toys! I want toys!").also { stage = END_DIALOGUE }
                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Daddy says you don't like Mr. Jones. Mr. Jones is nice. He brings me toys and sweets.",
                        ).also {
                            stage++
                        }
                    5 -> playerl(FaceAnim.FRIENDLY, "Jones is a bad man, Philipe.").also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You're a bad " + (if (player.isMale) "man" else "lady") + " I don't like you!",
                        ).also { stage++ }
                    7 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I'll try and console myself about that disappointment somehow.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PHILIPE_CARNILLEAN_888)
    }
}
