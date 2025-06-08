package content.global.skill.crafting.items.armour.capes

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * The dyes.
 */
enum class Dyes(val item: Item) {
    BLACK(Item(Items.BLACK_MUSHROOM_INK_4622)),
    RED(Item(Items.RED_DYE_1763)),
    YELLOW(Item(Items.YELLOW_DYE_1765)),
    BLUE(Item(Items.BLUE_DYE_1767)),
    ORANGE(Item(Items.ORANGE_DYE_1769)),
    GREEN(Item(Items.GREEN_DYE_1771)),
    PURPLE(Item(Items.PURPLE_DYE_1773)),
    PINK(Item(Items.PINK_DYE_6955));

    companion object {
        private val itemToDyeMap: MutableMap<Int?, Dyes?> = HashMap()

        init {
            for (dye in values()) {
                itemToDyeMap[dye.item.id] = dye
            }
        }

        /**
         * For item dyes.
         *
         * @param item the item
         * @return the dyes
         */
        @JvmStatic
        fun forItem(item: Item): Dyes? {
            return itemToDyeMap[item.id]
        }
    }
}
