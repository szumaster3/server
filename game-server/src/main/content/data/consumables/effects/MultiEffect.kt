package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Combines multiple consumable effects into one,
 * activating all contained effects sequentially.
 *
 * @param effects The effects to combine.
 */
class MultiEffect(
    vararg effects: ConsumableEffect,
) : ConsumableEffect() {
    private val effects: Array<ConsumableEffect> = effects as Array<ConsumableEffect>

    /**
     * Activates all combined effects on the player.
     */
    override fun activate(player: Player) {
        for (e in effects) {
            e.activate(player)
        }
    }

    /**
     * Sums the health effect values of all combined effects.
     */
    override fun getHealthEffectValue(player: Player): Int {
        var healing = 0
        for (effect in effects) {
            healing += effect.getHealthEffectValue(player)
        }
        return healing
    }
}
