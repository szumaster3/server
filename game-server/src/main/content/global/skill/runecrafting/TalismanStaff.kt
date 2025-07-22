package content.global.skill.runecrafting

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the different Runecrafting staves.
 */
enum class Staves(val item: Int, val experience: Double) {
    AIR_RC_STAFF(Items.AIR_TALISMAN_STAFF_13630, 25.0),
    MIND_RC_STAFF(Items.MIND_TALISMAN_STAFF_13631, 27.5),
    WATER_RC_STAFF(Items.WATER_TALISMAN_STAFF_13632, 30.0),
    EARTH_RC_STAFF(Items.EARTH_TALISMAN_STAFF_13633, 32.5),
    FIRE_RC_STAFF(Items.FIRE_TALISMAN_STAFF_13634, 35.0),
    BODY_RC_STAFF(Items.BODY_TALISMAN_STAFF_13635, 37.5),
    COSMIC_RC_STAFF(Items.COSMIC_TALISMAN_STAFF_13636, 40.0),
    CHAOS_RC_STAFF(Items.CHAOS_TALISMAN_STAFF_13637, 43.5),
    NATURE_RC_STAFF(Items.NATURE_TALISMAN_STAFF_13638, 45.0),
    LAW_RC_STAFF(Items.LAW_TALISMAN_STAFF_13639, 47.5),
    DEATH_RC_STAFF(Items.DEATH_TALISMAN_STAFF_13640, 50.0),
    BLOOD_RC_STAFF(Items.BLOOD_TALISMAN_STAFF_13641, 52.5);

    companion object {
        private val ITEM_TO_STAVES: MutableMap<Int, Staves> = HashMap()

        init {
            for (staff in values()) {
                ITEM_TO_STAVES[staff.item] = staff
            }
        }

        /**
         * Returns the Staves for the given item id.
         */
        fun forStaff(item: Int): Staves? {
            return ITEM_TO_STAVES[item]
        }
    }
}

/**
 * Represents a combination of a talismans.
 */
enum class TalismanStaff(val items: Item, val staves: Staves, val tiara: Int) {
    AIR(Item(Items.AIR_TALISMAN_1438), Staves.AIR_RC_STAFF, Items.AIR_TIARA_5527),
    MIND(Item(Items.MIND_TALISMAN_1448), Staves.MIND_RC_STAFF, Items.MIND_TIARA_5529),
    WATER(Item(Items.WATER_TALISMAN_1444), Staves.WATER_RC_STAFF, Items.WATER_TIARA_5531),
    EARTH(Item(Items.EARTH_TALISMAN_1440), Staves.EARTH_RC_STAFF, Items.EARTH_TIARA_5535),
    FIRE(Item(Items.FIRE_TALISMAN_1442), Staves.FIRE_RC_STAFF, Items.FIRE_TIARA_5537),
    BODY(Item(Items.BODY_TALISMAN_1446), Staves.BODY_RC_STAFF, Items.BODY_TIARA_5533),
    COSMIC(Item(Items.COSMIC_TALISMAN_1454), Staves.COSMIC_RC_STAFF, Items.COSMIC_TIARA_5539),
    CHAOS(Item(Items.CHAOS_TALISMAN_1452), Staves.CHAOS_RC_STAFF, Items.CHAOS_TIARA_5543),
    NATURE(Item(Items.NATURE_TALISMAN_1462), Staves.NATURE_RC_STAFF, Items.NATURE_TIARA_5541),
    LAW(Item(Items.LAW_TALISMAN_1458), Staves.LAW_RC_STAFF, Items.LAW_TIARA_5545),
    DEATH(Item(Items.DEATH_TALISMAN_1456), Staves.DEATH_RC_STAFF, Items.DEATH_TIARA_5547),
    BLOOD(Item(Items.BLOOD_TALISMAN_1450), Staves.BLOOD_RC_STAFF, Items.BLOOD_TIARA_5549);

    companion object {
        private val itemToTalismanStaves = values().associateBy { it.items.id }

        /**
         * Gets TalismanStaff for given Item, or null if none.
         */
        @JvmStatic
        fun from(item: Item): TalismanStaff? = itemToTalismanStaves[item.id]
    }
}
