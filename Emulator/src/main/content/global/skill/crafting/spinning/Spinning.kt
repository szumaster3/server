package content.global.skill.crafting.spinning

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Enum representing different spinning activities in the game.
 *
 * Each entry contains details about the spinning activity, such as the button ID, the required item,
 * the produced item, the required level, and the experience gained.
 */
enum class Spinning(
    val button: Int,
    val need: Int,
    val product: Int,
    val level: Int,
    val exp: Double,
) {
    /**
     * Spinning Wool into Ball of Wool.
     */
    WOOL(
        button = 19,
        need = Items.WOOL_1737,
        product = Items.BALL_OF_WOOL_1759,
        level = 1,
        exp = 2.5,
    ),

    /**
     * Spinning Flax into Bow String.
     */
    FLAX(
        button = 17,
        need = Items.FLAX_1779,
        product = Items.BOW_STRING_1777,
        level = 10,
        exp = 15.0,
    ),

    /**
     * Spinning Magic Roots into Magic String.
     */
    ROOT(
        button = 23,
        need = Items.MAGIC_ROOTS_6051,
        product = Items.MAGIC_STRING_6038,
        level = 19,
        exp = 30.0,
    ),

    /**
     * Spinning Oak Roots into Magic String.
     */
    ROOT_OAK(
        button = 23,
        need = Items.OAK_ROOTS_6043,
        product = Items.MAGIC_STRING_6038,
        level = 19,
        exp = 30.0,
    ),

    /**
     * Spinning Willow Roots into Magic String.
     */
    ROOT_WILLOW(
        button = 23,
        need = Items.WILLOW_ROOTS_6045,
        product = Items.MAGIC_STRING_6038,
        level = 19,
        exp = 30.0,
    ),

    /**
     * Spinning Maple Roots into Magic String.
     */
    ROOT_MAPLE(
        button = 23,
        need = Items.MAPLE_ROOTS_6047,
        product = Items.MAGIC_STRING_6038,
        level = 19,
        exp = 30.0,
    ),

    /**
     * Spinning Yew Roots into Magic String.
     */
    ROOT_YEW(
        button = 23,
        need = Items.YEW_ROOTS_6049,
        product = Items.MAGIC_STRING_6038,
        level = 19,
        exp = 30.0,
    ),

    /**
     * Spinning Spirit Roots into Magic String.
     */
    ROOT_SPIRIT(
        button = 23,
        need = Items.SPIRIT_ROOTS_6053,
        product = Items.MAGIC_STRING_6038,
        level = 19,
        exp = 30.0,
    ),

    /**
     * Spinning Sinew into Crossbow String.
     */
    SINEW(
        button = 27,
        need = Items.SINEW_9436,
        product = Items.CROSSBOW_STRING_9438,
        level = 10,
        exp = 15.0,
    ),

    /**
     * Spinning Oak Roots into Crossbow String.
     */
    TREE_ROOTS(
        button = 31,
        need = Items.OAK_ROOTS_6043,
        product = Items.CROSSBOW_STRING_9438,
        level = 10,
        exp = 15.0,
    ),

    /**
     * Spinning Yak Hair into Rope.
     */
    YAK(
        button = 35,
        need = Items.HAIR_10814,
        product = Items.ROPE_954,
        level = 30,
        exp = 25.0,
    ),
    ;

    companion object {
        /**
         * Returns the [Spinning] enum entry corresponding to the given button ID.
         *
         * @param id The button ID associated with a spinning activity.
         * @return The corresponding [Spinning] enum entry, or null if no match is found.
         */
        @JvmStatic
        fun forId(id: Int): Spinning? = values().find { it.button == id }

        /**
         * Returns a list of all required items for each spinning activity.
         *
         * @return A mutable list of [Item]s representing the required items for each spinning activity.
         */
        @JvmStatic
        fun getAllNeed(): MutableList<Item> = values().map { Item(it.need, 1) }.toMutableList()

        /**
         * Returns a list of all produced items from each spinning activity.
         *
         * @return A mutable list of [Item]s representing the products of each spinning activity.
         */
        @JvmStatic
        fun getAllProduct(): MutableList<Item> = values().map { Item(it.product, 1) }.toMutableList()
    }
}
