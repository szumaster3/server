package core.game.world.map.zone;

/**
 * The enum Zone type.
 */
public enum ZoneType {

    /**
     * Default zone type.
     */
    DEFAULT(0),

    /**
     * Safe zone type.
     */
    SAFE(1),

    /**
     * P o h zone type.
     */
    P_O_H(2),

    /**
     * Castle wars zone type.
     */
    CASTLE_WARS(3),

    /**
     * Trouble brewing zone type.
     */
    TROUBLE_BREWING(4),

    /**
     * Barbarian assault zone type.
     */
    BARBARIAN_ASSAULT(5);

    private final int id;

    ZoneType(int id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }
}
