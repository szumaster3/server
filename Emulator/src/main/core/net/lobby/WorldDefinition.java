package core.net.lobby;

import core.game.world.GameWorld;
import core.game.world.repository.Repository;

/**
 * The type World definition.
 */
public class WorldDefinition {

    private final String activity;

    private final int country;

    private final int flag;

    private final String ip;

    private final int location;

    private final String region;

    private final int worldId;

    private int players;

    /**
     * Instantiates a new World definition.
     *
     * @param worldId  the world id
     * @param location the location
     * @param flag     the flag
     * @param activity the activity
     * @param ip       the ip
     * @param region   the region
     * @param country  the country
     */
    public WorldDefinition(int worldId, int location, int flag, String activity, String ip, String region, int country) {
        this.worldId = worldId;
        this.location = location;
        this.flag = flag;
        this.activity = activity;
        this.ip = ip;
        this.region = region;
        this.country = country;
    }

    /**
     * Gets activity.
     *
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public int getCountry() {
        return country;
    }

    /**
     * Gets flag.
     *
     * @return the flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Gets ip.
     *
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public int getLocation() {
        return location;
    }

    /**
     * Gets region.
     *
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets world id.
     *
     * @return the world id
     */
    public int getWorldId() {
        return worldId;
    }

    /**
     * Gets player count.
     *
     * @return the player count
     */
    public int getPlayerCount() {
        if (worldId == GameWorld.getSettings().getWorldId()) {
            return Repository.getPlayers().size();
        }
        return players;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public int getPlayers() {
        return players;
    }

    /**
     * Sets players.
     *
     * @param players the players
     */
    public void setPlayers(int players) {
        this.players = players;
    }

}