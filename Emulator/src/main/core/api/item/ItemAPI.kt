package core.api.item

import core.api.inInventory
import core.cache.def.impl.ItemDefinition
import core.game.node.entity.player.Player

/**
 * Retrieves the item definition for a given item id.
 *
 * @param id The id for the item.
 * @return The [ItemDefinition] associated with the provided item id.
 */
fun itemDefinition(id: Int): ItemDefinition {
    return ItemDefinition.forId(id)
}

/**
 * Checks if all specified items are present in the player's inventory.
 *
 * @param player The player whose inventory is being checked.
 * @param ids The ids of the items to check for in the inventory.
 * @return True if all specified items are present in the inventory, otherwise false.
 */
fun allInInventory(
    player: Player,
    vararg ids: Int,
): Boolean {
    return ids.all { id ->
        inInventory(player, id)
    }
}

private class ItemAPI
