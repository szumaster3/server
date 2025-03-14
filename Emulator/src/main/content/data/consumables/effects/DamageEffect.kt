package content.data.consumables.effects

import core.api.impact
import core.game.consumable.ConsumableEffect
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player

class DamageEffect(
    val amt: Double,
    val isPercent: Boolean,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        impact(player, -getHealthEffectValue(player), ImpactHandler.HitsplatType.NORMAL)
    }

    override fun getHealthEffectValue(player: Player): Int {
        var amount = amt
        if (isPercent) {
            amount /= 100.0
            return -(amount * player.getSkills().lifepoints).toInt()
        }
        return -amt.toInt()
    }
}
