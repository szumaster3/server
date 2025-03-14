package content.data.consumables.effects

import core.api.setAttribute
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks

class SetAttributeEffect : ConsumableEffect {
    private var attrString: String
    private var attrValue: Any
    private var isTicks: Boolean = false

    constructor(attr: String, value: Any, isTicks: Boolean) {
        this.attrString = attr
        this.attrValue = value
        this.isTicks = isTicks
    }

    constructor(attr: String, value: Any) {
        this.attrString = attr
        this.attrValue = value
        this.isTicks = value is Int
    }

    override fun activate(player: Player) {
        if (isTicks) {
            val value = attrValue as Int + ticks
            setAttribute(player, attrString, value)
            return
        }
        setAttribute(player, attrString, attrValue)
    }
}
