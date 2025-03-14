package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class EnergyEffect(
    amt: Int,
) : ConsumableEffect() {
    var amt: Double = amt.toDouble()

    override fun activate(player: Player) {
        player.settings.updateRunEnergy(-amt)
    }
}
