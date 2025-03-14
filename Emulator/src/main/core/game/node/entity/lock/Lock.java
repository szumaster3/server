package core.game.node.entity.lock;

import core.game.world.GameWorld;

/**
 * The type Lock.
 */
public class Lock {

    private int expiration;

    private LockElapse elapse;

    private String message;

    /**
     * Instantiates a new Lock.
     */
    public Lock() {
        this(null);
    }

    /**
     * Instantiates a new Lock.
     *
     * @param message the message
     */
    public Lock(String message) {
        this.message = message;
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
        if (ticks > expiration - GameWorld.getTicks()) {
            this.expiration = GameWorld.getTicks() + ticks;
        }
    }

    /**
     * Unlock.
     */
    public void unlock() {
        this.expiration = 0;
    }

    /**
     * Is locked boolean.
     *
     * @return the boolean
     */
    public boolean isLocked() {
        return expiration > GameWorld.getTicks();
    }

    /**
     * Sets elapse.
     *
     * @param elapse the elapse
     * @return the elapse
     */
    public Lock setElapse(LockElapse elapse) {
        this.elapse = elapse;
        return this;
    }

    /**
     * Gets elapse event.
     *
     * @return the elapse event
     */
    public LockElapse getElapseEvent() {
        return elapse;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}