package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.link.quest.Quest
import core.tools.END_DIALOGUE
import shared.consts.Quests

/**
 * Represents the Oracle dialogue.
 */
class OracleDialogue : DialogueFile() {

    private var quest: Quest? = null

    override fun handle(componentID: Int, buttonID: Int) {
        if (player == null) return

        quest = player!!.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        val stageId = quest?.getStage(player) ?: 0

        when (stageId) {
            20 -> when (stage) {
                0 -> player(FaceAnim.NEUTRAL, "I seek a piece of the map to the island of Crandor.").also { stage++ }
                1 -> npc(FaceAnim.NEUTRAL, "The map's behind a door below,", "but entering is rather tough.", "This is what you need to know:", "You must use the following stuff.").also { stage++ }
                2 -> npc(FaceAnim.NEUTRAL, "First, a drink used by a mage.", "Next, some worm string, changed to sheet.", "Then, a small crustacean cage.", "Last, a bowl that's not seen heat.").also { stage = END_DIALOGUE }
            }

            else -> when (stage) {
                0 -> player(FaceAnim.HALF_ASKING, "Can you impart your wise knowledge on me, O Oracle?").also { stage++ }
                1 -> npc(FaceAnim.NEUTRAL, "Don't judge a book by its cover - judge it on its", "grammar and punctuation.").also { stage = END_DIALOGUE }
            }
        }
    }
}
