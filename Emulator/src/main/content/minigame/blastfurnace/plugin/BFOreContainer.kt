package content.minigame.blastfurnace.plugin

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import content.global.skill.smithing.Bar
import content.minigame.blastfurnace.plugin.BlastFurnace.Companion.getBarForOreId
import content.minigame.blastfurnace.plugin.BlastFurnace.Companion.getNeededCoal
import content.minigame.blastfurnace.plugin.BlastUtils.BAR_LIMIT
import core.game.node.item.Item
import shared.consts.Items
import core.game.node.entity.player.Player
import core.api.setVarbit
import core.api.getVarbit

/**
 * Represents a container for ores and bars in the Blast Furnace minigame.
 */
class BFOreContainer {

    /**
     * Array of ores stored in the container.
     */
    private var ores = Array(BlastUtils.ORE_LIMIT * 2) { -1 }

    /**
     * Array storing the number of bars produced per bar type.
     */
    private var barAmounts = Array(10) { 0 }

    /**
     * Gets the amount of coal in the container for the given player.
     *
     * @param player The player whose coal amount to retrieve.
     * @return The amount of coal in the container.
     */
    fun coalAmount(player: Player): Int =
        getVarbit(player, BlastUtils.COAL_LIMIT)

    /**
     * Adds coal to the container, respecting the coal limit
     * and updates the varbit.
     *
     * @param player The player adding coal.
     * @param amount The amount of coal to add.
     * @return The amount of coal that could not be added (overflow).
     */
    fun addCoal(player: Player, amount: Int): Int {
        val coalRemaining = coalAmount(player)
        val coalLimit = BlastUtils.COAL_LIMIT
        val maxAdd = coalLimit - coalRemaining
        val toAdd = amount.coerceAtMost(maxAdd)
        setVarbit(player, BlastUtils.COAL_LIMIT, coalRemaining + toAdd, true)
        return amount - toAdd
    }

    /**
     * Adds ores to the container. If coal is added, delegates to [addCoal].
     *
     * @param player The player adding ores.
     * @param id The item id of the ore.
     * @param amount The amount of ore to add.
     * @return The amount of ore that could not be added (overflow).
     */
    fun addOre(player: Player, id: Int, amount: Int): Int {
        if (id == Items.COAL_453) return addCoal(player, amount)

        var limit = BlastUtils.ORE_LIMIT
        if (getBarForOreId(id, -1, 99) == Bar.BRONZE) limit *= 2

        var amountLeft = amount
        var maxAdd = getAvailableSpace(player, id, 99)
        for (i in 0 until limit) {
            if (ores[i] == -1) {
                ores[i] = id
                if (--amountLeft == 0 || --maxAdd == 0) break
            }
        }
        return amountLeft
    }

    /**
     * Returns the amount of a specific ore in the container.
     *
     * @param player The player checking the ore.
     * @param id The item id of the ore.
     * @return The quantity of the specified ore.
     */
    fun getOreAmount(player: Player, id: Int): Int {
        if (id == Items.COAL_453) return coalAmount(player)

        var oreCount = 0
        for (i in 0 until BlastUtils.ORE_LIMIT) {
            if (ores[i] == id) oreCount++
        }
        return oreCount
    }

    /** Finds the index of the specified ore in the container. */
    private fun indexOfOre(id: Int): Int {
        for (i in ores.indices) if (ores[i] == id) return i
        return -1
    }

    /**
     * Gets a map of ore ids to their amounts in the container.
     *
     * @return A [HashMap] mapping ore ids to quantities.
     */
    fun getOreAmounts(): HashMap<Int, Int> {
        val map = HashMap<Int, Int>()
        for (ore in ores) {
            if (ore == -1) break
            map[ore] = (map[ore] ?: 0) + 1
        }
        return map
    }

    /**
     * Converts ores in the container to bars, consuming coal as required.
     *
     * @param player The player performing the conversion.
     * @param level The smithing level of the player (default 99).
     * @return The total experience gained from the conversion.
     */
    fun convertToBars(player: Player, level: Int = 99): Double {
        val newOres = Array(BlastUtils.ORE_LIMIT * 2) { -1 }
        var oreIndex = 0
        var xpReward = 0.0
        var coalRemaining = coalAmount(player)

        for (i in 0 until BlastUtils.ORE_LIMIT) {
            val bar = getBarForOreId(ores[i], coalRemaining, level)

            if (bar == null) {
                ores[i] = -1
                continue
            }

            if (barAmounts[bar.ordinal] >= BAR_LIMIT) {
                newOres[oreIndex++] = ores[i]
                continue
            }

            val coalNeeded = getNeededCoal(bar)

            if (bar == Bar.BRONZE) {
                val indexOfComplement =
                    when (ores[i]) {
                        Items.COPPER_ORE_436 -> indexOfOre(Items.TIN_ORE_438)
                        Items.TIN_ORE_438 -> indexOfOre(Items.COPPER_ORE_436)
                        else -> -1
                    }
                if (indexOfComplement == -1) {
                    newOres[oreIndex++] = ores[i]
                    continue
                }
                ores[indexOfComplement] = -1
            }

            if (coalRemaining >= coalNeeded) {
                coalRemaining -= coalNeeded
                ores[i] = -1
                barAmounts[bar.ordinal]++
                xpReward += bar.experience
            } else {
                newOres[oreIndex++] = ores[i]
            }
        }

        setVarbit(player, BlastUtils.COAL_LIMIT, coalRemaining, true)
        ores = newOres
        return xpReward
    }

    /**
     * Gets the number of bars for the given bar type.
     */
    fun getBarAmount(bar: Bar): Int = barAmounts[bar.ordinal]

    /**
     * Gets the total number of bars across all bar types.
     */
    fun getTotalBarAmount(): Int = barAmounts.sum()

    /**
     * Takes a amount of bars from the container.
     *
     * @param bar The type of bar to take.
     * @param amount The number of bars to take.
     * @return An [Item] containing the bar id taken, or null if none available.
     */
    fun takeBars(bar: Bar, amount: Int): Item? {
        val amt = amount.coerceAtMost(barAmounts[bar.ordinal])
        if (amt == 0) return null
        barAmounts[bar.ordinal] -= amt
        return Item(bar.product.id, amt)
    }

    /**
     * Calculates the available space for a given ore or coal.
     *
     * @param player The player checking available space.
     * @param ore The item id of the ore.
     * @param level The player's smithing level (default 99).
     * @return The number of slots available for the ore.
     */
    fun getAvailableSpace(player: Player, ore: Int, level: Int = 99): Int {
        if (ore == Items.COAL_453) return BlastUtils.COAL_LIMIT - coalAmount(player)

        var freeSlots = 0
        val bar = getBarForOreId(ore, coalAmount(player), level)!!
        val oreAmounts = HashMap<Int, Int>()
        for (i in 0 until BlastUtils.ORE_LIMIT) {
            if (ores[i] == -1) {
                var oreLimit = BlastUtils.ORE_LIMIT
                if (bar == Bar.BRONZE) oreLimit *= 2
                freeSlots = oreLimit - i
                break
            } else {
                oreAmounts[ores[i]] = (oreAmounts[ores[i]] ?: 0) + 1
            }
        }

        val currentAmount = oreAmounts[ore] ?: 0
        freeSlots = (BlastUtils.ORE_LIMIT - currentAmount).coerceAtMost(freeSlots)
        return (freeSlots - getBarAmount(bar)).coerceAtLeast(0)
    }

    /**
     * Check if the container has any ores stored.
     */
    fun hasAnyOre(): Boolean = ores[0] != -1

    /**
     * Serializes the container to a JSON object.
     *
     * @return A [JsonObject] representing ores, bars, and coal.
     */
    fun toJson(): JsonObject {
        val root = JsonObject()
        val oresJson = JsonArray()
        val barsJson = JsonArray()

        for (ore in this.ores) oresJson.add(ore)
        for (amount in barAmounts) barsJson.add(amount)

        root.add("ores", oresJson)
        root.add("bars", barsJson)
        root.addProperty("coal", 0)
        return root
    }

    companion object {
        /**
         * Deserializes a [BFOreContainer] from a JSON object.
         *
         * @param root The [JsonObject] containing container data.
         * @return A new instance of [BFOreContainer].
         */
        fun fromJson(root: JsonObject): BFOreContainer {
            val cont = BFOreContainer()
            val jsonOres = root.getAsJsonArray("ores")
            val jsonBars = root.getAsJsonArray("bars")

            for (i in 0 until BlastUtils.ORE_LIMIT) {
                cont.ores[i] = jsonOres.get(i)?.asInt ?: -1
            }
            for (i in 0 until 10) {
                cont.barAmounts[i] = jsonBars.get(i)?.asInt ?: 0
            }

            return cont
        }
    }
}
