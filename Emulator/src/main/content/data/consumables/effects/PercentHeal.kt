package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import kotlin.math.floor
import kotlin.math.min

class PercentHeal(
    base: Int,
    percent: Double,
) : ConsumableEffect() {
    var base: Int = 0
    var percent: Double = 0.0

    init {
        this.base = base
        this.percent = percent
    }

    override fun activate(player: Player) {
        val maxHp = player.getSkills().maximumLifepoints
        val curHp = player.getSkills().lifepoints
        var amount = floor(maxHp * percent).toInt()
        amount =
            (
                base +
                    min(
                        amount.toDouble(),
                        ((1.0 + percent) * maxHp.toDouble() - curHp.toDouble()).toInt().toDouble(),
                    )
            ).toInt()
        player.getSkills().healNoRestrictions(amount)
    }
}
