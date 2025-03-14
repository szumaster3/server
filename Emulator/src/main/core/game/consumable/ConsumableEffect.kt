package core.game.consumable

import core.game.node.entity.player.Player

abstract class ConsumableEffect {
    abstract fun activate(player: Player)

    open fun getHealthEffectValue(player: Player): Int = 0
}
