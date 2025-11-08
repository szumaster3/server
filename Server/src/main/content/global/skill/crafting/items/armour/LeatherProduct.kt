package content.global.skill.crafting.items.armour

import core.game.node.entity.player.link.diary.DiaryType
import shared.consts.Items

/**
 * Represents leather products
 */
enum class LeatherProduct(val input: Int, val product: Int, val amount: Int, val level: Int, val xp: Double, val studded: Boolean = false, val pair: Boolean = false, val diary: DiaryTask? = null, val type: Type = Type.SOFT) {
    LEATHER_BODY(Items.LEATHER_1741, Items.LEATHER_BODY_1129, 1, 14, 25.0, type = Type.SOFT),
    LEATHER_GLOVES(Items.LEATHER_1741, Items.LEATHER_GLOVES_1059, 1, 1, 13.8, pair = true, diary = DiaryTask(DiaryType.LUMBRIDGE, 1, 3), type = Type.SOFT),
    LEATHER_BOOTS(Items.LEATHER_1741, Items.LEATHER_BOOTS_1061, 1, 7, 16.0, pair = true, type = Type.SOFT),
    LEATHER_COWL(Items.LEATHER_1741, Items.LEATHER_COWL_1167, 1, 9, 18.5, type = Type.SOFT),
    LEATHER_VAMBRACES(Items.LEATHER_1741, Items.LEATHER_VAMBRACES_1063, 1, 11, 22.0, pair = true, type = Type.SOFT),
    LEATHER_CHAPS(Items.LEATHER_1741, Items.LEATHER_CHAPS_1095, 1, 18, 27.0, type = Type.SOFT),
    LEATHER_COIF(Items.LEATHER_1741, Items.COIF_1169, 1, 38, 37.0, type = Type.SOFT),
    HARD_LEATHER_BODY(Items.HARD_LEATHER_1743, Items.HARDLEATHER_BODY_1131, 1, 28, 35.0, type = Type.HARD),
    STUDDED_BODY(Items.HARD_LEATHER_1743, Items.STUDDED_BODY_1133, 1, 41, 40.0, studded = true, type = Type.STUDDED),
    STUDDED_CHAPS(Items.HARD_LEATHER_1743, Items.STUDDED_CHAPS_1097, 1, 44, 42.0, studded = true, type = Type.STUDDED),
    SNAKESKIN_BODY(Items.SNAKESKIN_6289, Items.SNAKESKIN_BODY_6322, 15, 53, 55.0, type = Type.SNAKESKIN),
    SNAKESKIN_CHAPS(Items.SNAKESKIN_6289, Items.SNAKESKIN_CHAPS_6324, 12, 51, 50.0, type = Type.SNAKESKIN),
    SNAKESKIN_VAMBRACES(Items.SNAKESKIN_6289, Items.SNAKESKIN_VBRACE_6330, 8, 47, 35.0, pair = true, type = Type.SNAKESKIN),
    SNAKESKIN_BANDANA(Items.SNAKESKIN_6289, Items.SNAKESKIN_BANDANA_6326, 5, 48, 45.0, type = Type.SNAKESKIN),
    SNAKESKIN_BOOTS(Items.SNAKESKIN_6289, Items.SNAKESKIN_BOOTS_6328, 6, 45, 30.0, pair = true, type = Type.SNAKESKIN),
    GREEN_DHIDE_VAMBRACES(Items.GREEN_D_LEATHER_1745, Items.GREEN_DHIDE_VAMB_1065, 1, 57, 62.0, pair = true, type = Type.DRAGON),
    GREEN_DHIDE_CHAPS(Items.GREEN_D_LEATHER_1745, Items.GREEN_DHIDE_CHAPS_1099, 2, 60, 124.0, type = Type.DRAGON),
    GREEN_DHIDE_BODY(Items.GREEN_D_LEATHER_1745, Items.GREEN_DHIDE_BODY_1135, 3, 63, 186.0, type = Type.DRAGON),
    BLUE_DHIDE_VAMBRACES(Items.BLUE_D_LEATHER_2505, Items.BLUE_DHIDE_VAMB_2487, 1, 66, 70.0, pair = true, type = Type.DRAGON),
    BLUE_DHIDE_CHAPS(Items.BLUE_D_LEATHER_2505, Items.BLUE_DHIDE_CHAPS_2493, 2, 68, 140.0, type = Type.DRAGON),
    BLUE_DHIDE_BODY(Items.BLUE_D_LEATHER_2505, Items.BLUE_DHIDE_BODY_2499, 3, 71, 210.0, type = Type.DRAGON),
    RED_DHIDE_VAMBRACES(Items.RED_DRAGON_LEATHER_2507, Items.RED_DHIDE_VAMB_2489, 1, 73, 78.0, pair = true, type = Type.DRAGON),
    RED_DHIDE_CHAPS(Items.RED_DRAGON_LEATHER_2507, Items.RED_DHIDE_CHAPS_2495, 2, 73, 156.0, type = Type.DRAGON),
    RED_DHIDE_BODY(Items.RED_DRAGON_LEATHER_2507, Items.RED_DHIDE_BODY_2501, 3, 77, 234.0, type = Type.DRAGON),
    BLACK_DHIDE_VAMBRACES(Items.BLACK_D_LEATHER_2509, Items.BLACK_DHIDE_VAMB_2491, 1, 79, 86.0, pair = true, type = Type.DRAGON),
    BLACK_DHIDE_CHAPS(Items.BLACK_D_LEATHER_2509, Items.BLACK_DHIDE_CHAPS_2497, 2, 82, 172.0, type = Type.DRAGON),
    BLACK_DHIDE_BODY(Items.BLACK_D_LEATHER_2509, Items.BLACK_DHIDE_BODY_2503, 3, 84, 258.0, type = Type.DRAGON),
    YAK_BODY(Items.CURED_YAK_HIDE_10820, Items.YAK_HIDE_ARMOUR_10822, 2, 46, 32.0, type = Type.YAK),
    YAK_LEGS(Items.CURED_YAK_HIDE_10820, Items.YAK_HIDE_ARMOUR_10824, 1, 43, 32.0, type = Type.YAK),
    SPIKED_VAMBRACES_LEATHER(Items.LEATHER_VAMBRACES_1063, Items.SPIKY_VAMBRACES_10077, 1, 32, 6.0, type = Type.SPIKED),
    SPIKED_VAMBRACES_GREEN(Items.GREEN_DHIDE_VAMB_1065, Items.GREEN_SPIKY_VAMBS_10079, 1, 32, 6.0, type = Type.SPIKED),
    SPIKED_VAMBRACES_BLUE(Items.BLUE_DHIDE_VAMB_2487, Items.BLUE_SPIKY_VAMBS_10081, 1, 32, 6.0, type = Type.SPIKED),
    SPIKED_VAMBRACES_RED(Items.RED_DHIDE_VAMB_2489, Items.RED_SPIKY_VAMBS_10083, 1, 32, 6.0, type = Type.SPIKED),
    SPIKED_VAMBRACES_BLACK(Items.BLACK_DHIDE_VAMB_2491, Items.BLACK_SPIKY_VAMBS_10085, 1, 32, 6.0, type = Type.SPIKED);

    enum class Type {
        SOFT, HARD, STUDDED, SNAKESKIN, DRAGON, YAK, SPIKED
    }

    data class DiaryTask(val type: DiaryType, val stage: Int, val step: Int)

    companion object {
        fun forInput(input: Int) = values().filter { it.input == input }
        fun forProduct(product: Int) = values().find { it.product == product }
    }
}