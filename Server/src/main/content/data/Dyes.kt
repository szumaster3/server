package content.data

import shared.consts.Items

/**
 * Represents dyes with their item ids.
 */
enum class Dyes(val id: Int) {
    BLACK(Items.BLACK_MUSHROOM_INK_4622),
    RED(Items.RED_DYE_1763),
    YELLOW(Items.YELLOW_DYE_1765),
    BLUE(Items.BLUE_DYE_1767),
    ORANGE(Items.ORANGE_DYE_1769),
    GREEN(Items.GREEN_DYE_1771),
    PURPLE(Items.PURPLE_DYE_1773),
    PINK(Items.PINK_DYE_6955);

    companion object {
        private val idToDye = values().associateBy { it.id }
        fun forId(id: Int) = idToDye[id]
    }
}