package content.global.skill.summoning.objectpets

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.tools.colorize
import core.tools.ticksToSeconds

class IncubatorTimer : PersistTimer(500, "incubation") {
    val incubatingEggs = HashMap<Int, IncubatingEgg>()

    override fun getInitialRunDelay(): Int = 50

    override fun parse(
        root: JsonObject,
        entity: Entity,
    ) {
        val eggs = root.getAsJsonArray("eggs") ?: return
        for (eggElement in eggs) {
            val eggInfo = eggElement.asJsonArray
            val egg = IncubatingEgg(
                eggInfo[0].asInt,
                IncubatorEgg.values()[eggInfo[1].asInt],
                eggInfo[2].asLong,
                eggInfo[3].asBoolean,
            )
            incubatingEggs[egg.region] = egg
        }
    }

    override fun save(
        root: JsonObject,
        entity: Entity,
    ) {
        val arr = JsonArray()
        for ((_, eggInfo) in incubatingEggs) {
            val eggArr = JsonArray()
            eggArr.add(eggInfo.region)
            eggArr.add(eggInfo.egg.ordinal)
            eggArr.add(eggInfo.endTime)
            eggArr.add(eggInfo.finished)
            arr.add(eggArr)
        }
        root.add("eggs", arr)
    }

    override fun onRegister(entity: Entity) {
        if (entity !is Player) return
        for ((region, _) in incubatingEggs) {
            setVarbit(entity.asPlayer(), varbitForRegion(region), 1, true)
        }
    }

    override fun run(entity: Entity): Boolean {
        if (entity !is Player) return false
        for ((_, egg) in incubatingEggs) {
            if (egg.finished) continue
            if (egg.isDone()) {
                sendMessage(entity, colorize("%RYour ${egg.egg.product.name.lowercase()} egg has finished hatching."))
                egg.finished = true
            }
        }
        return !incubatingEggs.isEmpty()
    }

    data class IncubatingEgg(
        val region: Int,
        val egg: IncubatorEgg,
        var endTime: Long,
        var finished: Boolean = false,
    )

    fun IncubatingEgg.isDone(): Boolean = endTime < System.currentTimeMillis()

    companion object {
        val TAVERLEY_REGION = 11573
        val TAVERLEY_VARBIT = 4277
        val YANILLE_REGION = 10288
        val YANILLE_VARBIT = 4221

        fun varbitForRegion(region: Int): Int = when (region) {
            TAVERLEY_REGION -> TAVERLEY_VARBIT
            YANILLE_REGION -> YANILLE_VARBIT
            else -> -1
        }

        fun getEggFor(
            player: Player,
            region: Int,
        ): IncubatingEgg? {
            val playerTimer = getTimer<IncubatorTimer>(player) ?: return null
            return playerTimer.incubatingEggs[region]
        }

        fun registerEgg(
            player: Player,
            region: Int,
            egg: IncubatorEgg,
        ) {
            val timer = getTimer<IncubatorTimer>(player) ?: IncubatorTimer()
            timer.incubatingEggs[region] = IncubatingEgg(
                region,
                egg,
                System.currentTimeMillis() + (ticksToSeconds(egg.incubationTime * 100) * 1000),
            )
            if (!hasTimerActive<IncubatorTimer>(player)) {
                registerTimer(player, timer)
            }
            setVarbit(player, varbitForRegion(region), 1, true)
        }

        fun removeEgg(
            player: Player,
            region: Int,
        ): IncubatorEgg? {
            val egg = getEggFor(player, region) ?: return null
            val timer = getTimer<IncubatorTimer>(player) ?: return null
            timer.incubatingEggs.remove(region)
            setVarbit(player, varbitForRegion(region), 0, true)
            return egg.egg
        }
    }
}
