package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.Context;

/**
 * The type Location context.
 */
public final class LocationContext implements Context {

    private final Player player;

    private final Location location;

    private final boolean teleport;

    /**
     * Instantiates a new Location context.
     *
     * @param player   the player
     * @param location the location
     * @param teleport the teleport
     */
    public LocationContext(Player player, Location location, boolean teleport) {
        this.player = player;
        this.location = location;
        this.teleport = teleport;
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
     * Is teleport boolean.
     *
     * @return the boolean
     */
    public boolean isTeleport() {
        return teleport;
    }

}