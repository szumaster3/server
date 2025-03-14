package content.region.kandarin.handlers

import content.region.kandarin.dialogue.IrwinFeaselbaumAboutNecromancyBookDialogue
import content.region.kandarin.dialogue.IrwinFeaselbaumAboutTornPagesDialogue
import core.api.*
import core.api.quest.getQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class NecromancerTowerListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.BOOKCASE_6894, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.NECROMANCY_BOOK_4837)) {
                sendMessage(player, "You search the books...")
                sendMessageWithDelay(player, "You find nothing of interest to you.", 1)
                return@on true
            }
            if (freeSlots(player) == 0) {
                sendMessage(player, "You need at least one free inventory space to take from the shelves.")
                return@on true
            }

            sendMessage(
                player,
                "You search the bookcase and find a book named 'On loan to the wizards' Guild...to be returned'.",
            )
            addItem(player, Items.NECROMANCY_BOOK_4837)
            return@on true
        }

        onUseWith(IntType.NPC, Items.TORN_PAGE_4809, NPCs.IRWIN_FEASELBAUM_2066) { player, _, _ ->
            if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) > 1) {
                openDialogue(player, IrwinFeaselbaumAboutTornPagesDialogue())
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.NECROMANCY_BOOK_4837, NPCs.IRWIN_FEASELBAUM_2066) { player, _, _ ->
            if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) > 1) {
                openDialogue(player, IrwinFeaselbaumAboutNecromancyBookDialogue())
            }
            return@onUseWith true
        }
    }
}
