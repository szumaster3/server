package core.game.interaction;

import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * The type Node usage event.
 */
public final class NodeUsageEvent {

    private final Player player;

    private final int componentId;

    private final Node used;

    private final Node with;

    /**
     * Instantiates a new Node usage event.
     *
     * @param player      the player
     * @param componentId the component id
     * @param used        the used
     * @param with        the with
     */
    public NodeUsageEvent(Player player, int componentId, Node used, Node with) {
        this.player = player;
        this.componentId = componentId;
        this.used = used;
        this.with = with;
    }

    /**
     * Gets base item.
     *
     * @return the base item
     */
    public Item getBaseItem() {
        return with instanceof Item ? (Item) with : null;
    }

    /**
     * Gets used item.
     *
     * @return the used item
     */
    public Item getUsedItem() {
        return used instanceof Item ? (Item) used : null;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
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
     * Gets used.
     *
     * @return the used
     */
    public Node getUsed() {
        return used;
    }

    /**
     * Gets used with.
     *
     * @return the used with
     */
    public Node getUsedWith() {
        return with;
    }

}
