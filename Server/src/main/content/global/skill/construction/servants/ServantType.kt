package content.global.skill.construction.servants

import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents servants NPC that player can hire for various services.
 */
enum class ServantType(
    val ids: IntArray,
    val cost: Int,
    val capacity: Int,
    val level: Int,
    val timer: Int,
    vararg val food: Int?,
) {
    NONE(intArrayOf(-1), -1, -1, -1, -1),
    RICK(intArrayOf(NPCs.RICK_4235, NPCs.RICK_4236), 500, 6, 20, 60),
    MAID(intArrayOf(NPCs.MAID_4237, NPCs.MAID_4238), 1000, 10, 25, 30, Items.STEW_2003),
    COOK(intArrayOf(NPCs.COOK_4239,NPCs.COOK_4240), 3000, 16, 30, 17, Items.PINEAPPLE_PIZZA_2301, Items.CUP_OF_TEA_712),
    BUTLER(intArrayOf(NPCs.BUTLER_4241,NPCs.BUTLER_4242), 5000, 20, 40, 12, Items.CHOCOLATE_CAKE_1897, Items.CUP_OF_TEA_712),
    DEMON_BUTLER(intArrayOf(NPCs.DEMON_BUTLER_4243, NPCs.DEMON_BUTLER_4244), 10000, 26, 50, 7, Items.CURRY_2011),
    ;

    val id: Int
        get() = ids[0]

    companion object {
        @JvmStatic
        fun forId(id: Int): ServantType? = values().find { id in it.ids }
    }
}