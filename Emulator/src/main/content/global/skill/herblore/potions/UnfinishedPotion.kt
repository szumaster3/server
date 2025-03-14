package content.global.skill.herblore.potions

import content.global.skill.herblore.HerblorePulse
import content.global.skill.herblore.herbs.Herbs
import core.game.node.item.Item
import org.rs.consts.Items
import java.util.*

enum class UnfinishedPotion(
    val base: Item,
    val ingredient: Item,
    val level: Int,
    val potion: Item,
) {
    GUAM(HerblorePulse.VIAL_OF_WATER, Herbs.GUAM.product, 3, Item(Items.GUAM_POTIONUNF_91)),
    ROGUE_PURSE(HerblorePulse.VIAL_OF_WATER, Herbs.ROGUES_PUSE.product, 3, Item(Items.ROGUES_PURSE_POTIONUNF_4840)),
    MARRENTILL(HerblorePulse.VIAL_OF_WATER, Herbs.MARRENTILL.product, 5, Item(Items.MARRENTILL_POTIONUNF_93)),
    TARROMIN(HerblorePulse.VIAL_OF_WATER, Herbs.TARROMIN.product, 12, Item(Items.TARROMIN_POTIONUNF_95)),
    HARRALANDER(HerblorePulse.VIAL_OF_WATER, Herbs.HARRALANDER.product, 22, Item(Items.HARRALANDER_POTIONUNF_97)),
    RANARR(HerblorePulse.VIAL_OF_WATER, Herbs.RANARR.product, 30, Item(Items.RANARR_POTIONUNF_99)),
    TOADFLAX(HerblorePulse.VIAL_OF_WATER, Herbs.TOADFLAX.product, 34, Item(Items.TOADFLAX_POTIONUNF_3002)),
    SPIRIT_WEED(HerblorePulse.VIAL_OF_WATER, Herbs.SPIRIT_WEED.product, 40, Item(Items.SPIRIT_WEED_POTIONUNF_12181)),
    IRIT(HerblorePulse.VIAL_OF_WATER, Herbs.IRIT.product, 45, Item(Items.IRIT_POTIONUNF_101)),
    AVANTOE(HerblorePulse.VIAL_OF_WATER, Herbs.AVANTOE.product, 50, Item(Items.AVANTOE_POTIONUNF_103)),
    KWUARM(HerblorePulse.VIAL_OF_WATER, Herbs.KWUARM.product, 55, Item(Items.KWUARM_POTIONUNF_105)),
    SNAPDRAGON(HerblorePulse.VIAL_OF_WATER, Herbs.SNAPDRAGON.product, 63, Item(Items.SNAPDRAGON_POTIONUNF_3004)),
    CADANTINE(HerblorePulse.VIAL_OF_WATER, Herbs.CADANTINE.product, 66, Item(Items.CADANTINE_POTIONUNF_107)),
    LANTADYME(HerblorePulse.VIAL_OF_WATER, Herbs.LANTADYME.product, 69, Item(Items.LANTADYME_POTIONUNF_2483)),
    DWARF_WEED(HerblorePulse.VIAL_OF_WATER, Herbs.DWARF_WEED.product, 72, Item(Items.DWARF_WEED_POTIONUNF_109)),
    TORSTOL(HerblorePulse.VIAL_OF_WATER, Herbs.TORSTOL.product, 75, Item(Items.TORSTOL_POTIONUNF_111)),
    STRONG_WEAPON_POISON(
        HerblorePulse.COCONUT_MILK,
        Item(Items.CACTUS_SPINE_6016),
        73,
        Item(Items.WEAPON_POISON_PLUSUNF_5936),
    ),
    SUPER_STRONG_WEAPON_POISON(
        HerblorePulse.COCONUT_MILK,
        Item(Items.CAVE_NIGHTSHADE_2398),
        82,
        Item(Items.WEAPON_POISON_PLUS_PLUSUNF_5939),
    ),
    STRONG_ANTIPOISON(HerblorePulse.COCONUT_MILK, Herbs.TOADFLAX.product, 68, Item(Items.ANTIPOISON_PLUSUNF_5942)),
    SUPER_STRONG_ANTIPOISON(
        HerblorePulse.COCONUT_MILK,
        Herbs.IRIT.product,
        79,
        Item(Items.ANTIPOISON_PLUS_PLUSUNF_5951),
    ),
    ;

    companion object {
        fun forItem(
            item: Item,
            base: Item,
        ): UnfinishedPotion? {
            return Arrays
                .stream(values())
                .filter { potion: UnfinishedPotion? ->
                    (potion!!.ingredient.id == item.id || potion.ingredient.id == base.id) &&
                        (item.id == potion.base.id || base.id == potion.base.id)
                }.findFirst()
                .orElse(null)
        }
    }
}
