package core.game.container;

import core.game.node.item.Item;

/**
 * Represents an event related to a container, tracking changes to its items.
 */
public final class ContainerEvent {

    /**
     * A constant representing a null item.
     */
    public static final Item NULL_ITEM = new Item(0, 0);

    private final Item[] items;
    private boolean clear;

    /**
     * Constructs a new ContainerEvent with the specified size.
     *
     * @param size the number of slots in the container
     */
    public ContainerEvent(int size) {
        this.items = new Item[size];
    }

    /**
     * Flags the specified slot as containing a null item.
     *
     * @param slot the slot index to flag as null
     */
    public void flagNull(int slot) {
        items[slot] = NULL_ITEM;
    }

    /**
     * Updates the specified slot with a given item.
     *
     * @param slot the slot index to update
     * @param item the item to place in the slot
     */
    public void flag(int slot, Item item) {
        items[slot] = item;
    }

    /**
     * Retrieves the number of slots that have been changed.
     *
     * @return the count of changed slots
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
     * Retrieves the slot indices that have been modified.
     *
     * @return an array of indices corresponding to changed slots
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
        System.arraycopy(slots, 0, slot, 0, size);
        return slot;
    }

    /**
     * Retrieves the array of items currently stored in the container.
     *
     * @return an array of items in the container
     */
    public Item[] getItems() {
        return items;
    }

    /**
     * Clears the container by setting all slots to null and marking it as cleared.
     */
    public void flagEmpty() {
        this.clear = true;
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
    }

    /**
     * Checks if the container has been cleared.
     *
     * @return {@code true} if the container is cleared, otherwise {@code false}
     */
    public boolean isClear() {
        return clear;
    }

    /**
     * Sets the cleared status of the container.
     *
     * @param clear {@code true} to mark the container as cleared, otherwise {@code false}
     */
    public void setClear(boolean clear) {
        this.clear = clear;
    }
}