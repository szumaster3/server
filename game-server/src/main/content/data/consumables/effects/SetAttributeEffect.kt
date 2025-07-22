package content.data.consumables.effects

import core.api.setAttribute
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks

/**
 * Sets a player attribute to a specified value or to a value plus current game ticks.
 *
 * @param attrString The attribute key.
 * @param attrValue The value to set for the attribute.
 * @param isTicks If true, adds current game ticks to [attrValue] (assumed Int).
 */
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

    /**
     * Sets the attribute on the player, optionally adding ticks if [isTicks] is true.
     */
    override fun activate(player: Player) {
        if (isTicks) {
            val value = attrValue as Int + ticks
            setAttribute(player, attrString, value)
            return
        }
        setAttribute(player, attrString, attrValue)
    }
}
