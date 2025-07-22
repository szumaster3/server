package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.tools.RandomFunction

/**
 * Applies a random boost or reduction to a specified skill.
 *
 * @param skillSlot The skill to affect.
 * @param a Minimum value of skill modification.
 * @param b Maximum value of skill modification.
 */
class RandomSkillEffect(
    val skillSlot: Int,
    val a: Int,
    val b: Int,
) : ConsumableEffect() {

    /**
     * Activates skill modification with a random value between [a] and [b].
     */
    override fun activate(player: Player) {
        val effect = SkillEffect(skillSlot, RandomFunction.random(a, b).toDouble(), 0.0)
        effect.activate(player)
    }
}
