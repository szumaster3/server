package content.global.travel.eagle.caves

import core.api.sendMessage
import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Scenery

class DesertEagleCave : InteractionListener {

    override fun defineListeners() {
        on(Scenery.CAVE_19425, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You enter the tunnel.")
            teleport(player, Location.create(3420, 9562, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.CAVE_19791, IntType.SCENERY, "exit") { player, _ ->
            teleport(player, Location.create(3405, 3159, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }
    }
}