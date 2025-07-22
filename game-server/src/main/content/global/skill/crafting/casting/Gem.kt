package content.global.skill.crafting.casting

import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Represents different types of gems that can be cut.
 */
enum class Gem(val uncut: Item, val gem: Item, val level: Int, val animation: Animation, val exp: Double, ) {
    SAPPHIRE(Item(Items.UNCUT_SAPPHIRE_1623), Item(Items.SAPPHIRE_1607), 20, Animation(Animations.CUT_SAPPHIRE_888), 50.0),
    EMERALD(Item(Items.UNCUT_EMERALD_1621), Item(Items.EMERALD_1605), 27, Animation(Animations.CUT_EMERALD_889), 67.0),
    RUBY(Item(Items.UNCUT_RUBY_1619), Item(Items.RUBY_1603), 34, Animation(Animations.CUT_RUBY_887), 85.0),
    DIAMOND(Item(Items.UNCUT_DIAMOND_1617), Item(Items.DIAMOND_1601), 43, Animation(Animations.CUT_DIAMOND_886), 107.5),
    DRAGONSTONE(Item(Items.UNCUT_DRAGONSTONE_1631), Item(Items.DRAGONSTONE_1615), 55, Animation(Animations.CUT_DRAGONSTONE_885), 137.5),
    ONYX(Item(Items.UNCUT_ONYX_6571), Item(Items.ONYX_6573), 67, Animation(Animations.CHISEL_ONYX_2717), 168.0),
    OPAL(Item(Items.UNCUT_OPAL_1625), Item(Items.OPAL_1609), 1, Animation(Animations.CUT_OPAL_890), 10.0),
    JADE(Item(Items.UNCUT_JADE_1627), Item(Items.JADE_1611), 13, Animation(Animations.CUT_JADE_891), 20.0),
    RED_TOPAZ(Item(Items.UNCUT_RED_TOPAZ_1629), Item(Items.RED_TOPAZ_1613), 16, Animation(Animations.CUT_TOPAZ_892), 25.0),
    ;

    companion object {
        /**
         * Gets the [Gem] for a given cut [Item].
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
         * Gets the [Gem] for a given uncut [Item].
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
