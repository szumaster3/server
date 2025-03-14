package content.global.skill.crafting.pottery

import core.game.node.item.Item
import org.rs.consts.Items

enum class Pottery(
    val unfinished: Item,
    val product: Item,
    val level: Int,
    val exp: Double,
    val fireExp: Double,
) {
    POT(
        unfinished = Item(Items.UNFIRED_POT_1787),
        product = Item(Items.EMPTY_POT_1931),
        level = 1,
        exp = 6.3,
        fireExp = 6.3,
    ),
    DISH(
        unfinished = Item(Items.UNFIRED_PIE_DISH_1789),
        product = Item(Items.PIE_DISH_2313),
        level = 7,
        exp = 15.0,
        fireExp = 10.0,
    ),
    BOWL(
        unfinished = Item(Items.UNFIRED_BOWL_1791),
        product = Item(Items.BOWL_1923),
        level = 8,
        exp = 18.0,
        fireExp = 15.0,
    ),
    PLANT(
        unfinished = Item(Items.UNFIRED_PLANT_POT_5352),
        product = Item(Items.PLANT_POT_5350),
        level = 19,
        exp = 20.0,
        fireExp = 17.5,
    ),
    LID(
        unfinished = Item(Items.UNFIRED_POT_LID_4438),
        product = Item(Items.POT_LID_4440),
        level = 25,
        exp = 20.0,
        fireExp = 20.0,
    ),
    ;

    companion object {
        @JvmStatic
        fun forId(id: Int): Pottery? {
            return values().find { it.unfinished.id == id }
        }
    }
}
