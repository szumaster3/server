package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object HamMemberLootTable {

    val NPC_ID_MALE = intArrayOf(
        NPCs.HAM_MEMBER_1714
    )

    val NPC_ID_FEMALE = intArrayOf(
        NPCs.HAM_MEMBER_1715
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.BOOTS_4310, 1, 1, 0.25),
        WeightedItem(Items.HAM_CLOAK_4304, 1, 1, 0.25),
        WeightedItem(Items.GLOVES_4308, 1, 1, 0.25),
        WeightedItem(Items.HAM_HOOD_4302, 1, 1, 0.25),
        WeightedItem(Items.HAM_LOGO_4306, 1, 1, 0.25),
        WeightedItem(Items.HAM_ROBE_4300, 1, 1, 0.25),
        WeightedItem(Items.HAM_SHIRT_4298, 1, 1, 0.25),
        WeightedItem(Items.COINS_995, 1, 21, 5.5),
        WeightedItem(Items.BRONZE_ARROW_882, 1, 13, 3.0 / 102),
        WeightedItem(Items.BRONZE_AXE_1351, 1, 1, 3.0 / 102),
        WeightedItem(Items.BRONZE_DAGGER_1205, 1, 1, 3.0 / 102),
        WeightedItem(Items.BRONZE_PICKAXE_1265, 1, 1, 3.0 / 102),
        WeightedItem(Items.IRON_AXE_1349, 1, 1, 3.0 / 102),
        WeightedItem(Items.IRON_DAGGERP_1219, 1, 1, 3.0 / 102),
        WeightedItem(Items.IRON_PICKAXE_1267, 1, 1, 3.0 / 102),
        WeightedItem(Items.LEATHER_BODY_1129, 1, 1, 3.0 / 102),
        WeightedItem(Items.STEEL_ARROW_886, 1, 13, 2.0 / 102),
        WeightedItem(Items.STEEL_AXE_1353, 1, 1, 2.0 / 102),
        WeightedItem(Items.STEEL_DAGGER_1207, 1, 1, 2.0 / 102),
        WeightedItem(Items.STEEL_PICKAXE_1269, 1, 1, 2.0 / 102),
        WeightedItem(Items.BUTTONS_688, 1, 1, 4.0 / 102),
        WeightedItem(Items.DAMAGED_ARMOUR_697, 1, 1, 4.0 / 102),
        WeightedItem(Items.RUSTY_SWORD_686, 1, 1, 4.0 / 102),
        WeightedItem(Items.FEATHER_314, 1, 7, 3.0 / 102),
        WeightedItem(Items.LOGS_1511, 1, 1, 3.0 / 102),
        WeightedItem(Items.THREAD_1734, 1, 10, 3.0 / 102),
        WeightedItem(Items.COWHIDE_1739, 1, 1, 3.0 / 102),
        WeightedItem(Items.KNIFE_946, 1, 1, 2.0 / 102),
        WeightedItem(Items.NEEDLE_1733, 1, 1, 2.0 / 102),
        WeightedItem(Items.RAW_ANCHOVIES_321, 1, 1, 2.0 / 102),
        WeightedItem(Items.RAW_CHICKEN_2138, 1, 1, 2.0 / 102),
        WeightedItem(Items.TINDERBOX_590, 1, 1, 2.0 / 102),
        WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 2.0 / 102),
        WeightedItem(Items.COAL_453, 1, 1, 2.0 / 102),
        WeightedItem(Items.IRON_ORE_441, 1, 1, 2.0 / 102),
        WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 2.0 / 102),
        WeightedItem(Items.GRIMY_GUAM_199, 1, 1, 6.0 / 561),
        WeightedItem(Items.GRIMY_MARRENTILL_201, 1, 1, 3.0 / 561),
        WeightedItem(Items.GRIMY_TARROMIN_203, 1, 1, 2.0 / 561)
    ).insertEasyClue(1.0)
}