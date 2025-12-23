package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object KnightLootTable {

    val NPC_ID = intArrayOf(
        NPCs.KNIGHT_OF_ARDOUGNE_23,
        NPCs.KNIGHT_OF_ARDOUGNE_26
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 50, 50, 1.0, true)
    )
}