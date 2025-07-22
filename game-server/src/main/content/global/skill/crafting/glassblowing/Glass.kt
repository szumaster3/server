package content.global.skill.crafting.glassblowing

import org.rs.consts.Items

enum class Glass(
    val buttonId: Int,
    val productId: Int,
    val amount: Int,
    val requiredLevel: Int,
    val experience: Double
) {
    EMPTY_VIAL(38, Items.VIAL_229, 1, 33, 35.0),
    UNPOWERED_ORB(39, Items.UNPOWERED_ORB_567, 1, 46, 52.5),
    BEER_GLASS(40, Items.BEER_GLASS_1919, 1, 1, 17.5),
    EMPTY_CANDLE_LANTERN(41, Items.CANDLE_LANTERN_4527, 1, 4, 19.0),
    EMPTY_OIL_LAMP(42, Items.OIL_LAMP_4525, 1, 12, 25.0),
    LANTERN_LENS(43, Items.LANTERN_LENS_4542, 1, 49, 55.0),
    FISHBOWL(44, Items.FISHBOWL_6667, 1, 42, 42.5),
    EMPTY_LIGHT_ORB(45, Items.LIGHT_ORB_10973, 1, 87, 70.0);

    companion object {
        private val lookupMap: MutableMap<Int, Glass> = HashMap()

        init {
            for (glass in values()) {
                lookupMap[glass.buttonId] = glass
                lookupMap[glass.productId] = glass
            }
        }

        fun getById(id: Int): Glass? {
            return lookupMap[id]
        }
    }
}
