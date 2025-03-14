package content.global.skill.firemaking

import org.rs.consts.Graphics
import org.rs.consts.Items

enum class Origami(
    val base: Int,
    val product: Int,
    val graphic: Int,
) {
    YELLOW(
        base = Items.YELLOW_DYE_1765,
        product = Items.YELLOW_BALLOON_9935,
        graphic = Graphics.YELLOW_BALLOON_FLY_UPWARDS_883,
    ),
    BLUE(
        base = Items.BLUE_DYE_1767,
        product = Items.BLUE_BALLOON_9936,
        graphic = Graphics.BLUE_BALLOON_FLY_UPWARDS_886,
    ),
    RED(
        base = Items.RED_DYE_1763,
        product = Items.RED_BALLOON_9937,
        graphic = Graphics.RED_BALLOON_FLY_UPWARDS_889,
    ),
    ORANGE(
        base = Items.ORANGE_DYE_1769,
        product = Items.ORANGE_BALLOON_9938,
        graphic = Graphics.ORANGE_BALLOON_FLY_UPWARDS_892,
    ),
    GREEN(
        base = Items.GREEN_DYE_1771,
        product = Items.GREEN_BALLOON_9939,
        graphic = Graphics.GREEN_BALLOON_FLY_UPWARDS_895,
    ),
    PURPLE(
        base = Items.PURPLE_DYE_1773,
        product = Items.PURPLE_BALLOON_9940,
        graphic = Graphics.PURPLE_BALLOON_FLY_UPWARDS_898,
    ),
    PINK(
        base = Items.PINK_DYE_6955,
        product = Items.PINK_BALLOON_9941,
        graphic = Graphics.PINK_BALLOON_FLY_UPWARDS_901,
    ),
    BLACK(
        base = Items.BLACK_MUSHROOM_INK_4622,
        product = Items.BLACK_BALLOON_9942,
        graphic = Graphics.BLACK_BALLOON_FLY_UPWARDS_904,
    ),
    ;

    companion object {
        @JvmStatic
        fun forId(itemId: Int): Origami? {
            return values().find { it.base == itemId }
        }

        @JvmStatic
        fun forBalloon(used: Int): Origami? {
            return values().find { it.product == used }
        }
    }
}
