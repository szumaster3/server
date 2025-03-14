package core.game.container;

import com.google.errorprone.annotations.CheckReturnValue;
import core.api.ContainerListener;
import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rs.consts.Items;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Container.
 */
public class Container {

    private Item[] items;

    private final int capacity;

    private SortType sortType;

    private final ContainerType type;

    private ContainerEvent event;

    private final List<ContainerListener> listeners = new ArrayList<>(20);

    /**
     * Instantiates a new Container.
     *
     * @param capacity the capacity
     */
    public Container(int capacity) {
        this(capacity, ContainerType.DEFAULT);
    }

    /**
     * Instantiates a new Container.
     *
     * @param capacity the capacity
     * @param items    the items
     */
    public Container(int capacity, Item... items) {
        this(capacity);
        add(items);
    }

    /**
     * Instantiates a new Container.
     *
     * @param capacity the capacity
     * @param type     the type
     */
    public Container(int capacity, ContainerType type) {
        this(capacity, type, SortType.ID);
    }

    /**
     * Instantiates a new Container.
     *
     * @param capacity the capacity
     * @param type     the type
     * @param sortType the sort type
     */
    public Container(int capacity, ContainerType type, SortType sortType) {
        this.capacity = capacity;
        this.type = type;
        this.items = new Item[capacity];
        this.sortType = sortType;
        this.event = new ContainerEvent(capacity);
    }

    /**
     * Register container.
     *
     * @param listener the listener
     * @return the container
     */
    public Container register(ContainerListener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Add boolean.
     *
     * @param items the items
     * @return the boolean
     */
    public boolean add(Item... items) {
        boolean addedAll = true;
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            if (!add(item, false)) {
                addedAll = false;
                break;
            }
        }
        update();
        return addedAll;
    }

    /**
     * Add list.
     *
     * @param items the items
     */
    public void addList(List<Item> items) {
        items.stream().filter(Objects::nonNull).forEach(this::add);
        update();
    }

    /**
     * Insert.
     *
     * @param fromSlot the from slot
     * @param toSlot   the to slot
     */
    public void insert(int fromSlot, int toSlot) {
        insert(fromSlot, toSlot, true);
    }

    /**
     * Insert.
     *
     * @param fromSlot the from slot
     * @param toSlot   the to slot
     * @param update   the update
     */
    public void insert(int fromSlot, int toSlot, boolean update) {
        Item temp = items[fromSlot];
        if (toSlot > fromSlot) {
            for (int i = fromSlot; i < toSlot; i++) {
                replace(get(i + 1), i, false);
            }
        } else if (fromSlot > toSlot) {
            for (int i = fromSlot; i > toSlot; i--) {
                replace(get(i - 1), i, false);
            }
        }
        replace(temp, toSlot, update);
    }

    /**
     * Add boolean.
     *
     * @param item   the item
     * @param player the player
     * @return the boolean
     */
    public boolean add(final Item item, final Player player) {
        if (!add(item, true, -1)) {
            GroundItemManager.create(item, player);
            return false;
        }
        return true;
    }

    /**
     * Add if doesnt have boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean addIfDoesntHave(final Item item) {
        if (containsItem(item)) {
            return false;
        } else {
            return add(item);

        }
    }

    /**
     * Add boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean add(Item item) {
        return add(item, true, -1);
    }

    /**
     * Add boolean.
     *
     * @param item         the item
     * @param fireListener the fire listener
     * @return the boolean
     */
    public boolean add(Item item, boolean fireListener) {
        return add(item, fireListener, -1);
    }

    /**
     * Add boolean.
     *
     * @param item          the item
     * @param fireListener  the fire listener
     * @param preferredSlot the preferred slot
     * @return the boolean
     */
    public boolean add(Item item, boolean fireListener, int preferredSlot) {
        item = item.copy();
        int maximum = getMaximumAdd(item);
        if (maximum == 0) {
            return false;
        }
        if (preferredSlot > -1 && items[preferredSlot] != null) {
            preferredSlot = -1;
        }
        if (item.getAmount() > maximum) {
            item.setAmount(maximum);
        }
        if (type != ContainerType.NEVER_STACK && (item.getDefinition().isStackable() || type == ContainerType.ALWAYS_STACK || type == ContainerType.SHOP)) {
            boolean hashBased = sortType == SortType.HASH;
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    if ((hashBased && items[i].getIdHash() == item.getIdHash()) || (!hashBased && items[i].getId() == item.getId())) {
                        int totalCount = item.getAmount() + items[i].getAmount();
                        items[i] = new Item(items[i].getId(), totalCount, item.getCharge());
                        items[i].setIndex(i);
                        event.flag(i, items[i]);
                        if (fireListener) {
                            update();
                        }
                        return true;
                    }
                }
            }
            int slot = preferredSlot > -1 ? preferredSlot : freeSlot();
            if (slot == -1) {
                return false;
            }
            items[slot] = item;
            item.setIndex(slot);
            event.flag(slot, item);
            if (fireListener) {
                update();
            }
            return true;
        }
        int slots = freeSlots();
        if (slots >= item.getAmount()) {
            for (int i = 0; i < item.getAmount(); i++) {
                int slot = i == 0 && preferredSlot > -1 ? preferredSlot : freeSlot();
                items[slot] = new Item(item.getId(), 1, item.getCharge());
                items[slot].setIndex(slot);
                event.flag(slot, items[slot]);
            }
            if (fireListener) {
                update();
            }
            return true;
        }
        return false;
    }

    /**
     * Remove boolean.
     *
     * @param items the items
     * @return the boolean
     */
    @CheckReturnValue
    public boolean remove(Item... items) {
        boolean removedAll = true;
        for (Item item : items) {
            if (!remove(item, false)) {
                removedAll = false;
            }
        }
        update();
        return removedAll;
    }

    /**
     * Remove boolean.
     *
     * @param item the item
     * @return the boolean
     */
    @CheckReturnValue
    public boolean remove(Item item) {
        return remove(item, true);
    }

    /**
     * Remove boolean.
     *
     * @param item         the item
     * @param fireListener the fire listener
     * @return the boolean
     */
    @CheckReturnValue
    public boolean remove(Item item, boolean fireListener) {
        int slot = getSlot(item);
        if (slot != -1) {
            return remove(item, slot, fireListener);
        }
        return false;
    }

    /**
     * Remove boolean.
     *
     * @param item         the item
     * @param slot         the slot
     * @param fireListener the fire listener
     * @return the boolean
     */
    @CheckReturnValue
    public boolean remove(Item item, int slot, boolean fireListener) {
        if (!contains(item.getId(), item.getAmount()))
            return false;
        Item oldItem = items[slot];
        if (oldItem == null || oldItem.getId() != item.getId()) {
            return false;
        }
        if (item.getAmount() < 1) {
            return true;
        }
        if (oldItem.getDefinition().isStackable() || type.equals(ContainerType.ALWAYS_STACK) || type == ContainerType.SHOP) {
            if (item.getAmount() >= oldItem.getAmount()) {
                items[slot] = null;
                event.flagNull(slot);
                if (fireListener) {
                    update();
                }
                return true;
            }
            items[slot] = new Item(item.getId(), oldItem.getAmount() - item.getAmount(), item.getCharge());
            items[slot].setIndex(slot);
            event.flag(slot, items[slot]);
            if (fireListener) {
                update();
            }
            return true;
        }
        items[slot] = null;
        event.flagNull(slot);
        int removed = 1;
        for (int i = removed; i < item.getAmount(); i++) {
            slot = getSlot(item);
            if (slot != -1) {
                items[slot] = null;
                event.flagNull(slot);
            } else {
                break;
            }
        }
        if (fireListener) {
            update();
        }
        return true;
    }

    /**
     * Remove all boolean.
     *
     * @param ids the ids
     * @return the boolean
     */
    public boolean removeAll(int[] ids) {
        boolean removedAll = true;
        for (int id : ids) {
            if (!removeAll(id)) {
                removedAll = false;
            }
        }
        update();
        return removedAll;
    }

    /**
     * Remove all boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean removeAll(int id) {
        ArrayList<Item> matchingIdItems = new ArrayList<>();
        for (Item item : this.items) {
            if (item != null && item.getId() == id) {
                matchingIdItems.add(item);
            }
        }
        boolean res = true;
        for (Item item : matchingIdItems) {
            if (!remove(item, false)) {
                res = false;
            }
        }
        return res;
    }

    /**
     * Replace item.
     *
     * @param item the item
     * @param slot the slot
     * @return the item
     */
    public Item replace(Item item, int slot) {
        return replace(item, slot, true);
    }

    /**
     * Replace item.
     *
     * @param item         the item
     * @param slot         the slot
     * @param fireListener the fire listener
     * @return the item
     */
    public Item replace(Item item, int slot, boolean fireListener) {
        if (item != null) {
            if (item.getAmount() < 1 && type != ContainerType.SHOP) {
                item = null;
            } else {
                item = item.copy();
            }
        }
        Item oldItem = items[slot];
        items[slot] = item;
        if (item == null) {
            event.flagNull(slot);
        } else {
            item.setIndex(slot);
            event.flag(slot, item);
        }
        if (fireListener) {
            update();
        }
        return oldItem;
    }

    /**
     * Update.
     */
    public void update() {
        if (event.getChangeCount() < 1 && !event.isClear()) {
            return;
        }
        for (ContainerListener listener : listeners) {
            listener.update(this, event);
        }
        event.setClear(false);
        event = new ContainerEvent(capacity);
    }

    /**
     * Update.
     *
     * @param force the force
     */
    public void update(boolean force) {
        if (event.getChangeCount() < 1 && !force) {
            return;
        }
        for (ContainerListener listener : listeners) {
            listener.update(this, event);
        }
        event = new ContainerEvent(capacity);
    }

    /**
     * Refresh.
     */
    public void refresh() {
        for (ContainerListener listener : listeners) {
            listener.refresh(this);
        }
        event = new ContainerEvent(capacity);
    }

    /**
     * Refresh.
     *
     * @param listener the listener
     */
    public void refresh(ContainerListener listener) {
        listener.refresh(this);
        event = new ContainerEvent(capacity);
    }

    /**
     * Gets as id.
     *
     * @param slot the slot
     * @return the as id
     */
    public int getAsId(int slot) {
        if (slot < 0 || slot >= items.length || items[slot] == null) {
            return 0;
        }
        return items[slot].getId();
    }

    /**
     * Get item.
     *
     * @param slot the slot
     * @return the item
     */
    public Item get(int slot) {
        if (slot < 0 || slot >= items.length) {
            return null;
        }
        return items[slot];
    }

    /**
     * Gets new.
     *
     * @param slot the slot
     * @return the new
     */
    public Item getNew(int slot) {
        Item item = items[slot];
        if (item != null) {
            return item;
        }
        return new Item(0);
    }

    /**
     * Gets id.
     *
     * @param slot the slot
     * @return the id
     */
    public int getId(int slot) {
        if (slot >= items.length) {
            return -1;
        }
        Item item = items[slot];
        if (item != null) {
            return item.getId();
        }
        return -1;
    }

    /**
     * Parse int.
     *
     * @param buffer the buffer
     * @return the int
     */
    public int parse(ByteBuffer buffer) {
        int slot;
        int total = 0;
        while ((slot = buffer.getShort()) != -1) {
            int id = buffer.getShort() & 0xFFFF;
            int amount = buffer.getInt();
            int charge = buffer.getInt();
            if (id >= ItemDefinition.getDefinitions().size() || slot >= items.length || slot < 0) {
                continue;
            }
            Item item = items[slot] = new Item(id, amount, charge);
            item.setIndex(slot);
            total += item.getValue();
        }
        return total;
    }

    /**
     * Parse.
     *
     * @param itemArray the item array
     */
    public void parse(JSONArray itemArray) {
        AtomicInteger total = new AtomicInteger(0);
        itemArray.forEach(item -> {
            JSONObject i = (JSONObject) item;
            int slot = Integer.parseInt(i.get("slot").toString());
            int id = Integer.parseInt(i.get("id").toString());
            int amount = Integer.parseInt(i.get("amount").toString());
            int charge = Integer.parseInt(i.get("charge").toString());
            if (id >= ItemDefinition.getDefinitions().size() || id < 0 || slot >= items.length || id == Items.MAGIC_CARPET_5614) {
            } else {
                Item it = items[slot] = new Item(id, amount, charge);
                it.setIndex(slot);
                total.set(total.get() + (int) it.getValue());
            }
        });
    }

    /**
     * Save long.
     *
     * @param buffer the buffer
     * @return the long
     */
    public long save(ByteBuffer buffer) {
        long totalValue = 0;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item == null) {
                continue;
            }
            buffer.putShort((short) i);
            buffer.putShort((short) item.getId());
            buffer.putInt(item.getAmount());
            buffer.putInt(item.getCharge());
            totalValue += item.getValue();
        }
        buffer.putShort((short) -1);
        return totalValue;
    }

    /**
     * Copy.
     *
     * @param c the c
     */
    public void copy(Container c) {
        items = new Item[c.items.length];
        for (int i = 0; i < items.length; i++) {
            Item it = c.items[i];
            if (it == null) {
                continue;
            }
            items[i] = new Item(it.getId(), it.getAmount(), it.getCharge());
            items[i].setIndex(i);
        }
    }

    /**
     * Format string.
     *
     * @return the string
     */
    public String format() {
        String log = "";
        Map<Integer, Integer> map = new HashMap<>();
        Integer old = null;
        for (Item item : items) {
            if (item != null) {
                old = map.get(item.getId());
                map.put(item.getId(), old == null ? item.getAmount() : old + item.getAmount());

            }
        }
        for (int i : map.keySet()) {
            log += i + "," + map.get(i) + "|";
        }
        if (log.length() > 0 && log.charAt(log.length() - 1) == '|') {
            log = log.substring(0, log.length() - 1);
        }
        return log;
    }

    /**
     * Contains item boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean containsItem(Item item) {
        return contains(item.getId(), item.getAmount());
    }

    /**
     * Contains items boolean.
     *
     * @param items the items
     * @return the boolean
     */
    public boolean containsItems(Item... items) {
        for (Item i : items) {
            if (!containsItem(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Contains boolean.
     *
     * @param itemId the item id
     * @param amount the amount
     * @return the boolean
     */
    public boolean contains(int itemId, int amount) {
        int count = 0;
        for (Item item : items) {
            if (item != null && item.getId() == itemId) {
                if ((count += item.getAmount()) >= amount) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Contains at least one item boolean.
     *
     * @param itemId the item id
     * @return the boolean
     */
    public boolean containsAtLeastOneItem(int itemId) {
        for (Item item : items) {
            if (item != null && item.getId() == itemId && item.getAmount() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains at least one item boolean.
     *
     * @param itemIds the item ids
     * @return the boolean
     */
    public boolean containsAtLeastOneItem(int[] itemIds) {
        for (int id : itemIds) {
            if (getAmount(id) > 0)
                return true;
        }
        return false;
    }

    /**
     * Contains at least one item boolean.
     *
     * @param items the items
     * @return the boolean
     */
    public boolean containsAtLeastOneItem(Item... items) {
        for (Item item : items) {
            if (containsItem(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains all boolean.
     *
     * @param itemIds the item ids
     * @return the boolean
     */
    public boolean containsAll(int... itemIds) {
        for (int i : itemIds) {
            if (!containsAtLeastOneItem(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add all.
     *
     * @param container the container
     */
    public void addAll(Container container) {
        add(container.items);
    }

    /**
     * Gets maximum add.
     *
     * @param item the item
     * @return the maximum add
     */
    public int getMaximumAdd(Item item) {
        if (type != ContainerType.NEVER_STACK) {
            if (item.getDefinition().isStackable() || type == ContainerType.ALWAYS_STACK || type == ContainerType.SHOP) {
                if (contains(item.getId(), 1)) {
                    return Integer.MAX_VALUE - getAmount(item);
                }
                return freeSlots() > 0 ? Integer.MAX_VALUE : 0;
            }
        }
        return freeSlots();
    }

    /**
     * Has space for boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean hasSpaceFor(Item item) {
        return item.getAmount() <= getMaximumAdd(item);
    }

    /**
     * Has space for boolean.
     *
     * @param items the items
     * @return the boolean
     */
    public boolean hasSpaceFor(Item... items) {
        Container c = new Container(28, ContainerType.DEFAULT);
        c.add(items);
        return this.hasSpaceFor(c);
    }

    /**
     * Has space for boolean.
     *
     * @param c the c
     * @return the boolean
     */
    public boolean hasSpaceFor(Container c) {
        if (c == null) {
            return false;
        }
        Container check = new Container(capacity, type);
        check.addAll(this);
        for (Item item : c.items) {
            if (item != null) {
                if (!check.add(item, false)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets slot.
     *
     * @param item the item
     * @return the slot
     */
    public int getSlot(Item item) {
        if (item == null) {
            return -1;
        }
        int id = item.getId();
        for (int i = 0; i < items.length; i++) {
            Item it = items[i];
            if (it != null && it.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets slot hash.
     *
     * @param item the item
     * @return the slot hash
     */
    public int getSlotHash(Item item) {
        if (item == null) {
            return -1;
        }
        int idHash = item.getIdHash();
        for (int i = 0; i < items.length; i++) {
            Item it = items[i];
            if (it != null && it.getIdHash() == idHash) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets item.
     *
     * @param item the item
     * @return the item
     */
    public Item getItem(Item item) {
        return get(getSlot(item));
    }

    /**
     * Get item.
     *
     * @param item the item
     * @return the item
     */
    public Item get(Item item) {
        for (Item i : items) {
            if (i == null) continue;
            if (item.getId() == i.getId()) return i;
        }
        return null;
    }

    /**
     * Gets all.
     *
     * @param item the item
     * @return the all
     */
    public ArrayList<Item> getAll(Item item) {
        ArrayList<Item> ret = new ArrayList<Item>();
        for (Item i : items) {
            if (i == null) continue;
            if (item.getId() == i.getId()) ret.add(i);
        }
        return ret;
    }

    /**
     * Free slot int.
     *
     * @return the int
     */
    public int freeSlot() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets add slot.
     *
     * @param item the item
     * @return the add slot
     */
    public int getAddSlot(Item item) {
        if (type != ContainerType.NEVER_STACK && (item.getDefinition().isStackable() || type.equals(ContainerType.ALWAYS_STACK) || type == ContainerType.SHOP)) {
            boolean hashBased = sortType == SortType.HASH;
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    if ((hashBased && items[i].getIdHash() == item.getIdHash()) || (!hashBased && items[i].getId() == item.getId())) {
                        return i;
                    }
                }
            }
        }
        return freeSlot();
    }

    /**
     * Free slots int.
     *
     * @return the int
     */
    public int freeSlots() {
        return capacity - itemCount();
    }

    /**
     * Item count int.
     *
     * @return the int
     */
    public int itemCount() {
        int size = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                size++;
            }
        }
        return size;
    }

    /**
     * Contain items boolean.
     *
     * @param itemIds the item ids
     * @return the boolean
     */
    public boolean containItems(int... itemIds) {
        for (int i = 0; i < itemIds.length; i++) {
            if (!contains(itemIds[i], 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets amount.
     *
     * @param item the item
     * @return the amount
     */
    public int getAmount(Item item) {
        if (item == null) {
            return 0;
        }
        int count = 0;
        for (Item i : items) {
            if (i != null && i.getId() == item.getId()) {
                count += i.getAmount();
            }
        }
        return count;
    }

    /**
     * Gets amount.
     *
     * @param id the id
     * @return the amount
     */
    public int getAmount(int id) {
        return getAmount(new Item(id));
    }

    /**
     * Shift.
     */
    public void shift() {
        final Item[] itemss = items;
        clear(false);
        for (Item item : itemss) {
            if (item == null) {
                continue;
            }
            add(item, false);
        }
        refresh();
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        for (Item item : items) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is full boolean.
     *
     * @return the boolean
     */
    public boolean isFull() {
        return freeSlots() < 1;
    }

    /**
     * Clear.
     */
    public void clear() {
        clear(true);
    }

    /**
     * Clear.
     *
     * @param update the update
     */
    public void clear(boolean update) {
        items = new Item[capacity];
        event.flagEmpty();
        if (update) {
            refresh();
        }
    }

    /**
     * Gets wealth.
     *
     * @return the wealth
     */
    public int getWealth() {
        int wealth = 0;
        for (Item i : items) {
            if (i == null) {
                continue;
            }
            wealth += i.getDefinition().getValue() * i.getAmount();
        }
        return wealth;
    }

    /**
     * To array item [ ].
     *
     * @return the item [ ]
     */
    public Item[] toArray() {
        return items;
    }

    /**
     * Gets listeners.
     *
     * @return the listeners
     */
    public List<ContainerListener> getListeners() {
        return listeners;
    }

    /**
     * Capacity int.
     *
     * @return the int
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Gets event.
     *
     * @return the event
     */
    public ContainerEvent getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "Container{" +
            "items=" + Arrays.toString(items) +
            ", capacity=" + capacity +
            ", sortType=" + sortType +
            ", type=" + type +
            ", event=" + event +
            ", listeners=" + listeners +
            '}';
    }
}
