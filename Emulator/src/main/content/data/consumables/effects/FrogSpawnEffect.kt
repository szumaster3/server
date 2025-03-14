package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class FrogSpawnEffect : ConsumableEffect() {
    private val odds =
        listOf(
            3 to 12.19,
            4 to 34.00,
            5 to 28.95,
            6 to 31.90,
        )

    override fun activate(player: Player) {
        val healingAmount = getHealthEffectValue()
        player.getSkills().healNoRestrictions(healingAmount)
    }

    private fun getHealthEffectValue(): Int {
        val cumulativeOdds = odds.runningFold(0.0) { acc, (_, chance) -> acc + chance }
        val randomValue = Math.random() * 100

        return odds[cumulativeOdds.indexOfFirst { randomValue < it } - 1].first
    }
}
