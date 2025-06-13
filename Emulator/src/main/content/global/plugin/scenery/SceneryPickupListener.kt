package content.global.plugin.scenery

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class SceneryPickupListener : InteractionListener {

    override fun defineListeners() {
        registerSceneryPickup(BANDIT_CAMP_QUARRY, "take-axe", Items.BRONZE_PICKAXE_1265)
        registerSceneryPickup(LUMBRIDGE_CHICKEN_FARM, "take-axe", Items.BRONZE_AXE_1351)
        registerSceneryPickup(VARROCK_UNKNOWN, "take-axe", Items.BRONZE_AXE_1351)
        registerSceneryPickup(RIMMINGTON_MINING_SITE, "take", Items.SPADE_952, newId = 0)
    }

    private fun registerSceneryPickup(
        sceneryId: Int,
        option: String,
        itemId: Int,
        newId: Int? = null,
        delay: Int = 500
    ) {
        on(sceneryId, IntType.SCENERY, option) { player, node ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "Not enough inventory space.")
                return@on true
            }
            addItem(player, itemId, 1)
            val replacementId = newId ?: (node.id + 1)
            replaceScenery(node.asScenery(), replacementId, delay)
            return@on true
        }
    }

    companion object {
        const val BANDIT_CAMP_QUARRY = Scenery.ROCK_11097
        const val VARROCK_UNKNOWN = Scenery.LOGS_5581
        const val LUMBRIDGE_CHICKEN_FARM = Scenery.LOGS_36974
        const val RIMMINGTON_MINING_SITE = Scenery.SPADE_9662
    }
}