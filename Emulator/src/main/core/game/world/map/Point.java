package core.game.world.map;

/**
 * The type Point.
 */
public final class Point {

	private final int x;

	private final int y;

	private final int diffX;

	private final int diffY;

	private final Direction direction;

	private boolean runDisabled;

    /**
     * Instantiates a new Point.
     *
     * @param x the x
     * @param y the y
     */
    public Point(int x, int y) {
		this(x, y, null, 0, 0);
	}

    /**
     * Instantiates a new Point.
     *
     * @param x         the x
     * @param y         the y
     * @param direction the direction
     */
    public Point(int x, int y, Direction direction) {
		this(x, y, direction, 0, 0);
	}

    /**
     * Instantiates a new Point.
     *
     * @param x         the x
     * @param y         the y
     * @param direction the direction
     * @param diffX     the diff x
     * @param diffY     the diff y
     */
    public Point(int x, int y, Direction direction, int diffX, int diffY) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.diffX = diffX;
		this.diffY = diffY;
	}

    /**
     * Instantiates a new Point.
     *
     * @param x           the x
     * @param y           the y
     * @param direction   the direction
     * @param diffX       the diff x
     * @param diffY       the diff y
     * @param runDisabled the run disabled
     */
    public Point(int x, int y, Direction direction, int diffX, int diffY, boolean runDisabled) {
		this(x, y, direction, diffX, diffY);
		this.runDisabled = runDisabled;
	}

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
		return x;
	}

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
		return y;
	}

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public Direction getDirection() {
		return direction;
	}

    /**
     * Gets diff x.
     *
     * @return the diff x
     */
    public int getDiffX() {
		return diffX;
	}

    /**
     * Gets diff y.
     *
     * @return the diff y
     */
    public int getDiffY() {
		return diffY;
	}

    /**
     * Is run disabled boolean.
     *
     * @return the boolean
     */
    public boolean isRunDisabled() {
		return runDisabled;
	}

    /**
     * Sets run disabled.
     *
     * @param runDisabled the run disabled
     */
    public void setRunDisabled(boolean runDisabled) {
		this.runDisabled = runDisabled;
	}
}