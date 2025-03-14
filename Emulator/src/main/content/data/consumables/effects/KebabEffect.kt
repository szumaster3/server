package content.data.consumables.effects

import core.api.sendMessage
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction

class KebabEffect : ConsumableEffect() {
    override fun activate(player: Player) {
        val randomNumber = RandomFunction.nextInt(100)
        val effect: ConsumableEffect
        if (randomNumber < 66) {
            effect = PercentageHealthEffect(10)
            val initialLifePoints = player.getSkills().lifepoints
            effect.activate(player)
            if (player.getSkills().lifepoints > initialLifePoints) {
                sendMessage(player, "It heals some health.")
            }
        } else if (randomNumber < 87) {
            effect = RandomHealthEffect(10, 20)
            effect.activate(player)
            sendMessage(player, "That was a good kebab. You feel a lot better.")
        } else if (randomNumber < 96) {
            if (RandomFunction.nextInt(100) < 50) {
                val affectedSkillSlot = RandomFunction.nextInt(Skills.NUM_SKILLS - 1)
                effect =
                    when (affectedSkillSlot) {
                        Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH ->
                            MultiEffect(
                                SkillEffect(Skills.ATTACK, -3.0, 0.0),
                                SkillEffect(Skills.DEFENCE, -3.0, 0.0),
                                SkillEffect(Skills.STRENGTH, -3.0, 0.0),
                            )

                        else -> SkillEffect(affectedSkillSlot, -3.0, 0.0)
                    }
                sendMessage(player, "That tasted a bit dodgy. You feel a bit ill.")
                effect.activate(player)
            } else {
                sendMessage(player, "That kebab didn't seem to do a lot.")
            }
        } else {
            effect =
                MultiEffect(
                    HealingEffect(30),
                    RandomSkillEffect(Skills.ATTACK, 1, 3),
                    RandomSkillEffect(Skills.DEFENCE, 1, 3),
                    RandomSkillEffect(Skills.STRENGTH, 1, 3),
                )
            effect.activate(player)
            sendMessage(player, "Wow, that was an amazing kebab! You feel really invigorated.")
        }
    }
}
