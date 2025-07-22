package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Heals the player by a percentage of their maximum lifepoints.
 *
 * @param percentage The healing percentage (e.g., 10 for 10%).
 */
class PercentageHealthEffect(
    percentage: Int,
) : ConsumableEffect() {
    private val percentage = percentage * 0.01

    /**
     * Activates healing by the calculated percentage amount.
     */
    override fun activate(player: Player) {
        val effect = HealingEffect(getHealthEffectValue(player))
        effect.activate(player)
    }

    /**
     * Calculates healing amount as a percentage of max lifepoints.
     */
    override fun getHealthEffectValue(player: Player): Int = (player.getSkills().maximumLifepoints * percentage).toInt()
}
