package content.global.plugins.`object`

import core.api.replaceScenery
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

class ChestOptionPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles Zanaris chests.
         */

        on(Scenery.OPEN_CHEST_12121, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node.asScenery(), Scenery.CLOSED_CHEST_12120, -1)
            return@on true
        }

        on(Scenery.CLOSED_CHEST_12120, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_12121, -1)
            return@on true
        }

        /*
         * Handles Fred farm house chests.
         */

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

        /*
         * Handles Heroes' Guild chests.
         */

        on(Scenery.CHEST_2632, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CHEST_2633, -1)
            return@on true
        }

        on(Scenery.CHEST_2633, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CHEST_2632, -1)
            return@on true
        }

        /*
         * Handles Witchaven dungeon chests (Treasure Trails).
         */

        on(Scenery.CHEST_18304, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CHEST_18321, -1, node.location)
            return@on true
        }

        on(Scenery.CHEST_18321, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CHEST_18304, -1, node.location)
            return@on true
        }
    }
}
