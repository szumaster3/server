package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

/**
 * Applies a mixed effect: boosts Magic skill slightly, heals a small amount,
 * and reduces Attack, Strength, and Defence skills.
 */
class WizardsMindBombEffect : ConsumableEffect() {

    /**
     * Activates the multi-effect with conditional Magic boost and fixed healing/debuffs.
     */
    override fun activate(player: Player) {
        val magicLevelBoost = if (player.getSkills().getLevel(Skills.MAGIC) > 50) 3 else 2
        val effect =
            MultiEffect(
                SkillEffect(Skills.MAGIC, magicLevelBoost.toDouble(), 0.0),
                HealingEffect(HEALING),
                SkillEffect(Skills.ATTACK, -3.0, 0.0),
                SkillEffect(Skills.STRENGTH, -4.0, 0.0),
                SkillEffect(Skills.DEFENCE, -4.0, 0.0),
            )
        effect.activate(player)
    }

    /**
     * Returns the healing amount.
     */
    override fun getHealthEffectValue(player: Player): Int = HEALING

    companion object {
        private const val HEALING = 1
    }
}
