package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Varc update context.
 */
public class VarcUpdateContext implements Context {

    /**
     * The Varc id.
     */
    public int varcId;
    /**
     * The Value.
     */
    public int value;
    /**
     * The Player.
     */
    Player player;

    /**
     * Instantiates a new Varc update context.
     *
     * @param player the player
     * @param varcId the varc id
     * @param value  the value
     */
    public VarcUpdateContext(Player player, int varcId, int value) {
        this.player = player;
        this.varcId = varcId;
        this.value = value;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}

