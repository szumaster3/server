package core.game.node.item

import core.cache.def.impl.ItemDefinition
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Plugin

/**
 * Base plugin for item-related interactions.
 *
 * @author Vexia
 */
abstract class ItemPlugin : Plugin<Any> {

    companion object {
        /**
         * Constant representing item drop events.
         */
        const val DROP = 1
    }

    /**
     * Handles an event for this plugin.
     */
    override fun fireEvent(identifier: String, vararg args: Any?): Any = this

    /**
     * Registers this plugin for given item ids.
     *
     * @param ids Item IDs to register.
     */
    fun register(vararg ids: Int) {
        for (id in ids) {
            ItemDefinition.forId(id).itemPlugin = this
        }
    }

    /**
     * Removes an item from a player's inventory. Default does nothing.
     */
    open fun remove(player: Player, item: Item, type: Int) {}

    /**
     * Checks if a player can pick up a ground item. Defaults to true.
     */
    open fun canPickUp(player: Player, item: GroundItem, type: Int): Boolean = true

    /**
     * Creates a dropped item at a location. Defaults to true.
     */
    open fun createDrop(item: Item, player: Player, npc: NPC?, location: Location): Boolean = true

    /**
     * Gets the item used in place of the original for interactions.
     */
    open fun getItem(item: Item, npc: NPC?): Item = item

    /**
     * Gets the item used when dropped on death.
     */
    open fun getDeathItem(item: Item): Item = item
}
