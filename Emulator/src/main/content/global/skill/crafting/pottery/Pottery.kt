package content.global.skill.crafting.pottery

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the different pottery crafting.
 */
enum class Pottery(
    val unfinished: Item,
    val product: Item,
    val level: Int,
    val exp: Double,
    val fireExp: Double,
) {
    /**
     * Crafting an Unfired Pot into an Empty Pot.
     */
    POT(
        unfinished = Item(Items.UNFIRED_POT_1787),
        product = Item(Items.EMPTY_POT_1931),
        level = 1,
        exp = 6.3,
        fireExp = 6.3,
    ),

    /**
     * Crafting an Unfired Pie Dish into a Pie Dish.
     */
    DISH(
        unfinished = Item(Items.UNFIRED_PIE_DISH_1789),
        product = Item(Items.PIE_DISH_2313),
        level = 7,
        exp = 15.0,
        fireExp = 10.0,
    ),

    /**
     * Crafting an Unfired Bowl into a Bowl.
     */
    BOWL(
        unfinished = Item(Items.UNFIRED_BOWL_1791),
        product = Item(Items.BOWL_1923),
        level = 8,
        exp = 18.0,
        fireExp = 15.0,
    ),

    /**
     * Crafting an Unfired Plant Pot into a Plant Pot.
     */
    PLANT(
        unfinished = Item(Items.UNFIRED_PLANT_POT_5352),
        product = Item(Items.PLANT_POT_5350),
        level = 19,
        exp = 20.0,
        fireExp = 17.5,
    ),

    /**
     * Crafting an Unfired Pot Lid into a Pot Lid.
     */
    LID(
        unfinished = Item(Items.UNFIRED_POT_LID_4438),
        product = Item(Items.POT_LID_4440),
        level = 25,
        exp = 20.0,
        fireExp = 20.0,
    ),
    ;

    companion object {
        /**
         * Returns the [Pottery] enum entry corresponding to the given unfinished item ID.
         *
         * @param id The ID of the unfinished pottery item.
         * @return The corresponding [Pottery] enum entry, or null if no match is found.
         */
        @JvmStatic
        fun forId(id: Int): Pottery? = values().find { it.unfinished.id == id }
    }
}
