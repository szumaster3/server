package content.global.activity.oldman

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

/**
 * Enum represents Wise old man delivery activity reward table.
 * @author szu
 */
enum class WomTaskReward(
    val npc: IntArray,
    val table: WeightBasedTable,
) {
    REWARD_TABLE(
        intArrayOf(NPCs.WISE_OLD_MAN_2253),
        WeightBasedTable.create(
            // Gems
            WeightedItem(Items.UNCUT_DIAMOND_1617, 1, 1, 1.0),
            WeightedItem(Items.UNCUT_RUBY_1619, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_EMERALD_1621, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_RED_TOPAZ_1629, 1, 1, 8.0),
            WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 5.0),

            // Runes
            WeightedItem(Items.AIR_RUNE_556, 3, 75, 10.0),
            WeightedItem(Items.WATER_RUNE_555, 2, 50, 10.0),
            WeightedItem(Items.EARTH_RUNE_557, 2, 50, 10.0),
            WeightedItem(Items.BODY_RUNE_559, 2, 50, 10.0),
            WeightedItem(Items.MIND_RUNE_558, 2, 50, 10.0),
            WeightedItem(Items.FIRE_RUNE_554, 1, 25, 10.0),
            WeightedItem(Items.LAW_RUNE_563, 1, 25, 10.0),
            WeightedItem(Items.NATURE_RUNE_561, 1, 25, 10.0),
            WeightedItem(Items.CHAOS_RUNE_562, 1, 25, 10.0),

            // Herbs
            WeightedItem(Items.GRIMY_GUAM_200, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_MARRENTILL_202, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_TARROMIN_204, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_HARRALANDER_206, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_RANARR_208, 1, 10, 10.0),

            // Seeds
            WeightedItem(Items.POTATO_SEED_5318, 1, 9, 5.0),
            WeightedItem(Items.ONION_SEED_5319, 1, 6, 5.0),
            WeightedItem(Items.TOMATO_SEED_5322, 1, 4, 5.0),
            WeightedItem(Items.CABBAGE_SEED_5324, 1, 3, 5.0),
            WeightedItem(Items.MARIGOLD_SEED_5096, 1, 2, 5.0),
            WeightedItem(Items.YANILLIAN_SEED_5309, 1, 6, 5.0),
            WeightedItem(Items.CACTUS_SEED_5280, 1, 1, 5.0),
            WeightedItem(Items.CADAVABERRY_SEED_5102, 1, 1, 5.0),
            WeightedItem(Items.HAMMERSTONE_SEED_5307, 1, 9, 5.0),
            WeightedItem(Items.JANGERBERRY_SEED_5104, 1, 1, 5.0),
            WeightedItem(Items.BARLEY_SEED_5305, 1, 13, 5.0),
            WeightedItem(Items.NASTURTIUM_SEED_5098, 1, 1, 5.0),
            WeightedItem(Items.STRAWBERRY_SEED_5323, 1, 1, 5.0),
            WeightedItem(Items.JUTE_SEED_5306, 1, 9, 5.0),
            WeightedItem(Items.WOAD_SEED_5099, 1, 1, 5.0),
            WeightedItem(Items.GUAM_SEED_5291, 1, 1, 5.0),
            WeightedItem(Items.MARRENTILL_SEED_5292, 1, 1, 5.0),
            WeightedItem(Items.TARROMIN_SEED_5293, 1, 1, 5.0),
            WeightedItem(Items.TOADFLAX_SEED_5296, 1, 1, 5.0),
            WeightedItem(Items.HARRALANDER_SEED_5294, 1, 1, 5.0),
            WeightedItem(Items.WATERMELON_SEED_5321, 2, 2, 5.0),
            WeightedItem(Items.ROSEMARY_SEED_5097, 1, 1, 5.0),
            WeightedItem(Items.KRANDORIAN_SEED_5310, 1, 4, 5.0),
            WeightedItem(Items.REDBERRY_SEED_5101, 1, 1, 5.0),
            WeightedItem(Items.DWELLBERRY_SEED_5103, 1, 1, 5.0),
            WeightedItem(Items.ASGARNIAN_SEED_5308, 1, 7, 5.0),
            WeightedItem(Items.SWEETCORN_SEED_5320, 2, 2, 5.0),
            WeightedItem(Items.WHITEBERRY_SEED_5105, 1, 1, 5.0),
            WeightedItem(Items.WILDBLOOD_SEED_5311, 3, 3, 5.0),

            // Rare seeds
            WeightedItem(Items.LIMPWURT_SEED_5100, 1, 1, 1.0),
        )
    ),
    ;

    companion object {
        val rewardMap = HashMap<Int, WeightBasedTable>()

        init {
            WomTaskReward.values().forEach {
                it.npc.forEach { id -> rewardMap[id] = it.table }
            }
        }

        fun forId(id: Int): WeightBasedTable? = rewardMap[id]
    }
}
