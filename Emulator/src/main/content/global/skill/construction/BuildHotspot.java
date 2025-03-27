package content.global.skill.construction;

import core.game.world.update.flag.context.Animation;
import org.rs.consts.Scenery;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Build hotspot.
 */
public enum BuildHotspot {

    /**
     * Centrepiece 1 build hotspot.
     */
    CENTREPIECE_1(Scenery.CENTREPIECE_SPACE_15361, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.PORTAL, Decoration.ROCK, Decoration.POND, Decoration.IMP_STATUE, Decoration.SMALL_OBELISK, Decoration.DUNGEON_ENTRANCE),
    /**
     * Big tree 1 build hotspot.
     */
    BIG_TREE_1(Scenery.BIG_TREE_SPACE_15362, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.BIG_DEAD_TREE, Decoration.BIG_TREE, Decoration.BIG_OAK_TREE, Decoration.BIG_WILLOW_TREE, Decoration.BIG_MAPLE_TREE, Decoration.BIG_YEW_TREE, Decoration.BIG_MAGIC_TREE),
    /**
     * Tree 1 build hotspot.
     */
    TREE_1(Scenery.TREE_SPACE_15363, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.DEAD_TREE, Decoration.TREE, Decoration.OAK_TREE, Decoration.WILLOW_TREE, Decoration.MAPLE_TREE, Decoration.YEW_TREE, Decoration.MAGIC_TREE),
    /**
     * Big plant 1 build hotspot.
     */
    BIG_PLANT_1(Scenery.BIG_PLANT_SPACE_1_15364, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.FERN, Decoration.BUSH, Decoration.TALL_PLANT),
    /**
     * Big plant 2 build hotspot.
     */
    BIG_PLANT_2(Scenery.BIG_PLANT_SPACE_2_15365, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.SHORT_PLANT, Decoration.LARGE_LEAF_PLANT, Decoration.HUGE_PLANT),
    /**
     * Small plant 1 build hotspot.
     */
    SMALL_PLANT_1(Scenery.SMALL_PLANT_SPACE_1_15366, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.PLANT, Decoration.SMALL_FERN, Decoration.FERN_SP),
    /**
     * Small plant 2 build hotspot.
     */
    SMALL_PLANT_2(Scenery.SMALL_PLANT_SPACE_2_15367, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.DOCK_LEAF, Decoration.THISTLE, Decoration.REEDS),

    /**
     * Chairs 1 build hotspot.
     */
    CHAIRS_1(Scenery.CHAIR_SPACE_15410, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CRUDE_CHAIR, Decoration.WOODEN_CHAIR, Decoration.ROCKING_CHAIR, Decoration.OAK_CHAIR, Decoration.OAK_ARMCHAIR, Decoration.TEAK_ARMCHAIR, Decoration.MAHOGANY_ARMCHAIR),
    /**
     * Chairs 2 build hotspot.
     */
    CHAIRS_2(Scenery.CHAIR_SPACE_15411, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CRUDE_CHAIR, Decoration.WOODEN_CHAIR, Decoration.ROCKING_CHAIR, Decoration.OAK_CHAIR, Decoration.OAK_ARMCHAIR, Decoration.TEAK_ARMCHAIR, Decoration.MAHOGANY_ARMCHAIR),
    /**
     * Chairs 3 build hotspot.
     */
    CHAIRS_3(Scenery.CHAIR_SPACE_15412, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CRUDE_CHAIR, Decoration.WOODEN_CHAIR, Decoration.ROCKING_CHAIR, Decoration.OAK_CHAIR, Decoration.OAK_ARMCHAIR, Decoration.TEAK_ARMCHAIR, Decoration.MAHOGANY_ARMCHAIR),
    /**
     * Fireplace build hotspot.
     */
    FIREPLACE(Scenery.FIREPLACE_SPACE_15418, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CLAY_FIREPLACE, Decoration.STONE_FIREPLACE, Decoration.MARBLE_FIREPLACE),
    /**
     * Fireplace 2 build hotspot.
     */
    FIREPLACE2(Scenery.FIREPLACE_SPACE_15267, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CLAY_FIREPLACE, Decoration.STONE_FIREPLACE, Decoration.MARBLE_FIREPLACE),
    /**
     * Curtains build hotspot.
     */
    CURTAINS(Scenery.CURTAIN_SPACE_15419, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.TORN_CURTAINS, Decoration.CURTAINS, Decoration.OPULENT_CURTAINS),
    /**
     * Bookcase build hotspot.
     */
    BOOKCASE(Scenery.BOOKCASE_SPACE_15416, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.WOODEN_BOOKCASE, Decoration.OAK_BOOKCASE, Decoration.MAHOGANY_BOOKCASE),
    /**
     * Rug build hotspot.
     */
    RUG(Scenery.RUG_SPACE_15415, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CORNER, Decoration.RED_RUG_CORNER, Decoration.OPULENT_RUG_CORNER),
    /**
     * Rug 2 build hotspot.
     */
    RUG2(Scenery.RUG_SPACE_15414, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_END, Decoration.RED_RUG_END, Decoration.OPULENT_RUG_END),
    /**
     * Rug 3 build hotspot.
     */
    RUG3(Scenery.RUG_SPACE_15413, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CENTER, Decoration.RED_RUG_CENTER, Decoration.OPULENT_RUG_CENTER),

    /**
     * Larder build hotspot.
     */
    LARDER(Scenery.LARDER_SPACE_15403, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.WOODEN_LARDER, Decoration.OAK_LARDER, Decoration.TEAK_LARDER),
    /**
     * Sink build hotspot.
     */
    SINK(Scenery.SINK_SPACE_15404, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.PUMP_AND_DRAIN, Decoration.PUMP_AND_TUB, Decoration.SINK),
    /**
     * Kitchen table build hotspot.
     */
    KITCHEN_TABLE(Scenery.TABLE_SPACE_15405, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.KITCHEN_WOODEN_TABLE, Decoration.KITCHEN_OAK_TABLE, Decoration.KITCHEN_TEAK_TABLE),
    /**
     * Cat blanket build hotspot.
     */
    CAT_BLANKET(Scenery.CAT_BASKET_SPACE_15402, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CAT_BLANKET, Decoration.CAT_BASKET, Decoration.CAST_BASKET_CUSHIONED),
    /**
     * Stove build hotspot.
     */
    STOVE(Scenery.STOVE_SPACE_15398, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.BASIC_FIREPIT, Decoration.FIREPIT_WITH_HOOK, Decoration.FIREPIT_WITH_POT, Decoration.SMALL_OVEN, Decoration.LARGE_OVEN, Decoration.BASIC_RANGE, Decoration.FANCY_RANGE),
    /**
     * Shelves build hotspot.
     */
    SHELVES(Scenery.SHELF_SPACE_15400, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.WOODEN_SHELVES_1, Decoration.WOODEN_SHELVES_2, Decoration.WOODEN_SHELVES_3, Decoration.OAK_SHELVES_1, Decoration.OAK_SHELVES_2, Decoration.TEAK_SHELVES_1, Decoration.TEAK_SHELVES_2),
    /**
     * Shelves 2 build hotspot.
     */
    SHELVES_2(Scenery.SHELF_SPACE_15399, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.WOODEN_SHELVES_1, Decoration.WOODEN_SHELVES_2, Decoration.WOODEN_SHELVES_3, Decoration.OAK_SHELVES_1, Decoration.OAK_SHELVES_2, Decoration.TEAK_SHELVES_1, Decoration.TEAK_SHELVES_2),
    /**
     * Barrels build hotspot.
     */
    BARRELS(Scenery.BARREL_SPACE_15401, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.BASIC_BEER_BARREL, Decoration.CIDER_BARREL, Decoration.ASGARNIAN_ALE_BARREL, Decoration.GREENMANS_ALE_BARREL, Decoration.DRAGON_BITTER_BARREL, Decoration.CHEFS_DELIGHT_BARREL),

    /**
     * Fireplace dining build hotspot.
     */
    FIREPLACE_DINING(Scenery.FIREPLACE_SPACE_15301, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CLAY_FIREPLACE, Decoration.STONE_FIREPLACE, Decoration.MARBLE_FIREPLACE),
    /**
     * Dining table build hotspot.
     */
    DINING_TABLE(Scenery.TABLE_SPACE_15298, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.DINING_TABLE_WOOD, Decoration.DINING_TABLE_OAK, Decoration.DINING_TABLE_CARVED_OAK, Decoration.DINING_TABLE_TEAK, Decoration.DINING_TABLE_CARVED_TEAK, Decoration.DINING_TABLE_MAHOGANY, Decoration.DINING_TABLE_OPULENT),
    /**
     * Dining curtains build hotspot.
     */
    DINING_CURTAINS(Scenery.CURTAIN_SPACE_15302, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.TORN_CURTAINS, Decoration.CURTAINS, Decoration.OPULENT_CURTAINS),
    /**
     * Dining bench 1 build hotspot.
     */
    DINING_BENCH_1(Scenery.SEATING_SPACE_15300, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.BENCH_WOODEN, Decoration.BENCH_OAK, Decoration.BENCH_CARVED_OAK, Decoration.BENCH_TEAK, Decoration.BENCH_CARVED_TEAK, Decoration.BENCH_MAHOGANY, Decoration.BENCH_GILDED),
    /**
     * Dining bench 2 build hotspot.
     */
    DINING_BENCH_2(Scenery.SEATING_SPACE_15299, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.BENCH_WOODEN, Decoration.BENCH_OAK, Decoration.BENCH_CARVED_OAK, Decoration.BENCH_TEAK, Decoration.BENCH_CARVED_TEAK, Decoration.BENCH_MAHOGANY, Decoration.BENCH_GILDED),
    /**
     * Rope bell pull build hotspot.
     */
    ROPE_BELL_PULL(Scenery.BELL_PULL_SPACE_15304, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.ROPE_PULL, Decoration.BELL_PULL, Decoration.FANCY_BELL_PULL),
    /**
     * Wall decoration build hotspot.
     */
    WALL_DECORATION(Scenery.DECORATION_SPACE_15303, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DECORATION, Decoration.TEAK_DECORATION, Decoration.GILDED_DECORATION),

    /**
     * Repair build hotspot.
     */
    REPAIR(Scenery.REPAIR_SPACE_15448, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.REPAIR_BENCH, Decoration.WHETSTONE, Decoration.ARMOUR_STAND),
    /**
     * Workbench build hotspot.
     */
    WORKBENCH(Scenery.WORKBENCH_SPACE_15439, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.WORKBENCH_WOODEN, Decoration.WORKBENCH_OAK, Decoration.WORKBENCH_STEEL_FRAME, Decoration.WORKBENCH_WITH_VICE, Decoration.WORKBENCH_WITH_LATHE),
    /**
     * Crafting build hotspot.
     */
    CRAFTING(Scenery.CLOCKMAKING_SPACE_15441, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CRAFTING_TABLE_1, Decoration.CRAFTING_TABLE_2, Decoration.CRAFTING_TABLE_3, Decoration.CRAFTING_TABLE_4),
    /**
     * Tool 1 build hotspot.
     */
    TOOL1(Scenery.TOOL_SPACE_15443, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.TOOL_STORE_1, Decoration.TOOL_STORE_2, Decoration.TOOL_STORE_3, Decoration.TOOL_STORE_4, Decoration.TOOL_STORE_5),
    /**
     * Tool 2 build hotspot.
     */
    TOOL2(Scenery.TOOL_SPACE_15444, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.TOOL_STORE_1, Decoration.TOOL_STORE_2, Decoration.TOOL_STORE_3, Decoration.TOOL_STORE_4, Decoration.TOOL_STORE_5),
    /**
     * Tool 3 build hotspot.
     */
    TOOL3(Scenery.TOOL_SPACE_15445, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.TOOL_STORE_1, Decoration.TOOL_STORE_2, Decoration.TOOL_STORE_3, Decoration.TOOL_STORE_4, Decoration.TOOL_STORE_5),
    /**
     * Tool 4 build hotspot.
     */
    TOOL4(Scenery.TOOL_SPACE_15446, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.TOOL_STORE_1, Decoration.TOOL_STORE_2, Decoration.TOOL_STORE_3, Decoration.TOOL_STORE_4, Decoration.TOOL_STORE_5),
    /**
     * Tool 5 build hotspot.
     */
    TOOL5(Scenery.TOOL_SPACE_15447, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.TOOL_STORE_1, Decoration.TOOL_STORE_2, Decoration.TOOL_STORE_3, Decoration.TOOL_STORE_4, Decoration.TOOL_STORE_5),
    /**
     * Heraldry build hotspot.
     */
    HERALDRY(Scenery.HERALDRY_SPACE_15450, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.PLUMING_STAND, Decoration.SHIELD_EASEL, Decoration.BANNER_EASEL),

    /**
     * Bed build hotspot.
     */
    BED(Scenery.BED_SPACE_15260, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.WOODEN_BED, Decoration.OAK_BED, Decoration.LARGE_OAK_BED, Decoration.TEAK_BED, Decoration.LARGE_TEAK_BED, Decoration.FOUR_POSTER, Decoration.GILDED_FOUR_POSTER),
    /**
     * Clock build hotspot.
     */
    CLOCK(Scenery.CLOCK_SPACE_15268, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_CLOCK, Decoration.TEAK_CLOCK, Decoration.GILDED_CLOCK),
    /**
     * Dresser build hotspot.
     */
    DRESSER(Scenery.DRESSER_SPACE_15262, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.SHAVING_STAND, Decoration.OAK_SHAVING_STAND, Decoration.OAK_DRESSER, Decoration.TEAK_DRESSER, Decoration.FANCY_TEAK_DRESSER, Decoration.MAHOGANY_DRESSER, Decoration.GILDED_DRESSER),
    /**
     * Drawers build hotspot.
     */
    DRAWERS(Scenery.WARDROBE_SPACE_15261, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.SHOE_BOX, Decoration.OAK_DRAWERS, Decoration.OAK_WARDROBE, Decoration.TEAK_DRAWERS, Decoration.TEAK_WARDROBE, Decoration.MAHOGANY_WARDROBE, Decoration.GILDED_WARDROBE),
    /**
     * Bedroom curtains build hotspot.
     */
    BEDROOM_CURTAINS(Scenery.CURTAIN_SPACE_15263, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.TORN_CURTAINS, Decoration.CURTAINS, Decoration.OPULENT_CURTAINS),
    /**
     * Bedroom rug build hotspot.
     */
    BEDROOM_RUG(Scenery.RUG_SPACE_15264, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CENTER, Decoration.RED_RUG_CENTER, Decoration.OPULENT_RUG_CENTER),
    /**
     * Bedroom rug 2 build hotspot.
     */
    BEDROOM_RUG2(Scenery.RUG_SPACE_15265, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_END, Decoration.RED_RUG_END, Decoration.OPULENT_RUG_END),
    /**
     * Bedroom rug 3 build hotspot.
     */
    BEDROOM_RUG3(Scenery.RUG_SPACE_15266, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CORNER, Decoration.RED_RUG_CORNER, Decoration.OPULENT_RUG_CORNER),

    /**
     * Armour space build hotspot.
     */
    ARMOUR_SPACE(Scenery.ARMOUR_SPACE_15385, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.MITHRIL_ARMOUR, Decoration.ADAMANT_ARMOUR, Decoration.RUNE_ARMOUR),
    /**
     * Armour space 2 build hotspot.
     */
    ARMOUR_SPACE2(Scenery.ARMOUR_SPACE_15384, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.MITHRIL_ARMOUR, Decoration.ADAMANT_ARMOUR, Decoration.RUNE_ARMOUR),
    /**
     * Head trophy build hotspot.
     */
    HEAD_TROPHY(Scenery.HEAD_TROPHY_SPACE_15382, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.CRAWLING_HAND, Decoration.COCKATRICE_HEAD, Decoration.BASILISK_HEAD, Decoration.KURASK_HEAD, Decoration.ABYSSAL_DEMON_HEAD, Decoration.KBD_HEAD, Decoration.KQ_HEAD),
    /**
     * Rune case build hotspot.
     */
    RUNE_CASE(Scenery.RUNE_CASE_SPACE_15386, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.RUNE_CASE1, Decoration.RUNE_CASE2),
    /**
     * Fishing trophy build hotspot.
     */
    FISHING_TROPHY(Scenery.FISHING_TROPHY_SPACE_15383, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.MOUNTED_BASS, Decoration.MOUNTED_SWORDFISH, Decoration.MOUNTED_SHARK),
    /**
     * Hall rug build hotspot.
     */
    HALL_RUG(Scenery.RUG_SPACE_15377, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CENTER, Decoration.RED_RUG_CENTER, Decoration.OPULENT_RUG_CENTER),
    /**
     * Hall rug 2 build hotspot.
     */
    HALL_RUG2(Scenery.RUG_SPACE_15378, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_END, Decoration.RED_RUG_END, Decoration.OPULENT_RUG_END),
    /**
     * Hall rug 3 build hotspot.
     */
    HALL_RUG3(Scenery.RUG_SPACE_15379, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CORNER, Decoration.RED_RUG_CORNER, Decoration.OPULENT_RUG_CORNER),

    /**
     * Ranging game build hotspot.
     */
    RANGING_GAME(Scenery.RANGING_GAME_SPACE_15346, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.HOOP_AND_STICK, Decoration.DARTBOARD, Decoration.ARCHERY_TARGET),
    /**
     * Attack stone build hotspot.
     */
    ATTACK_STONE(Scenery.STONE_SPACE_15344, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CLAY_STONE, Decoration.LIMESTONE_STONE, Decoration.MARBLE_STONE),
    /**
     * Prize chest build hotspot.
     */
    PRIZE_CHEST(Scenery.PRIZE_CHEST_SPACE_15343, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_CHEST, Decoration.TEAK_CHEST, Decoration.MAHOGANY_CHEST),
    /**
     * Elemental balance build hotspot.
     */
    ELEMENTAL_BALANCE(Scenery.ELEMENTAL_BALANCE_SPACE_15345, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.BALANCE_1, Decoration.BALANCE_2, Decoration.BALANCE_3),
    /**
     * Game space build hotspot.
     */
    GAME_SPACE(Scenery.GAME_SPACE_15342, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.JESTER, Decoration.TREASURE_HUNT, Decoration.HANGMAN),

    /**
     * Teleport focus build hotspot.
     */
    TELEPORT_FOCUS(Scenery.CENTREPIECE_SPACE_15409, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.TELEPORT_FOCUS, Decoration.GREATER_TELEPORT_FOCUS, Decoration.SCRYING_POOL),
    /**
     * Portal 1 build hotspot.
     */
    PORTAL1(Scenery.PORTAL_SPACE_15406, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.TEAK_PORTAL, Decoration.MAHOGANY_PORTAL, Decoration.MARBLE_PORTAL,
        Decoration.TEAK_VARROCK_PORTAL,
        Decoration.MAHOGANY_VARROCK_PORTAL,
        Decoration.MARBLE_VARROCK_PORTAL,
        Decoration.TEAK_LUMBRIDGE_PORTAL,
        Decoration.MAHOGANY_LUMBRIDGE_PORTAL,
        Decoration.MARBLE_LUMBRIDGE_PORTAL,
        Decoration.TEAK_FALADOR_PORTAL,
        Decoration.MAHOGANY_FALADOR_PORTAL,
        Decoration.MARBLE_FALADOR_PORTAL,
        Decoration.TEAK_CAMELOT_PORTAL,
        Decoration.MAHOGANY_CAMELOT_PORTAL,
        Decoration.MARBLE_CAMELOT_PORTAL,
        Decoration.TEAK_ARDOUGNE_PORTAL,
        Decoration.MAHOGANY_ARDOUGNE_PORTAL,
        Decoration.MARBLE_ARDOUGNE_PORTAL,
        Decoration.TEAK_YANILLE_PORTAL,
        Decoration.MAHOGANY_YANILLE_PORTAL,
        Decoration.MARBLE_YANILLE_PORTAL,
        Decoration.TEAK_KHARYRLL_PORTAL,
        Decoration.MAHOGANY_KHARYRLL_PORTAL,
        Decoration.MARBLE_KHARYRLL_PORTAL),
    /**
     * Portal 2 build hotspot.
     */
    PORTAL2(Scenery.PORTAL_SPACE_15407, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.TEAK_PORTAL, Decoration.MAHOGANY_PORTAL, Decoration.MARBLE_PORTAL,
        Decoration.TEAK_VARROCK_PORTAL,
        Decoration.MAHOGANY_VARROCK_PORTAL,
        Decoration.MARBLE_VARROCK_PORTAL,
        Decoration.TEAK_LUMBRIDGE_PORTAL,
        Decoration.MAHOGANY_LUMBRIDGE_PORTAL,
        Decoration.MARBLE_LUMBRIDGE_PORTAL,
        Decoration.TEAK_FALADOR_PORTAL,
        Decoration.MAHOGANY_FALADOR_PORTAL,
        Decoration.MARBLE_FALADOR_PORTAL,
        Decoration.TEAK_CAMELOT_PORTAL,
        Decoration.MAHOGANY_CAMELOT_PORTAL,
        Decoration.MARBLE_CAMELOT_PORTAL,
        Decoration.TEAK_ARDOUGNE_PORTAL,
        Decoration.MAHOGANY_ARDOUGNE_PORTAL,
        Decoration.MARBLE_ARDOUGNE_PORTAL,
        Decoration.TEAK_YANILLE_PORTAL,
        Decoration.MAHOGANY_YANILLE_PORTAL,
        Decoration.MARBLE_YANILLE_PORTAL,
        Decoration.TEAK_KHARYRLL_PORTAL,
        Decoration.MAHOGANY_KHARYRLL_PORTAL,
        Decoration.MARBLE_KHARYRLL_PORTAL),
    /**
     * Portal 3 build hotspot.
     */
    PORTAL3(Scenery.PORTAL_SPACE_15408, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.TEAK_PORTAL, Decoration.MAHOGANY_PORTAL, Decoration.MARBLE_PORTAL,
        Decoration.TEAK_VARROCK_PORTAL,
        Decoration.MAHOGANY_VARROCK_PORTAL,
        Decoration.MARBLE_VARROCK_PORTAL,
        Decoration.TEAK_LUMBRIDGE_PORTAL,
        Decoration.MAHOGANY_LUMBRIDGE_PORTAL,
        Decoration.MARBLE_LUMBRIDGE_PORTAL,
        Decoration.TEAK_FALADOR_PORTAL,
        Decoration.MAHOGANY_FALADOR_PORTAL,
        Decoration.MARBLE_FALADOR_PORTAL,
        Decoration.TEAK_CAMELOT_PORTAL,
        Decoration.MAHOGANY_CAMELOT_PORTAL,
        Decoration.MARBLE_CAMELOT_PORTAL,
        Decoration.TEAK_ARDOUGNE_PORTAL,
        Decoration.MAHOGANY_ARDOUGNE_PORTAL,
        Decoration.MARBLE_ARDOUGNE_PORTAL,
        Decoration.TEAK_YANILLE_PORTAL,
        Decoration.MAHOGANY_YANILLE_PORTAL,
        Decoration.MARBLE_YANILLE_PORTAL,
        Decoration.TEAK_KHARYRLL_PORTAL,
        Decoration.MAHOGANY_KHARYRLL_PORTAL,
        Decoration.MARBLE_KHARYRLL_PORTAL),

    /**
     * Guild trophy build hotspot.
     */
    GUILD_TROPHY(Scenery.GUILD_TROPHY_SPACE_15394, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.ANTIDRAGON_SHIELD, Decoration.AMULET_OF_GLORY, Decoration.CAPE_OF_LEGENDS),
    /**
     * Portrait build hotspot.
     */
    PORTRAIT(Scenery.PORTRAIT_SPACE_15392, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.KING_ARTHUR, Decoration.ELENA, Decoration.GIANT_DWARF, Decoration.MISCELLANIANS),
    /**
     * Landscape build hotspot.
     */
    LANDSCAPE(Scenery.LANDSCAPE_SPACE_15393, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.LUMBRIDGE, Decoration.THE_DESERT, Decoration.MORYTANIA, Decoration.KARAMJA, Decoration.ISAFDAR),
    /**
     * Sword build hotspot.
     */
    SWORD(Scenery.SWORD_SPACE_15395, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.SILVERLIGHT, Decoration.EXCALIBUR, Decoration.DARKLIGHT),
    /**
     * Map build hotspot.
     */
    MAP(Scenery.MAP_SPACE_15396, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.SMALL_MAP, Decoration.MEDIUM_MAP, Decoration.LARGE_MAP),
    /**
     * Bookcase 2 build hotspot.
     */
    BOOKCASE2(Scenery.BOOKCASE_SPACE_15397, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.WOODEN_BOOKCASE, Decoration.OAK_BOOKCASE, Decoration.MAHOGANY_BOOKCASE),

    /**
     * Obelisk build hotspot.
     */
    OBELISK(44911, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.MINI_OBELISK),
    /**
     * Pet feeder build hotspot.
     */
    PET_FEEDER(44910, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_PET_FEEDER, Decoration.TEAK_PET_FEEDER, Decoration.MAHOGANY_PET_FEEDER),
    /**
     * Pet house build hotspot.
     */
    PET_HOUSE(44909, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_PET_HOUSE, Decoration.TEAK_PET_HOUSE, Decoration.MAHOGANY_PET_HOUSE, Decoration.CONSECRATED_PET_HOUSE, Decoration.DESECRATED_PET_HOUSE, Decoration.NATURAL_PET_HOUSE),
    /**
     * Habitat 1 build hotspot.
     */
    HABITAT_1(44907, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.GARDEN_HABITAT, Decoration.JUNGLE_HABITAT, Decoration.DESERT_HABITAT, Decoration.POLAR_HABITAT, Decoration.VOLCANIC_HABITAT),
    /**
     * Habitat 2 build hotspot.
     */
    HABITAT_2(44908, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.GARDEN_HABITAT, Decoration.JUNGLE_HABITAT, Decoration.DESERT_HABITAT, Decoration.POLAR_HABITAT, Decoration.VOLCANIC_HABITAT),

    /**
     * Wall decoration 2 build hotspot.
     */
    WALL_DECORATION2(Scenery.DECORATION_SPACE_15297, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DECORATION, Decoration.TEAK_DECORATION, Decoration.GILDED_DECORATION),
    /**
     * Storage space build hotspot.
     */
    STORAGE_SPACE(Scenery.STORAGE_SPACE_15296, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.GLOVE_RACK, Decoration.WEAPONS_RACK, Decoration.EXTRA_WEAPONS_RACK),
    /**
     * Cr ring build hotspot.
     */
    CR_RING(Scenery.COMBAT_RING_SPACE_15277, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr corner build hotspot.
     */
    CR_CORNER(Scenery.COMBAT_RING_SPACE_15278, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr corner 2 build hotspot.
     */
    CR_CORNER2(Scenery.COMBAT_RING_SPACE_15279, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr ring 3 build hotspot.
     */
    CR_RING3(Scenery.COMBAT_RING_SPACE_15280, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.MAGIC_BARRIER),
    /**
     * Cr corner 3 build hotspot.
     */
    CR_CORNER3(Scenery.COMBAT_RING_SPACE_15281, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr corner 4 build hotspot.
     */
    CR_CORNER4(Scenery.COMBAT_RING_SPACE_15282, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr invisible wall build hotspot.
     */
    CR_INVISIBLE_WALL(Scenery.COMBAT_RING_SPACE_15283, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.INVISIBLE_WALL, Decoration.INVISIBLE_WALL, Decoration.INVISIBLE_WALL, Decoration.INVISIBLE_WALL, Decoration.MAGIC_BARRIER),
    /**
     * Cr invisible wall 2 build hotspot.
     */
    CR_INVISIBLE_WALL2(Scenery.COMBAT_RING_SPACE_15284, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.INVISIBLE_WALL2, Decoration.INVISIBLE_WALL2, Decoration.INVISIBLE_WALL2, Decoration.INVISIBLE_WALL2, Decoration.MAGIC_BARRIER),
    /**
     * Cr invisible wall 3 build hotspot.
     */
    CR_INVISIBLE_WALL3(Scenery.COMBAT_RING_SPACE_15285, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.INVISIBLE_WALL3, Decoration.INVISIBLE_WALL3, Decoration.INVISIBLE_WALL3, Decoration.INVISIBLE_WALL3, Decoration.MAGIC_BARRIER),
    /**
     * Cr ring 4 build hotspot.
     */
    CR_RING4(Scenery.COMBAT_RING_SPACE_15286, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr ring 2 build hotspot.
     */
    CR_RING2(Scenery.COMBAT_RING_SPACE_15287, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_RING, Decoration.FENCING_RING, Decoration.COMBAT_RING, Decoration.NOTHING, Decoration.MAGIC_BARRIER),
    /**
     * Cr floor build hotspot.
     */
    CR_FLOOR(Scenery.COMBAT_RING_SPACE_15288, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT_SIDE, Decoration.FENCING_MAT_SIDE, Decoration.COMBAT_MAT_SIDE, Decoration.BALANCE_BEAM_CENTER, Decoration.NOTHING2),
    /**
     * Cr floor 2 build hotspot.
     */
    CR_FLOOR2(Scenery.COMBAT_RING_SPACE_15289, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT_CORNER, Decoration.FENCING_MAT_CORNER, Decoration.COMBAT_MAT_CORNER, Decoration.BALANCE_BEAM_RIGHT, Decoration.NOTHING2),
    /**
     * Cr floor 3 build hotspot.
     */
    CR_FLOOR3(Scenery.COMBAT_RING_SPACE_15290, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT_CORNER, Decoration.FENCING_MAT_CORNER, Decoration.COMBAT_MAT_CORNER, Decoration.BALANCE_BEAM_LEFT, Decoration.RANGING_PEDESTALS),
    /**
     * Cr floor 4 build hotspot.
     */
    CR_FLOOR4(Scenery.COMBAT_RING_SPACE_15291, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT_SIDE, Decoration.FENCING_MAT_SIDE, Decoration.COMBAT_MAT_SIDE, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr floor 5 build hotspot.
     */
    CR_FLOOR5(Scenery.COMBAT_RING_SPACE_15292, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT, Decoration.FENCING_MAT, Decoration.COMBAT_MAT, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr floor 6 build hotspot.
     */
    CR_FLOOR6(Scenery.COMBAT_RING_SPACE_15293, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT_SIDE, Decoration.FENCING_MAT_SIDE, Decoration.COMBAT_MAT_SIDE, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr floor 7 build hotspot.
     */
    CR_FLOOR7(Scenery.COMBAT_RING_SPACE_15294, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT_CORNER, Decoration.FENCING_MAT_CORNER, Decoration.COMBAT_MAT_CORNER, Decoration.NOTHING, Decoration.NOTHING2),
    /**
     * Cr floor 8 build hotspot.
     */
    CR_FLOOR8(Scenery.COMBAT_RING_SPACE_15295, BuildHotspotType.LINKED, BuildingUtils.BUILD_MID_ANIM, Decoration.BOXING_MAT_CORNER, Decoration.FENCING_MAT_CORNER, Decoration.COMBAT_MAT_CORNER, Decoration.NOTHING, Decoration.RANGING_PEDESTALS),

    /**
     * Q hall rug build hotspot.
     */
    Q_HALL_RUG(Scenery.RUG_SPACE_15387, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CENTER, Decoration.RED_RUG_CENTER, Decoration.OPULENT_RUG_CENTER),
    /**
     * Q hall rug 2 build hotspot.
     */
    Q_HALL_RUG2(Scenery.RUG_SPACE_15388, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_END, Decoration.RED_RUG_END, Decoration.OPULENT_RUG_END),
    /**
     * Q hall rug 3 build hotspot.
     */
    Q_HALL_RUG3(Scenery.RUG_SPACE_15389, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CORNER, Decoration.RED_RUG_CORNER, Decoration.OPULENT_RUG_CORNER),

    /**
     * Globe build hotspot.
     */
    GLOBE(Scenery.GLOBE_SPACE_15421, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.GLOBE, Decoration.ORNAMENTAL_GLOBE, Decoration.LUNAR_GLOBE, Decoration.CELESTIAL_GLOBE, Decoration.ARMILLARY_SPHERE, Decoration.SMALL_ORREY, Decoration.LARGE_ORREY),
    /**
     * Lectern build hotspot.
     */
    LECTERN(Scenery.LECTERN_SPACE_15420, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_LECTERN, Decoration.EAGLE_LECTERN, Decoration.DEMON_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN),
    /**
     * Crystal ball build hotspot.
     */
    CRYSTAL_BALL(Scenery.CRYSTAL_BALL_SPACE_15422, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.CRYSTAL_BALL, Decoration.ELEMENTAL_SPHERE, Decoration.CRYSTAL_OF_POWER),
    /**
     * Bookcase 3 build hotspot.
     */
    BOOKCASE3(Scenery.BOOKCASE_SPACE_15425, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.WOODEN_BOOKCASE, Decoration.OAK_BOOKCASE, Decoration.MAHOGANY_BOOKCASE),
    /**
     * Wall chart build hotspot.
     */
    WALL_CHART(Scenery.WALL_CHART_SPACE_15423, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.ALCHEMICAL_CHART, Decoration.ASTRONOMICAL_CHART, Decoration.INFERNAL_CHART),
    /**
     * Telescope build hotspot.
     */
    TELESCOPE(Scenery.TELESCOPE_SPACE_15424, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.TELESCOPE1, Decoration.TELESCOPE2, Decoration.TELESCOPE3),

    /**
     * Treasure chest build hotspot.
     */
    TREASURE_CHEST(Scenery.TREASURE_CHEST_SPACE_18813, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_TREASURE_CHEST, Decoration.TEAK_TREASURE_CHEST, Decoration.MAHOGANY_TREASURE_CHEST),
    /**
     * Armour case build hotspot.
     */
    ARMOUR_CASE(Scenery.ARMOUR_CASE_SPACE_18815, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_ARMOUR_CASE, Decoration.TEAK_ARMOUR_CASE, Decoration.MGANY_ARMOUR_CASE),
    /**
     * Magic wardrobe build hotspot.
     */
    MAGIC_WARDROBE(Scenery.MAGIC_WARDROBE_SPACE_18811, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_MAGIC_WARDROBE, Decoration.C_OAK_MAGIC_WARDROBE, Decoration.TEAK_MAGIC_WARDROBE, Decoration.C_TEAK_MAGIC_WARDROBE, Decoration.MGANY_MAGIC_WARDROBE, Decoration.GILDED_MAGIC_WARDROBE, Decoration.MARBLE_MAGIC_WARDROBE),
    /**
     * Cape rack build hotspot.
     */
    CAPE_RACK(Scenery.CAPE_RACK_SPACE_18810, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_CAPE_RACK, Decoration.TEAK_CAPE_RACK, Decoration.MGANY_CAPE_RACK, Decoration.GILDED_CAPE_RACK, Decoration.MARBLE_CAPE_RACK, Decoration.MAGIC_CAPE_RACK),
    /**
     * Toy box build hotspot.
     */
    TOY_BOX(Scenery.TOY_BOX_SPACE_18812, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_TOY_BOX, Decoration.TEAK_TOY_BOX, Decoration.MAHOGANY_TOY_BOX),
    /**
     * Costume box build hotspot.
     */
    COSTUME_BOX(Scenery.FANCY_DRESS_BOX_SPACE_18814, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_COSTUME_BOX, Decoration.TEAK_COSTUME_BOX, Decoration.MAHOGANY_COSTUME_BOX),

    /**
     * Altar build hotspot.
     */
    ALTAR(Scenery.ALTAR_SPACE_15270, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_ALTAR, Decoration.TEAK_ALTAR, Decoration.CLOTH_ALTAR, Decoration.MAHOGANY_ALTAR, Decoration.LIMESTONE_ALTAR, Decoration.MARBLE_ALTAR, Decoration.GILDED_ALTAR),
    /**
     * Statue build hotspot.
     */
    STATUE(Scenery.STATUE_SPACE_15275, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.SMALL_STATUE, Decoration.MEDIUM_STATUE, Decoration.LARGE_STATUE),
    /**
     * Musical build hotspot.
     */
    MUSICAL(Scenery.MUSICAL_SPACE_15276, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.WINDCHIMES, Decoration.BELLS, Decoration.ORGAN),
    /**
     * Icon build hotspot.
     */
    ICON(Scenery.ICON_SPACE_15269, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.SARADOMIN_SYMBOL, Decoration.ZAMORAK_SYMBOL, Decoration.GUTHIX_SYMBOL, Decoration.SARADOMIN_ICON, Decoration.ZAMORAK_ICON, Decoration.GUTHIX_ICON, Decoration.ICON_OF_BOB),
    /**
     * Burners build hotspot.
     */
    BURNERS(Scenery.LAMP_SPACE_15271, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.STEEL_TORCHES, Decoration.WOODEN_TORCHES, Decoration.STEEL_CANDLESTICKS, Decoration.GOLD_CANDLESTICKS, Decoration.INCENSE_BURNERS, Decoration.MAHOGANY_BURNERS, Decoration.MARBLE_BURNERS),
    /**
     * The Chapel window.
     */
    CHAPEL_WINDOW(new int[]{Scenery.WINDOW_SPACE_13730, Scenery.WINDOW_SPACE_13728, Scenery.WINDOW_SPACE_13732, Scenery.WINDOW_SPACE_13729, Scenery.WINDOW_SPACE_13733, Scenery.WINDOW_SPACE_13731}, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.SHUTTERED_WINDOW, Decoration.DECORATIVE_WINDOW, Decoration.STAINED_GLASS),
    /**
     * Chapel rug build hotspot.
     */
    CHAPEL_RUG(Scenery.RUG_SPACE_15273, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_END, Decoration.RED_RUG_END, Decoration.OPULENT_RUG_END),
    /**
     * Chapel rug 2 build hotspot.
     */
    CHAPEL_RUG2(Scenery.RUG_SPACE_15274, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.BROWN_RUG_CORNER, Decoration.RED_RUG_CORNER, Decoration.OPULENT_RUG_CORNER),

    /**
     * Centrepiece 2 build hotspot.
     */
    CENTREPIECE_2(Scenery.CENTREPIECE_SPACE_15368, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.PORTAL, Decoration.GAZEBO, Decoration.DUNGEON_ENTRANCE, Decoration.SMALL_FOUNTAIN, Decoration.LARGE_FOUNTAIN, Decoration.POSH_FOUNTAIN),
    /**
     * Fencing build hotspot.
     */
    FENCING(Scenery.FENCING_15369, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.BOUNDARY_STONES, Decoration.WOODEN_FENCE, Decoration.STONE_WALL, Decoration.IRON_RAILINGS, Decoration.PICKET_FENCE, Decoration.GARDEN_FENCE, Decoration.MARBLE_WALL),
    /**
     * Small plant 1 build hotspot.
     */
    SMALL_PLANT1(Scenery.SMALL_PLANT_15375, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.SUNFLOWER, Decoration.MARIGOLDS, Decoration.ROSES),
    /**
     * Big plant 1 build hotspot.
     */
    BIG_PLANT1(Scenery.BIG_PLANT_15373, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.SUNFLOWER_BIG, Decoration.MARIGOLDS_BIG, Decoration.ROSES_BIG),
    /**
     * Small plant 2 build hotspot.
     */
    SMALL_PLANT2(Scenery.SMALL_PLANT_2_15376, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.ROSEMARY, Decoration.DAFFODILS, Decoration.BLUEBELLS),
    /**
     * Big plant 2 build hotspot.
     */
    BIG_PLANT2(Scenery.BIG_PLANT_2_15374, BuildHotspotType.INDIVIDUAL, BuildingUtils.PLANT_ANIM, Decoration.ROSEMARY_BIG, Decoration.DAFFODILS_BIG, Decoration.BLUEBELLS_BIG),
    /**
     * Hedge 1 build hotspot.
     */
    HEDGE1(Scenery.HEDGING_15370, BuildHotspotType.LINKED, BuildingUtils.PLANT_ANIM, Decoration.THORNY_HEDGE1, Decoration.NICE_HEDGE1, Decoration.SMALL_BOX_HEDGE1, Decoration.TOPIARY_HEDGE1, Decoration.FANCY_HEDGE1, Decoration.TALL_FANCY_HEDGE1, Decoration.TALL_BOX_HEDGE1),
    /**
     * Hedge 2 build hotspot.
     */
    HEDGE2(Scenery.HEDGING_15371, BuildHotspotType.LINKED, BuildingUtils.PLANT_ANIM, Decoration.THORNY_HEDGE2, Decoration.NICE_HEDGE2, Decoration.SMALL_BOX_HEDGE2, Decoration.TOPIARY_HEDGE2, Decoration.FANCY_HEDGE2, Decoration.TALL_FANCY_HEDGE2, Decoration.TALL_BOX_HEDGE2),
    /**
     * Hedge 3 build hotspot.
     */
    HEDGE3(Scenery.HEDGING_15372, BuildHotspotType.LINKED, BuildingUtils.PLANT_ANIM, Decoration.THORNY_HEDGE3, Decoration.NICE_HEDGE3, Decoration.SMALL_BOX_HEDGE3, Decoration.TOPIARY_HEDGE3, Decoration.FANCY_HEDGE3, Decoration.TALL_FANCY_HEDGE3, Decoration.TALL_BOX_HEDGE3),

    /**
     * Throne build hotspot.
     */
    THRONE(Scenery.THRONE_SPACE_15426, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_THRONE, Decoration.TEAK_THRONE, Decoration.MAHOGANY_THRONE, Decoration.GILDED_THRONE, Decoration.SKELETON_THRONE, Decoration.CRYSTAL_THRONE, Decoration.DEMONIC_THRONE),
    /**
     * Lever build hotspot.
     */
    LEVER(Scenery.LEVER_SPACE_15435, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.OAK_LEVER, Decoration.TEAK_LEVER, Decoration.MAHOGANY_LEVER),
    /**
     * The Floor.
     */
    FLOOR(new int[]{Scenery.FLOOR_SPACE_15427, Scenery.FLOOR_SPACE_15428, Scenery.FLOOR_SPACE_15429, Scenery.FLOOR_SPACE_15430, Scenery.FLOOR_SPACE_15431, Scenery.FLOOR_SPACE_15432}, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_LOW_ANIM, Decoration.FLOOR_DECORATION, Decoration.STEEL_CAGE, Decoration.FLOOR_TRAP, Decoration.MAGIC_CIRCLE, Decoration.MAGIC_CAGE),
    /**
     * Seating 1 build hotspot.
     */
    SEATING1(Scenery.SEATING_SPACE_15436, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.CARVED_TEAK_BENCH, Decoration.MAHOGANY_BENCH, Decoration.GILDED_BENCH),
    /**
     * Seating 2 build hotspot.
     */
    SEATING2(Scenery.SEATING_SPACE_15437, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_MID_ANIM, Decoration.CARVED_TEAK_BENCH, Decoration.MAHOGANY_BENCH, Decoration.GILDED_BENCH),
    /**
     * Trapdoor build hotspot.
     */
    TRAPDOOR(Scenery.TRAPDOOR_SPACE_15438, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_LOW_ANIM, Decoration.OAK_TRAPDOOR, Decoration.TEAK_TRAPDOOR, Decoration.MAHOGANY_TRAPDOOR),
    /**
     * Decoration build hotspot.
     */
    DECORATION(Scenery.DECORATION_SPACE_15434, BuildHotspotType.CREST, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DECO, Decoration.TEAK_DECO, Decoration.GILDED_DECO, Decoration.ROUND_SHIELD, Decoration.SQUARE_SHIELD, Decoration.KITE_SHIELD),

    /**
     * Floor mid build hotspot.
     */
    FLOOR_MID(Scenery.FLOOR_SPACE_MID_15347, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.SPIKES_MID, Decoration.TENTACLE_MID, Decoration.FP_FLOOR_MID, Decoration.ROCNAR_FLOOR_MID),
    /**
     * Floor side build hotspot.
     */
    FLOOR_SIDE(Scenery.FLOOR_SPACE_SIDE_15348, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.SPIKES_SIDE, Decoration.TENTACLE_SIDE, Decoration.FP_FLOOR_SIDE, Decoration.ROCNAR_FLOOR_SIDE),
    /**
     * Floor corner build hotspot.
     */
    FLOOR_CORNER(Scenery.FLOOR_SPACE_CORNER_15349, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.SPIKES_CORNER, Decoration.TENTACLE_CORNER, Decoration.FP_FLOOR_CORNER, Decoration.ROCNAR_FLOOR_CORNER),
    /**
     * Oubliette floor build hotspot.
     */
    OUBLIETTE_FLOOR(Scenery.FLOOR_SPACE_15350, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.SPIKES_FL, Decoration.TENTACLE_FL, Decoration.FLAME_PIT, Decoration.ROCNAR_FL),
    /**
     * Oubliette floor 1 build hotspot.
     */
    OUBLIETTE_FLOOR_1(Scenery.FLOOR_SPACE_15351, BuildHotspotType.LINKED, BuildingUtils.BUILD_LOW_ANIM, Decoration.SPIKES_FL, Decoration.TENTACLE_FL, Decoration.FLAME_PIT, Decoration.ROCNAR),
    /**
     * Prison build hotspot.
     */
    PRISON(Scenery.PRISON_SPACE_15352, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_CAGE, Decoration.OAK_STEEL_CAGE, Decoration.STEEL_CAGE_OU, Decoration.SPIKED_CAGE, Decoration.BONE_CAGE),
    /**
     * Prison door build hotspot.
     */
    PRISON_DOOR(Scenery.PRISON_SPACE_15353, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_CAGE_DOOR, Decoration.OAK_STEEL_CAGE_DOOR, Decoration.STEEL_CAGE_DOOR, Decoration.SPIKED_CAGE_DOOR, Decoration.BONE_CAGE_DOOR),
    /**
     * Guard build hotspot.
     */
    GUARD(Scenery.GUARD_SPACE_15354, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.SKELETON_GUARD, Decoration.GUARD_DOG, Decoration.HOBGOBLIN, Decoration.BABY_RED_DRAGON, Decoration.HUGE_SPIDER, Decoration.TROLL, Decoration.HELLHOUND),
    /**
     * Ladder build hotspot.
     */
    LADDER(Scenery.LADDER_SPACE_15356, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_LADDER, Decoration.TEAK_LADDER, Decoration.MAHOGANY_LADDER),
    /**
     * Oubliette light build hotspot.
     */
    OUBLIETTE_LIGHT(Scenery.LIGHTING_SPACE_15355, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.CANDLE, Decoration.TORCH, Decoration.SKULL_TORCH),

    /**
     * Dungeon door left build hotspot.
     */
    DUNGEON_DOOR_LEFT(Scenery.DOOR_SPACE_15328, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DOOR_LEFT, Decoration.STEEL_DOOR_LEFT, Decoration.MARBLE_DOOR_LEFT),
    /**
     * Dungeon door right build hotspot.
     */
    DUNGEON_DOOR_RIGHT(Scenery.DOOR_SPACE_15329, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DOOR_RIGHT, Decoration.STEEL_DOOR_RIGHT, Decoration.MARBLE_DOOR_RIGHT),
    /**
     * Dungeon door left 2 build hotspot.
     */
    DUNGEON_DOOR_LEFT2(Scenery.DOOR_SPACE_15326, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DOOR_LEFT, Decoration.STEEL_DOOR_LEFT, Decoration.MARBLE_DOOR_LEFT),
    /**
     * Dungeon door right 2 build hotspot.
     */
    DUNGEON_DOOR_RIGHT2(Scenery.DOOR_SPACE_15327, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DOOR_RIGHT, Decoration.STEEL_DOOR_RIGHT, Decoration.MARBLE_DOOR_RIGHT),
    /**
     * Dungeon door left 3 build hotspot.
     */
    DUNGEON_DOOR_LEFT3(36672, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DOOR_LEFT, Decoration.STEEL_DOOR_LEFT, Decoration.MARBLE_DOOR_LEFT),
    /**
     * Dungeon door right 3 build hotspot.
     */
    DUNGEON_DOOR_RIGHT3(36675, BuildHotspotType.LINKED, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_DOOR_RIGHT, Decoration.STEEL_DOOR_RIGHT, Decoration.MARBLE_DOOR_RIGHT),
    /**
     * Dungeon trap build hotspot.
     */
    DUNGEON_TRAP(Scenery.TRAP_SPACE_15324, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_LOW_ANIM, Decoration.SPIKE_TRAP, Decoration.MAN_TRAP, Decoration.TANGLE_TRAP, Decoration.MARBLE_TRAP, Decoration.TELEPORT_TRAP),
    /**
     * Dungeon trap 2 build hotspot.
     */
    DUNGEON_TRAP2(Scenery.TRAP_SPACE_15325, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_LOW_ANIM, Decoration.SPIKE_TRAP, Decoration.MAN_TRAP, Decoration.TANGLE_TRAP, Decoration.MARBLE_TRAP, Decoration.TELEPORT_TRAP),
    /**
     * Dungeon light build hotspot.
     */
    DUNGEON_LIGHT(Scenery.LIGHTING_SPACE_15330, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.CANDLE, Decoration.TORCH, Decoration.SKULL_TORCH),
    /**
     * Dungeon deco build hotspot.
     */
    DUNGEON_DECO(Scenery.DECORATION_SPACE_15331, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.DECORATIVE_BLOOD, Decoration.DECORATIVE_PIPE, Decoration.HANGING_SKELETON),
    /**
     * Dungeon guard build hotspot.
     */
    DUNGEON_GUARD(Scenery.GUARD_SPACE_15323, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.SKELETON_GUARD, Decoration.GUARD_DOG, Decoration.HOBGOBLIN, Decoration.BABY_RED_DRAGON, Decoration.HUGE_SPIDER, Decoration.TROLL, Decoration.HELLHOUND),
    /**
     * Dungeon guard 2 build hotspot.
     */
    DUNGEON_GUARD2(Scenery.GUARD_SPACE_15336, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.SKELETON_GUARD, Decoration.GUARD_DOG, Decoration.HOBGOBLIN, Decoration.BABY_RED_DRAGON, Decoration.HUGE_SPIDER, Decoration.TROLL, Decoration.HELLHOUND),
    /**
     * Dungeon guard 3 build hotspot.
     */
    DUNGEON_GUARD3(Scenery.GUARD_SPACE_15337, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.SKELETON_GUARD, Decoration.GUARD_DOG, Decoration.HOBGOBLIN, Decoration.BABY_RED_DRAGON, Decoration.HUGE_SPIDER, Decoration.TROLL, Decoration.HELLHOUND),
    /**
     * Dungeon light 2 build hotspot.
     */
    DUNGEON_LIGHT2(Scenery.LIGHTING_SPACE_34138, BuildHotspotType.RECURSIVE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.CANDLE, Decoration.TORCH, Decoration.SKULL_TORCH),
    /**
     * Dungeon pit guard build hotspot.
     */
    DUNGEON_PIT_GUARD(36676, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.PIT_DOG, Decoration.PIT_OGRE, Decoration.PIT_ROCK_PROTECTOR, Decoration.PIT_SCABARITE, Decoration.PIT_BLACK_DEMON, Decoration.PIT_IRON_DRAGON),

    /**
     * Monster build hotspot.
     */
    MONSTER(Scenery.MONSTER_SPACE_15257, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.DEMON, Decoration.KALPHITE_SOLDIER, Decoration.TOK_XIL, Decoration.DAGANNOTH, Decoration.STEEL_DRAGON),
    /**
     * Treasure chest 1 build hotspot.
     */
    TREASURE_CHEST1(Scenery.TREASURE_SPACE_15256, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.WOODEN_CRATE, Decoration.OAK_T_CHEST, Decoration.TEAK_T_CHEST, Decoration.MGANY_T_CHEST, Decoration.MAGIC_CHEST),
    /**
     * Wall decoration 1 build hotspot.
     */
    WALL_DECORATION1(Scenery.DECORATION_SPACE_15259, BuildHotspotType.CREST, BuildingUtils.BUILD_HIGH_ANIM, Decoration.ROUND_SHIELD, Decoration.SQUARE_SHIELD, Decoration.KITE_SHIELD),

    /**
     * Stairways build hotspot.
     */
    STAIRWAYS(Scenery.STAIR_SPACE_15380, BuildHotspotType.STAIRCASE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_STAIRCASE, Decoration.TEAK_STAIRCASE, Decoration.SPIRAL_STAIRCASE, Decoration.MARBLE_STAIRCASE, Decoration.MARBLE_SPIRAL),
    /**
     * Quest stairways build hotspot.
     */
    QUEST_STAIRWAYS(Scenery.STAIR_SPACE_15390, BuildHotspotType.STAIRCASE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_STAIRCASE, Decoration.TEAK_STAIRCASE, Decoration.SPIRAL_STAIRCASE, Decoration.MARBLE_STAIRCASE, Decoration.MARBLE_SPIRAL),
    /**
     * The Stairways dungeon.
     */
    STAIRWAYS_DUNGEON(new int[]{Scenery.STAIR_SPACE_15380, Scenery.STAIR_SPACE_15380, Scenery.STAIR_SPACE_15380, Scenery.STAIR_SPACE_15380, Scenery.STAIR_SPACE_15390, Scenery.STAIR_SPACE_15390}, BuildHotspotType.STAIRCASE, BuildingUtils.BUILD_HIGH_ANIM, Decoration.OAK_STAIRCASE, Decoration.TEAK_STAIRCASE, Decoration.SPIRAL_STAIRCASE, Decoration.MARBLE_STAIRCASE, Decoration.MARBLE_SPIRAL),

    /**
     * Stairs down build hotspot.
     */
    STAIRS_DOWN(Scenery.STAIR_SPACE_15381, BuildHotspotType.STAIRCASE, BuildingUtils.BUILD_LOW_ANIM, Decoration.OAK_STAIRS_DOWN, Decoration.TEAK_STAIRS_DOWN, Decoration.SPIRAL_STAIRS_DOWN, Decoration.MARBLE_STAIRS_DOWN, Decoration.MARBLE_SPIRAL_DOWN),
    /**
     * Stairs down 2 build hotspot.
     */
    STAIRS_DOWN2(Scenery.STAIR_SPACE_15391, BuildHotspotType.STAIRCASE, BuildingUtils.BUILD_LOW_ANIM, Decoration.OAK_STAIRS_DOWN, Decoration.TEAK_STAIRS_DOWN, Decoration.SPIRAL_STAIRS_DOWN, Decoration.MARBLE_STAIRS_DOWN, Decoration.MARBLE_SPIRAL_DOWN),

    /**
     * Window build hotspot.
     */
    WINDOW(Scenery.WINDOW_13830, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_HIGH_ANIM, Decoration.BASIC_WOOD_WINDOW, Decoration.BASIC_STONE_WINDOW, Decoration.WHITEWASHED_STONE_WINDOW, Decoration.FREMENNIK_WINDOW, Decoration.TROPICAL_WOOD_WINDOW, Decoration.FANCY_STONE_WINDOW),

    /**
     * Flatpack build hotspot.
     */
    FLATPACK(38427, BuildHotspotType.INDIVIDUAL, BuildingUtils.BUILD_MID_ANIM, Decoration.FLATPACK, Decoration.FLATPACK, Decoration.FLATPACK, Decoration.FLATPACK, Decoration.FLATPACK, Decoration.FLATPACK, Decoration.FLATPACK, Decoration.FLATPACK),
    ;

    private final int objectId;

    private final int[] objectIds;

    private final Decoration[] decorations;

    private final BuildHotspotType type;

    private final Animation buildingAnimation;

    private static final List<BuildHotspot[]> linkedHotspots = new ArrayList<BuildHotspot[]>();

    static {
        linkedHotspots.add(new BuildHotspot[]{RUG, RUG2, RUG3});
        linkedHotspots.add(new BuildHotspot[]{BEDROOM_RUG, BEDROOM_RUG2, BEDROOM_RUG3});
        linkedHotspots.add(new BuildHotspot[]{HALL_RUG, HALL_RUG2, HALL_RUG3});
        linkedHotspots.add(new BuildHotspot[]{CR_CORNER, CR_CORNER2, CR_CORNER3, CR_CORNER4, CR_RING,
            CR_RING2, CR_RING3, CR_RING4, CR_FLOOR, CR_FLOOR2, CR_FLOOR3, CR_FLOOR4, CR_FLOOR5,
            CR_FLOOR6, CR_FLOOR7, CR_FLOOR8, CR_INVISIBLE_WALL, CR_INVISIBLE_WALL2, CR_INVISIBLE_WALL3});
        linkedHotspots.add(new BuildHotspot[]{Q_HALL_RUG, Q_HALL_RUG2, Q_HALL_RUG3});
        linkedHotspots.add(new BuildHotspot[]{CHAPEL_RUG, CHAPEL_RUG2});
        linkedHotspots.add(new BuildHotspot[]{HEDGE1, HEDGE2, HEDGE3});
        linkedHotspots.add(new BuildHotspot[]{FLOOR_MID, FLOOR_SIDE, FLOOR_CORNER, OUBLIETTE_FLOOR, OUBLIETTE_FLOOR_1});
        linkedHotspots.add(new BuildHotspot[]{PRISON, PRISON_DOOR});
        linkedHotspots.add(new BuildHotspot[]{DUNGEON_DOOR_LEFT, DUNGEON_DOOR_RIGHT});
        linkedHotspots.add(new BuildHotspot[]{DUNGEON_DOOR_LEFT2, DUNGEON_DOOR_RIGHT2});
        linkedHotspots.add(new BuildHotspot[]{SMALL_PLANT_1, SMALL_PLANT1});
        linkedHotspots.add(new BuildHotspot[]{SHELVES, SHELVES_2});
    }

    private BuildHotspot(int objectId, BuildHotspotType type, Animation buildingAnimation, Decoration... decorations) {
        this.objectId = objectId;
        this.objectIds = null;
        this.type = type;
        this.buildingAnimation = buildingAnimation;
        this.decorations = decorations;
    }

    private BuildHotspot(int[] objectIds, BuildHotspotType type, Animation buildingAnimation, Decoration... decorations) {
        this.objectId = objectIds[0];
        this.objectIds = objectIds;
        this.type = type;
        this.buildingAnimation = buildingAnimation;
        this.decorations = decorations;
    }

    /**
     * For id build hotspot.
     *
     * @param id    the id
     * @param style the style
     * @return the build hotspot
     */
    public static BuildHotspot forId(int id, HousingStyle style) {
        for (BuildHotspot spot : values()) {
            if (spot.getObjectId(style) == id) {
                return spot;
            }
        }
        return null;
    }

    /**
     * Get linked hotspots build hotspot [ ].
     *
     * @param b the b
     * @return the build hotspot [ ]
     */
    public static BuildHotspot[] getLinkedHotspots(BuildHotspot b) {
        for (BuildHotspot[] list : linkedHotspots) {
            for (BuildHotspot bh : list) {
                if (bh == b) {
                    return list;
                }
            }
        }
        return null;
    }

    /**
     * Gets decoration index.
     *
     * @param deco the deco
     * @return the decoration index
     */
    public int getDecorationIndex(Decoration deco) {
        for (int i = 0; i < decorations.length; i++) {
            if (decorations[i] == deco) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets object id.
     *
     * @param style the style
     * @return the object id
     */
    public int getObjectId(HousingStyle style) {
        if (objectIds != null) {
            return objectIds[style.ordinal()];
        }
        return objectId;
    }

    /**
     * Gets object id.
     *
     * @return the object id
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public BuildHotspotType getType() {
        return type;
    }

    /**
     * Get decorations decoration [ ].
     *
     * @return the decoration [ ]
     */
    public Decoration[] getDecorations() {
        return decorations;
    }

    /**
     * Get object ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getObjectIds() {
        return objectIds;
    }

    /**
     * Gets building animation.
     *
     * @return the building animation
     */
    public Animation getBuildingAnimation() {
        return buildingAnimation;
    }

}
