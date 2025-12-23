package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object MasterFarmerLootTable {

    val NPC_ID = intArrayOf(
        NPCs.MASTER_FARMER_2234,
        NPCs.MASTER_FARMER_2235,
        NPCs.MARTIN_THE_MASTER_GARDENER_3299
    )

    private val ALLOTMENTS = WeightBasedTable.create(
        WeightedItem(Items.POTATO_SEED_5318, 1, 4, 177.0),
        WeightedItem(Items.ONION_SEED_5319, 1, 3, 133.0),
        WeightedItem(Items.SWEETCORN_SEED_5320, 1, 2, 28.3),
        WeightedItem(Items.WATERMELON_SEED_5321, 1, 1, 6.7),
        WeightedItem(Items.TOMATO_SEED_5322, 1, 2, 63.7),
        WeightedItem(Items.STRAWBERRY_SEED_5323, 1, 1, 14.2),
        WeightedItem(Items.CABBAGE_SEED_5324, 1, 3, 69.4),
    )

    private val HOPS = WeightBasedTable.create(
        WeightedItem(Items.BARLEY_SEED_5305, 1, 12, 55.6),
        WeightedItem(Items.HAMMERSTONE_SEED_5307, 1, 9, 55.6),
        WeightedItem(Items.ASGARNIAN_SEED_5308, 1, 6, 41.8),
        WeightedItem(Items.JUTE_SEED_5306, 1, 9, 41.5),
        WeightedItem(Items.YANILLIAN_SEED_5309, 1, 6, 27.7),
        WeightedItem(Items.KRANDORIAN_SEED_5310, 1, 6, 13.9),
        WeightedItem(Items.WILDBLOOD_SEED_5311, 1, 3, 7.0)
    )

    private val FLOWERS = WeightBasedTable.create(
        WeightedItem(Items.MARIGOLD_SEED_5096, 1, 1, 45.9),
        WeightedItem(Items.ROSEMARY_SEED_5097, 1, 1, 19.6),
        WeightedItem(Items.NASTURTIUM_SEED_5098, 1, 1, 30.4),
        WeightedItem(Items.WOAD_SEED_5099, 1, 1, 14.5),
        WeightedItem(Items.LIMPWURT_SEED_5100, 1, 1, 11.6)
    )

    private val BUSHES = WeightBasedTable.create(
        WeightedItem(Items.REDBERRY_SEED_5101, 1, 1, 10.3),
        WeightedItem(Items.CADAVABERRY_SEED_5102, 1, 1, 7.2),
        WeightedItem(Items.DWELLBERRY_SEED_5103, 1, 1, 5.0),
        WeightedItem(Items.JANGERBERRY_SEED_5104, 1, 1, 0.77),
        WeightedItem(Items.WHITEBERRY_SEED_5105, 1, 1, 0.28),
        WeightedItem(Items.POISON_IVY_SEED_5106, 1, 1, 0.053)
    )

    private val SPECIAL = WeightBasedTable.create(
        WeightedItem(Items.MUSHROOM_SPORE_5282, 1, 1, 0.203),
        WeightedItem(Items.BELLADONNA_SEED_5281, 1, 1, 0.122),
        WeightedItem(Items.CACTUS_SEED_5280, 1, 1, 0.081),
        WeightedItem(Items.POTATO_CACTUS_3138, 1, 1, 0.0204),
    )

    private val HERBS = WeightBasedTable.create(
        WeightedItem(Items.GUAM_SEED_5291, 1, 1, 0.0171),
        WeightedItem(Items.MARRENTILL_SEED_5292, 1, 1, 0.0105),
        WeightedItem(Items.TARROMIN_SEED_5293, 1, 1, 0.0071),
        WeightedItem(Items.HARRALANDER_SEED_5294, 1, 1, 0.00485),
        WeightedItem(Items.RANARR_SEED_5295, 1, 1, 0.00372),
        WeightedItem(Items.TOADFLAX_SEED_5296, 1, 1, 0.00226),
        WeightedItem(Items.IRIT_SEED_5297, 1, 1, 0.00154),
        WeightedItem(Items.AVANTOE_SEED_5298, 1, 1, 0.00106),
        WeightedItem(Items.KWUARM_SEED_5299, 1, 1, 0.00072),
        WeightedItem(Items.SNAPDRAGON_SEED_5300, 1, 1, 0.00054),
        WeightedItem(Items.CADANTINE_SEED_5301, 1, 1, 0.00034),
        WeightedItem(Items.LANTADYME_SEED_5302, 1, 1, 0.00024),
        WeightedItem(Items.DWARF_WEED_SEED_5303, 1, 1, 0.000144),
        WeightedItem(Items.TORSTOL_SEED_5304, 1, 1, 0.000108)
    )

    val LOOT: WeightBasedTable by lazy {
        WeightBasedTable().apply {
            addAll(content.global.skill.thieving.pickpocket.loot.MasterFarmerLootTable.ALLOTMENTS)
            addAll(content.global.skill.thieving.pickpocket.loot.MasterFarmerLootTable.HOPS)
            addAll(content.global.skill.thieving.pickpocket.loot.MasterFarmerLootTable.FLOWERS)
            addAll(content.global.skill.thieving.pickpocket.loot.MasterFarmerLootTable.BUSHES)
            addAll(content.global.skill.thieving.pickpocket.loot.MasterFarmerLootTable.SPECIAL)
            addAll(content.global.skill.thieving.pickpocket.loot.MasterFarmerLootTable.HERBS)
        }
    }
}
