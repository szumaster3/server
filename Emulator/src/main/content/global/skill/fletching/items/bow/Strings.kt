package content.global.skill.fletching.items.bow

import org.rs.consts.Items

/**
 * Represents the types of bow strings.
 *
 * @property indicator  The type of string (1 for bow, 2 for crossbow).
 * @property unfinished The unfinished item id.
 * @property product    The product item id.
 * @property level      The required level to fletch.
 * @property experience The experience gained from creating the item.
 * @property animation  The animation id used during fletch.
 */
enum class Strings(
    private val indicator: Byte,
    val unfinished: Int,
    val product: Int,
    val level: Int,
    val experience: Double,
    val animation: Int,
) {
    SHORT_BOW(
        indicator = 1,
        unfinished = Items.SHORTBOW_U_50,
        product = Items.SHORTBOW_841,
        level = 5,
        experience = 5.0,
        animation = 6678,
    ),
    LONG_BOW(
        indicator = 1,
        unfinished = Items.LONGBOW_U_48,
        product = Items.LONGBOW_839,
        level = 10,
        experience = 10.0,
        animation = 6684,
    ),
    OAK_SHORTBOW(
        indicator = 1,
        unfinished = Items.OAK_SHORTBOW_U_54,
        product = Items.OAK_SHORTBOW_843,
        level = 20,
        experience = 16.5,
        animation = 6679,
    ),
    OAK_LONGBOW(
        indicator = 1,
        unfinished = Items.OAK_LONGBOW_U_56,
        product = Items.OAK_LONGBOW_845,
        level = 25,
        experience = 25.0,
        animation = 6685,
    ),
    COMP_OGRE_BOW(
        indicator = 1,
        unfinished = Items.UNSTRUNG_COMP_BOW_4825,
        product = Items.COMP_OGRE_BOW_4827,
        level = 30,
        experience = 40.0,
        animation = 6685,
    ),
    WILLOW_SHORTBOW(
        indicator = 1,
        unfinished = Items.WILLOW_SHORTBOW_U_60,
        product = Items.WILLOW_SHORTBOW_849,
        level = 35,
        experience = 33.3,
        animation = 6680,
    ),
    WILLOW_LONGBOW(
        indicator = 1,
        unfinished = Items.WILLOW_LONGBOW_U_58,
        product = Items.WILLOW_LONGBOW_847,
        level = 40,
        experience = 41.5,
        animation = 6686,
    ),
    MAPLE_SHORTBOW(
        indicator = 1,
        unfinished = Items.MAPLE_SHORTBOW_U_64,
        product = Items.MAPLE_SHORTBOW_853,
        level = 50,
        experience = 50.0,
        animation = 6681,
    ),
    MAPLE_LONGBOW(
        indicator = 1,
        unfinished = Items.MAPLE_LONGBOW_U_62,
        product = Items.MAPLE_LONGBOW_851,
        level = 55,
        experience = 58.3,
        animation = 6687,
    ),
    YEW_SHORTBOW(
        indicator = 1,
        unfinished = Items.YEW_SHORTBOW_U_68,
        product = Items.YEW_SHORTBOW_857,
        level = 65,
        experience = 67.5,
        animation = 6682,
    ),
    YEW_LONGBOW(
        indicator = 1,
        unfinished = Items.YEW_LONGBOW_U_66,
        product = Items.YEW_LONGBOW_855,
        level = 70,
        experience = 75.0,
        animation = 6688,
    ),
    MAGIC_SHORTBOW(
        indicator = 1,
        unfinished = Items.MAGIC_SHORTBOW_U_72,
        product = Items.MAGIC_SHORTBOW_861,
        level = 80,
        experience = 83.3,
        animation = 6683,
    ),
    MAGIC_LONGBOW(
        indicator = 1,
        unfinished = Items.MAGIC_LONGBOW_U_70,
        product = Items.MAGIC_LONGBOW_859,
        level = 85,
        experience = 91.5,
        animation = 6689,
    ),
    BRONZE_CBOW(
        indicator = 2,
        unfinished = Items.BRONZE_CBOW_U_9454,
        product = Items.BRONZE_CROSSBOW_9174,
        level = 9,
        experience = 6.0,
        animation = 6671,
    ),
    BLURITE_CBOW(
        indicator = 2,
        unfinished = Items.BLURITE_CBOW_U_9456,
        product = Items.BLURITE_CROSSBOW_9176,
        level = 24,
        experience = 16.0,
        animation = 6672,
    ),
    IRON_CBOW(
        indicator = 2,
        unfinished = Items.IRON_CBOW_U_9457,
        product = Items.IRON_CROSSBOW_9177,
        level = 39,
        experience = 22.0,
        animation = 6673,
    ),
    STEEL_CBOW(
        indicator = 2,
        unfinished = Items.STEEL_CBOW_U_9459,
        product = Items.STEEL_CROSSBOW_9179,
        level = 46,
        experience = 27.0,
        animation = 6674,
    ),
    MITHIRIL_CBOW(
        indicator = 2,
        unfinished = Items.MITHRIL_CBOW_U_9461,
        product = Items.MITH_CROSSBOW_9181,
        level = 54,
        experience = 32.0,
        animation = 6675,
    ),
    ADAMANT_CBOW(
        indicator = 2,
        unfinished = Items.ADAMANT_CBOW_U_9463,
        product = Items.ADAMANT_CROSSBOW_9183,
        level = 61,
        experience = 41.0,
        animation = 6676,
    ),
    RUNITE_CBOW(
        indicator = 2,
        unfinished = Items.RUNITE_CBOW_U_9465,
        product = Items.RUNE_CROSSBOW_9185,
        level = 69,
        experience = 50.0,
        animation = 6677,
    ),
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
