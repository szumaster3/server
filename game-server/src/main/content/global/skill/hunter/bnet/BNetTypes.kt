package content.global.skill.hunter.bnet

import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.item.ChanceItem
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Butterfly net types.
 */
enum class BNetTypes(
    val node: BNetNode,
) {
    RUBY_HARVEST(
        BNetNode(
            intArrayOf(NPCs.RUBY_HARVEST_5085),
            intArrayOf(15, 80, 75),
            doubleArrayOf(24.0, 300.0, 50.0),
            arrayOf(
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_RED_913),
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_RED_917),
            ),
            ChanceItem(Items.RUBY_HARVEST_10020),
        ),
    ),
    SAPPHIRE_GLACIALIS(
        BNetNode(
            intArrayOf(NPCs.SAPPHIRE_GLACIALIS_5084, NPCs.SAPPHIRE_GLACIALIS_7499),
            intArrayOf(25, 85, 80),
            doubleArrayOf(34.0, 400.0, 70.0),
            arrayOf(
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_BLUE_912),
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_BLUE_916),
            ),
            ChanceItem(Items.SAPPHIRE_GLACIALIS_10018),
        ),
    ),
    SNOWY_KNIGHT(
        BNetNode(
            intArrayOf(NPCs.SNOWY_KNIGHT_5083, NPCs.SNOWY_KNIGHT_7498),
            intArrayOf(35, 90, 85),
            doubleArrayOf(44.0, 500.0, 100.0),
            arrayOf(
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_WHITE_911),
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_WHITE_915),
            ),
            ChanceItem(Items.SNOWY_KNIGHT_10016),
        ),
    ),
    BLACK_WARLOCK(
        BNetNode(
            intArrayOf(NPCs.BLACK_WARLOCK_5082),
            intArrayOf(45, 95, 90),
            doubleArrayOf(54.0, 650.0, 125.0),
            arrayOf(
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_BLACK_910),
                Graphics(org.rs.consts.Graphics.RELEASE_A_BUTTERFLY_BLACK_914),
            ),
            ChanceItem(Items.BLACK_WARLOCK_10014),
        ),
    ),
    BABY_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.BABY_IMPLING_1028, NPCs.BABY_IMPLING_6055),
            17,
            20.0,
            18.0,
            Item(Items.BABY_IMPLING_JAR_11238),
            ChanceItem(Items.CHISEL_1755, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.THREAD_1734, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.NEEDLE_1733, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.KNIFE_946, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.CHEESE_1985, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.HAMMER_2347, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BALL_OF_WOOL_1759, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BUCKET_OF_MILK_1927, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.ANCHOVIES_319, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SPICE_2007, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SPICE_2007, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.FLAX_1779, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.MUD_PIE_7170, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SEAWEED_401, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.AIR_TALISMAN_1438, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SILVER_BAR_2355, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.SAPPHIRE_1607, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.HARD_LEATHER_1743, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.LOBSTER_379, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.SOFT_CLAY_1761, 1, 1, DropFrequency.RARE),
        ),
    ),
    YOUNG_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.YOUNG_IMPLING_1029, NPCs.YOUNG_IMPLING_6056),
            22,
            22.0,
            20.0,
            Item(Items.YOUNG_IMPLING_JAR_11240),
            ChanceItem(Items.STEEL_NAILS_1539, 5, 5, DropFrequency.COMMON),
            ChanceItem(Items.LOCKPICK_1523, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PURE_ESSENCE_7936, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.TUNA_361, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.CHOCOLATE_SLICE_1901, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.MEAT_PIZZA_2293, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.COAL_453, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BOW_STRING_1777, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STEEL_AXE_1353, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STEEL_FULL_HELM_1157, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.SNAPE_GRASS_231, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SOFT_CLAY_1761, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.STUDDED_CHAPS_1097, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.OAK_PLANK_8778, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.DEFENCE_POTION3_133, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.YEW_LONGBOW_855, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.GARDEN_PIE_7178, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.JANGERBERRIES_247, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.MITHRIL_BAR_2359, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.CURRY_LEAF_5970, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BUCKET_OF_SAND_1784, 4, 4, DropFrequency.UNCOMMON),
        ),
    ),
    GOURMET_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.GOURMET_IMPLING_1030, NPCs.GOURMET_IMPLING_6057),
            28,
            24.0,
            22.0,
            Item(Items.GOURM_IMPLING_JAR_11242),
            ChanceItem(Items.TUNA_361, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BASS_365, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.CURRY_2011, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.MEAT_PIE_2327, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.CHOCOLATE_CAKE_1897, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.FROG_SPAWN_5004, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SPICE_2007, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.CURRY_LEAF_5970, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.LOBSTER_380, 4, 4, DropFrequency.RARE),
            ChanceItem(Items.UGTHANKI_KEBAB_1883, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.SHARK_386, 3, 3, DropFrequency.RARE),
            ChanceItem(Items.CHEFS_DELIGHT_5755, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.FISH_PIE_7188, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.RAINBOW_FISH_10137, 5, 5, DropFrequency.RARE),
            ChanceItem(Items.GARDEN_PIE_7179, 6, 6, DropFrequency.RARE),
            ChanceItem(Items.SWORDFISH_374, 3, 3, DropFrequency.RARE),
            ChanceItem(Items.STRAWBERRIES5_5406, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.COOKED_KARAMBWAN_3145, 2, 2, DropFrequency.RARE),
        ),
    ),
    EARTH_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.EARTH_IMPLING_1031, NPCs.EARTH_IMPLING_6058),
            36,
            27.0,
            25.0,
            Item(Items.EARTH_IMPLING_JAR_11244),
            ChanceItem(Items.EARTH_TIARA_5535, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.EARTH_RUNE_557, 32, 32, DropFrequency.COMMON),
            ChanceItem(Items.COMPOST_6033, 6, 6, DropFrequency.COMMON),
            ChanceItem(Items.FIRE_TALISMAN_1442, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.EMERALD_1606, 2, 2, DropFrequency.RARE),
            ChanceItem(Items.BUCKET_OF_SAND_1784, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(Items.UNICORN_HORN_237, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.EARTH_TALISMAN_1440, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.GOLD_ORE_444, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.MITHRIL_PICKAXE_1273, 2, 2, DropFrequency.RARE),
            ChanceItem(Items.SUPERCOMPOST_6035, 2, 2, DropFrequency.RARE),
            ChanceItem(Items.MITHRIL_ORE_447, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.STEEL_BAR_2353, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.WILDBLOOD_SEED_5311, 2, 2, DropFrequency.COMMON),
            ChanceItem(Items.HARRALANDER_SEED_5294, 2, 2, DropFrequency.COMMON),
        ),
    ),
    ESSENCE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.ESSENCE_IMPLING_1032, NPCs.ESSENCE_IMPLING_6059),
            42,
            29.0,
            27.0,
            Item(Items.ESS_IMPLING_JAR_11246),
            ChanceItem(Items.RUNE_ESSENCE_1436, 200, 200, DropFrequency.RARE),
            ChanceItem(Items.PURE_ESSENCE_7937, 20, 35, DropFrequency.COMMON),
            ChanceItem(Items.CHAOS_RUNE_562, 4, 4, DropFrequency.COMMON),
            ChanceItem(Items.COSMIC_RUNE_564, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(Items.STEAM_RUNE_4694, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(Items.LAVA_RUNE_4699, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(Items.SMOKE_RUNE_4697, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(Items.BLOOD_RUNE_565, 7, 7, DropFrequency.RARE),
            ChanceItem(Items.SOUL_RUNE_566, 11, 11, DropFrequency.RARE),
            ChanceItem(Items.LAW_RUNE_563, 13, 13, DropFrequency.RARE),
            ChanceItem(Items.NATURE_RUNE_561, 13, 13, DropFrequency.RARE),
            ChanceItem(Items.DEATH_RUNE_560, 13, 13, DropFrequency.RARE),
            ChanceItem(Items.WATER_RUNE_555, 13, 30, DropFrequency.COMMON),
            ChanceItem(Items.MIND_RUNE_558, 25, 25, DropFrequency.COMMON),
            ChanceItem(Items.AIR_RUNE_556, 25, 60, DropFrequency.COMMON),
            ChanceItem(Items.BODY_RUNE_559, 28, 30, DropFrequency.COMMON),
            ChanceItem(Items.FIRE_RUNE_554, 50, 50, DropFrequency.COMMON),
            ChanceItem(Items.MIND_TALISMAN_1448, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BOW_STRING_1777, 92, 92, DropFrequency.RARE),
        ),
    ),
    ECLECTIC_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.ECLECTIC_IMPLING_1033, NPCs.ECLECTIC_IMPLING_6060),
            50,
            32.0,
            30.0,
            Item(Items.ECLECTIC_IMPLING_JAR_11248),
            ChanceItem(Items.MITHRIL_PICKAXE_1273, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.ADAMANT_KITESHIELD_1199, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BLUE_DHIDE_CHAPS_2493, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.RED_SPIKY_VAMBS_10083, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.RUNE_DAGGER_1213, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.BATTLESTAFF_1391, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(Items.CURRY_LEAF_5970, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.SNAPE_GRASS_231, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.AIR_RUNE_556, 30, 60, DropFrequency.COMMON),
            ChanceItem(Items.OAK_PLANK_8779, 4, 4, DropFrequency.COMMON),
            ChanceItem(Items.CANDLE_LANTERN_4527, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.GOLD_ORE_444, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.GOLD_BAR_2358, 5, 5, DropFrequency.UNCOMMON),
            ChanceItem(Items.PURE_ESSENCE_7937, 20, 35, DropFrequency.UNCOMMON),
            ChanceItem(Items.UNICORN_HORN_237, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.ADAMANTITE_ORE_450, 10, 10, DropFrequency.RARE),
            ChanceItem(Items.SLAYERS_RESPITE_5760, 2, 2, DropFrequency.RARE),
            ChanceItem(Items.WILD_PIE_7208, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.WATERMELON_SEED_5321, 3, 3, DropFrequency.RARE),
            ChanceItem(Items.DIAMOND_1601, 1, 1, DropFrequency.VERY_RARE),
        ),
    ),
    NATURE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.NATURE_IMPLING_1034, NPCs.NATURE_IMPLING_6061),
            58,
            36.0,
            34.0,
            Item(Items.NATURE_IMPLING_JAR_11250),
            ChanceItem(Items.DWARF_WEED_SEED_5303, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(Items.CLEAN_TORSTOL_270, 2, 2, DropFrequency.VERY_RARE),
            ChanceItem(Items.RANARR_SEED_5295, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.TORSTOL_SEED_5304, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.AVANTOE_SEED_5298, 5, 5, DropFrequency.UNCOMMON),
            ChanceItem(Items.KWUARM_SEED_5299, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.IRIT_SEED_5297, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.COCONUT_5974, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.CLEAN_SNAPDRAGON_3000, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.ORANGE_TREE_SEED_5285, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.CURRY_TREE_SEED_5286, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.LIMPWURT_SEED_5100, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.JANGERBERRY_SEED_5104, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BELLADONNA_SEED_5281, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.HARRALANDER_SEED_5294, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.CACTUS_SPINE_6016, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.MAGIC_LOGS_1513, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.CLEAN_TARROMIN_254, 4, 4, DropFrequency.COMMON),
            ChanceItem(Items.WILLOW_SEED_5313, 1, 1, DropFrequency.UNCOMMON),
        ),
    ),
    MAGPIE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.MAGPIE_IMPLING_1035, NPCs.MAGPIE_IMPLING_6062),
            65,
            216.0,
            44.0,
            Item(Items.MAGPIE_IMPLING_JAR_11252),
            500,
            ChanceItem(Items.DIAMOND_AMULET_1682, 3, 3, DropFrequency.COMMON),
            ChanceItem(Items.AMULET_OF_POWER_1732, 3, 3, DropFrequency.COMMON),
            ChanceItem(Items.RING_OF_FORGING_2569, 3, 3, DropFrequency.COMMON),
            ChanceItem(Items.SPLITBARK_GAUNTLETS_3391, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.RUNE_WARHAMMER_1347, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.RING_OF_LIFE_2571, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(Items.MYSTIC_BOOTS_4097, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.MYSTIC_GLOVES_4095, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.DRAGON_DAGGER_1215, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.RUNE_SQ_SHIELD_1185, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.NATURE_TIARA_5541, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BLACK_DRAGONHIDE_1748, 6, 6, DropFrequency.COMMON),
            ChanceItem(Items.RUNITE_BAR_2364, 2, 2, DropFrequency.UNCOMMON),
            ChanceItem(Items.DIAMOND_1602, 4, 4, DropFrequency.RARE),
            ChanceItem(Items.PINEAPPLE_SEED_5287, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, DropFrequency.RARE),
            ChanceItem(Items.SINISTER_KEY_993, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(Items.SNAPDRAGON_SEED_5300, 1, 1, DropFrequency.VERY_RARE),
        ),
    ),
    NINJA_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.NINJA_IMPLING_6053, NPCs.NINJA_IMPLING_6063),
            74,
            240.0,
            50.0,
            Item(Items.NINJA_IMPLING_JAR_11254),
            2000,
            ChanceItem(Items.MYSTIC_BOOTS_4097, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.RUNE_CHAINBODY_1113, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SPLITBARK_HELM_3385, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.DRAGON_DAGGER_1215, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.RUNE_DART_811, 70, 70, DropFrequency.UNCOMMON),
            ChanceItem(Items.RUNE_SCIMITAR_1333, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.RUNE_WARHAMMER_1347, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.ONYX_BOLTS_9342, 2, 2, DropFrequency.UNCOMMON),
            ChanceItem(Items.ONYX_BOLT_TIPS_9194, 4, 4, DropFrequency.RARE),
            ChanceItem(Items.PRAYER_POTION3_140, 4, 4, DropFrequency.COMMON),
            ChanceItem(Items.DAGANNOTH_HIDE_6155, 3, 3, DropFrequency.UNCOMMON),
            ChanceItem(Items.BLACK_DRAGONHIDE_1748, 10, 16, DropFrequency.COMMON),
        ),
    ),
    PIRATE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.PIRATE_IMPLING_7845, NPCs.PIRATE_IMPLING_7846),
            76,
            270.0,
            57.0,
            Item(Items.PIRATE_IMPLING_JAR_13337),
            2500,
            ChanceItem(Items.PIRATE_BOOTS_7114, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.GOLD_BAR_2358, 15, 15, DropFrequency.UNCOMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7110, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7122, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7128, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7134, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_13358, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_13360, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_13362, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7116, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7126, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7132, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7138, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_13364, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_13366, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_13368, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7112, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7124, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7130, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7136, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_8924, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_8926, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_8998, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_9000, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.LEFT_EYEPATCH_13355, 1, 1, DropFrequency.RARE),
        ),
        /*  Trouble brewing.
            ChanceItem(Items.BUCKET_1925, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BUCKET_OF_WATER_1929, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BOWL_1923, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.LOGS_1511, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.SCRAPEY_BARK_8977, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BOWL_OF_RED_WATER_8972, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BOWL_OF_BLUE_WATER_8974, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.RED_FLOWERS_8938, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BLUE_FLOWERS_8936, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.SWEETGRUBS_8981, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BITTERNUT_8976, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.PIECES_OF_EIGHT_8951, 1, 10, DropFrequency.RARE)
         */
    ),
    DRAGON_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.DRAGON_IMPLING_6054, NPCs.DRAGON_IMPLING_6064),
            83,
            300.0,
            65.0,
            Item(Items.DRAGON_IMPLING_JAR_11256),
            3000,
            ChanceItem(Items.AMULET_OF_GLORY_1705, 2, 3, DropFrequency.RARE),
            ChanceItem(Items.MYSTIC_ROBE_BOTTOM_4093, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(Items.DRAGONSTONE_AMMY_1684, 2, 3, DropFrequency.VERY_RARE),
            ChanceItem(Items.DRAGON_ARROW_11212, 100, 500, DropFrequency.COMMON),
            ChanceItem(Items.DRAGON_BOLTS_9341, 3, 40, DropFrequency.COMMON),
            ChanceItem(Items.DRAGON_LONGSWORD_1305, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.DRAGON_DAGGERP_PLUS_PLUS_5699, 3, 3, DropFrequency.UNCOMMON),
            ChanceItem(Items.DRAGON_DART_11230, 105, 350, DropFrequency.UNCOMMON),
            ChanceItem(Items.DRAGON_DART_TIP_11232, 105, 350, DropFrequency.COMMON),
            ChanceItem(Items.DRAGON_ARROWTIPS_11237, 100, 500, DropFrequency.COMMON),
            ChanceItem(Items.DRAGON_BOLT_TIPS_9193, 10, 49, DropFrequency.COMMON),
            ChanceItem(Items.EARTH_TIARA_5535, 111, 297, DropFrequency.COMMON),
            ChanceItem(Items.MAGIC_SEED_5316, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.DRAGON_BONES_537, 52, 99, DropFrequency.UNCOMMON),
            ChanceItem(Items.DRAGONSTONE_1616, 3, 6, DropFrequency.UNCOMMON),
            ChanceItem(Items.SNAPDRAGON_SEED_5300, 6, 6, DropFrequency.RARE),
            ChanceItem(Items.SUMMER_PIE_7219, 5, 15, DropFrequency.RARE),
        ),
    ), ;

    fun handle(
        player: Player,
        npc: NPC,
    ) {
        player.pulseManager.run(BNetPulse(player, npc, node))
    }

    companion object {
        private val implings = mutableListOf<ImplingNode>()

        /**
         * Retrieves the first node associated with the player's inventory reward item.
         *
         * @param player The player to check for the associated node.
         * @return The first node found or null if no matching node is found.
         */
        @JvmStatic
        fun getImpling(player: Player): ImplingNode? = implings.firstOrNull { player.inventory.containsItem(it.reward) }

        /**
         * Retrieves the BNetTypes associated with the given npc based on the npc id.
         *
         * @param npc The NPC to check for a matching BNetType.
         * @return The BNetTypes that match the npc id or null if no match is found.
         */
        @JvmStatic
        fun forNpc(npc: NPC): BNetTypes? = values().firstOrNull { type ->
            type.node.npcs.contains(npc.id)
        }

        /**
         * Retrieves the BNetNode associated with the given item based on the item id.
         *
         * @param item The item to check for a matching BNetNode.
         * @return The BNetNode that matches the item id or null if no match is found.
         */
        @JvmStatic
        fun forItem(item: Item): BNetNode? = values().firstOrNull { type ->
            type.node.reward.id == item.id
        }?.node

        /**
         * Retrieves the list of all ImplingNodes.
         *
         * @return A list containing all ImplingNodes.
         */
        fun getImplings(): List<ImplingNode> = implings

        init {
            implings.addAll(
                values().mapNotNull { type ->
                    type.node as? ImplingNode
                }
            )
        }
    }
}
