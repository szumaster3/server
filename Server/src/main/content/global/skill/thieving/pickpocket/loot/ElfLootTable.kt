package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object ElfLootTable {

    val NPC_ID = intArrayOf(
        NPCs.ARVEL_2365,
        NPCs.ARVEL_7433,
        NPCs.GOREU_2363,
        NPCs.GOREU_7431,
        NPCs.KELYN_2367,
        NPCs.KELYN_7435,
        NPCs.MAWRTH_2366,
        NPCs.MAWRTH_7434,
        NPCs.YSGAWYN_2364,
        NPCs.YSGAWYN_7432
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 280, 350, 105.0),
        WeightedItem(Items.DEATH_RUNE_560, 2, 2, 8.0),
        WeightedItem(Items.JUG_OF_WINE_1993, 1, 1, 6.0),
        WeightedItem(Items.NATURE_RUNE_561, 3, 3, 5.0),
        WeightedItem(Items.FIRE_ORB_569, 1, 1, 2.0),
        WeightedItem(Items.DIAMOND_1601, 1, 1, 1.0),
        WeightedItem(Items.GOLD_ORE_444, 1, 1, 1.0)
    )
}