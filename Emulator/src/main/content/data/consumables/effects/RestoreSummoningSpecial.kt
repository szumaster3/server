package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class RestoreSummoningSpecial : ConsumableEffect() {
    override fun activate(player: Player) {
        val f = player.familiarManager.familiar
        f?.updateSpecialPoints(-15)
    }
}
