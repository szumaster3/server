package content.global.skill.crafting.casting.gold;

import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import org.rs.consts.Components;
import org.rs.consts.Items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static core.api.ContentAPIKt.*;
import static core.api.ui.InterfaceAPIKt.sendInterfaceConfig;

/**
 * The type Jewellery.
 */
public class Jewellery {

    /**
     * The constant RING_MOULD.
     */
    public static final int RING_MOULD = Items.RING_MOULD_1592;
    /**
     * The constant AMULET_MOULD.
     */
    public static final int AMULET_MOULD = Items.AMULET_MOULD_1595;
    /**
     * The constant NECKLACE_MOULD.
     */
    public static final int NECKLACE_MOULD = Items.NECKLACE_MOULD_1597;
    /**
     * The constant BRACELET_MOULD.
     */
    public static final int BRACELET_MOULD = Items.BRACELET_MOULD_11065;
    /**
     * The constant GOLD_BAR.
     */
    public static final int GOLD_BAR = Items.GOLD_BAR_2357;
    /**
     * The constant PERFECT_GOLD_BAR.
     */
    public static final int PERFECT_GOLD_BAR = Items.PERFECT_GOLD_BAR_2365;
    /**
     * The constant SAPPHIRE.
     */
    public static final int SAPPHIRE = Items.SAPPHIRE_1607;
    /**
     * The constant EMERALD.
     */
    public static final int EMERALD = Items.EMERALD_1605;
    /**
     * The constant RUBY.
     */
    public static final int RUBY = Items.RUBY_1603;
    /**
     * The constant DIAMOND.
     */
    public static final int DIAMOND = Items.DIAMOND_1601;
    /**
     * The constant DRAGONSTONE.
     */
    public static final int DRAGONSTONE = Items.DRAGONSTONE_1615;
    /**
     * The constant ONYX.
     */
    public static final int ONYX = Items.ONYX_6573;

    private static final Map<Integer, int[]> mouldComponentMap = new HashMap<>();

    static {
        mouldComponentMap.put(RING_MOULD, new int[]{20, 22, 24, 26, 28, 30, 32, 35});
        mouldComponentMap.put(NECKLACE_MOULD, new int[]{42, 44, 46, 48, 50, 52, 54});
        mouldComponentMap.put(AMULET_MOULD, new int[]{61, 63, 65, 67, 69, 71, 73});
        mouldComponentMap.put(BRACELET_MOULD, new int[]{80, 82, 84, 86, 88, 90, 92});
    }

    /**
     * Open.
     *
     * @param player the player
     */
    public static void open(Player player) {
        openInterface(player, Components.CRAFTING_GOLD_446);

        mouldComponentMap.forEach((mould, components) -> {
            boolean hasMould = inInventory(player, mould, 1);
            for (int component : components) {
                sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, component, !hasMould);
            }
        });

        IntStream.of(RING_MOULD, NECKLACE_MOULD, AMULET_MOULD, BRACELET_MOULD).forEach(mould -> {
            if (inInventory(player, mould, 1)) {
                sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, getComponentIdForMould(mould), true);
            }
        });

        for (JewelleryItem data : JewelleryItem.values()) {
            int length = 0;
            for (int item : data.getItems()) {
                if (inInventory(player, item, 1)) {
                    length++;
                }
            }

            if (!inInventory(player, mouldFor(data.name()), 1)) {
                length--;
            }

            if (length == data.getItems().length && getStatLevel(player, Skills.CRAFTING) >= data.getLevel()) {
                player.getPacketDispatch().sendItemZoomOnInterface(
                    data.getSendItem(), 230, Components.CRAFTING_GOLD_446, data.getComponentId()
                );
            } else {
                handleItemZoomOnInterface(player, data, length);
            }
        }
    }

    /**
     * Make.
     *
     * @param player the player
     * @param data   the data
     * @param amount the amount
     */
    public static void make(Player player, JewelleryItem data, int amount) {
        int availableAmount = getAvailableAmount(player, data, amount);
        if (availableAmount <= 0) {
            sendMessage(player, "You don't have the required items to make this item.");
            return;
        }
        if (getStatLevel(player, Skills.CRAFTING) < data.getLevel()) {
            sendMessage(player, "You need a crafting level of " + data.getLevel() + " to craft this.");
            return;
        }

        List<Item> items = Arrays.stream(data.getItems())
            .mapToObj(itemId -> new Item(itemId, availableAmount))
            .collect(Collectors.toList());

        closeInterface(player);
        submitIndividualPulse(player, new JewelleryCraftingPulse(player, null, data, availableAmount));
    }

    private static int getAvailableAmount(Player player, JewelleryItem data, int amount) {
        int availableAmount = amount;

        if (Arrays.stream(data.getItems()).anyMatch(item -> item == GOLD_BAR)) {
            availableAmount = Math.min(availableAmount, amountInInventory(player, GOLD_BAR));
        } else if (Arrays.stream(data.getItems()).anyMatch(item -> item == PERFECT_GOLD_BAR)) {
            availableAmount = Math.min(availableAmount, amountInInventory(player, PERFECT_GOLD_BAR));
        } else {
            int first = amountInInventory(player, data.getItems()[0]);
            int second = amountInInventory(player, data.getItems()[1]);
            availableAmount = Math.min(availableAmount, Math.min(first, second));
        }

        return availableAmount;
    }

    private static void handleItemZoomOnInterface(Player player, JewelleryItem data, int length) {
        String name = ItemDefinition.forId(data.getSendItem()).getName().toLowerCase();
        if (length != data.getItems().length || !isCraftableItem(player, data, name)) {
            sendItemZoomOnInterface(
                player,
                Components.CRAFTING_GOLD_446,
                data.getComponentId(),
                getItemZoomIdByName(name),
                1
            );
        }
    }

    private static boolean isCraftableItem(Player player, JewelleryItem data, String name) {
        return (name.contains("amulet") && inInventory(player, AMULET_MOULD, 1)) ||
               (name.contains("ring") && inInventory(player, RING_MOULD, 1)) ||
               (name.contains("necklace") && inInventory(player, NECKLACE_MOULD, 1)) ||
               (name.contains("bracelet") && inInventory(player, BRACELET_MOULD, 1));
    }

    private static int getItemZoomIdByName(String name) {
        if (name.contains("ring")) return Items.RING_PICTURE_1647;
        if (name.contains("necklace")) return Items.NECKLACE_PICTURE_1666;
        if (name.contains("amulet")) return Items.AMULET_PICTURE_1685;
        if (name.contains("bracelet")) return Items.BRACELET_PICTURE_11067;
        return -1;
    }

    private static int mouldFor(String name) {
        if (name.contains("ring")) return RING_MOULD;
        if (name.contains("necklace")) return NECKLACE_MOULD;
        if (name.contains("amulet")) return AMULET_MOULD;
        if (name.contains("bracelet")) return BRACELET_MOULD;
        return -1;
    }

    private static int getComponentIdForMould(int mould) {
        switch (mould) {
            case RING_MOULD:
                return 14;
            case NECKLACE_MOULD:
                return 36;
            case AMULET_MOULD:
                return 55;
            case BRACELET_MOULD:
                return 74;
            default:
                return -1;
        }
    }

    /**
     * The enum Jewellery item.
     */
    public enum JewelleryItem {

        /**
         * The Gold ring.
         */
        GOLD_RING(5, 15, 19, Items.GOLD_RING_1635, new int[]{GOLD_BAR}),
        /**
         * The Sapphire ring.
         */
        SAPPHIRE_RING(20, 40, 21, Items.SAPPHIRE_RING_1637, new int[]{SAPPHIRE, GOLD_BAR}),
        /**
         * The Emerald ring.
         */
        EMERALD_RING(27, 55, 23, Items.EMERALD_RING_1639, new int[]{EMERALD, GOLD_BAR}),
        /**
         * The Ruby ring.
         */
        RUBY_RING(34, 70, 25, Items.RUBY_RING_1641, new int[]{RUBY, GOLD_BAR}),
        /**
         * The Perfect ring.
         */
        PERFECT_RING(40, 0, 25, Items.PERFECT_RING_773, new int[]{RUBY, PERFECT_GOLD_BAR}),
        /**
         * The Diamond ring.
         */
        DIAMOND_RING(43, 85, 27, Items.DIAMOND_RING_1643, new int[]{DIAMOND, GOLD_BAR}),
        /**
         * The Dragonstone ring.
         */
        DRAGONSTONE_RING(55, 100, 29, Items.DRAGONSTONE_RING_1645, new int[]{DRAGONSTONE, GOLD_BAR}),
        /**
         * The Onyx ring.
         */
        ONYX_RING(67, 115, 31, Items.ONYX_RING_6575, new int[]{ONYX, GOLD_BAR}),

        /**
         * The Gold necklace.
         */
        GOLD_NECKLACE(6, 20, 41, Items.GOLD_NECKLACE_1654, new int[]{GOLD_BAR}),
        /**
         * The Sapphire necklace.
         */
        SAPPHIRE_NECKLACE(22, 55, 43, Items.SAPPHIRE_NECKLACE_1656, new int[]{SAPPHIRE, GOLD_BAR}),
        /**
         * The Emerald necklace.
         */
        EMERALD_NECKLACE(29, 60, 45, Items.EMERALD_NECKLACE_1658, new int[]{EMERALD, GOLD_BAR}),
        /**
         * The Ruby necklace.
         */
        RUBY_NECKLACE(40, 75, 47, Items.RUBY_NECKLACE_1660, new int[]{RUBY, GOLD_BAR}),
        /**
         * The Perfect necklace.
         */
        PERFECT_NECKLACE(40, 0, 47, Items.PERFECT_NECKLACE_774, new int[]{RUBY, PERFECT_GOLD_BAR}),
        /**
         * The Diamond necklace.
         */
        DIAMOND_NECKLACE(56, 90, 49, Items.DIAMOND_NECKLACE_1662, new int[]{DIAMOND, GOLD_BAR}),
        /**
         * The Dragonstone necklace.
         */
        DRAGONSTONE_NECKLACE(72, 105, 51, Items.DRAGON_NECKLACE_1664, new int[]{DRAGONSTONE, GOLD_BAR}),
        /**
         * The Onyx necklace.
         */
        ONYX_NECKLACE(82, 120, 53, Items.ONYX_NECKLACE_6577, new int[]{ONYX, GOLD_BAR}),
        /**
         * The Slayer ring.
         */
        SLAYER_RING(75, 15, 34, Items.RING_OF_SLAYING8_13281, new int[]{Items.ENCHANTED_GEM_4155, GOLD_BAR}),

        /**
         * The Gold amulet.
         */
        GOLD_AMULET(8, 30, 60, Items.GOLD_AMULET_1673, new int[]{GOLD_BAR}),
        /**
         * The Sapphire amulet.
         */
        SAPPHIRE_AMULET(24, 63, 62, Items.SAPPHIRE_AMULET_1675, new int[]{SAPPHIRE, GOLD_BAR}),
        /**
         * The Emerald amulet.
         */
        EMERALD_AMULET(31, 70, 64, Items.EMERALD_AMULET_1677, new int[]{EMERALD, GOLD_BAR}),
        /**
         * The Ruby amulet.
         */
        RUBY_AMULET(50, 85, 66, Items.RUBY_AMULET_1679, new int[]{RUBY, GOLD_BAR}),
        /**
         * The Diamond amulet.
         */
        DIAMOND_AMULET(70, 100, 68, Items.DIAMOND_AMULET_1681, new int[]{DIAMOND, GOLD_BAR}),
        /**
         * The Dragonstone amulet.
         */
        DRAGONSTONE_AMULET(80, 150, 70, Items.DRAGONSTONE_AMMY_1683, new int[]{DRAGONSTONE, GOLD_BAR}),
        /**
         * The Onyx amulet.
         */
        ONYX_AMULET(90, 165, 72, Items.ONYX_AMULET_6579, new int[]{ONYX, GOLD_BAR}),

        /**
         * The Gold bracelet.
         */
        GOLD_BRACELET(7, 25, 79, Items.GOLD_BRACELET_11069, new int[]{GOLD_BAR}),
        /**
         * The Sapphire bracelet.
         */
        SAPPHIRE_BRACELET(23, 60, 81, Items.SAPPHIRE_BRACELET_11072, new int[]{SAPPHIRE, GOLD_BAR}),
        /**
         * The Emerald bracelet.
         */
        EMERALD_BRACELET(30, 65, 83, Items.EMERALD_BRACELET_11076, new int[]{EMERALD, GOLD_BAR}),
        /**
         * The Ruby bracelet.
         */
        RUBY_BRACELET(42, 80, 85, Items.RUBY_BRACELET_11085, new int[]{RUBY, GOLD_BAR}),
        /**
         * The Diamond bracelet.
         */
        DIAMOND_BRACELET(58, 95, 87, Items.DIAMOND_BRACELET_11092, new int[]{DIAMOND, GOLD_BAR}),
        /**
         * The Dragonstone bracelet.
         */
        DRAGONSTONE_BRACELET(74, 110, 89, Items.DRAGON_BRACELET_11115, new int[]{DRAGONSTONE, GOLD_BAR}),
        /**
         * The Onyx bracelet.
         */
        ONYX_BRACELET(84, 125, 91, Items.ONYX_BRACELET_11130, new int[]{ONYX, GOLD_BAR});

        private final int level;
        private final double experience;
        private final int componentId;
        private final int sendItem;
        private final int[] items;

        private static final Map<Integer, JewelleryItem> productMap = new HashMap<>();

        static {
            for (JewelleryItem item : values()) {
                productMap.put(item.getSendItem(), item);
            }
        }

        JewelleryItem(int level, double experience, int componentId, int sendItem, int[] items) {
            this.level = level;
            this.experience = experience;
            this.componentId = componentId;
            this.sendItem = sendItem;
            this.items = items;
        }

        /**
         * Gets level.
         *
         * @return the level
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets experience.
         *
         * @return the experience
         */
        public double getExperience() {
            return experience;
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
         * Gets send item.
         *
         * @return the send item
         */
        public int getSendItem() {
            return sendItem;
        }

        /**
         * Get items int [ ].
         *
         * @return the int [ ]
         */
        public int[] getItems() {
            return items;
        }

        /**
         * For product jewellery item.
         *
         * @param id the id
         * @return the jewellery item
         */
        public static JewelleryItem forProduct(int id) {
            return productMap.get(id);
        }
    }
}
