package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class NettleTeaEffect : ConsumableEffect() {
    companion object {
        private const val HEALING = 3
    }

    override fun activate(player: Player) {
        val effect =
            if (player.skills.lifepoints < player.skills.maximumLifepoints) {
                MultiEffect(HealingEffect(3), EnergyEffect(10))
            } else {
                HealingEffect(3)
            }
        effect.activate(player)
    }

    override fun getHealthEffectValue(player: Player): Int {
        return HEALING
    }
}
