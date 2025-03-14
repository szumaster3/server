package content.global.skill.crafting.items.weapon

import org.rs.consts.Items

enum class Battlestaff(
    val required: Int,
    val productId: Int,
    val amount: Int = 1,
    val requiredLevel: Int,
    val experience: Double,
) {
    WATER_BATTLESTAFF(
        required = Items.WATER_ORB_571,
        productId = Items.WATER_BATTLESTAFF_1395,
        requiredLevel = 54,
        experience = 100.0,
    ),
    EARTH_BATTLESTAFF(
        required = Items.EARTH_ORB_575,
        productId = Items.EARTH_BATTLESTAFF_1399,
        requiredLevel = 58,
        experience = 112.5,
    ),
    FIRE_BATTLESTAFF(
        required = Items.FIRE_ORB_569,
        productId = Items.FIRE_BATTLESTAFF_1393,
        requiredLevel = 62,
        experience = 125.0,
    ),
    AIR_BATTLESTAFF(
        required = Items.AIR_ORB_573,
        productId = Items.AIR_BATTLESTAFF_1397,
        requiredLevel = 66,
        experience = 137.5,
    ),
    ;

    companion object {
        @JvmStatic
        fun forId(itemId: Int): Battlestaff? {
            return values().find { it.required == itemId }
        }

        @JvmStatic
        fun forProductId(productId: Int): Battlestaff? {
            return values().find { it.productId == productId }
        }
    }
}
