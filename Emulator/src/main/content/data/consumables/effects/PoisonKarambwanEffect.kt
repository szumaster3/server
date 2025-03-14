package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class PoisonKarambwanEffect : ConsumableEffect() {
    override fun activate(player: Player) {
        if (player.getSkills().lifepoints > 5) {
            effect.activate(player)
        }
    }

    override fun getHealthEffectValue(player: Player): Int {
        return HEALING
    }

    companion object {
        private const val HEALING = -5
        private val effect = PoisonEffect(5)
    }
}
