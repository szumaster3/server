package content.region.fremennik.quest.horror.handlers.bookcase

import content.data.QuestItem
import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import org.rs.consts.Items
import org.rs.consts.Quests

class LighthouseBookcase : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> handleRequirements()
            1 -> showOptions()
            2 -> handleSelection(buttonID)
        }
    }

    private fun handleRequirements() {
        if (isQuestComplete(player!!, Quests.HORROR_FROM_THE_DEEP)) {
            openDialogue(player!!, QuestItem(Items.MANUAL_3847))
        } else {
            sendDialogue(player!!, "There are three books here that look important... What would you like to do?")
            stage++
        }
    }

    private fun showOptions() {
        options(
            "Take the Lighthouse Manual",
            "Take the ancient Diary",
            "Take Jossik's Journal",
            "Take all three books",
        )
        stage++
    }

    private fun handleSelection(buttonID: Int) {
        val bookID =
            when (buttonID) {
                1 -> listOf(Items.MANUAL_3847)
                2 -> listOf(Items.DIARY_3846)
                3 -> listOf(Items.JOURNAL_3845)
                4 -> listOf(Items.MANUAL_3847, Items.DIARY_3846, Items.JOURNAL_3845)
                else -> return
            }

        if (freeSlots(player!!) < bookID.size) {
            sendDialogue(player!!, "You do not have enough room to take ${if (bookID.size > 1) "all three" else "that"}.")
        } else {
            bookID.forEach { addItem(player!!, it, 1) }
        }
        end()
    }
}
