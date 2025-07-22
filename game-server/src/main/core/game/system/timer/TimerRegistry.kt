package core.game.system.timer

import core.api.hasTimerActive
import core.api.log
import core.api.registerTimer
import core.game.node.entity.Entity
import core.tools.Log

/**
 * Singleton registry for managing timers by their identifiers.
 */
object TimerRegistry {
    /**
     * Maps timer identifier strings (lowercase) to timer prototypes.
     */
    val timerMap = HashMap<String, RSTimer>()

    /**
     * List of timers marked as automatic to be added to entities.
     */
    val autoTimers = ArrayList<RSTimer>()

    /**
     * Gets a timer prototype in the registry.
     *
     * @param timer Timer instance to register.
     * Logs a warning if the identifier is already in use and skips registration.
     */
    @JvmStatic
    fun registerTimer(timer: RSTimer) {
        log(this::class.java, Log.WARN, "Registering timer ${timer::class.java.simpleName}")
        if (timerMap.containsKey(timer.identifier.lowercase())) {
            log(
                this::class.java,
                Log.ERR,
                "Timer identifier ${timer.identifier} already in use by ${timerMap[timer.identifier.lowercase()]!!::class.java.simpleName}! Not loading ${timer::class.java.simpleName}!",
            )
            return
        }
        timerMap[timer.identifier.lowercase()] = timer
        if (timer.isAuto) autoTimers.add(timer)
    }

    /**
     * Gets a timer instance by identifier, optionally passing constructor arguments.
     *
     * @param identifier Timer identifier string.
     * @param args Optional arguments for timer instance retrieval.
     * @return A new timer instance or null if not found.
     */
    fun getTimerInstance(
        identifier: String,
        vararg args: Any,
    ): RSTimer? {
        val t = timerMap[identifier.lowercase()]
        return if (args.isNotEmpty()) {
            t?.getTimer(*args)
        } else {
            t?.retrieveInstance()
        }
    }

    /**
     * Adds all automatic timers to the specified entity if not already active.
     *
     * @param entity The entity to add timers to.
     */
    @JvmStatic
    fun addAutoTimers(entity: Entity) {
        for (timer in autoTimers) {
            if (!hasTimerActive(entity, timer.identifier)) {
                registerTimer(entity, timer.retrieveInstance())
            }
        }
    }

    /**
     * Gets a timer instance by its type, optionally with constructor arguments.
     *
     * @param T Timer subclass type to retrieve.
     * @param args Optional constructor arguments.
     * @return Timer instance or null if not found.
     */
    inline fun <reified T> getTimerInstance(vararg args: Any): T? {
        for ((_, inst) in timerMap) {
            if (inst is T) {
                return if (args.isNotEmpty()) {
                    inst.getTimer(*args) as? T
                } else {
                    inst.retrieveInstance() as? T
                }
            }
        }
        return null
    }
}