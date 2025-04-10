package content.global.skill.crafting.items.weapon

import org.rs.consts.Items

/**
 * Represents the different types of battlestaffs that can be crafted.
 */
enum class Battlestaff(
    val required: Int,
    val productId: Int,
    val amount: Int = 1,
    val requiredLevel: Int,
    val experience: Double,
) {
    /**
     * Crafting Water Battlestaff using a Water Orb.
     */
    WATER_BATTLESTAFF(
        required = Items.WATER_ORB_571,
        productId = Items.WATER_BATTLESTAFF_1395,
        requiredLevel = 54,
        experience = 100.0,
    ),

    /**
     * Crafting Earth Battlestaff using an Earth Orb.
     */
    EARTH_BATTLESTAFF(
        required = Items.EARTH_ORB_575,
        productId = Items.EARTH_BATTLESTAFF_1399,
        requiredLevel = 58,
        experience = 112.5,
    ),

    /**
     * Crafting Fire Battlestaff using a Fire Orb.
     */
    FIRE_BATTLESTAFF(
        required = Items.FIRE_ORB_569,
        productId = Items.FIRE_BATTLESTAFF_1393,
        requiredLevel = 62,
        experience = 125.0,
    ),

    /**
     * Crafting Air Battlestaff using an Air Orb.
     */
    AIR_BATTLESTAFF(
        required = Items.AIR_ORB_573,
        productId = Items.AIR_BATTLESTAFF_1397,
        requiredLevel = 66,
        experience = 137.5,
    ),
    ;

    companion object {
        /**
         * Returns the [Battlestaff] enum entry corresponding to the given orb item id.
         *
         * @param itemId The id of the orb required for crafting the battle staff.
         * @return The corresponding [Battlestaff] enum entry, or `null` if no match is found.
         */
        @JvmStatic
        fun forId(itemId: Int): Battlestaff? = values().find { it.required == itemId }

        /**
         * Returns the [Battlestaff] enum entry corresponding to the given battle staff product id.
         *
         * @param productId The id of the battle staff product.
         * @return The corresponding [Battlestaff] enum entry, or `null` if no match is found.
         */
        @JvmStatic
        fun forProductId(productId: Int): Battlestaff? = values().find { it.productId == productId }
    }
}
