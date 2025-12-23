package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

// Shield of Arrav.
object CuratorHaigHelenLootTable {
    val NPC_ID = intArrayOf(NPCs.CURATOR_HAIG_HALEN_646)
    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.DISPLAY_CABINET_KEY_4617, 1, 1, 1.0, true)
    )
}

// Perils of Ice Mountain.
object DrorkarLootTable {
    val NPC_ID = intArrayOf(NPCs.CURATOR_HAIG_HALEN_646)
    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.DWARVEN_KEY_13234, 1, 1, 1.0, true)
    )
}

// Fairytale II - Cure a Queen.
object FairyGodFatherLootTable {
    val NPC_ID = intArrayOf(NPCs.FAIRY_GODFATHER_4433)
    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.QUEENS_SECATEURS_9020, 1, 1, 1.0, true)
    )
}

// Temple of Ikov.
object MovarioLootTable {
    val NPC_ID = intArrayOf(NPCs.MOVARIO_5825)
    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.PENDANT_OF_LUCIEN_86, 1, 1, 1.0, true)
    )
}

// Land of the Goblin.
enum class Priest(val npcId: Int, val loot: WeightBasedTable) {
    PRIEST_0(
        NPCs.PRIEST_6482,
        WeightBasedTable.create(WeightedItem(Items.EKELESHUUN_KEY_11795, 1, 1, 1.0, true))),
    PRIEST_1(
        NPCs.PRIEST_6483,
        WeightBasedTable.create(WeightedItem(Items.NAROGOSHUUN_KEY_11796, 1, 1, 1.0, true))),
    PRIEST_2(
        NPCs.PRIEST_6484,
        WeightBasedTable.create(WeightedItem(Items.HUZAMOGAARB_KEY_11797, 1, 1, 1.0, true))),
    PRIEST_3(
        NPCs.PRIEST_6485,
        WeightBasedTable.create(WeightedItem(Items.SARAGORGAK_KEY_11798, 1, 1, 1.0, true))),
    PRIEST_4(
        NPCs.PRIEST_6486,
        WeightBasedTable.create(WeightedItem(Items.HOROGOTHGAR_KEY_11799, 1, 1, 1.0, true))),
    PRIEST_5(
        NPCs.PRIEST_6487,
        WeightBasedTable.create(WeightedItem(Items.YURKOLGOKH_KEY_11800, 1, 1, 1.0, true)));
}

object UnknownLootTable {
    val NPC_ID = intArrayOf(
        5606,
        NPCs.GRINDXPLOX_8250,// 	Grindxplox	Thieving 1	0.0	unknown	unknown
        NPCs.GRINDXPLOX_8251
    )
    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.COINS_995, 1, 1, 1.0, true)
    )
}

/*

    Added separately.

    - 'BLACK-EYE'_5589
    - GUMMY'_5591
    - NO FINGERS'_5590
    - THE GUNS'_5592
    - BERRY_1129
    - GRINDXPLOX_8250
    - GRINDXPLOX_8251
    - NARF_3238
    - NULL_5606
    - SANDY_3112
    - SIGMUND_2082
    - STUDENT_617
    - TWIG_1128
    - ZEALOT_1528

*/