package content.global.skill.crafting.items.armour.leather.studded

import org.rs.consts.Items

/**
 * Represents the different types of studded leather armour that can be crafted.
 */
enum class StuddedArmour(
    val leather: Int,
    val product: Int,
    val level: Int,
    val experience: Double,
) {
    /**
     * Crafting Studded Chaps using Leather Chaps.
     */
    CHAPS(
        leather = Items.LEATHER_CHAPS_1095,
        product = Items.STUDDED_CHAPS_1097,
        level = 44,
        experience = 42.0,
    ),

    /**
     * Crafting Studded Body using Leather Body.
     */
    BODY(
        leather = Items.LEATHER_BODY_1129,
        product = Items.STUDDED_BODY_1133,
        level = 41,
        experience = 40.0,
    ),
    ;

    companion object {
        /**
         * Returns the [StuddedArmour] enum entry corresponding to the given leather item id.
         *
         * @param itemId The id of the leather item.
         * @return The corresponding [StuddedArmour] enum entry, or null if no match is found.
         */
        @JvmStatic
        fun forId(itemId: Int): StuddedArmour? = StuddedArmour.values().find { it.leather == itemId }
    }
}
