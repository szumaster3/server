package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

class RandomEnergyEffect(
    val a: Int,
    val b: Int,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        val effect = EnergyEffect(RandomFunction.random(a, b))
        effect.activate(player)
    }
}
