package core.game.world.map;

/**
 * The enum Map distance.
 */
public enum MapDistance {

    /**
     * Rendering map distance.
     */
    RENDERING(15),

    /**
     * Sound map distance.
     */
    SOUND(5), ;

	private final int distance;

	private MapDistance(int distance) {
		this.distance = distance;
	}

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public int getDistance() {
		return distance;
	}
}