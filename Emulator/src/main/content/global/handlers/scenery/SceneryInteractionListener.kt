package content.global.handlers.scenery

import core.api.addItem
import core.api.hasSpaceFor
import core.api.replaceScenery
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class SceneryInteractionListener : InteractionListener {
    override fun defineListeners() {
        on(BANDIT_CAMP_QUARRY, IntType.SCENERY, "take-axe") { player, node ->
            if (!hasSpaceFor(player, Item(Items.BRONZE_PICKAXE_1265))) {
                sendMessage(player, "Not enough inventory space.")
                return@on true
            }
            addItem(player, Items.BRONZE_PICKAXE_1265)
            replaceScenery(node.asScenery(), (node.id + 1), 250)
            return@on true
        }

        on(LUMBRIDGE_CHICKEN_FARM, IntType.SCENERY, "take-axe") { player, node ->
            if (!hasSpaceFor(player, Item(Items.BRONZE_AXE_1351))) {
                sendMessage(player, "Not enough inventory space.")
                return@on true
            }
            addItem(player, Items.BRONZE_AXE_1351)
            replaceScenery(node.asScenery(), (node.id + 1), 250)
            return@on true
        }

        on(VARROCK_UNKNOWN, IntType.SCENERY, "take-axe") { player, node ->
            if (!hasSpaceFor(player, Item(Items.BRONZE_AXE_1351))) {
                sendMessage(player, "Not enough inventory space.")
                return@on true
            }
            addItem(player, Items.BRONZE_AXE_1351)
            replaceScenery(node.asScenery(), (node.id + 1), 250)
            return@on true
        }

        on(RIMMINGTON_MINING_SITE, IntType.SCENERY, "take") { player, node ->
            if (!hasSpaceFor(player, Item(Items.SPADE_952))) {
                sendMessage(player, "Not enough inventory space.")
                return@on true
            }
            addItem(player, Items.SPADE_952)
            replaceScenery(node.asScenery(), 0, 250)
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
