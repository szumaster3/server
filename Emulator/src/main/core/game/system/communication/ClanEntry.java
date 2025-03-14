package core.game.system.communication;

import core.game.node.entity.player.Player;
import core.game.world.GameWorld;

/**
 * The type Clan entry.
 */
public class ClanEntry {

    private final String name;

    private Player player;

    private int worldId;

    /**
     * Instantiates a new Clan entry.
     *
     * @param player the player
     */
    public ClanEntry(Player player) {
        this.player = player;
        this.name = player.getName();
        this.worldId = GameWorld.getSettings().getWorldId();
    }

    /**
     * Instantiates a new Clan entry.
     *
     * @param name    the name
     * @param worldId the world id
     */
    public ClanEntry(String name, int worldId) {
        this.name = name;
        this.worldId = worldId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        ClanEntry e = (ClanEntry) o;
        if (name != null && !name.equals(e.name)) {
            return false;
        }
        return e.player == player;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
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
     * Sets world id.
     *
     * @param worldId the world id
     */
    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }
}