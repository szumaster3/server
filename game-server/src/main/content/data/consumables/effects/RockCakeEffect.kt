package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Applies damage equal to 10% of current lifepoints if lifepoints are above 1.
 */
class RockCakeEffect : ConsumableEffect() {

    /**
     * Activates damage effect if player has more than 1 lifepoint.
     */
    override fun activate(player: Player) {
        if (player.getSkills().lifepoints > 1) {
            effect.activate(player)
        }
    }

    /**
     * Returns negative health effect value equal to 10% of current lifepoints.
     */
    override fun getHealthEffectValue(player: Player): Int = (player.getSkills().lifepoints * -0.1).toInt()

    companion object {
        private val effect = DamageEffect(10.0, true)
    }
}
