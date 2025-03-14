package content.global.skill.fletching.items.bolts

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the types of Kebbit bolts.
 *
 * @param base       The base item id.
 * @param product    The product item id.
 * @param level      The required level to craft.
 * @param experience The experience gained from crafting.
 */
enum class KebbitBolt(
    val base: Int,
    val product: Int,
    val level: Int,
    val experience: Double,
) {
    KEBBIT_BOLT(
        base = Items.KEBBIT_SPIKE_10105,
        product = Items.KEBBIT_BOLTS_10158,
        level = 32,
        experience = 5.80,
    ),
    LONG_KEBBIT_BOLT(
        base = Items.LONG_KEBBIT_SPIKE_10107,
        product = Items.LONG_KEBBIT_BOLTS_10159,
        level = 83,
        experience = 7.90,
    ),
    ;

    companion object {
        fun forId(item: Item): KebbitBolt? {
            return values().firstOrNull { it.base == item.id }
        }
    }
}
