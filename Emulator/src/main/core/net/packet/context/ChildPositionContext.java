package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

import java.awt.*;

/**
 * The type Child position context.
 */
public final class ChildPositionContext implements Context {

    private final Player player;

    private final int interfaceId;

    private final int childId;

    private final Point position;

    /**
     * Instantiates a new Child position context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param positionX   the position x
     * @param positionY   the position y
     */
    public ChildPositionContext(Player player, int interfaceId, int childId, int positionX, int positionY) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.childId = childId;
        this.position = new Point(positionX, positionY);
    }

    @Override
    public Player getPlayer() {
        return player;
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
     * Gets position.
     *
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

}