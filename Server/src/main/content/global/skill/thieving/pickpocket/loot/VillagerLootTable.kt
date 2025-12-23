package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object VillagerLootTable {

    val NPC_ID = intArrayOf(
        NPCs.VILLAGER_1888,
        NPCs.VILLAGER_1889,
        NPCs.VILLAGER_1890,
        NPCs.VILLAGER_1892,
        NPCs.VILLAGER_1893,
        NPCs.VILLAGER_1894,
        NPCs.VILLAGER_1896,
        NPCs.VILLAGER_1897,
        NPCs.VILLAGER_1898,
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 5, 5, 1.0, true)
    )
}