package content.global.skill.crafting.gem

import shared.consts.Animations
import shared.consts.Items

/**
 * Represents different types of gems that can be cut.
 */
enum class Gem(val uncut: Int, val cut: Int, val animation: Int, val level: Int, val xp: Double) {
    OPAL(Items.UNCUT_OPAL_1625, Items.OPAL_1609, Animations.CUT_OPAL_890, 1, 10.0),
    JADE(Items.UNCUT_JADE_1627, Items.JADE_1611, Animations.CUT_JADE_891, 13, 20.0),
    RED_TOPAZ(Items.UNCUT_RED_TOPAZ_1629, Items.RED_TOPAZ_1613, Animations.CUT_TOPAZ_892,16, 25.0),
    SAPPHIRE(Items.UNCUT_SAPPHIRE_1623, Items.SAPPHIRE_1607, Animations.CUT_SAPPHIRE_888,20, 50.0),
    EMERALD(Items.UNCUT_EMERALD_1621, Items.EMERALD_1605, Animations.CUT_EMERALD_889, 27,67.0),
    RUBY(Items.UNCUT_RUBY_1619, Items.RUBY_1603, Animations.CUT_RUBY_887,34, 85.0),
    DIAMOND(Items.UNCUT_DIAMOND_1617, Items.DIAMOND_1601, Animations.CUT_DIAMOND_886, 43, 107.5),
    DRAGONSTONE(Items.UNCUT_DRAGONSTONE_1631, Items.DRAGONSTONE_1615, Animations.CUT_DRAGONSTONE_885,55, 137.5),
    ONYX(Items.UNCUT_ONYX_6571, Items.ONYX_6573, Animations.CHISEL_ONYX_2717,67, 168.0),
    ;

    companion object {
        /**
         * Gets the [Gem] for a given uncut item id.
         */
        fun forId(uncutId: Int): Gem? {
            return values().firstOrNull { it.uncut == uncutId }
        }
    }
}