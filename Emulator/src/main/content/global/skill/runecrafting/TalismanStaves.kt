package content.global.skill.runecrafting

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents a combination of a talisman, corresponding rune crafting staff, and tiara.
 *
 * @property items The [Item] representing the talisman.
 * @property staves The [Staves] enum representing the corresponding rune crafting staff.
 * @property tiara The ID of the corresponding tiara item.
 */
enum class TalismanStaves(
    val items: Item,
    val staves: Staves,
    val tiara: Int,
) {
    /**
     * Air talisman, associated with the Air rune crafting staff and Air tiara.
     */
    AIR(Item(Items.AIR_TALISMAN_1438), Staves.AIR_RC_STAFF, Items.AIR_TIARA_5527),

    /**
     * Mind talisman, associated with the Mind rune crafting staff and Mind tiara.
     */
    MIND(Item(Items.MIND_TALISMAN_1448), Staves.MIND_RC_STAFF, Items.MIND_TIARA_5529),

    /**
     * Water talisman, associated with the Water rune crafting staff and Water tiara.
     */
    WATER(Item(Items.WATER_TALISMAN_1444), Staves.WATER_RC_STAFF, Items.WATER_TIARA_5531),

    /**
     * Earth talisman, associated with the Earth rune crafting staff and Earth tiara.
     */
    EARTH(Item(Items.EARTH_TALISMAN_1440), Staves.EARTH_RC_STAFF, Items.EARTH_TIARA_5535),

    /**
     * Fire talisman, associated with the Fire rune crafting staff and Fire tiara.
     */
    FIRE(Item(Items.FIRE_TALISMAN_1442), Staves.FIRE_RC_STAFF, Items.FIRE_TIARA_5537),

    /**
     * Body talisman, associated with the Body rune crafting staff and Body tiara.
     */
    BODY(Item(Items.BODY_TALISMAN_1446), Staves.BODY_RC_STAFF, Items.BODY_TIARA_5533),

    /**
     * Cosmic talisman, associated with the Cosmic rune crafting staff and Cosmic tiara.
     */
    COSMIC(Item(Items.COSMIC_TALISMAN_1454), Staves.COSMIC_RC_STAFF, Items.COSMIC_TIARA_5539),

    /**
     * Chaos talisman, associated with the Chaos rune crafting staff and Chaos tiara.
     */
    CHAOS(Item(Items.CHAOS_TALISMAN_1452), Staves.CHAOS_RC_STAFF, Items.CHAOS_TIARA_5543),

    /**
     * Nature talisman, associated with the Nature rune crafting staff and Nature tiara.
     */
    NATURE(Item(Items.NATURE_TALISMAN_1462), Staves.NATURE_RC_STAFF, Items.NATURE_TIARA_5541),

    /**
     * Law talisman, associated with the Law rune crafting staff and Law tiara.
     */
    LAW(Item(Items.LAW_TALISMAN_1458), Staves.LAW_RC_STAFF, Items.LAW_TIARA_5545),

    /**
     * Death talisman, associated with the Death rune crafting staff and Death tiara.
     */
    DEATH(Item(Items.DEATH_TALISMAN_1456), Staves.DEATH_RC_STAFF, Items.DEATH_TIARA_5547),

    /**
     * Blood talisman, associated with the Blood rune crafting staff and Blood tiara.
     */
    BLOOD(Item(Items.BLOOD_TALISMAN_1450), Staves.BLOOD_RC_STAFF, Items.BLOOD_TIARA_5549);

    companion object {

        /**
         * A map to look up the corresponding [TalismanStaves] by [Item] id.
         */
        private val itemToTalismanStaves = HashMap<Int, TalismanStaves>()

        init {
            for (talisman in values()) {
                itemToTalismanStaves[talisman.items.id] = talisman
            }
        }

        /**
         * Retrieves the corresponding [TalismanStaves] for the given [Item].
         *
         * @param item The [Item] to search for.
         * @return The matching [TalismanStaves], or `null` if no match is found.
         */
        @JvmStatic
        fun from(item: Item): TalismanStaves? = itemToTalismanStaves[item.id]
    }
}
