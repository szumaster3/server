package content.global.skill.crafting.casting

import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Represents different types of gems that can be cut.
 */
enum class Gem(
    val uncut: Item,
    val gem: Item,
    val level: Int,
    val animation: Animation,
    val exp: Double,
) {
    SAPPHIRE(
        uncut = Item(Items.UNCUT_SAPPHIRE_1623),
        gem = Item(Items.SAPPHIRE_1607),
        level = 20,
        animation = Animation(Animations.CUT_SAPPHIRE_888),
        exp = 50.0,
    ),
    EMERALD(
        uncut = Item(Items.UNCUT_EMERALD_1621),
        gem = Item(Items.EMERALD_1605),
        level = 27,
        animation = Animation(Animations.CUT_EMERALD_889),
        exp = 67.0,
    ),
    RUBY(
        uncut = Item(Items.UNCUT_RUBY_1619),
        gem = Item(Items.RUBY_1603),
        level = 34,
        animation = Animation(Animations.CUT_RUBY_887),
        exp = 85.0,
    ),
    DIAMOND(
        uncut = Item(Items.UNCUT_DIAMOND_1617),
        gem = Item(Items.DIAMOND_1601),
        level = 43,
        animation = Animation(Animations.CUT_DIAMOND_886),
        exp = 107.5,
    ),
    DRAGONSTONE(
        uncut = Item(Items.UNCUT_DRAGONSTONE_1631),
        gem = Item(Items.DRAGONSTONE_1615),
        level = 55,
        animation = Animation(Animations.CUT_DRAGONSTONE_885),
        exp = 137.5,
    ),
    ONYX(
        uncut = Item(Items.UNCUT_ONYX_6571),
        gem = Item(Items.ONYX_6573),
        level = 67,
        animation = Animation(Animations.CHISEL_ONYX_2717),
        exp = 168.0,
    ),
    OPAL(
        uncut = Item(Items.UNCUT_OPAL_1625),
        gem = Item(Items.OPAL_1609),
        level = 1,
        animation = Animation(Animations.CUT_OPAL_890),
        exp = 10.0,
    ),
    JADE(
        uncut = Item(Items.UNCUT_JADE_1627),
        gem = Item(Items.JADE_1611),
        level = 13,
        animation = Animation(Animations.CUT_JADE_891),
        exp = 20.0,
    ),
    RED_TOPAZ(
        uncut = Item(Items.UNCUT_RED_TOPAZ_1629),
        gem = Item(Items.RED_TOPAZ_1613),
        level = 16,
        animation = Animation(Animations.CUT_TOPAZ_892),
        exp = 25.0,
    ),
    ;

    companion object {
        /**
         * Gets the corresponding [Gem] for a given cut [Item].
         * @param item The item to check.
         * @return The matching [Gem] or null if no match is found.
         */
        fun forItem(item: Item): Gem? {
            for (gem in values()) {
                if (gem.gem.id == item.id) {
                    return gem
                }
            }
            return null
        }

        /**
         * Retrieves the corresponding [Gem] for a given uncut [Item].
         * @param item The item to check.
         * @return The matching [Gem] or null if no match is found.
         */
        fun forId(item: Item): Gem? {
            for (gem in Gem.values()) {
                if (gem.uncut.id == item.id) {
                    return gem
                }
            }
            return null
        }
    }
}
