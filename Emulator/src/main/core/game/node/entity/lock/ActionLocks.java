package core.game.node.entity.lock;

import core.game.node.Node;
import core.game.world.GameWorld;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Action locks.
 */
public final class ActionLocks {

    private Lock movementLock = new Lock();

    private Lock teleportLock = new Lock();

    private Lock componentLock = new Lock();

    private Lock interactionLock = new Lock();

    private Lock equipmentLock = null;

    private Map<String, Lock> customLocks;

    /**
     * Instantiates a new Action locks.
     */
    public ActionLocks() {
        this.customLocks = new HashMap<>();
    }

    /**
     * Lock.
     */
    public void lock() {
        lock(Integer.MAX_VALUE - GameWorld.getTicks());
    }

    /**
     * Lock.
     *
     * @param ticks the ticks
     */
    public void lock(int ticks) {
        lockMovement(ticks);
        lockInteractions(ticks);
    }

    /**
     * Unlock.
     */
    public void unlock() {
        unlockMovement();
        unlockInteraction();
    }

    /**
     * Lock movement.
     *
     * @param ticks the ticks
     */
    public void lockMovement(int ticks) {
        movementLock.lock(ticks);
    }

    /**
     * Unlock movement.
     */
    public void unlockMovement() {
        movementLock.unlock();
    }

    /**
     * Is movement locked boolean.
     *
     * @return the boolean
     */
    public boolean isMovementLocked() {
        return movementLock.isLocked();
    }

    /**
     * Lock teleport.
     *
     * @param ticks the ticks
     */
    public void lockTeleport(int ticks) {
        teleportLock.lock(ticks);
    }

    /**
     * Unlock teleport.
     */
    public void unlockTeleport() {
        teleportLock.unlock();
    }

    /**
     * Is teleport locked boolean.
     *
     * @return the boolean
     */
    public boolean isTeleportLocked() {
        return teleportLock.isLocked();
    }

    /**
     * Lock component.
     *
     * @param ticks the ticks
     */
    public void lockComponent(int ticks) {
        componentLock.lock(ticks);
    }

    /**
     * Unlock component.
     */
    public void unlockComponent() {
        componentLock.unlock();
    }

    /**
     * Is component locked boolean.
     *
     * @return the boolean
     */
    public boolean isComponentLocked() {
        return componentLock.isLocked();
    }

    /**
     * Lock interactions.
     *
     * @param ticks the ticks
     */
    public void lockInteractions(int ticks) {
        interactionLock.lock(ticks);
    }

    /**
     * Unlock interaction.
     */
    public void unlockInteraction() {
        interactionLock.unlock();
    }

    /**
     * Is interaction locked boolean.
     *
     * @return the boolean
     */
    public boolean isInteractionLocked() {
        return interactionLock.isLocked();
    }

    /**
     * Lock lock.
     *
     * @param key   the key
     * @param ticks the ticks
     * @return the lock
     */
    public Lock lock(String key, int ticks) {
        return lock(key, ticks, null);
    }

    /**
     * Lock lock.
     *
     * @param key    the key
     * @param ticks  the ticks
     * @param elapse the elapse
     * @return the lock
     */
    public Lock lock(String key, int ticks, LockElapse elapse) {
        Lock lock = customLocks.get(key);
        if (lock == null) {
            customLocks.put(key, lock = new Lock());
        }
        lock.setElapse(elapse).lock(ticks);
        return lock;
    }

    /**
     * Unlock.
     *
     * @param key  the key
     * @param node the node
     */
    public void unlock(String key, Node node) {
        unlock(key, true, node);
    }

    /**
     * Unlock.
     *
     * @param key         the key
     * @param cacheRemove the cache remove
     * @param node        the node
     */
    public void unlock(String key, boolean cacheRemove, Node node) {
        Lock lock = customLocks.get(key);
        if (lock != null) {
            lock.unlock();
            if (lock.getElapseEvent() != null) {
                lock.getElapseEvent().elapse(node, lock);
            }
            if (cacheRemove) {
                customLocks.remove(key);
            }
        }
    }

    /**
     * Is locked boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean isLocked(String key) {
        Lock lock = customLocks.get(key);
        return lock != null && lock.isLocked();
    }

    /**
     * Gets equipment lock.
     *
     * @return the equipment lock
     */
    public Lock getEquipmentLock() {
        return equipmentLock;
    }

    /**
     * Sets equipment lock.
     *
     * @param equipmentLock the equipment lock
     */
    public void setEquipmentLock(Lock equipmentLock) {
        this.equipmentLock = equipmentLock;
    }

}