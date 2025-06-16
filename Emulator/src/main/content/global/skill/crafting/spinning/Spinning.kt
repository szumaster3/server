package content.global.skill.crafting.spinning

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Enum representing different spinning activities.
 */
enum class Spinning(val button: Int, val need: Int, val product: Int, val level: Int, val exp: Double, ) {
    WOOL(19, Items.WOOL_1737, Items.BALL_OF_WOOL_1759, 1, 2.5),
    FLAX(17, Items.FLAX_1779, Items.BOW_STRING_1777, 10, 15.0),
    ROOT(23, Items.MAGIC_ROOTS_6051, Items.MAGIC_STRING_6038, 19, 30.0),
    ROOT_OAK(23, Items.OAK_ROOTS_6043, Items.MAGIC_STRING_6038, 19, 30.0),
    ROOT_WILLOW(23, Items.WILLOW_ROOTS_6045, Items.MAGIC_STRING_6038, 19, 30.0),
    ROOT_MAPLE(23, Items.MAPLE_ROOTS_6047, Items.MAGIC_STRING_6038, 19, 30.0),
    ROOT_YEW(23, Items.YEW_ROOTS_6049, Items.MAGIC_STRING_6038, 19, 30.0),
    ROOT_SPIRIT(23, Items.SPIRIT_ROOTS_6053, Items.MAGIC_STRING_6038, 19, 30.0),
    SINEW(27, Items.SINEW_9436, Items.CROSSBOW_STRING_9438, 10, 15.0),
    TREE_ROOTS(31, Items.OAK_ROOTS_6043, Items.CROSSBOW_STRING_9438, 10, 15.0),
    YAK(35, Items.HAIR_10814, Items.ROPE_954, 30, 25.0),
    ;

    companion object {
        private val buttonMap: Map<Int, Spinning> = values().associateBy { it.button }
        private val allNeed: List<Item> = values().map { Item(it.need, 1) }
        private val allProduct: List<Item> = values().map { Item(it.product, 1) }

        /**
         * Finds [Spinning] by button id or null if none.
         */
        fun forId(id: Int): Spinning? = buttonMap[id]

        /**
         * Gets all required items for spinning.
         */
        fun getAllNeed(): List<Item> = allNeed

        /**
         * Gets all product items from spinning.
         */
        fun getAllProduct(): List<Item> = allProduct
    }
}
