package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Minimap state context.
 */
public class MinimapStateContext implements Context {

    private final Player player;

    private final int state;

    /**
     * Instantiates a new Minimap state context.
     *
     * @param player the player
     * @param state  the state
     */
    public MinimapStateContext(Player player, int state) {
        this.player = player;
        this.state = state;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public int getState() {
        return state;
    }

}