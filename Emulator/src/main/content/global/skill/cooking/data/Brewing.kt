package content.global.skill.cooking.data

import core.game.node.item.Item
import org.rs.consts.Items

enum class Brewing(
    val level: Int,
    val experience: Double,
    val product: Item,
    inputs: Array<Item>,
) {
    ASGARNIAN_ALE(
        level = 24,
        experience = 284.0,
        product = Item(Items.ASGARNIAN_ALE4_5785, 1),
        inputs = arrayOf(Item(Items.ASGARNIAN_HOPS_5996, 4)),
    ),
    AXEMAN_FOLLY(
        level = 49,
        experience = 413.0,
        product = Item(Items.AXEMANS_FOLLY4_5825, 1),
        inputs = arrayOf(Item(Items.OAK_ROOTS_6043, 1)),
    ),
    CHEFS_DELIGHT(
        level = 54,
        experience = 446.0,
        product = Item(Items.CHEFS_DELIGHT4_5834, 1),
        inputs = arrayOf(Item(Items.CHOCOLATE_DUST_1975, 4)),
    ),
    COMPOST(
        level = 1,
        experience = 1.0,
        product = Item(Items.COMPOST_6032, 1),
        inputs = arrayOf(Item(Items.BARLEY_MALT_6008)),
    ),
    DRAGON_BITTER(
        level = 39,
        experience = 347.0,
        product = Item(Items.DRAGON_BITTER4_5809, 1),
        inputs = arrayOf(Item(Items.KRANDORIAN_HOPS_6000, 4)),
    ),
    DWARVEN_STOUT(
        level = 19,
        experience = 215.0,
        product = Item(Items.DWARVEN_STOUT4_5777, 1),
        inputs = arrayOf(Item(Items.HAMMERSTONE_HOPS_5994, 4)),
    ),
    GREENMAN_ALE(
        level = 29,
        experience = 281.0,
        product = Item(Items.GREENMANS_ALE4_5793, 1),
        inputs = arrayOf(Item(Items.CLEAN_HARRALANDER_255, 4)),
    ),
    KELDA_STOUT(
        level = 22,
        experience = 0.0,
        product = Item(Items.KELDA_STOUT_6118, 1),
        inputs = arrayOf(Item(Items.KELDA_HOPS_6113, 4)),
    ),
    MATURE_CIDER(
        level = 14,
        experience = 192.0,
        product = Item(Items.MATURE_CIDER_5765, 1),
        inputs = arrayOf(Item(Items.APPLE_MUSH_5992, 4), Item(Items.THE_STUFF_8988, 1)),
    ),
    MIND_BOMB(
        level = 34,
        experience = 314.0,
        product = Item(Items.MIND_BOMB4_5801, 1),
        inputs = arrayOf(Item(Items.YANILLIAN_HOPS_5998, 4)),
    ),
    MOONLIGHT_MEAD(
        level = 44,
        experience = 380.0,
        product = Item(Items.MOONLIGHT_MEAD4_5817, 1),
        inputs = arrayOf(Item(Items.MUSHROOM_6004, 4)),
    ),
    SLAYER_RESPITE(
        level = 59,
        experience = 479.0,
        product = Item(Items.SLAYERS_RESPITE4_5841, 1),
        inputs = arrayOf(Item(Items.WILDBLOOD_HOPS_6002, 4)),
    ),
    WIZARDS_MIND_BOMB(
        level = 34,
        experience = 314.0,
        product = Item(Items.WIZARDS_MIND_BOMB_1907, 1),
        inputs = arrayOf(Item(Items.YANILLIAN_HOPS_5999, 4), Item(Items.THE_STUFF_8988)),
    ),
    ;

    val inputs: Array<Item> = inputs
}
