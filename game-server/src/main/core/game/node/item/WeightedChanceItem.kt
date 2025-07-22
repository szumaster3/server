package core.game.node.item

import core.tools.RandomFunction

/**
 * Represents an item with a weighted chance.
 *
 * @param id The unique identifier for the item.
 * @param minimumAmount The minimum amount of the item to be generated.
 * @param maximumAmount The maximum amount of the item to be generated. Defaults to the minimum amount.
 * @param weight The weight associated with the item, affecting its selection probability.
 */
class WeightedChanceItem(
    val id: Int,
    val minimumAmount: Int,
    val maximumAmount: Int = minimumAmount,
    val weight: Int,
) {
    /**
     * Constructs a `WeightedChanceItem` where the amount of the item is the same for both minimum and maximum.
     *
     * @param id The unique identifier for the item.
     * @param amount The amount of the item to be generated.
     * @param weight The weight associated with the item, affecting its selection probability.
     */
    constructor(id: Int, amount: Int, weight: Int) : this(id, amount, amount, weight)

    /**
     * Generates a random amount of the item within the specified range of minimum and maximum amounts.
     *
     * @return An `Item` instance representing the generated item with a random quantity.
     */
    fun getItem(): Item = Item(id, RandomFunction.random(minimumAmount, maximumAmount))
}
