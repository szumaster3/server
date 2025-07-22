package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Applies 5 poison damage if the player has more than 5 lifepoints.
 */
class PoisonKarambwanEffect : ConsumableEffect() {

    /**
     * Activates poison damage if player's lifepoints exceed 5.
     */
    override fun activate(player: Player) {
        if (player.getSkills().lifepoints > 5) {
            effect.activate(player)
        }
    }

    /**
     * Returns fixed poison damage amount.
     */
    override fun getHealthEffectValue(player: Player): Int = HEALING

    companion object {
        private const val HEALING = -5
        private val effect = PoisonEffect(5)
    }
}
