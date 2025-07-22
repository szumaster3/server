package core.game.system.communication;

import core.game.node.entity.player.Player;
import core.game.world.GameWorld;

import java.util.Objects;

/**
 * Represents an entry of a player in a clan.
 */
public class ClanEntry {

    private final String name;
    private Player player;
    private int worldId;

    /**
     * Constructs a new ClanEntry for the given player.
     *
     * @param player the player instance
     */
    public ClanEntry(Player player) {
        this.player = player;
        this.name = player.getName();
        this.worldId = GameWorld.getSettings().getWorldId();
    }

    /**
     * Constructs a new ClanEntry for the given player name and world ID.
     *
     * @param name    the player's name
     * @param worldId the world ID where the player is located
     */
    public ClanEntry(String name, int worldId) {
        this.name = name;
        this.worldId = worldId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClanEntry)) return false;
        ClanEntry that = (ClanEntry) o;
        return worldId == that.worldId &&
                Objects.equals(name, that.name) &&
                Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, player, worldId);
    }

    /**
     * Gets the player name.
     *
     * @return the player name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player instance.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the player instance.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the world id.
     *
     * @return the world id
     */
    public int getWorldId() {
        return worldId;
    }

    /**
     * Sets the world id.
     *
     * @param worldId the world id
     */
    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }
}