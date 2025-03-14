package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class MultiEffect(
    vararg effects: ConsumableEffect,
) : ConsumableEffect() {
    private val effects: Array<ConsumableEffect> = effects as Array<ConsumableEffect>

    override fun activate(player: Player) {
        for (e in effects) {
            e.activate(player)
        }
    }

    override fun getHealthEffectValue(player: Player): Int {
        var healing = 0
        for (effect in effects) {
            healing += effect.getHealthEffectValue(player)
        }
        return healing
    }
}
