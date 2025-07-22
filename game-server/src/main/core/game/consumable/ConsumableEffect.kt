package core.game.consumable

import core.game.node.entity.player.Player

/**
 * Handles effects applied when a consumable is used.
 */
abstract class ConsumableEffect {

    /**
     * Activates the effect.
     *
     * @param player The player to apply the effect to.
     */
    abstract fun activate(player: Player)

    /**
     * Gets the health impact value.
     *
     * @param player The player affected.
     * @return Health effect value.
     */
    open fun getHealthEffectValue(player: Player): Int = 0
}