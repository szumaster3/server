package core.game.system.timer

import core.api.getWorldTicks
import core.game.node.entity.Entity
import org.json.simple.JSONObject
import kotlin.reflect.full.createInstance

abstract class PersistTimer(
    runInterval: Int,
    identifier: String,
    isSoft: Boolean = false,
    isAuto: Boolean = false,
    flags: Array<TimerFlag> = arrayOf(),
) : RSTimer(runInterval, identifier, isSoft, isAuto, flags) {
    open fun save(
        root: JSONObject,
        entity: Entity,
    ) {
        root["ticksLeft"] = (nextExecution - getWorldTicks()).toString()
    }

    open fun parse(
        root: JSONObject,
        entity: Entity,
    ) {
        runInterval = root["ticksLeft"].toString().toInt()
    }

    override fun retrieveInstance(): RSTimer {
        return this::class.createInstance()
    }
}
