package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object FarmerLootTable {

    val NPC_ID = intArrayOf(
        NPCs.FARMER_7,
        NPCs.FARMER_1757,
        NPCs.FARMER_1758
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 9, 9, 1.0, true),
        WeightedItem(Items.POTATO_SEED_5318, 1, 1, 1.0, true)
    )
}
