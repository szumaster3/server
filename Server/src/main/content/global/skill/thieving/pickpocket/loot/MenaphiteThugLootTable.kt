package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object MenaphiteThugLootTable {

    val NPC_ID = intArrayOf(
        NPCs.MENAPHITE_THUG_1904,
        NPCs.MENAPHITE_THUG_1905
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 60, 60, 1.0, true)
    )
}