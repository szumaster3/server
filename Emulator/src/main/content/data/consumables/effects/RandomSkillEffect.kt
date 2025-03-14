package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

class RandomSkillEffect(
    val skillSlot: Int,
    val a: Int,
    val b: Int,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        val effect = SkillEffect(skillSlot, RandomFunction.random(a, b).toDouble(), 0.0)
        effect.activate(player)
    }
}
