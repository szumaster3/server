package core.game.container.impl;

import core.api.ContainerListener;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.interaction.InteractionListeners;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.config.ItemConfigParser;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;
import core.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static core.api.ContentAPIKt.setVarp;

/**
 * The type Equipment container.
 */
public final class EquipmentContainer extends Container {

    /**
     * The constant SLOT_HAT.
     */
    public static final int SLOT_HAT = 0, /**
     * The Slot cape.
     */
    SLOT_CAPE = 1, /**
     * The Slot amulet.
     */
    SLOT_AMULET = 2, /**
     * The Slot weapon.
     */
    SLOT_WEAPON = 3, /**
     * The Slot chest.
     */
    SLOT_CHEST = 4, /**
     * The Slot shield.
     */
    SLOT_SHIELD = 5, /**
     * The Slot legs.
     */
    SLOT_LEGS = 7, /**
     * The Slot hands.
     */
    SLOT_HANDS = 9, /**
     * The Slot feet.
     */
    SLOT_FEET = 10, /**
     * The Slot ring.
     */
    SLOT_RING = 12, /**
     * The Slot arrows.
     */
    SLOT_ARROWS = 13;

    private static final String[] BONUS_NAMES = {"Stab: ", "Slash: ", "Crush: ", "Magic: ", "Ranged: ", "Stab: ", "Slash: ", "Crush: ", "Magic: ", "Ranged: ", "Summoning: ", "Strength: ", "Prayer: "};

    private final Player player;

    /**
     * Instantiates a new Equipment container.
     *
     * @param player the player
     */
    public EquipmentContainer(Player player) {
        super(14);
        this.player = player;
        register(new EquipmentListener(player));
    }

    @Override
    public boolean add(Item item, boolean fire) {
        return add(item, fire, true);
    }

    /**
     * Add boolean.
     *
     * @param item          the item
     * @param fire          the fire
     * @param fromInventory the from inventory
     * @return the boolean
     */
    public boolean add(Item item, boolean fire, boolean fromInventory) {
        return add(item, player.getInventory().getSlot(item), fire, fromInventory);
    }

    /**
     * Add boolean.
     *
     * @param newItem       the new item
     * @param inventorySlot the inventory slot
     * @param fire          the fire
     * @param fromInventory the from inventory
     * @return the boolean
     */
    public boolean add(Item newItem, int inventorySlot, boolean fire, boolean fromInventory) {
        int equipmentSlot = newItem.getDefinition().getConfiguration(ItemConfigParser.EQUIP_SLOT, -1);
        if (!isEquippable(newItem, equipmentSlot)) return false;

        ArrayList<Item> itemsToRemove = new ArrayList<>();

        Item currentItem = super.get(equipmentSlot);
        if (currentItem != null) itemsToRemove.add(currentItem);

        Item secondaryEquip = getSecondaryEquipIfApplicable(newItem, equipmentSlot);
        if (secondaryEquip != null) itemsToRemove.add(secondaryEquip);

        if (fromInventory && !player.getInventory().remove(newItem, inventorySlot, true)) {
            return false;
        }

        if (!itemsToRemove.isEmpty()) {
            ArrayList<Item> invalidatedEntries = new ArrayList<>();
            for (Item current : itemsToRemove) {
                if (current.getId() == newItem.getId() && current.getDefinition().isStackable()) {
                    addStackableItemToExistingStack(newItem, fromInventory, equipmentSlot, current);
                    invalidatedEntries.add(current);
                }
            }
            itemsToRemove.removeAll(invalidatedEntries);

            if (itemsToRemove.isEmpty()) {
                return true;
            }

            boolean successfullyRemovedAll = tryUnequipCurrent(itemsToRemove, newItem, inventorySlot);

            if (!successfullyRemovedAll) {
                if (fromInventory)
                    player.getInventory().add(newItem);
                return false;
            }
        }

        super.replace(newItem, equipmentSlot, fire);
        setWeaponInterfaceWeaponName(newItem);
        return true;
    }

    private boolean isEquippable(Item newItem, int slot) {
        if (slot < 0) {
            return false;
        }
        return newItem.getDefinition().hasRequirement(player, true, true);
    }

    private void setWeaponInterfaceWeaponName(Item newItem) {
        if (newItem.getSlot() == SLOT_WEAPON) {
            player.getPacketDispatch().sendString(newItem.getName(), 92, 0);
        }
    }

    private boolean tryUnequipCurrent(ArrayList<Item> current, Item newItem, int preferredSlot) {
        if (current.isEmpty()) return true;
        int freeSlots = player.getInventory().freeSlots();
        int neededSlots = getNeededSlotsToUnequip(current);

        boolean hasSpaceForUnequippedItems = freeSlots >= neededSlots;

        if (!hasSpaceForUnequippedItems) {
            player.getPacketDispatch().sendMessage("Not enough space in your inventory.");
            return false;
        }

        boolean listenersSayWeCanUnequip = runUnequipHooks(current, newItem);

        boolean allRemoved = true;
        for (Item item : current) {
            if (!remove(item)) {
                allRemoved = false;
                break;
            }
        }

        boolean allAdded = allRemoved;
        if (allRemoved) {
            for (Item item : current) {
                if (!player.getInventory().add(item, true, preferredSlot)) {
                    allAdded = false;
                    break;
                }
            }
        }

        if (listenersSayWeCanUnequip && allRemoved && allAdded) return true;
        else {
            for (Item item : current) {
                if (!containsItem(item)) {
                    add(item);
                }
            }
        }

        return false;
    }

    private int getNeededSlotsToUnequip(ArrayList<Item> current) {
        int neededSlots = 0;

        for (Item item : current) {
            if (!item.getDefinition().isStackable()) {
                neededSlots++;
            } else {
                if (player.getInventory().getAmount(item.getId()) == 0) {
                    neededSlots++;
                }
            }
        }
        return neededSlots;
    }

    @Nullable
    private Item getSecondaryEquipIfApplicable(Item newItem, int equipmentSlot) {
        Item secondaryEquipItem = null;
        if (newItem.getDefinition().getConfiguration(ItemConfigParser.TWO_HANDED, false)) {
            secondaryEquipItem = get(SLOT_SHIELD);
        } else if (equipmentSlot == SLOT_SHIELD) {
            Item inSlot = player.getEquipment().get(SLOT_WEAPON);
            if (inSlot != null && inSlot.getDefinition().getConfiguration(ItemConfigParser.TWO_HANDED, false))
                secondaryEquipItem = get(SLOT_WEAPON);
        }
        return secondaryEquipItem;
    }

    private boolean runUnequipHooks(ArrayList<Item> currentItems, Item newItem) {
        boolean canContinue = true;

        for (Item currentItem : currentItems) {
            Plugin<Object> plugin = currentItem.getDefinition().getConfiguration("equipment", null);
            if (plugin != null) {
                Object object = plugin.fireEvent("unequip", player, currentItem, newItem);
                if (object != null && !((Boolean) object)) {
                    canContinue = false;
                    break;
                }
            }

            canContinue = InteractionListeners.run(currentItem.getId(), player, currentItem, false);

            if (!canContinue) break;
        }

        return canContinue;
    }

    private void addStackableItemToExistingStack(Item item, boolean fromInventory, int slot, Item current) {
        int amount = getMaximumAdd(item);
        if (item.getAmount() > amount) {
            amount += current.getAmount();
        } else {
            amount = current.getAmount() + item.getAmount();
        }
        Item transferItem = new Item(current.getId(), amount);
        if (fromInventory) {
            player.getInventory().remove(transferItem);
        }
        replace(transferItem, slot);
    }

    private static class EquipmentListener implements ContainerListener {

        private final Player player;

        /**
         * Instantiates a new Equipment listener.
         *
         * @param player the player
         */
        public EquipmentListener(Player player) {
            this.player = player;
        }

        @Override
        public void update(Container c, ContainerEvent event) {
            int[] slots = event.getSlots();
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 387, 28, 94, event.getItems(), false, slots));
            update(c);
            boolean updateDefenceAnimation = false;
            for (int slot : slots) {
                if (slot == EquipmentContainer.SLOT_WEAPON) {
                    player.getProperties().setAttackSpeed(c.getNew(slot).getDefinition().getConfiguration(ItemConfigParser.ATTACK_SPEED, 4));
                    WeaponInterface inter = player.getExtension(WeaponInterface.class);
                    if (inter == null) {
                        break;
                    }
                    inter.updateInterface();
                    updateDefenceAnimation = true;
                } else if (slot == EquipmentContainer.SLOT_SHIELD) {
                    updateDefenceAnimation = true;
                }
            }
            if (updateDefenceAnimation) {
                player.getProperties().updateDefenceAnimation();
            }
        }

        @Override
        public void refresh(Container c) {
            player.getProperties().setAttackSpeed(c.getNew(3).getDefinition().getConfiguration(ItemConfigParser.ATTACK_SPEED, 4));
            WeaponInterface inter = player.getExtension(WeaponInterface.class);
            if (inter != null) {
                inter.updateInterface();
            }
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 387, 28, 94, c.toArray(), 14, false));
            update(c);
            player.getProperties().updateDefenceAnimation();
        }

        /**
         * Update.
         *
         * @param c the c
         */
        public void update(Container c) {
            if (c.getNew(SLOT_SHIELD).getId() != 11283 && player.getAttribute("dfs_spec", false)) {
                player.removeAttribute("dfs_spec");
                player.getProperties().getCombatPulse().setHandler(null);
                if (!player.getSettings().isSpecialToggled()) {
                    setVarp(player, 301, 0);
                }
            }
            player.getAppearance().setAnimations();
            player.updateAppearance();
            player.getSettings().updateWeight();
            updateBonuses(player);
        }
    }

    /**
     * Update bonuses.
     *
     * @param player the player
     */
    public static void updateBonuses(Player player) {
        int[] bonuses = new int[15];
        for (Item item : player.getEquipment().toArray()) {
            if (item != null) {
                int[] bonus = item.getDefinition().getConfiguration(ItemConfigParser.BONUS, new int[15]);
                for (int i = 0; i < bonus.length; i++) {
                    if (i == 14 && bonuses[i] != 0) {
                        continue;
                    }
                    bonuses[i] += bonus[i];
                }
            }
        }
        Item shield = player.getEquipment().get(SLOT_SHIELD);
        if (shield != null && shield.getId() == 11283) {
            int increase = shield.getCharge() / 20;
            bonuses[5] += increase;
            bonuses[6] += increase;
            bonuses[7] += increase;
            bonuses[9] += increase;
            bonuses[10] += increase;
        }
        player.getProperties().setBonuses(bonuses);
        update(player);
    }

    /**
     * Update.
     *
     * @param player the player
     */
    public static void update(Player player) {
        if (!player.getInterfaceManager().hasMainComponent(667)) {
            return;
        }
        int index = 0;
        int[] bonuses = player.getProperties().getBonuses();
        for (int i = 36; i < 50; i++) {
            if (i == 47) {
                continue;
            }
            int bonus = bonuses[index];
            String bonusValue = bonus > -1 ? ("+" + bonus) : Integer.toString(bonus);
            player.getPacketDispatch().sendString(BONUS_NAMES[index++] + bonusValue, 667, i);
        }
        player.getPacketDispatch().sendString("Attack bonus", 667, 34);
    }
}
