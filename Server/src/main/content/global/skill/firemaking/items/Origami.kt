package content.global.skill.firemaking.items

import shared.consts.Graphics
import shared.consts.Items

/**
 * Represents origami items.
 */
enum class Origami(val base: Int, val product: Int, val graphic: Int) {
    YELLOW(Items.YELLOW_DYE_1765, Items.YELLOW_BALLOON_9935, Graphics.YELLOW_BALLOON_FLY_UPWARDS_883),
    BLUE(Items.BLUE_DYE_1767, Items.BLUE_BALLOON_9936, Graphics.BLUE_BALLOON_FLY_UPWARDS_886),
    RED(Items.RED_DYE_1763, Items.RED_BALLOON_9937, Graphics.RED_BALLOON_FLY_UPWARDS_889),
    ORANGE(Items.ORANGE_DYE_1769, Items.ORANGE_BALLOON_9938, Graphics.ORANGE_BALLOON_FLY_UPWARDS_892),
    GREEN(Items.GREEN_DYE_1771, Items.GREEN_BALLOON_9939, Graphics.GREEN_BALLOON_FLY_UPWARDS_895),
    PURPLE(Items.PURPLE_DYE_1773, Items.PURPLE_BALLOON_9940, Graphics.PURPLE_BALLOON_FLY_UPWARDS_898),
    PINK(Items.PINK_DYE_6955, Items.PINK_BALLOON_9941, Graphics.PINK_BALLOON_FLY_UPWARDS_901),
    BLACK(Items.BLACK_MUSHROOM_INK_4622, Items.BLACK_BALLOON_9942, Graphics.BLACK_BALLOON_FLY_UPWARDS_904),
    ;

    companion object {
        private val baseMap: Map<Int, Origami> = values().associateBy { it.base }
        private val productMap: Map<Int, Origami> = values().associateBy { it.product }

        /**
         * Finds [Origami] by base item id.
         */
        @JvmStatic
        fun forId(itemId: Int): Origami? = baseMap[itemId]

        /**
         * Finds [Origami] by product item id.
         */
        @JvmStatic
        fun forBalloon(used: Int): Origami? = productMap[used]
    }
}
