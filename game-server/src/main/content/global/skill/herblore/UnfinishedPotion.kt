package content.global.skill.herblore

import org.rs.consts.Items

/**
 * Enum representing unfinished potions used in Herblore.
 */
enum class UnfinishedPotion(val base: Int, val ingredient: Int, val level: Int, val potion: Int) {
    GUAM(HerblorePulse.VIAL_OF_WATER, HerbItem.GUAM.product.id, 3, Items.GUAM_POTIONUNF_91),
    ROGUE_PURSE(HerblorePulse.VIAL_OF_WATER, HerbItem.ROGUES_PUSE.product.id, 3, Items.ROGUES_PURSE_POTIONUNF_4840),
    MARRENTILL(HerblorePulse.VIAL_OF_WATER, HerbItem.MARRENTILL.product.id, 5, Items.MARRENTILL_POTIONUNF_93), TARROMIN(HerblorePulse.VIAL_OF_WATER, HerbItem.TARROMIN.product.id, 12, Items.TARROMIN_POTIONUNF_95),
    OGRE(HerblorePulse.VIAL_OF_WATER, Items.JANGERBERRIES_247, 14, Items.VIAL_2389),
    HARRALANDER(HerblorePulse.VIAL_OF_WATER, HerbItem.HARRALANDER.product.id, 22, Items.HARRALANDER_POTIONUNF_97), RANARR(HerblorePulse.VIAL_OF_WATER, HerbItem.RANARR.product.id, 30, Items.RANARR_POTIONUNF_99),
    TOADFLAX(HerblorePulse.VIAL_OF_WATER, HerbItem.TOADFLAX.product.id, 34, Items.TOADFLAX_POTIONUNF_3002), SPIRIT_WEED(HerblorePulse.VIAL_OF_WATER, HerbItem.SPIRIT_WEED.product.id, 40, Items.SPIRIT_WEED_POTIONUNF_12181),
    IRIT(HerblorePulse.VIAL_OF_WATER, HerbItem.IRIT.product.id, 45, Items.IRIT_POTIONUNF_101),
    AVANTOE(HerblorePulse.VIAL_OF_WATER, HerbItem.AVANTOE.product.id, 50, Items.AVANTOE_POTIONUNF_103), KWUARM(HerblorePulse.VIAL_OF_WATER, HerbItem.KWUARM.product.id, 55, Items.KWUARM_POTIONUNF_105),
    SNAPDRAGON(HerblorePulse.VIAL_OF_WATER, HerbItem.SNAPDRAGON.product.id, 63, Items.SNAPDRAGON_POTIONUNF_3004),
    CADANTINE(HerblorePulse.VIAL_OF_WATER, HerbItem.CADANTINE.product.id, 66, Items.CADANTINE_POTIONUNF_107), LANTADYME(HerblorePulse.VIAL_OF_WATER, HerbItem.LANTADYME.product.id, 69, Items.LANTADYME_POTIONUNF_2483),
    DWARF_WEED(HerblorePulse.VIAL_OF_WATER, HerbItem.DWARF_WEED.product.id, 72, Items.DWARF_WEED_POTIONUNF_109), TORSTOL(HerblorePulse.VIAL_OF_WATER, HerbItem.TORSTOL.product.id, 75, Items.TORSTOL_POTIONUNF_111),
    STRONG_WEAPON_POISON(HerblorePulse.COCONUT_MILK, Items.CACTUS_SPINE_6016, 73, Items.WEAPON_POISON_PLUSUNF_5936),
    SUPER_STRONG_WEAPON_POISON(HerblorePulse.COCONUT_MILK, Items.CAVE_NIGHTSHADE_2398, 82, Items.WEAPON_POISON_PLUS_PLUSUNF_5939),
    STRONG_ANTIPOISON(HerblorePulse.COCONUT_MILK, HerbItem.TOADFLAX.product.id, 68, Items.ANTIPOISON_PLUSUNF_5942),
    SUPER_STRONG_ANTIPOISON(HerblorePulse.COCONUT_MILK, HerbItem.IRIT.product.id, 79, Items.ANTIPOISON_PLUS_PLUSUNF_5951), ;

    companion object {
        /**
         * Finds the unfinished potion matching the given item and base.
         *
         * @param item The id of the ingredient or base.
         * @param base The id of the base ingredient (vial of water or coconut milk).
         * @return The matching [UnfinishedPotion] or null if no match is found.
         */
        fun forID(item: Int, base: Int): UnfinishedPotion? = values().firstOrNull { potion ->
            (potion.ingredient == item || potion.ingredient == base) && (item == potion.base || base == potion.base)
        }
    }
}
