package core.game.node.entity.lock

/**
 * Represents a time-based lock used to restrict actions for a specified duration (in game ticks).
 *
 * @property message (Optional) message shown when the lock is active.
 */
class Lock(var message: String? = null) {

    private var expiration: Int = 0
    private var elapse: LockElapse? = null

    /**
     * Locks this instance for the specified number of ticks.
     *
     * @param ticks Number of game ticks to lock.
     */
    fun lock(ticks: Int) {
        val currentTicks = core.game.world.GameWorld.ticks
        if (ticks > expiration - currentTicks) {
            expiration = currentTicks + ticks
        }
    }

    /**
     * Unlocks this instance immediately.
     */
    fun unlock() {
        expiration = 0
    }

    /**
     * Returns whether this instance is currently locked.
     *
     * @return `true` if locked, otherwise `false`.
     */
    fun isLocked(): Boolean = expiration > core.game.world.GameWorld.ticks

    /**
     * Sets the elapse event for this lock.
     *
     * @param elapse The elapse callback.
     * @return This instance for chaining.
     */
    fun setElapse(elapse: LockElapse?): Lock {
        this.elapse = elapse
        return this
    }

    /**
     * Gets the elapse event associated with this lock.
     *
     * @return The [LockElapse] instance or `null`.
     */
    fun getElapseEvent(): LockElapse? = elapse
}
