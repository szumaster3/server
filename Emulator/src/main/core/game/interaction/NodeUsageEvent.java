package core.game.interaction;

import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Event triggered when a node is used on another node.
 */
public final class NodeUsageEvent {

    private final Player player;
    private final int componentId;
    private final Node used, with;

    /**
     * Creates a new usage event.
     *
     * @param player      the player using the item.
     * @param componentId interface component id.
     * @param used        the node being used.
     * @param with        the target node.
     */
    public NodeUsageEvent(Player player, int componentId, Node used, Node with) {
        this.player = player;
        this.componentId = componentId;
        this.used = used;
        this.with = with;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the interface component id.
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * @return the node being used.
     */
    public Node getUsed() {
        return used;
    }

    /**
     * @return the target node.
     */
    public Node getUsedWith() {
        return with;
    }

    /**
     * @return the used node as item, or null if not an item.
     */
    public Item getUsedItem() {
        return used instanceof Item ? (Item) used : null;
    }

    /**
     * @return the target node as item, or null if not an item.
     */
    public Item getBaseItem() {
        return with instanceof Item ? (Item) with : null;
    }
}
