package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object WatchmanLootTable {

    val NPC_ID = intArrayOf(
        NPCs.WATCHMAN_34
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 60, 60, 1.0, true),
        WeightedItem(Items.BREAD_2309, 1, 1, 1.0, true)
    )
}