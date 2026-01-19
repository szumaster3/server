package content.global.skill.construction.decoration.costumeroom

import core.game.node.entity.player.Player
import shared.consts.Items

/**
 * Represents a storable item in the Costume Room across all box types.
 */
enum class Storable(
    val displayId: Int,
    val takeIds: IntArray,
    val type: StorableType,
    val tier: Int = 0
) {

    ArenaBook(Items.ARENA_BOOK_6891, intArrayOf(Items.ARENA_BOOK_6891), type = StorableType.BOOK, 0),
    MyNotes(Items.MY_NOTES_11339, intArrayOf(Items.MY_NOTES_11339), type = StorableType.BOOK, 0),
    CrumblingTome(Items.CRUMBLING_TOME_4707, intArrayOf(Items.CRUMBLING_TOME_4707), type = StorableType.BOOK, 0),
    PieRecipeBook(Items.PIE_RECIPE_BOOK_7162, intArrayOf(Items.PIE_RECIPE_BOOK_7162), type = StorableType.BOOK, 0),
    GiannesCookBook(
        Items.GIANNES_COOK_BOOK_2167,
        intArrayOf(Items.GIANNES_COOK_BOOK_2167),
        type = StorableType.BOOK,
        0
    ),
    GameBook(Items.GAME_BOOK_7681, intArrayOf(Items.GAME_BOOK_7681), type = StorableType.BOOK, 0),
    StrongholdNotes(Items.STRONGHOLD_NOTES_9004, intArrayOf(Items.STRONGHOLD_NOTES_9004), type = StorableType.BOOK, 0),
    CocktailGuide(Items.COCKTAIL_GUIDE_2023, intArrayOf(Items.COCKTAIL_GUIDE_2023), type = StorableType.BOOK, 0),
    TarnsDiary(Items.TARNS_DIARY_10587, intArrayOf(Items.TARNS_DIARY_10587), type = StorableType.BOOK, 0),
    ConstructionGuide(
        Items.CONSTRUCTION_GUIDE_8463,
        intArrayOf(Items.CONSTRUCTION_GUIDE_8463),
        type = StorableType.BOOK,
        0
    ),
    GlassblowingBook(
        Items.GLASSBLOWING_BOOK_11656,
        intArrayOf(Items.GLASSBLOWING_BOOK_11656),
        type = StorableType.BOOK,
        0
    ),
    BrewinGuide(Items.BREWIN_GUIDE_8989, intArrayOf(Items.BREWIN_GUIDE_8989), type = StorableType.BOOK, 0),
    SecurityBook(Items.SECURITY_BOOK_9003, intArrayOf(Items.SECURITY_BOOK_9003), type = StorableType.BOOK, 0),
    QueenHelpBook(Items.QUEEN_HELP_BOOK_10562, intArrayOf(Items.QUEEN_HELP_BOOK_10562), type = StorableType.BOOK, 0),
    AbyssalBook(Items.ABYSSAL_BOOK_5520, intArrayOf(Items.ABYSSAL_BOOK_5520), type = StorableType.BOOK, 0),
    ExplorersNotes(Items.EXPLORERS_NOTES_11677, intArrayOf(Items.EXPLORERS_NOTES_11677), type = StorableType.BOOK, 0),
    GoblinBook(Items.GOBLIN_BOOK_10999, intArrayOf(Items.GOBLIN_BOOK_10999), type = StorableType.BOOK, 0),
    DwarvenLore(Items.DWARVEN_LORE_4568, intArrayOf(Items.DWARVEN_LORE_4568), type = StorableType.BOOK, 0),
    BookOPiracy(Items.BOOK_O_PIRACY_7144, intArrayOf(Items.BOOK_O_PIRACY_7144), type = StorableType.BOOK, 0),
    ClockworkBook(Items.CLOCKWORK_BOOK_10594, intArrayOf(Items.CLOCKWORK_BOOK_10594), type = StorableType.BOOK, 0),
    ScabariteNotes(Items.SCABARITE_NOTES_11975, intArrayOf(Items.SCABARITE_NOTES_11975), type = StorableType.BOOK, 0),
    Translation(Items.TRANSLATION_4655, intArrayOf(Items.TRANSLATION_4655), type = StorableType.BOOK, 0),
    BookOnChemicals(Items.BOOK_ON_CHEMICALS_711, intArrayOf(Items.BOOK_ON_CHEMICALS_711), type = StorableType.BOOK, 0),
    InstructionManual(Items.INSTRUCTION_MANUAL_5, intArrayOf(Items.INSTRUCTION_MANUAL_5), type = StorableType.BOOK, 0),
    BirdBook(Items.BIRD_BOOK_10173, intArrayOf(Items.BIRD_BOOK_10173), type = StorableType.BOOK, 0),
    FeatheredJournal(
        Items.FEATHERED_JOURNAL_10179,
        intArrayOf(Items.FEATHERED_JOURNAL_10179),
        type = StorableType.BOOK,
        0
    ),
    BatteredBook(Items.BATTERED_BOOK_2886, intArrayOf(Items.BATTERED_BOOK_2886), type = StorableType.BOOK, 0),
    BeatenBook(Items.BEATEN_BOOK_9717, intArrayOf(Items.BEATEN_BOOK_9717), type = StorableType.BOOK, 0),
    AHandwrittenBook(
        Items.A_HANDWRITTEN_BOOK_9627,
        intArrayOf(Items.A_HANDWRITTEN_BOOK_9627),
        type = StorableType.BOOK,
        0
    ),
    VarmensNotes(Items.VARMENS_NOTES_4616, intArrayOf(Items.VARMENS_NOTES_4616), type = StorableType.BOOK, 0),

    // Capes.
    LegendsCape(Items.CAPE_OF_LEGENDS_10635, intArrayOf(Items.CAPE_OF_LEGENDS_1052), type = StorableType.CAPE),
    ObsidianCape(Items.OBSIDIAN_CAPE_10636, intArrayOf(Items.OBSIDIAN_CAPE_6568), type = StorableType.CAPE),
    FireCape(Items.FIRE_CAPE_10637, intArrayOf(Items.FIRE_CAPE_6570), type = StorableType.CAPE),
    TeamCape(Items.TEAM_1_CAPE_10638, intArrayOf(Items.TEAM_10_CAPE_4333), type = StorableType.CAPE),
    AttackCape(Items.ATTACK_CAPE_10639, intArrayOf(Items.ATTACK_CAPE_9747), type = StorableType.CAPE),
    StrengthCape(Items.STRENGTH_CAPE_10640, intArrayOf(Items.STRENGTH_CAPE_9750), type = StorableType.CAPE),
    DefenceCape(Items.DEFENCE_CAPE_10641, intArrayOf(Items.DEFENCE_CAPE_9753), type = StorableType.CAPE),
    RangingCape(Items.RANGING_CAPE_10642, intArrayOf(Items.RANGING_CAPE_9756), type = StorableType.CAPE),
    PrayerCape(Items.PRAYER_CAPE_10643, intArrayOf(Items.PRAYER_CAPE_9759), type = StorableType.CAPE),
    MagicCape(Items.MAGIC_CAPE_10644, intArrayOf(Items.MAGIC_CAPE_9762), type = StorableType.CAPE),
    RunecraftCape(Items.RUNECRAFT_CAPE_10645, intArrayOf(Items.RUNECRAFT_CAPE_9765), type = StorableType.CAPE),
    HunterCape(Items.HUNTER_CAPE_10646, intArrayOf(Items.HUNTER_CAPE_9948), type = StorableType.CAPE),
    HitpointsCape(Items.HITPOINTS_CAPE_10647, intArrayOf(Items.HITPOINTS_CAPE_9768), type = StorableType.CAPE),
    AgilityCape(Items.AGILITY_CAPE_10648, intArrayOf(Items.AGILITY_CAPE_9771), type = StorableType.CAPE),
    HerbloreCape(Items.HERBLORE_CAPE_10649, intArrayOf(Items.HERBLORE_CAPE_9774), type = StorableType.CAPE),
    ThievingCape(Items.THIEVING_CAPE_10650, intArrayOf(Items.THIEVING_CAPE_9777), type = StorableType.CAPE),
    CraftingCape(Items.CRAFTING_CAPE_10651, intArrayOf(Items.CRAFTING_CAPE_9780), type = StorableType.CAPE),
    FletchingCape(Items.FLETCHING_CAPE_10652, intArrayOf(Items.FLETCHING_CAPE_9783), type = StorableType.CAPE),
    SlayerCape(Items.SLAYER_CAPE_10653, intArrayOf(Items.SLAYER_CAPE_9786), type = StorableType.CAPE),
    ConstructionCape(Items.CONSTRUCT_CAPE_10654, intArrayOf(Items.CONSTRUCT_CAPE_9789), type = StorableType.CAPE),
    MiningCape(Items.MINING_CAPE_10655, intArrayOf(Items.MINING_CAPE_9792), type = StorableType.CAPE),
    SmithingCape(Items.SMITHING_CAPE_10656, intArrayOf(Items.SMITHING_CAPE_9795), type = StorableType.CAPE),
    FishingCape(Items.FISHING_CAPE_10657, intArrayOf(Items.FISHING_CAPE_9798), type = StorableType.CAPE),
    CookingCape(Items.COOKING_CAPE_10658, intArrayOf(Items.COOKING_CAPE_9801), type = StorableType.CAPE),
    FiremakingCape(Items.FIREMAKING_CAPE_10659, intArrayOf(Items.FIREMAKING_CAPE_9804), type = StorableType.CAPE),
    WoodcuttingCape(Items.WOODCUTTING_CAPE_10660, intArrayOf(Items.WOODCUTTING_CAPE_9807), type = StorableType.CAPE),
    FarmingCape(Items.FARMING_CAPE_10661, intArrayOf(Items.FARMING_CAPE_9810), type = StorableType.CAPE),
    QuestCape(Items.QUEST_POINT_CAPE_10662, intArrayOf(Items.QUEST_POINT_CAPE_9813), type = StorableType.CAPE),
    SpottedCape(Items.SPOTTED_CAPE_10663, intArrayOf(Items.SPOTTED_CAPE_10663), type = StorableType.CAPE),
    SpottierCape(Items.SPOTTIER_CAPE_10664, intArrayOf(Items.SPOTTIER_CAPE_10664), type = StorableType.CAPE),

    // Toys.
    MimeCostume(
        Items.MIME_MASK_3057,
        intArrayOf(
            Items.MIME_MASK_3057,
            Items.MIME_TOP_3058,
            Items.MIME_LEGS_3059,
            Items.MIME_GLOVES_3060,
            Items.MIME_BOOTS_3061
        ),
        type = StorableType.FANCY
    ),
    RoyalFrogCostume(
        Items.PRINCE_TUNIC_6184,
        intArrayOf(
            Items.PRINCE_TUNIC_6184,
            Items.PRINCE_LEGGINGS_6185,
            Items.PRINCESS_BLOUSE_6186,
            Items.PRINCESS_SKIRT_6187
        ),
        type = StorableType.FANCY
    ),
    ZombieOutfit(
        Items.ZOMBIE_SHIRT_7592,
        intArrayOf(
            Items.ZOMBIE_MASK_7594,
            Items.ZOMBIE_SHIRT_7592,
            Items.ZOMBIE_TROUSERS_7593,
            Items.ZOMBIE_GLOVES_7595,
            Items.ZOMBIE_BOOTS_7596
        ),
        type = StorableType.FANCY
    ),
    CamoOutfit(
        Items.CAMO_TOP_6654,
        intArrayOf(Items.CAMO_HELMET_6656, Items.CAMO_TOP_6654, Items.CAMO_BOTTOMS_6655),
        type = StorableType.FANCY
    ),
    LederhosenOutfit(
        Items.LEDERHOSEN_TOP_6180,
        intArrayOf(Items.LEDERHOSEN_TOP_6180, Items.LEDERHOSEN_SHORTS_6181, Items.LEDERHOSEN_HAT_6182),
        type = StorableType.FANCY
    ),
    ShadeRobes(Items.SHADE_ROBE_546, intArrayOf(Items.SHADE_ROBE_546, Items.SHADE_ROBE_548), type = StorableType.FANCY),
    JackLanternMask(Items.JACK_LANTERN_MASK_9920, intArrayOf(Items.JACK_LANTERN_MASK_9920), type = StorableType.TOY),
    SkeletonBoots(Items.SKELETON_BOOTS_9921, intArrayOf(Items.SKELETON_BOOTS_9921), type = StorableType.TOY),
    SkeletonGloves(Items.SKELETON_GLOVES_9922, intArrayOf(Items.SKELETON_GLOVES_9922), type = StorableType.TOY),
    SkeletonLeggings(Items.SKELETON_LEGGINGS_9923, intArrayOf(Items.SKELETON_LEGGINGS_9923), type = StorableType.TOY),
    SkeletonShirt(Items.SKELETON_SHIRT_9924, intArrayOf(Items.SKELETON_SHIRT_9924), type = StorableType.TOY),
    SkeletonMask(Items.SKELETON_MASK_9925, intArrayOf(Items.SKELETON_MASK_9925), type = StorableType.TOY),
    EasterRing(Items.EASTER_RING_7927, intArrayOf(Items.EASTER_RING_7927), type = StorableType.TOY),
    ZombieHead(Items.ZOMBIE_HEAD_6722, intArrayOf(Items.ZOMBIE_HEAD_6722), type = StorableType.TOY),
    RubberChicken(Items.RUBBER_CHICKEN_4566, intArrayOf(Items.RUBBER_CHICKEN_4566), type = StorableType.TOY),
    YoYo(Items.YO_YO_4079, intArrayOf(Items.YO_YO_4079), type = StorableType.TOY),
    BunnyEars(Items.BUNNY_EARS_1037, intArrayOf(Items.BUNNY_EARS_1037), type = StorableType.TOY),
    Scythe(Items.SCYTHE_1419, intArrayOf(Items.SCYTHE_1419), type = StorableType.TOY),
    ReindeerHat(Items.REINDEER_HAT_10507, intArrayOf(Items.REINDEER_HAT_10507), type = StorableType.TOY),
    WintumberTree(Items.WINTUMBER_TREE_10508, intArrayOf(Items.WINTUMBER_TREE_10508), type = StorableType.TOY),
    BobbleHat(Items.BOBBLE_HAT_6856, intArrayOf(Items.BOBBLE_HAT_6856), type = StorableType.TOY),
    BobbleScarf(Items.BOBBLE_SCARF_6857, intArrayOf(Items.BOBBLE_SCARF_6857), type = StorableType.TOY),
    JesterHat(Items.JESTER_HAT_6858, intArrayOf(Items.JESTER_HAT_6858), type = StorableType.TOY),
    JesterScarf(Items.JESTER_SCARF_6859, intArrayOf(Items.JESTER_SCARF_6859), type = StorableType.TOY),
    TriJesterHat(Items.TRI_JESTER_HAT_6860, intArrayOf(Items.TRI_JESTER_HAT_6860), type = StorableType.TOY),
    TriJesterScarf(Items.TRI_JESTER_SCARF_6861, intArrayOf(Items.TRI_JESTER_SCARF_6861), type = StorableType.TOY),
    WoollyHat(Items.WOOLLY_HAT_6862, intArrayOf(Items.WOOLLY_HAT_6862), type = StorableType.TOY),
    WoollyScarf(Items.WOOLLY_SCARF_6863, intArrayOf(Items.WOOLLY_SCARF_6863), type = StorableType.TOY),
    ChickenFeet(Items.CHICKEN_FEET_11019, intArrayOf(Items.CHICKEN_FEET_11019), type = StorableType.TOY),
    ChickenWings(Items.CHICKEN_WINGS_11020, intArrayOf(Items.CHICKEN_WINGS_11020), type = StorableType.TOY),
    ChickenHead(Items.CHICKEN_HEAD_11021, intArrayOf(Items.CHICKEN_HEAD_11021), type = StorableType.TOY),
    ChickenLegs(Items.CHICKEN_LEGS_11022, intArrayOf(Items.CHICKEN_LEGS_11022), type = StorableType.TOY),
    GrimReaperHood(Items.GRIM_REAPER_HOOD_11789, intArrayOf(Items.GRIM_REAPER_HOOD_11789), type = StorableType.TOY),
    SnowGlobe(Items.SNOW_GLOBE_11949, intArrayOf(Items.SNOW_GLOBE_11949), type = StorableType.TOY),
    ChocaticeCape(Items.CHOCATRICE_CAPE_12645, intArrayOf(Items.CHOCATRICE_CAPE_12645), type = StorableType.TOY),
    WarlockTop(
        Items.WARLOCK_TOP_14076,
        intArrayOf(Items.WARLOCK_TOP_14076, Items.WITCH_TOP_14078),
        type = StorableType.TOY
    ),
    WarlockLegs(
        Items.WARLOCK_LEGS_14077,
        intArrayOf(Items.WARLOCK_LEGS_14077, Items.WITCH_SKIRT_14079),
        type = StorableType.TOY
    ),
    WarlockCloak(
        Items.WARLOCK_CLOAK_14081,
        intArrayOf(Items.WARLOCK_CLOAK_14081, Items.WITCH_CLOAK_14088),
        type = StorableType.TOY
    ),
    SantaCostumeTop(
        Items.SANTA_COSTUME_TOP_14595,
        intArrayOf(Items.SANTA_COSTUME_TOP_14595, Items.SANTA_COSTUME_TOP_14600),
        type = StorableType.TOY
    ),
    SantaCostumeGloves(
        Items.SANTA_COSTUME_GLOVES_14602,
        intArrayOf(Items.SANTA_COSTUME_GLOVES_14602),
        type = StorableType.TOY
    ),
    SantaCostumeLegs(
        Items.SANTA_COSTUME_LEGS_14603,
        intArrayOf(Items.SANTA_COSTUME_LEGS_14603, Items.SANTA_COSTUME_LEGS_14604),
        type = StorableType.TOY
    ),
    SantaCostumeBoots(
        Items.SANTA_COSTUME_BOOTS_14605,
        intArrayOf(Items.SANTA_COSTUME_BOOTS_14605),
        type = StorableType.TOY
    ),
    IceAmulet(Items.ICE_AMULET_14596, intArrayOf(Items.ICE_AMULET_14596), type = StorableType.TOY),
    Marionette(
        Items.BLUE_MARIONETTE_6865,
        intArrayOf(Items.BLUE_MARIONETTE_6865, Items.GREEN_MARIONETTE_6866, Items.RED_MARIONETTE_6867),
        type = StorableType.TOY
    ),

    // Treasure - low.
    TrimmedBlackArmour(
        Items.BLACK_PLATEBODY_T_2583,
        intArrayOf(
            Items.BLACK_FULL_HELMT_2587,
            Items.BLACK_PLATEBODY_T_2583,
            Items.BLACK_PLATELEGS_T_2585,
            Items.BLACK_KITESHIELD_T_2589
        ),
        type = StorableType.TRAILS,
        0
    ),
    GoldTrimmedBlackArmour(
        Items.BLACK_PLATEBODY_G_2591,
        intArrayOf(
            Items.BLACK_FULL_HELMG_2595,
            Items.BLACK_PLATEBODY_G_2591,
            Items.BLACK_PLATELEGS_G_2593,
            Items.BLACK_KITESHIELD_G_2597
        ),
        type = StorableType.TRAILS,
        0
    ),
    BlackHeraldicH1(Items.BLACK_HELM_H1_10306, intArrayOf(Items.BLACK_HELM_H1_10306), type = StorableType.TRAILS, 0),
    BlackHeraldicH2(Items.BLACK_HELM_H2_10308, intArrayOf(Items.BLACK_HELM_H2_10308), type = StorableType.TRAILS, 0),
    BlackHeraldicH3(Items.BLACK_HELM_H3_10310, intArrayOf(Items.BLACK_HELM_H3_10310), type = StorableType.TRAILS, 0),
    BlackHeraldicH4(Items.BLACK_HELM_H4_10312, intArrayOf(Items.BLACK_HELM_H4_10312), type = StorableType.TRAILS, 0),
    BlackHeraldicH5(Items.BLACK_HELM_H5_10314, intArrayOf(Items.BLACK_HELM_H5_10314), type = StorableType.TRAILS, 0),
    BlackHeraldicKiteshieldH1(
        Items.BLACK_SHIELDH1_7332,
        intArrayOf(Items.BLACK_SHIELDH1_7332),
        type = StorableType.TRAILS,
        0
    ),
    BlackHeraldicKiteshieldH2(
        Items.BLACK_SHIELDH2_7338,
        intArrayOf(Items.BLACK_SHIELDH2_7338),
        type = StorableType.TRAILS,
        0
    ),
    BlackHeraldicKiteshieldH3(
        Items.BLACK_SHIELDH3_7344,
        intArrayOf(Items.BLACK_SHIELDH3_7344),
        type = StorableType.TRAILS,
        0
    ),
    BlackHeraldicKiteshieldH4(
        Items.BLACK_SHIELDH4_7350,
        intArrayOf(Items.BLACK_SHIELDH4_7350),
        type = StorableType.TRAILS,
        0
    ),
    BlackHeraldicKiteshieldH5(
        Items.BLACK_SHIELDH5_7356,
        intArrayOf(Items.BLACK_SHIELDH5_7356),
        type = StorableType.TRAILS,
        0
    ),
    TrimmedStuddedArmour(
        Items.STUDDED_BODY_T_7364,
        intArrayOf(Items.STUDDED_BODY_T_7364, Items.STUDDED_CHAPS_T_7368),
        type = StorableType.TRAILS,
        0
    ),
    GoldTrimmedStuddedArmour(
        Items.STUDDED_BODY_G_7362,
        intArrayOf(Items.STUDDED_BODY_G_7362, Items.STUDDED_CHAPS_G_7366),
        type = StorableType.TRAILS,
        0
    ),
    TrimmedWizardRobes(
        Items.WIZARD_ROBE_T_7392,
        intArrayOf(Items.WIZARD_HAT_T_7396, Items.WIZARD_ROBE_T_7392, Items.BLUE_SKIRT_T_7388),
        type = StorableType.TRAILS,
        0
    ),
    GoldTrimmedWizardRobes(
        Items.WIZARD_ROBE_G_7390,
        intArrayOf(Items.WIZARD_HAT_G_7394, Items.WIZARD_ROBE_G_7390, Items.BLUE_SKIRT_G_7386),
        type = StorableType.TRAILS,
        0
    ),
    WizardBoots(Items.WIZARD_BOOTS_2579, intArrayOf(Items.WIZARD_BOOTS_2579), type = StorableType.TRAILS, 0),
    TrimmedAmuletOfMagic(
        Items.AMULET_OF_MAGICT_10366,
        intArrayOf(Items.AMULET_OF_MAGICT_10366),
        type = StorableType.TRAILS,
        0
    ),
    HighwaymanMask(Items.HIGHWAYMAN_MASK_2631, intArrayOf(Items.HIGHWAYMAN_MASK_2631), type = StorableType.TRAILS, 0),
    Pantaloons(Items.PANTALOONS_10396, intArrayOf(Items.PANTALOONS_10396), type = StorableType.TRAILS, 0),
    PowderedWig(Items.A_POWDERED_WIG_10392, intArrayOf(Items.A_POWDERED_WIG_10392), type = StorableType.TRAILS, 0),
    FlaredTrousers(Items.FLARED_TROUSERS_10394, intArrayOf(Items.FLARED_TROUSERS_10394), type = StorableType.TRAILS, 0),
    SleepingCap(Items.SLEEPING_CAP_10398, intArrayOf(Items.SLEEPING_CAP_10398), type = StorableType.TRAILS, 0),
    BlackBeret(Items.BLACK_BERET_2635, intArrayOf(Items.BLACK_BERET_2635), type = StorableType.TRAILS, 0),
    WhiteBeret(Items.WHITE_BERET_2637, intArrayOf(Items.WHITE_BERET_2637), type = StorableType.TRAILS, 0),
    BlueBeret(Items.BLUE_BERET_2633, intArrayOf(Items.BLUE_BERET_2633), type = StorableType.TRAILS, 0),
    BobTheCatShirt(Items.BOB_SHIRT_10317, intArrayOf(Items.BOB_SHIRT_10317), type = StorableType.TRAILS, 0),
    BobTheCatShirt1(Items.BOB_SHIRT_10319, intArrayOf(Items.BOB_SHIRT_10319), type = StorableType.TRAILS, 0),
    BobTheCatShirt2(Items.BOB_SHIRT_10321, intArrayOf(Items.BOB_SHIRT_10321), type = StorableType.TRAILS, 0),
    BobTheCatShirt3(Items.BOB_SHIRT_10323, intArrayOf(Items.BOB_SHIRT_10323), type = StorableType.TRAILS, 0),
    BobTheCatShirt4(Items.BOB_SHIRT_10325, intArrayOf(Items.BOB_SHIRT_10325), type = StorableType.TRAILS, 0),
    BlackElegant(
        Items.BLACK_ELE_SHIRT_10400,
        intArrayOf(Items.BLACK_ELE_SHIRT_10400, Items.BLACK_ELE_LEGS_10402),
        type = StorableType.TRAILS,
        0
    ),
    RedElegant(
        Items.RED_ELE_SHIRT_10404,
        intArrayOf(Items.RED_ELE_SHIRT_10404, Items.RED_ELE_SHIRT_10405, Items.RED_ELE_LEGS_10406),
        type = StorableType.TRAILS,
        0
    ),
    BlueElegant(
        Items.BLUE_ELE_SHIRT_10408,
        intArrayOf(Items.BLUE_ELE_SHIRT_10408, Items.BLUE_ELE_LEGS_10410),
        type = StorableType.TRAILS,
        0
    ),
    GreenElegant(
        Items.GREEN_ELE_SHIRT_10412,
        intArrayOf(Items.GREEN_ELE_SHIRT_10412, Items.GREEN_ELE_LEGS_10414),
        type = StorableType.TRAILS,
        0
    ),
    PurpleElegant(
        Items.PURPLE_ELE_SHIRT_10416,
        intArrayOf(Items.PURPLE_ELE_SHIRT_10416, Items.PURPLE_ELE_LEGS_10418),
        type = StorableType.TRAILS,
        0
    ),
    BeretAndMask(Items.BERET_AND_MASK_11282, intArrayOf(Items.BERET_AND_MASK_11282), type = StorableType.TRAILS, 0),
    BlackCane(Items.BLACK_CANE_13095, intArrayOf(Items.BLACK_CANE_13095), type = StorableType.TRAILS, 0),
    SpikedHelmet(Items.SPIKED_HELMET_13105, intArrayOf(Items.SPIKED_HELMET_13105), type = StorableType.TRAILS, 0),

    // Treasure - medium.
    TrimmedAdamantiteArmour(
        Items.ADAM_PLATEBODY_T_2599,
        intArrayOf(
            Items.ADAM_FULL_HELMT_2605,
            Items.ADAM_PLATEBODY_T_2599,
            Items.ADAM_PLATELEGS_T_2601,
            Items.ADAM_KITESHIELD_T_2603
        ),
        type = StorableType.TRAILS,
        1
    ),
    GoldTrimmedAdamantiteArmour(
        Items.ADAM_PLATEBODY_G_2607,
        intArrayOf(
            Items.ADAM_FULL_HELMG_2613,
            Items.ADAM_PLATEBODY_G_2607,
            Items.ADAM_PLATELEGS_G_2609,
            Items.ADAM_KITESHIELD_G_2611
        ),
        type = StorableType.TRAILS,
        1
    ),
    AdamantHeraldicH1(
        Items.ADAMANT_HELM_H1_10296,
        intArrayOf(Items.ADAMANT_HELM_H1_10296),
        type = StorableType.TRAILS,
        1
    ),
    AdamantHeraldicH2(
        Items.ADAMANT_HELM_H2_10298,
        intArrayOf(Items.ADAMANT_HELM_H2_10298),
        type = StorableType.TRAILS,
        1
    ),
    AdamantHeraldicH3(
        Items.ADAMANT_HELM_H3_10300,
        intArrayOf(Items.ADAMANT_HELM_H3_10300),
        type = StorableType.TRAILS,
        1
    ),
    AdamantHeraldicH4(
        Items.ADAMANT_HELM_H4_10302,
        intArrayOf(Items.ADAMANT_HELM_H4_10302),
        type = StorableType.TRAILS,
        1
    ),
    AdamantHeraldicH5(
        Items.ADAMANT_HELM_H5_10304,
        intArrayOf(Items.ADAMANT_HELM_H5_10304),
        type = StorableType.TRAILS,
        1
    ),
    AdamantKiteshieldH1(
        Items.ADAMANT_SHIELDH1_7334,
        intArrayOf(Items.ADAMANT_SHIELDH1_7334),
        type = StorableType.TRAILS,
        1
    ),
    AdamantKiteshieldH2(
        Items.ADAMANT_SHIELDH2_7340,
        intArrayOf(Items.ADAMANT_SHIELDH2_7340),
        type = StorableType.TRAILS,
        1
    ),
    AdamantKiteshieldH3(
        Items.ADAMANT_SHIELDH3_7346,
        intArrayOf(Items.ADAMANT_SHIELDH3_7346),
        type = StorableType.TRAILS,
        1
    ),
    AdamantKiteshieldH4(
        Items.ADAMANT_SHIELDH4_7352,
        intArrayOf(Items.ADAMANT_SHIELDH4_7352),
        type = StorableType.TRAILS,
        1
    ),
    AdamantKiteshieldH5(
        Items.ADAMANT_SHIELDH5_7358,
        intArrayOf(Items.ADAMANT_SHIELDH5_7358),
        type = StorableType.TRAILS,
        1
    ),
    TrimmedGreenDhide(
        Items.DHIDE_BODY_T_7372,
        intArrayOf(Items.DHIDE_BODY_T_7372, Items.DHIDE_CHAPS_T_7380),
        type = StorableType.TRAILS,
        1
    ),
    GoldTrimmedGreenDhide(
        Items.DHIDE_BODY_G_7374,
        intArrayOf(Items.DHIDE_BODY_G_7374, Items.DHIDE_CHAPS_G_7378),
        type = StorableType.TRAILS,
        1
    ),
    RangerBoots(Items.RANGER_BOOTS_2577, intArrayOf(Items.RANGER_BOOTS_2577), type = StorableType.TRAILS, 1),
    TrimmedAmuletOfStrength(
        Items.STRENGTH_AMULETT_10364,
        intArrayOf(Items.STRENGTH_AMULETT_10364),
        type = StorableType.TRAILS,
        1
    ),
    RedHeadband(Items.RED_HEADBAND_2645, intArrayOf(Items.RED_HEADBAND_2645), type = StorableType.TRAILS, 1),
    BlackHeadband(Items.BLACK_HEADBAND_2647, intArrayOf(Items.BLACK_HEADBAND_2647), type = StorableType.TRAILS, 1),
    BrownHeadband(Items.BROWN_HEADBAND_2649, intArrayOf(Items.BROWN_HEADBAND_2649), type = StorableType.TRAILS, 1),
    RedBoater(Items.RED_BOATER_7319, intArrayOf(Items.RED_BOATER_7319), type = StorableType.TRAILS, 1),
    OrangeBoater(Items.ORANGE_BOATER_7321, intArrayOf(Items.ORANGE_BOATER_7321), type = StorableType.TRAILS, 1),
    GreenBoater(Items.GREEN_BOATER_7323, intArrayOf(Items.GREEN_BOATER_7323), type = StorableType.TRAILS, 1),
    BlueBoater(Items.BLUE_BOATER_7325, intArrayOf(Items.BLUE_BOATER_7325), type = StorableType.TRAILS, 1),
    BlackBoater(Items.BLACK_BOATER_7327, intArrayOf(Items.BLACK_BOATER_7327), type = StorableType.TRAILS, 1),
    BlackElegantBlouse(
        Items.BLACK_ELE_SHIRT_10400,
        intArrayOf(Items.BLACK_ELE_SHIRT_10400, Items.BLACK_ELE_LEGS_10402),
        type = StorableType.TRAILS,
        1
    ),
    RedElegantBlouse(
        Items.RED_ELE_BLOUSE_10424,
        intArrayOf(Items.RED_ELE_BLOUSE_10424, Items.RED_ELE_SKIRT_10426, Items.RED_ELE_LEGS_10406),
        type = StorableType.TRAILS,
        1
    ),
    BlueElegantBlouse(
        Items.BLUE_ELE_BLOUSE_10428,
        intArrayOf(Items.BLUE_ELE_BLOUSE_10428, Items.BLUE_ELE_SKIRT_10430),
        type = StorableType.TRAILS,
        1
    ),
    GreenElegantBlouse(
        Items.GREEN_ELE_BLOUSE_10432,
        intArrayOf(Items.GREEN_ELE_BLOUSE_10432, Items.GREEN_ELE_SKIRT_10434),
        type = StorableType.TRAILS,
        1
    ),
    PurpleElegantBlouse(
        Items.PURPLE_ELE_BLOUSE_10436,
        intArrayOf(Items.PURPLE_ELE_BLOUSE_10436, Items.PURPLE_ELE_SKIRT_10438),
        type = StorableType.TRAILS,
        1
    ),
    AdamantCane(Items.ADAMANT_CANE_13097, intArrayOf(Items.ADAMANT_CANE_13097), type = StorableType.TRAILS, 1),
    SheepMask(Items.SHEEP_MASK_13107, intArrayOf(Items.SHEEP_MASK_13107), type = StorableType.TRAILS, 1),
    BatMask(Items.BAT_MASK_13111, intArrayOf(Items.BAT_MASK_13111), type = StorableType.TRAILS, 1),
    WolfMask(Items.WOLF_MASK_13115, intArrayOf(Items.WOLF_MASK_13115), type = StorableType.TRAILS, 1),
    PenguinMask(Items.PENGUIN_MASK_13109, intArrayOf(Items.PENGUIN_MASK_13109), type = StorableType.TRAILS, 1),
    CatMask(Items.CAT_MASK_13113, intArrayOf(Items.CAT_MASK_13113), type = StorableType.TRAILS, 1),
    PithHelmet(Items.PITH_HELMET_13103, intArrayOf(Items.PITH_HELMET_13103), type = StorableType.TRAILS, 1),

    // Treasure - hard.
    TrimmedRuneArmour(
        Items.RUNE_PLATEBODY_T_2623,
        intArrayOf(
            Items.RUNE_FULL_HELM_T_2627,
            Items.RUNE_PLATEBODY_T_2623,
            Items.RUNE_PLATELEGS_T_2625,
            Items.RUNE_KITESHIELD_T_2629
        ),
        type = StorableType.TRAILS,
        2
    ),
    GoldTrimmedRuneArmour(
        Items.RUNE_PLATEBODY_G_2615,
        intArrayOf(
            Items.RUNE_FULL_HELMG_2619,
            Items.RUNE_PLATEBODY_G_2615,
            Items.RUNE_PLATELEGS_G_2617,
            Items.RUNE_KITESHIELD_G_2621
        ),
        type = StorableType.TRAILS,
        2
    ),
    GildedArmour(
        Items.GILDED_PLATEBODY_3481,
        intArrayOf(
            Items.GILDED_FULL_HELM_3486,
            Items.GILDED_PLATEBODY_3481,
            Items.GILDED_PLATELEGS_3483,
            Items.GILDED_KITESHIELD_3488
        ),
        type = StorableType.TRAILS,
        2
    ),
    RuneHelmH1(Items.RUNE_HELM_H1_10286, intArrayOf(Items.RUNE_HELM_H1_10286), type = StorableType.TRAILS, 2),
    RuneHelmH2(Items.RUNE_HELM_H2_10288, intArrayOf(Items.RUNE_HELM_H2_10288), type = StorableType.TRAILS, 2),
    RuneHelmH3(Items.RUNE_HELM_H3_10290, intArrayOf(Items.RUNE_HELM_H3_10290), type = StorableType.TRAILS, 2),
    RuneHelmH4(Items.RUNE_HELM_H4_10292, intArrayOf(Items.RUNE_HELM_H4_10292), type = StorableType.TRAILS, 2),
    RuneHelmH5(Items.RUNE_HELM_H5_10294, intArrayOf(Items.RUNE_HELM_H5_10294), type = StorableType.TRAILS, 2),
    RuneShieldH1(Items.RUNE_SHIELDH1_7336, intArrayOf(Items.RUNE_SHIELDH1_7336), type = StorableType.TRAILS, 2),
    RuneShieldH2(Items.RUNE_SHIELDH2_7342, intArrayOf(Items.RUNE_SHIELDH2_7342), type = StorableType.TRAILS, 2),
    RuneShieldH3(Items.RUNE_SHIELDH3_7348, intArrayOf(Items.RUNE_SHIELDH3_7348), type = StorableType.TRAILS, 2),
    RuneShieldH4(Items.RUNE_SHIELDH4_7354, intArrayOf(Items.RUNE_SHIELDH4_7354), type = StorableType.TRAILS, 2),
    RuneShieldH5(Items.RUNE_SHIELDH5_7360, intArrayOf(Items.RUNE_SHIELDH5_7360), type = StorableType.TRAILS, 2),
    ZamorakRuneArmour(
        Items.ZAMORAK_PLATEBODY_2653,
        intArrayOf(
            Items.ZAMORAK_FULL_HELM_2657,
            Items.ZAMORAK_PLATEBODY_2653,
            Items.ZAMORAK_PLATELEGS_2655,
            Items.ZAMORAK_KITESHIELD_2659
        ),
        type = StorableType.TRAILS,
        2
    ),
    SaradominRuneArmour(
        Items.SARADOMIN_PLATEBODY_2661,
        intArrayOf(
            Items.SARADOMIN_FULL_HELM_2665,
            Items.SARADOMIN_PLATEBODY_2661,
            Items.SARADOMIN_PLATELEGS_2663,
            Items.SARADOMIN_KITESHIELD_2667
        ),
        type = StorableType.TRAILS,
        2
    ),
    GuthixRuneArmour(
        Items.GUTHIX_PLATEBODY_2669,
        intArrayOf(
            Items.GUTHIX_FULL_HELM_2673,
            Items.GUTHIX_PLATEBODY_2669,
            Items.GUTHIX_PLATELEGS_2671,
            Items.GUTHIX_KITESHIELD_2675
        ),
        type = StorableType.TRAILS,
        2
    ),
    TrimmedBlueDhide(
        Items.DHIDE_BODY_T_7373,
        intArrayOf(Items.DHIDE_BODY_T_7373, Items.DHIDE_CHAPS_T_7384),
        type = StorableType.TRAILS,
        2
    ),
    GoldTrimmedBlueDhide(
        Items.DHIDE_BODY_G_7374,
        intArrayOf(Items.DHIDE_BODY_G_7374, Items.DHIDE_CHAPS_G_7382),
        type = StorableType.TRAILS,
        2
    ),
    SaradominBlessedDhide(
        Items.SARADOMIN_DHIDE_10386,
        intArrayOf(Items.SARADOMIN_DHIDE_10386, Items.SARADOMIN_CHAPS_10388, Items.SARADOMIN_BRACERS_10384),
        type = StorableType.TRAILS,
        2
    ),
    GuthixBlessedDhide(
        Items.GUTHIX_DRAGONHIDE_10378,
        intArrayOf(Items.GUTHIX_DRAGONHIDE_10378, Items.GUTHIX_CHAPS_10380, Items.GUTHIX_BRACERS_10376),
        type = StorableType.TRAILS,
        2
    ),
    ZamorakBlessedDhide(
        Items.ZAMORAK_DHIDE_10370,
        intArrayOf(Items.ZAMORAK_DHIDE_10370, Items.ZAMORAK_CHAPS_10372, Items.ZAMORAK_BRACERS_10368),
        type = StorableType.TRAILS,
        2
    ),
    RobinHoodHat(Items.ROBIN_HOOD_HAT_2581, intArrayOf(Items.ROBIN_HOOD_HAT_2581), type = StorableType.TRAILS, 2),
    EnchantedRobes(
        Items.ENCHANTED_TOP_7399,
        intArrayOf(Items.ENCHANTED_HAT_7400, Items.ENCHANTED_TOP_7399, Items.ENCHANTED_ROBE_7398),
        type = StorableType.TRAILS,
        2
    ),
    SaradominVestments(
        Items.SARADOMIN_ROBE_TOP_10458,
        intArrayOf(
            Items.SARADOMIN_MITRE_10452,
            Items.SARADOMIN_ROBE_TOP_10458,
            Items.SARADOMIN_ROBE_LEGS_10464,
            Items.SARADOMIN_STOLE_10470,
            Items.SARADOMIN_CLOAK_10447
        ),
        type = StorableType.TRAILS,
        2
    ),
    ZamorakVestments(
        Items.ZAMORAK_ROBE_TOP_10460,
        intArrayOf(
            Items.ZAMORAK_MITRE_10456,
            Items.ZAMORAK_ROBE_TOP_10460,
            Items.ZAMORAK_ROBE_LEGS_10468,
            Items.ZAMORAK_STOLE_10474,
            Items.ZAMORAK_CLOAK_10450
        ),
        type = StorableType.TRAILS,
        2
    ),
    GuthixVestments(
        Items.GUTHIX_ROBE_TOP_10462,
        intArrayOf(
            Items.GUTHIX_MITRE_10454,
            Items.GUTHIX_ROBE_TOP_10462,
            Items.GUTHIX_ROBE_LEGS_10466,
            Items.GUTHIX_STOLE_10472,
            Items.GUTHIX_CLOAK_10448
        ),
        type = StorableType.TRAILS,
        2
    ),
    TrimmedAmuletOfGlory(
        Items.AMULET_OF_GLORYT_10362,
        intArrayOf(Items.AMULET_OF_GLORYT_10362),
        type = StorableType.TRAILS,
        2
    ),
    PiratesHat(Items.PIRATES_HAT_2651, intArrayOf(Items.PIRATES_HAT_2651), type = StorableType.TRAILS, 2),
    CavalierMask(
        Items.CAVALIER_AND_MASK_11280,
        intArrayOf(Items.CAVALIER_AND_MASK_11280),
        type = StorableType.TRAILS,
        2
    ),
    DarkCavalier(Items.DARK_CAVALIER_2641, intArrayOf(Items.DARK_CAVALIER_2641), type = StorableType.TRAILS, 2),
    TanCavalier(Items.TAN_CAVALIER_2639, intArrayOf(Items.TAN_CAVALIER_2639), type = StorableType.TRAILS, 2),
    BlackCavalier(Items.BLACK_CAVALIER_2643, intArrayOf(Items.BLACK_CAVALIER_2643), type = StorableType.TRAILS, 2),
    RuneCane(Items.RUNE_CANE_13099, intArrayOf(Items.RUNE_CANE_13099), type = StorableType.TRAILS, 2),
    TopHat(Items.TOP_HAT_13101, intArrayOf(Items.TOP_HAT_13101), type = StorableType.TRAILS, 2),

    // Magic wardrobe.
    MysticBlueSet(
        Items.MYSTIC_HAT_4089,
        intArrayOf(
            Items.MYSTIC_HAT_4089,
            Items.MYSTIC_ROBE_TOP_4091,
            Items.MYSTIC_ROBE_BOTTOM_4093,
            Items.MYSTIC_GLOVES_4095,
            Items.MYSTIC_BOOTS_4098
        ),
        type = StorableType.ARMOUR,
        0
    ),
    MysticDarkSet(
        Items.MYSTIC_HAT_4099,
        intArrayOf(
            Items.MYSTIC_HAT_4099,
            Items.MYSTIC_ROBE_TOP_4101,
            Items.MYSTIC_ROBE_BOTTOM_4103,
            Items.MYSTIC_GLOVES_4105,
            Items.MYSTIC_BOOTS_4107
        ),
        type = StorableType.ARMOUR,
        0
    ),
    MysticLightSet(
        Items.MYSTIC_HAT_4109,
        intArrayOf(
            Items.MYSTIC_HAT_4109,
            Items.MYSTIC_ROBE_TOP_4111,
            Items.MYSTIC_ROBE_BOTTOM_4113,
            Items.MYSTIC_GLOVES_4115,
            Items.MYSTIC_BOOTS_4117
        ),
        type = StorableType.ARMOUR,
        0
    ),
    SkeletalSet(
        Items.SKELETAL_HELM_6137,
        intArrayOf(
            Items.SKELETAL_HELM_6137,
            Items.SKELETAL_TOP_6139,
            Items.SKELETAL_BOTTOMS_6141,
            Items.SKELETAL_GLOVES_6153,
            Items.SKELETAL_BOOTS_6147
        ),
        type = StorableType.ARMOUR,
        1
    ),
    InfinitySet(
        Items.INFINITY_TOP_6916,
        intArrayOf(
            Items.INFINITY_HAT_6918,
            Items.INFINITY_TOP_6916,
            Items.INFINITY_BOTTOMS_6924,
            Items.INFINITY_GLOVES_6922,
            Items.INFINITY_BOOTS_6920
        ),
        type = StorableType.ARMOUR,
        1
    ),
    SplitbarkSet(
        Items.SPLITBARK_HELM_3385,
        intArrayOf(
            Items.SPLITBARK_HELM_3385,
            Items.SPLITBARK_BODY_3387,
            Items.SPLITBARK_LEGS_3389,
            Items.SPLITBARK_GAUNTLETS_3391,
            Items.SPLITBARK_BOOTS_3393
        ),
        type = StorableType.ARMOUR,
        2
    ),
    GhostlySet(
        Items.GHOSTLY_BOOTS_6106,
        intArrayOf(
            Items.GHOSTLY_ROBE_6107,
            Items.GHOSTLY_ROBE_6108,
            Items.GHOSTLY_HOOD_6109,
            Items.GHOSTLY_GLOVES_6110,
            Items.GHOSTLY_BOOTS_6106
        ),
        type = StorableType.ARMOUR,
        2
    ),
    MoonclanSet(
        Items.MOONCLAN_HAT_9069,
        intArrayOf(
            Items.MOONCLAN_HELM_9068,
            Items.MOONCLAN_ARMOUR_9070,
            Items.MOONCLAN_SKIRT_9071,
            Items.MOONCLAN_GLOVES_9072,
            Items.MOONCLAN_BOOTS_9073
        ),
        type = StorableType.ARMOUR,
        3
    ),
    LunarSet(
        Items.LUNAR_HELM_9096,
        intArrayOf(
            Items.LUNAR_HELM_9096,
            Items.LUNAR_TORSO_9097,
            Items.LUNAR_LEGS_9098,
            Items.LUNAR_GLOVES_9099,
            Items.LUNAR_BOOTS_9100,
            Items.LUNAR_CAPE_9101,
            Items.LUNAR_AMULET_9102,
            Items.LUNAR_RING_9104
        ),
        type = StorableType.ARMOUR,
        3
    ),
    RunecrafterYellow(
        Items.RUNECRAFTER_HAT_13615,
        intArrayOf(Items.RUNECRAFTER_HAT_13615, Items.RUNECRAFTER_ROBE_13614, Items.RUNECRAFTER_SKIRT_13617),
        type = StorableType.ARMOUR,
        4
    ),
    RunecrafterGreen(
        Items.RUNECRAFTER_HAT_13620,
        intArrayOf(Items.RUNECRAFTER_HAT_13620, Items.RUNECRAFTER_ROBE_13619, Items.RUNECRAFTER_SKIRT_13622),
        type = StorableType.ARMOUR,
        4
    ),
    RunecrafterBlue(
        Items.RUNECRAFTER_HAT_13625,
        intArrayOf(Items.RUNECRAFTER_HAT_13625, Items.RUNECRAFTER_ROBE_13624, Items.RUNECRAFTER_SKIRT_13627),
        type = StorableType.ARMOUR,
        5
    ),
    DagonHaiSet(
        Items.DAGONHAI_ROBE_TOP_14497,
        intArrayOf(Items.DAGONHAI_HAT_14499, Items.DAGONHAI_ROBE_TOP_14497, Items.DAGONHAI_ROBE_BOTTOM_14501),
        type = StorableType.ARMOUR,
        5
    ),

    // Armour case.
    WhiteKnightArmour(
        Items.WHITE_PLATEBODY_6617,
        intArrayOf(Items.WHITE_PLATEBODY_6617, Items.WHITE_FULL_HELM_6623, Items.WHITE_PLATELEGS_6625),
        type = StorableType.ARMOUR_CASE,
        0
    ),
    DecorativeArmour(
        Items.DECORATIVE_ARMOUR_4069,
        intArrayOf(
            Items.DECORATIVE_ARMOUR_4069,
            Items.DECORATIVE_SWORD_4068,
            Items.DECORATIVE_ARMOUR_4070,
            Items.DECORATIVE_HELM_4071,
            Items.DECORATIVE_SHIELD_4072
        ),
        type = StorableType.ARMOUR_CASE,
        0
    ),
    VoidKnightArmour(
        Items.VOID_KNIGHT_TOP_8839,
        intArrayOf(
            Items.VOID_KNIGHT_TOP_8839,
            Items.VOID_KNIGHT_ROBE_8840,
            Items.VOID_KNIGHT_GLOVES_8842,
            Items.VOID_MAGE_HELM_11663,
            Items.VOID_RANGER_HELM_11664,
            Items.VOID_MELEE_HELM_11665
        ),
        type = StorableType.ARMOUR_CASE,
        1
    ),
    RogueArmour(
        Items.ROGUE_MASK_5554,
        intArrayOf(Items.ROGUE_MASK_5554, Items.ROGUE_TOP_5553, Items.ROGUE_TROUSERS_5555),
        type = StorableType.ARMOUR_CASE,
        1
    ),
    SpinedArmour(
        Items.SPINED_HELM_6131,
        intArrayOf(Items.SPINED_HELM_6131, Items.SPINED_BODY_6133, Items.SPINED_CHAPS_6135),
        type = StorableType.ARMOUR_CASE,
        1
    ),
    RockshellArmour(
        Items.ROCK_SHELL_HELM_6128,
        intArrayOf(Items.ROCK_SHELL_HELM_6128, Items.ROCK_SHELL_PLATE_6129, Items.ROCK_SHELL_LEGS_6130),
        type = StorableType.ARMOUR_CASE,
        1
    ),
    TribalMask(
        Items.TRIBAL_MASK_6335,
        intArrayOf(Items.TRIBAL_MASK_6335, Items.TRIBAL_MASK_6337, Items.TRIBAL_MASK_6339),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    TempleKnightInitiateArmour(
        Items.INITIATE_HAUBERK_5575,
        intArrayOf(Items.INITIATE_SALLET_5574, Items.INITIATE_HAUBERK_5575, Items.INITIATE_CUISSE_5576),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    TempleKnightProselyteArmour(
        Items.PROSELYTE_HAUBERK_9674,
        intArrayOf(Items.PROSELYTE_SALLET_9672, Items.PROSELYTE_HAUBERK_9674, Items.PROSELYTE_CUISSE_9676),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    MournerGear(
        Items.MOURNER_TOP_6065,
        intArrayOf(
            Items.MOURNER_TOP_6065,
            Items.MOURNER_TROUSERS_6066,
            Items.MOURNER_BOOTS_6069,
            Items.MOURNER_CLOAK_6070
        ),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    GraahkHunterGear(
        Items.GRAAHK_TOP_10049,
        intArrayOf(Items.GRAAHK_TOP_10049, Items.GRAAHK_LEGS_10047),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    LarupiaHunterGear(
        Items.LARUPIA_TOP_10043,
        intArrayOf(Items.LARUPIA_TOP_10043, Items.LARUPIA_LEGS_10041),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    KyattHunterGear(
        Items.KYATT_TOP_10037,
        intArrayOf(Items.KYATT_TOP_10037, Items.KYATT_LEGS_10035),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    PolarCamouflageGear(
        Items.POLAR_CAMO_TOP_10065,
        intArrayOf(Items.POLAR_CAMO_TOP_10628, Items.POLAR_CAMO_LEGS_10067),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    JungleCamouflageGear(
        Items.JUNGLE_CAMO_TOP_10057,
        intArrayOf(Items.JUNGLE_CAMO_TOP_10057, Items.JUNGLE_CAMO_LEGS_10059),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    WoodCamouflageGear(
        Items.WOOD_CAMO_TOP_10053,
        intArrayOf(Items.WOOD_CAMO_TOP_10053, Items.WOOD_CAMO_LEGS_10055),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    DesertCamouflageGear(
        Items.DESERT_CAMO_TOP_10061,
        intArrayOf(Items.DESERT_CAMO_TOP_10061, Items.DESERT_CAMO_LEGS_10063),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    BuildersCostume(
        Items.HARD_HAT_10862,
        intArrayOf(Items.BUILDERS_SHIRT_10863, Items.BUILDERS_TROUSERS_10864, Items.BUILDERS_BOOTS_10865),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    LumberjackCostume(
        Items.LUMBERJACK_TOP_10939,
        intArrayOf(Items.LUMBERJACK_TOP_10939, Items.LUMBERJACK_LEGS_10940, Items.LUMBERJACK_HAT_10941),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    BomberJacketCostume(
        Items.BOMBER_JACKET_9944,
        intArrayOf(Items.BOMBER_JACKET_9944, Items.BOMBER_CAP_9945),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    HAMRobes(
        Items.HAM_SHIRT_4298,
        intArrayOf(
            Items.HAM_SHIRT_4298,
            Items.HAM_ROBE_4300,
            Items.HAM_HOOD_4302,
            Items.HAM_CLOAK_4304,
            Items.HAM_LOGO_4306
        ),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    RedTricorn(
        Items.RED_TRICORN_HAT_13190,
        intArrayOf(Items.RED_TRICORN_HAT_8961, Items.RED_NAVAL_SHIRT_8954, Items.RED_NAVY_SLACKS_8993),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    BlueTricorn(
        Items.BLUE_TRICORN_HAT_13188,
        intArrayOf(Items.BLUE_TRICORN_HAT_8959, Items.BLUE_NAVAL_SHIRT_8952, Items.BLUE_NAVY_SLACKS_8991),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    BrownTricorn(
        Items.BROWN_TRICORN_HAT_13191,
        intArrayOf(Items.BROWN_TRICORN_HAT_8962, Items.BROWN_NAVAL_SHIRT_8955, Items.BROWN_NAVY_SLACKS_8994),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    GreenTricorn(
        Items.GREEN_TRICORN_HAT_13189,
        intArrayOf(Items.GREEN_TRICORN_HAT_8960, Items.GREEN_NAVAL_SHIRT_8953, Items.GREEN_NAVY_SLACKS_8992),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    GreyTricorn(
        Items.GREY_TRICORN_HAT_13194,
        intArrayOf(Items.GREY_TRICORN_HAT_8965, Items.GREY_NAVAL_SHIRT_8958, Items.GREY_NAVY_SLACKS_8997),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    PurpleTricorn(
        Items.PURPLE_TRICORN_HAT_13193,
        intArrayOf(Items.PURPLE_TRICORN_HAT_8964, Items.PURPLE_NAVAL_SHIRT_8957, Items.PURPLE_NAVY_SLACKS_8996),
        type = StorableType.ARMOUR_CASE,
        2
    ),
    EliteBlackArmour(
        Items.ELITE_BLACK_PLATEBODY_14492,
        intArrayOf(
            Items.ELITE_BLACK_FULL_HELM_14494,
            Items.ELITE_BLACK_PLATEBODY_14492,
            Items.ELITE_BLACK_PLATELEGS_14490
        ),
        type = StorableType.ARMOUR_CASE,
        2
    );
    ;

    companion object {
        private val idToStorable: Map<Int, Storable> by lazy {
            values().flatMap { storable ->
                storable.takeIds.map { id -> id to storable }
            }.toMap()
        }

        private val allItemIds: Set<Int> by lazy { idToStorable.keys }

        private val storableIdSets: Map<Storable, Set<Int>> by lazy {
            values().associateWith { it.takeIds.toSet() }
        }

        fun getItems(type: StorableType, tier: Int? = null): List<Storable> =
            values().filter { it.type == type && (tier == null || it.tier == tier) }

        fun getTiers(type: StorableType): Set<Int> =
            values().filter { it.type == type }.map { it.tier }.toSet()

        fun fromId(id: Int): Storable? = idToStorable[id]

        fun hasStorable(player: Player, storable: Storable): Boolean {
            val ownedIds = (player.equipment.toArray() + player.bank.toArray() + player.inventory.toArray())
                .mapNotNull { it?.id }.toSet()
            return storableIdSets[storable]?.any { it in ownedIds } ?: false
        }

        fun hasAnyStorable(player: Player): Boolean {
            val ownedIds = (player.equipment.toArray() + player.bank.toArray() + player.inventory.toArray())
                .mapNotNull { it?.id }.toSet()
            return allItemIds.any { it in ownedIds }
        }

        fun getStorablesInBank(player: Player): List<Storable> {
            val bankIds = player.bank.toArray().mapNotNull { it?.id }.toSet()
            return values().filter { storableIdSets[it]?.any { id -> id in bankIds } ?: false }
        }

        fun hasStorableEquipped(player: Player, storable: Storable): Boolean {
            val equippedIds = player.equipment.toArray().mapNotNull { it?.id }.toSet()
            return storableIdSets[storable]?.any { it in equippedIds } ?: false
        }

        fun hasStorableInInventory(player: Player, storable: Storable): Boolean {
            val inventoryIds = player.inventory.toArray().mapNotNull { it?.id }.toSet()
            return storableIdSets[storable]?.any { it in inventoryIds } ?: false
        }
    }
}

