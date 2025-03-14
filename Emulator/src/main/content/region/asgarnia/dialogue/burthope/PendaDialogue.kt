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
 * Represents the Penda dialogue.
 */
@Initializable
class PendaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val random = RandomFunction.random(1, 5)
        if (isQuestComplete(player!!, Quests.DEATH_PLATEAU)) {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage = random }
                1 -> npcl(FaceAnim.HAPPY, "I heard about what you did, thank you!").also { stage = END_DIALOGUE }
                2 ->
                    npcl(FaceAnim.WORRIED, "The White Knights are still going to take over.").also {
                        stage =
                            END_DIALOGUE
                    }
                3 ->
                    npcl(FaceAnim.PANICKED, "I hear the Imperial Guard are preparing an attack!").also {
                        stage =
                            END_DIALOGUE
                    }
                4 -> npcl(FaceAnim.HAPPY, "Thank you so much!").also { stage = END_DIALOGUE }
                5 -> npcl(FaceAnim.HAPPY, "Surely we are safe now!").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage = random }
                1 -> npcl(FaceAnim.ANGRY, "Let me at 'em!").also { stage = END_DIALOGUE }
                2 -> npcl(FaceAnim.ANGRY, "Trolls? Schmolls!").also { stage = END_DIALOGUE }
                3 -> npcl(FaceAnim.PANICKED, "The trolls are coming!").also { stage = END_DIALOGUE }
                4 -> npcl(FaceAnim.PANICKED, "The trolls took my baby son!").also { stage = END_DIALOGUE }
                5 -> npcl(FaceAnim.PANICKED, "Trolls!").also { stage = END_DIALOGUE }
                6 -> npcl(FaceAnim.FRIENDLY, "Hello stranger.").also { stage = END_DIALOGUE }
                7 -> npcl(FaceAnim.ANGRY, "Go away!").also { stage = END_DIALOGUE }
                8 -> npcl(FaceAnim.PANICKED, "Run!").also { stage = END_DIALOGUE }
                9 -> npcl(FaceAnim.FRIENDLY, "Hi!").also { stage = END_DIALOGUE }
                10 -> npcl(FaceAnim.SAD, "The Imperial Guard can no longer protect us!").also { stage = END_DIALOGUE }
                11 -> npcl(FaceAnim.WORRIED, "The White Knights will soon have control!").also { stage = END_DIALOGUE }
                12 -> npcl(FaceAnim.FRIENDLY, "Welcome to Burthorpe!").also { stage++ }
                13 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PENDA_1087)
    }
}
