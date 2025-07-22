package content.global.skill.crafting.items.leather

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the different types of soft leather armour.
 */
enum class SoftLeather(
    val button: Int,
    val level: Int,
    val experience: Double,
    val product: Item,
) {
    ARMOUR(28, 14, 25.0, Item(Items.LEATHER_BODY_1129)),
    GLOVES(29, 1, 13.8, Item(Items.LEATHER_GLOVES_1059)),
    BOOTS(30, 7, 16.3, Item(Items.LEATHER_BOOTS_1061)),
    VAMBRACES(31, 11, 22.0, Item(Items.LEATHER_VAMBRACES_1063)),
    CHAPS(32, 18, 27.0, Item(Items.LEATHER_CHAPS_1095)),
    COIF(33, 38, 37.0, Item(Items.COIF_1169)),
    COWL(34, 9, 18.5, Item(Items.LEATHER_COWL_1167)),
    ;

    companion object {
        private val buttonMap: Map<Int, SoftLeather> = values().associateBy { it.button }

        /**
         * Finds [SoftLeather] by button id or returns null.
         */
        fun forButton(button: Int): SoftLeather? = buttonMap[button]
    }
}
