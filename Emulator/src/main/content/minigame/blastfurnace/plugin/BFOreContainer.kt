package content.minigame.blastfurnace.plugin

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import content.global.skill.smithing.Bar
import content.minigame.blastfurnace.plugin.BlastFurnace.Companion.getBarForOreId
import content.minigame.blastfurnace.plugin.BlastFurnace.Companion.getNeededCoal
import content.minigame.blastfurnace.plugin.BlastUtils.BAR_LIMIT
import core.game.node.item.Item
import shared.consts.Items

class BFOreContainer {
    private var coalRemaining = 0
    private var ores = Array(BlastUtils.ORE_LIMIT * 2) { -1 }
    private var barAmounts = Array(10) { 0 }

    fun addCoal(amount: Int): Int {
        val maxAdd = BlastUtils.COAL_LIMIT - coalRemaining
        val toAdd = amount.coerceAtMost(maxAdd)
        coalRemaining += toAdd
        return amount - toAdd
    }

    fun coalAmount(): Int = coalRemaining

    fun addOre(
        id: Int,
        amount: Int,
    ): Int {
        if (id == Items.COAL_453) return addCoal(amount)

        var limit = BlastUtils.ORE_LIMIT
        if (getBarForOreId(id, -1, 99) == Bar.BRONZE) limit *= 2

        var amountLeft = amount
        var maxAdd = getAvailableSpace(id, 99)
        for (i in 0 until limit) {
            if (ores[i] == -1) {
                ores[i] = id
                if (--amountLeft == 0 || --maxAdd == 0) break
            }
        }
        return amountLeft
    }

    fun getOreAmount(id: Int): Int {
        if (id == Items.COAL_453) return coalAmount()

        var oreCount = 0
        for (i in 0 until BlastUtils.ORE_LIMIT) {
            if (ores[i] == id) oreCount++
        }
        return oreCount
    }

    fun indexOfOre(id: Int): Int {
        for (i in ores.indices) if (ores[i] == id) return i
        return -1
    }

    fun getOreAmounts(): HashMap<Int, Int> {
        val map = HashMap<Int, Int>()
        for (ore in ores) {
            if (ore == -1) break
            map[ore] = (map[ore] ?: 0) + 1
        }
        return map
    }

    fun convertToBars(level: Int = 99): Double {
        val newOres = Array(BlastUtils.ORE_LIMIT * 2) { -1 }
        var oreIndex = 0
        var xpReward = 0.0
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
        ores = newOres
        return xpReward
    }

    fun getBarAmount(bar: Bar): Int = barAmounts[bar.ordinal]

    fun getTotalBarAmount(): Int {
        var total = 0
        for (i in barAmounts) total += i
        return total
    }

    fun takeBars(
        bar: Bar,
        amount: Int,
    ): Item? {
        val amt = amount.coerceAtMost(barAmounts[bar.ordinal])
        if (amt == 0) return null

        barAmounts[bar.ordinal] -= amt
        return Item(bar.product.id, amt)
    }

    fun getAvailableSpace(
        ore: Int,
        level: Int = 99,
    ): Int {
        if (ore == Items.COAL_453) return BlastUtils.COAL_LIMIT - coalRemaining

        var freeSlots = 0
        val bar = getBarForOreId(ore, coalRemaining, level)!!
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

    fun hasAnyOre(): Boolean = ores[0] != -1

    fun toJson(): JsonObject {
        val root = JsonObject()
        val oresJson = JsonArray()
        val barsJson = JsonArray()

        for (ore in this.ores) oresJson.add(ore)
        for (amount in barAmounts) barsJson.add(amount)

        root.add("ores", oresJson)
        root.add("bars", barsJson)
        root.addProperty("coal", coalRemaining)
        return root
    }

    companion object {
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

            cont.coalRemaining = root.get("coal")?.asInt ?: 0
            return cont
        }
    }
}
