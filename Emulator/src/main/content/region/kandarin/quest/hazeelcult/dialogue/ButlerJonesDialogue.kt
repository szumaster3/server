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
class ButlerJonesDialogue(
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
                    1 -> npcl(FaceAnim.FRIENDLY, "Hello. How are you today?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Good thank you, and yourself?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Very well, thank you.").also { stage = END_DIALOGUE }
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
                            FaceAnim.NEUTRAL,
                            "Hello, adventurer. Such a terrible shame about Scruffy. I wonder if the family will ever fully recover.",
                        ).also {
                            stage++
                        }
                    2 -> npcl(FaceAnim.FRIENDLY, "Anyway, I hear your quest is going well.").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "Really?").also { stage++ }
                    4 -> npcl(FaceAnim.ANNOYED, "Oh yes. Do keep up the good work.").also { stage = END_DIALOGUE }
                }

            (questStage == 100) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, HazeelCultListener.MAHJARRAT, true) &&
                            !getAttribute(player, HazeelCultListener.CARNILLEAN, true)
                        ) {
                            playerl(FaceAnim.FRIENDLY, "Hello stranger.").also { stage = 1 }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage = 6 }
                        }
                    }
                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "It's an honour to be in your presence again, adventurer. I hope things are well?",
                        ).also {
                            stage++
                        }
                    2 -> playerl(FaceAnim.FRIENDLY, "Not bad, thanks. Yourself?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Unfortunately, I am still forced to deal with this insufferable family. Many generations have passed, but they are still the enemy. As such, they must be kept a close eye on.",
                        ).also {
                            stage++
                        }
                    4 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Still, I have no doubt that the time will soon come for me to leave this place. Our lord will certainly have need of me elsewhere once his current work is complete.",
                        ).also {
                            stage++
                        }
                    5 -> playerl(FaceAnim.FRIENDLY, "I see. Well good luck with it all.").also { stage = END_DIALOGUE }
                    6 -> npcl(FaceAnim.ANNOYED, "Why hello there.").also { stage++ }
                    7 -> playerl(FaceAnim.FRIENDLY, "I take it you're the new butler...?").also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "That's right. I hear that they had some problems with the last one.",
                        ).also { stage++ }
                    9 -> playerl(FaceAnim.FRIENDLY, "Yes, you could say that...").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BUTLER_JONES_890)
    }
}
