package content.region.kandarin.ardougne.quest.hazeelcult.dialogue

import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ClausTheChefDialogue(player: Player? = null) : Dialogue(player) {
    

    override fun handle(componentID: Int, buttonID: Int, ): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)
        when {
            (questStage in 0..2) -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Sorry, can't stop to chat! You would be amazed at how many meals this family gets through daily!").also { stage = END_DIALOGUE }
                }
            }
            (questStage == 3) -> {
                when (stage) {
                    0 -> {
                        if (getAttribute(player, "hazeelcult:mahjarrat", true)) {
                            npcl(FaceAnim.FRIENDLY, "Hello there. Caught any of those weird cultists yet?").also { stage++ }
                        } else {
                            sendMessage(player, "They aren't interested in talking to you.").also { stage = END_DIALOGUE }
                        }
                    }
                    1 -> playerl(FaceAnim.NEUTRAL, "Afraid not.").also { stage++ }
                    2 -> npcl(FaceAnim.HALF_ASKING, "Well, keep at it!").also { stage = END_DIALOGUE }
                }
            }

            (questStage == 100) -> {
                when (stage) {
                    0 -> {
                        if (getAttribute(player, "hazeelcult:mahjarrat", true) && !getAttribute(player, "hazeelcult:carnillean", true)) {
                            playerl(FaceAnim.FRIENDLY, "Hey.").also { stage++ }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Hiya.").also { stage = 2 }
                        }
                    }
                    1 -> npcl(FaceAnim.NEUTRAL, "Oh, hello there. Sorry, but I can't really talk right now. Things haven't been great here recently, and I have a lot of work to do.").also { stage = END_DIALOGUE }
                    2 -> npcl(FaceAnim.HALF_ASKING, "Well hello there adventurer! Are we fit and well?").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "Yep, fine thanks.").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, "Glad to hear it.").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CLAUS_THE_CHEF_886)
}
