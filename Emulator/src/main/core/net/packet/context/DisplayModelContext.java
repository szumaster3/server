package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Display model context.
 */
public class DisplayModelContext implements Context {

    private final Player player;
    private final ModelType type;
    private final int nodeId;
    private final int interfaceId;
    private final int childId;
    private int amount;
    private int zoom;

    /**
     * Instantiates a new Display model context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public DisplayModelContext(Player player, int interfaceId, int childId) {
        this(player, ModelType.PLAYER, -1, 0, interfaceId, childId);
    }

    /**
     * Instantiates a new Display model context.
     *
     * @param player      the player
     * @param nodeId      the node id
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public DisplayModelContext(Player player, int nodeId, int interfaceId, int childId) {
        this(player, ModelType.NPC, nodeId, 0, interfaceId, childId);
    }

    /**
     * Instantiates a new Display model context.
     *
     * @param player      the player
     * @param type        the type
     * @param nodeId      the node id
     * @param amount      the amount
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public DisplayModelContext(Player player, ModelType type, int nodeId, int amount, int interfaceId, int childId) {
        this.player = player;
        this.type = type;
        this.nodeId = nodeId;
        this.amount = amount;
        this.interfaceId = interfaceId;
        this.childId = childId;
    }

    /**
     * Instantiates a new Display model context.
     *
     * @param player      the player
     * @param type        the type
     * @param nodeId      the node id
     * @param zoom        the zoom
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param object      the object
     */
    public DisplayModelContext(Player player, ModelType type, int nodeId, int zoom, int interfaceId, int childId, Object... object) {
        this.player = player;
        this.type = type;
        this.nodeId = nodeId;
        this.setZoom(zoom);
        this.interfaceId = interfaceId;
        this.childId = childId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public ModelType getType() {
        return type;
    }

    /**
     * Gets node id.
     *
     * @return the node id
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
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
     * Gets zoom.
     *
     * @return the zoom
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * Sets zoom.
     *
     * @param zoom the zoom
     */
    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    /**
     * The enum Model type.
     */
    public static enum ModelType {
        /**
         * Player model type.
         */
        PLAYER,
        /**
         * Npc model type.
         */
        NPC,
        /**
         * Item model type.
         */
        ITEM,
        /**
         * Model model type.
         */
        MODEL;
    }

}