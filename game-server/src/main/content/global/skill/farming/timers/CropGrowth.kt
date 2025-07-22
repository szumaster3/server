package content.global.skill.farming.timers

import content.global.skill.farming.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.tools.secondsToTicks
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class CropGrowth : PersistTimer(500, "farming:crops", isSoft = true) {
    private val patchMap = HashMap<FarmingPatch, Patch>()
    lateinit var player: Player

    override fun onRegister(entity: Entity) {
        player = (entity as? Player)!!
        runOfflineCatchupLogic()
    }

    override fun getInitialRunDelay(): Int {
        val now = LocalTime.now()
        val minsUntil5MinSync = 5 - (now.minute % 5)
        val ticks = secondsToTicks(minsUntil5MinSync * 60)
        player.debug("[CropGrowth] Scheduled first growth cycle for $ticks ticks from now.")
        return ticks
    }

    override fun run(entity: Entity): Boolean {
        var removeList = ArrayList<FarmingPatch>()
        for ((fp, patch) in patchMap) {
            if (patch.getCurrentState() in 1..3 && patch.nextGrowth == 0L) {
                patch.nextGrowth = System.currentTimeMillis() + 60000
                continue
            }

            if (patch.nextGrowth < (System.currentTimeMillis() + 240_000L) && !patch.isDead) {
                patch.nextGrowth =
                    System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(patch.getStageGrowthMinutes().toLong())
                patch.update()
            }

            if (patch.getCurrentState() == 0) {
                removeList.add(fp)
            }
        }
        removeList.forEach { patchMap.remove(it) }
        removeList.clear()
        return patchMap.isNotEmpty()
    }

    private fun runOfflineCatchupLogic() {
        for ((_, patch) in patchMap) {
            val type = patch.patch.type
            val shouldPlayCatchup =
                !patch.isGrown() || (type == PatchType.BUSH_PATCH && patch.getFruitOrBerryCount() < 4) || (type == PatchType.FRUIT_TREE_PATCH && patch.getFruitOrBerryCount() < 6)
            if (shouldPlayCatchup && !patch.isDead) {
                var stagesToSimulate = if (!patch.isGrown()) {
                    if (patch.isWeedy() || patch.isEmptyAndWeeded()) {
                        patch.currentGrowthStage % 4
                    } else {
                        patch.plantable!!.stages - patch.currentGrowthStage
                    }
                } else {
                    0
                }

                if (patch.plantable != null) {
                    if (type == PatchType.BUSH_PATCH) {
                        stagesToSimulate += 4.coerceAtMost(4 - patch.getFruitOrBerryCount())
                    }
                    if (type == PatchType.FRUIT_TREE_PATCH) {
                        stagesToSimulate += 6.coerceAtMost(6 - patch.getFruitOrBerryCount())
                    }
                }

                val nowTime = System.currentTimeMillis()
                var simulatedTime = patch.nextGrowth

                while (simulatedTime < nowTime && stagesToSimulate-- > 0 && !patch.isDead) {
                    val timeToIncrement = TimeUnit.MINUTES.toMillis(patch.getStageGrowthMinutes().toLong())
                    patch.update()
                    simulatedTime += timeToIncrement
                }
            }
        }
    }

    fun getPatch(
        patch: FarmingPatch,
        addPatch: Boolean,
    ): Patch = patchMap[patch] ?: (Patch(player, patch).also { if (addPatch) patchMap[patch] = it })

    fun getPatches(): MutableCollection<Patch> = patchMap.values

    override fun save(
        root: JSONObject,
        entity: Entity,
    ) {
        val patches = JSONArray()
        for ((key, patch) in patchMap) {
            val p = JSONObject()
            p["patch-ordinal"] = key.ordinal
            p["patch-plantable-ordinal"] = patch.plantable?.ordinal ?: -1
            p["patch-watered"] = patch.isWatered
            p["patch-diseased"] = patch.isDiseased
            p["patch-dead"] = patch.isDead
            p["patch-stage"] = patch.currentGrowthStage
            p["patch-state"] = patch.getCurrentState()
            p["patch-nextGrowth"] = patch.nextGrowth
            p["patch-harvestAmt"] = patch.harvestAmt
            p["patch-checkHealth"] = patch.isCheckHealth
            p["patch-compost"] = patch.compost.ordinal
            p["patch-paidprot"] = patch.protectionPaid
            p["patch-croplives"] = patch.cropLives
            patches.add(p)
        }
        root["patches"] = patches
    }

    override fun parse(
        root: JSONObject,
        entity: Entity,
    ) {
        val data = root["patches"] as JSONArray
        for (d in data) {
            val p = d as JSONObject
            val patchOrdinal = p["patch-ordinal"].toString().toInt()
            val patchPlantableOrdinal = p["patch-plantable-ordinal"].toString().toInt()
            val patchWatered = p["patch-watered"] as Boolean
            val patchDiseased = p["patch-diseased"] as Boolean
            val patchDead = p["patch-dead"] as Boolean
            val patchStage = p["patch-stage"].toString().toInt()
            val nextGrowth = p["patch-nextGrowth"].toString().toLong()
            val harvestAmt = (p["patch-harvestAmt"] ?: 0).toString().toInt()
            val checkHealth = p["patch-checkHealth"] as Boolean
            val savedState = p["patch-state"].toString().toInt()
            val compostOrdinal = p["patch-compost"].toString().toInt()
            val protectionPaid = p["patch-paidprot"] as Boolean
            val cropLives = if (p["patch-croplives"] != null) p["patch-croplives"].toString().toInt() else 3
            val fPatch = FarmingPatch.values()[patchOrdinal]
            val plantable = if (patchPlantableOrdinal != -1) Plantable.values()[patchPlantableOrdinal] else null
            val patch = Patch(
                (entity as? Player)!!,
                fPatch,
                plantable,
                patchStage,
                patchDiseased,
                patchDead,
                patchWatered,
                nextGrowth,
                harvestAmt,
                checkHealth,
            )

            patch.cropLives = cropLives
            patch.compost = CompostType.values()[compostOrdinal]
            patch.protectionPaid = protectionPaid
            patch.setCurrentState(savedState)

            if ((savedState - (patch?.plantable?.value ?: 0)) > patch.currentGrowthStage) {
                patch.setCurrentState(savedState)
            } else {
                patch.setCurrentState((patch.plantable?.value ?: 0) + patch.currentGrowthStage)
            }

            if (patchMap[fPatch] == null) {
                patchMap[fPatch] = patch
            }
        }
    }
}
