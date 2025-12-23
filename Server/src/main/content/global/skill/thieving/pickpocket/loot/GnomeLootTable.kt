package content.global.skill.thieving.pickpocket.loot

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

object GnomeLootTable {

    val NPC_ID = intArrayOf(
        NPCs.GNOME_66,
        NPCs.GNOME_67,
        NPCs.GNOME_68,
        NPCs.GNOME_WOMAN_168,
        NPCs.GNOME_WOMAN_169,
        NPCs.GNOME_2249,
        NPCs.GNOME_2250,
        NPCs.GNOME_2251,
        NPCs.GNOME_2371,
        NPCs.GNOME_2649,
        NPCs.GNOME_2650,
        NPCs.GNOME_6002,
        NPCs.GNOME_6004
    )

    val LOOT = WeightBasedTable.create(
        WeightedItem(Items.ARROW_SHAFT_52, 2, 4, 56.0),
        WeightedItem(Items.COINS_995, 300, 300, 30.0),
        WeightedItem(Items.SWAMP_TOAD_2150, 1, 1, 24.0),
        WeightedItem(Items.GOLD_ORE_444, 1, 1, 8.0),
        WeightedItem(Items.EARTH_RUNE_557, 1, 1, 5.0),
        WeightedItem(Items.KING_WORM_2162, 1, 1, 3.0),
        WeightedItem(Items.FIRE_ORB_569, 1, 1, 2.0)
    ).insertMediumClue(128.0 / 150.0)
}