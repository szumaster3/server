package content.data.consumables.effects

import core.api.impact
import core.game.consumable.ConsumableEffect
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player

/**
 * Applies poison damage to the player.
 *
 * @param amount The amount of poison damage to apply.
 */
class PoisonEffect(
    var amount: Int,
) : ConsumableEffect() {

    /**
     * Deals poison damage with a poison hitsplat.
     */
    override fun activate(player: Player) {
        impact(player, amount, ImpactHandler.HitsplatType.POISON)
    }

    /**
     * Returns negative health effect value representing damage.
     */
    override fun getHealthEffectValue(player: Player): Int = -amount
}
