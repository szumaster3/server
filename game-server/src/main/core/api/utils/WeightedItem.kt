package core.api.utils

import core.game.node.item.Item
import core.tools.RandomFunction

/**
 * Represents an item with a weighted chance to drop, including
 * the range of amounts and whether the drop is guaranteed.
 *
 * @property id The item id.
 * @property minAmt The minimum amount of the item to drop.
 * @property maxAmt The maximum amount of the item to drop.
 * @property weight The weight determining the likelihood of this drop.
 * @property guaranteed If true, the item always drops regardless of weight.
 */
class WeightedItem(
    var id: Int,
    var minAmt: Int,
    var maxAmt: Int,
    var weight: Double,
    var guaranteed: Boolean = false,
) {
    /**
     * Generates an [Item] instance with a random amount within [minAmt], [maxAmt].
     *
     * @return A new Item with randomized amount.
     */
    fun getItem(): Item = Item(id, RandomFunction.random(minAmt, maxAmt))
}
