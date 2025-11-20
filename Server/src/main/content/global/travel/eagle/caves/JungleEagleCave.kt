package content.global.travel.eagle.caves

import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Scenery

class JungleEagleCave : InteractionListener {

    override fun defineListeners() {
        on(Scenery.TALL_VINE_36710, IntType.SCENERY, "climb") { player, _ ->
            // Grows: 40 min, Varbit: 3109
            teleport(player, Location.create(2526, 9324, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(intArrayOf(Scenery.CAVE_19759, Scenery.CAVE_19760), IntType.SCENERY, "exit") { player, _ ->
            teleport(player, Location.create(2513, 2926, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }
    }
}