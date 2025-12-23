package content.global.skill.thieving

import core.api.getUsedOption
import core.api.lockInteractions
import core.api.sendDialogue
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.Scenery

class ThievingOptionPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles thieving scenery options.
         */

        on(IntType.SCENERY, "steal-from", "steal from", "steal") { player, node ->
            val scenery = node as? Scenery ?: return@on true
            val stall = ThievingDefinition.Stall.values().firstOrNull { scenery.id in it.fullIDs } ?: return@on true
            ThievingDefinition.Stall.handleSteal(player, scenery, stall)
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

        on(ThievingDefinition.Chests.OBJECT_IDS, IntType.SCENERY, "open") { player, node ->
            ThievingDefinition.Chests.forId(node.id)?.open(player, node as Scenery)
            return@on true
        }

        /*
         * Handles searching for traps.
         */

        on(ThievingDefinition.Chests.OBJECT_IDS, IntType.SCENERY, "search for traps") { player, node ->
            ThievingDefinition.Chests.forId(node.id)?.searchTraps(player, node as Scenery)
            return@on true
        }

        /*
         * Handles opening locked doors.
         */

        on(ThievingDefinition.Doors.DOOR_IDS, IntType.SCENERY, "open", "pick-lock") { player, node ->
            val option = getUsedOption(player)
            val door = ThievingDefinition.Doors.forLocation(node.location)

            when (option) {
                "open" -> {
                    if (door == null) {
                        sendMessage(player, "The door is locked.")
                    } else {
                        door.open(player, node as Scenery)
                    }
                    return@on true
                }
                "pick-lock" -> {
                    if (door == null) {
                        sendMessage(player, "This door cannot be unlocked.")
                    } else {
                        door.pickLock(player, node as Scenery)
                    }
                    return@on true
                }
                else -> return@on false
            }
        }
    }
}
