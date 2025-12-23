package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object DesertBanditLootTable {

    val NPC_ID = intArrayOf(
        NPCs.BANDIT_1926,
        NPCs.BANDIT_1931
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 30, 30, 5.0),
        WeightedItem(Items.ANTIPOISON1_179, 1, 1, 1.0),
        WeightedItem(Items.LOCKPICK_1523, 1, 1, 1.0)
    )
}