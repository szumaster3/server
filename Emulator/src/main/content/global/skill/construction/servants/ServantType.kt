package content.global.skill.construction.servants

import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

enum class ServantType(
    val id: Int,
    val cost: Int,
    val capacity: Int,
    val level: Int,
    val timer: Int,
    vararg val food: Item?,
) {
    NONE(
        id = -1,
        cost = -1,
        capacity = -1,
        level = -1,
        timer = -1,
    ),
    RICK(
        id = NPCs.RICK_4235,
        cost = 500,
        capacity = 6,
        level = 20,
        timer = 60,
    ),
    MAID(
        id = NPCs.MAID_4237,
        cost = 1000,
        capacity = 10,
        level = 25,
        timer = 50,
        Item(Items.STEW_2003),
    ),
    COOK(
        id = NPCs.COOK_4239,
        cost = 3000,
        capacity = 16,
        level = 30,
        timer = 17,
        Item(Items.PINEAPPLE_PIZZA_2301), Item(Items.AMULET_OF_GLORY4_1712),
    ),
    BUTLER(
        id = NPCs.BUTLER_4241,
        cost = 5000,
        capacity = 20,
        level = 40,
        timer = 12,
        Item(Items.CHOCOLATE_CAKE_1897), Item(Items.CUP_OF_TEA_712),
    ),
    DEMON_BUTLER(
        id = NPCs.DEMON_BUTLER_4243,
        cost = 10000,
        capacity = 26,
        level = 50,
        timer = 7,
        Item(Items.CURRY_2011),
    ), ;

    companion object {
        @JvmStatic
        fun forId(id: Int): ServantType? = values().find { it.id == id }
    }
}
