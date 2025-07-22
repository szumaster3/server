package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Heals the player by 19 hitpoints.
 */
class UgthankiKebabEffect : ConsumableEffect() {

    /**
     * Sends a chat message if the player is injured, then applies healing.
     */
    override fun activate(player: Player) {
        if (player.getSkills().lifepoints < player.getSkills().maximumLifepoints) {
            player.sendChat("Yum!")
        }
        effect.activate(player)
    }

    /**
     * Returns the healing amount.
     */
    override fun getHealthEffectValue(player: Player): Int = HEALING

    companion object {
        private const val HEALING = 19
        private val effect = HealingEffect(HEALING)
    }
}
