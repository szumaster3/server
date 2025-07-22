package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

/**
 * Restores a random amount of run energy between [a] and [b].
 *
 * @param a Minimum amount of energy restored.
 * @param b Maximum amount of energy restored.
 */
class RandomEnergyEffect(
    val a: Int,
    val b: Int,
) : ConsumableEffect() {

    /**
     * Activates energy restoration with a random value in the range.
     */
    override fun activate(player: Player) {
        val effect = EnergyEffect(RandomFunction.random(a, b))
        effect.activate(player)
    }
}
