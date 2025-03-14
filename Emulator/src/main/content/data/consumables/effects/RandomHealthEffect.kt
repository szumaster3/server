package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

class RandomHealthEffect(
    val a: Int,
    val b: Int,
) : ConsumableEffect() {
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

    override fun getHealthEffectValue(player: Player): Int {
        return RandomFunction.random(a, b)
    }
}
