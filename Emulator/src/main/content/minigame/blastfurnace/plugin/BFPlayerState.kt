package content.minigame.blastfurnace.plugin

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import content.global.skill.smithing.Bar
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.Vars

class BFPlayerState(
    val player: Player,
) {
    var container = BFOreContainer()
    val oresOnBelt = ArrayList<BFBeltOre>()
    var barsNeedCooled = false
        private set

    fun processOresIntoBars(): Boolean {
        if (barsNeedCooled && getVarbit(player, DISPENSER_STATE) == 1) {
            setVarbit(player, DISPENSER_STATE, 2, true)
            return false
        }

        if (getVarbit(player, DISPENSER_STATE) != 0 || !container.hasAnyOre()) return false

        val xpReward = container.convertToBars(getStatLevel(player, Skills.SMITHING))
        if (xpReward > 0) {
            rewardXP(player, Skills.SMITHING, xpReward)
            setVarbit(player, DISPENSER_STATE, 1, true)
            barsNeedCooled = true
            return true
        }

        return false
    }

    fun updateOres() {
        val toRemove = ArrayList<BFBeltOre>()
        for (ore in oresOnBelt) {
            if (ore.tick()) toRemove.add(ore)
        }
        oresOnBelt.removeAll(toRemove)
    }

    fun coolBars() {
        barsNeedCooled = false
        setVarbit(player, DISPENSER_STATE, 3, true)
    }

    fun checkBars() {
        if (getVarbit(player, DISPENSER_STATE) == 3) setVarbit(player, DISPENSER_STATE, 0, true)
    }

    fun hasBarsClaimable(): Boolean = container.getTotalBarAmount() > 0

    fun claimBars(
        bar: Bar,
        amount: Int,
    ): Boolean {
        if (barsNeedCooled) return false

        val maxAmt = amount.coerceAtMost(freeSlots(player))
        if (maxAmt == 0) return false

        val reward = container.takeBars(bar, maxAmt) ?: return false
        addItem(player, reward.id, reward.amount)
        setBarClaimVarbits()
        return true
    }

    fun setBarClaimVarbits() {
        for (bar in Bar.values()) {
            val amount = container.getBarAmount(bar)
            val varbit = getVarbitForBar(bar)
            if (varbit == 0) continue

            if (getVarbit(player, varbit) == amount) continue

            setVarbit(player, varbit, amount, true)
        }

        var totalCoalNeeded = 0
        val level = getStatLevel(player, Skills.SMITHING)
        for ((id, amount) in container.getOreAmounts()) {
            val barType = BlastFurnace.getBarForOreId(id, container.coalAmount(), level)
            totalCoalNeeded += BlastFurnace.getNeededCoal(barType!!) * amount
        }

        setVarbit(player, COAL_NEEDED, (totalCoalNeeded - container.coalAmount()).coerceAtLeast(0))
    }

    private fun getVarbitForBar(bar: Bar): Int =
        when (bar) {
            Bar.BRONZE -> BRONZE_COUNT
            Bar.IRON -> IRON_COUNT
            Bar.STEEL -> STEEL_COUNT
            Bar.MITHRIL -> MITHRIL_COUNT
            Bar.ADAMANT -> ADDY_COUNT
            Bar.RUNITE -> RUNITE_COUNT
            Bar.GOLD -> GOLD_COUNT
            Bar.SILVER -> SILVER_COUNT
            Bar.PERFECT_GOLD -> PERF_GOLD_COUNT
            else -> 0
        }

    fun toJson(): JsonObject {
        val save = JsonObject()
        save.add("bf-ore-cont", container.toJson())

        val beltOres = JsonArray()
        for (ore in oresOnBelt) {
            beltOres.add(ore.toJson())
        }

        if (beltOres.size() > 0) {
            save.add("bf-belt-ores", beltOres)
        }
        save.addProperty("barsHot", barsNeedCooled)

        return save
    }

    fun readJson(data: JsonObject) {
        oresOnBelt.clear()
        if (data.has("bf-ore-cont")) {
            val contJson = data.getAsJsonObject("bf-ore-cont")
            container = BFOreContainer.fromJson(contJson)
        }
        if (data.has("bf-belt-ores")) {
            val beltArray = data.getAsJsonArray("bf-belt-ores")
            for (oreElem in beltArray) {
                val oreInfo = oreElem.asJsonObject
                val id = oreInfo.get("id").asInt
                val amount = oreInfo.get("amount").asInt
                val location = Location.fromString(oreInfo.get("location").asString)
                val ore = BFBeltOre(player, id, amount, location)
                oresOnBelt.add(ore)
            }
        }
        if (data.has("barsHot")) {
            barsNeedCooled = data.get("barsHot").asBoolean
        }
    }

    companion object {
        const val DISPENSER_STATE = Vars.VARBIT_BF_DISPENSER_STATE_936
        const val COAL_NEEDED     = Vars.VARBIT_BF_COAL_NEEDED_940
        const val BRONZE_COUNT    = Vars.VARBIT_BF_BRONZE_COUNT_941
        const val IRON_COUNT      = Vars.VARBIT_BF_IRON_COUNT_942
        const val STEEL_COUNT     = Vars.VARBIT_BF_STEEL_COUNT_943
        const val MITHRIL_COUNT   = Vars.VARBIT_BF_MITHRIL_COUNT_944
        const val ADDY_COUNT      = Vars.VARBIT_BF_ADDY_COUNT_945
        const val RUNITE_COUNT    = Vars.VARBIT_BF_RUNITE_COUNT_946
        const val GOLD_COUNT      = Vars.VARBIT_BF_GOLD_COUNT_947
        const val SILVER_COUNT    = Vars.VARBIT_BF_SILVER_COUNT_948
        const val PERF_GOLD_COUNT = Vars.VARBIT_BF_PERFECT_GOLD_COUNT_958
        const val COAL_TOTAL      = Vars.VARBIT_BF_COAL_TOTAL_949
    }
}
