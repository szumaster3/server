package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Windows pane context.
 */
public final class WindowsPaneContext implements Context {

    private final Player player;

    private final int windowId;

    private final int type;

    /**
     * Instantiates a new Windows pane context.
     *
     * @param player   the player
     * @param windowId the window id
     * @param type     the type
     */
    public WindowsPaneContext(Player player, int windowId, int type) {
        this.player = player;
        this.windowId = windowId;
        this.type = type;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets window id.
     *
     * @return the window id
     */
    public int getWindowId() {
        return windowId;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

}
