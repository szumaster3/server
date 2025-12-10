package content.global.skill.thieving

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import shared.consts.Items
import shared.consts.NPCs

/**
 * Contains pickpocket thieving loot tables for NPCs.
 */
object ThievingLootTable {

    // Man
    val MAN_IDS = intArrayOf(NPCs.MAN_1, NPCs.MAN_2, NPCs.MAN_3, NPCs.WOMAN_4, NPCs.WOMAN_5, NPCs.WOMAN_6, NPCs.MAN_16, NPCs.MAN_24, NPCs.WOMAN_25, NPCs.MAN_170, NPCs.MAN_1086, NPCs.MAN_2675, NPCs.MAN_3224, NPCs.MAN_3225, NPCs.WOMAN_3227, NPCs.MAN_5923, NPCs.WOMAN_5924, NPCs.MAN_7873, NPCs.MAN_7874, NPCs.MAN_7875, NPCs.MAN_7876, NPCs.MAN_7877, NPCs.MAN_7878, NPCs.MAN_7879, NPCs.WOMAN_7880, NPCs.WOMAN_7881, NPCs.WOMAN_7882, NPCs.WOMAN_7883, NPCs.WOMAN_7884, NPCs.WOMAN_7925, NPCs.HENGEL_2683, NPCs.ANJA_2684)
    val MAN_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 3, 3, 1.0, true))

    // Curator Haig Helen
    val CURATOR_HAIG_HELEN_IDS = intArrayOf(NPCs.CURATOR_HAIG_HALEN_646)
    val CURATOR_HAIG_HELEN_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.DISPLAY_CABINET_KEY_4617, 1, 1, 1.0, true))

    // Gang of thieves
    val GANG_OF_THIEVE_IDS = intArrayOf(NPCs.CUFFS_3237, NPCs.NARF_3238, NPCs.RUSTY_3239, NPCs.JEFF_3240)
    val GANG_OF_THIEVE_PICKPOCKET_LOOT = MAN_PICKPOCKET_LOOT

    // Farmer
    val FARMER_IDS = intArrayOf(NPCs.FARMER_7, NPCs.FARMER_1757, NPCs.FARMER_1758)
    val FARMER_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 9, 9, 1.0, true), WeightedItem(Items.POTATO_SEED_5318, 1, 1, 1.0, true))

    // Ham member
    val HAM_MALE_IDS = intArrayOf(NPCs.HAM_MEMBER_1714)
    val HAM_FEMALE_IDS = intArrayOf(NPCs.HAM_MEMBER_1715)
    val HAM_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 1, 21, 5.5), WeightedItem(Items.TINDERBOX_590, 1, 1, 5.0), WeightedItem(Items.LOGS_1511, 1, 1, 7.0), WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 2.5), WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 2.5), WeightedItem(Items.RAW_ANCHOVIES_321, 1, 1, 7.0), WeightedItem(Items.RAW_CHICKEN_2138, 1, 1, 3.5), WeightedItem(Items.HAM_CLOAK_4304, 1, 1, 0.25), WeightedItem(Items.HAM_HOOD_4302, 1, 1, 0.25), WeightedItem(Items.HAM_LOGO_4306, 1, 1, 0.25), WeightedItem(Items.HAM_ROBE_4300, 1, 1, 0.25), WeightedItem(Items.HAM_SHIRT_4298, 1, 1, 0.25), WeightedItem(Items.BOOTS_4310, 1, 1, 1.0), WeightedItem(Items.GLOVES_4308, 1, 1, 1.0), WeightedItem(Items.BRONZE_PICKAXE_1265, 1, 1, 5.0), WeightedItem(Items.IRON_PICKAXE_1267, 1, 1, 5.0), WeightedItem(Items.STEEL_PICKAXE_1269, 1, 1, 2.5), WeightedItem(Items.GRIMY_GUAM_199, 1, 1, 2.0), WeightedItem(Items.GRIMY_HARRALANDER_205, 1, 1, 2.0), WeightedItem(Items.GRIMY_KWUARM_213, 1, 1, 2.0), WeightedItem(Items.GRIMY_MARRENTILL_201, 1, 1, 1.5), WeightedItem(Items.RUSTY_SWORD_686, 1, 1, 3.5), WeightedItem(Items.BROKEN_ARMOUR_698, 1, 1, 3.5), WeightedItem(Items.BROKEN_STAFF_689, 1, 1, 3.2), WeightedItem(Items.BROKEN_ARROW_687, 1, 1, 3.1), WeightedItem(Items.BUTTONS_688, 1, 1, 3.0)).insertEasyClue(1.0)

    // Warrior
    val WARRIOR_IDS = intArrayOf(NPCs.WARRIOR_WOMAN_15, NPCs.AL_KHARID_WARRIOR_18)
    val WARRIOR_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 18, 18, 1.0, true))

    // Villager
    val VILLAGER_IDS = intArrayOf(1887, NPCs.VILLAGER_1888, NPCs.VILLAGER_1889, NPCs.VILLAGER_1890, NPCs.VILLAGER_1891, NPCs.VILLAGER_1892, NPCs.VILLAGER_1893, NPCs.VILLAGER_1894, NPCs.VILLAGER_1895, NPCs.VILLAGER_1896, NPCs.VILLAGER_1897, NPCs.VILLAGER_1898, NPCs.VILLAGER_1899, NPCs.VILLAGER_1900)
    val VILLAGER_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 5, 5, 1.0, true))

    // Rogues
    val ROGUE_IDS = intArrayOf(NPCs.ROGUE_187, NPCs.ROGUE_GUARD_2267, NPCs.ROGUE_GUARD_2268, NPCs.ROGUE_GUARD_2269, NPCs.ROGUE_8122)
    val ROGUE_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 25, 40, 5.0, true), WeightedItem(Items.JUG_OF_WINE_1993, 1, 1, 6.0), WeightedItem(Items.AIR_RUNE_556, 8, 8, 8.0), WeightedItem(Items.LOCKPICK_1523, 1, 1, 5.0), WeightedItem(Items.IRON_DAGGERP_1219, 1, 1, 1.0))

    // Cave goblin
    val CAVE_GOBLIN_IDS = intArrayOf(NPCs.CAVE_GOBLIN_5752, NPCs.CAVE_GOBLIN_5768)
    val CAVE_GOBLIN_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.BAT_SHISH_10964, 1, 1, 2.5), WeightedItem(Items.FINGERS_10965, 1, 1, 2.5), WeightedItem(Items.COATED_FROGS_LEGS_10963, 1, 1, 2.5), WeightedItem(Items.COINS_995, 30, 30, 6.5), WeightedItem(Items.OIL_LAMP_4522, 1, 1, 0.5), WeightedItem(Items.BULLSEYE_LANTERN_4544, 1, 1, 0.5), WeightedItem(Items.UNLIT_TORCH_596, 1, 1, 0.5), WeightedItem(Items.TINDERBOX_590, 1, 1, 0.5), WeightedItem(Items.SWAMP_TAR_1939, 1, 1, 0.5), WeightedItem(Items.IRON_ORE_441, 1, 4, 0.25))

    // Master farmer
    val MASTER_FARMER_IDS = intArrayOf(NPCs.MASTER_FARMER_2234, NPCs.MASTER_FARMER_2235, NPCs.MARTIN_THE_MASTER_GARDENER_3299)
    val MASTER_FARMER_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.POTATO_SEED_5318, 1, 3, 50.0), WeightedItem(Items.ONION_SEED_5319, 1, 3, 50.0), WeightedItem(Items.CABBAGE_SEED_5324, 1, 3, 50.0), WeightedItem(Items.TOMATO_SEED_5322, 1, 2, 50.0), WeightedItem(Items.SWEETCORN_SEED_5320, 1, 2, 50.0), WeightedItem(Items.STRAWBERRY_SEED_5323, 1, 1, 25.0), WeightedItem(Items.WATERMELON_SEED_5321, 1, 1, 8.0), WeightedItem(Items.BARLEY_SEED_5305, 1, 4, 50.0), WeightedItem(Items.HAMMERSTONE_SEED_5307, 1, 3, 50.0), WeightedItem(Items.ASGARNIAN_SEED_5308, 1, 3, 50.0), WeightedItem(Items.JUTE_SEED_5306, 1, 3, 50.0), WeightedItem(Items.YANILLIAN_SEED_5309, 1, 2, 25.0), WeightedItem(Items.KRANDORIAN_SEED_5310, 1, 2, 25.0), WeightedItem(Items.WILDBLOOD_SEED_5311, 1, 1, 8.0), WeightedItem(Items.MARIGOLD_SEED_5096, 1, 1, 50.0), WeightedItem(Items.NASTURTIUM_SEED_5098, 1, 1, 50.0), WeightedItem(Items.ROSEMARY_SEED_5097, 1, 1, 50.0), WeightedItem(Items.WOAD_SEED_5099, 1, 1, 50.0), WeightedItem(Items.LIMPWURT_SEED_5100, 1, 1, 25.0), WeightedItem(Items.REDBERRY_SEED_5101, 1, 1, 50.0), WeightedItem(Items.CADAVABERRY_SEED_5102, 1, 1, 50.0), WeightedItem(Items.DWELLBERRY_SEED_5103, 1, 1, 25.0), WeightedItem(Items.JANGERBERRY_SEED_5104, 1, 1, 25.0), WeightedItem(Items.WHITEBERRY_SEED_5105, 1, 1, 25.0), WeightedItem(Items.GUAM_SEED_5291, 1, 1, 50.0), WeightedItem(Items.MARRENTILL_SEED_5292, 1, 1, 50.0), WeightedItem(Items.TARROMIN_SEED_5293, 1, 1, 50.0), WeightedItem(Items.HARRALANDER_SEED_5294, 1, 1, 25.0), WeightedItem(Items.RANARR_SEED_5295, 1, 1, 8.0), WeightedItem(Items.TOADFLAX_SEED_5296, 1, 1, 8.0), WeightedItem(Items.IRIT_SEED_5297, 1, 1, 8.0), WeightedItem(Items.AVANTOE_SEED_5298, 1, 1, 8.0), WeightedItem(Items.KWUARM_SEED_5299, 1, 1, 8.0), WeightedItem(Items.SNAPDRAGON_SEED_5300, 1, 1, 5.0), WeightedItem(Items.CADANTINE_SEED_5301, 1, 1, 8.0), WeightedItem(Items.LANTADYME_SEED_5302, 1, 1, 5.0), WeightedItem(Items.DWARF_WEED_SEED_5303, 1, 1, 5.0), WeightedItem(Items.TORSTOL_SEED_5304, 1, 1, 5.0))

    // Guards
    val GUARD_IDS = intArrayOf(NPCs.MASTER_FARMER_2234, NPCs.MASTER_FARMER_2235, NPCs.MARTIN_THE_MASTER_GARDENER_3299)
    val GUARD_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 30, 30, 1.0, true))

    // Fremennik citizens
    val FREMENNIK_IDS = intArrayOf(NPCs.AGNAR_1305, NPCs.FREIDIR_1306, NPCs.BORROKAR_1307, NPCs.LANZIG_1308, NPCs.PONTAK_1309, NPCs.FREYGERD_1310, NPCs.LENSA_1311, NPCs.JENNELLA_1312, NPCs.SASSILIK_1313, NPCs.INGA_1314)
    val FREMENNIK_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 40, 40, 1.0, true))

    // Bearded bandit
    val BEARDED_BANDIT_IDS = intArrayOf(NPCs.BANDIT_1880, NPCs.BANDIT_1881, NPCs.BANDIT_6174, NPCs.BANDIT_6388)
    val BEARDED_BANDIT_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.ANTIPOISON4_2446, 1, 1, 1.0), WeightedItem(Items.LOCKPICK_1523, 1, 1, 2.0), WeightedItem(Items.COINS_995, 1, 1, 4.0))

    // Desert bandit
    val DESERT_BANDIT_IDS = intArrayOf(NPCs.BANDIT_1926, NPCs.BARTENDER_1921)
    val DESERT_BANDIT_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 50, 1, 3.0), WeightedItem(Items.ANTIPOISON4_2446, 1, 1, 1.0), WeightedItem(Items.LOCKPICK_1523, 1, 1, 1.0))

    // Knight of Ardougne
    val KNIGHT_OF_ADROUGNE_IDS = intArrayOf(NPCs.KNIGHT_OF_ARDOUGNE_23, NPCs.KNIGHT_OF_ARDOUGNE_26)
    val KNIGHT_OF_ADROUGNE_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 50, 50, 1.0, true))

    // Yanille guard
    val YANILLE_WATCHMAN_IDS = intArrayOf(NPCs.WATCHMAN_34)
    val YANILLE_WATCHMAN_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 60, 60, 1.0, true), WeightedItem(Items.BREAD_2309, 1, 1, 1.0, true))

    // Menaphite Thug
    val MENAPHITE_THUG_IDS = intArrayOf(NPCs.MENAPHITE_THUG_1905)
    val MENAPHITE_THUG_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 60, 60, 1.0, true))

    // Paladin
    val PALADIN_IDS = intArrayOf(NPCs.PALADIN_20, NPCs.PALADIN_2256)
    val PALADIN_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 80, 80, 1.0, true), WeightedItem(Items.CHAOS_RUNE_562, 2, 2, 1.0, true))

    // Gnome
    val GNOME_IDS = intArrayOf(NPCs.GNOME_66, NPCs.GNOME_67, NPCs.GNOME_68, NPCs.GNOME_WOMAN_168, NPCs.GNOME_WOMAN_169, NPCs.GNOME_2249, NPCs.GNOME_2250, NPCs.GNOME_2251, NPCs.GNOME_2371, NPCs.GNOME_2649, NPCs.GNOME_2650, NPCs.GNOME_6002, NPCs.GNOME_6004)
    val GNOME_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 300, 300, 2.5), WeightedItem(Items.EARTH_RUNE_557, 1, 1, 3.5), WeightedItem(Items.GOLD_ORE_445, 1, 1, 1.0), WeightedItem(Items.FIRE_ORB_569, 1, 1, 5.0), WeightedItem(Items.SWAMP_TOAD_2150, 1, 1, 8.0), WeightedItem(Items.KING_WORM_2162, 1, 1, 9.0))

    // Hero
    val HERO_IDS = intArrayOf(NPCs.HERO_21)
    val HERO_PICKPOCKET_LOOT = WeightBasedTable.create(WeightedItem(Items.COINS_995, 200, 300, 1.5), WeightedItem(Items.DEATH_RUNE_560, 2, 2, 1.0), WeightedItem(Items.BLOOD_RUNE_565, 1, 1, 0.5), WeightedItem(Items.FIRE_ORB_569, 1, 1, 2.5), WeightedItem(Items.DIAMOND_1601, 1, 1, 2.0), WeightedItem(Items.GOLD_ORE_444, 1, 1, 1.5), WeightedItem(Items.JUG_OF_WINE_1993, 1, 1, 3.0))
}
