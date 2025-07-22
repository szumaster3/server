package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

/**
 * Has a 10% chance to heal the player by 9 hitpoints when activated.
 */
class SmellingUgthankiKebabEffect : ConsumableEffect() {

    /**
     * Activates healing effect with a 10% chance.
     */
    override fun activate(player: Player) {
        if (RandomFunction.nextInt(100) < PERCENTAGE) {
            effect.activate(player)
        }
    }

    /**
     * Returns healing amount if chance succeeds; otherwise zero.
     */
    override fun getHealthEffectValue(player: Player): Int =
        if (RandomFunction.nextInt(100) < PERCENTAGE) {
            HEALING
        } else {
            0
        }

    companion object {
        private const val PERCENTAGE = 10
        private const val HEALING = 9
        private val effect = HealingEffect(HEALING)
    }
}
