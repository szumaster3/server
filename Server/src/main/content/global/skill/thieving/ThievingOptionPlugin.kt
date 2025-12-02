package content.global.skill.thieving

import core.api.lockInteractions
import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.Scenery

class ThievingOptionPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles thieving scenery options.
         */

        on(IntType.SCENERY, "steal-from", "steal from", "steal") { player, node ->
            val scenery = node as? Scenery ?: return@on true // rzutowanie
            val stall = Stall.values().firstOrNull { scenery.id in it.fullIDs } ?: return@on true
            Stall.handleStall(player, scenery, stall)
            lockInteractions(player, 6)
            return@on true
        }

        /*
         * Handles clothes stall interaction in Keldagrim.
         */

        on(shared.consts.Scenery.CLOTHES_STALL_6165, IntType.SCENERY, "steal-from") { player, _ ->
            sendDialogue(player, "You don't really see anything you'd want to steal from this stall.")
            return@on true
        }

        /*
         * Handles opening thieving chests.
         */

        on(ChestsDefinition.allObjectIds, IntType.SCENERY, "open") { player, node ->
            ChestsDefinition.forId(node.id)?.open(player, node as Scenery)
            return@on true
        }

        /*
         * Handles searching for traps.
         */

        on(ChestsDefinition.allObjectIds, IntType.SCENERY, "search for traps") { player, node ->
            ChestsDefinition.forId(node.id)?.searchTraps(player, node as Scenery)
            return@on true
        }
    }
}
