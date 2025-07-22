package content.data.consumables.effects

import core.api.impact
import core.game.consumable.ConsumableEffect
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player

/**
 * Applies damage to the player, either flat or percentage-based.
 *
 * @param amt Amount of damage or percent.
 * @param isPercent True if damage is percent of lifepoints.
 */
class DamageEffect(val amt: Double, val isPercent: Boolean) : ConsumableEffect() {

    /**
     * Deals damage as a negative impact with a normal hitsplat.
     */
    override fun activate(player: Player) {
        impact(player, -getHealthEffectValue(player), ImpactHandler.HitsplatType.NORMAL)
    }

    /**
     * Calculates the damage value based on flat or percent input.
     */
    override fun getHealthEffectValue(player: Player): Int {
        var amount = amt
        if (isPercent) {
            amount /= 100.0
            return -(amount * player.getSkills().lifepoints).toInt()
        }
        return -amt.toInt()
    }
}
