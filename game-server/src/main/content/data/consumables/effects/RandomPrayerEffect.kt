package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

/**
 * Restores a random amount of prayer points between [a] and [b].
 *
 * @param a Minimum prayer points restored.
 * @param b Maximum prayer points restored.
 */
class RandomPrayerEffect(
    val a: Int,
    val b: Int,
) : ConsumableEffect() {

    /**
     * Activates prayer restoration with a random amount.
     */
    override fun activate(player: Player) {
        val effect = PrayerEffect(RandomFunction.random(a, b).toDouble(), 0.0)
        effect.activate(player)
    }
}
