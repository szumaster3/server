package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object RogueLootTable {

    val NPC_ID = intArrayOf(
        NPCs.ROGUE_187,
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 25, 40, 1.171),
        WeightedItem(Items.AIR_RUNE_556, 8, 8, 16.0),
        WeightedItem(Items.JUG_OF_WINE_1993, 1, 1, 24.0),
        WeightedItem(Items.LOCKPICK_1523, 1, 1, 28.8),
        WeightedItem(Items.IRON_DAGGERP_1219, 1, 1, 144.0)
    )
}
