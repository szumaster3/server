package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object PollnivnianBanditLootTable {

    val NPC_ID = intArrayOf(
        NPCs.BANDIT_1880,
        NPCs.BANDIT_1881
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 50, 50, 1.0, true)
    )
}