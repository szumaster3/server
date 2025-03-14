package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;

/**
 * The type Run script.
 */
public abstract class RunScript {

    /**
     * The Player.
     */
    protected Player player;

    /**
     * The Value.
     */
    protected Object value;

    /**
     * Instantiates a new Run script.
     */
    public RunScript() {

    }

    /**
     * Handle boolean.
     *
     * @return the boolean
     */
    public abstract boolean handle();

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
