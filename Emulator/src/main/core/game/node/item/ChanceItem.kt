package core.game.node.item

import core.game.node.entity.npc.drop.DropFrequency
import core.tools.RandomFunction

class ChanceItem
    @JvmOverloads
    constructor(
        id: Int,
        minimumAmount: Int = 1,
        maximumAmount: Int = 1,
        charge: Int = 1000,
        chanceRate: Double = 1.0,
        frequency: DropFrequency? = DropFrequency.UNCOMMON,
        setRate: Int = -1,
    ) : Item(id, minimumAmount, charge) {
        @JvmField var chanceRate: Double = 0.0

        var minimumAmount: Int = 0

        var maximumAmount: Int = 0

        var tableSlot: Int = 0

        @JvmField var dropFrequency: DropFrequency? = null

        var setRate: Int = -1

        constructor(id: Int, minimumAmount: Int, chanceRate: Double) : this(
            id,
            minimumAmount,
            minimumAmount,
            1000,
            chanceRate,
        )

        constructor(id: Int, minimumAmount: Int, maximumAmount: Int, chanceRate: Double) : this(
            id,
            minimumAmount,
            maximumAmount,
            1000,
            chanceRate,
        )

        constructor(id: Int, minimumAmount: Int, maximumAmount: Int, frequency: DropFrequency?) : this(
            id,
            minimumAmount,
            maximumAmount,
            1000,
            DropFrequency.rate(frequency).toDouble(),
        )

        init {
            this.minimumAmount = minimumAmount
            this.maximumAmount = maximumAmount
            this.chanceRate = chanceRate
            this.dropFrequency = frequency
            this.setRate = setRate
        }

        constructor(id: Int, amount: Int, frequency: DropFrequency?) : this(id, amount, amount, frequency)

        val randomItem: Item
            get() {
                if (minimumAmount == maximumAmount) {
                    return Item(id, minimumAmount)
                }
                return Item(id, RandomFunction.random(minimumAmount, maximumAmount))
            }

        val copy: ChanceItem
            get() {
                val newItem = ChanceItem(id, minimumAmount, maximumAmount, charge, chanceRate, dropFrequency)
                return newItem
            }

        override fun toString(): String {
            return "ChanceItem " + super.toString() + " [chanceRate=" + chanceRate + ", minimumAmount=" + minimumAmount +
                ", maximumAmount=" +
                maximumAmount +
                ", tableSlot=" +
                tableSlot +
                ", dropFrequency=" +
                dropFrequency +
                "]"
        }

        companion object {
            val DROP_RATES: IntArray = intArrayOf(5, 15, 150, 750)

            @JvmStatic
            fun getItem(vararg table: ChanceItem): ChanceItem {
                return getItem(RandomFunction.getRandomDouble(75.0), *table)
            }

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
