package content.global.skill.gather.mining

import core.ServerConstants
import core.game.node.item.WeightedChanceItem
import core.game.world.repository.Repository.players
import shared.consts.Items
import shared.consts.Scenery

/**
 * The mining nodes.
 */
enum class MiningNode(
    /**
     * The Full.
     */
    var id: Int,
    /**
     * Empty mining node.
     */
    var emptyId: Int,
    /**
     * The Identifier.
     */
    var identifier: Byte
) {
    /**
     * Copper ore 0 mining node.
     */
    COPPER_ORE_0(Scenery.ROCKS_2090, Scenery.ROCKS_450, 1.toByte()),

    /**
     * Copper ore 1 mining node.
     */
    COPPER_ORE_1(Scenery.ROCKS_2091, Scenery.ROCKS_452, 1.toByte()),

    /**
     * Copper ore 2 mining node.
     */
    COPPER_ORE_2(Scenery.MINERAL_VEIN_4976, Scenery.MINERAL_VEIN_4994, 1.toByte()),

    /**
     * Copper ore 3 mining node.
     */
    COPPER_ORE_3(Scenery.MINERAL_VEIN_4977, Scenery.MINERAL_VEIN_4995, 1.toByte()),

    /**
     * Copper ore 4 mining node.
     */
    COPPER_ORE_4(Scenery.MINERAL_VEIN_4978, Scenery.MINERAL_VEIN_4996, 1.toByte()),

    /**
     * Copper ore 5 mining node.
     */
    COPPER_ORE_5(Scenery.ROCKS_9710, Scenery.ROCKS_18954, 1.toByte()),

    /**
     * Copper ore 6 mining node.
     */
    COPPER_ORE_6(Scenery.ROCKS_9709, Scenery.ROCKS_32448, 1.toByte()),

    /**
     * Copper ore 7 mining node.
     */
    COPPER_ORE_7(Scenery.ROCKS_9708, Scenery.ROCKS_32447, 1.toByte()),

    /**
     * Copper ore 8 mining node.
     */
    COPPER_ORE_8(Scenery.ROCKS_11960, Scenery.ROCKS_11555, 1.toByte()),

    /**
     * Copper ore 9 mining node.
     */
    COPPER_ORE_9(Scenery.ROCKS_11961, Scenery.ROCKS_11556, 1.toByte()),

    /**
     * Copper ore 10 mining node.
     */
    COPPER_ORE_10(Scenery.ROCKS_11962, Scenery.ROCKS_11557, 1.toByte()),

    /**
     * Copper ore 11 mining node.
     */
    COPPER_ORE_11(Scenery.ROCKS_11937, Scenery.ROCKS_11553, 1.toByte()),

    /**
     * Copper ore 12 mining node.
     */
    COPPER_ORE_12(Scenery.ROCKS_11936, Scenery.ROCKS_11552, 1.toByte()),

    /**
     * Copper ore 13 mining node.
     */
    COPPER_ORE_13(Scenery.ROCKS_11938, Scenery.ROCKS_11554, 1.toByte()),

    /**
     * Copper ore 14 mining node.
     */
    COPPER_ORE_14(Scenery.RUBBLE_12746, Scenery.ROCKS_450, 1.toByte()),

    /**
     * Copper ore 15 mining node.
     */
    COPPER_ORE_15(Scenery.ROCKS_14906, Scenery.ROCKS_14894, 1.toByte()),

    /**
     * Copper ore 16 mining node.
     */
    COPPER_ORE_16(Scenery.ROCKS_14907, Scenery.ROCKS_14895, 1.toByte()),

    /**
     * Copper ore 17 mining node.
     */
    COPPER_ORE_17(Scenery.ORE_VEIN_20448, Scenery.ORE_VEIN_20445, 1.toByte()),

    /**
     * Copper ore 18 mining node.
     */
    COPPER_ORE_18(Scenery.ORE_VEIN_20451, Scenery.ORE_VEIN_20445, 1.toByte()),

    /**
     * Copper ore 19 mining node.
     */
    COPPER_ORE_19(Scenery.ORE_VEIN_20446, Scenery.ORE_VEIN_20443, 1.toByte()),

    /**
     * Copper ore 20 mining node.
     */
    COPPER_ORE_20(Scenery.ORE_VEIN_20447, Scenery.ORE_VEIN_20444, 1.toByte()),

    /**
     * Copper ore 21 mining node.
     */
    COPPER_ORE_21(Scenery.ORE_VEIN_20408, Scenery.ORE_VEIN_20407, 1.toByte()),

    /**
     * Copper ore 22 mining node.
     */
    COPPER_ORE_22(Scenery.ROCKS_18993, Scenery.ROCKS_19005, 1.toByte()),

    /**
     * Copper ore 23 mining node.
     */
    COPPER_ORE_23(Scenery.ROCKS_18992, Scenery.ROCKS_19004, 1.toByte()),

    /**
     * Copper ore 24 mining node.
     */
    COPPER_ORE_24(Scenery.ROCKS_19007, Scenery.ROCKS_19016, 1.toByte()),

    /**
     * Copper ore 25 mining node.
     */
    COPPER_ORE_25(Scenery.ROCKS_19006, Scenery.ROCKS_19021, 1.toByte()),

    /**
     * Copper ore 26 mining node.
     */
    COPPER_ORE_26(Scenery.ROCKS_18991, Scenery.ROCKS_19003, 1.toByte()),

    /**
     * Copper ore 27 mining node.
     */
    COPPER_ORE_27(Scenery.ROCKS_19008, Scenery.ROCKS_19017, 1.toByte()),

    /**
     * Copper ore 28 mining node.
     */
    COPPER_ORE_28(Scenery.ROCKS_21285, Scenery.ROCKS_21297, 1.toByte()),

    /**
     * Copper ore 29 mining node.
     */
    COPPER_ORE_29(Scenery.ROCKS_21284, Scenery.ROCKS_21296, 1.toByte()),

    /**
     * Copper ore 30 mining node.
     */
    COPPER_ORE_30(Scenery.ROCKS_21286, Scenery.ROCKS_21298, 1.toByte()),

    /**
     * Copper ore 31 mining node.
     */
    COPPER_ORE_31(Scenery.ROCKS_29231, Scenery.ROCKS_29219, 1.toByte()),

    /**
     * Copper ore 32 mining node.
     */
    COPPER_ORE_32(Scenery.ROCKS_29230, Scenery.ROCKS_29218, 1.toByte()),

    /**
     * Copper ore 33 mining node.
     */
    COPPER_ORE_33(Scenery.ROCKS_29232, Scenery.ROCKS_29220, 1.toByte()),

    /**
     * Copper ore 34 mining node.
     */
    COPPER_ORE_34(Scenery.ROCKS_31082, Scenery.ROCKS_37650, 1.toByte()),

    /**
     * Copper ore 35 mining node.
     */
    COPPER_ORE_35(Scenery.ROCKS_31081, Scenery.ROCKS_37649, 1.toByte()),

    /**
     * Copper ore 36 mining node.
     */
    COPPER_ORE_36(Scenery.ROCKS_31080, Scenery.ROCKS_37639, 1.toByte()),

    /**
     * Copper ore 37 mining node.
     */
    COPPER_ORE_37(Scenery.ROCKS_37647, Scenery.ROCKS_37650, 1.toByte()),

    /**
     * Copper ore 38 mining node.
     */
    COPPER_ORE_38(Scenery.ROCKS_37646, Scenery.ROCKS_37649, 1.toByte()),

    /**
     * Copper ore 39 mining node.
     */
    COPPER_ORE_39(Scenery.ROCKS_37645, Scenery.ROCKS_37639, 1.toByte()),

    /**
     * Copper ore 40 mining node.
     */
    COPPER_ORE_40(Scenery.ROCKS_37637, Scenery.ROCKS_37639, 1.toByte()),

    /**
     * Copper ore 41 mining node.
     */
    COPPER_ORE_41(Scenery.ROCKS_37688, Scenery.ROCKS_21298, 1.toByte()),

    /**
     * Copper ore 42 mining node.
     */
    COPPER_ORE_42(Scenery.ROCKS_37686, Scenery.ROCKS_21296, 1.toByte()),

    /**
     * Copper ore 43 mining node.
     */
    COPPER_ORE_43(Scenery.ROCKS_37687, Scenery.ROCKS_21297, 1.toByte()),

    /**
     * Copper ore 44 mining node.
     */
    COPPER_ORE_44(Scenery.ROCKS_3042, Scenery.ROCKS_11552, 1.toByte()),

    /**
     * Tin ore 0 mining node.
     */
    TIN_ORE_0(Scenery.ROCKS_2094, Scenery.ROCKS_450, 2.toByte()),

    /**
     * Tin ore 1 mining node.
     */
    TIN_ORE_1(Scenery.ROCKS_2095, Scenery.ROCKS_452, 2.toByte()),

    /**
     * Tin ore 2 mining node.
     */
    TIN_ORE_2(Scenery.ROCKS_3043, Scenery.ROCKS_11552, 2.toByte()),

    /**
     * Tin ore 3 mining node.
     */
    TIN_ORE_3(Scenery.MINERAL_VEIN_4979, Scenery.MINERAL_VEIN_4994, 2.toByte()),

    /**
     * Tin ore 4 mining node.
     */
    TIN_ORE_4(Scenery.MINERAL_VEIN_4980, Scenery.MINERAL_VEIN_4995, 2.toByte()),

    /**
     * Tin ore 5 mining node.
     */
    TIN_ORE_5(Scenery.MINERAL_VEIN_4981, Scenery.MINERAL_VEIN_4996, 2.toByte()),

    /**
     * Tin ore 6 mining node.
     */
    TIN_ORE_6(Scenery.ROCKS_11957, Scenery.ROCKS_11555, 2.toByte()),

    /**
     * Tin ore 7 mining node.
     */
    TIN_ORE_7(Scenery.ROCKS_11958, Scenery.ROCKS_11556, 2.toByte()),

    /**
     * Tin ore 8 mining node.
     */
    TIN_ORE_8(Scenery.ROCKS_11959, Scenery.ROCKS_11557, 2.toByte()),

    /**
     * Tin ore 9 mining node.
     */
    TIN_ORE_9(Scenery.ROCKS_11934, Scenery.ROCKS_11553, 2.toByte()),

    /**
     * Tin ore 10 mining node.
     */
    TIN_ORE_10(Scenery.ROCKS_11935, Scenery.ROCKS_11554, 2.toByte()),

    /**
     * Tin ore 11 mining node.
     */
    TIN_ORE_11(Scenery.ROCKS_11933, Scenery.ROCKS_11552, 2.toByte()),

    /**
     * Tin ore 12 mining node.
     */
    TIN_ORE_12(Scenery.ROCKS_14902, Scenery.ROCKS_14894, 2.toByte()),

    /**
     * Tin ore 13 mining node.
     */
    TIN_ORE_13(Scenery.ROCKS_14903, Scenery.ROCKS_14895, 2.toByte()),

    /**
     * Tin ore 14 mining node.
     */
    TIN_ORE_14(Scenery.ROCKS_18995, Scenery.ROCKS_19004, 2.toByte()),

    /**
     * Tin ore 15 mining node.
     */
    TIN_ORE_15(Scenery.ROCKS_18994, Scenery.ROCKS_19003, 2.toByte()),

    /**
     * Tin ore 16 mining node.
     */
    TIN_ORE_16(Scenery.ROCKS_18996, Scenery.ROCKS_19005, 2.toByte()),

    /**
     * Tin ore 17 mining node.
     */
    TIN_ORE_17(Scenery.ROCKS_19025, Scenery.ROCKS_19016, 2.toByte()),

    /**
     * Tin ore 18 mining node.
     */
    TIN_ORE_18(Scenery.ROCKS_19024, Scenery.ROCKS_19021, 2.toByte()),

    /**
     * Tin ore 19 mining node.
     */
    TIN_ORE_19(Scenery.ROCKS_19026, Scenery.ROCKS_19017, 2.toByte()),

    /**
     * Tin ore 20 mining node.
     */
    TIN_ORE_20(Scenery.ROCKS_21293, Scenery.ROCKS_21296, 2.toByte()),

    /**
     * Tin ore 21 mining node.
     */
    TIN_ORE_21(Scenery.ROCKS_21295, Scenery.ROCKS_21298, 2.toByte()),

    /**
     * Tin ore 22 mining node.
     */
    TIN_ORE_22(Scenery.ROCKS_21294, Scenery.ROCKS_21297, 2.toByte()),

    /**
     * Tin ore 23 mining node.
     */
    TIN_ORE_23(Scenery.ROCKS_29227, Scenery.ROCKS_29218, 2.toByte()),

    /**
     * Tin ore 24 mining node.
     */
    TIN_ORE_24(Scenery.ROCKS_29229, Scenery.ROCKS_29220, 2.toByte()),

    /**
     * Tin ore 25 mining node.
     */
    TIN_ORE_25(Scenery.ROCKS_29228, Scenery.ROCKS_29219, 2.toByte()),

    /**
     * Tin ore 26 mining node.
     */
    TIN_ORE_26(Scenery.ROCKS_31079, Scenery.ROCKS_37650, 2.toByte()),

    /**
     * Tin ore 27 mining node.
     */
    TIN_ORE_27(Scenery.ROCKS_31078, Scenery.ROCKS_37649, 2.toByte()),

    /**
     * Tin ore 28 mining node.
     */
    TIN_ORE_28(Scenery.ROCKS_31077, Scenery.ROCKS_37639, 2.toByte()),

    /**
     * Tin ore 29 mining node.
     */
    TIN_ORE_29(Scenery.ROCKS_37644, Scenery.ROCKS_37650, 2.toByte()),

    /**
     * Tin ore 30 mining node.
     */
    TIN_ORE_30(Scenery.ROCKS_37643, Scenery.ROCKS_37649, 2.toByte()),

    /**
     * Tin ore 31 mining node.
     */
    TIN_ORE_31(Scenery.ROCKS_37642, Scenery.ROCKS_37639, 2.toByte()),

    /**
     * Tin ore 32 mining node.
     */
    TIN_ORE_32(Scenery.ROCKS_37638, Scenery.ROCKS_37639, 2.toByte()),

    /**
     * Tin ore 33 mining node.
     */
    TIN_ORE_33(Scenery.ROCKS_37685, Scenery.ROCKS_21298, 2.toByte()),

    /**
     * Clay 0 mining node.
     */
    CLAY_0(Scenery.ROCKS_2109, Scenery.ROCKS_452, 3.toByte()),

    /**
     * Clay 1 mining node.
     */
    CLAY_1(Scenery.ROCKS_2108, Scenery.ROCKS_450, 3.toByte()),

    /**
     * Clay 2 mining node.
     */
    CLAY_2(Scenery.ROCKS_9712, Scenery.ROCKS_32448, 3.toByte()),

    /**
     * Clay 3 mining node.
     */
    CLAY_3(Scenery.ROCKS_9713, Scenery.ROCKS_18954, 3.toByte()),

    /**
     * Clay 4 mining node.
     */
    CLAY_4(Scenery.ROCKS_9711, Scenery.ROCKS_32447, 3.toByte()),

    /**
     * Clay 5 mining node.
     */
    CLAY_5(Scenery.ROCKS_10949, Scenery.ROCKS_10945, 3.toByte()),

    /**
     * Clay 6 mining node.
     */
    CLAY_6(Scenery.ROCKS_11190, Scenery.ROCKS_21297, 3.toByte()),

    /**
     * Clay 7 mining node.
     */
    CLAY_7(Scenery.ROCKS_11191, Scenery.ROCKS_21298, 3.toByte()),

    /**
     * Clay 8 mining node.
     */
    CLAY_8(Scenery.ROCKS_11189, Scenery.ROCKS_21296, 3.toByte()),

    /**
     * Clay 9 mining node.
     */
    CLAY_9(Scenery.MINERAL_VEIN_12942, Scenery.MINERAL_VEIN_4995, 3.toByte()),

    /**
     * Clay 10 mining node.
     */
    CLAY_10(Scenery.MINERAL_VEIN_12943, Scenery.MINERAL_VEIN_4996, 3.toByte()),

    /**
     * Clay 11 mining node.
     */
    CLAY_11(Scenery.MINERAL_VEIN_12941, Scenery.MINERAL_VEIN_4994, 3.toByte()),

    /**
     * Clay 12 mining node.
     */
    CLAY_12(Scenery.ROCKS_14904, Scenery.ROCKS_14894, 3.toByte()),

    /**
     * Clay 13 mining node.
     */
    CLAY_13(Scenery.ROCKS_14905, Scenery.ROCKS_14895, 3.toByte()),

    /**
     * Clay 14 mining node.
     */
    CLAY_14(Scenery.ROCKS_15505, Scenery.ROCKS_11557, 3.toByte()),

    /**
     * Clay 15 mining node.
     */
    CLAY_15(Scenery.ROCKS_15504, Scenery.ROCKS_11556, 3.toByte()),

    /**
     * Clay 16 mining node.
     */
    CLAY_16(Scenery.ROCKS_15503, Scenery.ROCKS_11555, 3.toByte()),

    /**
     * Clay 17 mining node.
     */
    CLAY_17(Scenery.ORE_VEIN_20449, Scenery.ORE_VEIN_20443, 3.toByte()),

    /**
     * Clay 18 mining node.
     */
    CLAY_18(Scenery.ORE_VEIN_20450, Scenery.ORE_VEIN_20444, 3.toByte()),

    /**
     * Clay 19 mining node.
     */
    CLAY_19(Scenery.ORE_VEIN_20409, Scenery.ORE_VEIN_20407, 3.toByte()),

    /**
     * Clay 20 mining node.
     */
    CLAY_20(Scenery.ROCKS_32429, Scenery.ROCKS_33400, 3.toByte()),

    /**
     * Clay 21 mining node.
     */
    CLAY_21(Scenery.ROCKS_32430, Scenery.ROCKS_33401, 3.toByte()),

    /**
     * Clay 22 mining node.
     */
    CLAY_22(Scenery.ROCKS_32431, Scenery.ROCKS_33402, 3.toByte()),

    /**
     * Clay 23 mining node.
     */
    CLAY_23(Scenery.ROCKS_31062, Scenery.ROCKS_37639, 3.toByte()),

    /**
     * Clay 24 mining node.
     */
    CLAY_24(Scenery.ROCKS_31063, Scenery.ROCKS_37649, 3.toByte()),

    /**
     * Clay 25 mining node.
     */
    CLAY_25(Scenery.ROCKS_31064, Scenery.ROCKS_37650, 3.toByte()),

    /**
     * Limestone 0 mining node.
     */
    LIMESTONE_0(Scenery.PILE_OF_ROCK_4027, Scenery.PILE_OF_ROCK_4028, 4.toByte()),

    /**
     * Limestone 1 mining node.
     */
    LIMESTONE_1(Scenery.PILE_OF_ROCK_4028, Scenery.PILE_OF_ROCK_4029, 4.toByte()),

    /**
     * Limestone 2 mining node.
     */
    LIMESTONE_2(Scenery.PILE_OF_ROCK_4029, Scenery.PILE_OF_ROCK_4030, 4.toByte()),

    /**
     * Limestone 3 mining node.
     */
    LIMESTONE_3(Scenery.PILE_OF_ROCK_4030, Scenery.PILE_OF_ROCK_4027, 4.toByte()),

    /**
     * Blurite ore 0 mining node.
     */
    BLURITE_ORE_0(Scenery.ROCKS_33220, Scenery.ROCKS_33222, 5.toByte()),

    /**
     * Blurite ore 1 mining node.
     */
    BLURITE_ORE_1(Scenery.ROCKS_33221, Scenery.ROCKS_33223, 5.toByte()),

    /**
     * Iron ore 0 mining node.
     */
    IRON_ORE_0(Scenery.ROCKS_2092, Scenery.ROCKS_450, 6.toByte()),

    /**
     * Iron ore 1 mining node.
     */
    IRON_ORE_1(Scenery.ROCKS_2093, Scenery.ROCKS_452, 6.toByte()),

    /**
     * Iron ore 2 mining node.
     */
    IRON_ORE_2(Scenery.MINERAL_VEIN_4982, Scenery.MINERAL_VEIN_4994, 6.toByte()),

    /**
     * Iron ore 3 mining node.
     */
    IRON_ORE_3(Scenery.MINERAL_VEIN_4983, Scenery.MINERAL_VEIN_4995, 6.toByte()),

    /**
     * Iron ore 4 mining node.
     */
    IRON_ORE_4(Scenery.MINERAL_VEIN_4984, Scenery.MINERAL_VEIN_4996, 6.toByte()),

    /**
     * Iron ore 5 mining node.
     */
    IRON_ORE_5(Scenery.ROCKS_6943, Scenery.ROCKS_21296, 6.toByte()),

    /**
     * Iron ore 6 mining node.
     */
    IRON_ORE_6(Scenery.ROCKS_6944, Scenery.ROCKS_21297, 6.toByte()),

    /**
     * Iron ore 7 mining node.
     */
    IRON_ORE_7(Scenery.ROCKS_9718, Scenery.ROCKS_32448, 6.toByte()),

    /**
     * Iron ore 8 mining node.
     */
    IRON_ORE_8(Scenery.ROCKS_9719, Scenery.ROCKS_18954, 6.toByte()),

    /**
     * Iron ore 9 mining node.
     */
    IRON_ORE_9(Scenery.ROCKS_9717, Scenery.ROCKS_32447, 6.toByte()),

    /**
     * Iron ore 10 mining node.
     */
    IRON_ORE_10(Scenery.ROCKS_11956, Scenery.ROCKS_11557, 6.toByte()),

    /**
     * Iron ore 11 mining node.
     */
    IRON_ORE_11(Scenery.ROCKS_11954, Scenery.ROCKS_11555, 6.toByte()),

    /**
     * Iron ore 12 mining node.
     */
    IRON_ORE_12(Scenery.ROCKS_11955, Scenery.ROCKS_11556, 6.toByte()),

    /**
     * Iron ore 13 mining node.
     */
    IRON_ORE_13(Scenery.ROCKS_14914, Scenery.ROCKS_14895, 6.toByte()),

    /**
     * Iron ore 14 mining node.
     */
    IRON_ORE_14(Scenery.ROCKS_14913, Scenery.ROCKS_14894, 6.toByte()),

    /**
     * Iron ore 15 mining node.
     */
    IRON_ORE_15(Scenery.ROCKS_14858, Scenery.ROCKS_25373, 6.toByte()),

    /**
     * Iron ore 16 mining node.
     */
    IRON_ORE_16(Scenery.ROCKS_14857, Scenery.ROCKS_25372, 6.toByte()),

    /**
     * Iron ore 17 mining node.
     */
    IRON_ORE_17(Scenery.ROCKS_14856, Scenery.ROCKS_25371, 6.toByte()),

    /**
     * Iron ore 18 mining node.
     */
    IRON_ORE_18(Scenery.ROCKS_14900, Scenery.ROCKS_14894, 6.toByte()),

    /**
     * Iron ore 19 mining node.
     */
    IRON_ORE_19(Scenery.ROCKS_14901, Scenery.ROCKS_14895, 6.toByte()),

    /**
     * Iron ore 20 mining node.
     */
    IRON_ORE_20(Scenery.ORE_VEIN_20423, Scenery.ORE_VEIN_20444, 6.toByte()),

    /**
     * Iron ore 21 mining node.
     */
    IRON_ORE_21(Scenery.ORE_VEIN_20422, Scenery.ORE_VEIN_20443, 6.toByte()),

    /**
     * Iron ore 22 mining node.
     */
    IRON_ORE_22(Scenery.ORE_VEIN_20425, Scenery.ORE_VEIN_20407, 6.toByte()),

    /**
     * Iron ore 23 mining node.
     */
    IRON_ORE_23(Scenery.ORE_VEIN_20424, Scenery.ORE_VEIN_20445, 6.toByte()),

    /**
     * Iron ore 24 mining node.
     */
    IRON_ORE_24(Scenery.ROCKS_19002, Scenery.ROCKS_19005, 6.toByte()),

    /**
     * Iron ore 25 mining node.
     */
    IRON_ORE_25(Scenery.ROCKS_19001, Scenery.ROCKS_19004, 6.toByte()),

    /**
     * Iron ore 26 mining node.
     */
    IRON_ORE_26(Scenery.ROCKS_19000, Scenery.ROCKS_19003, 6.toByte()),

    /**
     * Iron ore 27 mining node.
     */
    IRON_ORE_27(Scenery.ROCKS_21281, Scenery.ROCKS_21296, 6.toByte()),

    /**
     * Iron ore 28 mining node.
     */
    IRON_ORE_28(Scenery.ROCKS_21283, Scenery.ROCKS_21298, 6.toByte()),

    /**
     * Iron ore 29 mining node.
     */
    IRON_ORE_29(Scenery.ROCKS_21282, Scenery.ROCKS_21297, 6.toByte()),

    /**
     * Iron ore 30 mining node.
     */
    IRON_ORE_30(Scenery.ROCKS_29221, Scenery.ROCKS_29218, 6.toByte()),

    /**
     * Iron ore 31 mining node.
     */
    IRON_ORE_31(Scenery.ROCKS_29223, Scenery.ROCKS_29220, 6.toByte()),

    /**
     * Iron ore 32 mining node.
     */
    IRON_ORE_32(Scenery.ROCKS_29222, Scenery.ROCKS_29219, 6.toByte()),

    /**
     * Iron ore 33 mining node.
     */
    IRON_ORE_33(Scenery.ROCKS_32441, Scenery.ROCKS_33400, 6.toByte()),

    /**
     * Iron ore 34 mining node.
     */
    IRON_ORE_34(Scenery.ROCKS_32443, Scenery.ROCKS_33402, 6.toByte()),

    /**
     * Iron ore 35 mining node.
     */
    IRON_ORE_35(Scenery.ROCKS_32442, Scenery.ROCKS_33401, 6.toByte()),

    /**
     * Iron ore 36 mining node.
     */
    IRON_ORE_36(Scenery.ROCKS_32452, Scenery.ROCKS_32448, 6.toByte()),

    /**
     * Iron ore 37 mining node.
     */
    IRON_ORE_37(Scenery.ROCKS_32451, Scenery.ROCKS_32447, 6.toByte()),

    /**
     * Iron ore 38 mining node.
     */
    IRON_ORE_38(Scenery.ROCKS_31073, Scenery.ROCKS_37650, 6.toByte()),

    /**
     * Iron ore 39 mining node.
     */
    IRON_ORE_39(Scenery.ROCKS_31072, Scenery.ROCKS_37649, 6.toByte()),

    /**
     * Iron ore 40 mining node.
     */
    IRON_ORE_40(Scenery.ROCKS_31071, Scenery.ROCKS_37639, 6.toByte()),

    /**
     * Iron ore 41 mining node.
     */
    IRON_ORE_41(Scenery.ROCKS_37307, Scenery.ROCKS_11552, 6.toByte()),

    /**
     * Iron ore 42 mining node.
     */
    IRON_ORE_42(Scenery.ROCKS_37309, Scenery.ROCKS_11554, 6.toByte()),

    /**
     * Iron ore 43 mining node.
     */
    IRON_ORE_43(Scenery.ROCKS_37308, Scenery.ROCKS_11553, 6.toByte()),

    /**
     * Silver ore 0 mining node.
     */
    SILVER_ORE_0(Scenery.ROCKS_2101, Scenery.ROCKS_452, 7.toByte()),

    /**
     * Silver ore 1 mining node.
     */
    SILVER_ORE_1(Scenery.ROCKS_2100, Scenery.ROCKS_450, 7.toByte()),

    /**
     * Silver ore 2 mining node.
     */
    SILVER_ORE_2(Scenery.ROCKS_6945, Scenery.ROCKS_21296, 7.toByte()),

    /**
     * Silver ore 3 mining node.
     */
    SILVER_ORE_3(Scenery.ROCKS_6946, Scenery.ROCKS_21297, 7.toByte()),

    /**
     * Silver ore 4 mining node.
     */
    SILVER_ORE_4(Scenery.ROCKS_9716, Scenery.ROCKS_18954, 7.toByte()),

    /**
     * Silver ore 5 mining node.
     */
    SILVER_ORE_5(Scenery.ROCKS_9714, Scenery.ROCKS_32447, 7.toByte()),

    /**
     * Silver ore 6 mining node.
     */
    SILVER_ORE_6(Scenery.ROCKS_9715, Scenery.ROCKS_32448, 7.toByte()),

    /**
     * Silver ore 7 mining node.
     */
    SILVER_ORE_7(Scenery.ROCKS_11188, Scenery.ROCKS_21298, 7.toByte()),

    /**
     * Silver ore 8 mining node.
     */
    SILVER_ORE_8(Scenery.ROCKS_11186, Scenery.ROCKS_21296, 7.toByte()),

    /**
     * Silver ore 9 mining node.
     */
    SILVER_ORE_9(Scenery.ROCKS_11187, Scenery.ROCKS_21297, 7.toByte()),

    /**
     * Silver ore 10 mining node.
     */
    SILVER_ORE_10(Scenery.ROCKS_15581, Scenery.ROCKS_14834, 7.toByte()),

    /**
     * Silver ore 11 mining node.
     */
    SILVER_ORE_11(Scenery.ROCKS_15580, Scenery.ROCKS_14833, 7.toByte()),

    /**
     * Silver ore 12 mining node.
     */
    SILVER_ORE_12(Scenery.ROCKS_15579, Scenery.ROCKS_14832, 7.toByte()),

    /**
     * Silver ore 13 mining node.
     */
    SILVER_ORE_13(Scenery.ROCKS_16998, Scenery.ROCKS_14915, 7.toByte()),

    /**
     * Silver ore 14 mining node.
     */
    SILVER_ORE_14(Scenery.ROCKS_16999, Scenery.ROCKS_14916, 7.toByte()),

    /**
     * Silver ore 15 mining node.
     */
    SILVER_ORE_15(Scenery.ROCKS_17007, Scenery.ROCKS_14915, 7.toByte()),

    /**
     * Silver ore 16 mining node.
     */
    SILVER_ORE_16(Scenery.ROCKS_17000, Scenery.ROCKS_31061, 7.toByte()),

    /**
     * Silver ore 17 mining node.
     */
    SILVER_ORE_17(Scenery.ROCKS_17009, Scenery.ROCKS_31061, 7.toByte()),

    /**
     * Silver ore 18 mining node.
     */
    SILVER_ORE_18(Scenery.ROCKS_17008, Scenery.ROCKS_14916, 7.toByte()),

    /**
     * Silver ore 19 mining node.
     */
    SILVER_ORE_19(Scenery.ROCKS_17385, Scenery.ROCKS_32447, 7.toByte()),

    /**
     * Silver ore 20 mining node.
     */
    SILVER_ORE_20(Scenery.ROCKS_17387, Scenery.ROCKS_18954, 7.toByte()),

    /**
     * Silver ore 21 mining node.
     */
    SILVER_ORE_21(Scenery.ROCKS_17386, Scenery.ROCKS_32448, 7.toByte()),

    /**
     * Silver ore 22 mining node.
     */
    SILVER_ORE_22(Scenery.ROCKS_29225, Scenery.ROCKS_29219, 7.toByte()),

    /**
     * Silver ore 23 mining node.
     */
    SILVER_ORE_23(Scenery.ROCKS_29224, Scenery.ROCKS_29218, 7.toByte()),

    /**
     * Silver ore 24 mining node.
     */
    SILVER_ORE_24(Scenery.ROCKS_29226, Scenery.ROCKS_29220, 7.toByte()),

    /**
     * Silver ore 25 mining node.
     */
    SILVER_ORE_25(Scenery.ROCKS_32445, Scenery.ROCKS_33401, 7.toByte()),

    /**
     * Silver ore 26 mining node.
     */
    SILVER_ORE_26(Scenery.ROCKS_32444, Scenery.ROCKS_33400, 7.toByte()),

    /**
     * Silver ore 27 mining node.
     */
    SILVER_ORE_27(Scenery.ROCKS_32446, Scenery.ROCKS_33402, 7.toByte()),

    /**
     * Silver ore 28 mining node.
     */
    SILVER_ORE_28(Scenery.ROCKS_31075, Scenery.ROCKS_37649, 7.toByte()),

    /**
     * Silver ore 29 mining node.
     */
    SILVER_ORE_29(Scenery.ROCKS_31074, Scenery.ROCKS_37639, 7.toByte()),

    /**
     * Silver ore 30 mining node.
     */
    SILVER_ORE_30(Scenery.ROCKS_31076, Scenery.ROCKS_37650, 7.toByte()),

    /**
     * Silver ore 31 mining node.
     */
    SILVER_ORE_31(Scenery.ROCKS_37305, Scenery.ROCKS_11553, 7.toByte()),

    /**
     * Silver ore 32 mining node.
     */
    SILVER_ORE_32(Scenery.ROCKS_37304, Scenery.ROCKS_11552, 7.toByte()),

    /**
     * Silver ore 33 mining node.
     */
    SILVER_ORE_33(Scenery.ROCKS_37306, Scenery.ROCKS_11554, 7.toByte()),

    /**
     * Silver ore 34 mining node.
     */
    SILVER_ORE_34(Scenery.ROCKS_37670, Scenery.ROCKS_11552, 7.toByte()),

    /**
     * Silver ore 35 mining node.
     */
    SILVER_ORE_35(Scenery.ROCKS_11948, Scenery.ROCKS_11555, 7.toByte()),

    /**
     * Silver ore 36 mining node.
     */
    SILVER_ORE_36(Scenery.ROCKS_11949, Scenery.ROCKS_11556, 7.toByte()),

    /**
     * Silver ore 37 mining node.
     */
    SILVER_ORE_37(Scenery.ROCKS_11950, Scenery.ROCKS_11557, 7.toByte()),

    /**
     * Silver ore 38 mining node.
     */
    SILVER_ORE_38(2311, Scenery.ROCKS_11552, 7.toByte()),

    /**
     * Coal 0 mining node.
     */
    COAL_0(Scenery.ROCKS_2097, 452, 8.toByte()),

    /**
     * Coal 1 mining node.
     */
    COAL_1(Scenery.ROCKS_2096, 450, 8.toByte()),

    /**
     * Coal 2 mining node.
     */
    COAL_2(Scenery.MINERAL_VEIN_4985, Scenery.MINERAL_VEIN_4994, 8.toByte()),

    /**
     * Coal 3 mining node.
     */
    COAL_3(Scenery.MINERAL_VEIN_4986, Scenery.MINERAL_VEIN_4995, 8.toByte()),

    /**
     * Coal 4 mining node.
     */
    COAL_4(Scenery.MINERAL_VEIN_4987, Scenery.MINERAL_VEIN_4996, 8.toByte()),

    /**
     * Coal 5 mining node.
     */
    COAL_5(Scenery.ROCKS_4676, Scenery.ROCKS_450, 8.toByte()),

    /**
     * Coal 6 mining node.
     */
    COAL_6(Scenery.ROCKS_10948, Scenery.ROCKS_10944, 8.toByte()),

    /**
     * Coal 7 mining node.
     */
    COAL_7(Scenery.ROCKS_11964, Scenery.ROCKS_11556, 8.toByte()),

    /**
     * Coal 8 mining node.
     */
    COAL_8(Scenery.ROCKS_11965, Scenery.ROCKS_11557, 8.toByte()),

    /**
     * Coal 9 mining node.
     */
    COAL_9(Scenery.ROCKS_11963, Scenery.ROCKS_11555, 8.toByte()),

    /**
     * Coal 10 mining node.
     */
    COAL_10(Scenery.ROCKS_11932, Scenery.ROCKS_11554, 8.toByte()),

    /**
     * Coal 11 mining node.
     */
    COAL_11(Scenery.ROCKS_11930, Scenery.ROCKS_11552, 8.toByte()),

    /**
     * Coal 12 mining node.
     */
    COAL_12(Scenery.ROCKS_11931, Scenery.ROCKS_11553, 8.toByte()),

    /**
     * Coal 13 mining node.
     */
    COAL_13(Scenery.ROCKS_15246, Scenery.ROCKS_15249, 8.toByte()),

    /**
     * Coal 14 mining node.
     */
    COAL_14(Scenery.ROCKS_15247, Scenery.ROCKS_15250, 8.toByte()),

    /**
     * Coal 15 mining node.
     */
    COAL_15(Scenery.ROCKS_15248, Scenery.ROCKS_15251, 8.toByte()),

    /**
     * Coal 16 mining node.
     */
    COAL_16(Scenery.ROCKS_14852, Scenery.ROCKS_25373, 8.toByte()),

    /**
     * Coal 17 mining node.
     */
    COAL_17(Scenery.ROCKS_14851, Scenery.ROCKS_25372, 8.toByte()),

    /**
     * Coal 18 mining node.
     */
    COAL_18(Scenery.ROCKS_14850, Scenery.ROCKS_25371, 8.toByte()),

    /**
     * Coal 19 mining node.
     */
    COAL_19(Scenery.ORE_VEIN_20410, Scenery.ORE_VEIN_20443, 8.toByte()),

    /**
     * Coal 20 mining node.
     */
    COAL_20(Scenery.ORE_VEIN_20411, Scenery.ORE_VEIN_20444, 8.toByte()),

    /**
     * Coal 21 mining node.
     */
    COAL_21(Scenery.ORE_VEIN_20412, Scenery.ORE_VEIN_20445, 8.toByte()),

    /**
     * Coal 22 mining node.
     */
    COAL_22(Scenery.ORE_VEIN_20413, Scenery.ORE_VEIN_20407, 8.toByte()),

    /**
     * Coal 23 mining node.
     */
    COAL_23(Scenery.ROCKS_18999, Scenery.ROCKS_19005, 8.toByte()),

    /**
     * Coal 24 mining node.
     */
    COAL_24(Scenery.ROCKS_18998, Scenery.ROCKS_19004, 8.toByte()),

    /**
     * Coal 25 mining node.
     */
    COAL_25(Scenery.ROCKS_18997, Scenery.ROCKS_19003, 8.toByte()),

    /**
     * Coal 26 mining node.
     */
    COAL_26(Scenery.ROCKS_21287, Scenery.ROCKS_21296, 8.toByte()),

    /**
     * Coal 27 mining node.
     */
    COAL_27(Scenery.ROCKS_21289, Scenery.ROCKS_21298, 8.toByte()),

    /**
     * Coal 28 mining node.
     */
    COAL_28(Scenery.ROCKS_21288, Scenery.ROCKS_21297, 8.toByte()),

    /**
     * Coal 29 mining node.
     */
    COAL_29(Scenery.ROCKS_23565, Scenery.ROCKS_21298, 8.toByte()),

    /**
     * Coal 30 mining node.
     */
    COAL_30(Scenery.ROCKS_23564, Scenery.ROCKS_21297, 8.toByte()),

    /**
     * Coal 31 mining node.
     */
    COAL_31(Scenery.ROCKS_23563, Scenery.ROCKS_21296, 8.toByte()),

    /**
     * Coal 32 mining node.
     */
    COAL_32(Scenery.ROCKS_29215, Scenery.ROCKS_29218, 8.toByte()),

    /**
     * Coal 33 mining node.
     */
    COAL_33(Scenery.ROCKS_29217, Scenery.ROCKS_29220, 8.toByte()),

    /**
     * Coal 34 mining node.
     */
    COAL_34(Scenery.ROCKS_29216, Scenery.ROCKS_29219, 8.toByte()),

    /**
     * Coal 35 mining node.
     */
    COAL_35(Scenery.ROCKS_32426, Scenery.ROCKS_33400, 8.toByte()),

    /**
     * Coal 36 mining node.
     */
    COAL_36(Scenery.ROCKS_32427, Scenery.ROCKS_33401, 8.toByte()),

    /**
     * Coal 37 mining node.
     */
    COAL_37(Scenery.ROCKS_32428, Scenery.ROCKS_33402, 8.toByte()),

    /**
     * Coal 38 mining node.
     */
    COAL_38(Scenery.ROCKS_32450, Scenery.ROCKS_32448, 8.toByte()),

    /**
     * Coal 39 mining node.
     */
    COAL_39(Scenery.ROCKS_32449, Scenery.ROCKS_32447, 8.toByte()),

    /**
     * Coal 40 mining node.
     */
    COAL_40(Scenery.ROCKS_31068, Scenery.ROCKS_37639, 8.toByte()),

    /**
     * Coal 41 mining node.
     */
    COAL_41(Scenery.ROCKS_31069, Scenery.ROCKS_37649, 8.toByte()),

    /**
     * Coal 42 mining node.
     */
    COAL_42(Scenery.ROCKS_31070, Scenery.ROCKS_37650, 8.toByte()),

    /**
     * Coal 43 mining node.
     */
    COAL_43(Scenery.ROCKS_31168, Scenery.ROCKS_14833, 8.toByte()),

    /**
     * Coal 44 mining node.
     */
    COAL_44(Scenery.ROCKS_31169, Scenery.ROCKS_14834, 8.toByte()),

    /**
     * Coal 45 mining node.
     */
    COAL_45(Scenery.ROCKS_31167, Scenery.ROCKS_14832, 8.toByte()),

    /**
     * Coal 46 mining node.
     */
    COAL_46(Scenery.ROCKS_37699, Scenery.ROCKS_21298, 8.toByte()),

    /**
     * Coal 47 mining node.
     */
    COAL_47(Scenery.ROCKS_37698, Scenery.ROCKS_21297, 8.toByte()),

    /**
     * Coal 48 mining node.
     */
    COAL_48(Scenery.ROCKS_37697, Scenery.ROCKS_21296, 8.toByte()),

    /**
     * Gold ore 0 mining node.
     */
    GOLD_ORE_0(Scenery.ROCKS_2099, Scenery.ROCKS_452, 9.toByte()),

    /**
     * Gold ore 1 mining node.
     */
    GOLD_ORE_1(Scenery.ROCKS_2098, Scenery.ROCKS_450, 9.toByte()),

    /**
     * Gold ore 2 mining node.
     */
    GOLD_ORE_2(Scenery.ROCKS_2611, Scenery.ROCKS_21298, 9.toByte()),

    /**
     * Gold ore 3 mining node.
     */
    GOLD_ORE_3(Scenery.ROCKS_2610, Scenery.ROCKS_21297, 9.toByte()),

    /**
     * Gold ore 4 mining node.
     */
    GOLD_ORE_4(Scenery.ROCKS_2609, Scenery.ROCKS_21296, 9.toByte()),

    /**
     * Gold ore 5 mining node.
     */
    GOLD_ORE_5(Scenery.ROCKS_9722, Scenery.ROCKS_18954, 9.toByte()),

    /**
     * Gold ore 6 mining node.
     */
    GOLD_ORE_6(Scenery.ROCKS_9720, Scenery.ROCKS_32447, 9.toByte()),

    /**
     * Gold ore 7 mining node.
     */
    GOLD_ORE_7(Scenery.ROCKS_9721, Scenery.ROCKS_32448, 9.toByte()),

    /**
     * Gold ore 8 mining node.
     */
    GOLD_ORE_8(Scenery.ROCKS_11183, Scenery.ROCKS_21296, 9.toByte()),

    /**
     * Gold ore 9 mining node.
     */
    GOLD_ORE_9(Scenery.ROCKS_11184, Scenery.ROCKS_21297, 9.toByte()),

    /**
     * Gold ore 10 mining node.
     */
    GOLD_ORE_10(Scenery.ROCKS_11185, Scenery.ROCKS_21298, 9.toByte()),

    /**
     * Gold ore 11 mining node.
     */
    GOLD_ORE_11(Scenery.ROCKS_11952, Scenery.ROCKS_11556, 9.toByte()),

    /**
     * Gold ore 12 mining node.
     */
    GOLD_ORE_12(Scenery.ROCKS_11953, Scenery.ROCKS_11557, 9.toByte()),

    /**
     * Gold ore 13 mining node.
     */
    GOLD_ORE_13(Scenery.ROCKS_11951, Scenery.ROCKS_11555, 9.toByte()),

    /**
     * Gold ore 14 mining node.
     */
    GOLD_ORE_14(Scenery.ROCKS_15578, Scenery.ROCKS_14834, 9.toByte()),

    /**
     * Gold ore 15 mining node.
     */
    GOLD_ORE_15(Scenery.ROCKS_15577, Scenery.ROCKS_14833, 9.toByte()),

    /**
     * Gold ore 16 mining node.
     */
    GOLD_ORE_16(Scenery.ROCKS_15576, Scenery.ROCKS_14832, 9.toByte()),

    /**
     * Gold ore 17 mining node.
     */
    GOLD_ORE_17(Scenery.ROCKS_17002, Scenery.ROCKS_14916, 9.toByte()),

    /**
     * Gold ore 18 mining node.
     */
    GOLD_ORE_18(Scenery.ROCKS_17003, Scenery.ROCKS_31061, 9.toByte()),

    /**
     * Gold ore 19 mining node.
     */
    GOLD_ORE_19(Scenery.ROCKS_17001, Scenery.ROCKS_14915, 9.toByte()),

    /**
     * Gold ore 20 mining node.
     */
    GOLD_ORE_20(Scenery.ROCKS_21291, Scenery.ROCKS_21297, 9.toByte()),

    /**
     * Gold ore 21 mining node.
     */
    GOLD_ORE_21(Scenery.ROCKS_21290, Scenery.ROCKS_21296, 9.toByte()),

    /**
     * Gold ore 22 mining node.
     */
    GOLD_ORE_22(Scenery.ROCKS_21292, Scenery.ROCKS_21298, 9.toByte()),

    /**
     * Gold ore 23 mining node.
     */
    GOLD_ORE_23(Scenery.ROCKS_32433, Scenery.ROCKS_33401, 9.toByte()),

    /**
     * Gold ore 24 mining node.
     */
    GOLD_ORE_24(Scenery.ROCKS_32432, Scenery.ROCKS_33400, 9.toByte()),

    /**
     * Gold ore 25 mining node.
     */
    GOLD_ORE_25(Scenery.ROCKS_32434, Scenery.ROCKS_33402, 9.toByte()),

    /**
     * Gold ore 26 mining node.
     */
    GOLD_ORE_26(Scenery.ROCKS_31065, Scenery.ROCKS_37639, 9.toByte()),

    /**
     * Gold ore 27 mining node.
     */
    GOLD_ORE_27(Scenery.ROCKS_31066, Scenery.ROCKS_37649, 9.toByte()),

    /**
     * Gold ore 28 mining node.
     */
    GOLD_ORE_28(Scenery.ROCKS_31067, Scenery.ROCKS_37650, 9.toByte()),

    /**
     * Gold ore 29 mining node.
     */
    GOLD_ORE_29(Scenery.ROCKS_37311, Scenery.ROCKS_11553, 9.toByte()),

    /**
     * Gold ore 30 mining node.
     */
    GOLD_ORE_30(Scenery.ROCKS_37310, Scenery.ROCKS_11552, 9.toByte()),

    /**
     * Gold ore 31 mining node.
     */
    GOLD_ORE_31(Scenery.ROCKS_37312, Scenery.ROCKS_11554, 9.toByte()),

    /**
     * Gold ore 32 mining node.
     */
    GOLD_ORE_32(Scenery.ROCKS_37471, Scenery.ROCKS_15249, 9.toByte()),

    /**
     * Gold ore 33 mining node.
     */
    GOLD_ORE_33(Scenery.ROCKS_37473, Scenery.ROCKS_15251, 9.toByte()),

    /**
     * Gold ore 34 mining node.
     */
    GOLD_ORE_34(Scenery.ROCKS_37472, Scenery.ROCKS_15250, 9.toByte()),

    /**
     * Mithril ore 0 mining node.
     */
    MITHRIL_ORE_0(Scenery.ROCKS_2103, Scenery.ROCKS_452, 10.toByte()),

    /**
     * Mithril ore 1 mining node.
     */
    MITHRIL_ORE_1(Scenery.ROCKS_2102, Scenery.ROCKS_450, 10.toByte()),

    /**
     * Mithril ore 2 mining node.
     */
    MITHRIL_ORE_2(Scenery.MINERAL_VEIN_4988, Scenery.MINERAL_VEIN_4994, 10.toByte()),

    /**
     * Mithril ore 3 mining node.
     */
    MITHRIL_ORE_3(Scenery.MINERAL_VEIN_4989, Scenery.MINERAL_VEIN_4995, 10.toByte()),

    /**
     * Mithril ore 4 mining node.
     */
    MITHRIL_ORE_4(Scenery.MINERAL_VEIN_4990, Scenery.MINERAL_VEIN_4996, 10.toByte()),

    /**
     * Mithril ore 5 mining node.
     */
    MITHRIL_ORE_5(Scenery.ROCKS_11943, Scenery.ROCKS_11553, 10.toByte()),

    /**
     * Mithril ore 6 mining node.
     */
    MITHRIL_ORE_6(Scenery.ROCKS_11942, Scenery.ROCKS_11552, 10.toByte()),

    /**
     * Mithril ore 7 mining node.
     */
    MITHRIL_ORE_7(Scenery.ROCKS_11945, Scenery.ROCKS_11555, 10.toByte()),

    /**
     * Mithril ore 8 mining node.
     */
    MITHRIL_ORE_8(Scenery.ROCKS_11944, Scenery.ROCKS_11554, 10.toByte()),

    /**
     * Mithril ore 9 mining node.
     */
    MITHRIL_ORE_9(Scenery.ROCKS_11947, Scenery.ROCKS_11557, 10.toByte()),

    /**
     * Mithril ore 10 mining node.
     */
    MITHRIL_ORE_10(Scenery.ROCKS_11946, Scenery.ROCKS_11556, 10.toByte()),

    /**
     * Mithril ore 11 mining node.
     */
    MITHRIL_ORE_11(Scenery.ROCKS_14855, Scenery.ROCKS_25373, 10.toByte()),

    /**
     * Mithril ore 12 mining node.
     */
    MITHRIL_ORE_12(Scenery.ROCKS_14854, Scenery.ROCKS_25372, 10.toByte()),

    /**
     * Mithril ore 13 mining node.
     */
    MITHRIL_ORE_13(Scenery.ROCKS_14853, Scenery.ROCKS_25371, 10.toByte()),

    /**
     * Mithril ore 14 mining node.
     */
    MITHRIL_ORE_14(Scenery.ROCKS_14854, Scenery.ROCKS_25372, 10.toByte()),

    /**
     * Mithril ore 15 mining node.
     */
    MITHRIL_ORE_15(Scenery.RUNE_ESSENCE_16687, Scenery.ROCKS_450, 10.toByte()),

    /**
     * Mithril ore 16 mining node.
     */
    MITHRIL_ORE_16(Scenery.ORE_VEIN_20421, Scenery.ORE_VEIN_20407, 10.toByte()),

    /**
     * Mithril ore 17 mining node.
     */
    MITHRIL_ORE_17(Scenery.ORE_VEIN_20420, Scenery.ORE_VEIN_20445, 10.toByte()),

    /**
     * Mithril ore 18 mining node.
     */
    MITHRIL_ORE_18(Scenery.ORE_VEIN_20419, Scenery.ORE_VEIN_20444, 10.toByte()),

    /**
     * Mithril ore 19 mining node.
     */
    MITHRIL_ORE_19(Scenery.ORE_VEIN_20418, Scenery.ORE_VEIN_20443, 10.toByte()),

    /**
     * Mithril ore 20 mining node.
     */
    MITHRIL_ORE_20(Scenery.ROCKS_19012, Scenery.ROCKS_19021, 10.toByte()),

    /**
     * Mithril ore 21 mining node.
     */
    MITHRIL_ORE_21(Scenery.ROCKS_19013, Scenery.ROCKS_19016, 10.toByte()),

    /**
     * Mithril ore 22 mining node.
     */
    MITHRIL_ORE_22(Scenery.ROCKS_19014, Scenery.ROCKS_19017, 10.toByte()),

    /**
     * Mithril ore 23 mining node.
     */
    MITHRIL_ORE_23(Scenery.ROCKS_21278, Scenery.ROCKS_21296, 10.toByte()),

    /**
     * Mithril ore 24 mining node.
     */
    MITHRIL_ORE_24(Scenery.ROCKS_21279, Scenery.ROCKS_21297, 10.toByte()),

    /**
     * Mithril ore 25 mining node.
     */
    MITHRIL_ORE_25(Scenery.ROCKS_21280, Scenery.ROCKS_21298, 10.toByte()),

    /**
     * Mithril ore 26 mining node.
     */
    MITHRIL_ORE_26(Scenery.ROCKS_25369, Scenery.ROCKS_10586, 10.toByte()),

    /**
     * Mithril ore 27 mining node.
     */
    MITHRIL_ORE_27(Scenery.ROCKS_25368, Scenery.ROCKS_10585, 10.toByte()),

    /**
     * Mithril ore 28 mining node.
     */
    MITHRIL_ORE_28(Scenery.ROCKS_25370, Scenery.ROCKS_10587, 10.toByte()),

    /**
     * Mithril ore 29 mining node.
     */
    MITHRIL_ORE_29(Scenery.ROCKS_29236, Scenery.ROCKS_29218, 10.toByte()),

    /**
     * Mithril ore 30 mining node.
     */
    MITHRIL_ORE_30(Scenery.ROCKS_29237, Scenery.ROCKS_29219, 10.toByte()),

    /**
     * Mithril ore 31 mining node.
     */
    MITHRIL_ORE_31(Scenery.ROCKS_29238, Scenery.ROCKS_29220, 10.toByte()),

    /**
     * Mithril ore 32 mining node.
     */
    MITHRIL_ORE_32(Scenery.ROCKS_32439, Scenery.ROCKS_33401, 10.toByte()),

    /**
     * Mithril ore 33 mining node.
     */
    MITHRIL_ORE_33(Scenery.ROCKS_32438, Scenery.ROCKS_33400, 10.toByte()),

    /**
     * Mithril ore 34 mining node.
     */
    MITHRIL_ORE_34(Scenery.ROCKS_32440, Scenery.ROCKS_33402, 10.toByte()),

    /**
     * Mithril ore 35 mining node.
     */
    MITHRIL_ORE_35(Scenery.ROCKS_31087, Scenery.ROCKS_37649, 10.toByte()),

    /**
     * Mithril ore 36 mining node.
     */
    MITHRIL_ORE_36(Scenery.ROCKS_31086, Scenery.ROCKS_37639, 10.toByte()),

    /**
     * Mithril ore 37 mining node.
     */
    MITHRIL_ORE_37(Scenery.ROCKS_31088, Scenery.ROCKS_37650, 10.toByte()),

    /**
     * Mithril ore 38 mining node.
     */
    MITHRIL_ORE_38(Scenery.ROCKS_31170, Scenery.ROCKS_14832, 10.toByte()),

    /**
     * Mithril ore 39 mining node.
     */
    MITHRIL_ORE_39(Scenery.ROCKS_31171, Scenery.ROCKS_14833, 10.toByte()),

    /**
     * Mithril ore 40 mining node.
     */
    MITHRIL_ORE_40(Scenery.ROCKS_31172, Scenery.ROCKS_14834, 10.toByte()),

    /**
     * Mithril ore 41 mining node.
     */
    MITHRIL_ORE_41(Scenery.ROCKS_37692, Scenery.ROCKS_21296, 10.toByte()),

    /**
     * Mithril ore 42 mining node.
     */
    MITHRIL_ORE_42(Scenery.ROCKS_37693, Scenery.ROCKS_21297, 10.toByte()),

    /**
     * Mithril ore 43 mining node.
     */
    MITHRIL_ORE_43(Scenery.ROCKS_37694, Scenery.ROCKS_21298, 10.toByte()),

    /**
     * Adamantite ore 0 mining node.
     */
    ADAMANTITE_ORE_0(Scenery.ROCKS_2105, Scenery.ROCKS_452, 11.toByte()),

    /**
     * Adamantite ore 1 mining node.
     */
    ADAMANTITE_ORE_1(Scenery.ROCKS_2104, Scenery.ROCKS_450, 11.toByte()),

    /**
     * Adamantite ore 2 mining node.
     */
    ADAMANTITE_ORE_2(Scenery.MINERAL_VEIN_4991, Scenery.MINERAL_VEIN_4994, 11.toByte()),

    /**
     * Adamantite ore 3 mining node.
     */
    ADAMANTITE_ORE_3(Scenery.MINERAL_VEIN_4992, Scenery.MINERAL_VEIN_4995, 11.toByte()),

    /**
     * Adamantite ore 4 mining node.
     */
    ADAMANTITE_ORE_4(Scenery.MINERAL_VEIN_4993, Scenery.MINERAL_VEIN_4996, 11.toByte()),

    /**
     * Adamantite ore 5 mining node.
     */
    ADAMANTITE_ORE_5(Scenery.ROCKS_11941, Scenery.ROCKS_11554, 11.toByte()),

    /**
     * Adamantite ore 6 mining node.
     */
    ADAMANTITE_ORE_6(Scenery.ROCKS_11940, Scenery.ROCKS_11553, 11.toByte()),

    /**
     * Adamantite ore 7 mining node.
     */
    ADAMANTITE_ORE_7(Scenery.ROCKS_11939, Scenery.ROCKS_11552, 11.toByte()),

    /**
     * Adamantite ore 8 mining node.
     */
    ADAMANTITE_ORE_8(Scenery.ROCKS_14864, Scenery.ROCKS_25373, 11.toByte()),

    /**
     * Adamantite ore 9 mining node.
     */
    ADAMANTITE_ORE_9(Scenery.ROCKS_14863, Scenery.ROCKS_25372, 11.toByte()),

    /**
     * Adamantite ore 10 mining node.
     */
    ADAMANTITE_ORE_10(Scenery.ROCKS_14862, Scenery.ROCKS_25371, 11.toByte()),

    /**
     * Adamantite ore 11 mining node.
     */
    ADAMANTITE_ORE_11(Scenery.ORE_VEIN_20417, Scenery.ORE_VEIN_20407, 11.toByte()),

    /**
     * Adamantite ore 12 mining node.
     */
    ADAMANTITE_ORE_12(Scenery.ORE_VEIN_20416, Scenery.ORE_VEIN_20445, 11.toByte()),

    /**
     * Adamantite ore 13 mining node.
     */
    ADAMANTITE_ORE_13(Scenery.ORE_VEIN_20414, Scenery.ORE_VEIN_20443, 11.toByte()),

    /**
     * Adamantite ore 14 mining node.
     */
    ADAMANTITE_ORE_14(Scenery.ORE_VEIN_20415, Scenery.ORE_VEIN_20444, 11.toByte()),

    /**
     * Adamantite ore 15 mining node.
     */
    ADAMANTITE_ORE_15(Scenery.ROCKS_19020, Scenery.ROCKS_19017, 11.toByte()),

    /**
     * Adamantite ore 16 mining node.
     */
    ADAMANTITE_ORE_16(Scenery.ROCKS_19018, Scenery.ROCKS_19021, 11.toByte()),

    /**
     * Adamantite ore 17 mining node.
     */
    ADAMANTITE_ORE_17(Scenery.ROCKS_19019, Scenery.ROCKS_19016, 11.toByte()),

    /**
     * Adamantite ore 18 mining node.
     */
    ADAMANTITE_ORE_18(Scenery.ROCKS_21275, Scenery.ROCKS_21296, 11.toByte()),

    /**
     * Adamantite ore 19 mining node.
     */
    ADAMANTITE_ORE_19(Scenery.ROCKS_21276, Scenery.ROCKS_21297, 11.toByte()),

    /**
     * Adamantite ore 20 mining node.
     */
    ADAMANTITE_ORE_20(Scenery.ROCKS_21277, Scenery.ROCKS_21298, 11.toByte()),

    /**
     * Adamantite ore 21 mining node.
     */
    ADAMANTITE_ORE_21(Scenery.ROCKS_29233, Scenery.ROCKS_29218, 11.toByte()),

    /**
     * Adamantite ore 22 mining node.
     */
    ADAMANTITE_ORE_22(Scenery.ROCKS_29234, Scenery.ROCKS_29219, 11.toByte()),

    /**
     * Adamantite ore 23 mining node.
     */
    ADAMANTITE_ORE_23(Scenery.ROCKS_29235, Scenery.ROCKS_29220, 11.toByte()),

    /**
     * Adamantite ore 24 mining node.
     */
    ADAMANTITE_ORE_24(Scenery.ROCKS_32435, Scenery.ROCKS_33400, 11.toByte()),

    /**
     * Adamantite ore 25 mining node.
     */
    ADAMANTITE_ORE_25(Scenery.ROCKS_32437, Scenery.ROCKS_33402, 11.toByte()),

    /**
     * Adamantite ore 26 mining node.
     */
    ADAMANTITE_ORE_26(Scenery.ROCKS_32436, Scenery.ROCKS_33401, 11.toByte()),

    /**
     * Adamantite ore 27 mining node.
     */
    ADAMANTITE_ORE_27(Scenery.ROCKS_31083, Scenery.ROCKS_37639, 11.toByte()),

    /**
     * Adamantite ore 28 mining node.
     */
    ADAMANTITE_ORE_28(Scenery.ROCKS_31085, Scenery.ROCKS_37650, 11.toByte()),

    /**
     * Adamantite ore 29 mining node.
     */
    ADAMANTITE_ORE_29(Scenery.ROCKS_31084, Scenery.ROCKS_37649, 11.toByte()),

    /**
     * Adamantite ore 30 mining node.
     */
    ADAMANTITE_ORE_30(Scenery.ROCKS_31173, Scenery.ROCKS_14832, 11.toByte()),

    /**
     * Adamantite ore 31 mining node.
     */
    ADAMANTITE_ORE_31(Scenery.ROCKS_31174, Scenery.ROCKS_14833, 11.toByte()),

    /**
     * Adamantite ore 32 mining node.
     */
    ADAMANTITE_ORE_32(Scenery.ROCKS_31175, Scenery.ROCKS_14834, 11.toByte()),

    /**
     * Adamantite ore 33 mining node.
     */
    ADAMANTITE_ORE_33(Scenery.ROCKS_37468, Scenery.ROCKS_15249, 11.toByte()),

    /**
     * Adamantite ore 34 mining node.
     */
    ADAMANTITE_ORE_34(Scenery.ROCKS_37469, Scenery.ROCKS_15250, 11.toByte()),

    /**
     * Adamantite ore 35 mining node.
     */
    ADAMANTITE_ORE_35(Scenery.ROCKS_37470, Scenery.ROCKS_15251, 11.toByte()),

    /**
     * Adamantite ore 36 mining node.
     */
    ADAMANTITE_ORE_36(Scenery.ROCKS_37689, Scenery.ROCKS_21296, 11.toByte()),

    /**
     * Adamantite ore 37 mining node.
     */
    ADAMANTITE_ORE_37(Scenery.ROCKS_37690, Scenery.ROCKS_21297, 11.toByte()),

    /**
     * Adamantite ore 38 mining node.
     */
    ADAMANTITE_ORE_38(Scenery.ROCKS_37691, Scenery.ROCKS_21298, 11.toByte()),

    /**
     * Runite ore 0 mining node.
     */
    RUNITE_ORE_0(Scenery.ROCKS_2107, Scenery.ROCKS_452, 12.toByte()),

    /**
     * Runite ore 1 mining node.
     */
    RUNITE_ORE_1(Scenery.ROCKS_2106, Scenery.ROCKS_450, 12.toByte()),

    /**
     * Runite ore 2 mining node.
     */
    RUNITE_ORE_2(Scenery.ROCKS_14861, Scenery.ROCKS_25373, 12.toByte()),

    /**
     * Runite ore 3 mining node.
     */
    RUNITE_ORE_3(Scenery.ROCKS_14860, Scenery.ROCKS_25372, 12.toByte()),

    /**
     * Runite ore 4 mining node.
     */
    RUNITE_ORE_4(Scenery.ROCKS_14859, Scenery.ROCKS_25371, 12.toByte()),

    /**
     * Runite ore 5 mining node.
     */
    RUNITE_ORE_5(Scenery.ROCKS_33079, Scenery.ROCKS_33401, 12.toByte()),

    /**
     * Runite ore 6 mining node.
     */
    RUNITE_ORE_6(Scenery.ROCKS_33078, Scenery.ROCKS_33400, 12.toByte()),

    /**
     * Runite ore 7 mining node.
     */
    RUNITE_ORE_7(Scenery.ROCKS_37208, Scenery.ROCKS_21296, 12.toByte()),

    /**
     * Runite ore 8 mining node.
     */
    RUNITE_ORE_8(Scenery.ROCKS_37465, Scenery.ROCKS_15249, 12.toByte()),

    /**
     * Runite ore 9 mining node.
     */
    RUNITE_ORE_9(Scenery.ROCKS_37466, Scenery.ROCKS_15250, 12.toByte()),

    /**
     * Runite ore 10 mining node.
     */
    RUNITE_ORE_10(Scenery.ROCKS_37467, Scenery.ROCKS_15251, 12.toByte()),

    /**
     * Runite ore 11 mining node.
     */
    RUNITE_ORE_11(Scenery.ROCKS_37695, Scenery.ROCKS_21297, 12.toByte()),

    /**
     * Runite ore 12 mining node.
     */
    RUNITE_ORE_12(Scenery.ROCKS_37696, Scenery.ROCKS_21298, 12.toByte()),

    /**
     * Magic stone 0 mining node.
     */
    MAGIC_STONE_0(Scenery.ROCKS_6669, Scenery.ROCKS_21296, 18.toByte()),

    /**
     * Magic stone 1 mining node.
     */
    MAGIC_STONE_1(Scenery.ROCKS_6671, Scenery.ROCKS_21298, 18.toByte()),

    /**
     * Magic stone 2 mining node.
     */
    MAGIC_STONE_2(Scenery.ROCKS_6670, Scenery.ROCKS_21297, 18.toByte()),


    /**
     * Gem rock 0 mining node.
     */
    GEM_ROCK_0(Scenery.ROCKS_23567, Scenery.ROCKS_21297, 13.toByte()),

    /**
     * Gem rock 1 mining node.
     */
    GEM_ROCK_1(Scenery.ROCKS_23566, Scenery.ROCKS_21296, 13.toByte()),

    /**
     * Gem rock 2 mining node.
     */
    GEM_ROCK_2(Scenery.ROCKS_23568, Scenery.ROCKS_21298, 13.toByte()),

    /**
     * Gem rock 3 mining node.
     */
    GEM_ROCK_3(Scenery.ROCKS_23560, Scenery.ROCKS_25371, 13.toByte()),

    /**
     * Gem rock 4 mining node.
     */
    GEM_ROCK_4(Scenery.ROCKS_23561, Scenery.ROCKS_25372, 13.toByte()),

    /**
     * Gem rock 5 mining node.
     */
    GEM_ROCK_5(Scenery.ROCKS_23562, Scenery.ROCKS_21298, 13.toByte()),

    /**
     * Gem rock 6 mining node.
     */
    GEM_ROCK_6(Scenery.GEM_ROCK_9030, Scenery.LIGHT_JUNGLE_9010, 13.toByte()),

    /**
     * Gem rock 7 mining node.
     */
    GEM_ROCK_7(Scenery.GEM_ROCK_9031, Scenery.MEDIUM_JUNGLE_9015, 13.toByte()),

    /**
     * Gem rock 8 mining node.
     */
    GEM_ROCK_8(Scenery.GEM_ROCK_9032, Scenery.DENSE_JUNGLE_9020, 13.toByte()),

    /**
     * Rune essence 0 mining node.
     */
    RUNE_ESSENCE_0(Scenery.RUNE_ESSENCE_2491, -1, 14.toByte()),

    /**
     * Rune essence 1 mining node.
     */
    RUNE_ESSENCE_1(Scenery.ROCK_16684, -1, 14.toByte()),


    /**
     * Sandstone mining node.
     */
    SANDSTONE(Scenery.ROCKS_10946, Scenery.ROCKS_10944, 15.toByte()),

    /**
     * Granite mining node.
     */
    GRANITE(Scenery.ROCKS_10947, Scenery.ROCKS_10945, 16.toByte()),

    /**
     * Rubium mining node.
     */
    RUBIUM(Scenery.RUBIUM_29746, Scenery.WALL_29747, 17.toByte()),

    /**
     * Obsidian mining node 0.
     */
    OBSIDIAN_0(Scenery.OBSIDIAN_WALL_9376, Scenery.OBSIDIAN_WALL_31229, 19.toByte()),

    /**
     * Obsidian mining node 1.
     */
    OBSIDIAN_1(Scenery.OBSIDIAN_WALL_31229, Scenery.OBSIDIAN_WALL_31230, 19.toByte()),

    /**
     * Obsidian mining node 2.
     */
    OBSIDIAN_2(Scenery.OBSIDIAN_WALL_31230, Scenery.OBSIDIAN_WALL_9376, 19.toByte());

    /**
     * Respawn rate mining node.
     */
    var respawnRate: Int = 0

    /**
     * Reward mining node.
     */
    var reward: Int = 0

    /**
     * Level mining node.
     */
    var level: Int = 0

    /**
     * The Rate.
     */
    var rate: Double = 0.0

    /**
     * Experience mining node.
     */
    var experience: Double = 0.0

    init {
        when (identifier.toInt() and 0xFF) {
            1, 2 -> {
                respawnRate = 4 or (8 shl 16)
                experience = 17.5
                rate = 0.05
                reward = if (identifier.toInt() == 1) 436 else 438
                level = 1
            }

            3 -> {
                respawnRate = 1 or (1 shl 16)
                experience = 5.0
                rate = 0.1
                reward = 434
                level = 1
            }

            4 -> {
                respawnRate = 10 or (20 shl 16)
                experience = 26.5
                rate = 0.2
                reward = 3211
                level = 10
            }

            5 -> {
                respawnRate = 10 or (20 shl 16)
                experience = 17.5
                rate = 0.2
                reward = 668
                level = 10
            }

            6 -> {
                respawnRate = 15 or (25 shl 16)
                experience = 35.0
                rate = 0.2
                reward = 440
                level = 15
            }

            7 -> {
                respawnRate = 100 or (200 shl 16)
                experience = 40.0
                rate = 0.3
                reward = 442
                level = 20
            }

            8 -> {
                respawnRate = 50 or (100 shl 16)
                experience = 50.0
                rate = 0.4
                reward = 453
                level = 30
            }

            9 -> {
                respawnRate = 100 or (200 shl 16)
                experience = 65.0
                rate = 0.6
                reward = 444
                level = 40
            }

            10 -> {
                respawnRate = 200 or (400 shl 16)
                experience = 80.0
                rate = 0.70
                reward = 447
                level = 55
            }

            11 -> {
                respawnRate = 400 or (800 shl 16)
                experience = 95.0
                rate = 0.85
                reward = 449
                level = 70
            }

            12 -> {
                respawnRate = 1250 or (2500 shl 16)
                experience = 125.0
                rate = 0.95
                reward = 451
                level = 85
            }

            13 -> {
                respawnRate = 166 or (175 shl 16)
                experience = 65.0
                rate = 0.95
                reward = 1625
                level = 40
            }

            14 -> {
                respawnRate = 1 or (1 shl 16)
                experience = 5.0
                rate = 0.1
                reward = 1436
                level = 1
            }

            15 -> {
                respawnRate = 30 or (60 shl 16)
                experience = 30.0
                rate = 0.2
                reward = 6971
                level = 35
            }

            16 -> {
                respawnRate = 10 or (20 shl 16)
                experience = 50.0
                rate = 0.2
                reward = 6979
                level = 45
            }

            17 -> {
                respawnRate = 50 or (100 shl 16)
                experience = 17.5
                rate = 0.6
                reward = 12630
                level = 46
            }

            18 -> {
                respawnRate = 100 or (200 shl 16)
                experience = 0.0
                rate = 0.3
                reward = 4703
                level = 20
            }

            19 -> {
                respawnRate = 100 or (200 shl 16)
                experience = 40.0
                rate = 0.74
                reward = 13245
                level = 41
            }
        }
    }

    val rewardAmount: Int
        /**
         * Gets reward amount.
         *
         * @return the reward amount
         */
        get() = 1

    val minimumRespawn: Int
        /**
         * Gets minimum respawn.
         *
         * @return the minimum respawn
         */
        get() = respawnRate and 0xFFFF

    val maximumRespawn: Int
        /**
         * Gets maximum respawn.
         *
         * @return the maximum respawn
         */
        get() = (respawnRate shr 16) and 0xFFFF

    val respawnDuration: Int
        /**
         * Gets respawn duration.
         *
         * @return the respawn duration
         */
        get() {
            val minimum = respawnRate and 0xFFFF
            val maximum = (respawnRate shr 16) and 0xFFFF
            val playerRatio =
                ServerConstants.MAX_PLAYERS.toDouble() / players.size
            return (minimum + ((maximum - minimum) / playerRatio)).toInt()
        }

    companion object {
        /**
         * The gem rock reward.
         */
        var GEM_ROCK_REWARD: MutableList<WeightedChanceItem> = ArrayList(20)

        init {
            GEM_ROCK_REWARD.add(WeightedChanceItem(Items.UNCUT_OPAL_1625, 1, 60))
            GEM_ROCK_REWARD.add(WeightedChanceItem(Items.UNCUT_JADE_1627, 1, 30))
            GEM_ROCK_REWARD.add(WeightedChanceItem(Items.UNCUT_RED_TOPAZ_1629, 1, 15))
            GEM_ROCK_REWARD.add(WeightedChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, 9))
            GEM_ROCK_REWARD.add(WeightedChanceItem(Items.UNCUT_EMERALD_1621, 1, 5))
            GEM_ROCK_REWARD.add(WeightedChanceItem(Items.UNCUT_RUBY_1619, 1, 5))
            GEM_ROCK_REWARD.add(WeightedChanceItem(Items.UNCUT_DIAMOND_1617, 1, 4))
        }

        private val NODE_MAP = HashMap<Int, MiningNode>()
        private val EMPTY_MAP = HashMap<Int, Int?>()

        init {
            for (node in values()) {
                NODE_MAP.putIfAbsent(node.id, node)
            }
            for (node in values()) {
                EMPTY_MAP.putIfAbsent(node.emptyId, node.id)
            }
        }

        /**
         * For id mining node.
         *
         * @param id the id
         * @return the mining node
         */
        fun forId(id: Int): MiningNode? {
            return NODE_MAP[id]
        }

        /**
         * Is empty boolean.
         *
         * @param id the id
         * @return the boolean
         */
        fun isEmpty(id: Int): Boolean {
            return EMPTY_MAP[id] != null
        }
    }
}
