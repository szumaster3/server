package content.region.island.lunarisle.plugin

import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Components

/**
 * Handles the lunar isle travel functionality for players.
 */
object LunarIsleUtils {
    /**
     * Initiates the sailing.
     *
     * @param player The player who is initiating the travel.
     * @param destination The destination to which the player is traveling.
     */
    @JvmStatic
    fun sail(
        player: Player,
        destination: Destination,
    ) {
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
 *
 * @property destName The name of the destination.
 * @property destination The [Location] representing the coordinates of the destination.
 * @property animation The animation id used during the travel animation.
 */
enum class Destination(
    val destName: String,
    val destination: Location,
    val animation: Int,
) {
    PIRATES_COVE_TO_MOONCLAN_ISLAND(
        destName = "Pirates' Cove",
        destination = Location.create(2131, 3900, 2),
        animation = 2344,
    ),
    MOONCLAN_ISLAND_TO_PIRATES_COVE(
        destName = "Moonclan Island",
        destination = Location.create(2216, 3797, 2),
        animation = 2345,
    ),
}
