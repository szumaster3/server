package core.game.system.timer

import core.api.getWorldTicks
import core.game.node.entity.Entity
import org.json.simple.JSONObject
import kotlin.reflect.full.createInstance

/**
 * Abstract timer with support for persistence (saving and loading state).
 */
abstract class PersistTimer(
    runInterval: Int,
    identifier: String,
    isSoft: Boolean = false,
    isAuto: Boolean = false,
    flags: Array<TimerFlag> = arrayOf(),
) : RSTimer(runInterval, identifier, isSoft, isAuto, flags) {

    /**
     * Saves timer state into the given JSON object.
     *
     * @param root JSON object to save data into.
     * @param entity The entity this timer is associated with.
     */
    open fun save(
        root: JSONObject,
        entity: Entity,
    ) {
        root["ticksLeft"] = (nextExecution - getWorldTicks()).toString()
    }

    /**
     * Parses timer state from the given JSON object.
     *
     * @param root JSON object containing saved timer data.
     * @param entity The entity this timer is associated with.
     */
    open fun parse(
        root: JSONObject,
        entity: Entity,
    ) {
        runInterval = root["ticksLeft"].toString().toInt()
    }

    /**
     * Gets a new instance of this timer type.
     *
     * @return A fresh instance of the timer.
     */
    override fun retrieveInstance(): RSTimer = this::class.createInstance()
}