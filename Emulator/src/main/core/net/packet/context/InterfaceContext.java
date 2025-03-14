package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Interface context.
 */
public final class InterfaceContext implements Context {

    private final int windowId;
    private final int interfaceId;
    private final boolean walkable;
    private Player player;
    private int componentId;

    /**
     * Instantiates a new Interface context.
     *
     * @param player      the player
     * @param windowId    the window id
     * @param componentId the component id
     * @param interfaceId the interface id
     * @param walkable    the walkable
     */
    public InterfaceContext(Player player, int windowId, int componentId, int interfaceId, boolean walkable) {
        this.player = player;
        this.windowId = windowId;
        this.componentId = componentId;
        this.interfaceId = interfaceId;
        this.walkable = walkable;
    }

    /**
     * Transform interface context.
     *
     * @param player the player
     * @param id     the id
     * @return the interface context
     */
    public InterfaceContext transform(Player player, int id) {
        return new InterfaceContext(player, windowId, componentId, id, walkable);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     * @return the player
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
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
     * Gets component id.
     *
     * @return the component id
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * Sets component id.
     *
     * @param componentId the component id
     */
    public void setComponentId(int componentId) {
        this.componentId = componentId;
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
     * Is walkable boolean.
     *
     * @return the boolean
     */
    public boolean isWalkable() {
        return walkable;
    }

}