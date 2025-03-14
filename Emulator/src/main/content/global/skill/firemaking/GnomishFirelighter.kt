package content.global.skill.firemaking

import org.rs.consts.Items

enum class GnomishFirelighter(
    val base: Int,
    val product: Int,
) {
    RED(
        base = Items.RED_FIRELIGHTER_7329,
        product = Items.RED_LOGS_7404,
    ),
    GREEN(
        base = Items.GREEN_FIRELIGHTER_7330,
        product = Items.GREEN_LOGS_7405,
    ),
    BLUE(
        base = Items.BLUE_FIRELIGHTER_7331,
        product = Items.BLUE_LOGS_7406,
    ),
    PURPLE(
        base = Items.PURPLE_FIRELIGHTER_10326,
        product = Items.PURPLE_LOGS_10329,
    ),
    WHITE(
        base = Items.WHITE_FIRELIGHTER_10327,
        product = Items.WHITE_LOGS_10328,
    ),
    ;

    companion object {
        private val productMap: Map<Int, GnomishFirelighter> = values().associateBy { it.base }

        @JvmStatic
        fun forProduct(product: Int): GnomishFirelighter? = productMap[product]
    }
}
