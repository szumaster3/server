package content.region.misthalin.quest.losttribe.handlers

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.Components

object GoblinFollower {
    fun sendToMines(player: Player) {
        travel(player, Location.create(3319, 9616, 0))
        sendMessage(player, "Kazgar shows you the way through the tunnels.")
    }

    fun sendToLumbridge(player: Player) {
        travel(player, Location.create(3232, 9610, 0))
        sendMessage(player, "Mistag shows you the way through the tunnels.")
    }

    private fun travel(
        player: Player,
        location: Location,
    ) {
        lock(player, 8)
        lockInteractions(player, 8)
        GameWorld.Pulser.submit(
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> openInterface(player, Components.FADE_TO_BLACK_120)
                        4 -> {
                            teleport(player, location, TeleportManager.TeleportType.INSTANT)
                        }

                        8 -> {
                            openInterface(player, Components.FADE_FROM_BLACK_170)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }
}
