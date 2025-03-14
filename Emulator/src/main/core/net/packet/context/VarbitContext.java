package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Varbit context.
 */
public class VarbitContext implements Context {

    /**
     * The Varbit id.
     */
    public int varbitId;
    /**
     * The Value.
     */
    public int value;
    /**
     * The Player.
     */
    Player player;

    /**
     * Instantiates a new Varbit context.
     *
     * @param player   the player
     * @param varbitId the varbit id
     * @param value    the value
     */
    public VarbitContext(Player player, int varbitId, int value) {
        this.player = player;
        this.varbitId = varbitId;
        this.value = value;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
