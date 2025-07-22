package content.global.skill.fletching.items.bolts

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the types of Kebbit bolts.
 */
enum class KebbitBolt(val base: Int, val product: Int, val level: Int, val experience: Double) {
    KEBBIT_BOLT(Items.KEBBIT_SPIKE_10105, Items.KEBBIT_BOLTS_10158, 32, 5.80),
    LONG_KEBBIT_BOLT(Items.LONG_KEBBIT_SPIKE_10107, Items.LONG_KEBBIT_BOLTS_10159, 83, 7.90),
    ;

    companion object {
        fun forId(item: Item): KebbitBolt? = values().firstOrNull { it.base == item.id }
    }
}
