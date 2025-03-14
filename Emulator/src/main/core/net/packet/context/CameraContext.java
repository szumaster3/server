package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Camera context.
 */
public final class CameraContext implements Context {

    private final Player player;
    private final CameraType type;
    private final int x;
    private final int y;
    private final int height;
    private final int zoomSpeed;
    private final int speed;

    /**
     * Instantiates a new Camera context.
     *
     * @param player    the player
     * @param type      the type
     * @param x         the x
     * @param y         the y
     * @param height    the height
     * @param speed     the speed
     * @param zoomSpeed the zoom speed
     */
    public CameraContext(Player player, CameraType type, int x, int y, int height, int speed, int zoomSpeed) {
        this.player = player;
        this.type = type;
        this.x = x;
        this.y = y;
        this.height = height;
        this.speed = speed;
        this.zoomSpeed = zoomSpeed;
    }

    /**
     * Transform camera context.
     *
     * @param player the player
     * @param x      the x
     * @param y      the y
     * @return the camera context
     */
    public CameraContext transform(final Player player, final int x, final int y) {
        return new CameraContext(player, type, this.x + x, this.y + y, height, speed, zoomSpeed);
    }

    /**
     * Transform camera context.
     *
     * @param heightOffset the height offset
     * @return the camera context
     */
    public CameraContext transform(final int heightOffset) {
        return new CameraContext(player, type, x, y, height + heightOffset, speed, zoomSpeed);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public CameraType getType() {
        return type;
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
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets zoom speed.
     *
     * @return the zoom speed
     */
    public int getZoomSpeed() {
        return zoomSpeed;
    }

    /**
     * The enum Camera type.
     */
    public static enum CameraType {
        /**
         * Position camera type.
         */
        POSITION(154),
        /**
         * Rotation camera type.
         */
        ROTATION(125),
        /**
         * Set camera type.
         */
        SET(187),
        /**
         * Shake camera type.
         */
        SHAKE(27),
        /**
         * Reset camera type.
         */
        RESET(24);

        private final int opcode;

        private CameraType(int opcode) {
            this.opcode = opcode;
        }

        /**
         * Opcode int.
         *
         * @return the int
         */
        public int opcode() {
            return opcode;
        }
    }

}