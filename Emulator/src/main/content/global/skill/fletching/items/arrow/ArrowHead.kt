package content.global.skill.fletching.items.arrow

import org.rs.consts.Items

enum class ArrowHead(
    val unfinished: Int,
    val finished: Int,
    val level: Int,
    val experience: Double,
) {
    BRONZE_ARROW(Items.BRONZE_ARROWTIPS_39, Items.BRONZE_ARROW_882, 1, 1.3),
    IRON_ARROW(Items.IRON_ARROWTIPS_40, Items.IRON_ARROW_884, 15, 2.5),
    STEEL_ARROW(Items.STEEL_ARROWTIPS_41, Items.STEEL_ARROW_886, 30, 5.0),
    MITHRIL_ARROW(Items.MITHRIL_ARROWTIPS_42, Items.MITHRIL_ARROW_888, 45, 7.5),
    ADAMANT_ARROW(Items.ADAMANT_ARROWTIPS_43, Items.ADAMANT_ARROW_890, 60, 10.0),
    RUNE_ARROW(Items.RUNE_ARROWTIPS_44, Items.RUNE_ARROW_892, 75, 12.5),
    DRAGON_ARROW(Items.DRAGON_ARROWTIPS_11237, Items.DRAGON_ARROW_11212, 90, 15.0),
    BROAD_ARROW(Items.BROAD_ARROW_HEADS_13278, Items.BROAD_ARROW_4160, 52, 15.0),
    ;

    companion object {
        private val PRODUCT_MAP: MutableMap<Int, ArrowHead> = HashMap()

        init {
            for (arrowHead in values()) {
                PRODUCT_MAP[arrowHead.unfinished] = arrowHead
            }
        }

        fun getByUnfinishedId(id: Int): ArrowHead? {
            return PRODUCT_MAP[id]
        }
    }
}
