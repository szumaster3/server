package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type String context.
 */
public class StringContext implements Context {

    private Player player;

    private String string;

    private int interfaceId;

    private int lineId;

    /**
     * Instantiates a new String context.
     *
     * @param player      the player
     * @param string      the string
     * @param interfaceId the interface id
     * @param lineId      the line id
     */
    public StringContext(Player player, String string, int interfaceId, int lineId) {
        this.player = player;
        this.string = string;
        this.interfaceId = interfaceId;
        this.lineId = lineId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets string.
     *
     * @return the string
     */
    public String getString() {
        return string;
    }

    /**
     * Gets interface id.
     *
     * @return the interface id
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets line id.
     *
     * @return the line id
     */
    public int getLineId() {
        return lineId;
    }
}
