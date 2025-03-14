package content.global.skill.construction.item

import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.rs.consts.Items

enum class Nails(
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

    fun isBend(): Boolean = RandomFunction.getRandom(bendRate) == 0

    companion object {
        val values = enumValues<Nails>()
        val product = values.associateBy { it.itemId }

        @JvmStatic
        fun get(
            player: Player,
            requiredAmount: Int,
        ): Nails? {
            for (nailType in values()) {
                if (player.inventory.contains(nailType.itemId, requiredAmount)) {
                    return nailType
                }
            }
            return null
        }
    }
}
