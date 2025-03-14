package content.data.consumables.effects

import core.api.event.isPoisoned
import core.api.heal
import core.api.sendMessage
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction

class GuthixRestEffect : ConsumableEffect() {
    override fun activate(player: Player) {
        val energyRestore = RandomFunction.random(5, 15)

        if (player.skills.lifepoints == player.skills.maximumLifepoints) {
            val hpBoost = SkillEffect(Skills.HITPOINTS, 5.0, 0.0)
            hpBoost.activate(player)
            heal(player, 5)
        } else {
            heal(player, 5)
        }

        if (isPoisoned(player)) {
            heal(player, 1)
        }

        if (player.settings.runEnergy < 100.0) {
            player.settings.updateRunEnergy(energyRestore.toDouble())
        }

        sendMessage(player, "You sip the tea, feeling revitalized.")
    }
}
