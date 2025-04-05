package core.game.node.item

import core.cache.def.impl.ItemDefinition
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Plugin

/**
 * Represents a base plugin for handling item-related interactions.
 *
 * @see Plugin
 */
abstract class ItemPlugin : Plugin<Any> {

    companion object {
        /**
         * Constant for representing item drop events.
         */
        const val DROP = 1
    }

    /**
     * Fired when an event is triggered for the item plugin.
     *
     * @param identifier The unique identifier for the event being triggered.
     * @param args The arguments associated with the event.
     * @return The current instance of the `ItemPlugin`.
     */
    override fun fireEvent(
        identifier: String,
        vararg args: Any?,
    ): Any = this

    /**
     * Registers this plugin to handle specific item ids.
     *
     * @param ids The item id that this plugin will handle.
     * @see ItemDefinition
     */
    fun register(vararg ids: Int) {
        for (id in ids) {
            ItemDefinition.forId(id).itemPlugin = this
        }
    }

    /**
     * Removes an item from a player inventory.
     *
     * @param player The player who is interacting with the item.
     * @param item The item being removed.
     * @param type The type of removal (e.g., consumption, discard).
     */
    open fun remove(
        player: Player,
        item: Item,
        type: Int,
    ) {
        // Default implementation can be overridden in subclasses.
    }

    /**
     * Determines whether a player can pick up an item from the ground.
     *
     * @param player The player attempting to pick up the item.
     * @param item The item being considered for pickup.
     * @param type The type of interaction (e.g., pickup, trade).
     * @return True if the player can pick up the item, false otherwise.
     */
    open fun canPickUp(
        player: Player,
        item: GroundItem,
        type: Int,
    ): Boolean = true

    /**
     * Creates a drop of an item at a specific location when an item is dropped by a player or NPC.
     *
     * @param item The item being dropped.
     * @param player The player dropping the item.
     * @param npc The NPC that may have triggered the drop (can be null).
     * @param location The location where the item should be dropped.
     * @return True if the drop was successfully created, false otherwise.
     */
    open fun createDrop(
        item: Item,
        player: Player,
        npc: NPC?,
        location: Location,
    ): Boolean = true

    /**
     * Returns the item that should be used in place of the original item when picked up.
     *
     * @param item The item being interacted with.
     * @param npc The NPC that may be interacting with the item (can be null).
     * @return The item to be used for the interaction.
     */
    open fun getItem(
        item: Item,
        npc: NPC?,
    ): Item = item

    /**
     * Returns the item that should be used when an item is dropped upon the death of an NPC or player.
     *
     * @param item The item to be used for the death drop.
     * @return The item to be used as the death drop.
     */
    open fun getDeathItem(item: Item): Item = item
}
