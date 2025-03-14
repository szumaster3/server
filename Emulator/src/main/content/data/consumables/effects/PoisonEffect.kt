package content.data.consumables.effects

import core.api.impact
import core.game.consumable.ConsumableEffect
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player

class PoisonEffect(
    var amount: Int,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        impact(player, amount, ImpactHandler.HitsplatType.POISON)
    }

    override fun getHealthEffectValue(player: Player): Int {
        return -amount
    }
}
