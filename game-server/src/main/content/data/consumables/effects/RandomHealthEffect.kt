package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

/**
 * Applies a random health effect between [a] and [b].
 * Heals if positive, damages if zero or negative.
 *
 * @param a Minimum health effect value.
 * @param b Maximum health effect value.
 */
class RandomHealthEffect(
    val a: Int,
    val b: Int,
) : ConsumableEffect() {

    /**
     * Activates healing or damage based on random health value.
     */
    override fun activate(player: Player) {
        val effect: ConsumableEffect
        val healthEffectValue = getHealthEffectValue(player)
        effect =
            if (healthEffectValue > 0) {
                HealingEffect(healthEffectValue)
            } else {
                DamageEffect(healthEffectValue.toDouble(), false)
            }
        effect.activate(player)
    }

    /**
     * Returns a random value between [a] and [b].
     */
    override fun getHealthEffectValue(player: Player): Int = RandomFunction.random(a, b)
}
