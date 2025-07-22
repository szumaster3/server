package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import kotlin.math.floor
import kotlin.math.min

/**
 * Heals the player by a base amount plus a percentage of max hitpoints,
 * ensuring not to exceed a calculated cap.
 *
 * @param base Flat heal amount.
 * @param percent Percentage (as decimal) of max hitpoints to heal.
 */
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

    /**
     * Heals the player by the sum of [base] and a capped percentage of max HP.
     */
    override fun activate(player: Player) {
        val maxHp = player.getSkills().maximumLifepoints
        val curHp = player.getSkills().lifepoints
        var amount = floor(maxHp * percent).toInt()
        amount = (base + min(amount.toDouble(), ((1.0 + percent) * maxHp.toDouble() - curHp.toDouble()).toInt().toDouble())).toInt()
        player.getSkills().healNoRestrictions(amount)
    }
}
