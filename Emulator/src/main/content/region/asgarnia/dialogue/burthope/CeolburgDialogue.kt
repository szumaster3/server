package content.region.asgarnia.dialogue.burthope

import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Ceolburg dialogue.
 */
@Initializable
class CeolburgDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val random = RandomFunction.random(1, 3)
        if (isQuestComplete(player!!, Quests.DEATH_PLATEAU)) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage = random }
                1 -> npcl(FaceAnim.HAPPY, "I heard about what you did, thank you!").also { stage = END_DIALOGUE }
                2 -> npcl(FaceAnim.HAPPY, "Thank you so much!").also { stage = END_DIALOGUE }
                3 -> npcl(FaceAnim.HAPPY, "Surely we are safe now!").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage = random }
                1 -> npcl(FaceAnim.FRIENDLY, "Hello stranger.").also { stage = END_DIALOGUE }
                2 -> npcl(FaceAnim.FRIENDLY, "Hi!").also { stage = END_DIALOGUE }
                3 -> npcl(FaceAnim.FRIENDLY, "Welcome to Burthorpe!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CEOLBURG_1089)
    }
}
