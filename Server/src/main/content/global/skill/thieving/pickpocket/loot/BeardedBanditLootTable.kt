package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object BeardedBanditLootTable {

    val NPC_ID = intArrayOf(
        NPCs.BANDIT_6174,
        NPCs.BANDIT_6388
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.ANTIPOISON1_179, 1, 1, 1.0),
        WeightedItem(Items.LOCKPICK_1523, 1, 1, 2.0),
        WeightedItem(Items.COINS_995, 1, 1, 4.0)
    )
}