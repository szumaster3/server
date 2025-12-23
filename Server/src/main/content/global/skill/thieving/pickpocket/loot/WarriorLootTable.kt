package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object WarriorLootTable {

    val NPC_ID = intArrayOf(
        NPCs.WARRIOR_WOMAN_15,
        NPCs.AL_KHARID_WARRIOR_18
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 18, 18, 1.0, true)
    )
}