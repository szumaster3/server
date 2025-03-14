package content.global.skill.herblore.potions

import content.global.skill.herblore.GrindItem
import core.game.node.item.Item
import org.rs.consts.Items
import java.util.*

enum class FinishedPotion(
    val unfinished: UnfinishedPotion,
    val ingredient: Item,
    val level: Int,
    val experience: Double,
    val potion: Item,
) {
    ATTACK_POTION(
        UnfinishedPotion.GUAM,
        Item(Items.EYE_OF_NEWT_221),
        3,
        25.0,
        Item(Items.ATTACK_POTION3_121),
    ),
    ANTIPOISON_POTION(
        UnfinishedPotion.MARRENTILL,
        Item(Items.UNICORN_HORN_DUST_235),
        5,
        37.5,
        Item(Items.ANTIPOISON3_175),
    ),
    RELICYM_BALM(
        UnfinishedPotion.ROGUE_PURSE,
        Item(Items.CLEAN_SNAKE_WEED_1526),
        8,
        0.0,
        Item(Items.RELICYMS_BALM3_4844),
    ),
    STRENGTH_POTION(
        UnfinishedPotion.TARROMIN,
        Item(Items.LIMPWURT_ROOT_225),
        12,
        50.0,
        Item(Items.STRENGTH_POTION3_115),
    ),
    RESTORE_POTION(
        UnfinishedPotion.HARRALANDER,
        Item(Items.RED_SPIDERS_EGGS_223),
        22,
        62.5,
        Item(Items.RESTORE_POTION3_127),
    ),
    ENERGY_POTION(
        UnfinishedPotion.HARRALANDER,
        Item(Items.CHOCOLATE_DUST_1975),
        26,
        67.5,
        Item(Items.ENERGY_POTION3_3010),
    ),
    DEFENCE_POTION(
        UnfinishedPotion.RANARR,
        Item(Items.WHITE_BERRIES_239),
        30,
        45.0,
        Item(Items.DEFENCE_POTION3_133),
    ),
    AGILITY_POTION(
        UnfinishedPotion.TOADFLAX,
        Item(Items.TOADS_LEGS_2152),
        34,
        80.0,
        Item(Items.AGILITY_POTION3_3034),
    ),
    COMBAT_POTION(
        UnfinishedPotion.HARRALANDER,
        Item(Items.GOAT_HORN_DUST_9736),
        36,
        84.0,
        Item(Items.COMBAT_POTION3_9741),
    ),
    PRAYER_POTION(
        UnfinishedPotion.RANARR,
        Item(Items.SNAPE_GRASS_231),
        38,
        87.5,
        Item(Items.PRAYER_POTION3_139),
    ),
    SUMMONING_POTION(
        UnfinishedPotion.SPIRIT_WEED,
        Item(Items.COCKATRICE_EGG_12109),
        40,
        92.0,
        Item(Items.SUMMONING_POTION3_12142),
    ),
    SUPER_ATTACK(
        UnfinishedPotion.IRIT,
        Item(Items.EYE_OF_NEWT_221),
        45,
        100.0,
        Item(Items.SUPER_ATTACK3_145),
    ),
    SUPER_ANTIPOISON(
        UnfinishedPotion.IRIT,
        Item(Items.UNICORN_HORN_DUST_235),
        48,
        106.3,
        Item(Items.SUPER_ANTIPOISON3_181),
    ),
    FISHING_POTION(
        UnfinishedPotion.AVANTOE,
        Item(Items.SNAPE_GRASS_231),
        50,
        112.5,
        Item(Items.FISHING_POTION3_151),
    ),
    SUPER_ENERGY(
        UnfinishedPotion.AVANTOE,
        Item(Items.MORT_MYRE_FUNGUS_2970),
        52,
        117.5,
        Item(Items.SUPER_ENERGY3_3018),
    ),
    HUNTING_POTION(
        UnfinishedPotion.AVANTOE,
        Item(Items.KEBBIT_TEETH_DUST_10111),
        53,
        120.0,
        Item(Items.HUNTER_POTION3_10000),
    ),
    SUPER_STRENGTH(
        UnfinishedPotion.KWUARM,
        Item(Items.LIMPWURT_ROOT_225),
        55,
        125.0,
        Item(Items.SUPER_STRENGTH3_157),
    ),
    WEAPON_POISON(
        UnfinishedPotion.KWUARM,
        Item(Items.DRAGON_SCALE_DUST_241),
        60,
        137.5,
        Item(Items.WEAPON_POISON_187),
    ),
    SUPER_RESTORE(
        UnfinishedPotion.SNAPDRAGON,
        Item(Items.RED_SPIDERS_EGGS_223),
        63,
        142.5,
        Item(Items.SUPER_RESTORE3_3026),
    ),
    SUPER_DEFENCE(
        UnfinishedPotion.CADANTINE,
        Item(Items.WHITE_BERRIES_239),
        66,
        160.0,
        Item(Items.SUPER_DEFENCE3_163),
    ),
    ANTIFIRE(
        UnfinishedPotion.LANTADYME,
        Item(Items.DRAGON_SCALE_DUST_241),
        69,
        157.5,
        Item(Items.ANTIFIRE_POTION3_2454),
    ),
    SUPER_RANGING_POTION(
        UnfinishedPotion.DWARF_WEED,
        Item(Items.WINE_OF_ZAMORAK_245),
        72,
        162.5,
        Item(Items.RANGING_POTION3_169),
    ),
    SUPER_MAGIC(
        UnfinishedPotion.LANTADYME,
        Item(Items.POTATO_CACTUS_3138),
        76,
        172.5,
        Item(Items.MAGIC_POTION3_3042),
    ),
    ZAMORAK_BREW(
        UnfinishedPotion.TORSTOL,
        Item(Items.JANGERBERRIES_247),
        78,
        175.0,
        Item(Items.ZAMORAK_BREW3_189),
    ),
    SARADOMIN_BREW(
        UnfinishedPotion.TOADFLAX,
        GrindItem.BIRDS_NEST.product,
        81,
        180.0,
        Item(Items.SARADOMIN_BREW3_6687),
    ),
    STRONG_WEAPON_POISON(
        UnfinishedPotion.STRONG_WEAPON_POISON,
        Item(Items.RED_SPIDERS_EGGS_223),
        73,
        165.0,
        Item(Items.WEAPON_POISON_PLUS_5937),
    ),
    SUPER_STRONG_WEAPON_POISON(
        UnfinishedPotion.SUPER_STRONG_WEAPON_POISON,
        Item(Items.POISON_IVY_BERRIES_6018),
        82,
        190.0,
        Item(Items.WEAPON_POISON_PLUS_PLUS_5940),
    ),
    STRONG_ANTI_POISON(
        UnfinishedPotion.STRONG_ANTIPOISON,
        Item(Items.YEW_ROOTS_6049),
        68,
        155.0,
        Item(Items.ANTIPOISON_PLUS3_5945),
    ),
    SUPER_STRONG_ANTI_POISON(
        UnfinishedPotion.SUPER_STRONG_ANTIPOISON,
        Item(Items.MAGIC_ROOTS_6051),
        79,
        177.5,
        Item(Items.ANTIPOISON_PLUS_PLUS3_5954),
    ),
    BLAMISH_OIL(
        UnfinishedPotion.HARRALANDER,
        Item(Items.BLAMISH_SNAIL_SLIME_1581),
        25,
        80.0,
        Item(Items.BLAMISH_OIL_1582),
    ),
    ;

    companion object {
        fun getPotion(
            unf: Item,
            ingredient: Item,
        ): FinishedPotion? {
            return Arrays
                .stream(values())
                .filter { potion: FinishedPotion? ->
                    potion!!.unfinished.potion.id == unf.id &&
                        potion.ingredient.id == ingredient.id
                }.findFirst()
                .orElse(null)
        }
    }
}
