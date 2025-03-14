package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Run script context.
 */
public class RunScriptContext implements Context {

    private Player player;

    private int id;

    private Object[] objects;

    private String string;

    /**
     * Instantiates a new Run script context.
     *
     * @param player  the player
     * @param id      the id
     * @param string  the string
     * @param objects the objects
     */
    public RunScriptContext(Player player, int id, String string, Object... objects) {
        this.player = player;
        this.id = id;
        this.objects = objects;
        this.string = string;
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
     * Get objects object [ ].
     *
     * @return the object [ ]
     */
    public Object[] getObjects() {
        return objects;
    }

    /**
     * Gets string.
     *
     * @return the string
     */
    public String getString() {
        return string;
    }
}
