package content.global.skill.construction.servants

import core.game.node.item.Item
import org.rs.consts.NPCs

enum class ServantType(
    val npcId: Int,
    val cost: Int,
    val capacity: Int,
    val level: Int,
    val timer: Int,
    vararg val food: Item?,
) {
    NONE(
        npcId = -1,
        cost = -1,
        capacity = -1,
        level = -1,
        timer = -1,
    ),
    RICK(
        npcId = NPCs.RICK_4235,
        cost = 500,
        capacity = 6,
        level = 20,
        timer = 60,
    ),
    MAID(
        npcId = NPCs.MAID_4237,
        cost = 1000,
        capacity = 10,
        level = 25,
        timer = 50,
        food = arrayOf(Item(2003)),
    ),
    COOK(
        npcId = NPCs.COOK_4239,
        cost = 3000,
        capacity = 16,
        level = 30,
        timer = 17,
        food = arrayOf(Item(2301), Item(712)),
    ),
    BUTLER(
        npcId = NPCs.BUTLER_4241,
        cost = 5000,
        capacity = 20,
        level = 40,
        timer = 12,
        food = arrayOf(Item(1897), Item(712)),
    ),
    DEMON_BUTLER(
        npcId = NPCs.DEMON_BUTLER_4243,
        cost = 10000,
        capacity = 26,
        level = 50,
        timer = 7,
        food = arrayOf(Item(2011)),
    ),
    ;

    companion object {
        @JvmStatic
        fun forId(id: Int): ServantType? = values().find { it.npcId == id }
    }
}
