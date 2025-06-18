package content.global.skill.summoning.item

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the enchanted headgear items for the Summoning skill.
 */
enum class EnchantedHeadgear(val defaultItem: Item, val enchantedItem: Item, val chargedItem: Item, val scrollCapacity: Int, val requiredLevel: Int) {
    ANTLERS(Item(Items.ANTLERS_12204), Item(Items.ANTLERS_12204), Item(Items.ANTLERS_CHARGED_12206), 40, 10),
    ADAMANT_FULL_HELM(Item(Items.ADAMANT_FULL_HELM_1161), Item(Items.ADAMANT_FULL_HELM_E_12658), Item(Items.ADAMANT_FULL_HELM_CHARGED_12659), 50, 20),
    SLAYER_HELMET(Item(Items.SLAYER_HELMET_13263), Item(Items.SLAYER_HELMET_E_14636), Item(Items.SLAYER_HELMET_CHARGED_14637), 50, 20),
    SNAKESKIN_BANDANA(Item(Items.SNAKESKIN_BANDANA_6326), Item(Items.SNAKESKIN_BANDANA_E_12660), Item(Items.SNAKESKIN_BANDANA_CHARGED_12661), 50, 20),
    LIZARD_SKULL(Item(Items.LIZARD_SKULL_12207), Item(Items.LIZARD_SKULL_12207), Item(Items.LIZARD_SKULL_CHARGED_12209), 65, 30),
    SPLITBARK_HELM(Item(Items.SPLITBARK_HELM_3385), Item(Items.SPLITBARK_HELM_E_12662), Item(Items.SPLITBARK_HELM_CHARGED_12663), 50, 30),
    RUNE_FULL_HELM(Item(Items.RUNE_FULL_HELM_1163), Item(Items.RUNE_FULL_HELM_E_12664), Item(Items.RUNE_FULL_HELM_CHARGED_12665), 60, 30),
    WARRIOR_HELM(Item(Items.WARRIOR_HELM_3753), Item(Items.WARRIOR_HELM_E_12676), Item(Items.WARRIOR_HELM_CHARGED_12677), 70, 35),
    BERSERKER_HELM(Item(Items.BERSERKER_HELM_3751), Item(Items.BERSERKER_HELM_E_12674), Item(Items.BERSERKER_HELM_CHARGED_12675), 70, 35),
    ARCHER_HELM(Item(Items.ARCHER_HELM_3749), Item(Items.ARCHER_HELM_E_12672), Item(Items.ARCHER_HELM_CHARGED_12673), 70, 35),
    FARSEER_HELM(Item(Items.FARSEER_HELM_3755), Item(Items.FARSEER_HELM_E_12678), Item(Items.FARSEER_HELM_CHARGED_12679), 70, 35),
    HELM_OF_NEITIZNOT(Item(Items.HELM_OF_NEITIZNOT_10828), Item(Items.HELM_OF_NEITIZNOT_E_12680), Item(Items.HELM_OF_NEITIZNOT_CHARGED_12681), 90, 45),
    FEATHER_HEADDRESS_0(Item(Items.FEATHER_HEADDRESS_12210), Item(Items.FEATHER_HEADDRESS_12210), Item(Items.FEATHER_HEADDRESS_CHARGED_12212), 150, 50),
    FEATHER_HEADDRESS_1(Item(Items.FEATHER_HEADDRESS_12222), Item(Items.FEATHER_HEADDRESS_12222), Item(Items.FEATHER_HEADDRESS_CHARGED_12224), 150, 50),
    FEATHER_HEADDRESS_2(Item(Items.FEATHER_HEADDRESS_12216), Item(Items.FEATHER_HEADDRESS_12216), Item(Items.FEATHER_HEADDRESS_CHARGED_12218), 150, 50),
    FEATHER_HEADDRESS_3(Item(Items.FEATHER_HEADDRESS_12219), Item(Items.FEATHER_HEADDRESS_12219), Item(Items.FEATHER_HEADDRESS_CHARGED_12221), 150, 50),
    FEATHER_HEADDRESS_4(Item(Items.FEATHER_HEADDRESS_12213), Item(Items.FEATHER_HEADDRESS_12213), Item(Items.FEATHER_HEADDRESS_CHARGED_12215), 150, 50),
    DRAGON_HELM(Item(Items.DRAGON_MED_HELM_1149), Item(Items.DRAGON_MED_HELM_E_12666), Item(Items.DRAGON_MED_HELM_CHARGED_12667), 110, 50),
    LUNAR_HELM(Item(Items.LUNAR_HELM_9096), Item(Items.LUNAR_HELM_E_12668), Item(Items.LUNAR_HELM_CHARGED_12669), 110, 55),
    ARMADYL_HELM(Item(Items.ARMADYL_HELMET_11718), Item(Items.ARMADYL_HELMET_E_12670), Item(Items.ARMADYL_HELMET_CHARGED_12671), 120, 60);

    companion object {
        /**
         * All item ids to headgear and type.
         */
        val itemMap = hashMapOf<Int, Pair<EnchantedHeadgear, HeadgearType>>()

        init {
            values().forEach { headgear ->
                itemMap[headgear.defaultItem.id] = headgear to HeadgearType.DEFAULT
                itemMap[headgear.enchantedItem.id] = headgear to HeadgearType.ENCHANTED
                itemMap[headgear.chargedItem.id] = headgear to HeadgearType.CHARGED
            }
        }

        /**
         * Gets the [EnchantedHeadgear] and its type for the given item.
         */
        fun forItem(item: Item): Pair<EnchantedHeadgear, HeadgearType>? = itemMap[item.id]
    }

    enum class HeadgearType {
        DEFAULT, ENCHANTED, CHARGED
    }
}