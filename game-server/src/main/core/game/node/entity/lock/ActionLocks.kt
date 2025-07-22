package core.game.node.entity.lock

import core.game.node.Node
import core.game.world.GameWorld

/**
 * Manages various action-based locks for a game node.
 */
class ActionLocks {

    private val movementLock = Lock()
    private val teleportLock = Lock()
    private val componentLock = Lock()
    private val interactionLock = Lock()
    var equipmentLock: Lock? = null
    private val customLocks: MutableMap<String, Lock> = HashMap()

    /**
     * Locks movement and interactions indefinitely.
     */
    fun lock() {
        lock(Int.MAX_VALUE - GameWorld.ticks)
    }

    /**
     * Locks movement and interactions for a given number of ticks.
     *
     * @param ticks Number of ticks to lock for.
     */
    fun lock(ticks: Int) {
        lockMovement(ticks)
        lockInteractions(ticks)
    }

    /**
     * Unlocks movement and interactions.
     */
    fun unlock() {
        unlockMovement()
        unlockInteraction()
    }

    /**
     * Locks movement for the given number of ticks.
     *
     * @param ticks Number of ticks to lock movement for.
     */
    fun lockMovement(ticks: Int) {
        movementLock.lock(ticks)
    }

    /**
     * Unlocks movement.
     */
    fun unlockMovement() {
        movementLock.unlock()
    }

    /**
     * @return `true` if movement is currently locked.
     */
    fun isMovementLocked(): Boolean = movementLock.isLocked()

    /**
     * Locks teleportation for the given number of ticks.
     *
     * @param ticks Number of ticks to lock teleportation for.
     */
    fun lockTeleport(ticks: Int) {
        teleportLock.lock(ticks)
    }

    /**
     * Unlocks teleportation.
     */
    fun unlockTeleport() {
        teleportLock.unlock()
    }

    /**
     * @return `true` if teleportation is currently locked.
     */
    fun isTeleportLocked(): Boolean = teleportLock.isLocked()

    /**
     * Locks component interactions for the given number of ticks.
     *
     * @param ticks Number of ticks to lock component interaction for.
     */
    fun lockComponent(ticks: Int) {
        componentLock.lock(ticks)
    }

    /**
     * Unlocks component interaction.
     */
    fun unlockComponent() {
        componentLock.unlock()
    }

    /**
     * @return `true` if components are currently locked.
     */
    fun isComponentLocked(): Boolean = componentLock.isLocked()

    /**
     * Locks general interactions for the given number of ticks.
     *
     * @param ticks Number of ticks to lock interaction for.
     */
    fun lockInteractions(ticks: Int) {
        interactionLock.lock(ticks)
    }

    /**
     * Unlocks general interactions.
     */
    fun unlockInteraction() {
        interactionLock.unlock()
    }

    /**
     * @return `true` if general interactions are currently locked.
     */
    fun isInteractionLocked(): Boolean = interactionLock.isLocked()

    /**
     * Locks a custom key-based lock for a given number of ticks.
     *
     * @param key Identifier for the custom lock.
     * @param ticks Number of ticks to lock for.
     * @return The [Lock] instance.
     */
    fun lock(key: String, ticks: Int): Lock = lock(key, ticks, null)

    /**
     * Locks a custom key-based lock for a given number of ticks and sets an optional elapse callback.
     *
     * @param key Identifier for the custom lock.
     * @param ticks Number of ticks to lock for.
     * @param elapse Optional callback when the lock elapses.
     * @return The [Lock] instance.
     */
    fun lock(key: String, ticks: Int, elapse: LockElapse?): Lock {
        val lock = customLocks.getOrPut(key) { Lock() }
        lock.setElapse(elapse).lock(ticks)
        return lock
    }

    /**
     * Unlocks a custom lock and invokes its elapse event if set.
     *
     * @param key Identifier for the custom lock.
     * @param node The node associated with the lock.
     */
    fun unlock(key: String, node: Node) {
        unlock(key, true, node)
    }

    /**
     * Unlocks a custom lock, optionally removing it from cache and invoking its elapse event if set.
     *
     * @param key Identifier for the custom lock.
     * @param cacheRemove Whether to remove the lock from the map after unlocking.
     * @param node The node associated with the lock.
     */
    fun unlock(key: String, cacheRemove: Boolean, node: Node) {
        val lock = customLocks[key] ?: return
        lock.unlock()
        lock.getElapseEvent()?.elapse(node, lock)
        if (cacheRemove) {
            customLocks.remove(key)
        }
    }

    /**
     * Checks if a custom lock is currently active.
     *
     * @param key Identifier for the custom lock.
     * @return `true` if the custom lock is active, otherwise `false`.
     */
    fun isLocked(key: String): Boolean {
        return customLocks[key]?.isLocked() ?: false
    }

}
