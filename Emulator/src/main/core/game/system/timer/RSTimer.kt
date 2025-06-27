package core.game.system.timer

import core.game.node.entity.Entity
import kotlin.reflect.full.createInstance

/**
 * Timer feature.
 *
 * @param runInterval Time in ticks between each execution of the timer.
 * @param identifier A unique identifier for this timer (used in persistence or debugging).
 * @param isSoft If false (default and preferred), the timer will pause when the entity is in a "blocked" state (e.g. in cutscene or dialogue).
 * @param isAuto If true, the timer is automatically created and started for every entity, regardless of persistence state. Use sparingly.
 * @param flags Optional flags used to categorize or handle special logic for this timer.
 */
abstract class RSTimer(
    var runInterval: Int,
    val identifier: String = "generictimer",
    val isSoft: Boolean = false,
    val isAuto: Boolean = false,
    val flags: Array<TimerFlag> = arrayOf()
) {

    /**
     * Called every time the timer's interval elapses.
     * If `isSoft` is false, execution is delayed if the entity is blocked (e.g., has an interface open).
     *
     * @param entity The entity to which the timer is attached.
     * @return `true` to schedule the timer again, or `false` to stop it and remove it from the entity.
     */
    abstract fun run(entity: Entity): Boolean

    /**
     * Determines the delay (in ticks) before the first execution of the timer.
     * By default, returns [runInterval], but can be overridden for precise timing (e.g., syncing with a realtime clock).
     */
    open fun getInitialRunDelay(): Int {
        return runInterval
    }

    /**
     * Called before the timer is registered on the entity.
     * For persistent timers, this is invoked after data has been parsed, but before the timer is added to the active list.
     */
    open fun beforeRegister(entity: Entity) {}

    /**
     * Called after the timer is registered on the entity.
     * For persistent timers, this is invoked after data parsing and after the timer is added to the active list.
     */
    open fun onRegister(entity: Entity) {}

    /**
     * Called when the timer is removed from the entity, either manually or after returning false from [run].
     */
    open fun onRemoval(entity: Entity) {}

    /**
     * Internal: Last tick when the timer executed.
     */
    var lastExecution: Int = 0

    /**
     * Internal: Next scheduled tick for timer execution.
     */
    var nextExecution: Int = 0

    /**
     * Creates a new instance of this timer using reflection.
     * Used when no arguments are provided (default behavior).
     */
    open fun retrieveInstance(): RSTimer {
        return this::class.createInstance()
    }

    /**
     * Creates a new instance of this timer, optionally using arguments.
     * Called when `getTimer(...)` is invoked from higher-level systems with parameters.
     *
     * Override this if your timer requires arguments to initialize.
     */
    open fun getTimer(vararg args: Any): RSTimer {
        return retrieveInstance()
    }
}

