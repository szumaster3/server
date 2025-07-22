package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Restores 15 special points to the player's familiar, if any.
 */
class RestoreSummoningSpecial : ConsumableEffect() {

    /**
     * Reduces the familiar's special points by 15 (restoring special energy).
     */
    override fun activate(player: Player) {
        val f = player.familiarManager.familiar
        f?.updateSpecialPoints(-15)
    }
}
