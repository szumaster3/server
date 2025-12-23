package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object FremennikCitizenLootTable {

    val NPC_ID = intArrayOf(
        NPCs.AGNAR_1305,
        NPCs.FREIDIR_1306,
        NPCs.BORROKAR_1307,
        NPCs.LANZIG_1308,
        NPCs.PONTAK_1309,
        NPCs.FREYGERD_1310,
        NPCs.LENSA_1311,
        NPCs.JENNELLA_1312,
        NPCs.SASSILIK_1313,
        NPCs.INGA_1314
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 40, 40, 1.0, true)
    )
}