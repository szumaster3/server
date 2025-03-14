package content.region.wilderness.handlers

import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class WildernessLadderListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.LADDER_1765, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location.create(3069, 10257, 0))
            return@on true
        }

        on(Scenery.LADDER_1767, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location.create(3017, 10250, 0))
            return@on true
        }
    }
}
