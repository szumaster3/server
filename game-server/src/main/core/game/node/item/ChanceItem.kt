package core.game.node.item

import core.game.node.entity.npc.drop.DropFrequency
import core.tools.RandomFunction

/**
 * Represents an item with a chance-rate.
 *
 * @author Emperor
 */
class ChanceItem
@JvmOverloads constructor(
    id: Int,
    minimumAmount: Int = 1,
    maximumAmount: Int = 1,
    charge: Int = 1000,
    chanceRate: Double = 1.0,
    frequency: DropFrequency? = DropFrequency.UNCOMMON,
    setRate: Int = -1,
) : Item(id, minimumAmount, charge) {

    /**
     * The chance rate (probability) of the item dropping.
     */
    @JvmField
    var chanceRate: Double = 0.0

    /**
     * The minimum number of this item to drop.
     */
    var minimumAmount: Int = 0

    /**
     * The maximum number of this item to drop.
     */
    var maximumAmount: Int = 0

    /**
     * The slot number of the loot table that this item is assigned to.
     */
    var tableSlot: Int = 0

    /**
     * The frequency at which this item can drop.
     */
    @JvmField
    var dropFrequency: DropFrequency? = null

    /**
     * A set rate for modifying the drop rate.
     */
    var setRate: Int = -1

    /**
     * Secondary constructor for initializing with a specified chance rate and amount range.
     *
     * @param id The item ID.
     * @param minimumAmount The minimum amount of the item to drop.
     * @param chanceRate The probability of the item dropping.
     */
    constructor(id: Int, minimumAmount: Int, chanceRate: Double) : this(
        id,
        minimumAmount,
        minimumAmount,
        1000,
        chanceRate,
    )

    /**
     * Secondary constructor for initializing with a specified chance rate and amount range.
     *
     * @param id The item ID.
     * @param minimumAmount The minimum amount of the item to drop.
     * @param maximumAmount The maximum amount of the item to drop.
     * @param chanceRate The probability of the item dropping.
     */
    constructor(id: Int, minimumAmount: Int, maximumAmount: Int, chanceRate: Double) : this(
        id,
        minimumAmount,
        maximumAmount,
        1000,
        chanceRate,
    )

    /**
     * Secondary constructor for initializing with a specified drop frequency.
     *
     * @param id The item ID.
     * @param minimumAmount The minimum amount of the item to drop.
     * @param maximumAmount The maximum amount of the item to drop.
     * @param frequency The drop frequency (e.g., COMMON, UNCOMMON).
     */
    constructor(id: Int, minimumAmount: Int, maximumAmount: Int, frequency: DropFrequency?) : this(
        id,
        minimumAmount,
        maximumAmount,
        1000,
        DropFrequency.rate(frequency).toDouble(),
    )

    /**
     * Secondary constructor for initializing with a specified amount and frequency.
     *
     * @param id The item ID.
     * @param amount The amount of the item to drop.
     * @param frequency The drop frequency (e.g., COMMON, UNCOMMON).
     */
    constructor(id: Int, amount: Int, frequency: DropFrequency?) : this(id, amount, amount, frequency)

    init {
        this.minimumAmount = minimumAmount
        this.maximumAmount = maximumAmount
        this.chanceRate = chanceRate
        this.dropFrequency = frequency
        this.setRate = setRate
    }

    /**
     * Returns a random item with a quantity between the minimum and maximum amounts.
     *
     * @return A random item based on the `minimumAmount` and `maximumAmount`.
     */
    val randomItem: Item
        get() {
            if (minimumAmount == maximumAmount) {
                return Item(id, minimumAmount)
            }
            return Item(id, RandomFunction.random(minimumAmount, maximumAmount))
        }

    /**
     * Creates a copy of this `ChanceItem`.
     *
     * @return A new `ChanceItem` with the same properties as this one.
     */
    val copy: ChanceItem
        get() {
            val newItem = ChanceItem(id, minimumAmount, maximumAmount, charge, chanceRate, dropFrequency)
            return newItem
        }

    /**
     * Converts this `ChanceItem` to a string representation.
     *
     * @return A string that represents this `ChanceItem` with its properties.
     */
    override fun toString(): String =
        "ChanceItem " + super.toString() + " [chanceRate=" + chanceRate + ", minimumAmount=" + minimumAmount + ", maximumAmount=" + maximumAmount + ", tableSlot=" + tableSlot + ", dropFrequency=" + dropFrequency + "]"

    companion object {

        /** Drop rates for different types of items. */
        val DROP_RATES: IntArray = intArrayOf(5, 15, 150, 750)

        /**
         * Selects a random item from a list of `ChanceItem` objects based on a random chance.
         *
         * @param table A variable number of `ChanceItem` objects to choose from.
         * @return A randomly selected `ChanceItem` object.
         */
        @JvmStatic
        fun getItem(vararg table: ChanceItem): ChanceItem = getItem(RandomFunction.getRandomDouble(75.0), *table)

        /**
         * Selects a random item from a list of `ChanceItem` objects based on a specific chance.
         *
         * @param chance The chance (probability) to use for selecting an item.
         * @param table A variable number of `ChanceItem` objects to choose from.
         * @return A randomly selected `ChanceItem` object.
         */
        @JvmStatic
        fun getItem(
            chance: Double,
            vararg table: ChanceItem,
        ): ChanceItem {
            val chance = RandomFunction.getRandomDouble(chance)

            val totalChanceRate = table.sumOf { it.chanceRate }

            var cumulativeChance = 0.0
            val normalizedChance = chance * totalChanceRate

            for (item in table) {
                cumulativeChance += item.chanceRate

                if (normalizedChance <= cumulativeChance) {
                    return item
                }
            }

            return table.last()
        }
    }
}
