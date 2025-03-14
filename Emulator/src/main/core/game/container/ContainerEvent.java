package core.game.container;

import core.game.node.item.Item;

/**
 * The type Container event.
 */
public final class ContainerEvent {

    /**
     * The constant NULL_ITEM.
     */
    public static final Item NULL_ITEM = new Item(0, 0);

    private final Item[] items;

    private boolean clear;

    /**
     * Instantiates a new Container event.
     *
     * @param size the size
     */
    public ContainerEvent(int size) {
        this.items = new Item[size];
    }

    /**
     * Flag null.
     *
     * @param slot the slot
     */
    public void flagNull(int slot) {
        items[slot] = NULL_ITEM;
    }

    /**
     * Flag.
     *
     * @param slot the slot
     * @param item the item
     */
    public void flag(int slot, Item item) {
        items[slot] = item;
    }

    /**
     * Gets change count.
     *
     * @return the change count
     */
    public int getChangeCount() {
        int count = 0;
        for (Item item : items) {
            if (item != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get slots int [ ].
     *
     * @return the int [ ]
     */
    public int[] getSlots() {
        int size = 0;
        int[] slots = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                slots[size++] = i;
            }
        }
        int[] slot = new int[size];
        for (int i = 0; i < size; i++) {
            slot[i] = slots[i];
        }
        return slot;
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
     * Flag empty.
     */
    public void flagEmpty() {
        this.clear = true;
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
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