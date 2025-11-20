package content.global.travel.eagle.caves

import core.api.sendMessage
import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Scenery

class PolarEagleCave : InteractionListener {

    override fun defineListeners() {
        on(Scenery.ROCKY_HANDHOLDS_19846, IntType.SCENERY, "climb") { player, _ ->
            teleport(player, Location.create(2744, 3830, 1), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.ROCKY_HANDHOLDS_19847, IntType.SCENERY, "climb") { player, _ ->
            teleport(player, Location.create(2740, 3830, 1), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.CAVE_19762, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You enter the tunnel.")
            teleport(player, Location.create(2707, 10205, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(intArrayOf(Scenery.CAVE_19763,Scenery.CAVE_19764), IntType.SCENERY, "exit") { player, _ ->
            teleport(player, Location.create(2744, 3830, 1), TeleportManager.TeleportType.INSTANT)
            return@on true
        }
    }
}