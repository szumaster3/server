package content.global.skill.crafting.pottery

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the different pottery crafting.
 */
enum class Pottery(val unfinished: Item, val product: Item, val level: Int, val exp: Double, val fireExp: Double, ) {
    POT(Item(Items.UNFIRED_POT_1787), Item(Items.EMPTY_POT_1931), 1, 6.3, 6.3),
    DISH(Item(Items.UNFIRED_PIE_DISH_1789), Item(Items.PIE_DISH_2313), 7, 15.0, 10.0),
    BOWL(Item(Items.UNFIRED_BOWL_1791), Item(Items.BOWL_1923), 8, 18.0, 15.0),
    PLANT(Item(Items.UNFIRED_PLANT_POT_5352), Item(Items.PLANT_POT_5350), 19, 20.0, 17.5),
    LID(Item(Items.UNFIRED_POT_LID_4438), Item(Items.POT_LID_4440), 25, 20.0, 20.0),
    ;

    companion object {
        private val unfinishedMap: Map<Int, Pottery> = values().associateBy { it.unfinished.id }

        /**
         * Finds [Pottery] by unfinished item id or returns `null` if none.
         */
        @JvmStatic
        fun forId(id: Int): Pottery? = unfinishedMap[id]
    }
}
