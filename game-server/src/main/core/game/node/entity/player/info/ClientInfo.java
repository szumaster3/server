package core.game.node.entity.player.info;

/**
 * The type Client info.
 */
public final class ClientInfo {

    private int displayMode;

    private int windowMode;

    private int screenWidth;

    private int screenHeight;

    /**
     * Instantiates a new Client info.
     *
     * @param displayMode  the display mode
     * @param windowMode   the window mode
     * @param screenWidth  the screen width
     * @param screenHeight the screen height
     */
    public ClientInfo(int displayMode, int windowMode, int screenWidth, int screenHeight) {
        this.displayMode = displayMode;
        this.windowMode = windowMode;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Is high detail boolean.
     *
     * @return the boolean
     */
    public boolean isHighDetail() {
        return displayMode > 0; // ?
    }

    /**
     * Is resizable boolean.
     *
     * @return the boolean
     */
    public boolean isResizable() {
        return windowMode > 1;
    }

    /**
     * Gets display mode.
     *
     * @return the display mode
     */
    public int getDisplayMode() {
        return displayMode;
    }

    /**
     * Sets display mode.
     *
     * @param displayMode the display mode
     */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    /**
     * Gets window mode.
     *
     * @return the window mode
     */
    public int getWindowMode() {
        return windowMode;
    }

    /**
     * Sets window mode.
     *
     * @param windowMode the window mode
     */
    public void setWindowMode(int windowMode) {
        this.windowMode = windowMode;
    }

    /**
     * Gets screen width.
     *
     * @return the screen width
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Sets screen width.
     *
     * @param screenWidth the screen width
     */
    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * Gets screen height.
     *
     * @return the screen height
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Sets screen height.
     *
     * @param screenHeight the screen height
     */
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}