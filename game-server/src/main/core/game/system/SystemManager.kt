package core.game.system

import core.game.system.security.EncryptionManager
import core.game.world.GameWorld.majorUpdateWorker

/**
 * Manages the system state and coordinates updates, termination, and configurations.
 */
object SystemManager {
    private var state = SystemState.TERMINATED

    val updater = SystemUpdate()
    val terminator = SystemTermination()
    val systemConfig = SystemConfig()
    val encryption = EncryptionManager()

    /**
     * Sets the system state and triggers related actions.
     * @param state The new system state.
     */
    @JvmStatic
    fun flag(state: SystemState) {
        if (SystemManager.state == state) return
        SystemManager.state = state
        when (state) {
            SystemState.ACTIVE, SystemState.PRIVATE -> {
                majorUpdateWorker.started = false
                majorUpdateWorker.start()
            }
            SystemState.UPDATING -> updater.schedule()
            SystemState.TERMINATED -> terminator.terminate()
        }
    }

    /** Returns true if the system is active (not terminated). */
    val isActive: Boolean
        get() = state != SystemState.TERMINATED

    /** Returns true if the system is updating. */
    val isUpdating: Boolean
        get() = state == SystemState.UPDATING

    /** Returns true if the system is in private mode. */
    val isPrivate: Boolean
        get() = state == SystemState.PRIVATE

    /** Returns true if the system is terminated. */
    @JvmStatic
    val isTerminated: Boolean
        get() = state == SystemState.TERMINATED

    /** Returns the current system state. */
    fun state(): SystemState = state
}
