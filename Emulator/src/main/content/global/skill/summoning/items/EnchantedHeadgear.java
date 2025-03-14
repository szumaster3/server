package content.global.skill.summoning.items;

import core.game.node.item.Item;
import org.rs.consts.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum Enchanted headgear.
 */
public enum EnchantedHeadgear {
    /**
     * The Antlers.
     */
    ANTLERS(new Item(Items.ANTLERS_12204), new Item(Items.ANTLERS_12204), new Item(Items.ANTLERS_CHARGED_12206), 40, 10),
    /**
     * The Adamant full helm.
     */
    ADAMANT_FULL_HELM(new Item(Items.ADAMANT_FULL_HELM_1161), new Item(Items.ADAMANT_FULL_HELM_E_12658), new Item(Items.ADAMANT_FULL_HELM_CHARGED_12659), 50, 20),
    /**
     * The Slayer helmet.
     */
    SLAYER_HELMET(new Item(Items.SLAYER_HELMET_13263), new Item(Items.SLAYER_HELMET_E_14636), new Item(Items.SLAYER_HELMET_CHARGED_14637), 50, 20),
    /**
     * The Snakeskin bandana.
     */
    SNAKESKIN_BANDANA(new Item(Items.SNAKESKIN_BANDANA_6326), new Item(Items.SNAKESKIN_BANDANA_E_12660), new Item(Items.SNAKESKIN_BANDANA_CHARGED_12661), 50, 20),
    /**
     * The Lizard skull.
     */
    LIZARD_SKULL(new Item(Items.LIZARD_SKULL_12207), new Item(Items.LIZARD_SKULL_12207), new Item(Items.LIZARD_SKULL_CHARGED_12209), 65, 30),
    /**
     * The Splitbark helm.
     */
    SPLITBARK_HELM(new Item(Items.SPLITBARK_HELM_3385), new Item(Items.SPLITBARK_HELM_E_12662), new Item(Items.SPLITBARK_HELM_CHARGED_12663), 50, 30),
    /**
     * The Rune full helm.
     */
    RUNE_FULL_HELM(new Item(Items.RUNE_FULL_HELM_1163), new Item(Items.RUNE_FULL_HELM_E_12664), new Item(Items.RUNE_FULL_HELM_CHARGED_12665), 60, 30),
    /**
     * The Warrior helm.
     */
    WARRIOR_HELM(new Item(Items.WARRIOR_HELM_3753), new Item(Items.WARRIOR_HELM_E_12676), new Item(Items.WARRIOR_HELM_CHARGED_12677), 70, 35),
    /**
     * The Berserker helm.
     */
    BERSERKER_HELM(new Item(Items.BERSERKER_HELM_3751), new Item(Items.BERSERKER_HELM_E_12674), new Item(Items.BERSERKER_HELM_CHARGED_12675), 70, 35),
    /**
     * The Archer helm.
     */
    ARCHER_HELM(new Item(Items.ARCHER_HELM_3749), new Item(Items.ARCHER_HELM_E_12672), new Item(Items.ARCHER_HELM_CHARGED_12673), 70, 35),
    /**
     * The Farseer helm.
     */
    FARSEER_HELM(new Item(Items.FARSEER_HELM_3755), new Item(Items.FARSEER_HELM_E_12678), new Item(Items.FARSEER_HELM_CHARGED_12679), 70, 35),
    /**
     * The Helm of neitiznot.
     */
    HELM_OF_NEITIZNOT(new Item(Items.HELM_OF_NEITIZNOT_10828), new Item(Items.HELM_OF_NEITIZNOT_E_12680), new Item(Items.HELM_OF_NEITIZNOT_CHARGED_12681), 90, 45),
    /**
     * The Feather headdress 0.
     */
    FEATHER_HEADDRESS_0(new Item(Items.FEATHER_HEADDRESS_12210), new Item(Items.FEATHER_HEADDRESS_12210), new Item(Items.FEATHER_HEADDRESS_CHARGED_12212), 150, 50),
    /**
     * The Feather headdress 1.
     */
    FEATHER_HEADDRESS_1(new Item(Items.FEATHER_HEADDRESS_12222), new Item(Items.FEATHER_HEADDRESS_12222), new Item(Items.FEATHER_HEADDRESS_CHARGED_12224), 150, 50),
    /**
     * The Feather headdress 2.
     */
    FEATHER_HEADDRESS_2(new Item(Items.FEATHER_HEADDRESS_12216), new Item(Items.FEATHER_HEADDRESS_12216), new Item(Items.FEATHER_HEADDRESS_CHARGED_12218), 150, 50),
    /**
     * The Feather headdress 3.
     */
    FEATHER_HEADDRESS_3(new Item(Items.FEATHER_HEADDRESS_12219), new Item(Items.FEATHER_HEADDRESS_12219), new Item(Items.FEATHER_HEADDRESS_CHARGED_12221), 150, 50),
    /**
     * The Feather headdress 4.
     */
    FEATHER_HEADDRESS_4(new Item(Items.FEATHER_HEADDRESS_12213), new Item(Items.FEATHER_HEADDRESS_12213), new Item(Items.FEATHER_HEADDRESS_CHARGED_12215), 150, 50),
    /**
     * The Dragon helm.
     */
    DRAGON_HELM(new Item(Items.DRAGON_MED_HELM_1149), new Item(Items.DRAGON_MED_HELM_E_12666), new Item(Items.DRAGON_MED_HELM_CHARGED_12667), 110, 50),
    /**
     * The Lunar helm.
     */
    LUNAR_HELM(new Item(Items.LUNAR_HELM_9096), new Item(Items.LUNAR_HELM_E_12668), new Item(Items.LUNAR_HELM_CHARGED_12669), 110, 55),
    /**
     * The Armadyl helm.
     */
    ARMADYL_HELM(new Item(Items.ARMADYL_HELMET_11718), new Item(Items.ARMADYL_HELMET_E_12670), new Item(Items.ARMADYL_HELMET_CHARGED_12671), 120, 60);

    private final Item defaultItem;
    private final Item enchantedItem;
    private final Item chargedItem;
    private final int scrollCapacity;
    private final int requiredLevel;

    private static final Map<Integer, EnchantedHeadgear> product = new HashMap<>();

    static {
        for (EnchantedHeadgear headgear : values()) {
            product.put(headgear.getDefaultItem().getId(), headgear);
        }
    }

    EnchantedHeadgear(Item defaultItem, Item enchantedItem, Item chargedItem, int scrollCapacity, int requiredLevel) {
        this.defaultItem = defaultItem;
        this.enchantedItem = enchantedItem;
        this.chargedItem = chargedItem;
        this.scrollCapacity = scrollCapacity;
        this.requiredLevel = requiredLevel;
    }

    /**
     * Gets default item.
     *
     * @return the default item
     */
    public Item getDefaultItem() {
        return defaultItem;
    }

    /**
     * Gets enchanted item.
     *
     * @return the enchanted item
     */
    public Item getEnchantedItem() {
        return enchantedItem;
    }

    /**
     * Gets charged item.
     *
     * @return the charged item
     */
    public Item getChargedItem() {
        return chargedItem;
    }

    /**
     * Gets scroll capacity.
     *
     * @return the scroll capacity
     */
    public int getScrollCapacity() {
        return scrollCapacity;
    }

    /**
     * Gets required level.
     *
     * @return the required level
     */
    public int getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * For item enchanted headgear.
     *
     * @param item the item
     * @return the enchanted headgear
     */
    public static EnchantedHeadgear forItem(Item item) {
        return product.get(item.getId());
    }
}
