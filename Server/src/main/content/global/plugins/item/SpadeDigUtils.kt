package content.global.plugins.item

import core.game.node.entity.player.Player
import core.game.world.map.Location

/**
 * Utility object to manage spade digging event listeners keyed by location.
 */
object SpadeDigUtils {

    /**
     * Map of registered digging listeners.
     *
     * The key is a [Location] where digging is detected,
     * and the value is a function that takes a [Player] and performs some action.
     */
    val listeners = HashMap<Location, (Player) -> Unit>()

    /**
     * Registers a listener function for a specific digging [location].
     *
     * If a listener is already registered for the location, this call will NOT overwrite it.
     *
     * @param location The [Location] where the dig event will trigger the listener.
     * @param method The function to invoke when a player digs at the specified location.
     */
    fun registerListener(
        location: Location,
        method: (Player) -> Unit,
    ) {
        listeners.putIfAbsent(location, method)
    }

    /**
     * Executes the registered listener for the given [location] and [player].
     *
     * If a listener exists for the location, it will be invoked with the player as argument.
     *
     * @param location The [Location] where the player has dug.
     * @param player The [Player] who dug at the location.
     * @return `true` if a listener was found and executed; `false` otherwise.
     */
    @JvmStatic
    fun runListener(
        location: Location,
        player: Player,
    ): Boolean {
        if (listeners.containsKey(location)) {
            listeners[location]?.invoke(player)
            return true
        }
        return false
    }
}
