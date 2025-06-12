package content.region.kandarin.ardougne.east.plugin

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class NecromancerTowerPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles searching the bookcase at Necromaner Tower.
         */

        on(Scenery.BOOKCASE_6894, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.NECROMANCY_BOOK_4837)) {
                sendMessage(player, "You search the bookcase...")
                sendMessageWithDelay(player, "You find nothing of interest to you.", 1)
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You need at least one free inventory space to take from the shelves.")
                return@on true
            }

            sendMessage(player, "...and find a book named 'On loan to the wizards' Guild...to be returned'.")
            addItem(player, Items.NECROMANCY_BOOK_4837)
            return@on true
        }

        /*
         * Handles using the Torn pages on Irwin NPCs.
         */

        onUseWith(IntType.NPC, Items.TORN_PAGE_4809, NPCs.IRWIN_FEASELBAUM_2066) { player, _, _ ->
            if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) > 1) {
                openDialogue(player, IrwinFeaselbaumAboutTornPagesDialogue())
            }
            return@onUseWith true
        }

        /*
         * Handles using the Necromancy book on Irwin NPCs.
         */

        onUseWith(IntType.NPC, Items.NECROMANCY_BOOK_4837, NPCs.IRWIN_FEASELBAUM_2066) { player, _, _ ->
            if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) > 1) {
                openDialogue(player, IrwinFeaselbaumAboutNecromancyBookDialogue())
            }
            return@onUseWith true
        }
    }
}

private class IrwinFeaselbaumAboutTornPagesDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.IRWIN_FEASELBAUM_2066)
        when (stage) {
            0 -> playerl(
                FaceAnim.HALF_ASKING,
                "I found this torn page, do you know anything about it?",
            ).also { stage++ }

            1 -> npcl(
                "Hey...I hope you didn't rip that page out of one of those books, they'll be trouble if it was.",
            ).also {
                stage++
            }

            2 -> playerl("No, it wasn't actually, I got it from somewhere else, do you recognise it?").also { stage++ }
            3 -> npcl(
                "Hmm, well it certainly looks like text from one of the books. Have you checked at the wizards' guild in Yanille? We sometime loan books to them.",
            ).also {
                stage = END_DIALOGUE
            }
        }
    }
}

private class IrwinFeaselbaumAboutNecromancyBookDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.IRWIN_FEASELBAUM_2066)
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "Hi, do you know anything about this book?").also { stage++ }
            1 -> npcl(
                "Hey...that book has been loaned to the wizards' guild in Yanille, I hope you're not returning it in a damaged condition!",
            ).also {
                stage++
            }

            2 -> playerl(
                "Well, I was just wondering where the book came from actually and try to get some more information about it.",
            ).also {
                stage++
            }

            3 -> npcl(
                "Well, that book looks as if it's been through the wars...you're not going to return it in that condition. You take it back to the wizards at Yanille, they borrowed it, so it's their responsibility.",
            ).also {
                stage = END_DIALOGUE
            }
        }
    }
}
