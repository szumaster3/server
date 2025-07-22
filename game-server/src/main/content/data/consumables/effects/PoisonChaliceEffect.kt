package content.data.consumables.effects

import core.api.impact
import core.api.sendMessageWithDelay
import core.game.consumable.ConsumableEffect
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction

/**
 * Applies a random mixture of poison-related effects when used,
 * including stat boosts, reductions, healing, and damage.
 */
class PoisonChaliceEffect : ConsumableEffect() {

    /**
     * Activates a random effect with associated message and possible damage.
     */
    override fun activate(player: Player) {
        when (RandomFunction.nextInt(7)) {
            1 -> {
                val boost = rand(1.0, 4.0)
                MultiEffect(
                    SkillEffect(Skills.ATTACK, -boost, 0.0),
                    SkillEffect(Skills.STRENGTH, -boost, 0.0),
                    SkillEffect(Skills.DEFENCE, -boost, 0.0),
                    SkillEffect(Skills.CRAFTING, 1.0, 0.0)
                ).activate(player)
                sendMessageWithDelay(player, "You feel a little better.", 1)
            }
            2 -> {
                MultiEffect(
                    SkillEffect(Skills.ATTACK, -1.0, 0.0),
                    SkillEffect(Skills.STRENGTH, -1.0, 0.0),
                    SkillEffect(Skills.DEFENCE, -1.0, 0.0),
                    SkillEffect(Skills.THIEVING, 1.0, 0.0)
                ).activate(player)
                sendMessageWithDelay(player, "You feel a little strange.", 1)
            }
            3 -> {
                HealingEffect(calcHealing(player, 0.07, 10)).activate(player)
                sendMessageWithDelay(player, "It heals some health.", 1)
            }
            4 -> {
                MultiEffect(
                    HealingEffect(getHealthEffectValue(player)),
                    SkillEffect(Skills.THIEVING, 1.0, 0.0)
                ).activate(player)
                sendMessageWithDelay(player, "You feel a lot better.", 1)
            }
            5 -> {
                MultiEffect(
                    SkillEffect(Skills.ATTACK, 4.0, 0.0),
                    SkillEffect(Skills.STRENGTH, 4.0, 0.0),
                    SkillEffect(Skills.DEFENCE, 4.0, 0.0),
                    SkillEffect(Skills.THIEVING, 1.0, 0.0)
                ).activate(player)
                sendMessageWithDelay(player, "Wow! That was amazing! You feel really invigorated.", 1)
            }
            6 -> {
                val boost = rand(1.0, 2.0)
                MultiEffect(
                    SkillEffect(Skills.ATTACK, boost, 0.0),
                    SkillEffect(Skills.STRENGTH, boost, 0.0),
                    SkillEffect(Skills.DEFENCE, boost, 0.0)
                ).activate(player)
                sendMessageWithDelay(player, "That tasted a bit dodgy. You feel a bit ill.", 1)
                impact(player, hitValue, ImpactHandler.HitsplatType.NORMAL)
            }
            else -> sendMessageWithDelay(player, "It has a slight taste of apricot.", 1)
        }
    }

    /**
     * Calculates healing amount based on player's max lifepoints.
     */
    override fun getHealthEffectValue(player: Player): Int =
        calcHealing(player, 0.14, 20)

    companion object {
        private val hitValue = RandomFunction.random(1, 49)

        private fun rand(min: Double, max: Double) = RandomFunction.random(min, max)

        private fun calcHealing(player: Player, factor: Double, base: Int): Int =
            (base + (player.getSkills().maximumLifepoints - 100) * factor).toInt()
    }
}
