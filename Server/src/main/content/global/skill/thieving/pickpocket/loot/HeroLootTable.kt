package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object HeroLootTable {

    val NPC_ID = intArrayOf(
        NPCs.HERO_21
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 200, 200, 105.0),
        WeightedItem(Items.DEATH_RUNE_560, 2, 2, 8.0),
        WeightedItem(Items.JUG_OF_WINE_1993, 1, 1, 6.0),
        WeightedItem(Items.BLOOD_RUNE_565, 1, 1, 5.0),
        WeightedItem(Items.FIRE_ORB_569, 1, 1, 2.0),
        WeightedItem(Items.DIAMOND_1601, 1, 1, 1.0),
        WeightedItem(Items.GOLD_ORE_444, 1, 1, 1.0)
    )
}