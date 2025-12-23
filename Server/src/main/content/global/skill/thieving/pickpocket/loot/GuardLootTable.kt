package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object GuardLootTable {

    val NPC_ID = intArrayOf(
        NPCs.GUARD_9,
        NPCs.GUARD_32,
        NPCs.GUARD_296,
        NPCs.GUARD_297,
        NPCs.GUARD_298,
        NPCs.GUARD_299,
        NPCs.GUARD_2699,
        NPCs.GUARD_2700,
        NPCs.GUARD_2701,
        NPCs.GUARD_2702,
        NPCs.GUARD_2703,
        NPCs.GUARD_3228,
        NPCs.GUARD_3229,
        NPCs.GUARD_3230,
        NPCs.GUARD_3231,
        NPCs.GUARD_3232,
        NPCs.GUARD_3233,
        NPCs.GUARD_3241,
        NPCs.GUARD_3407,
        NPCs.GUARD_3408,
        NPCs.GUARD_4307,
        NPCs.GUARD_4308,
        NPCs.GUARD_4309,
        NPCs.GUARD_4310,
        NPCs.GUARD_4311,
        NPCs.GUARD_5919,
        NPCs.GUARD_5920,
        NPCs.GUARD_8173
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 30, 30, 1.0, true)
    )
}