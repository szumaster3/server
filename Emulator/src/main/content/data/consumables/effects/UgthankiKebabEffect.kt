package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class UgthankiKebabEffect : ConsumableEffect() {
    override fun activate(player: Player) {
        if (player.getSkills().lifepoints < player.getSkills().maximumLifepoints) {
            player.sendChat("Yum!")
        }
        effect.activate(player)
    }

    override fun getHealthEffectValue(player: Player): Int {
        return HEALING
    }

    companion object {
        private const val HEALING = 19
        private val effect = HealingEffect(HEALING)
    }
}
