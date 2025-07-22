package content.global.skill.cooking.brewing

import core.api.log
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.tools.Log
import core.tools.secondsToTicks
import org.json.simple.JSONArray
import org.json.simple.JSONObject

const val cycleTime = 12 * 60 * 60 * 1_000

/**
 * Represents brew growth timer.
 *
 * @author GregF
 */
class BrewGrowth : PersistTimer(cycleTime, "cooking:brewing", isSoft = true) {
    private val fermentingVatMap = HashMap<Int, FermentingVat>()
    lateinit var player: Player

    override fun onRegister(entity: Entity) {
        player = (entity as? Player)!!
        for (vat in fermentingVatMap) {
            catchUp(vat.value.nextBrew)
        }
    }

    override fun save(root: JSONObject, entity: Entity) {
        player = (entity as? Player)!!
        val vats = JSONArray()
        for ((key, vat) in fermentingVatMap) {
            val v = JSONObject()
            v["brew-ordinal"] = key
            v["brew-stuff"] = vat.theStuff
            v["brewing-nextBrew"] = vat.nextBrew
            v["brewing-stage"] = vat.stage.ordinal
            v["brewing-item"] = vat.brewingItem?.ordinal
            vats.add(v)
        }
        root["brewing"] = vats
    }

    override fun parse(root: JSONObject, entity: Entity) {
        player = (entity as? Player)!!
        val data = root["brewing"] as JSONArray
        for (d in data) {
            val v = d as JSONObject
            val vatOrdinal = v["brew-ordinal"].toString().toInt()
            val vatStuff = v["brew-stuff"].toString().toBoolean()
            val vatNextBrew = v["brewing-nextBrew"].toString().toLong()
            val vatStage = BrewingStage.values()[v["brewing-stage"].toString().toInt()]
            val vatItem = v["brewing-item"]?.toString()?.toIntOrNull()?.let {
                Brewable.values().getOrNull(it)
            }

            if (fermentingVatMap[vatOrdinal] == null) {
                val brewingVat = BrewingVat.values()[vatOrdinal]
                val fermentingVat = FermentingVat(player, brewingVat, vatStuff, vatNextBrew, vatStage, vatItem)
                fermentingVatMap[vatOrdinal] = fermentingVat
                fermentingVat.updateVat()
            }
        }
    }

    fun getVat(brewingVat: BrewingVat, addVat: Boolean = true): FermentingVat {
        return fermentingVatMap[brewingVat.ordinal] ?: (FermentingVat(player, brewingVat).also {
            if (addVat) fermentingVatMap[brewingVat.ordinal] = it
        })
    }

    private fun catchUp(timeTillGrow: Long) {
        if (timeTillGrow < System.currentTimeMillis()) {
            val seconds = (System.currentTimeMillis() - timeTillGrow) / 1000
            if (seconds > Int.MAX_VALUE) {
                execute(1024)
            } else {
                val cyclesToGrow = secondsToTicks(seconds.toInt()) / cycleTime
                log(this.javaClass, Log.DEBUG, "$player brewing is going through $cyclesToGrow cycles")
                execute(cyclesToGrow)
            }
        }

    }

    override fun run(entity: Entity): Boolean {
        player = (entity as? Player)!!
        return execute(0)
    }

    private fun execute(count: Int): Boolean {
        log(this.javaClass, Log.DEBUG, "${fermentingVatMap.size} entries to go through for ${player.name}")

        for (vat in fermentingVatMap) {
            if (vat.value.canBrew()) for (i in 0..count) {
                val resp = vat.value.brew()
                if (!resp) {
                    break
                }
            }
            else {
                fermentingVatMap.entries.removeIf { it.value.isVatEmpty() && it.value.isBarrelEmpty() }
            }
        }
        return fermentingVatMap.size > 0
    }

    fun getVats(): MutableCollection<FermentingVat> {
        return fermentingVatMap.values
    }
}