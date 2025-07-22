package content.global.skill.construction.items

import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.rs.consts.Items

/**
 * Represents different types of nails.
 */
enum class NailType(
    val itemId: Int,
    val bendRate: Int,
) {
    BRONZE(
        itemId = Items.BRONZE_NAILS_4819,
        bendRate = 5,
    ),
    IRON(
        itemId = Items.IRON_NAILS_4820,
        bendRate = 7,
    ),
    BLACK(
        itemId = Items.BLACK_NAILS_4821,
        bendRate = 8,
    ),
    STEEL(
        itemId = Items.STEEL_NAILS_1539,
        bendRate = 10,
    ),
    MITHRIL(
        itemId = Items.MITHRIL_NAILS_4822,
        bendRate = 13,
    ),
    ADAMANT(
        itemId = Items.ADAMANTITE_NAILS_4823,
        bendRate = 15,
    ),
    RUNE(
        itemId = Items.RUNE_NAILS_4824,
        bendRate = 20,
    ),
    ;

    /**
     * Returns true if a random roll matches the bend rate (indicating a bend).
     */
    fun isBend(): Boolean = RandomFunction.getRandom(bendRate) == 0

    companion object {
        val values = enumValues<NailType>()
        val product = values.associateBy { it.itemId }

        /**
         * Finds the first Nail type in the inventory with at least the required amount.
         * @return Nail type or null if none found.
         */
        @JvmStatic
        fun get(player: Player, amount: Int): NailType? {
            val allTypes = values()
            for (i in allTypes.indices.reversed()) {
                val type = allTypes[i]
                if (player.inventory.contains(type.itemId, amount)) {
                    return type
                }
            }
            return null
        }

    }
}
