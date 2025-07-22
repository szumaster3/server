package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Damages the player by 1 hitpoint if above 2 lifepoints.
 */
class DwarvenRockCakeEffect : ConsumableEffect() {

    /**
     * Applies damage only if lifepoints are greater than 2.
     */
    override fun activate(player: Player) {
        if (player.getSkills().lifepoints > 2) {
            effect.activate(player)
        }
    }

    /**
     * Gets damage value: -1 if lifepoints > 2, otherwise 0.
     */
    override fun getHealthEffectValue(player: Player): Int = if (player.getSkills().lifepoints > 2) -1 else 0

    companion object {
        /**
         * Damage effect dealing 1 flat damage.
         */
        private val effect = DamageEffect(1.0, false)
    }
}
