package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

class MatureWmbEffect : ConsumableEffect() {
    override fun activate(player: Player) {
        val magicLevelBoost = if (player.getSkills().getLevel(Skills.MAGIC) > 50) 4 else 3
        val effect =
            MultiEffect(
                SkillEffect(Skills.MAGIC, magicLevelBoost.toDouble(), 0.0),
                HealingEffect(HEALING),
                SkillEffect(Skills.ATTACK, -5.0, 0.0),
                SkillEffect(Skills.STRENGTH, -5.0, 0.0),
                SkillEffect(Skills.DEFENCE, -5.0, 0.0),
            )
        effect.activate(player)
    }

    override fun getHealthEffectValue(player: Player): Int {
        return HEALING
    }

    companion object {
        private const val HEALING = 1
    }
}
