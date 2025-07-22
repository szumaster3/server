package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Heals the player by a fixed amount.
 *
 * @param amt The amount to heal.
 */
class HealingEffect(
    var amt: Int,
) : ConsumableEffect() {

    /**
     * Heals the player by [amt].
     */
    override fun activate(player: Player) {
        player.getSkills().heal(amt)
    }

    /**
     * Returns the healing amount.
     */
    override fun getHealthEffectValue(player: Player): Int = amt
}
