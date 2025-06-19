package content.region.fremennik.island.lunar.plugin

import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Components

/**
 * Utility for travel between moonclan island and pirates' cove.
 */
object LunarIsleShip {
    /**
     * Initiates the sailing.
     */
    @JvmStatic
    fun sail(player: Player, destination: Destination, ) {
        lock(player, destination.animation)
        lockInteractions(player, destination.animation)
        openInterface(player, Components.LUNAR_COVE_BOAT_MAP_431)
        animateInterface(player, Components.LUNAR_COVE_BOAT_MAP_431, 1, destination.animation)
        teleport(player, destination.destination)
        val animDuration = animationDuration(getAnimation(destination.animation))
        submitWorldPulse(
            object : Pulse(animDuration) {
                override fun pulse(): Boolean {
                    closeInterface(player)
                    closeOverlay(player)
                    unlock(player)
                    return true
                }
            },
        )
        return
    }
}

/**
 * Represents the travel destinations.
 */
enum class Destination(val destName: String, val destination: Location, val animation: Int, ) {
    PIRATES_COVE_TO_MOONCLAN_ISLAND("Pirates' Cove", Location.create(2131, 3900, 2), 2344),
    MOONCLAN_ISLAND_TO_PIRATES_COVE("Moonclan Island", Location.create(2216, 3797, 2), 2345),
}
