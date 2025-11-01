package content.global.skill.runecrafting

import core.game.node.item.Item
import shared.consts.Items

/**
 * Represents the various elemental tiaras.
 */
enum class Tiara(val item: Int, val experience: Double) {
    AIR(Items.AIR_TIARA_5527, 25.0),
    MIND(Items.MIND_TIARA_5529, 27.5),
    WATER(Items.WATER_TIARA_5531, 30.0),
    EARTH(Items.EARTH_TIARA_5535, 32.5),
    FIRE(Items.FIRE_TIARA_5537, 35.0),
    BODY(Items.BODY_TIARA_5533, 37.5),
    COSMIC(Items.COSMIC_TIARA_5539, 40.0),
    CHAOS(Items.CHAOS_TIARA_5543, 43.5),
    NATURE(Items.NATURE_TIARA_5541, 45.0),
    LAW(Items.LAW_TIARA_5545, 47.5),
    DEATH(Items.DEATH_TIARA_5547, 50.0),
    BLOOD(Items.BLOOD_TIARA_5549, 52.5),
    ;

    /**
     * Gets the talisman for this tiara.
     */
    val talisman: Talisman?
        get() = Talisman.values().find { it.name == name }

    companion object {
        private val itemToTiara = values().associateBy { it.item }
        @JvmStatic
        fun forItem(item: Item): Tiara? = itemToTiara[item.id]
    }
}
