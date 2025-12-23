package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object HumanLootTable {

    val NPC_ID = intArrayOf(
        // Man
        NPCs.MAN_1, NPCs.MAN_2, NPCs.MAN_3, NPCs.MAN_16, NPCs.MAN_24, NPCs.MAN_170, NPCs.MAN_1086, NPCs.MAN_2675, NPCs.MAN_3223, NPCs.MAN_3224, NPCs.MAN_3225, NPCs.MAN_3915, NPCs.MAN_5923, NPCs.MAN_7873, NPCs.MAN_7874, NPCs.MAN_7875, NPCs.MAN_7876, NPCs.MAN_7877, NPCs.MAN_7878, NPCs.MAN_7879,
        // Woman
        NPCs.WOMAN_4, NPCs.WOMAN_5, NPCs.WOMAN_6, NPCs.WOMAN_25, NPCs.WOMAN_3226, NPCs.WOMAN_3227, NPCs.WOMAN_5924, NPCs.WOMAN_7880, NPCs.WOMAN_7881, NPCs.WOMAN_7882, NPCs.WOMAN_7883, NPCs.WOMAN_7884,
        // Others
        NPCs.HENGEL_2683, NPCs.ANJA_2684, NPCs.DRUNKEN_MAN_3222, NPCs.CUFFS_3237, NPCs.NARF_3238, NPCs.RUSTY_3239, NPCs.JEFF_3240,
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 3, 3, 1.0, true)
    )
}