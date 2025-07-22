package content.global.skill.crafting.items.leather

import org.rs.consts.Items

/**
 * Represents the snakeskin armour items.
 */
enum class SnakeskinLeather(val product: Int, val level: Int, val experience: Double, val amount: Int, ) {
    SNAKESKIN_BOOTS(Items.SNAKESKIN_BOOTS_6328, 45, 30.0, 6),
    SNAKESKIN_VAMBRACES(Items.SNAKESKIN_VBRACE_6330, 47, 35.0, 8),
    SNAKESKIN_BANDANA(Items.SNAKESKIN_BANDANA_6326, 48, 45.0, 5),
    SNAKESKIN_CHAPS(Items.SNAKESKIN_CHAPS_6324, 51, 50.0, 12),
    SNAKESKIN_BODY(Items.SNAKESKIN_BODY_6322, 53, 55.0, 15),
    ;

    companion object {
        /**
         * Map of product ids to [Snakeskin] entries.
         */
        @JvmStatic
        private val productMap = values().associateBy { it.product }

        /**
         * Gets [Snakeskin] by product id or null if none.
         */
        @JvmStatic
        fun forId(itemId: Int): SnakeskinLeather? = productMap[itemId]
    }
}
