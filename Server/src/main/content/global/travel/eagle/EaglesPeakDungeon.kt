package content.global.travel.eagle

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Quests
import shared.consts.Scenery

class EaglesPeakDungeon : InteractionListener {

    override fun defineListeners() {
        on(Scenery.ROCKY_OUTCROP_19925, IntType.SCENERY, "inspect") { player, _ ->
            if(!hasRequirement(player, Quests.EAGLES_PEAK)) {
                sendDialogueLines(
                    player,
                    "This area of rock doesn't quite seem to match the rest of the cliff",
                    "face. There appears to be a thin vertical slot in the middle of the",
                    "rock, with what appears to be a carving of a feather above it."
                )
                return@on false
            }

            // 1. sendDialogueLines(player, "You place the metal feather into the groove in the cliff face. It seems", "to catch in some sort of mechanism.")
            // 2. sendDialogueLines(player, "Part of the cliff face swings outwards to reveal a tunnel heading into", "the mountain.")

            setVarbit(player, 2780, 15)
            return@on true
        }


        /*
         * Handles entrance to the eagle peak dungeon.
         */

        on(Scenery.CAVE_ENTRANCE_19926, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You enter the tunnel.")
            teleport(player, Location.create(1994, 4983, 3), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles entrance to silver & gold feather.
         */

        on(Scenery.ENTRANCE_33018, IntType.SCENERY, "enter") { player, node ->
            val destination = when (node.location) {
                Location(1992, 4982, 3) -> Location(2328, 3496, 0) // Surface.
                Location(1985, 4948, 3) -> Location(1974, 4908, 2) // Bronze
                Location(2023, 4981, 3) -> Location(1957, 4908, 2) // Gold.
                else -> Location(1947, 4868, 2) // Silver.
            }

            teleport(player, destination, TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles exit from silver feather location.
         */

        on(Scenery.TUNNEL_19900, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(1987, 4972, 3), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles exit from gold feather location.
         */

        on(Scenery.TUNNEL_19894, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(2022, 4982, 3), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles exit from bronze feather location.
         */

        on(Scenery.TUNNEL_19906, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(1987, 4949, 3), TeleportManager.TeleportType.INSTANT)
            return@on true
        }
    }

    companion object {
        private val STONE_DOOR_IDS = intArrayOf(19843, 19991)
        private val STONE_DOOR_VARBIT = 3106
    }
}