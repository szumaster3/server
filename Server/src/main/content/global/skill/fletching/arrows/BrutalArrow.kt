package content.global.skill.fletching.arrows

import shared.consts.Items

/**
 * Represents the types of brutal arrows.
 */
enum class BrutalArrow(val base: Int, val product: Int, val level: Int, val experience: Double) {
    BRONZE_BRUTAL(Items.BRONZE_NAILS_4819, Items.BRONZE_BRUTAL_4773, 7, 1.4),
    IRON_BRUTAL(Items.IRON_NAILS_4820, Items.IRON_BRUTAL_4778, 18, 2.6),
    STEEL_BRUTAL(Items.STEEL_NAILS_1539, Items.STEEL_BRUTAL_4783, 33, 5.1),
    BLACK_BRUTAL(Items.BLACK_NAILS_4821, Items.BLACK_BRUTAL_4788, 38, 6.4),
    MITHRIL_BRUTAL(Items.MITHRIL_NAILS_4822, Items.MITHRIL_BRUTAL_4793, 49, 7.5),
    ADAMANT_BRUTAL(Items.ADAMANTITE_NAILS_4823, Items.ADAMANT_BRUTAL_4798, 62, 10.1),
    RUNE_BRUTAL(Items.RUNE_NAILS_4824, Items.RUNE_BRUTAL_4803, 77, 12.5),
    ;

    companion object {
        val values = enumValues<BrutalArrow>()
        val product = values.associateBy { it.base }
    }
}
