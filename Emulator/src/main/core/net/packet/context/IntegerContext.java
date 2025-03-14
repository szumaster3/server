package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Integer context.
 */
public class IntegerContext implements Context {

    private Player player;

    private int integer;

    /**
     * Instantiates a new Integer context.
     *
     * @param player  the player
     * @param integer the integer
     */
    public IntegerContext(Player player, int integer) {
        this.player = player;
        this.setInteger(integer);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets integer.
     *
     * @return the integer
     */
    public int getInteger() {
        return integer;
    }

    /**
     * Sets integer.
     *
     * @param integer the integer
     */
    public void setInteger(int integer) {
        this.integer = integer;
    }

}
