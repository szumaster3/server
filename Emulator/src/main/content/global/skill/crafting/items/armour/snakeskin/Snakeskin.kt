package content.global.skill.crafting.items.armour.snakeskin

import org.rs.consts.Items

enum class Snakeskin(
    val product: Int,
    val level: Int,
    val experience: Double,
    val amount: Int,
) {
    SNAKESKIN_BOOTS(product = Items.SNAKESKIN_BOOTS_6328, level = 45, experience = 30.0, amount = 6),
    SNAKESKIN_VAMBRACES(product = Items.SNAKESKIN_VBRACE_6330, level = 47, experience = 35.0, amount = 8),
    SNAKESKIN_BANDANA(product = Items.SNAKESKIN_BANDANA_6326, level = 48, experience = 45.0, amount = 5),
    SNAKESKIN_CHAPS(product = Items.SNAKESKIN_CHAPS_6324, level = 51, experience = 50.0, amount = 12),
    SNAKESKIN_BODY(product = Items.SNAKESKIN_BODY_6322, level = 53, experience = 55.0, amount = 15),
    ;

    companion object {
        @JvmStatic
        private val productMap = values().associateBy { it.product }

        @JvmStatic
        fun forId(itemId: Int): Snakeskin? = productMap[itemId]
    }
}
