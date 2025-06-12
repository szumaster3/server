package content.region.morytania.plugin.tarnslair.plugin.traps;

import core.game.world.map.Location;

/**
 * The type Passage scenery.
 */
public class PassageScenery {

    private final int objectId;
    private final Location location;

    /**
     * Instantiates a new Passage scenery.
     *
     * @param objectId the object id
     * @param location the location
     */
    public PassageScenery(int objectId, Location location) {
        this.objectId = objectId;
        this.location = location;
    }

    /**
     * Gets object id.
     *
     * @return the object id
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }
}