package core.net.packet.context;

import core.game.container.Container;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.Context;

/**
 * The type Container context.
 */
public final class ContainerContext implements Context {

    private final int interfaceId;
    private final int childId;
    private final int containerId;
    private final int length;
    private final boolean split;
    private final int[] slots;
    /**
     * The Ids.
     */
    public int[] ids;
    private Player player;
    private Item[] items;
    private boolean clear;

    /**
     * Instantiates a new Container context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param clear       the clear
     */
    public ContainerContext(Player player, int interfaceId, int childId, boolean clear) {
        this(player, interfaceId, childId, 0, null, 1, false);
        this.clear = clear;
    }

    /**
     * Instantiates a new Container context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param containerId the container id
     * @param container   the container
     * @param split       the split
     */
    public ContainerContext(Player player, int interfaceId, int childId, int containerId, Container container, boolean split) {
        this(player, interfaceId, childId, containerId, container.toArray(), container.toArray().length, split);
    }

    /**
     * Instantiates a new Container context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param containerId the container id
     * @param items       the items
     * @param split       the split
     */
    public ContainerContext(Player player, int interfaceId, int childId, int containerId, Item[] items, boolean split) {
        this(player, interfaceId, childId, containerId, items, items.length, split);
    }

    /**
     * Instantiates a new Container context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param containerId the container id
     * @param items       the items
     * @param length      the length
     * @param split       the split
     */
    public ContainerContext(Player player, int interfaceId, int childId, int containerId, Item[] items, int length, boolean split) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.childId = childId;
        this.containerId = containerId;
        this.items = items;
        this.length = length;
        this.split = split;
        this.slots = null;
    }

    /**
     * Instantiates a new Container context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param containerId the container id
     * @param items       the items
     */
    public ContainerContext(Player player, int interfaceId, int childId, int containerId, int[] items) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.childId = childId;
        this.containerId = containerId;
        this.ids = items;
        this.length = items.length;
        this.split = false;
        this.slots = null;
    }

    /**
     * Instantiates a new Container context.
     *
     * @param player      the player
     * @param interfaceId the interface id
     * @param childId     the child id
     * @param containerId the container id
     * @param items       the items
     * @param split       the split
     * @param slots       the slots
     */
    public ContainerContext(Player player, int interfaceId, int childId, int containerId, Item[] items, boolean split, int... slots) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.childId = childId;
        this.containerId = containerId;
        this.items = items;
        this.length = items.length;
        this.split = split;
        this.slots = slots;
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
     * Gets container id.
     *
     * @return the container id
     */
    public int getContainerId() {
        return containerId;
    }

    /**
     * Get items item [ ].
     *
     * @return the item [ ]
     */
    public Item[] getItems() {
        return items;
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Is split boolean.
     *
     * @return the boolean
     */
    public boolean isSplit() {
        return split;
    }

    /**
     * Get slots int [ ].
     *
     * @return the int [ ]
     */
    public int[] getSlots() {
        return slots;
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
     * Is clear boolean.
     *
     * @return the boolean
     */
    public boolean isClear() {
        return clear;
    }

    /**
     * Sets clear.
     *
     * @param clear the clear
     */
    public void setClear(boolean clear) {
        this.clear = clear;
    }

}