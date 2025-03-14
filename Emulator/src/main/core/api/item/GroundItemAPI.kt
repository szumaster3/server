package core.api.item

import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location

/**
 * Produces a ground item at the player's location.
 *
 * @param player The player for whom the ground item will be produced.
 * @param item The item id of the ground item to be created.
 */
fun produceGroundItem(
    player: Player,
    item: Int,
) {
    GroundItemManager.create(Item(item), player)
}

/**
 * Produces a ground item at a specified location.
 *
 * @param owner The player who owns the ground item, or null if the item has no owner.
 * @param id The item id of the ground item to be created.
 * @param amount The quantity of the item to be created.
 * @param location The location where the ground item will be placed.
 * @return The created [GroundItem] at the specified location.
 */
fun produceGroundItem(
    owner: Player?,
    id: Int,
    amount: Int,
    location: Location,
): GroundItem {
    return GroundItemManager.create(Item(id, amount), location, owner)
}

/**
 * Removes a ground item from the game world.
 *
 * @param node The [GroundItem] to be removed from the game world.
 */
fun removeGroundItem(node: GroundItem) {
    GroundItemManager.destroy(node)
}

/**
 * Checks if a ground item is valid and exists in the game world.
 *
 * @param node The [GroundItem] to be checked for validity.
 * @return True if the ground item exists in the world, otherwise false.
 */
fun isValidGroundItem(node: GroundItem): Boolean {
    return GroundItemManager.getItems().contains(node)
}

private class GroundItemAPI
