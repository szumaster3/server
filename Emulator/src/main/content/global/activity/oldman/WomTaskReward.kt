package content.global.activity.oldman

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import org.rs.consts.Items
import org.rs.consts.NPCs

enum class WomTaskReward(
    val npc: IntArray,
    val table: WeightBasedTable,
) {
    REWARD_TABLE(
        intArrayOf(NPCs.WISE_OLD_MAN_2253),
        WeightBasedTable.create(
            WeightedItem(Items.UNCUT_DIAMOND_1617, 1, 1, 1.0),
            WeightedItem(Items.UNCUT_RUBY_1619, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_EMERALD_1621, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 5.0),
            WeightedItem(Items.UNCUT_RED_TOPAZ_1629, 1, 1, 10.0),
            WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 5.0),
            WeightedItem(Items.FIRE_RUNE_554, 1, 25, 10.0),
            WeightedItem(Items.WATER_RUNE_555, 2, 50, 10.0),
            WeightedItem(Items.AIR_RUNE_556, 3, 75, 10.0),
            WeightedItem(Items.EARTH_RUNE_557, 2, 50, 10.0),
            WeightedItem(Items.MIND_RUNE_558, 2, 50, 10.0),
            WeightedItem(Items.BODY_RUNE_559, 2, 50, 10.0),
            WeightedItem(Items.NATURE_RUNE_561, 1, 25, 10.0),
            WeightedItem(Items.CHAOS_RUNE_562, 1, 25, 10.0),
            WeightedItem(Items.LAW_RUNE_563, 1, 25, 10.0),
            WeightedItem(Items.BARLEY_SEED_5305, 1, 1, 5.0),
            WeightedItem(Items.JUTE_SEED_5306, 1, 1, 5.0),
            WeightedItem(Items.GRIMY_GUAM_200, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_MARRENTILL_202, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_TARROMIN_204, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_HARRALANDER_206, 1, 10, 10.0),
            WeightedItem(Items.GRIMY_RANARR_208, 1, 10, 10.0),
            WeightedItem(Items.HAMMERSTONE_SEED_5307, 1, 1, 5.0),
            WeightedItem(Items.ASGARNIAN_SEED_5308, 1, 1, 5.0),
            WeightedItem(Items.YANILLIAN_SEED_5309, 1, 1, 5.0),
            WeightedItem(Items.KRANDORIAN_SEED_5310, 1, 1, 5.0),
            WeightedItem(Items.WILDBLOOD_SEED_5311, 1, 1, 5.0),
            WeightedItem(Items.ACORN_5312, 1, 1, 5.0),
            WeightedItem(Items.WILLOW_SEED_5313, 1, 1, 5.0),
            WeightedItem(Items.MAPLE_SEED_5314, 1, 1, 5.0),
            WeightedItem(Items.YEW_SEED_5315, 1, 1, 5.0),
            WeightedItem(Items.MAGIC_SEED_5316, 1, 1, 5.0),
            WeightedItem(Items.SPIRIT_SEED_5317, 1, 1, 5.0),
            WeightedItem(Items.POTATO_SEED_5318, 1, 1, 5.0),
            WeightedItem(Items.ONION_SEED_5319, 1, 1, 5.0),
            WeightedItem(Items.SWEETCORN_SEED_5320, 1, 1, 5.0),
            WeightedItem(Items.WATERMELON_SEED_5321, 1, 1, 5.0),
            WeightedItem(Items.TOMATO_SEED_5322, 1, 1, 5.0),
            WeightedItem(Items.STRAWBERRY_SEED_5323, 1, 1, 5.0),
            WeightedItem(Items.CABBAGE_SEED_5324, 1, 1, 5.0),
            WeightedItem(Items.CACTUS_SEED_5280, 1, 1, 5.0),
            WeightedItem(Items.BELLADONNA_SEED_5281, 1, 1, 5.0),
            WeightedItem(Items.MUSHROOM_SPORE_5282, 1, 1, 5.0),
            WeightedItem(Items.APPLE_TREE_SEED_5283, 1, 1, 5.0),
            WeightedItem(Items.BANANA_TREE_SEED_5284, 1, 1, 5.0),
            WeightedItem(Items.ORANGE_TREE_SEED_5285, 1, 1, 5.0),
            WeightedItem(Items.CURRY_TREE_SEED_5286, 1, 1, 5.0),
            WeightedItem(Items.PINEAPPLE_SEED_5287, 1, 1, 5.0),
            WeightedItem(Items.PAPAYA_TREE_SEED_5288, 1, 1, 5.0),
            WeightedItem(Items.PALM_TREE_SEED_5289, 1, 1, 5.0),
            WeightedItem(Items.CALQUAT_TREE_SEED_5290, 1, 1, 5.0),
            WeightedItem(Items.GUAM_SEED_5291, 1, 1, 5.0),
            WeightedItem(Items.MARRENTILL_SEED_5292, 1, 1, 5.0),
            WeightedItem(Items.TARROMIN_SEED_5293, 1, 1, 5.0),
            WeightedItem(Items.HARRALANDER_SEED_5294, 1, 1, 5.0),
            WeightedItem(Items.RANARR_SEED_5295, 1, 1, 4.0),
            WeightedItem(Items.TOADFLAX_SEED_5296, 1, 1, 5.0),
            WeightedItem(Items.IRIT_SEED_5297, 1, 1, 3.0),
            WeightedItem(Items.AVANTOE_SEED_5298, 1, 1, 5.0),
            WeightedItem(Items.KWUARM_SEED_5299, 1, 1, 5.0),
            WeightedItem(Items.SNAPDRAGON_SEED_5300, 1, 1, 1.0),
            WeightedItem(Items.CADANTINE_SEED_5301, 1, 1, 5.0),
            WeightedItem(Items.LANTADYME_SEED_5302, 1, 1, 5.0),
            WeightedItem(Items.DWARF_WEED_SEED_5303, 1, 1, 3.0),
            WeightedItem(Items.TORSTOL_SEED_5304, 1, 1, 5.0),
        ),
    ),
    ;

    companion object {
        val rewardMap = HashMap<Int, WeightBasedTable>()

        init {
            WomTaskReward.values().forEach {
                it.npc.forEach { id -> rewardMap[id] = it.table }
            }
        }

        fun forId(id: Int): WeightBasedTable? {
            return rewardMap[id]
        }
    }
}
