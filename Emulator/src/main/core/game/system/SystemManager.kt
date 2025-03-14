package core.game.system

import core.game.system.security.EncryptionManager
import core.game.world.GameWorld.majorUpdateWorker

object SystemManager {
    private var state = SystemState.TERMINATED

    val updater = SystemUpdate()

    val terminator = SystemTermination()

    val systemConfig = SystemConfig()

    val encryption = EncryptionManager()

    @JvmStatic
    fun flag(state: SystemState) {
        if (SystemManager.state == state) {
            return
        }
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

    val isActive: Boolean
        get() = state != SystemState.TERMINATED

    val isUpdating: Boolean
        get() = state == SystemState.UPDATING

    val isPrivate: Boolean
        get() = state == SystemState.PRIVATE

    @JvmStatic
    val isTerminated: Boolean
        get() = state == SystemState.TERMINATED

    fun state(): SystemState {
        return state
    }
}
