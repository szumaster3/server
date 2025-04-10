package content.global.skill.crafting.items.armour.snakeskin

import org.rs.consts.Items

/**
 * Represents the snakeskin armour items.
 */
enum class Snakeskin(
    val product: Int,
    val level: Int,
    val experience: Double,
    val amount: Int,
) {
    /**
     * Crafting Snakeskin Boots.
     */
    SNAKESKIN_BOOTS(product = Items.SNAKESKIN_BOOTS_6328, level = 45, experience = 30.0, amount = 6),

    /**
     * Crafting Snakeskin Vambraces.
     */
    SNAKESKIN_VAMBRACES(product = Items.SNAKESKIN_VBRACE_6330, level = 47, experience = 35.0, amount = 8),

    /**
     * Crafting Snakeskin Bandana.
     */
    SNAKESKIN_BANDANA(product = Items.SNAKESKIN_BANDANA_6326, level = 48, experience = 45.0, amount = 5),

    /**
     * Crafting Snakeskin Chaps.
     */
    SNAKESKIN_CHAPS(product = Items.SNAKESKIN_CHAPS_6324, level = 51, experience = 50.0, amount = 12),

    /**
     * Crafting Snakeskin Body.
     */
    SNAKESKIN_BODY(product = Items.SNAKESKIN_BODY_6322, level = 53, experience = 55.0, amount = 15),
    ;

    companion object {
        /**
         * A map of product IDs to corresponding [Snakeskin] enum entries.
         */
        @JvmStatic
        private val productMap = values().associateBy { it.product }

        /**
         * Returns the [Snakeskin] enum entry corresponding to the given product item ID.
         *
         * @param itemId The ID of the snakeskin item.
         * @return The corresponding [Snakeskin] enum entry, or null if no match is found.
         */
        @JvmStatic
        fun forId(itemId: Int): Snakeskin? = productMap[itemId]
    }
}
