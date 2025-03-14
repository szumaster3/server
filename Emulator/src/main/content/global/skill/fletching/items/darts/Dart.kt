package content.global.skill.fletching.items.darts

import org.rs.consts.Items

/**
 * Represents types of darts.
 *
 * @property unfinished The id of the unfinished dart tip.
 * @property finished   The id of the finished dart.
 * @property level      The required level to fletch.
 * @property experience The experience gained from creating dart.
 */
enum class Dart(
    val unfinished: Int,
    val finished: Int,
    val level: Int,
    val experience: Double,
) {
    BRONZE_DART(
        unfinished = Items.BRONZE_DART_TIP_819,
        finished = Items.BRONZE_DART_806,
        level = 1,
        experience = 1.8,
    ),
    IRON_DART(
        unfinished = Items.IRON_DART_TIP_820,
        finished = Items.IRON_DART_807,
        level = 22,
        experience = 3.8,
    ),
    STEEL_DART(
        unfinished = Items.STEEL_DART_TIP_821,
        finished = Items.STEEL_DART_808,
        level = 37,
        experience = 7.5,
    ),
    MITHRIL_DART(
        unfinished = Items.MITHRIL_DART_TIP_822,
        finished = Items.MITHRIL_DART_809,
        level = 52,
        experience = 11.2,
    ),
    ADAMANT_DART(
        unfinished = Items.ADAMANT_DART_TIP_823,
        finished = Items.ADAMANT_DART_810,
        level = 67,
        experience = 15.0,
    ),
    RUNE_DART(
        unfinished = Items.RUNE_DART_TIP_824,
        finished = Items.RUNE_DART_811,
        level = 81,
        experience = 18.8,
    ),
    DRAGON_DART(
        unfinished = Items.DRAGON_DART_TIP_11232,
        finished = Items.DRAGON_DART_11230,
        level = 95,
        experience = 25.0,
    ),
    ;

    companion object {
        val values = enumValues<Dart>()
        val product = values.associateBy { it.unfinished }

        fun isDart(id: Int): Boolean = id in product
    }
}
