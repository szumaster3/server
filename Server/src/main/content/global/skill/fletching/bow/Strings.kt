package content.global.skill.fletching.bow

import shared.consts.Animations
import shared.consts.Items

/**
 * Represents the types of bow strings.
 */
enum class Strings(private val indicator: Byte, val unfinished: Int, val product: Int, val level: Int, val experience: Double, val animation: Int) {
    // Bows
    SHORT_BOW(1, Items.SHORTBOW_U_50, Items.SHORTBOW_841, 5, 5.0, Animations.FLETCH_SHORTBOW_6678),
    LONG_BOW(1, Items.LONGBOW_U_48, Items.LONGBOW_839, 10, 10.0, Animations.FLETCH_SHIELDBOW_6684),
    OAK_SHORTBOW(1, Items.OAK_SHORTBOW_U_54, Items.OAK_SHORTBOW_843, 20, 16.5, Animations.FLETCH_OAK_SHORTBOW_6679),
    OAK_LONGBOW(1, Items.OAK_LONGBOW_U_56, Items.OAK_LONGBOW_845, 25, 25.0, Animations.FLETCH_OAK_SHIELDBOW_6685),
    COMP_OGRE_BOW(1, Items.UNSTRUNG_COMP_BOW_4825, Items.COMP_OGRE_BOW_4827, 30, 40.0, Animations.FLETCH_OAK_SHIELDBOW_6685),
    WILLOW_SHORTBOW(1, Items.WILLOW_SHORTBOW_U_60, Items.WILLOW_SHORTBOW_849, 35, 33.3, Animations.FLETCH_WILLOW_SHORTBOW_6680),
    WILLOW_LONGBOW(1, Items.WILLOW_LONGBOW_U_58, Items.WILLOW_LONGBOW_847, 40, 41.5, Animations.FLETCH_WILLOW_SHIELDBOW_6686),
    MAPLE_SHORTBOW(1, Items.MAPLE_SHORTBOW_U_64, Items.MAPLE_SHORTBOW_853, 50, 50.0, Animations.FLETCH_MAPLE_SHORTBOW_6681),
    MAPLE_LONGBOW(1, Items.MAPLE_LONGBOW_U_62, Items.MAPLE_LONGBOW_851, 55, 58.3, Animations.FLETCH_MAPLE_SHIELDBOW_6687),
    YEW_SHORTBOW(1, Items.YEW_SHORTBOW_U_68, Items.YEW_SHORTBOW_857, 65, 67.5, Animations.FLETCH_YEW_SHORTBOW_6682),
    YEW_LONGBOW(1, Items.YEW_LONGBOW_U_66, Items.YEW_LONGBOW_855, 70, 75.0, Animations.FLETCH_YEW_SHIELDBOW_6688),
    MAGIC_SHORTBOW(1, Items.MAGIC_SHORTBOW_U_72, Items.MAGIC_SHORTBOW_861, 80, 83.3, Animations.FLETCH_MAGIC_SHORTBOW_6683),
    MAGIC_LONGBOW(1, Items.MAGIC_LONGBOW_U_70, Items.MAGIC_LONGBOW_859, 85, 91.5, Animations.FLETCH_MAGIC_SHIELDBOW_6689),
    // Crossbows
    BRONZE_CBOW(2, Items.BRONZE_CBOW_U_9454, Items.BRONZE_CROSSBOW_9174, 9, 6.0, Animations.FLETCH_BOW_6671),
    BLURITE_CBOW(2, Items.BLURITE_CBOW_U_9456, Items.BLURITE_CROSSBOW_9176, 24, 16.0, Animations.FLETCH_BOW_6672),
    IRON_CBOW(2, Items.IRON_CBOW_U_9457, Items.IRON_CROSSBOW_9177, 39, 22.0, Animations.FLETCH_BOW_6673),
    STEEL_CBOW(2, Items.STEEL_CBOW_U_9459, Items.STEEL_CROSSBOW_9179, 46, 27.0, Animations.FLETCH_BOW_6674),
    MITHIRIL_CBOW(2, Items.MITHRIL_CBOW_U_9461, Items.MITH_CROSSBOW_9181, 54, 32.0, Animations.FLETCH_BOW_6675),
    ADAMANT_CBOW(2, Items.ADAMANT_CBOW_U_9463, Items.ADAMANT_CROSSBOW_9183, 61, 41.0, Animations.FLETCH_BOW_6676),
    RUNITE_CBOW(2, Items.RUNITE_CBOW_U_9465, Items.RUNE_CROSSBOW_9185, 69, 50.0, Animations.FLETCH_BOW_6677),
    ;

    val string: Int =
        when (indicator.toInt() and 0xFF) {
            1 -> Items.BOW_STRING_1777
            2 -> Items.CROSSBOW_STRING_9438
            else -> 0
        }

    companion object {
        val values = enumValues<Strings>()
        val product = values.associateBy { it.unfinished }
    }
}
