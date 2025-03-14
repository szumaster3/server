package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction

class SuperKebabEffect : ConsumableEffect() {
    override fun activate(player: Player) {
        if (RandomFunction.nextInt(8) < 5) {
            healingEffect.activate(player)
        }

        if (RandomFunction.nextInt(32) < 1) {
            val effect = SkillEffect(RandomFunction.nextInt(Skills.NUM_SKILLS), -1.0, 0.0)
            effect.activate(player)
        }
    }

    override fun getHealthEffectValue(player: Player): Int {
        return if (RandomFunction.nextInt(8) < 5) (3 + (player.getSkills().maximumLifepoints * 0.07)).toInt() else 0
    }

    companion object {
        private val healingEffect = MultiEffect(HealingEffect(3), PercentageHealthEffect(7))
    }
}
