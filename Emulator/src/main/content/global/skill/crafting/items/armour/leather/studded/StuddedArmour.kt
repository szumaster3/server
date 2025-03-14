package content.global.skill.crafting.items.armour.leather.studded

import org.rs.consts.Items

enum class StuddedArmour(
    val leather: Int,
    val product: Int,
    val level: Int,
    val experience: Double,
) {
    CHAPS(
        leather = Items.LEATHER_CHAPS_1095,
        product = Items.STUDDED_CHAPS_1097,
        level = 44,
        experience = 42.0,
    ),
    BODY(
        leather = Items.LEATHER_BODY_1129,
        product = Items.STUDDED_BODY_1133,
        level = 41,
        experience = 40.0,
    ),
    ;

    companion object {
        @JvmStatic
        fun forId(itemId: Int): StuddedArmour? {
            return StuddedArmour.values().find { it.leather == itemId }
        }
    }
}
