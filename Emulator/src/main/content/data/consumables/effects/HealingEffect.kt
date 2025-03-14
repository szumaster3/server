package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class HealingEffect(
    var amt: Int,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        player.getSkills().heal(amt)
    }

    override fun getHealthEffectValue(player: Player): Int {
        return amt
    }
}
