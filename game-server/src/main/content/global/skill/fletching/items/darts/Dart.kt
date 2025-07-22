package content.global.skill.fletching.items.darts

import org.rs.consts.Items

/**
 * Represents types of darts.
 */
enum class Dart(val unfinished: Int, val finished: Int, val level: Int, val experience: Double, ) {
    BRONZE_DART(Items.BRONZE_DART_TIP_819, Items.BRONZE_DART_806, 1, 1.8),
    IRON_DART(Items.IRON_DART_TIP_820, Items.IRON_DART_807, 22, 3.8),
    STEEL_DART(Items.STEEL_DART_TIP_821, Items.STEEL_DART_808, 37, 7.5),
    MITHRIL_DART(Items.MITHRIL_DART_TIP_822, Items.MITHRIL_DART_809, 52, 11.2),
    ADAMANT_DART(Items.ADAMANT_DART_TIP_823, Items.ADAMANT_DART_810, 67, 15.0),
    RUNE_DART(Items.RUNE_DART_TIP_824, Items.RUNE_DART_811, 81, 18.8),
    DRAGON_DART(Items.DRAGON_DART_TIP_11232, Items.DRAGON_DART_11230, 95, 25.0),
    ;

    companion object {
        val values = enumValues<Dart>()
        val product = values.associateBy { it.unfinished }

        fun isDart(id: Int): Boolean = id in product
    }
}
