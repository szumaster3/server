package core.game.node.entity.player.link;

/**
 * The enum Hint icon location.
 */
public enum HintIconLocation {
    /**
     * Entity hint icon location.
     */
    ENTITY(1),
    /**
     * Center hint icon location.
     */
    CENTER(2),
    /**
     * West hint icon location.
     */
    WEST(3),
    /**
     * East hint icon location.
     */
    EAST(4),
    /**
     * South hint icon location.
     */
    SOUTH(5),
    /**
     * North hint icon location.
     */
    NORTH(6);

    private final int location;

    HintIconLocation(int location) {
        this.location = location;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public int getLocation() {
        return location;
    }
}