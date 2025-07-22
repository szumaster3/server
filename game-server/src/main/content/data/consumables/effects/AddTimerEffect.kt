package content.data.consumables.effects

import core.api.registerTimer
import core.api.removeTimer
import core.api.spawnTimer
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Adds a timer to the player when used.
 *
 * @property identifier Timer name.
 * @property args Timer arguments.
 */
class AddTimerEffect(
    val identifier: String,
    vararg val args: Any,
) : ConsumableEffect() {

    /**
     * Replaces existing timer and registers a new one.
     */
    override fun activate(player: Player) {
        removeTimer(player, identifier)
        val timer = spawnTimer(identifier, *args) ?: return
        registerTimer(player, timer)
    }
}