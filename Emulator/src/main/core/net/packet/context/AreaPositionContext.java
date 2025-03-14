package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.Context;

/**
 * The type Area position context.
 */
public final class AreaPositionContext implements Context {

    private final Player player;

    private final Location location;

    private final int offsetX;

    private final int offsetY;

    /**
     * Instantiates a new Area position context.
     *
     * @param player   the player
     * @param location the location
     * @param offsetX  the offset x
     * @param offsetY  the offset y
     */
    public AreaPositionContext(Player player, Location location, int offsetX, int offsetY) {
        this.player = player;
        this.location = location;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets offset x.
     *
     * @return the offset x
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Gets offset y.
     *
     * @return the offset y
     */
    public int getOffsetY() {
        return offsetY;
    }

}