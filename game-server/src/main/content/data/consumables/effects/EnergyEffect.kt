package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Reduces the player run energy by a specified amount.
 *
 * @param amt The amount of run energy to reduce.
 */
class EnergyEffect(amt: Int) : ConsumableEffect() {
    var amt: Double = amt.toDouble()

    /**
     * Decreases the player's run energy by [amt].
     */
    override fun activate(player: Player) {
        player.settings.updateRunEnergy(-amt)
    }
}
