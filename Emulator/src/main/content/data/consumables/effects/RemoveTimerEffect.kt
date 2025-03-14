package content.data.consumables.effects

import core.api.removeTimer
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class RemoveTimerEffect(
    val identifier: String,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        removeTimer(player, identifier)
    }
}
