package content.region.fremennik.rellekka.miniquest.shadow_maj.dialogue

import content.region.fremennik.rellekka.miniquest.shadow_maj.plugin.GeneralShadow
import core.api.isQuestComplete
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Quests

/**
 * Represents the Falador Scout dialogue.
 *
 * # Relations
 * - [GeneralShadow]
 */
@Initializable
class FaladorScoutDialogue(player: Player? = null, ) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        return when {
            !GeneralShadow.hasGhostlySet(player) -> {
                npc("Whoooo wooo Whooooooooo")
                stage = END_DIALOGUE
                true
            }
            GeneralShadow.getShadowProgress(player) == 1 -> {
                player("Hello there! General Khazard sent me.")
                true
            }
            GeneralShadow.isComplete(player) -> {
                player("Hello again.")
                stage = 100
                true
            }
            else -> {
                sendDialogue(player, "The Scout is too busy to talk.")
                stage = END_DIALOGUE
                true
            }
        }
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> npc("Finally, news! What message does he send?").also { stage++ }
            1 -> player("Err, it was something about planets.").also { stage++ }
            2 -> npc("Well, spit it out! I can't wait here all day.").also { stage++ }
            3 -> player("Khazard says: 'The planets are nearly alignment; we", "will meet in the place of half light and ice soon. Beware", "of the others, for though they are weak and few, they", "are cunning.'").also { stage++ }
            4 -> npc("Good. I long for the slaughter. These local squabbles", "and politics bore me. I wonder if the others have begun", "to move as well?").also { stage++ }
            5 -> player("The other what? Who are you slaughtering?").also { stage++ }
            6 -> npc("Ah, not for your ears, messenger!").also { stage++ }
            7 -> player("So how's your mission going. Enjoying the Draynor", "woods?").also { stage++ }
            8 -> if (isQuestComplete(player, Quests.VAMPIRE_SLAYER)) {
                npc("There is talk in the village that the Count of Draynor", "Manor has been Slain by some meddling adventurer. I will", "need to enter the manor to verify this.").also { stage++ }
            } else {
                player("Have you been in touch with any of the other scouts?").also { stage = 10 }
            }
            9 -> npc("Other than that, nothing of interest.").also { stage = 12 }
            10 -> npc(FaceAnim.FRIENDLY, "I'm too busy scouting to be in touch. One headed this", "direction with me seeking the place of heat and sand, another sought a place of moisture and growth and the last sought gnomes.").also { stage++ }
            11 -> npc(FaceAnim.FRIENDLY, "One headed this direction with me seeking the place of heat and sand, another sought a place of moisture and growth and the last sought gnomes.").also { stage++ }
            12 -> {
                end()
                GeneralShadow.setShadowProgress(player, 2)
            }
            100 -> npc("I can't speak to you; I must continue on my mission.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(5576)
}
