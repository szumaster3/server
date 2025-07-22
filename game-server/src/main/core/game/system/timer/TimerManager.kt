package core.game.system.timer

import core.api.getWorldTicks
import core.api.log
import core.api.registerTimer
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.tools.Log
import org.json.simple.JSONObject

/**
 * Manages timers associated with an [Entity], handling their lifecycle and execution.
 *
 * @property entity The entity this manager is attached to.
 */
class TimerManager(
    val entity: Entity,
) {
    /**
     * Currently active timers.
     */
    val activeTimers = ArrayList<RSTimer>()

    /**
     * Timers registered during the current tick, to be activated next cycle.
     */
    val newTimers = ArrayList<RSTimer>()

    /**
     * Timers scheduled for removal.
     */
    val toRemoveTimers = ArrayList<RSTimer>()

    /**
     * Registers a new timer for this entity.
     */
    fun registerTimer(timer: RSTimer) {
        timer.onRegister(entity)
        newTimers.add(timer)
    }

    /**
     * Processes all timers: runs active timers and handles adding/removing timers.
     */
    fun processTimers() {
        activeTimers.removeAll(toRemoveTimers.toSet())
        newTimers.removeAll(toRemoveTimers.toSet())
        toRemoveTimers.clear()

        val canRunNormalTimers =
            (entity !is Player) || !(entity.asPlayer().hasModalOpen() || entity.scripts.delay > getWorldTicks())

        for (timer in activeTimers) {
            if (timer.nextExecution > getWorldTicks()) continue
            if (!canRunNormalTimers && !timer.isSoft) continue

            try {
                if (timer.run(entity)) {
                    timer.nextExecution = getWorldTicks() + timer.runInterval
                } else {
                    removeTimer(timer)
                }
            } catch (e: Exception) {
                log(this::class.java, Log.ERR, "Removing timer ${timer::class.java.simpleName} from ${entity.name} due to exception:")
                e.printStackTrace()
                removeTimer(timer)
            }
        }

        for (timer in newTimers) {
            activeTimers.add(timer)
            timer.nextExecution = timer.getInitialRunDelay() + getWorldTicks()
        }

        newTimers.clear()
    }

    /**
     * Clears all timers managed by this manager.
     */
    fun clearTimers() {
        activeTimers.clear()
        newTimers.clear()
        toRemoveTimers.clear()
    }

    /**
     * Removes timers flagged to be cleared on entity death.
     */
    fun onEntityDeath() {
        for (timer in activeTimers) {
            if (timer.flags.contains(TimerFlag.ClearOnDeath)) {
                removeTimer(timer)
            }
        }
    }

    /**
     * Saves the state of persistent timers to the given JSON object.
     *
     * @param root JSON object to store timers in.
     */
    fun saveTimers(root: JSONObject) {
        for (timer in activeTimers) {
            if (timer !is PersistTimer) continue
            val obj = JSONObject()
            timer.save(obj, entity)
            root[timer.identifier] = obj
        }
        for (timer in newTimers) {
            if (timer !is PersistTimer) continue
            val obj = JSONObject()
            timer.save(obj, entity)
            root[timer.identifier] = obj
        }
    }

    /**
     * Parses and registers persistent timers from a JSON object.
     *
     * @param root JSON object containing timer data.
     */
    fun parseTimers(root: JSONObject) {
        for ((identifier, dataObj) in root) {
            val data = dataObj as JSONObject
            val timer = TimerRegistry.getTimerInstance(identifier.toString()) as? PersistTimer
            if (timer == null) {
                log(this::class.java, Log.ERR, "No persistent timer found for identifier $identifier.")
                continue
            }
            timer.parse(data, entity)
            registerTimer(entity, timer)
        }
    }

    /**
     * Removes timers of type [T] from active and new timers.
     */
    inline fun <reified T : RSTimer> removeTimer() {
        for (timer in activeTimers) if (timer is T) removeTimer(timer)
        for (timer in newTimers) if (timer is T) removeTimer(timer)
    }

    /**
     * Gets the first active or new timer of type [T], or null if none found or scheduled for removal.
     */
    inline fun <reified T : RSTimer> getTimer(): T? {
        var t: T? = null
        for (timer in activeTimers) if (timer is T) t = timer
        for (timer in newTimers) if (timer is T) t = timer
        if (t == null || toRemoveTimers.contains(t)) return null
        return t
    }

    /**
     * Gets a timer by its identifier, or null if none found or scheduled for removal.
     */
    fun getTimer(identifier: String): RSTimer? {
        var t: RSTimer? = null
        for (timer in activeTimers) if (timer.identifier == identifier) t = timer
        for (timer in newTimers) if (timer.identifier == identifier) t = timer
        if (t == null || toRemoveTimers.contains(t)) return null
        return t
    }

    /**
     * Removes a timer by its identifier from active and new timers.
     */
    fun removeTimer(identifier: String) {
        for (timer in activeTimers) if (timer.identifier == identifier) removeTimer(timer)
        for (timer in newTimers) if (timer.identifier == identifier) removeTimer(timer)
    }

    /**
     * Removes a timer, scheduling it for removal and invoking its onRemoval handler.
     */
    fun removeTimer(timer: RSTimer) {
        timer.nextExecution = Int.MAX_VALUE
        toRemoveTimers.add(timer)
        try {
            timer.onRemoval(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}