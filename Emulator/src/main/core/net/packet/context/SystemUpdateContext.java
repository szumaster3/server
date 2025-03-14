package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type System update context.
 */
public class SystemUpdateContext implements Context {

    private Player player;

    private int time;

    /**
     * Instantiates a new System update context.
     *
     * @param player the player
     * @param time   the time
     */
    public SystemUpdateContext(Player player, int time) {
        this.player = player;
        this.setTime(time);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(int time) {
        this.time = time;
    }

}
