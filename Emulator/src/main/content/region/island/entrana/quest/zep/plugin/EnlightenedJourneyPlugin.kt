package content.region.island.entrana.quest.zep.plugin

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.removeItem
import core.api.sendMessage
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

// First:2x sandbag, 1x log, 5x relax, 1x red rope, 1x log, 2xrelax, 1x red rope, 2x relax, 2x logs, 3x relax.
// Second:1x log, 2x relax, 1x red rope, 1x rope, 7x relax, 1x sandbag, 2x relax, 1x red rope, 1x log, 1x rope, 1x relax.
// Third:1x log, 2x relax, 1x rope, 3x relax, 1x log, 1x red rope, 4x relax , 1x sandbag, 1x red rope, 2x relax , 1x rope.

class EnlightenedJourneyPlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.WILLOW_BRANCH_5933, Scenery.BASKET_19132) { player, _, _ ->
            if (getQuestStage(player, Quests.ENLIGHTENED_JOURNEY) >= 7) return@onUseWith true
            if (!removeItem(player, Item(Items.WILLOW_BRANCH_5933, 12))) {
                sendMessage(player, "You do not have enough willow branches.")
            } else {
                sendNPCDialogue(player, NPCs.AUGUSTE_5049, "Great! Let me just put it together and we'll be ready to lift off! Speak to me again in a moment.")
                setQuestStage(player, Quests.ENLIGHTENED_JOURNEY, 8)
            }
            return@onUseWith true
        }
    }
}
