package content.global.skill.crafting.items.leather

import org.rs.consts.Items

/**
 * Represents the dragon leather.
 */
enum class DragonLeather(val leather: Int, val amount: Int, val product: Int, val level: Int, val experience: Double) {
    GREEN_D_HIDE_VAMBS(Items.GREEN_D_LEATHER_1745, 1, Items.GREEN_DHIDE_VAMB_1065, 57, 62.0),
    GREEN_D_HIDE_CHAPS(Items.GREEN_D_LEATHER_1745, 2, Items.GREEN_DHIDE_CHAPS_1099, 60, 124.0),
    GREEN_D_HIDE_BODY(Items.GREEN_D_LEATHER_1745, 3, Items.GREEN_DHIDE_BODY_1135, 63, 186.0),
    BLUE_D_HIDE_VAMBS(Items.BLUE_D_LEATHER_2505, 1, Items.BLUE_DHIDE_VAMB_2487, 66, 70.0),
    BLUE_D_HIDE_CHAPS(Items.BLUE_D_LEATHER_2505, 2, Items.BLUE_DHIDE_CHAPS_2493, 68, 140.0),
    BLUE_D_HIDE_BODY(Items.BLUE_D_LEATHER_2505, 3, Items.BLUE_DHIDE_BODY_2499, 71, 210.0),
    RED_D_HIDE_VAMBS(Items.RED_DRAGON_LEATHER_2507, 1, Items.RED_DHIDE_VAMB_2489, 73, 78.0),
    RED_D_HIDE_CHAPS(Items.RED_DRAGON_LEATHER_2507, 2, Items.RED_DHIDE_CHAPS_2495, 75, 156.0),
    RED_D_HIDE_BODY(Items.RED_DRAGON_LEATHER_2507, 3, Items.RED_DHIDE_BODY_2501, 77, 234.0),
    BLACK_D_HIDE_VAMBS(Items.BLACK_D_LEATHER_2509, 1, Items.BLACK_DHIDE_VAMB_2491, 79, 86.0),
    BLACK_D_HIDE_CHAPS(Items.BLACK_D_LEATHER_2509, 2, Items.BLACK_DHIDE_CHAPS_2497, 82, 172.0),
    BLACK_D_HIDE_BODY(Items.BLACK_D_LEATHER_2509, 3, Items.BLACK_DHIDE_BODY_2503, 84, 258.0);

    companion object {
        private val productToDragonLeatherMap: MutableMap<Int, DragonLeather> = HashMap()

        init {
            for (leather in values()) {
                productToDragonLeatherMap[leather.product] = leather
            }
        }

        /**
         * For id dragon leather.
         *
         * @param productId the product id
         * @return the dragon leather
         */
        fun forId(productId: Int): DragonLeather? {
            return productToDragonLeatherMap[productId]
        }
    }
}
