package core.game.system.timer

import core.game.node.entity.Entity
import kotlin.reflect.full.createInstance

abstract class RSTimer(
    var runInterval: Int,
    val identifier: String = "generictimer",
    val isSoft: Boolean = false,
    val isAuto: Boolean = false,
    val flags: Array<TimerFlag> = arrayOf(),
) {
    abstract fun run(entity: Entity): Boolean

    open fun getInitialRunDelay(): Int {
        return runInterval
    }

    open fun onRegister(entity: Entity) {}

    open fun onRemoval(entity: Entity) {}

    var lastExecution: Int = 0
    var nextExecution: Int = 0

    open fun retrieveInstance(): RSTimer {
        return this::class.createInstance()
    }

    open fun getTimer(vararg args: Any): RSTimer {
        return retrieveInstance()
    }
}
