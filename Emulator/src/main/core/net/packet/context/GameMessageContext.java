package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Game message context.
 */
public final class GameMessageContext implements Context {

    private Player player;

    private String message;

    /**
     * Instantiates a new Game message context.
     *
     * @param player  the player
     * @param message the message
     */
    public GameMessageContext(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
