package content.global.handlers.scenery

import core.api.replaceScenery
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class ChestListener : InteractionListener {
    override fun defineListeners() {
        // Zanaris chest interaction (Fairy bank).
        on(Scenery.OPEN_CHEST_12121, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node.asScenery(), Scenery.CLOSED_CHEST_12120, -1)
            return@on true
        }

        on(Scenery.CLOSED_CHEST_12120, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_12121, -1)
            return@on true
        }
        // Fred farm house chest interaction.
        on(Scenery.CLOSED_CHEST_37009, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_37010, -1, node.location)
            return@on true
        }

        on(Scenery.OPEN_CHEST_37010, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node.asScenery(), Scenery.CLOSED_CHEST_37009, -1, node.location)
            return@on true
        }

        on(Scenery.OPEN_CHEST_37010, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the chest but find nothing.")
            return@on true
        }
    }
}
