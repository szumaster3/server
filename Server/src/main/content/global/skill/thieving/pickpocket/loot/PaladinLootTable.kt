package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object PaladinLootTable {

    val NPC_ID = intArrayOf(
        NPCs.PALADIN_20,
        NPCs.PALADIN_2256
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 80, 80, 1.0, true),
        WeightedItem(Items.CHAOS_RUNE_562, 2, 2, 1.0, true)
    ).insertHardClue(1.0 / 1000.0)
}
