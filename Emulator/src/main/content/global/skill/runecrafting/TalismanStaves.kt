package content.global.skill.runecrafting

import core.game.node.item.Item
import org.rs.consts.Items

enum class TalismanStaves(
    val items: Item,
    val staves: Staves,
    val tiara: Int,
) {
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
    BLOOD(Item(Items.BLOOD_TALISMAN_1450), Staves.BLOOD_RC_STAFF, Items.BLOOD_TIARA_5549),
    ;

    companion object {
        private val itemToTalismanStaves = HashMap<Int, TalismanStaves>()

        init {
            for (talisman in values()) {
                itemToTalismanStaves[talisman.items.id] = talisman
            }
        }

        @JvmStatic
        fun from(item: Item): TalismanStaves? = itemToTalismanStaves[item.id]
    }
}
