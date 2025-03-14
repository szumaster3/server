package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

class SmellingUgthankiKebabEffect : ConsumableEffect() {
    override fun activate(player: Player) {
        if (RandomFunction.nextInt(100) < PERCENTAGE) {
            effect.activate(player)
        }
    }

    override fun getHealthEffectValue(player: Player): Int {
        return if (RandomFunction.nextInt(100) < PERCENTAGE) HEALING else 0
    }

    companion object {
        private const val PERCENTAGE = 10
        private const val HEALING = 9
        private val effect = HealingEffect(HEALING)
    }
}
