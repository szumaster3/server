package content.global.skill.gathering.mining;

import core.ServerConstants;
import core.game.node.item.WeightedChanceItem;
import core.game.world.repository.Repository;
import org.rs.consts.Items;
import org.rs.consts.Scenery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The enum Mining node.
 */
public enum MiningNode {
    /**
     * Copper ore 0 mining node.
     */
    COPPER_ORE_0(Scenery.ROCKS_2090, Scenery.ROCKS_450, (byte) 1),
    /**
     * Copper ore 1 mining node.
     */
    COPPER_ORE_1(Scenery.ROCKS_2091,  Scenery.ROCKS_452, (byte) 1),
    /**
     * Copper ore 2 mining node.
     */
    COPPER_ORE_2(Scenery.MINERAL_VEIN_4976,  Scenery.MINERAL_VEIN_4994, (byte) 1),
    /**
     * Copper ore 3 mining node.
     */
    COPPER_ORE_3(Scenery.MINERAL_VEIN_4977,  Scenery.MINERAL_VEIN_4995, (byte) 1),
    /**
     * Copper ore 4 mining node.
     */
    COPPER_ORE_4(Scenery.MINERAL_VEIN_4978,  Scenery.MINERAL_VEIN_4996, (byte) 1),
    /**
     * Copper ore 5 mining node.
     */
    COPPER_ORE_5(Scenery.ROCKS_9710,  Scenery.ROCKS_18954, (byte) 1),
    /**
     * Copper ore 6 mining node.
     */
    COPPER_ORE_6(Scenery.ROCKS_9709,  Scenery.ROCKS_32448, (byte) 1),
    /**
     * Copper ore 7 mining node.
     */
    COPPER_ORE_7(Scenery.ROCKS_9708,  Scenery.ROCKS_32447, (byte) 1),
    /**
     * Copper ore 8 mining node.
     */
    COPPER_ORE_8(Scenery.ROCKS_11960,  Scenery.ROCKS_11555, (byte) 1),
    /**
     * Copper ore 9 mining node.
     */
    COPPER_ORE_9(Scenery.ROCKS_11961,  Scenery.ROCKS_11556, (byte) 1),
    /**
     * Copper ore 10 mining node.
     */
    COPPER_ORE_10(Scenery.ROCKS_11962,  Scenery.ROCKS_11557, (byte) 1),
    /**
     * Copper ore 11 mining node.
     */
    COPPER_ORE_11(Scenery.ROCKS_11937,  Scenery.ROCKS_11553, (byte) 1),
    /**
     * Copper ore 12 mining node.
     */
    COPPER_ORE_12(Scenery.ROCKS_11936,  Scenery.ROCKS_11552, (byte) 1),
    /**
     * Copper ore 13 mining node.
     */
    COPPER_ORE_13(Scenery.ROCKS_11938,  Scenery.ROCKS_11554, (byte) 1),
    /**
     * Copper ore 14 mining node.
     */
    COPPER_ORE_14(Scenery.RUBBLE_12746,  Scenery.ROCKS_450, (byte) 1),
    /**
     * Copper ore 15 mining node.
     */
    COPPER_ORE_15(Scenery.ROCKS_14906,  Scenery.ROCKS_14894, (byte) 1),
    /**
     * Copper ore 16 mining node.
     */
    COPPER_ORE_16(Scenery.ROCKS_14907,  Scenery.ROCKS_14895, (byte) 1),
    /**
     * Copper ore 17 mining node.
     */
    COPPER_ORE_17(Scenery.ORE_VEIN_20448,  Scenery.ORE_VEIN_20445, (byte) 1),
    /**
     * Copper ore 18 mining node.
     */
    COPPER_ORE_18(Scenery.ORE_VEIN_20451,  Scenery.ORE_VEIN_20445, (byte) 1),
    /**
     * Copper ore 19 mining node.
     */
    COPPER_ORE_19(Scenery.ORE_VEIN_20446,  Scenery.ORE_VEIN_20443, (byte) 1),
    /**
     * Copper ore 20 mining node.
     */
    COPPER_ORE_20(Scenery.ORE_VEIN_20447,  Scenery.ORE_VEIN_20444, (byte) 1),
    /**
     * Copper ore 21 mining node.
     */
    COPPER_ORE_21(Scenery.ORE_VEIN_20408,  Scenery.ORE_VEIN_20407, (byte) 1),
    /**
     * Copper ore 22 mining node.
     */
    COPPER_ORE_22(Scenery.ROCKS_18993,  Scenery.ROCKS_19005, (byte) 1),
    /**
     * Copper ore 23 mining node.
     */
    COPPER_ORE_23(Scenery.ROCKS_18992,  Scenery.ROCKS_19004, (byte) 1),
    /**
     * Copper ore 24 mining node.
     */
    COPPER_ORE_24(Scenery.ROCKS_19007,  Scenery.ROCKS_19016, (byte) 1),
    /**
     * Copper ore 25 mining node.
     */
    COPPER_ORE_25(Scenery.ROCKS_19006,  Scenery.ROCKS_19021, (byte) 1),
    /**
     * Copper ore 26 mining node.
     */
    COPPER_ORE_26(Scenery.ROCKS_18991,  Scenery.ROCKS_19003, (byte) 1),
    /**
     * Copper ore 27 mining node.
     */
    COPPER_ORE_27(Scenery.ROCKS_19008,  Scenery.ROCKS_19017, (byte) 1),
    /**
     * Copper ore 28 mining node.
     */
    COPPER_ORE_28(Scenery.ROCKS_21285,  Scenery.ROCKS_21297, (byte) 1),
    /**
     * Copper ore 29 mining node.
     */
    COPPER_ORE_29(Scenery.ROCKS_21284,  Scenery.ROCKS_21296, (byte) 1),
    /**
     * Copper ore 30 mining node.
     */
    COPPER_ORE_30(Scenery.ROCKS_21286,  Scenery.ROCKS_21298, (byte) 1),
    /**
     * Copper ore 31 mining node.
     */
    COPPER_ORE_31(Scenery.ROCKS_29231,  Scenery.ROCKS_29219, (byte) 1),
    /**
     * Copper ore 32 mining node.
     */
    COPPER_ORE_32(Scenery.ROCKS_29230,  Scenery.ROCKS_29218, (byte) 1),
    /**
     * Copper ore 33 mining node.
     */
    COPPER_ORE_33(Scenery.ROCKS_29232,  Scenery.ROCKS_29220, (byte) 1),
    /**
     * Copper ore 34 mining node.
     */
    COPPER_ORE_34(Scenery.ROCKS_31082,  Scenery.ROCKS_37650, (byte) 1),
    /**
     * Copper ore 35 mining node.
     */
    COPPER_ORE_35(Scenery.ROCKS_31081,  Scenery.ROCKS_37649, (byte) 1),
    /**
     * Copper ore 36 mining node.
     */
    COPPER_ORE_36(Scenery.ROCKS_31080,  Scenery.ROCKS_37639, (byte) 1),
    /**
     * Copper ore 37 mining node.
     */
    COPPER_ORE_37(Scenery.ROCKS_37647,  Scenery.ROCKS_37650, (byte) 1),
    /**
     * Copper ore 38 mining node.
     */
    COPPER_ORE_38(Scenery.ROCKS_37646,  Scenery.ROCKS_37649, (byte) 1),
    /**
     * Copper ore 39 mining node.
     */
    COPPER_ORE_39(Scenery.ROCKS_37645,  Scenery.ROCKS_37639, (byte) 1),
    /**
     * Copper ore 40 mining node.
     */
    COPPER_ORE_40(Scenery.ROCKS_37637,  Scenery.ROCKS_37639, (byte) 1),
    /**
     * Copper ore 41 mining node.
     */
    COPPER_ORE_41(Scenery.ROCKS_37688,  Scenery.ROCKS_21298, (byte) 1),
    /**
     * Copper ore 42 mining node.
     */
    COPPER_ORE_42(Scenery.ROCKS_37686,  Scenery.ROCKS_21296, (byte) 1),
    /**
     * Copper ore 43 mining node.
     */
    COPPER_ORE_43(Scenery.ROCKS_37687,  Scenery.ROCKS_21297, (byte) 1),
    /**
     * Copper ore 44 mining node.
     */
    COPPER_ORE_44(Scenery.ROCKS_3042,  Scenery.ROCKS_11552, (byte) 1),

    /**
     * Tin ore 0 mining node.
     */
    TIN_ORE_0( Scenery.ROCKS_2094, Scenery.ROCKS_450, (byte) 2),
    /**
     * Tin ore 1 mining node.
     */
    TIN_ORE_1( Scenery.ROCKS_2095, Scenery.ROCKS_452, (byte) 2),
    /**
     * Tin ore 2 mining node.
     */
    TIN_ORE_2( Scenery.ROCKS_3043, Scenery.ROCKS_11552, (byte) 2),
    /**
     * Tin ore 3 mining node.
     */
    TIN_ORE_3( Scenery.MINERAL_VEIN_4979, Scenery.MINERAL_VEIN_4994, (byte) 2),
    /**
     * Tin ore 4 mining node.
     */
    TIN_ORE_4( Scenery.MINERAL_VEIN_4980, Scenery.MINERAL_VEIN_4995, (byte) 2),
    /**
     * Tin ore 5 mining node.
     */
    TIN_ORE_5( Scenery.MINERAL_VEIN_4981, Scenery.MINERAL_VEIN_4996, (byte) 2),
    /**
     * Tin ore 6 mining node.
     */
    TIN_ORE_6( Scenery.ROCKS_11957, Scenery.ROCKS_11555, (byte) 2),
    /**
     * Tin ore 7 mining node.
     */
    TIN_ORE_7( Scenery.ROCKS_11958, Scenery.ROCKS_11556, (byte) 2),
    /**
     * Tin ore 8 mining node.
     */
    TIN_ORE_8( Scenery.ROCKS_11959, Scenery.ROCKS_11557, (byte) 2),
    /**
     * Tin ore 9 mining node.
     */
    TIN_ORE_9( Scenery.ROCKS_11934, Scenery.ROCKS_11553, (byte) 2),
    /**
     * Tin ore 10 mining node.
     */
    TIN_ORE_10(Scenery.ROCKS_11935, Scenery.ROCKS_11554, (byte) 2),
    /**
     * Tin ore 11 mining node.
     */
    TIN_ORE_11(Scenery.ROCKS_11933, Scenery.ROCKS_11552, (byte) 2),
    /**
     * Tin ore 12 mining node.
     */
    TIN_ORE_12(Scenery.ROCKS_14902, Scenery.ROCKS_14894, (byte) 2),
    /**
     * Tin ore 13 mining node.
     */
    TIN_ORE_13(Scenery.ROCKS_14903, Scenery.ROCKS_14895, (byte) 2),
    /**
     * Tin ore 14 mining node.
     */
    TIN_ORE_14(Scenery.ROCKS_18995, Scenery.ROCKS_19004, (byte) 2),
    /**
     * Tin ore 15 mining node.
     */
    TIN_ORE_15(Scenery.ROCKS_18994, Scenery.ROCKS_19003, (byte) 2),
    /**
     * Tin ore 16 mining node.
     */
    TIN_ORE_16(Scenery.ROCKS_18996, Scenery.ROCKS_19005, (byte) 2),
    /**
     * Tin ore 17 mining node.
     */
    TIN_ORE_17(Scenery.ROCKS_19025, Scenery.ROCKS_19016, (byte) 2),
    /**
     * Tin ore 18 mining node.
     */
    TIN_ORE_18(Scenery.ROCKS_19024, Scenery.ROCKS_19021, (byte) 2),
    /**
     * Tin ore 19 mining node.
     */
    TIN_ORE_19(Scenery.ROCKS_19026, Scenery.ROCKS_19017, (byte) 2),
    /**
     * Tin ore 20 mining node.
     */
    TIN_ORE_20(Scenery.ROCKS_21293, Scenery.ROCKS_21296, (byte) 2),
    /**
     * Tin ore 21 mining node.
     */
    TIN_ORE_21(Scenery.ROCKS_21295, Scenery.ROCKS_21298, (byte) 2),
    /**
     * Tin ore 22 mining node.
     */
    TIN_ORE_22(Scenery.ROCKS_21294, Scenery.ROCKS_21297, (byte) 2),
    /**
     * Tin ore 23 mining node.
     */
    TIN_ORE_23(Scenery.ROCKS_29227, Scenery.ROCKS_29218, (byte) 2),
    /**
     * Tin ore 24 mining node.
     */
    TIN_ORE_24(Scenery.ROCKS_29229, Scenery.ROCKS_29220, (byte) 2),
    /**
     * Tin ore 25 mining node.
     */
    TIN_ORE_25(Scenery.ROCKS_29228, Scenery.ROCKS_29219, (byte) 2),
    /**
     * Tin ore 26 mining node.
     */
    TIN_ORE_26(Scenery.ROCKS_31079, Scenery.ROCKS_37650, (byte) 2),
    /**
     * Tin ore 27 mining node.
     */
    TIN_ORE_27(Scenery.ROCKS_31078, Scenery.ROCKS_37649, (byte) 2),
    /**
     * Tin ore 28 mining node.
     */
    TIN_ORE_28(Scenery.ROCKS_31077, Scenery.ROCKS_37639, (byte) 2),
    /**
     * Tin ore 29 mining node.
     */
    TIN_ORE_29(Scenery.ROCKS_37644, Scenery.ROCKS_37650, (byte) 2),
    /**
     * Tin ore 30 mining node.
     */
    TIN_ORE_30(Scenery.ROCKS_37643, Scenery.ROCKS_37649, (byte) 2),
    /**
     * Tin ore 31 mining node.
     */
    TIN_ORE_31(Scenery.ROCKS_37642, Scenery.ROCKS_37639, (byte) 2),
    /**
     * Tin ore 32 mining node.
     */
    TIN_ORE_32(Scenery.ROCKS_37638, Scenery.ROCKS_37639, (byte) 2),
    /**
     * Tin ore 33 mining node.
     */
    TIN_ORE_33(Scenery.ROCKS_37685, Scenery.ROCKS_21298, (byte) 2),

    /**
     * Clay 0 mining node.
     */
    CLAY_0(Scenery.ROCKS_2109, Scenery.ROCKS_452, (byte) 3),
    /**
     * Clay 1 mining node.
     */
    CLAY_1(Scenery.ROCKS_2108, Scenery.ROCKS_450, (byte) 3),
    /**
     * Clay 2 mining node.
     */
    CLAY_2(Scenery.ROCKS_9712, Scenery.ROCKS_32448, (byte) 3),
    /**
     * Clay 3 mining node.
     */
    CLAY_3(Scenery.ROCKS_9713, Scenery.ROCKS_18954, (byte) 3),
    /**
     * Clay 4 mining node.
     */
    CLAY_4(Scenery.ROCKS_9711, Scenery.ROCKS_32447, (byte) 3),
    /**
     * Clay 5 mining node.
     */
    CLAY_5(Scenery.ROCKS_10949, Scenery.ROCKS_10945, (byte) 3),
    /**
     * Clay 6 mining node.
     */
    CLAY_6(Scenery.ROCKS_11190, Scenery.ROCKS_21297, (byte) 3),
    /**
     * Clay 7 mining node.
     */
    CLAY_7(Scenery.ROCKS_11191, Scenery.ROCKS_21298, (byte) 3),
    /**
     * Clay 8 mining node.
     */
    CLAY_8(Scenery.ROCKS_11189, Scenery.ROCKS_21296, (byte) 3),
    /**
     * Clay 9 mining node.
     */
    CLAY_9(Scenery.MINERAL_VEIN_12942, Scenery.MINERAL_VEIN_4995, (byte) 3),
    /**
     * Clay 10 mining node.
     */
    CLAY_10(Scenery.MINERAL_VEIN_12943, Scenery.MINERAL_VEIN_4996, (byte) 3),
    /**
     * Clay 11 mining node.
     */
    CLAY_11(Scenery.MINERAL_VEIN_12941, Scenery.MINERAL_VEIN_4994, (byte) 3),
    /**
     * Clay 12 mining node.
     */
    CLAY_12(Scenery.ROCKS_14904, Scenery.ROCKS_14894, (byte) 3),
    /**
     * Clay 13 mining node.
     */
    CLAY_13(Scenery.ROCKS_14905, Scenery.ROCKS_14895, (byte) 3),
    /**
     * Clay 14 mining node.
     */
    CLAY_14(Scenery.ROCKS_15505, Scenery.ROCKS_11557, (byte) 3),
    /**
     * Clay 15 mining node.
     */
    CLAY_15(Scenery.ROCKS_15504, Scenery.ROCKS_11556, (byte) 3),
    /**
     * Clay 16 mining node.
     */
    CLAY_16(Scenery.ROCKS_15503, Scenery.ROCKS_11555, (byte) 3),
    /**
     * Clay 17 mining node.
     */
    CLAY_17(Scenery.ORE_VEIN_20449, Scenery.ORE_VEIN_20443, (byte) 3),
    /**
     * Clay 18 mining node.
     */
    CLAY_18(Scenery.ORE_VEIN_20450, Scenery.ORE_VEIN_20444, (byte) 3),
    /**
     * Clay 19 mining node.
     */
    CLAY_19(Scenery.ORE_VEIN_20409, Scenery.ORE_VEIN_20407, (byte) 3),
    /**
     * Clay 20 mining node.
     */
    CLAY_20(Scenery.ROCKS_32429, Scenery.ROCKS_33400, (byte) 3),
    /**
     * Clay 21 mining node.
     */
    CLAY_21(Scenery.ROCKS_32430, Scenery.ROCKS_33401, (byte) 3),
    /**
     * Clay 22 mining node.
     */
    CLAY_22(Scenery.ROCKS_32431, Scenery.ROCKS_33402, (byte) 3),
    /**
     * Clay 23 mining node.
     */
    CLAY_23(Scenery.ROCKS_31062, Scenery.ROCKS_37639, (byte) 3),
    /**
     * Clay 24 mining node.
     */
    CLAY_24(Scenery.ROCKS_31063, Scenery.ROCKS_37649, (byte) 3),
    /**
     * Clay 25 mining node.
     */
    CLAY_25(Scenery.ROCKS_31064, Scenery.ROCKS_37650, (byte) 3),

    /**
     * Limestone 0 mining node.
     */
    LIMESTONE_0(Scenery.PILE_OF_ROCK_4027, Scenery.PILE_OF_ROCK_4028, (byte) 4),
    /**
     * Limestone 1 mining node.
     */
    LIMESTONE_1(Scenery.PILE_OF_ROCK_4028, Scenery.PILE_OF_ROCK_4029, (byte) 4),
    /**
     * Limestone 2 mining node.
     */
    LIMESTONE_2(Scenery.PILE_OF_ROCK_4029, Scenery.PILE_OF_ROCK_4030, (byte) 4),
    /**
     * Limestone 3 mining node.
     */
    LIMESTONE_3(Scenery.PILE_OF_ROCK_4030, Scenery.PILE_OF_ROCK_4027, (byte) 4),

    /**
     * Blurite ore 0 mining node.
     */
    BLURITE_ORE_0(Scenery.ROCKS_33220, Scenery.ROCKS_33222, (byte) 5),
    /**
     * Blurite ore 1 mining node.
     */
    BLURITE_ORE_1(Scenery.ROCKS_33221, Scenery.ROCKS_33223, (byte) 5),

    /**
     * Iron ore 0 mining node.
     */
    IRON_ORE_0(Scenery.ROCKS_2092, Scenery.ROCKS_450, (byte) 6),
    /**
     * Iron ore 1 mining node.
     */
    IRON_ORE_1(Scenery.ROCKS_2093, Scenery.ROCKS_452, (byte) 6),
    /**
     * Iron ore 2 mining node.
     */
    IRON_ORE_2(Scenery.MINERAL_VEIN_4982, Scenery.MINERAL_VEIN_4994, (byte) 6),
    /**
     * Iron ore 3 mining node.
     */
    IRON_ORE_3(Scenery.MINERAL_VEIN_4983, Scenery.MINERAL_VEIN_4995, (byte) 6),
    /**
     * Iron ore 4 mining node.
     */
    IRON_ORE_4(Scenery.MINERAL_VEIN_4984, Scenery.MINERAL_VEIN_4996, (byte) 6),
    /**
     * Iron ore 5 mining node.
     */
    IRON_ORE_5(Scenery.ROCKS_6943, Scenery.ROCKS_21296, (byte) 6),
    /**
     * Iron ore 6 mining node.
     */
    IRON_ORE_6(Scenery.ROCKS_6944, Scenery.ROCKS_21297, (byte) 6),
    /**
     * Iron ore 7 mining node.
     */
    IRON_ORE_7(Scenery.ROCKS_9718, Scenery.ROCKS_32448, (byte) 6),
    /**
     * Iron ore 8 mining node.
     */
    IRON_ORE_8(Scenery.ROCKS_9719, Scenery.ROCKS_18954, (byte) 6),
    /**
     * Iron ore 9 mining node.
     */
    IRON_ORE_9(Scenery.ROCKS_9717, Scenery.ROCKS_32447, (byte) 6),
    /**
     * Iron ore 10 mining node.
     */
    IRON_ORE_10(Scenery.ROCKS_11956, Scenery.ROCKS_11557, (byte) 6),
    /**
     * Iron ore 11 mining node.
     */
    IRON_ORE_11(Scenery.ROCKS_11954, Scenery.ROCKS_11555, (byte) 6),
    /**
     * Iron ore 12 mining node.
     */
    IRON_ORE_12(Scenery.ROCKS_11955, Scenery.ROCKS_11556, (byte) 6),
    /**
     * Iron ore 13 mining node.
     */
    IRON_ORE_13(Scenery.ROCKS_14914, Scenery.ROCKS_14895, (byte) 6),
    /**
     * Iron ore 14 mining node.
     */
    IRON_ORE_14(Scenery.ROCKS_14913, Scenery.ROCKS_14894, (byte) 6),
    /**
     * Iron ore 15 mining node.
     */
    IRON_ORE_15(Scenery.ROCKS_14858, Scenery.ROCKS_25373, (byte) 6),
    /**
     * Iron ore 16 mining node.
     */
    IRON_ORE_16(Scenery.ROCKS_14857, Scenery.ROCKS_25372, (byte) 6),
    /**
     * Iron ore 17 mining node.
     */
    IRON_ORE_17(Scenery.ROCKS_14856, Scenery.ROCKS_25371, (byte) 6),
    /**
     * Iron ore 18 mining node.
     */
    IRON_ORE_18(Scenery.ROCKS_14900, Scenery.ROCKS_14894, (byte) 6),
    /**
     * Iron ore 19 mining node.
     */
    IRON_ORE_19(Scenery.ROCKS_14901, Scenery.ROCKS_14895, (byte) 6),
    /**
     * Iron ore 20 mining node.
     */
    IRON_ORE_20(Scenery.ORE_VEIN_20423, Scenery.ORE_VEIN_20444, (byte) 6),
    /**
     * Iron ore 21 mining node.
     */
    IRON_ORE_21(Scenery.ORE_VEIN_20422, Scenery.ORE_VEIN_20443, (byte) 6),
    /**
     * Iron ore 22 mining node.
     */
    IRON_ORE_22(Scenery.ORE_VEIN_20425, Scenery.ORE_VEIN_20407, (byte) 6),
    /**
     * Iron ore 23 mining node.
     */
    IRON_ORE_23(Scenery.ORE_VEIN_20424, Scenery.ORE_VEIN_20445, (byte) 6),
    /**
     * Iron ore 24 mining node.
     */
    IRON_ORE_24(Scenery.ROCKS_19002, Scenery.ROCKS_19005, (byte) 6),
    /**
     * Iron ore 25 mining node.
     */
    IRON_ORE_25(Scenery.ROCKS_19001, Scenery.ROCKS_19004, (byte) 6),
    /**
     * Iron ore 26 mining node.
     */
    IRON_ORE_26(Scenery.ROCKS_19000, Scenery.ROCKS_19003, (byte) 6),
    /**
     * Iron ore 27 mining node.
     */
    IRON_ORE_27(Scenery.ROCKS_21281, Scenery.ROCKS_21296, (byte) 6),
    /**
     * Iron ore 28 mining node.
     */
    IRON_ORE_28(Scenery.ROCKS_21283, Scenery.ROCKS_21298, (byte) 6),
    /**
     * Iron ore 29 mining node.
     */
    IRON_ORE_29(Scenery.ROCKS_21282, Scenery.ROCKS_21297, (byte) 6),
    /**
     * Iron ore 30 mining node.
     */
    IRON_ORE_30(Scenery.ROCKS_29221, Scenery.ROCKS_29218, (byte) 6),
    /**
     * Iron ore 31 mining node.
     */
    IRON_ORE_31(Scenery.ROCKS_29223, Scenery.ROCKS_29220, (byte) 6),
    /**
     * Iron ore 32 mining node.
     */
    IRON_ORE_32(Scenery.ROCKS_29222, Scenery.ROCKS_29219, (byte) 6),
    /**
     * Iron ore 33 mining node.
     */
    IRON_ORE_33(Scenery.ROCKS_32441, Scenery.ROCKS_33400, (byte) 6),
    /**
     * Iron ore 34 mining node.
     */
    IRON_ORE_34(Scenery.ROCKS_32443, Scenery.ROCKS_33402, (byte) 6),
    /**
     * Iron ore 35 mining node.
     */
    IRON_ORE_35(Scenery.ROCKS_32442, Scenery.ROCKS_33401, (byte) 6),
    /**
     * Iron ore 36 mining node.
     */
    IRON_ORE_36(Scenery.ROCKS_32452, Scenery.ROCKS_32448, (byte) 6),
    /**
     * Iron ore 37 mining node.
     */
    IRON_ORE_37(Scenery.ROCKS_32451, Scenery.ROCKS_32447, (byte) 6),
    /**
     * Iron ore 38 mining node.
     */
    IRON_ORE_38(Scenery.ROCKS_31073, Scenery.ROCKS_37650, (byte) 6),
    /**
     * Iron ore 39 mining node.
     */
    IRON_ORE_39(Scenery.ROCKS_31072, Scenery.ROCKS_37649, (byte) 6),
    /**
     * Iron ore 40 mining node.
     */
    IRON_ORE_40(Scenery.ROCKS_31071, Scenery.ROCKS_37639, (byte) 6),
    /**
     * Iron ore 41 mining node.
     */
    IRON_ORE_41(Scenery.ROCKS_37307, Scenery.ROCKS_11552, (byte) 6),
    /**
     * Iron ore 42 mining node.
     */
    IRON_ORE_42(Scenery.ROCKS_37309, Scenery.ROCKS_11554, (byte) 6),
    /**
     * Iron ore 43 mining node.
     */
    IRON_ORE_43(Scenery.ROCKS_37308, Scenery.ROCKS_11553, (byte) 6),

    /**
     * Silver ore 0 mining node.
     */
    SILVER_ORE_0(Scenery.ROCKS_2101, Scenery.ROCKS_452, (byte) 7),
    /**
     * Silver ore 1 mining node.
     */
    SILVER_ORE_1(Scenery.ROCKS_2100, Scenery.ROCKS_450, (byte) 7),
    /**
     * Silver ore 2 mining node.
     */
    SILVER_ORE_2(Scenery.ROCKS_6945, Scenery.ROCKS_21296, (byte) 7),
    /**
     * Silver ore 3 mining node.
     */
    SILVER_ORE_3(Scenery.ROCKS_6946, Scenery.ROCKS_21297, (byte) 7),
    /**
     * Silver ore 4 mining node.
     */
    SILVER_ORE_4(Scenery.ROCKS_9716, Scenery.ROCKS_18954, (byte) 7),
    /**
     * Silver ore 5 mining node.
     */
    SILVER_ORE_5(Scenery.ROCKS_9714, Scenery.ROCKS_32447, (byte) 7),
    /**
     * Silver ore 6 mining node.
     */
    SILVER_ORE_6(Scenery.ROCKS_9715, Scenery.ROCKS_32448, (byte) 7),
    /**
     * Silver ore 7 mining node.
     */
    SILVER_ORE_7(Scenery.ROCKS_11188,Scenery.ROCKS_21298, (byte) 7),
    /**
     * Silver ore 8 mining node.
     */
    SILVER_ORE_8(Scenery.ROCKS_11186,Scenery.ROCKS_21296, (byte) 7),
    /**
     * Silver ore 9 mining node.
     */
    SILVER_ORE_9(Scenery.ROCKS_11187,Scenery.ROCKS_21297, (byte) 7),
    /**
     * Silver ore 10 mining node.
     */
    SILVER_ORE_10(Scenery.ROCKS_15581, Scenery.ROCKS_14834, (byte) 7),
    /**
     * Silver ore 11 mining node.
     */
    SILVER_ORE_11(Scenery.ROCKS_15580, Scenery.ROCKS_14833, (byte) 7),
    /**
     * Silver ore 12 mining node.
     */
    SILVER_ORE_12(Scenery.ROCKS_15579, Scenery.ROCKS_14832, (byte) 7),
    /**
     * Silver ore 13 mining node.
     */
    SILVER_ORE_13(Scenery.ROCKS_16998, Scenery.ROCKS_14915, (byte) 7),
    /**
     * Silver ore 14 mining node.
     */
    SILVER_ORE_14(Scenery.ROCKS_16999, Scenery.ROCKS_14916, (byte) 7),
    /**
     * Silver ore 15 mining node.
     */
    SILVER_ORE_15(Scenery.ROCKS_17007, Scenery.ROCKS_14915, (byte) 7),
    /**
     * Silver ore 16 mining node.
     */
    SILVER_ORE_16(Scenery.ROCKS_17000, Scenery.ROCKS_31061, (byte) 7),
    /**
     * Silver ore 17 mining node.
     */
    SILVER_ORE_17(Scenery.ROCKS_17009, Scenery.ROCKS_31061, (byte) 7),
    /**
     * Silver ore 18 mining node.
     */
    SILVER_ORE_18(Scenery.ROCKS_17008, Scenery.ROCKS_14916, (byte) 7),
    /**
     * Silver ore 19 mining node.
     */
    SILVER_ORE_19(Scenery.ROCKS_17385, Scenery.ROCKS_32447, (byte) 7),
    /**
     * Silver ore 20 mining node.
     */
    SILVER_ORE_20(Scenery.ROCKS_17387, Scenery.ROCKS_18954, (byte) 7),
    /**
     * Silver ore 21 mining node.
     */
    SILVER_ORE_21(Scenery.ROCKS_17386, Scenery.ROCKS_32448, (byte) 7),
    /**
     * Silver ore 22 mining node.
     */
    SILVER_ORE_22(Scenery.ROCKS_29225, Scenery.ROCKS_29219, (byte) 7),
    /**
     * Silver ore 23 mining node.
     */
    SILVER_ORE_23(Scenery.ROCKS_29224, Scenery.ROCKS_29218, (byte) 7),
    /**
     * Silver ore 24 mining node.
     */
    SILVER_ORE_24(Scenery.ROCKS_29226, Scenery.ROCKS_29220, (byte) 7),
    /**
     * Silver ore 25 mining node.
     */
    SILVER_ORE_25(Scenery.ROCKS_32445, Scenery.ROCKS_33401, (byte) 7),
    /**
     * Silver ore 26 mining node.
     */
    SILVER_ORE_26(Scenery.ROCKS_32444, Scenery.ROCKS_33400, (byte) 7),
    /**
     * Silver ore 27 mining node.
     */
    SILVER_ORE_27(Scenery.ROCKS_32446, Scenery.ROCKS_33402, (byte) 7),
    /**
     * Silver ore 28 mining node.
     */
    SILVER_ORE_28(Scenery.ROCKS_31075, Scenery.ROCKS_37649, (byte) 7),
    /**
     * Silver ore 29 mining node.
     */
    SILVER_ORE_29(Scenery.ROCKS_31074, Scenery.ROCKS_37639, (byte) 7),
    /**
     * Silver ore 30 mining node.
     */
    SILVER_ORE_30(Scenery.ROCKS_31076, Scenery.ROCKS_37650, (byte) 7),
    /**
     * Silver ore 31 mining node.
     */
    SILVER_ORE_31(Scenery.ROCKS_37305, Scenery.ROCKS_11553, (byte) 7),
    /**
     * Silver ore 32 mining node.
     */
    SILVER_ORE_32(Scenery.ROCKS_37304, Scenery.ROCKS_11552, (byte) 7),
    /**
     * Silver ore 33 mining node.
     */
    SILVER_ORE_33(Scenery.ROCKS_37306, Scenery.ROCKS_11554, (byte) 7),
    /**
     * Silver ore 34 mining node.
     */
    SILVER_ORE_34(Scenery.ROCKS_37670, Scenery.ROCKS_11552, (byte) 7),
    /**
     * Silver ore 35 mining node.
     */
    SILVER_ORE_35(Scenery.ROCKS_11948, Scenery.ROCKS_11555, (byte) 7),
    /**
     * Silver ore 36 mining node.
     */
    SILVER_ORE_36(Scenery.ROCKS_11949, Scenery.ROCKS_11556, (byte) 7),
    /**
     * Silver ore 37 mining node.
     */
    SILVER_ORE_37(Scenery.ROCKS_11950, Scenery.ROCKS_11557, (byte) 7),
    /**
     * Silver ore 38 mining node.
     */
    SILVER_ORE_38(2311, Scenery.ROCKS_11552, (byte) 7),

    /**
     * Coal 0 mining node.
     */
    COAL_0(Scenery.ROCKS_2097, 452, (byte) 8),
    /**
     * Coal 1 mining node.
     */
    COAL_1(Scenery.ROCKS_2096, 450, (byte) 8),
    /**
     * Coal 2 mining node.
     */
    COAL_2(Scenery.MINERAL_VEIN_4985, Scenery.MINERAL_VEIN_4994, (byte) 8),
    /**
     * Coal 3 mining node.
     */
    COAL_3(Scenery.MINERAL_VEIN_4986, Scenery.MINERAL_VEIN_4995, (byte) 8),
    /**
     * Coal 4 mining node.
     */
    COAL_4(Scenery.MINERAL_VEIN_4987, Scenery.MINERAL_VEIN_4996, (byte) 8),
    /**
     * Coal 5 mining node.
     */
    COAL_5(Scenery.ROCKS_4676, Scenery.ROCKS_450, (byte) 8),
    /**
     * Coal 6 mining node.
     */
    COAL_6(Scenery.ROCKS_10948, Scenery.ROCKS_10944, (byte) 8),
    /**
     * Coal 7 mining node.
     */
    COAL_7(Scenery.ROCKS_11964, Scenery.ROCKS_11556, (byte) 8),
    /**
     * Coal 8 mining node.
     */
    COAL_8(Scenery.ROCKS_11965, Scenery.ROCKS_11557, (byte) 8),
    /**
     * Coal 9 mining node.
     */
    COAL_9(Scenery.ROCKS_11963, Scenery.ROCKS_11555, (byte) 8),
    /**
     * Coal 10 mining node.
     */
    COAL_10(Scenery.ROCKS_11932, Scenery.ROCKS_11554, (byte) 8),
    /**
     * Coal 11 mining node.
     */
    COAL_11(Scenery.ROCKS_11930, Scenery.ROCKS_11552, (byte) 8),
    /**
     * Coal 12 mining node.
     */
    COAL_12(Scenery.ROCKS_11931, Scenery.ROCKS_11553, (byte) 8),
    /**
     * Coal 13 mining node.
     */
    COAL_13(Scenery.ROCKS_15246, Scenery.ROCKS_15249, (byte) 8),
    /**
     * Coal 14 mining node.
     */
    COAL_14(Scenery.ROCKS_15247, Scenery.ROCKS_15250, (byte) 8),
    /**
     * Coal 15 mining node.
     */
    COAL_15(Scenery.ROCKS_15248, Scenery.ROCKS_15251, (byte) 8),
    /**
     * Coal 16 mining node.
     */
    COAL_16(Scenery.ROCKS_14852, Scenery.ROCKS_25373, (byte) 8),
    /**
     * Coal 17 mining node.
     */
    COAL_17(Scenery.ROCKS_14851, Scenery.ROCKS_25372, (byte) 8),
    /**
     * Coal 18 mining node.
     */
    COAL_18(Scenery.ROCKS_14850, Scenery.ROCKS_25371, (byte) 8),
    /**
     * Coal 19 mining node.
     */
    COAL_19(Scenery.ORE_VEIN_20410, Scenery.ORE_VEIN_20443, (byte) 8),
    /**
     * Coal 20 mining node.
     */
    COAL_20(Scenery.ORE_VEIN_20411, Scenery.ORE_VEIN_20444, (byte) 8),
    /**
     * Coal 21 mining node.
     */
    COAL_21(Scenery.ORE_VEIN_20412, Scenery.ORE_VEIN_20445, (byte) 8),
    /**
     * Coal 22 mining node.
     */
    COAL_22(Scenery.ORE_VEIN_20413, Scenery.ORE_VEIN_20407, (byte) 8),
    /**
     * Coal 23 mining node.
     */
    COAL_23(Scenery.ROCKS_18999, Scenery.ROCKS_19005, (byte) 8),
    /**
     * Coal 24 mining node.
     */
    COAL_24(Scenery.ROCKS_18998, Scenery.ROCKS_19004, (byte) 8),
    /**
     * Coal 25 mining node.
     */
    COAL_25(Scenery.ROCKS_18997, Scenery.ROCKS_19003, (byte) 8),
    /**
     * Coal 26 mining node.
     */
    COAL_26(Scenery.ROCKS_21287, Scenery.ROCKS_21296, (byte) 8),
    /**
     * Coal 27 mining node.
     */
    COAL_27(Scenery.ROCKS_21289, Scenery.ROCKS_21298, (byte) 8),
    /**
     * Coal 28 mining node.
     */
    COAL_28(Scenery.ROCKS_21288, Scenery.ROCKS_21297, (byte) 8),
    /**
     * Coal 29 mining node.
     */
    COAL_29(Scenery.ROCKS_23565, Scenery.ROCKS_21298, (byte) 8),
    /**
     * Coal 30 mining node.
     */
    COAL_30(Scenery.ROCKS_23564, Scenery.ROCKS_21297, (byte) 8),
    /**
     * Coal 31 mining node.
     */
    COAL_31(Scenery.ROCKS_23563, Scenery.ROCKS_21296, (byte) 8),
    /**
     * Coal 32 mining node.
     */
    COAL_32(Scenery.ROCKS_29215, Scenery.ROCKS_29218, (byte) 8),
    /**
     * Coal 33 mining node.
     */
    COAL_33(Scenery.ROCKS_29217, Scenery.ROCKS_29220, (byte) 8),
    /**
     * Coal 34 mining node.
     */
    COAL_34(Scenery.ROCKS_29216, Scenery.ROCKS_29219, (byte) 8),
    /**
     * Coal 35 mining node.
     */
    COAL_35(Scenery.ROCKS_32426, Scenery.ROCKS_33400, (byte) 8),
    /**
     * Coal 36 mining node.
     */
    COAL_36(Scenery.ROCKS_32427, Scenery.ROCKS_33401, (byte) 8),
    /**
     * Coal 37 mining node.
     */
    COAL_37(Scenery.ROCKS_32428, Scenery.ROCKS_33402, (byte) 8),
    /**
     * Coal 38 mining node.
     */
    COAL_38(Scenery.ROCKS_32450, Scenery.ROCKS_32448, (byte) 8),
    /**
     * Coal 39 mining node.
     */
    COAL_39(Scenery.ROCKS_32449, Scenery.ROCKS_32447, (byte) 8),
    /**
     * Coal 40 mining node.
     */
    COAL_40(Scenery.ROCKS_31068, Scenery.ROCKS_37639, (byte) 8),
    /**
     * Coal 41 mining node.
     */
    COAL_41(Scenery.ROCKS_31069, Scenery.ROCKS_37649, (byte) 8),
    /**
     * Coal 42 mining node.
     */
    COAL_42(Scenery.ROCKS_31070, Scenery.ROCKS_37650, (byte) 8),
    /**
     * Coal 43 mining node.
     */
    COAL_43(Scenery.ROCKS_31168, Scenery.ROCKS_14833, (byte) 8),
    /**
     * Coal 44 mining node.
     */
    COAL_44(Scenery.ROCKS_31169, Scenery.ROCKS_14834, (byte) 8),
    /**
     * Coal 45 mining node.
     */
    COAL_45(Scenery.ROCKS_31167, Scenery.ROCKS_14832, (byte) 8),
    /**
     * Coal 46 mining node.
     */
    COAL_46(Scenery.ROCKS_37699, Scenery.ROCKS_21298, (byte) 8),
    /**
     * Coal 47 mining node.
     */
    COAL_47(Scenery.ROCKS_37698, Scenery.ROCKS_21297, (byte) 8),
    /**
     * Coal 48 mining node.
     */
    COAL_48(Scenery.ROCKS_37697, Scenery.ROCKS_21296, (byte) 8),

    /**
     * Gold ore 0 mining node.
     */
    GOLD_ORE_0(Scenery.ROCKS_2099, Scenery.ROCKS_452, (byte) 9),
    /**
     * Gold ore 1 mining node.
     */
    GOLD_ORE_1(Scenery.ROCKS_2098, Scenery.ROCKS_450, (byte) 9),
    /**
     * Gold ore 2 mining node.
     */
    GOLD_ORE_2(Scenery.ROCKS_2611, Scenery.ROCKS_21298, (byte) 9),
    /**
     * Gold ore 3 mining node.
     */
    GOLD_ORE_3(Scenery.ROCKS_2610, Scenery.ROCKS_21297, (byte) 9),
    /**
     * Gold ore 4 mining node.
     */
    GOLD_ORE_4(Scenery.ROCKS_2609, Scenery.ROCKS_21296, (byte) 9),
    /**
     * Gold ore 5 mining node.
     */
    GOLD_ORE_5(Scenery.ROCKS_9722, Scenery.ROCKS_18954, (byte) 9),
    /**
     * Gold ore 6 mining node.
     */
    GOLD_ORE_6(Scenery.ROCKS_9720, Scenery.ROCKS_32447, (byte) 9),
    /**
     * Gold ore 7 mining node.
     */
    GOLD_ORE_7(Scenery.ROCKS_9721, Scenery.ROCKS_32448, (byte) 9),
    /**
     * Gold ore 8 mining node.
     */
    GOLD_ORE_8(Scenery.ROCKS_11183, Scenery.ROCKS_21296, (byte) 9),
    /**
     * Gold ore 9 mining node.
     */
    GOLD_ORE_9(Scenery.ROCKS_11184, Scenery.ROCKS_21297, (byte) 9),
    /**
     * Gold ore 10 mining node.
     */
    GOLD_ORE_10(Scenery.ROCKS_11185, Scenery.ROCKS_21298, (byte) 9),
    /**
     * Gold ore 11 mining node.
     */
    GOLD_ORE_11(Scenery.ROCKS_11952, Scenery.ROCKS_11556, (byte) 9),
    /**
     * Gold ore 12 mining node.
     */
    GOLD_ORE_12(Scenery.ROCKS_11953, Scenery.ROCKS_11557, (byte) 9),
    /**
     * Gold ore 13 mining node.
     */
    GOLD_ORE_13(Scenery.ROCKS_11951, Scenery.ROCKS_11555, (byte) 9),
    /**
     * Gold ore 14 mining node.
     */
    GOLD_ORE_14(Scenery.ROCKS_15578, Scenery.ROCKS_14834, (byte) 9),
    /**
     * Gold ore 15 mining node.
     */
    GOLD_ORE_15(Scenery.ROCKS_15577, Scenery.ROCKS_14833, (byte) 9),
    /**
     * Gold ore 16 mining node.
     */
    GOLD_ORE_16(Scenery.ROCKS_15576, Scenery.ROCKS_14832, (byte) 9),
    /**
     * Gold ore 17 mining node.
     */
    GOLD_ORE_17(Scenery.ROCKS_17002, Scenery.ROCKS_14916, (byte) 9),
    /**
     * Gold ore 18 mining node.
     */
    GOLD_ORE_18(Scenery.ROCKS_17003, Scenery.ROCKS_31061, (byte) 9),
    /**
     * Gold ore 19 mining node.
     */
    GOLD_ORE_19(Scenery.ROCKS_17001, Scenery.ROCKS_14915, (byte) 9),
    /**
     * Gold ore 20 mining node.
     */
    GOLD_ORE_20(Scenery.ROCKS_21291, Scenery.ROCKS_21297, (byte) 9),
    /**
     * Gold ore 21 mining node.
     */
    GOLD_ORE_21(Scenery.ROCKS_21290, Scenery.ROCKS_21296, (byte) 9),
    /**
     * Gold ore 22 mining node.
     */
    GOLD_ORE_22(Scenery.ROCKS_21292, Scenery.ROCKS_21298, (byte) 9),
    /**
     * Gold ore 23 mining node.
     */
    GOLD_ORE_23(Scenery.ROCKS_32433, Scenery.ROCKS_33401, (byte) 9),
    /**
     * Gold ore 24 mining node.
     */
    GOLD_ORE_24(Scenery.ROCKS_32432, Scenery.ROCKS_33400, (byte) 9),
    /**
     * Gold ore 25 mining node.
     */
    GOLD_ORE_25(Scenery.ROCKS_32434, Scenery.ROCKS_33402, (byte) 9),
    /**
     * Gold ore 26 mining node.
     */
    GOLD_ORE_26(Scenery.ROCKS_31065, Scenery.ROCKS_37639, (byte) 9),
    /**
     * Gold ore 27 mining node.
     */
    GOLD_ORE_27(Scenery.ROCKS_31066, Scenery.ROCKS_37649, (byte) 9),
    /**
     * Gold ore 28 mining node.
     */
    GOLD_ORE_28(Scenery.ROCKS_31067, Scenery.ROCKS_37650, (byte) 9),
    /**
     * Gold ore 29 mining node.
     */
    GOLD_ORE_29(Scenery.ROCKS_37311, Scenery.ROCKS_11553, (byte) 9),
    /**
     * Gold ore 30 mining node.
     */
    GOLD_ORE_30(Scenery.ROCKS_37310, Scenery.ROCKS_11552, (byte) 9),
    /**
     * Gold ore 31 mining node.
     */
    GOLD_ORE_31(Scenery.ROCKS_37312, Scenery.ROCKS_11554, (byte) 9),
    /**
     * Gold ore 32 mining node.
     */
    GOLD_ORE_32(Scenery.ROCKS_37471, Scenery.ROCKS_15249, (byte) 9),
    /**
     * Gold ore 33 mining node.
     */
    GOLD_ORE_33(Scenery.ROCKS_37473, Scenery.ROCKS_15251, (byte) 9),
    /**
     * Gold ore 34 mining node.
     */
    GOLD_ORE_34(Scenery.ROCKS_37472, Scenery.ROCKS_15250, (byte) 9),

    /**
     * Mithril ore 0 mining node.
     */
    MITHRIL_ORE_0(Scenery.ROCKS_2103, Scenery.ROCKS_452, (byte) 10),
    /**
     * Mithril ore 1 mining node.
     */
    MITHRIL_ORE_1(Scenery.ROCKS_2102, Scenery.ROCKS_450, (byte) 10),
    /**
     * Mithril ore 2 mining node.
     */
    MITHRIL_ORE_2(Scenery.MINERAL_VEIN_4988, Scenery.MINERAL_VEIN_4994, (byte) 10),
    /**
     * Mithril ore 3 mining node.
     */
    MITHRIL_ORE_3(Scenery.MINERAL_VEIN_4989, Scenery.MINERAL_VEIN_4995, (byte) 10),
    /**
     * Mithril ore 4 mining node.
     */
    MITHRIL_ORE_4(Scenery.MINERAL_VEIN_4990, Scenery.MINERAL_VEIN_4996, (byte) 10),
    /**
     * Mithril ore 5 mining node.
     */
    MITHRIL_ORE_5(Scenery.ROCKS_11943, Scenery.ROCKS_11553, (byte) 10),
    /**
     * Mithril ore 6 mining node.
     */
    MITHRIL_ORE_6(Scenery.ROCKS_11942, Scenery.ROCKS_11552, (byte) 10),
    /**
     * Mithril ore 7 mining node.
     */
    MITHRIL_ORE_7(Scenery.ROCKS_11945, Scenery.ROCKS_11555, (byte) 10),
    /**
     * Mithril ore 8 mining node.
     */
    MITHRIL_ORE_8(Scenery.ROCKS_11944, Scenery.ROCKS_11554, (byte) 10),
    /**
     * Mithril ore 9 mining node.
     */
    MITHRIL_ORE_9(Scenery.ROCKS_11947, Scenery.ROCKS_11557, (byte) 10),
    /**
     * Mithril ore 10 mining node.
     */
    MITHRIL_ORE_10(Scenery.ROCKS_11946, Scenery.ROCKS_11556, (byte) 10),
    /**
     * Mithril ore 11 mining node.
     */
    MITHRIL_ORE_11(Scenery.ROCKS_14855, Scenery.ROCKS_25373, (byte) 10),
    /**
     * Mithril ore 12 mining node.
     */
    MITHRIL_ORE_12(Scenery.ROCKS_14854, Scenery.ROCKS_25372, (byte) 10),
    /**
     * Mithril ore 13 mining node.
     */
    MITHRIL_ORE_13(Scenery.ROCKS_14853, Scenery.ROCKS_25371, (byte) 10),
    /**
     * Mithril ore 14 mining node.
     */
    MITHRIL_ORE_14(Scenery.ROCKS_14854, Scenery.ROCKS_25372, (byte) 10),
    /**
     * Mithril ore 15 mining node.
     */
    MITHRIL_ORE_15(Scenery.RUNE_ESSENCE_16687, Scenery.ROCKS_450, (byte) 10),

    /**
     * Mithril ore 16 mining node.
     */
    MITHRIL_ORE_16(Scenery.ORE_VEIN_20421, Scenery.ORE_VEIN_20407, (byte) 10),
    /**
     * Mithril ore 17 mining node.
     */
    MITHRIL_ORE_17(Scenery.ORE_VEIN_20420, Scenery.ORE_VEIN_20445, (byte) 10),
    /**
     * Mithril ore 18 mining node.
     */
    MITHRIL_ORE_18(Scenery.ORE_VEIN_20419, Scenery.ORE_VEIN_20444, (byte) 10),
    /**
     * Mithril ore 19 mining node.
     */
    MITHRIL_ORE_19(Scenery.ORE_VEIN_20418, Scenery.ORE_VEIN_20443, (byte) 10),
    /**
     * Mithril ore 20 mining node.
     */
    MITHRIL_ORE_20(Scenery.ROCKS_19012, Scenery.ROCKS_19021, (byte) 10),
    /**
     * Mithril ore 21 mining node.
     */
    MITHRIL_ORE_21(Scenery.ROCKS_19013, Scenery.ROCKS_19016, (byte) 10),
    /**
     * Mithril ore 22 mining node.
     */
    MITHRIL_ORE_22(Scenery.ROCKS_19014, Scenery.ROCKS_19017, (byte) 10),
    /**
     * Mithril ore 23 mining node.
     */
    MITHRIL_ORE_23(Scenery.ROCKS_21278, Scenery.ROCKS_21296, (byte) 10),
    /**
     * Mithril ore 24 mining node.
     */
    MITHRIL_ORE_24(Scenery.ROCKS_21279, Scenery.ROCKS_21297, (byte) 10),
    /**
     * Mithril ore 25 mining node.
     */
    MITHRIL_ORE_25(Scenery.ROCKS_21280, Scenery.ROCKS_21298, (byte) 10),
    /**
     * Mithril ore 26 mining node.
     */
    MITHRIL_ORE_26(Scenery.ROCKS_25369, Scenery.ROCKS_10586, (byte) 10),
    /**
     * Mithril ore 27 mining node.
     */
    MITHRIL_ORE_27(Scenery.ROCKS_25368, Scenery.ROCKS_10585, (byte) 10),
    /**
     * Mithril ore 28 mining node.
     */
    MITHRIL_ORE_28(Scenery.ROCKS_25370, Scenery.ROCKS_10587, (byte) 10),
    /**
     * Mithril ore 29 mining node.
     */
    MITHRIL_ORE_29(Scenery.ROCKS_29236, Scenery.ROCKS_29218, (byte) 10),
    /**
     * Mithril ore 30 mining node.
     */
    MITHRIL_ORE_30(Scenery.ROCKS_29237, Scenery.ROCKS_29219, (byte) 10),
    /**
     * Mithril ore 31 mining node.
     */
    MITHRIL_ORE_31(Scenery.ROCKS_29238, Scenery.ROCKS_29220, (byte) 10),
    /**
     * Mithril ore 32 mining node.
     */
    MITHRIL_ORE_32(Scenery.ROCKS_32439, Scenery.ROCKS_33401, (byte) 10),
    /**
     * Mithril ore 33 mining node.
     */
    MITHRIL_ORE_33(Scenery.ROCKS_32438, Scenery.ROCKS_33400, (byte) 10),
    /**
     * Mithril ore 34 mining node.
     */
    MITHRIL_ORE_34(Scenery.ROCKS_32440, Scenery.ROCKS_33402, (byte) 10),
    /**
     * Mithril ore 35 mining node.
     */
    MITHRIL_ORE_35(Scenery.ROCKS_31087, Scenery.ROCKS_37649, (byte) 10),
    /**
     * Mithril ore 36 mining node.
     */
    MITHRIL_ORE_36(Scenery.ROCKS_31086, Scenery.ROCKS_37639, (byte) 10),
    /**
     * Mithril ore 37 mining node.
     */
    MITHRIL_ORE_37(Scenery.ROCKS_31088, Scenery.ROCKS_37650, (byte) 10),
    /**
     * Mithril ore 38 mining node.
     */
    MITHRIL_ORE_38(Scenery.ROCKS_31170, Scenery.ROCKS_14832, (byte) 10),
    /**
     * Mithril ore 39 mining node.
     */
    MITHRIL_ORE_39(Scenery.ROCKS_31171, Scenery.ROCKS_14833, (byte) 10),
    /**
     * Mithril ore 40 mining node.
     */
    MITHRIL_ORE_40(Scenery.ROCKS_31172, Scenery.ROCKS_14834, (byte) 10),
    /**
     * Mithril ore 41 mining node.
     */
    MITHRIL_ORE_41(Scenery.ROCKS_37692, Scenery.ROCKS_21296, (byte) 10),
    /**
     * Mithril ore 42 mining node.
     */
    MITHRIL_ORE_42(Scenery.ROCKS_37693, Scenery.ROCKS_21297, (byte) 10),
    /**
     * Mithril ore 43 mining node.
     */
    MITHRIL_ORE_43(Scenery.ROCKS_37694, Scenery.ROCKS_21298, (byte) 10),

    /**
     * Adamantite ore 0 mining node.
     */
    ADAMANTITE_ORE_0(Scenery.ROCKS_2105, Scenery.ROCKS_452, (byte) 11),
    /**
     * Adamantite ore 1 mining node.
     */
    ADAMANTITE_ORE_1(Scenery.ROCKS_2104, Scenery.ROCKS_450, (byte) 11),
    /**
     * Adamantite ore 2 mining node.
     */
    ADAMANTITE_ORE_2(Scenery.MINERAL_VEIN_4991, Scenery.MINERAL_VEIN_4994, (byte) 11),
    /**
     * Adamantite ore 3 mining node.
     */
    ADAMANTITE_ORE_3(Scenery.MINERAL_VEIN_4992, Scenery.MINERAL_VEIN_4995, (byte) 11),
    /**
     * Adamantite ore 4 mining node.
     */
    ADAMANTITE_ORE_4(Scenery.MINERAL_VEIN_4993, Scenery.MINERAL_VEIN_4996, (byte) 11),
    /**
     * Adamantite ore 5 mining node.
     */
    ADAMANTITE_ORE_5(Scenery.ROCKS_11941, Scenery.ROCKS_11554, (byte) 11),
    /**
     * Adamantite ore 6 mining node.
     */
    ADAMANTITE_ORE_6(Scenery.ROCKS_11940, Scenery.ROCKS_11553, (byte) 11),
    /**
     * Adamantite ore 7 mining node.
     */
    ADAMANTITE_ORE_7(Scenery.ROCKS_11939, Scenery.ROCKS_11552, (byte) 11),
    /**
     * Adamantite ore 8 mining node.
     */
    ADAMANTITE_ORE_8(Scenery.ROCKS_14864, Scenery.ROCKS_25373, (byte) 11),
    /**
     * Adamantite ore 9 mining node.
     */
    ADAMANTITE_ORE_9(Scenery.ROCKS_14863, Scenery.ROCKS_25372, (byte) 11),
    /**
     * Adamantite ore 10 mining node.
     */
    ADAMANTITE_ORE_10(Scenery.ROCKS_14862, Scenery.ROCKS_25371, (byte) 11),
    /**
     * Adamantite ore 11 mining node.
     */
    ADAMANTITE_ORE_11(Scenery.ORE_VEIN_20417, Scenery.ORE_VEIN_20407, (byte) 11),
    /**
     * Adamantite ore 12 mining node.
     */
    ADAMANTITE_ORE_12(Scenery.ORE_VEIN_20416, Scenery.ORE_VEIN_20445, (byte) 11),
    /**
     * Adamantite ore 13 mining node.
     */
    ADAMANTITE_ORE_13(Scenery.ORE_VEIN_20414, Scenery.ORE_VEIN_20443, (byte) 11),
    /**
     * Adamantite ore 14 mining node.
     */
    ADAMANTITE_ORE_14(Scenery.ORE_VEIN_20415, Scenery.ORE_VEIN_20444, (byte) 11),
    /**
     * Adamantite ore 15 mining node.
     */
    ADAMANTITE_ORE_15(Scenery.ROCKS_19020, Scenery.ROCKS_19017, (byte) 11),
    /**
     * Adamantite ore 16 mining node.
     */
    ADAMANTITE_ORE_16(Scenery.ROCKS_19018, Scenery.ROCKS_19021, (byte) 11),
    /**
     * Adamantite ore 17 mining node.
     */
    ADAMANTITE_ORE_17(Scenery.ROCKS_19019, Scenery.ROCKS_19016, (byte) 11),
    /**
     * Adamantite ore 18 mining node.
     */
    ADAMANTITE_ORE_18(Scenery.ROCKS_21275, Scenery.ROCKS_21296, (byte) 11),
    /**
     * Adamantite ore 19 mining node.
     */
    ADAMANTITE_ORE_19(Scenery.ROCKS_21276, Scenery.ROCKS_21297, (byte) 11),
    /**
     * Adamantite ore 20 mining node.
     */
    ADAMANTITE_ORE_20(Scenery.ROCKS_21277, Scenery.ROCKS_21298, (byte) 11),
    /**
     * Adamantite ore 21 mining node.
     */
    ADAMANTITE_ORE_21(Scenery.ROCKS_29233, Scenery.ROCKS_29218, (byte) 11),
    /**
     * Adamantite ore 22 mining node.
     */
    ADAMANTITE_ORE_22(Scenery.ROCKS_29234, Scenery.ROCKS_29219, (byte) 11),
    /**
     * Adamantite ore 23 mining node.
     */
    ADAMANTITE_ORE_23(Scenery.ROCKS_29235, Scenery.ROCKS_29220, (byte) 11),
    /**
     * Adamantite ore 24 mining node.
     */
    ADAMANTITE_ORE_24(Scenery.ROCKS_32435, Scenery.ROCKS_33400, (byte) 11),
    /**
     * Adamantite ore 25 mining node.
     */
    ADAMANTITE_ORE_25(Scenery.ROCKS_32437, Scenery.ROCKS_33402, (byte) 11),
    /**
     * Adamantite ore 26 mining node.
     */
    ADAMANTITE_ORE_26(Scenery.ROCKS_32436, Scenery.ROCKS_33401, (byte) 11),
    /**
     * Adamantite ore 27 mining node.
     */
    ADAMANTITE_ORE_27(Scenery.ROCKS_31083, Scenery.ROCKS_37639, (byte) 11),
    /**
     * Adamantite ore 28 mining node.
     */
    ADAMANTITE_ORE_28(Scenery.ROCKS_31085, Scenery.ROCKS_37650, (byte) 11),
    /**
     * Adamantite ore 29 mining node.
     */
    ADAMANTITE_ORE_29(Scenery.ROCKS_31084, Scenery.ROCKS_37649, (byte) 11),
    /**
     * Adamantite ore 30 mining node.
     */
    ADAMANTITE_ORE_30(Scenery.ROCKS_31173, Scenery.ROCKS_14832, (byte) 11),
    /**
     * Adamantite ore 31 mining node.
     */
    ADAMANTITE_ORE_31(Scenery.ROCKS_31174, Scenery.ROCKS_14833, (byte) 11),
    /**
     * Adamantite ore 32 mining node.
     */
    ADAMANTITE_ORE_32(Scenery.ROCKS_31175, Scenery.ROCKS_14834, (byte) 11),
    /**
     * Adamantite ore 33 mining node.
     */
    ADAMANTITE_ORE_33(Scenery.ROCKS_37468, Scenery.ROCKS_15249, (byte) 11),
    /**
     * Adamantite ore 34 mining node.
     */
    ADAMANTITE_ORE_34(Scenery.ROCKS_37469, Scenery.ROCKS_15250, (byte) 11),
    /**
     * Adamantite ore 35 mining node.
     */
    ADAMANTITE_ORE_35(Scenery.ROCKS_37470, Scenery.ROCKS_15251, (byte) 11),
    /**
     * Adamantite ore 36 mining node.
     */
    ADAMANTITE_ORE_36(Scenery.ROCKS_37689, Scenery.ROCKS_21296, (byte) 11),
    /**
     * Adamantite ore 37 mining node.
     */
    ADAMANTITE_ORE_37(Scenery.ROCKS_37690, Scenery.ROCKS_21297, (byte) 11),
    /**
     * Adamantite ore 38 mining node.
     */
    ADAMANTITE_ORE_38(Scenery.ROCKS_37691, Scenery.ROCKS_21298, (byte) 11),

    /**
     * Runite ore 0 mining node.
     */
    RUNITE_ORE_0(Scenery.ROCKS_2107, Scenery.ROCKS_452, (byte) 12),
    /**
     * Runite ore 1 mining node.
     */
    RUNITE_ORE_1(Scenery.ROCKS_2106, Scenery.ROCKS_450, (byte) 12),
    /**
     * Runite ore 2 mining node.
     */
    RUNITE_ORE_2(Scenery.ROCKS_14861, Scenery.ROCKS_25373, (byte) 12),
    /**
     * Runite ore 3 mining node.
     */
    RUNITE_ORE_3(Scenery.ROCKS_14860, Scenery.ROCKS_25372, (byte) 12),
    /**
     * Runite ore 4 mining node.
     */
    RUNITE_ORE_4(Scenery.ROCKS_14859, Scenery.ROCKS_25371, (byte) 12),
    /**
     * Runite ore 5 mining node.
     */
    RUNITE_ORE_5(Scenery.ROCKS_33079, Scenery.ROCKS_33401, (byte) 12),
    /**
     * Runite ore 6 mining node.
     */
    RUNITE_ORE_6(Scenery.ROCKS_33078, Scenery.ROCKS_33400, (byte) 12),
    /**
     * Runite ore 7 mining node.
     */
    RUNITE_ORE_7(Scenery.ROCKS_37208, Scenery.ROCKS_21296, (byte) 12),
    /**
     * Runite ore 8 mining node.
     */
    RUNITE_ORE_8(Scenery.ROCKS_37465, Scenery.ROCKS_15249, (byte) 12),
    /**
     * Runite ore 9 mining node.
     */
    RUNITE_ORE_9(Scenery.ROCKS_37466, Scenery.ROCKS_15250, (byte) 12),
    /**
     * Runite ore 10 mining node.
     */
    RUNITE_ORE_10(Scenery.ROCKS_37467, Scenery.ROCKS_15251, (byte) 12),
    /**
     * Runite ore 11 mining node.
     */
    RUNITE_ORE_11(Scenery.ROCKS_37695, Scenery.ROCKS_21297, (byte) 12),
    /**
     * Runite ore 12 mining node.
     */
    RUNITE_ORE_12(Scenery.ROCKS_37696, Scenery.ROCKS_21298, (byte) 12),

    /**
     * Magic stone 0 mining node.
     */
    MAGIC_STONE_0( Scenery.ROCKS_6669, Scenery.ROCKS_21296, (byte) 18),
    /**
     * Magic stone 1 mining node.
     */
    MAGIC_STONE_1( Scenery.ROCKS_6671, Scenery.ROCKS_21298, (byte) 18),
    /**
     * Magic stone 2 mining node.
     */
    MAGIC_STONE_2( Scenery.ROCKS_6670, Scenery.ROCKS_21297, (byte) 18),


    /**
     * Gem rock 0 mining node.
     */
    GEM_ROCK_0(Scenery.ROCKS_23567, Scenery.ROCKS_21297, (byte) 13),
    /**
     * Gem rock 1 mining node.
     */
    GEM_ROCK_1(Scenery.ROCKS_23566, Scenery.ROCKS_21296, (byte) 13),
    /**
     * Gem rock 2 mining node.
     */
    GEM_ROCK_2(Scenery.ROCKS_23568, Scenery.ROCKS_21298, (byte) 13),
    /**
     * Gem rock 3 mining node.
     */
    GEM_ROCK_3(Scenery.ROCKS_23560, Scenery.ROCKS_25371, (byte) 13),
    /**
     * Gem rock 4 mining node.
     */
    GEM_ROCK_4(Scenery.ROCKS_23561, Scenery.ROCKS_25372, (byte) 13),
    /**
     * Gem rock 5 mining node.
     */
    GEM_ROCK_5(Scenery.ROCKS_23562, Scenery.ROCKS_21298, (byte) 13),

    /**
     * Gem rock 6 mining node.
     */
    GEM_ROCK_6(Scenery.GEM_ROCK_9030, Scenery.LIGHT_JUNGLE_9010, (byte) 13),
    /**
     * Gem rock 7 mining node.
     */
    GEM_ROCK_7(Scenery.GEM_ROCK_9031,Scenery.MEDIUM_JUNGLE_9015, (byte) 13),
    /**
     * Gem rock 8 mining node.
     */
    GEM_ROCK_8(Scenery.GEM_ROCK_9032, Scenery.DENSE_JUNGLE_9020, (byte) 13),

    /**
     * Rune essence 0 mining node.
     */
    RUNE_ESSENCE_0(Scenery.RUNE_ESSENCE_2491, -1, (byte) 14),
    /**
     * Rune essence 1 mining node.
     */
    RUNE_ESSENCE_1(Scenery.ROCK_16684, -1, (byte) 14),


    /**
     * Sandstone mining node.
     */
    SANDSTONE(Scenery.ROCKS_10946, Scenery.ROCKS_10944, (byte) 15),

    /**
     * Granite mining node.
     */
    GRANITE(Scenery.ROCKS_10947, Scenery.ROCKS_10945, (byte) 16),

    /**
     * Rubium mining node.
     */
    RUBIUM(Scenery.RUBIUM_29746, Scenery.WALL_29747, (byte) 17),

    /**
     * Obsidian mining node 0.
     */
    OBSIDIAN_0(Scenery.OBSIDIAN_WALL_9376, Scenery.OBSIDIAN_WALL_31229, (byte) 19),

    /**
     * Obsidian mining node 1.
     */
    OBSIDIAN_1(Scenery.OBSIDIAN_WALL_31229, Scenery.OBSIDIAN_WALL_31230, (byte) 19),

    /**
     * Obsidian mining node 2.
     */
    OBSIDIAN_2(Scenery.OBSIDIAN_WALL_31230, Scenery.OBSIDIAN_WALL_9376, (byte) 19);

    /**
     * The gem rock reward.
     */
    public static List<WeightedChanceItem> GEM_ROCK_REWARD = new ArrayList<>(20);

    static {
        GEM_ROCK_REWARD.add(new WeightedChanceItem(Items.UNCUT_OPAL_1625, 1, 60));
        GEM_ROCK_REWARD.add(new WeightedChanceItem(Items.UNCUT_JADE_1627, 1, 30));
        GEM_ROCK_REWARD.add(new WeightedChanceItem(Items.UNCUT_RED_TOPAZ_1629, 1, 15));
        GEM_ROCK_REWARD.add(new WeightedChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, 9));
        GEM_ROCK_REWARD.add(new WeightedChanceItem(Items.UNCUT_EMERALD_1621, 1, 5));
        GEM_ROCK_REWARD.add(new WeightedChanceItem(Items.UNCUT_RUBY_1619, 1, 5));
        GEM_ROCK_REWARD.add(new WeightedChanceItem(Items.UNCUT_DIAMOND_1617, 1, 4));
    }

    /**
     * The Full.
     */
    int full,

    /**
     * Empty mining node.
     */
    empty,
    /**
     * Respawn rate mining node.
     */
    respawnRate,
    /**
     * Reward mining node.
     */
    reward,
    /**
     * Level mining node.
     */
    level;
    /**
     * The Rate.
     */
    double rate,
    /**
     * Experience mining node.
     */
    experience;
    /**
     * The Identifier.
     */
    public byte identifier;

    MiningNode(int full, int empty, byte identifier) {
        this.full = full;
        this.empty = empty;
        this.identifier = identifier;
        switch (identifier & 0xFF) {
            case 1:
            case 2:
                respawnRate = 4 | 8 << 16;
                experience = 17.5;
                rate = 0.05;
                reward = identifier == 1 ? 436 : 438;
                level = 1;
                break;
            case 3:
                respawnRate = 1 | 1 << 16;
                experience = 5.0;
                rate = 0.1;
                reward = 434;
                level = 1;
                break;
            case 4:
                respawnRate = 10 | 20 << 16;
                experience = 26.5;
                rate = 0.2;
                reward = 3211;
                level = 10;
                break;
            case 5:
                respawnRate = 10 | 20 << 16;
                experience = 17.5;
                rate = 0.2;
                reward = 668;
                level = 10;
                break;
            case 6:
                respawnRate = 15 | 25 << 16;
                experience = 35.0;
                rate = 0.2;
                reward = 440;
                level = 15;
                break;
            case 7:
                respawnRate = 100 | 200 << 16;
                experience = 40.0;
                rate = 0.3;
                reward = 442;
                level = 20;
                break;
            case 8:
                respawnRate = 50 | 100 << 16;
                experience = 50.0;
                rate = 0.4;
                reward = 453;
                level = 30;
                break;
            case 9:
                respawnRate = 100 | 200 << 16;
                experience = 65.0;
                rate = 0.6;
                reward = 444;
                level = 40;
                break;
            case 10:
                respawnRate = 200 | 400 << 16;
                experience = 80.0;
                rate = 0.70;
                reward = 447;
                level = 55;
                break;
            case 11:
                respawnRate = 400 | 800 << 16;
                experience = 95.0;
                rate = 0.85;
                reward = 449;
                level = 70;
                break;
            case 12:
                respawnRate = 1250 | 2500 << 16;
                experience = 125.0;
                rate = 0.95;
                reward = 451;
                level = 85;
                break;
            case 13:
                respawnRate = 166 | 175 << 16;
                experience = 65;
                rate = 0.95;
                reward = 1625;
                level = 40;
                break;
            case 14:
                respawnRate = 1 | 1 << 16;
                experience = 5.0;
                rate = 0.1;
                reward = 1436;
                level = 1;
                break;
            case 15:
                respawnRate = 30 | 60 << 16;
                experience = 30.0;
                rate = 0.2;
                reward = 6971;
                level = 35;
                break;
            case 16:
                respawnRate = 10 | 20 << 16;
                experience = 50.0;
                rate = 0.2;
                reward = 6979;
                level = 45;
                break;
            case 17:
                respawnRate = 50 | 100 << 16;
                experience = 17.5;
                rate = 0.6;
                reward = 12630;
                level = 46;
                break;
            case 18:
                respawnRate = 100 | 200 << 16;
                experience = 0.0;
                rate = 0.3;
                reward = 4703;
                level = 20;
                break;
            case 19:
                respawnRate = 100 | 200 << 16;
                experience = 40.0;
                rate = 0.74;
                reward = 13245;
                level = 41;
                break;
        }
    }

    private static HashMap<Integer, MiningNode> NODE_MAP = new HashMap<>();
    private static HashMap<Integer, Integer> EMPTY_MAP = new HashMap<>();

    static {
        for (MiningNode node : MiningNode.values()) {
            NODE_MAP.putIfAbsent(node.full, node);
        }
        for (MiningNode node : MiningNode.values()) {
            EMPTY_MAP.putIfAbsent(node.empty, node.full);
        }
    }

    /**
     * For id mining node.
     *
     * @param id the id
     * @return the mining node
     */
    public static MiningNode forId(int id) {
        return NODE_MAP.get(id);
    }

    /**
     * Is empty boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public static boolean isEmpty(int id) {
        return EMPTY_MAP.get(id) != null;
    }

    /**
     * Gets reward amount.
     *
     * @return the reward amount
     */
    public int getRewardAmount() {
        return 1;
    }

    /**
     * Gets empty id.
     *
     * @return the empty id
     */
    public int getEmptyId() {
        return empty;
    }

    /**
     * Gets reward.
     *
     * @return the reward
     */
    public int getReward() {
        return reward;
    }

    /**
     * Gets experience.
     *
     * @return the experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Gets respawn rate.
     *
     * @return the respawn rate
     */
    public int getRespawnRate() {
        return respawnRate;
    }

    /**
     * Gets rate.
     *
     * @return the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return full;
    }

    /**
     * Gets minimum respawn.
     *
     * @return the minimum respawn
     */
    public int getMinimumRespawn() {
        return respawnRate & 0xFFFF;
    }

    /**
     * Gets maximum respawn.
     *
     * @return the maximum respawn
     */
    public int getMaximumRespawn() {
        return (respawnRate >> 16) & 0xFFFF;
    }

    /**
     * Gets respawn duration.
     *
     * @return the respawn duration
     */
    public int getRespawnDuration() {
        int minimum = respawnRate & 0xFFFF;
        int maximum = (respawnRate >> 16) & 0xFFFF;
        double playerRatio = (double) ServerConstants.MAX_PLAYERS / Repository.getPlayers().size();
        return (int) (minimum + ((maximum - minimum) / playerRatio));
    }
}
