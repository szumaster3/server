package core.game.world.update.flag.context;

import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.chunk.GraphicUpdateFlag;

/**
 * The type Graphics.
 */
public class Graphics {

    private final int id;

    private final int height;

    private final int delay;

    /**
     * Instantiates a new Graphics.
     *
     * @param id the id
     */
    public Graphics(int id) {
        this(id, 0, 0);
    }

    /**
     * Instantiates a new Graphics.
     *
     * @param id     the id
     * @param height the height
     */
    public Graphics(int id, int height) {
        this(id, height, 0);
    }

    /**
     * Instantiates a new Graphics.
     *
     * @param id     the id
     * @param height the height
     * @param delay  the delay
     */
    public Graphics(int id, int height, int delay) {
        this.id = id;
        this.height = height;
        this.delay = delay;
    }

    /**
     * Create graphics.
     *
     * @param id the id
     * @return the graphics
     */
    public static Graphics create(int id) {
        return new Graphics(id, 0, 0);
    }

    /**
     * Create graphics.
     *
     * @param id     the id
     * @param height the height
     * @return the graphics
     */
    public static Graphics create(int id, int height) {
        return new Graphics(id, height, 0);
    }

    /**
     * Send.
     *
     * @param graphics the graphics
     * @param l        the l
     */
    public static void send(Graphics graphics, Location l) {
        RegionManager.getRegionChunk(l).flag(new GraphicUpdateFlag(graphics, l));
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets delay.
     *
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "Graphics [id=" + id + ", height=" + height + ", delay=" + delay + "]";
    }
}