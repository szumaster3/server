package content.global.skill.fletching.items.arrow

import org.rs.consts.Items

/**
 * Represents the types of brutal arrows.
 *
 * @property base       The id of the base item (nails).
 * @property product    The product id.
 * @property level      The level required to fletch.
 * @property experience The experience gained from creating the brutal arrow.
 */
enum class BrutalArrow(
    val base: Int,
    val product: Int,
    val level: Int,
    val experience: Double,
) {
    BRONZE_BRUTAL(
        base = Items.BRONZE_NAILS_4819,
        product = Items.BRONZE_BRUTAL_4773,
        level = 7,
        experience = 1.4,
    ),
    IRON_BRUTAL(
        base = Items.IRON_NAILS_4820,
        product = Items.IRON_BRUTAL_4778,
        level = 18,
        experience = 2.6,
    ),
    STEEL_BRUTAL(
        base = Items.STEEL_NAILS_1539,
        product = Items.STEEL_BRUTAL_4783,
        level = 33,
        experience = 5.1,
    ),
    BLACK_BRUTAL(
        base = Items.BLACK_NAILS_4821,
        product = Items.BLACK_BRUTAL_4788,
        level = 38,
        experience = 6.4,
    ),
    MITHRIL_BRUTAL(
        base = Items.MITHRIL_NAILS_4822,
        product = Items.MITHRIL_BRUTAL_4793,
        level = 49,
        experience = 7.5,
    ),
    ADAMANT_BRUTAL(
        base = Items.ADAMANTITE_NAILS_4823,
        product = Items.ADAMANT_BRUTAL_4798,
        level = 62,
        experience = 10.1,
    ),
    RUNE_BRUTAL(
        base = Items.RUNE_NAILS_4824,
        product = Items.RUNE_BRUTAL_4803,
        level = 77,
        experience = 12.5,
    ),
    ;

    companion object {
        val values = enumValues<BrutalArrow>()
        val product = values.associateBy { it.base }
    }
}
