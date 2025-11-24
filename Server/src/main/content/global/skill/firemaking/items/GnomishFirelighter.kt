package content.global.skill.firemaking.items

import shared.consts.Items

/**
 * Represents gnomish firelighters.
 */
enum class GnomishFirelighter(val base: Int, val product: Int) {
    RED(Items.RED_FIRELIGHTER_7329, Items.RED_LOGS_7404),
    GREEN(Items.GREEN_FIRELIGHTER_7330, Items.GREEN_LOGS_7405),
    BLUE(Items.BLUE_FIRELIGHTER_7331, Items.BLUE_LOGS_7406),
    PURPLE(Items.PURPLE_FIRELIGHTER_10326, Items.PURPLE_LOGS_10329),
    WHITE(Items.WHITE_FIRELIGHTER_10327, Items.WHITE_LOGS_10328),
    ;

    companion object {
        private val productMap: Map<Int, GnomishFirelighter> = values().associateBy { it.base }

        @JvmStatic
        fun forProduct(product: Int): GnomishFirelighter? = productMap[product]
    }
}
