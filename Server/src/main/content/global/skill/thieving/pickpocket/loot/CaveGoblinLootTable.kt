package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object CaveGoblinLootTable {

    val NPC_ID = intArrayOf(
        NPCs.CAVE_GOBLIN_5752,
        NPCs.CAVE_GOBLIN_5753,
        NPCs.CAVE_GOBLIN_5754,
        NPCs.CAVE_GOBLIN_5755,
        NPCs.CAVE_GOBLIN_5756,
        NPCs.CAVE_GOBLIN_5757,
        NPCs.CAVE_GOBLIN_5758,
        NPCs.CAVE_GOBLIN_5759,
        NPCs.CAVE_GOBLIN_5760,
        NPCs.CAVE_GOBLIN_5761,
        NPCs.CAVE_GOBLIN_5762,
        NPCs.CAVE_GOBLIN_5763,
        NPCs.CAVE_GOBLIN_5764,
        NPCs.CAVE_GOBLIN_5765,
        NPCs.CAVE_GOBLIN_5766,
        NPCs.CAVE_GOBLIN_5767,
        NPCs.CAVE_GOBLIN_5768,
        NPCs.CAVE_GOBLIN_5769
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.BAT_SHISH_10964, 1, 1, 20.0),
        WeightedItem(Items.COATED_FROGS_LEGS_10963, 1, 1, 20.0),
        WeightedItem(Items.FINGERS_10965, 1, 1, 20.0),
        WeightedItem(Items.FROGBURGER_10962, 1, 1, 20.0),
        WeightedItem(Items.FROGSPAWN_GUMBO_10961, 1, 1, 20.0),
        WeightedItem(Items.GREEN_GLOOP_SOUP_10960, 1, 1, 20.0),
        WeightedItem(Items.COINS_995, 10, 50, 2.857),
        WeightedItem(Items.BULLSEYE_LANTERN_4544, 1, 1, 20.0),
        WeightedItem(Items.CAVE_GOBLIN_WIRE_10981, 1, 2, 20.0),
        WeightedItem(Items.IRON_ORE_441, 1, 4, 20.0),
        WeightedItem(Items.OIL_LAMP_4522, 1, 1, 20.0),
        WeightedItem(Items.SWAMP_TAR_1939, 1, 1, 20.0),
        WeightedItem(Items.TINDERBOX_590, 1, 1, 20.0),
        WeightedItem(Items.UNLIT_TORCH_596, 1, 1, 20.0)
    )
}
