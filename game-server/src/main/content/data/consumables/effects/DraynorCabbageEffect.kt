package content.data.consumables.effects

import core.api.getStatLevel
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

/**
 * Heals the player based on Defence level when used.
 */
class DraynorCabbageEffect : ConsumableEffect() {

    /**
     * Activates healing effect with amount depending on Defence level.
     */
    override fun activate(player: Player) {
        val effect = HealingEffect(getHealthEffectValue(player))
        effect.activate(player)
    }

    /**
     * Gets heal amount: 3 if Defence > 50, else 4.
     */
    override fun getHealthEffectValue(player: Player): Int = if (getStatLevel(player, Skills.DEFENCE) > 50) 3 else 4
}
