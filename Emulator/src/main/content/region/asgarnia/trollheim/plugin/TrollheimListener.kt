package content.region.asgarnia.trollheim.plugin

import core.api.getRegionBorders
import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class TrollheimListener : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Scenery.CAVE_ENTRANCE_3759, Scenery.CAVE_ENTRANCE_3735), IntType.SCENERY, "enter") { player, node ->
            if (node.id == Scenery.CAVE_ENTRANCE_3759) {
                teleport(player, Location.create(2893, 10074, 0))
            } else {
                teleport(player, Location.create(2269, 4752, 0))
            }
            return@on true
        }

        on(Scenery.CAVE_EXIT_32738, IntType.SCENERY, "exit") { player, _ ->
            val location = if (!getRegionBorders(11677).insideBorder(player)) {
                Location.create(2858, 3577, 0)
            } else {
                Location.create(2893, 3671, 0)
            }
            teleport(player, location)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.CAVE_ENTRANCE_3759), "enter") { _, _ ->
            return@setDest Location.create(2893, 3671, 0)
        }
    }

}
