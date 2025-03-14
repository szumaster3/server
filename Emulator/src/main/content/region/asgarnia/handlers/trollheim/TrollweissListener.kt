package content.region.asgarnia.handlers.trollheim

import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class TrollweissListener : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Scenery.TUNNEL_5012, Scenery.TUNNEL_5013), IntType.SCENERY, "enter") { player, node ->
            when (node.id) {
                5012 -> teleport(player, Location.create(2799, 10134, 0))
                else -> player.properties.teleportLocation = Location.create(2796, 3719, 0)
            }
            return@on true
        }

        on(intArrayOf(Scenery.CAVE_ENTRANCE_5007, Scenery.CAVE_EXIT_32743), IntType.SCENERY, "enter") { player, node ->
            when (node.id) {
                32743 -> teleport(player, Location.create(2822, 3744, 0))
                else -> teleport(player, Location.create(2803, 10187, 0))
            }

            return@on true
        }

        on(intArrayOf(Scenery.TUNNEL_5009, Scenery.CREVASSE_33185), IntType.SCENERY, "enter") { player, node ->
            when (node.id) {
                33185 -> teleport(player, Location.create(2778, 3869, 0))
                else -> teleport(player, Location.create(2772, 10232, 0))
            }
            return@on true
        }
    }
}
