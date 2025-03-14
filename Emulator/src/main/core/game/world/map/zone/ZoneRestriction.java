package core.game.world.map.zone;

/**
 * The enum Zone restriction.
 */
public enum ZoneRestriction {

    /**
     * Followers zone restriction.
     */
    FOLLOWERS,

    /**
     * Random events zone restriction.
     */
    RANDOM_EVENTS,

    /**
     * Fires zone restriction.
     */
    FIRES,

    /**
     * Members zone restriction.
     */
    MEMBERS,

    /**
     * Cannon zone restriction.
     */
    CANNON,

    /**
     * Graves zone restriction.
     */
    GRAVES,

    /**
     * Teleport zone restriction.
     */
    TELEPORT;

    /**
     * Gets flag.
     *
     * @return the flag
     */
    public int getFlag() {
        return 1 << ordinal();
    }
}