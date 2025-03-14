package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Player context.
 */
public final class PlayerContext implements Context {

    private final Player player;

    /**
     * Instantiates a new Player context.
     *
     * @param player the player
     */
    public PlayerContext(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}