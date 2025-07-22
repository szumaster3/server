package content.data.consumables.effects

import core.api.heal
import core.api.isPoisoned
import core.api.sendMessage
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction

/**
 * Restores health, run energy, and cures minor poison on use.
 */
class GuthixRestEffect : ConsumableEffect() {

    /**
     * Heals player, boosts HP if at full health,
     * restores run energy, cures poison, and sends a message.
     */
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
