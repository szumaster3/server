package core.game.ge

import core.game.node.item.Item
import org.rs.consts.Items

enum class ItemSet(
    val itemId: Int,
    vararg val components: Int,
) {
    /*
     * Standard armour sets.
     */

    BRONZE_L(
        Items.BRONZE_ARMOUR_SET_L_11814,
        Items.BRONZE_FULL_HELM_1155,
        Items.BRONZE_PLATEBODY_1117,
        Items.BRONZE_PLATELEGS_1075,
        Items.BRONZE_KITESHIELD_1189,
    ),
    BRONZE_SK(
        Items.BRONZE_ARMOUR_SET_SK_11816,
        Items.BRONZE_FULL_HELM_1155,
        Items.BRONZE_PLATEBODY_1117,
        Items.BRONZE_PLATESKIRT_1087,
        Items.BRONZE_KITESHIELD_1189,
    ),
    IRON_L(
        Items.IRON_ARMOUR_SET_L_11818,
        Items.IRON_FULL_HELM_1153,
        Items.IRON_PLATEBODY_1115,
        Items.IRON_PLATELEGS_1067,
        Items.IRON_KITESHIELD_1191,
    ),
    IRON_SK(
        Items.IRON_ARMOUR_SET_SK_11820,
        Items.IRON_FULL_HELM_1153,
        Items.IRON_PLATEBODY_1115,
        Items.IRON_PLATESKIRT_1081,
        Items.IRON_KITESHIELD_1191,
    ),
    STEEL_L(
        Items.STEEL_ARMOUR_SET_L_11822,
        Items.STEEL_FULL_HELM_1157,
        Items.STEEL_PLATEBODY_1119,
        Items.STEEL_PLATELEGS_1069,
        Items.STEEL_KITESHIELD_1193,
    ),
    STEEL_SK(
        Items.STEEL_ARMOUR_SET_SK_11824,
        Items.STEEL_FULL_HELM_1157,
        Items.STEEL_PLATEBODY_1119,
        Items.STEEL_PLATESKIRT_1083,
        Items.STEEL_KITESHIELD_1193,
    ),
    BLACK_L(
        Items.BLACK_ARMOUR_SET_L_11826,
        Items.BLACK_FULL_HELM_1165,
        Items.BLACK_PLATEBODY_1125,
        Items.BLACK_PLATELEGS_1077,
        Items.BLACK_KITESHIELD_1195,
    ),
    BLACK_SK(
        Items.BLACK_ARMOUR_SET_SK_11828,
        Items.BLACK_FULL_HELM_1165,
        Items.BLACK_PLATEBODY_1125,
        Items.BLACK_PLATESKIRT_1089,
        Items.BLACK_KITESHIELD_1195,
    ),
    MITHRIL_L(
        Items.MITHRIL_ARMOUR_SET_L_11830,
        Items.MITHRIL_FULL_HELM_1159,
        Items.MITHRIL_PLATEBODY_1121,
        Items.MITHRIL_PLATELEGS_1071,
        Items.MITHRIL_KITESHIELD_1197,
    ),
    MITHRIL_SK(
        Items.MITHRIL_ARMOUR_SET_SK_11832,
        Items.MITHRIL_FULL_HELM_1159,
        Items.MITHRIL_PLATEBODY_1121,
        Items.MITHRIL_PLATESKIRT_1085,
        Items.MITHRIL_KITESHIELD_1197,
    ),
    ADAMANT_L(
        Items.ADAMANT_ARMOUR_SET_L_11834,
        Items.ADAMANT_FULL_HELM_1161,
        Items.ADAMANT_PLATEBODY_1123,
        Items.ADAMANT_PLATELEGS_1073,
        Items.ADAMANT_KITESHIELD_1199,
    ),
    ADAMANT_SK(
        Items.ADAMANT_ARMOUR_SET_SK_11836,
        Items.ADAMANT_FULL_HELM_1161,
        Items.ADAMANT_PLATEBODY_1123,
        Items.ADAMANT_PLATESKIRT_1091,
        Items.ADAMANT_KITESHIELD_1199,
    ),
    RUNE_L(
        Items.RUNE_ARMOUR_SET_L_11838,
        Items.RUNE_FULL_HELM_1163,
        Items.RUNE_PLATEBODY_1127,
        Items.RUNE_PLATELEGS_1079,
        Items.RUNE_KITESHIELD_1201,
    ),
    RUNE_SK(
        Items.RUNE_ARMOUR_SET_SK_11840,
        Items.RUNE_FULL_HELM_1163,
        Items.RUNE_PLATEBODY_1127,
        Items.RUNE_PLATESKIRT_1093,
        Items.RUNE_KITESHIELD_1201,
    ),
    DRAGON_L(
        Items.DRAGON_CHAIN_ARMOUR_SET_L_11842,
        Items.DRAGON_MED_HELM_1149,
        Items.DRAGON_CHAINBODY_3140,
        Items.DRAGON_PLATELEGS_4087,
    ),
    DRAGON_SK(
        Items.DRAGON_CHAIN_ARMOUR_SET_SK_11844,
        Items.DRAGON_MED_HELM_1149,
        Items.DRAGON_CHAINBODY_3140,
        Items.DRAGON_PLATESKIRT_4585,
    ),
    DRAGON_PLATE_L(
        Items.DRAGON_PLATE_ARMOUR_SET_L_14529,
        Items.DRAGON_FULL_HELM_11335,
        Items.DRAGON_PLATEBODY_14479,
        Items.DRAGON_PLATELEGS_4087,
    ),
    DRAGON_PLATE_SK(
        Items.DRAGON_PLATE_ARMOUR_SET_SK_14531,
        Items.DRAGON_FULL_HELM_11335,
        Items.DRAGON_PLATEBODY_14479,
        Items.DRAGON_PLATESKIRT_4585,
    ),

    PLACEHOLDER_2(-1, -1, -1, -1),
    PLACEHOLDER_3(-1, -1, -1, -1),

    /*
     * Barrows armour sets.
     */

    AHRIMS(
        Items.AHRIMS_SET_11846,
        Items.AHRIMS_HOOD_4708,
        Items.AHRIMS_ROBETOP_4712,
        Items.AHRIMS_ROBESKIRT_4714,
        Items.AHRIMS_STAFF_4710,
    ),
    DHAROKS(
        Items.DHAROKS_SET_11848,
        Items.DHAROKS_HELM_4716,
        Items.DHAROKS_PLATEBODY_4720,
        Items.DHAROKS_PLATELEGS_4722,
        Items.DHAROKS_GREATAXE_4718,
    ),
    GUTHANS(
        Items.GUTHANS_SET_11850,
        Items.GUTHANS_HELM_4724,
        Items.GUTHANS_PLATEBODY_4728,
        Items.GUTHANS_CHAINSKIRT_4730,
        Items.GUTHANS_WARSPEAR_4726,
    ),
    KARILS(
        Items.KARILS_SET_11852,
        Items.KARILS_COIF_4732,
        Items.KARILS_LEATHERTOP_4736,
        Items.KARILS_LEATHERSKIRT_4738,
        Items.KARILS_CROSSBOW_4734,
    ),
    TORAGS(
        Items.TORAGS_SET_11854,
        Items.TORAGS_HELM_4745,
        Items.TORAGS_PLATEBODY_4749,
        Items.TORAGS_PLATELEGS_4751,
        Items.TORAGS_HAMMERS_4747,
    ),
    VERACS(
        Items.VERACS_SET_11856,
        Items.VERACS_HELM_4753,
        Items.VERACS_BRASSARD_4757,
        Items.VERACS_PLATESKIRT_4759,
        Items.VERACS_FLAIL_4755,
    ),

    PLACEHOLDER_4(-1, -1, -1, -1),
    PLACEHOLDER_5(-1, -1, -1, -1),
    PLACEHOLDER_6(-1, -1, -1, -1),
    PLACEHOLDER_7(-1, -1, -1, -1),

    /*
     * Third-Age armour sets.
     */

    THIRD_AGE_MELEE(
        Items.THIRD_AGE_MELEE_SET_11858,
        Items.THIRD_AGE_FULL_HELMET_10350,
        Items.THIRD_AGE_PLATEBODY_10348,
        Items.THIRD_AGE_PLATELEGS_10346,
        Items.THIRD_AGE_KITESHIELD_10352,
    ),
    THIRD_AGE_RANGE(
        Items.THIRD_AGE_RANGER_SET_11860,
        Items.THIRD_AGE_RANGE_COIF_10334,
        Items.THIRD_AGE_RANGE_TOP_10330,
        Items.THIRD_AGE_RANGE_LEGS_10332,
        Items.THIRD_AGE_VAMBRACES_10336,
    ),
    THIRD_AGE_MAGE(
        Items.THIRD_AGE_MAGE_SET_11862,
        Items.THIRD_AGE_MAGE_HAT_10342,
        Items.THIRD_AGE_ROBE_TOP_10338,
        Items.THIRD_AGE_ROBE_10340,
        Items.THIRD_AGE_AMULET_10344,
    ),

    PLACEHOLDER_8(-1, -1, -1, -1),
    PLACEHOLDER_9(-1, -1, -1, -1),
    PLACEHOLDER_10(-1, -1, -1, -1),
    PLACEHOLDER_11(-1, -1, -1, -1),
    PLACEHOLDER_12(-1, -1, -1, -1),
    PLACEHOLDER_13(-1, -1, -1, -1),
    PLACEHOLDER_14(-1, -1, -1, -1),

    /*
     * Dragonhide armour sets.
     */

    GREEN_DHIDE(
        Items.GREEN_DRAGONHIDE_SET_11864,
        Items.GREEN_DHIDE_BODY_1135,
        Items.GREEN_DHIDE_CHAPS_1099,
        Items.GREEN_DHIDE_VAMB_1065,
    ),
    BLUE_DHIDE(
        Items.BLUE_DRAGONHIDE_SET_11866,
        Items.BLUE_DHIDE_BODY_2499,
        Items.BLUE_DHIDE_CHAPS_2493,
        Items.BLUE_DHIDE_VAMB_2487,
    ),
    RED_DHIDE(
        Items.RED_DRAGONHIDE_SET_11868,
        Items.RED_DHIDE_BODY_2501,
        Items.RED_DHIDE_CHAPS_2495,
        Items.RED_DHIDE_VAMB_2489,
    ),
    BLACK_DHIDE(
        Items.BLACK_DRAGONHIDE_SET_11870,
        Items.BLACK_DHIDE_BODY_2503,
        Items.BLACK_DHIDE_CHAPS_2497,
        Items.BLACK_DHIDE_VAMB_2491,
    ),

    PLACEHOLDER_15(-1, -1, -1, -1),
    PLACEHOLDER_16(-1, -1, -1, -1),
    PLACEHOLDER_17(-1, -1, -1, -1),
    PLACEHOLDER_18(-1, -1, -1, -1),
    PLACEHOLDER_19(-1, -1, -1, -1),
    PLACEHOLDER_20(-1, -1, -1, -1),

    /*
     * Magical armour sets.
     */

    MYSTIC(
        Items.MYSTIC_ROBES_SET_11872,
        Items.MYSTIC_HAT_4089,
        Items.MYSTIC_ROBE_TOP_4091,
        Items.MYSTIC_ROBE_BOTTOM_4093,
        Items.MYSTIC_GLOVES_4095,
        Items.MYSTIC_BOOTS_4097,
    ),
    LIGHT_MYSTIC(
        Items.LIGHT_MYSTIC_ROBES_SET_11960,
        Items.MYSTIC_HAT_4109,
        Items.MYSTIC_ROBE_TOP_4111,
        Items.MYSTIC_ROBE_BOTTOM_4113,
        Items.MYSTIC_GLOVES_4115,
        Items.MYSTIC_BOOTS_4117,
    ),
    DARK_MYSTIC(
        Items.DARK_MYSTIC_ROBES_SET_11962,
        Items.MYSTIC_HAT_4099,
        Items.MYSTIC_ROBE_TOP_4101,
        Items.MYSTIC_ROBE_BOTTOM_4103,
        Items.MYSTIC_GLOVES_4105,
        Items.MYSTIC_BOOTS_4107,
    ),
    INFINITY(
        Items.INFINITY_ROBES_SET_11874,
        Items.INFINITY_HAT_6918,
        Items.INFINITY_TOP_6916,
        Items.INFINITY_BOTTOMS_6924,
        Items.INFINITY_GLOVES_6922,
        Items.INFINITY_BOOTS_6920,
    ),
    SPLITBARK(
        Items.SPLITBARK_ARMOUR_SET_11876,
        Items.SPLITBARK_HELM_3385,
        Items.SPLITBARK_BODY_3387,
        Items.SPLITBARK_LEGS_3389,
        Items.SPLITBARK_GAUNTLETS_3391,
        Items.SPLITBARK_BOOTS_3393,
    ),

    PLACEHOLDER_21(-1, -1, -1, -1),
    PLACEHOLDER_22(-1, -1, -1, -1),
    PLACEHOLDER_23(-1, -1, -1, -1),
    PLACEHOLDER_24(-1, -1, -1, -1),
    PLACEHOLDER_25(-1, -1, -1, -1),

    /*
     * Trimmed armour sets.
     */

    BLACK_TRIMMED_L(
        Items.BLACK_TRIMMED_ARMOUR_SET_L_11878,
        Items.BLACK_FULL_HELMT_2587,
        Items.BLACK_PLATEBODY_T_2583,
        Items.BLACK_PLATELEGS_T_2585,
        Items.BLACK_KITESHIELD_T_2589,
    ),
    BLACK_TRIMMED_SK(
        Items.BLACK_TRIMMED_ARMOUR_SET_SK_11880,
        Items.BLACK_FULL_HELMT_2587,
        Items.BLACK_PLATEBODY_T_2583,
        Items.BLACK_PLATESKIRT_T_3472,
        Items.BLACK_KITESHIELD_T_2589,
    ),
    BLACK_GOLD_TRIMMED_L(
        Items.BLACK_GOLD_TRIMMED_ARMOUR_SET_L_11882,
        Items.BLACK_FULL_HELMG_2595,
        Items.BLACK_PLATEBODY_G_2591,
        Items.BLACK_PLATELEGS_G_2593,
        Items.BLACK_KITESHIELD_G_2597,
    ),
    BLACK_GOLD_TRIMMED_SK(
        Items.BLACK_GOLD_TRIMMED_ARMOUR_SET_SK_11884,
        Items.BLACK_FULL_HELMG_2595,
        Items.BLACK_PLATEBODY_G_2591,
        Items.BLACK_PLATESKIRT_G_3473,
        Items.BLACK_KITESHIELD_G_2597,
    ),
    ADAMANT_TRIMMED_L(
        Items.ADAMANT_TRIMMED_ARMOUR_SET_L_11886,
        Items.ADAM_FULL_HELMT_2605,
        Items.ADAM_PLATEBODY_T_2599,
        Items.ADAM_PLATELEGS_T_2601,
        Items.ADAM_KITESHIELD_T_2603,
    ),
    ADAMANT_TRIMMED_SK(
        Items.ADAMANT_TRIMMED_ARMOUR_SET_SK_11888,
        Items.ADAM_FULL_HELMT_2605,
        Items.ADAM_PLATEBODY_T_2599,
        Items.ADAM_PLATESKIRT_T_3474,
    ),
    ADAMANT_GOLD_TRIMMED_L(
        Items.ADAMANT_GOLD_TRIMMED_ARMOUR_SET_L_11890,
        Items.ADAM_FULL_HELMG_2613,
        Items.ADAM_PLATEBODY_G_2607,
        Items.ADAM_PLATELEGS_G_2609,
        Items.ADAM_KITESHIELD_G_2611,
    ),
    ADAMANT_GOLD_TRIMMED_SK(
        Items.ADAMANT_GOLD_TRIMMED_ARMOUR_SET_SK_11892,
        Items.ADAM_FULL_HELMG_2613,
        Items.ADAM_PLATESKIRT_G_3475,
        Items.ADAM_KITESHIELD_G_2611,
    ),
    RUNE_TRIMMED_L(
        Items.RUNE_TRIMMED_ARMOUR_SET_L_11894,
        Items.RUNE_FULL_HELM_T_2627,
        Items.RUNE_PLATEBODY_T_2623,
        Items.RUNE_PLATELEGS_T_2625,
        Items.RUNE_KITESHIELD_T_2629,
    ),
    RUNE_TRIMMED_SK(
        Items.RUNE_TRIMMED_ARMOUR_SET_SK_11896,
        Items.RUNE_FULL_HELM_T_2627,
        Items.RUNE_PLATEBODY_T_2623,
        Items.RUNE_PLATESKIRT_T_3477,
        Items.RUNE_KITESHIELD_T_2629,
    ),

    /*
     * Gold trimmed armour.
     */

    RUNE_GOLD_TRIMMED_L(
        Items.RUNE_GOLD_TRIMMED_ARMOUR_SET_L_11898,
        Items.RUNE_FULL_HELMG_2619,
        Items.RUNE_PLATEBODY_G_2615,
        Items.RUNE_PLATELEGS_G_2617,
        Items.RUNE_KITESHIELD_G_2621,
    ),
    RUNE_GOLD_TRIMMED_SK(
        Items.RUNE_GOLD_TRIMMED_ARMOUR_SET_SK_11900,
        Items.RUNE_FULL_HELMG_2619,
        Items.RUNE_PLATEBODY_G_2615,
        Items.RUNE_PLATESKIRT_G_3476,
        Items.RUNE_KITESHIELD_G_2621,
    ),

    PLACEHOLDER_26(-1, -1, -1, -1),
    PLACEHOLDER_27(-1, -1, -1, -1),
    PLACEHOLDER_28(-1, -1, -1, -1),
    PLACEHOLDER_29(-1, -1, -1, -1),
    PLACEHOLDER_30(-1, -1, -1, -1),
    PLACEHOLDER_31(-1, -1, -1, -1),
    PLACEHOLDER_32(-1, -1, -1, -1),
    PLACEHOLDER_33(-1, -1, -1, -1),

    /*
     * Magical armour sets.
     */

    ENCHANTED(
        Items.ENCHANTED_SET_11902,
        Items.ENCHANTED_HAT_7400,
        Items.ENCHANTED_TOP_7399,
        Items.ENCHANTED_ROBE_7398,
    ),
    TRIMMED_BLUE_WIZARD(
        Items.TRIMMED_BLUE_WIZARD_SET_11904,
        Items.WIZARD_HAT_T_7396,
        Items.WIZARD_ROBE_T_7392,
        Items.BLUE_SKIRT_T_7388,
    ),
    GOLD_TRIMMED_BLUE_WIZARD(
        Items.GOLD_TRIMMED_BLUE_WIZARD_SET_11906,
        Items.WIZARD_HAT_G_7394,
        Items.WIZARD_ROBE_G_7390,
        Items.BLUE_SKIRT_G_7386,
    ),

    PLACEHOLDER_34(-1, -1, -1, -1),
    PLACEHOLDER_35(-1, -1, -1, -1),
    PLACEHOLDER_36(-1, -1, -1, -1),
    PLACEHOLDER_37(-1, -1, -1, -1),
    PLACEHOLDER_38(-1, -1, -1, -1),
    PLACEHOLDER_39(-1, -1, -1, -1),
    PLACEHOLDER_40(-1, -1, -1, -1),

    /*
     * Blessed ranged armour sets.
     */

    TRIMMED_LEATHER(
        Items.TRIMMED_LEATHER_ARMOUR_SET_11908,
        Items.STUDDED_BODY_T_7364,
        Items.STUDDED_CHAPS_T_7368,
    ),
    GOLD_TRIMMED_LEATHER(
        Items.GOLD_TRIMMED_LEATHER_ARMOUR_SET_11910,
        Items.STUDDED_BODY_G_7362,
        Items.STUDDED_CHAPS_G_7366,
    ),
    GREEN_DHIDE_T(
        Items.GREEN_DHIDE_TRIMMED_SET_11912,
        Items.DHIDE_BODY_T_7372,
        Items.DHIDE_CHAPS_T_7380,
    ),
    GREEN_DHIDE_G(
        Items.GREEN_DHIDE_GOLD_TRIMMED_SET_11914,
        Items.DHIDE_BODYG_7370,
        Items.DHIDE_CHAPS_G_7378,
    ),
    BLUE_DHIDE_T(
        Items.BLUE_DHIDE_TRIMMED_SET_11916,
        Items.DHIDE_BODY_T_7376,
        Items.DHIDE_CHAPS_T_7384,
    ),
    BLUE_DHIDE_G(
        Items.BLUE_DHIDE_GOLD_TRIMMED_SET_11918,
        Items.DHIDE_BODY_G_7374,
        Items.DHIDE_CHAPS_G_7382,
    ),
    GREEN_DHIDE_BLESSED(
        Items.GREEN_DHIDE_BLESSED_SET_11920,
        Items.GUTHIX_COIF_10382,
        Items.GUTHIX_DRAGONHIDE_10378,
        Items.GUTHIX_CHAPS_10380,
        Items.GUTHIX_BRACERS_10376,
    ),
    BLUE_DHIDE_BLESSED(
        Items.BLUE_DHIDE_BLESSED_SET_11922,
        Items.SARADOMIN_COIF_10390,
        Items.SARADOMIN_DHIDE_10386,
        Items.SARADOMIN_CHAPS_10388,
        Items.SARADOMIN_BRACERS_10384,
    ),
    RED_DHIDE_BLESSED(
        Items.RED_DHIDE_BLESSED_SET_11924,
        Items.ZAMORAK_COIF_10374,
        Items.ZAMORAK_DHIDE_10370,
        Items.ZAMORAK_CHAPS_10372,
        Items.ZAMORAK_BRACERS_10368,
    ),

    PLACEHOLDER_41(-1, -1, -1, -1),

    /*
     * God armour sets.
     */

    GUTHIX_L(
        Items.GUTHIX_ARMOUR_SET_L_11926,
        Items.GUTHIX_FULL_HELM_2673,
        Items.GUTHIX_PLATEBODY_2669,
        Items.GUTHIX_PLATELEGS_2671,
        Items.GUTHIX_KITESHIELD_2675,
    ),
    SARADOMIN_L(
        Items.SARADOMIN_ARMOUR_SET_L_11928,
        Items.SARADOMIN_FULL_HELM_2665,
        Items.SARADOMIN_PLATEBODY_2661,
        Items.SARADOMIN_PLATELEGS_2663,
        Items.SARADOMIN_KITESHIELD_2667,
    ),
    ZAMORAK_L(
        Items.ZAMORAK_ARMOUR_SET_L_11930,
        Items.ZAMORAK_FULL_HELM_2657,
        Items.ZAMORAK_PLATEBODY_2653,
        Items.ZAMORAK_PLATELEGS_2655,
        Items.ZAMORAK_KITESHIELD_2659,
    ),
    GUTHIX_SK(
        Items.GUTHIX_ARMOUR_SET_SK_11932,
        Items.GUTHIX_FULL_HELM_2673,
        Items.GUTHIX_PLATEBODY_2669,
        Items.GUTHIX_PLATESKIRT_3480,
        Items.GUTHIX_KITESHIELD_2675,
    ),
    SARADOMIN_SK(
        Items.SARADOMIN_ARMOUR_SET_SK_11934,
        Items.SARADOMIN_FULL_HELM_2665,
        Items.SARADOMIN_PLATEBODY_2661,
        Items.SARADOMIN_PLATESKIRT_3479,
        Items.SARADOMIN_KITESHIELD_2667,
    ),
    ZAMORAK_SK(
        Items.ZAMORAK_ARMOUR_SET_SK_11936,
        Items.ZAMORAK_FULL_HELM_2657,
        Items.ZAMORAK_PLATEBODY_2653,
        Items.ZAMORAK_PLATESKIRT_3478,
        Items.ZAMORAK_KITESHIELD_2659,
    ),
    GILDED_L(
        Items.GILDED_ARMOUR_SET_L_11938,
        Items.GILDED_FULL_HELM_3486,
        Items.GILDED_PLATEBODY_3481,
        Items.GILDED_PLATELEGS_3483,
        Items.GILDED_KITESHIELD_3488,
    ),
    GILDED_SK(
        Items.GILDED_ARMOUR_SET_SK_11940,
        Items.GILDED_FULL_HELM_3486,
        Items.GILDED_PLATEBODY_3481,
        Items.GILDED_PLATESKIRT_3485,
        Items.GILDED_KITESHIELD_3488,
    ),

    PLACEHOLDER_42(-1, -1, -1, -1),
    PLACEHOLDER_43(-1, -1, -1, -1),

    /*
     * Fremennik armour sets.
     */

    ROCKSHELL(
        Items.ROCKSHELL_ARMOUR_SET_11942,
        Items.ROCK_SHELL_HELM_6128,
        Items.ROCK_SHELL_PLATE_6129,
        Items.ROCK_SHELL_LEGS_6130,
        Items.ROCK_SHELL_GLOVES_6151,
        Items.ENCHANTED_LYRE6_14591,
    ),
    SPINED(
        Items.SPINED_ARMOUR_SET_11944,
        Items.SPINED_HELM_6131,
        Items.SPINED_BODY_6133,
        Items.SPINED_CHAPS_6135,
        Items.SPINED_GLOVES_6149,
        Items.SPINED_BOOTS_6143,
    ),
    SKELETAL(
        Items.SKELETAL_ARMOUR_SET_11946,
        Items.SKELETAL_HELM_6137,
        Items.SKELETAL_TOP_6139,
        Items.SKELETAL_BOTTOMS_6141,
        Items.SKELETAL_GLOVES_6153,
        Items.SKELETAL_BOOTS_6147,
    ),

    PLACEHOLDER_44(-1, -1, -1, -1),
    PLACEHOLDER_45(-1, -1, -1, -1),
    PLACEHOLDER_46(-1, -1, -1, -1),
    PLACEHOLDER_47(-1, -1, -1, -1),
    PLACEHOLDER_48(-1, -1, -1, -1),
    PLACEHOLDER_49(-1, -1, -1, -1),
    PLACEHOLDER_50(-1, -1, -1, -1),

    /*
     * Dwarf cannon set.
     */

    CANNON(
        Items.DWARF_CANNON_SET_11967,
        Items.CANNON_BASE_6,
        Items.CANNON_STAND_8,
        Items.CANNON_BARRELS_10,
        Items.CANNON_FURNACE_12,
    ),

    PLACEHOLDER_51(-1, -1, -1, -1),
    PLACEHOLDER_52(-1, -1, -1, -1),
    PLACEHOLDER_53(-1, -1, -1, -1),
    PLACEHOLDER_54(-1, -1, -1, -1),
    PLACEHOLDER_55(-1, -1, -1, -1),
    PLACEHOLDER_56(-1, -1, -1, -1),
    PLACEHOLDER_57(-1, -1, -1, -1),
    PLACEHOLDER_58(-1, -1, -1, -1),
    PLACEHOLDER_59(-1, -1, -1, -1),

    /*
     * Elite armour set.
     */

    DRAGON_HAI_ROBES_SET(
        Items.DAGONHAI_ROBES_SET_14525,
        Items.DAGONHAI_HAT_14499,
        Items.DAGONHAI_ROBE_TOP_14497,
        Items.DAGONHAI_ROBE_BOTTOM_14501,
    ),
    ELITE_BLACK_ARMOR_SET(
        Items.ELITE_BLACK_ARMOUR_SET_14527,
        Items.ELITE_BLACK_FULL_HELM_14494,
        Items.ELITE_BLACK_PLATEBODY_14492,
        Items.ELITE_BLACK_PLATELEGS_14490,
    ),
    ;

    companion object {
        private val ITEM_SETS = mutableMapOf<Int, ItemSet>()
        private val itemArray = mutableListOf<Item?>()

        init {
            values().forEach { set ->
                val item = if (set.itemId == -1) Item() else Item(set.itemId)
                itemArray.add(item)
                ITEM_SETS[set.itemId] = set
            }
        }

        @JvmStatic
        fun forId(setId: Int): ItemSet? = ITEM_SETS[setId]

        @JvmStatic
        fun getItemArray(): Array<Item?> = itemArray.toTypedArray()
    }
}
