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
class ClausTheChefDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questName = "Hazeel Cult"
        val questStage = getQuestStage(player!!, questName)

        when {
            (questStage in 0..2) ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Sorry, can't stop to chat! You would be amazed at how many meals this family gets through daily!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            (questStage == 3) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, HazeelCultListener.MAHJARRAT, true)) {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Hello there. Caught any of those weird cultists yet?",
                            ).also { stage++ }
                        } else {
                            sendMessage(
                                player,
                                "They aren't interested in talking to you.",
                            ).also { stage = END_DIALOGUE }
                        }
                    }

                    1 -> playerl(FaceAnim.NEUTRAL, "Afraid not.").also { stage++ }
                    2 -> npcl(FaceAnim.HALF_ASKING, "Well, keep at it!").also { stage = END_DIALOGUE }
                }

            (questStage == 100) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, HazeelCultListener.MAHJARRAT, true) &&
                            !getAttribute(player, HazeelCultListener.CARNILLEAN, true)
                        ) {
                            playerl(FaceAnim.FRIENDLY, "Hey.").also { stage = 1 }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Hiya.").also { stage = 4 }
                        }
                    }

                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Oh, hello there. Sorry, but I can't really talk right now. Things haven't been great here recently, and I have a lot of work to do.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    4 ->
                        npcl(
                            FaceAnim.HALF_ASKING,
                            "Well hello there adventurer! Are we fit and well?",
                        ).also { stage++ }
                    5 -> playerl(FaceAnim.FRIENDLY, "Yep, fine thanks.").also { stage++ }
                    6 -> npcl(FaceAnim.FRIENDLY, "Glad to hear it.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CLAUS_THE_CHEF_886)
    }
}
