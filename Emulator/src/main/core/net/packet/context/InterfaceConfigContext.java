package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Interface config context.
 */
public class InterfaceConfigContext implements Context {

    private Player player;

    private int interfaceId;

    private int childId;

    private boolean hide;

    /**
     * Instantiates a new Interface config context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param hide        the hide
     */
    public InterfaceConfigContext(Player player, int interfaceId, int childId, boolean hide) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.childId = childId;
        this.hide = hide;
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
     * Gets child id.
     *
     * @return the child id
     */
    public int getChildId() {
        return childId;
    }

    /**
     * Is hidden boolean.
     *
     * @return the boolean
     */
    public boolean isHidden() {
        return hide;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
