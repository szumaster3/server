package core.api.utils

import core.tools.RandomFunction

/**
 * Represents a weighted table of items where each item is associated with a weight.
 * The table allows you to add items with weights, remove items, and roll an item randomly based on its weight.
 */
class WeightedTable<T> : ArrayList<Pair<T?, Double>>() {
    // The total weight of all the elements in the table.
    var totalWeight: Double = 0.0

    /**
     * Adds an item to the table with the specified weight.
     *
     * @param element The item to add.
     * @param weight The weight associated with the item.
     * @return True if the item was successfully added, false otherwise.
     */
    fun add(
        element: T?,
        weight: Double,
    ): Boolean {
        totalWeight += weight
        return super.add(Pair(element, weight))
    }

    /**
     * Removes an item from the table.
     *
     * @param element The item to remove.
     * @return True if the item was removed, false otherwise.
     */
    fun remove(element: T?): Boolean {
        var index = -1
        for ((i, pair) in this.withIndex()) {
            val (elem, _) = pair
            if (element == elem) {
                index = i
                break
            }
        }
        if (index == -1) return false

        this.removeAt(index)
        return true
    }

    /**
     * Removes an item at the specified index.
     * The total weight of the table is updated accordingly.
     *
     * @param index The index of the item to remove.
     * @return The pair of the removed element and its weight.
     */
    override fun removeAt(index: Int): Pair<T?, Double> {
        val (_, weight) = this[index]
        totalWeight -= weight
        return super.removeAt(index)
    }

    /**
     * Rolls the table and returns a randomly selected item based on the weight distribution.
     * Items with higher weights are more likely to be selected.
     *
     * @return The selected item, or null if the table is empty.
     */
    fun roll(): T? {
        if (this.size == 1) {
            return this[0].component1()
        } else if (this.size == 0) {
            return null
        }

        var shuffled = this.shuffled() // Shuffle to avoid bias
        var randWeight = RandomFunction.random(0.0, totalWeight)

        for ((element, weight) in shuffled) {
            randWeight -= weight
            if (randWeight <= 0) return element
        }

        return null
    }

    companion object {
        /**
         * Creates a new [WeightedTable] with the specified items and their weights.
         *
         * @param elements A variable number of pairs of items and their associated weights.
         * @return A new [WeightedTable] instance.
         */
        fun <T> create(vararg elements: Pair<T?, Double>): WeightedTable<T> {
            var table = WeightedTable<T>()
            for ((element, weight) in elements) table.add(element, weight)
            return table
        }
    }
}
