package content.region.fremennik.plugin.dungeon.plugin

import core.api.*
import core.api.utils.Vector
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class IceTrollDungeonPlugin : InteractionListener {

    val ROPE_BRIDGE     = intArrayOf(Scenery.ROPE_BRIDGE_21316,Scenery.ROPE_BRIDGE_21317,Scenery.ROPE_BRIDGE_21318,Scenery.ROPE_BRIDGE_21319)
    val ENTER_ENTRANCE  = intArrayOf(Scenery.CAVE_21584, Scenery.CAVE_21585, Scenery.CAVE_21586)
    val EXIT_ENTRANCE   = intArrayOf(Scenery.CAVE_OPENING_21597, Scenery.CAVE_OPENING_21598, Scenery.CAVE_OPENING_21599)

    override fun defineListeners() {

        /*
         * Handles crossing the bridges (Varbit: 3315).
         */

        on(ROPE_BRIDGE, IntType.SCENERY, "walk-across") { player, node ->
            val destination = Vector.betweenLocs(player.location, node.location)
            val direction = when (node.id) {
                21316 -> Direction.SOUTH
                21319 -> Direction.EAST
                21318 -> Direction.WEST
                 else -> Direction.NORTH
            }
            lock(player,6)
            registerLogoutListener(player,"rope-bridge"){ p -> p.location = node.location }
            forceMove(player, player.location, player.location.transform(destination.toDirection(), 5), 0, 150, direction)
            runTask(player, 7) { clearLogoutListener(player, "rope-bridge") }
            return@on true
        }

        /*
         * Handles cave entrances (to dungeon).
         */

        on(ENTER_ENTRANCE, IntType.SCENERY, "open") { player, node ->
            replaceScenery(node.asScenery(), Scenery.CAVE_21587, 3)
            openInterface(player, Components.FADE_TO_BLACK_115)
            teleport(player, Location.create(2394, 10300, 1), TeleportManager.TeleportType.INSTANT, 3)
            queueScript(player, 6, QueueStrength.SOFT) {
                openInterface(player, Components.FADE_FROM_BLACK_170)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles cave exits (west).
         */

        on(EXIT_ENTRANCE, IntType.SCENERY, "exit") { player, _ ->
            teleport(player, Location.create(2317, 3893, 0), TeleportManager.TeleportType.INSTANT, 3)
            return@on true
        }

        /*
         * Handles stone ladder (east).
         */

        on(Scenery.STONE_LADDER_21592, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(2401, 3888, 0), TeleportManager.TeleportType.INSTANT, 3)
            return@on true
        }

        /*
         * Handles requesting NPC for supplies.
         */

        on(NPCs.BORK_SIGMUNDSON_5477, IntType.NPC, "request-supplies") { _, _ ->
            return@on true
        }
    }
}