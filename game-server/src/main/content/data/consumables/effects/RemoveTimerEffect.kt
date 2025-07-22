package content.data.consumables.effects

import core.api.removeTimer
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Removes a timer identified by [identifier] from the player.
 *
 * @param identifier The ID of the timer to remove.
 */
class RemoveTimerEffect(
    val identifier: String,
) : ConsumableEffect() {

    /**
     * Removes the specified timer from the player.
     */
    override fun activate(player: Player) {
        removeTimer(player, identifier)
    }
}
