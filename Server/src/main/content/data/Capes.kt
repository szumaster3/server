package content.data

import shared.consts.Items

/**
 * Represents capes and their corresponding dye and item ids.
 */
enum class Capes(val dyeId: Int, val capeId: Int) {
    BLACK(Dyes.BLACK.id, Items.BLACK_CAPE_1019),
    RED(Dyes.RED.id, Items.RED_CAPE_1007),
    BLUE(Dyes.BLUE.id, Items.BLUE_CAPE_1021),
    YELLOW(Dyes.YELLOW.id, Items.YELLOW_CAPE_1023),
    GREEN(Dyes.GREEN.id, Items.GREEN_CAPE_1027),
    PURPLE(Dyes.PURPLE.id, Items.PURPLE_CAPE_1029),
    ORANGE(Dyes.ORANGE.id, Items.ORANGE_CAPE_1031),
    PINK(Dyes.PINK.id, Items.PINK_CAPE_6959);

    companion object {
        private val dyeIdToCape = values().associateBy { it.dyeId }
        fun forDyeId(id: Int) = dyeIdToCape[id]
    }
}