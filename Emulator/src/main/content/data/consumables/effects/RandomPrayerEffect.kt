package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

class RandomPrayerEffect(
    val a: Int,
    val b: Int,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        val effect = PrayerEffect(RandomFunction.random(a, b).toDouble(), 0.0)
        effect.activate(player)
    }
}
