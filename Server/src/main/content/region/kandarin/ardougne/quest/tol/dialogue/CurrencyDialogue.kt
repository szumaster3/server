package content.region.kandarin.ardougne.quest.tol.dialogue

import content.data.GameAttributes
import core.api.getAttribute
import core.api.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Currency dialogue.
 */
@Initializable
class CurrencyDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.TOWER_OF_LIFE)) {
            in 0..2 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "This is quite the tower.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Yeah, it cost a fair bit.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "How much?").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Well, I had to 'alch' a fair few belongings of my fellow alchemists to afford it.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "I bet they love you for that.").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Everyone loves someone that can create money.").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "I don't.").also { stage = END_DIALOGUE }
            }

            3 -> if (getAttribute(player, GameAttributes.TOL_TOWER_ACCESS, 0) == 1) {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi. Sorry, but why do they call you 'Currency'?").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "That's down to my interest in all things of monetary value.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "You must be pretty rich then.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "I wish. And I was, up until recently.").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "What happened?").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "This tower. Set me back a fair few million.").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "I'll bet! It pleases you, then, to know that I have just become a builder and have been granted permission to go mess around inside.").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "This is not a good day for me.").also { stage = END_DIALOGUE }
                }
            } else {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi, Currency. Got any spare cash?").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Nope; you?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "No, do you?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Afraid not. You?").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "I'm going to stop before this gets violent.").also { stage = END_DIALOGUE }
                }
            }

            6 -> if (getAttribute(player, GameAttributes.TOL_CUTSCENE, false)) {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "That is one very sad creature.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "It'll be alright: it just needs a pat on the back.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Why am I suddenly glad that you're not responsible for sorting out this mess?").also { stage = END_DIALOGUE }
                }
            }

            8 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "The Homunculus is a bit better off now.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Excellent. So we can go back in.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Not just yet.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CURRENCY_THE_ALCHEMIST_5587)
}
