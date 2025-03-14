package content.global.skill.crafting.loom

import core.game.node.item.Item
import org.rs.consts.Items

enum class Weaving(
    val product: Item,
    val required: Item,
    val level: Int,
    val experience: Double,
) {
    SACK(
        product = Item(Items.EMPTY_SACK_5418),
        required = Item(Items.JUTE_FIBRE_5931, 4),
        level = 21,
        experience = 38.0,
    ),
    BASKET(
        product = Item(Items.BASKET_5376),
        required = Item(Items.WILLOW_BRANCH_5933, 6),
        level = 36,
        experience = 56.0,
    ),
    CLOTH(
        product = Item(Items.STRIP_OF_CLOTH_3224),
        required = Item(Items.BALL_OF_WOOL_1759, 4),
        level = 10,
        experience = 12.0,
    ),
}
