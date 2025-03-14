package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Config context.
 */
public class ConfigContext implements Context {

    private Player player;

    private int id;

    private int value;

    private boolean cs2;

    /**
     * Instantiates a new Config context.
     *
     * @param player the player
     * @param id     the id
     * @param value  the value
     */
    public ConfigContext(Player player, int id, int value) {
        this(player, id, value, false);
    }

    /**
     * Instantiates a new Config context.
     *
     * @param player the player
     * @param id     the id
     * @param value  the value
     * @param cs2    the cs 2
     */
    public ConfigContext(Player player, int id, int value, boolean cs2) {
        this.player = player;
        this.id = id;
        this.value = value;
        this.cs2 = cs2;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     * @return the player
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
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
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Is cs 2 boolean.
     *
     * @return the boolean
     */
    public boolean isCs2() {
        return cs2;
    }
}
