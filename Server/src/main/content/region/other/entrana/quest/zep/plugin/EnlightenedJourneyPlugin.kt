package content.region.other.entrana.quest.zep.plugin

import content.region.other.entrana.quest.zep.cutscene.AirBalloonCutscene
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class EnlightenedJourneyPlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.WILLOW_BRANCH_5933, Scenery.BASKET_19132) { player, _, _ ->
            if (getQuestStage(player, Quests.ENLIGHTENED_JOURNEY) >= 7) {
                if (!removeItem(player, Item(Items.WILLOW_BRANCH_5933, 12))) {
                    sendMessage(player, "You do not have enough willow branches.")
                } else {
                    sendNPCDialogue(player, NPCs.AUGUSTE_5049, "Great! Let me just put it together and we'll be ready to lift off! Speak to me again in a moment.")
                    runTask(player, 3) {
                        AirBalloonCutscene(player).start(true)
                        setQuestStage(player, Quests.ENLIGHTENED_JOURNEY, 8)
                    }
                }
            }
            return@onUseWith true
        }
    }
}
