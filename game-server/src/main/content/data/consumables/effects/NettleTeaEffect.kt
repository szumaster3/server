package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Heals 3 hitpoints and restores 10 run energy if not at full health,
 * otherwise just heals 3 hitpoints.
 */
class NettleTeaEffect : ConsumableEffect() {
    companion object {
        private const val HEALING = 3
    }

    /**
     * Activates healing and possibly energy restoration.
     */
    override fun activate(player: Player) {
        val effect =
            if (player.skills.lifepoints < player.skills.maximumLifepoints) {
                MultiEffect(HealingEffect(3), EnergyEffect(10))
            } else {
                HealingEffect(3)
            }
        effect.activate(player)
    }

    /**
     * Returns fixed healing amount.
     */
    override fun getHealthEffectValue(player: Player): Int = HEALING
}
