package core.game.node.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.DestinationFlag;
import core.game.interaction.InteractPlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.equipment.DegradableEquipment;

/**
 * The type Item.
 */
public class Item extends Node {

    private int idHash;

    private int amount;

    private ItemDefinition definition;

    /**
     * Instantiates a new Item.
     */
    public Item() {
        super("null", null);
        super.interactPlugin = new InteractPlugin(this);
        this.idHash = -1 << 16 | 1000;
    }

    /**
     * Instantiates a new Item.
     *
     * @param id the id
     */
    public Item(int id) {
        this(id, 1, 1000);
    }

    /**
     * Instantiates a new Item.
     *
     * @param id     the id
     * @param amount the amount
     */
    public Item(int id, int amount) {
        this(id, amount, 1000);
    }

    /**
     * Instantiates a new Item.
     *
     * @param id     the id
     * @param amount the amount
     * @param charge the charge
     */
    public Item(int id, int amount, int charge) {
        super(ItemDefinition.forId(id).getName(), null);
        super.destinationFlag = DestinationFlag.ITEM;
        super.index = -1; // Item slot.
        super.interactPlugin = new InteractPlugin(this);
        this.idHash = id << 16 | charge;
        this.amount = amount;
        this.definition = ItemDefinition.forId(id);
    }

    /**
     * Gets drop item.
     *
     * @return the drop item
     */
    public Item getDropItem() {
        int itemId = DegradableEquipment.getDropReplacement(getId());
        if (itemId != getId()) {
            return new Item(itemId, getAmount());
        }
        return this;
    }

    /**
     * Gets operate handler.
     *
     * @return the operate handler
     */
    public OptionHandler getOperateHandler() {
        return ItemDefinition.getOptionHandler(getId(), "operate");
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public long getValue() {
        long value = 1;
        if (definition.getValue() > value) {
            value = definition.getValue();
        }
        if (definition.getAlchemyValue(true) > value) {
            value = definition.getAlchemyValue(true);
        }
        return value * getAmount();
    }

    /**
     * Gets alchemy value.
     *
     * @return the alchemy value
     */
    public long getAlchemyValue() {
        long value = 1;
        if (definition.getAlchemyValue(true) > value) {
            value = definition.getAlchemyValue(true);
        }
        return value * getAmount();
    }

    /**
     * Copy item.
     *
     * @return the item
     */
    public Item copy() {
        return new Item(getId(), getAmount(), getCharge());
    }

    /**
     * Gets note change.
     *
     * @return the note change
     */
    public int getNoteChange() {
        int noteId = definition.getNoteId();
        if (noteId > -1) {
            return noteId;
        }
        return getId();
    }

    public int getId() {
        return idHash >> 16 & 0xFFFF;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.idHash = id << 16 | (idHash & 0xFFFF);
        this.definition = ItemDefinition.forId(id);
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
     * Sets amount.
     *
     * @param amount the amount
     */
    public void setAmount(int amount) {
        if (amount < 0) {
            amount = 0;
        }
        this.amount = amount;
    }

    /**
     * Is charged boolean.
     *
     * @return the boolean
     */
    public boolean isCharged() {
        return getCharge() > 0;
    }

    /**
     * Gets charge.
     *
     * @return the charge
     */
    public int getCharge() {
        return idHash & 0xFFFF;
    }

    /**
     * Sets charge.
     *
     * @param charge the charge
     */
    public void setCharge(int charge) {
        this.idHash = (idHash >> 16 & 0xFFFF) << 16 | charge;
    }

    public int getIdHash() {
        return idHash;
    }

    /**
     * Sets id hash.
     *
     * @param hash the hash
     */
    public void setIdHash(int hash) {
        this.idHash = hash;
    }

    /**
     * Has item plugin boolean.
     *
     * @return the boolean
     */
    public boolean hasItemPlugin() {
        return getPlugin() != null;
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public ItemPlugin getPlugin() {
        if (definition == null) {
            return null;
        }
        return definition.getItemPlugin();
    }

    /**
     * Gets definition.
     *
     * @return the definition
     */
    public ItemDefinition getDefinition() {
        return definition;
    }

    /**
     * Sets definition.
     *
     * @param definition the definition
     */
    public void setDefinition(ItemDefinition definition) {
        this.definition = definition;
    }

    /**
     * Gets slot.
     *
     * @return the slot
     */
    public int getSlot() {
        return index;
    }

    @Override
    public String toString() {
        return "Item id=" + getId() + ", name=" + getName() + ", amount=" + amount;
    }
}
