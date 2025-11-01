package content.global.skill.herblore.potions

import shared.consts.Items

/**
 * Represents finished potions in the Herblore skill.
 */
enum class FinishedPotion(
    val unfinished: UnfinishedPotion,
    val ingredient: Int,
    val level: Int,
    val experience: Double,
    val potion: Int
) {
    ATTACK_POTION(UnfinishedPotion.GUAM, Items.EYE_OF_NEWT_221, 3, 25.0, Items.ATTACK_POTION3_121),
    ANTIDOTE_POTION(UnfinishedPotion.MARRENTILL, Items.UNICORN_HORN_DUST_235, 5, 37.5, Items.ANTIPOISON3_175),
    RELIC_BALM(UnfinishedPotion.ROGUE_PURSE, Items.CLEAN_SNAKE_WEED_1526, 8, 0.0, Items.RELICYMS_BALM3_4844),
    STRENGTH_POTION(UnfinishedPotion.TARROMIN, Items.LIMPWURT_ROOT_225, 12, 50.0, Items.STRENGTH_POTION3_115),
    OGRE_POTION(UnfinishedPotion.GUAM, Items.JANGERBERRIES_247, 14, 10.0, Items.VIAL_2390),
    RESTORE_POTION(UnfinishedPotion.HARRALANDER, Items.RED_SPIDERS_EGGS_223, 22, 62.5, Items.RESTORE_POTION3_127),
    ENERGY_POTION(UnfinishedPotion.HARRALANDER, Items.CHOCOLATE_DUST_1975, 26, 67.5, Items.ENERGY_POTION3_3010),
    DEFENCE_POTION(UnfinishedPotion.RANARR, Items.WHITE_BERRIES_239, 30, 45.0, Items.DEFENCE_POTION3_133),
    AGILITY_POTION(UnfinishedPotion.TOADFLAX, Items.TOADS_LEGS_2152, 34, 80.0, Items.AGILITY_POTION3_3034),
    COMBAT_POTION(UnfinishedPotion.HARRALANDER, Items.GOAT_HORN_DUST_9736, 36, 84.0, Items.COMBAT_POTION3_9741),
    PRAYER_POTION(UnfinishedPotion.RANARR, Items.SNAPE_GRASS_231, 38, 87.5, Items.PRAYER_POTION3_139),
    SUMMONING_POTION(UnfinishedPotion.SPIRIT_WEED, Items.COCKATRICE_EGG_12109, 40, 92.0, Items.SUMMONING_POTION3_12142),
    SUPER_ATTACK(UnfinishedPotion.IRIT, Items.EYE_OF_NEWT_221, 45, 100.0, Items.SUPER_ATTACK3_145),
    SUPER_ANTIDOTE(UnfinishedPotion.IRIT, Items.UNICORN_HORN_DUST_235, 48, 106.3, Items.SUPER_ANTIPOISON3_181),
    FISHING_POTION(UnfinishedPotion.AVANTOE, Items.SNAPE_GRASS_231, 50, 112.5, Items.FISHING_POTION3_151),
    SUPER_ENERGY(UnfinishedPotion.AVANTOE, Items.MORT_MYRE_FUNGUS_2970, 52, 117.5, Items.SUPER_ENERGY3_3018),
    HUNTER_POTION(UnfinishedPotion.AVANTOE, Items.KEBBIT_TEETH_DUST_10111, 53, 120.0, Items.HUNTER_POTION3_10000),
    SUPER_STRENGTH(UnfinishedPotion.KWUARM, Items.LIMPWURT_ROOT_225, 55, 125.0, Items.SUPER_STRENGTH3_157),
    WEAPON_POISON(UnfinishedPotion.KWUARM, Items.DRAGON_SCALE_DUST_241, 60, 137.5, Items.WEAPON_POISON_187),
    SUPER_RESTORE(UnfinishedPotion.SNAPDRAGON, Items.RED_SPIDERS_EGGS_223, 63, 142.5, Items.SUPER_RESTORE3_3026),
    SUPER_DEFENCE(UnfinishedPotion.CADANTINE, Items.WHITE_BERRIES_239, 66, 160.0, Items.SUPER_DEFENCE3_163),
    ANTIFIRE_POTION(UnfinishedPotion.LANTADYME, Items.DRAGON_SCALE_DUST_241, 69, 157.5, Items.ANTIFIRE_POTION3_2454),
    SUPER_RANGING_POTION(UnfinishedPotion.DWARF_WEED, Items.WINE_OF_ZAMORAK_245, 72, 162.5, Items.RANGING_POTION3_169),
    SUPER_MAGIC(UnfinishedPotion.LANTADYME, Items.POTATO_CACTUS_3138, 76, 172.5, Items.MAGIC_POTION3_3042),
    ZAMORAK_BREW(UnfinishedPotion.TORSTOL, Items.JANGERBERRIES_247, 78, 175.0, Items.ZAMORAK_BREW3_189),
    SARADOMIN_BREW(UnfinishedPotion.TOADFLAX, Items.CRUSHED_NEST_6693, 81, 180.0, Items.SARADOMIN_BREW3_6687),
    STRONG_WEAPON_POISON(UnfinishedPotion.STRONG_WEAPON_POISON, Items.RED_SPIDERS_EGGS_223, 73, 165.0, Items.WEAPON_POISON_PLUS_5937),
    SUPER_STRONG_WEAPON_POISON(UnfinishedPotion.SUPER_STRONG_WEAPON_POISON, Items.POISON_IVY_BERRIES_6018, 82, 190.0, Items.WEAPON_POISON_PLUS_PLUS_5940),
    STRONG_ANTIDOTE(UnfinishedPotion.STRONG_ANTIPOISON, Items.YEW_ROOTS_6049, 68, 155.0, Items.ANTIPOISON_PLUS3_5945),
    SUPER_STRONG_ANTIDOTE(UnfinishedPotion.SUPER_STRONG_ANTIPOISON, Items.MAGIC_ROOTS_6051, 79, 177.5, Items.ANTIPOISON_PLUS_PLUS3_5954),
    BLAMISH_OIL(UnfinishedPotion.HARRALANDER, Items.BLAMISH_SNAIL_SLIME_1581, 25, 80.0, Items.BLAMISH_OIL_1582),
    ;

    companion object {
        /**
         * Returns the finished potion for the given unfinished potion and ingredient items, or null if none found.
         */
        fun getPotion(unfinished: Int, ingredient: Int): FinishedPotion? {
            return values().firstOrNull {
                it.unfinished.potion == unfinished && it.ingredient == ingredient
            }
        }
    }
}