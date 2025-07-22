package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

/**
 * Applies a combined effect of healing and stat changes when consumed.
 *
 * Heals 15 hitpoints, boosts Strength by 10,
 * but reduces Attack by 40.
 */
class KegOfBeerEffect : ConsumableEffect() {

    /**
     * Activates healing and skill effects on the player.
     */
    override fun activate(player: Player) {
        val effect =
            MultiEffect(
                HealingEffect(15),
                SkillEffect(Skills.STRENGTH, 10.0, 0.0),
                SkillEffect(Skills.ATTACK, -40.0, 0.0),
            )
        effect.activate(player)
    }
}
