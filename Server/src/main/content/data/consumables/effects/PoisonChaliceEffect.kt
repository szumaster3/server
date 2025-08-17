package content.data.consumables.effects

import core.api.impact
import core.api.sendMessage
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
        val effectIndex = RandomFunction.nextInt(EFFECTS.size)
        EFFECTS[effectIndex](player)
    }

    /**
     * Calculates healing amount based on player's max lifepoints.
     */
    override fun getHealthEffectValue(player: Player): Int =
        calcHealing(player, 0.14, 20)

    companion object {
        private fun rand(min: Double, max: Double) = RandomFunction.random(min, max)
        private fun calcHealing(player: Player, factor: Double, base: Int): Int =
            (base + (player.skills.maximumLifepoints - 100) * factor).toInt()

        private val EFFECTS: List<(Player) -> Unit> = listOf(
            { player ->
                sendMessage(player, "It has a slight taste of apricot.", 1)
            },
            { player ->
                val boost = rand(1.0, 4.0)
                multiEffect(player,
                    SkillEffect(Skills.ATTACK, -boost, 0.0),
                    SkillEffect(Skills.STRENGTH, -boost, 0.0),
                    SkillEffect(Skills.DEFENCE, -boost, 0.0),
                    SkillEffect(Skills.CRAFTING, 1.0, 0.0),
                    message = "You feel a little better."
                )
            },
            { player ->
                multiEffect(player,
                    SkillEffect(Skills.ATTACK, -1.0, 0.0),
                    SkillEffect(Skills.STRENGTH, -1.0, 0.0),
                    SkillEffect(Skills.DEFENCE, -1.0, 0.0),
                    SkillEffect(Skills.THIEVING, 1.0, 0.0),
                    message = "You feel a little strange."
                )
            },
            { player ->
                multiEffect(player,
                    HealingEffect(calcHealing(player, 0.07, 10)),
                    message = "It heals some health."
                )
            },
            { player ->
                multiEffect(player,
                    HealingEffect(calcHealing(player, 0.14, 20)),
                    SkillEffect(Skills.THIEVING, 1.0, 0.0),
                    message = "You feel a lot better!"
                )
            },
            { player ->
                multiEffect(player,
                    SkillEffect(Skills.ATTACK, 4.0, 0.0),
                    SkillEffect(Skills.STRENGTH, 4.0, 0.0),
                    SkillEffect(Skills.DEFENCE, 4.0, 0.0),
                    SkillEffect(Skills.THIEVING, 1.0, 0.0),
                    message = "Wow! That was amazing! You feel really invigorated."
                )
            },
            { player ->
                val boost = rand(1.0, 2.0)
                multiEffect(player,
                    SkillEffect(Skills.ATTACK, boost, 0.0),
                    SkillEffect(Skills.STRENGTH, boost, 0.0),
                    SkillEffect(Skills.DEFENCE, boost, 0.0),
                    message = "That tasted a bit dodgy. You feel a bit ill."
                )
                impact(player, RandomFunction.random(1, 49), ImpactHandler.HitsplatType.NORMAL)
            }
        )

        private fun multiEffect(player: Player, vararg effects: ConsumableEffect, message: String) {
            MultiEffect(*effects).activate(player)
            sendMessage(player, message, 1)
        }
    }
}