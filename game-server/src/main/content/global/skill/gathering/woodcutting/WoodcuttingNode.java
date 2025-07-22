package content.global.skill.gathering.woodcutting;

import core.ServerConstants;
import core.game.world.repository.Repository;
import org.rs.consts.Scenery;

import java.util.HashMap;

/**
 * The enum Woodcutting node.
 */
public enum WoodcuttingNode {
    /**
     * Standard tree 1 woodcutting node.
     */
    STANDARD_TREE_1(Scenery.TREE_1276, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 2 woodcutting node.
     */
    STANDARD_TREE_2( Scenery.TREE_1277, Scenery.TREE_STUMP_1343,  (byte) 1),
    /**
     * Standard tree 3 woodcutting node.
     */
    STANDARD_TREE_3( Scenery.TREE_1278, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 4 woodcutting node.
     */
    STANDARD_TREE_4( Scenery.TREE_1279, Scenery.TREE_STUMP_1345,  (byte) 1),
    /**
     * Standard tree 5 woodcutting node.
     */
    STANDARD_TREE_5( Scenery.TREE_1280, Scenery.TREE_STUMP_1343,  (byte) 1),
    /**
     * Standard tree 6 woodcutting node.
     */
    STANDARD_TREE_6( Scenery.TREE_1330, Scenery.TREE_STUMP_1341,  (byte) 1),
    /**
     * Standard tree 7 woodcutting node.
     */
    STANDARD_TREE_7( Scenery.TREE_1331, Scenery.TREE_STUMP_1341,  (byte) 1),
    /**
     * Standard tree 8 woodcutting node.
     */
    STANDARD_TREE_8( Scenery.TREE_1332, Scenery.TREE_STUMP_1341,  (byte) 1),
    /**
     * Standard tree 9 woodcutting node.
     */
    STANDARD_TREE_9( Scenery.TREE_2409, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 10 woodcutting node.
     */
    STANDARD_TREE_10(Scenery.TUTORIAL_TREE_3033, Scenery.TREE_STUMP_1345,  (byte) 1),
    /**
     * Standard tree 11 woodcutting node.
     */
    STANDARD_TREE_11(Scenery.TREE_3034, Scenery.TREE_STUMP_1345,  (byte) 1),
    /**
     * Standard tree 12 woodcutting node.
     */
    STANDARD_TREE_12(Scenery.TREE_3035, Scenery.TREE_STUMP_1347,  (byte) 1),
    /**
     * Standard tree 13 woodcutting node.
     */
    STANDARD_TREE_13(Scenery.TREE_3036, Scenery.TREE_STUMP_1351,  (byte) 1),
    /**
     * Standard tree 14 woodcutting node.
     */
    STANDARD_TREE_14(Scenery.TREE_3879, Scenery.TREE_STUMP_3880,  (byte) 1),
    /**
     * Standard tree 15 woodcutting node.
     */
    STANDARD_TREE_15(Scenery.TREE_3881, Scenery.TREE_STUMP_3880,  (byte) 1),
    /**
     * Standard tree 16 woodcutting node.
     */
    STANDARD_TREE_16(Scenery.TREE_3882, Scenery.TREE_STUMP_3880,  (byte) 1),
    /**
     * Standard tree 17 woodcutting node.
     */
    STANDARD_TREE_17(Scenery.TREE_3883, Scenery.TREE_STUMP_3884,  (byte) 1),
    /**
     * Standard tree 18 woodcutting node.
     */
    STANDARD_TREE_18(Scenery.TREE_10041, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 19 woodcutting node.
     */
    STANDARD_TREE_19(Scenery.TREE_14308, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 20 woodcutting node.
     */
    STANDARD_TREE_20(Scenery.TREE_14309, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 21 woodcutting node.
     */
    STANDARD_TREE_21(Scenery.TREE_16264, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 22 woodcutting node.
     */
    STANDARD_TREE_22(Scenery.TREE_16265, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 23 woodcutting node.
     */
    STANDARD_TREE_23(Scenery.TREE_30132, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 24 woodcutting node.
     */
    STANDARD_TREE_24(Scenery.TREE_30133, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 25 woodcutting node.
     */
    STANDARD_TREE_25(Scenery.TREE_37477, Scenery.TREE_STUMP_1342,  (byte) 1),
    /**
     * Standard tree 26 woodcutting node.
     */
    STANDARD_TREE_26(Scenery.TREE_37478, Scenery.TREE_STUMP_37653, (byte) 1),
    /**
     * Standard tree 27 woodcutting node.
     */
    STANDARD_TREE_27(Scenery.TREE_37652, Scenery.TREE_STUMP_37653, (byte) 1),

    /**
     * Dead tree 1 woodcutting node.
     */
    DEAD_TREE_1( Scenery.DEAD_TREE_1282, Scenery.TREE_STUMP_1347,  (byte) 2),
    /**
     * Dead tree 2 woodcutting node.
     */
    DEAD_TREE_2( Scenery.DEAD_TREE_1283, Scenery.TREE_STUMP_1347,  (byte) 2),
    /**
     * Dead tree 3 woodcutting node.
     */
    DEAD_TREE_3( Scenery.DEAD_TREE_1284, Scenery.TREE_STUMP_1348,  (byte) 2),
    /**
     * Dead tree 4 woodcutting node.
     */
    DEAD_TREE_4( Scenery.DEAD_TREE_1285, Scenery.TREE_STUMP_1349,  (byte) 2),
    /**
     * Dead tree 5 woodcutting node.
     */
    DEAD_TREE_5( Scenery.DEAD_TREE_1286, Scenery.TREE_STUMP_1351,  (byte) 2),
    /**
     * Dead tree 6 woodcutting node.
     */
    DEAD_TREE_6( Scenery.DEAD_TREE_1289, Scenery.TREE_STUMP_1353,  (byte) 2),
    /**
     * Dead tree 7 woodcutting node.
     */
    DEAD_TREE_7( Scenery.DEAD_TREE_1290, Scenery.TREE_STUMP_1354,  (byte) 2),
    /**
     * Dead tree 8 woodcutting node.
     */
    DEAD_TREE_8( Scenery.DEAD_TREE_1291, Scenery.TREE_STUMP_23054, (byte) 2),
    /**
     * Dead tree 9 woodcutting node.
     */
    DEAD_TREE_9( Scenery.DEAD_TREE_1365, Scenery.TREE_STUMP_1352,  (byte) 2),
    /**
     * Dead tree 10 woodcutting node.
     */
    DEAD_TREE_10(Scenery.DEAD_TREE_1383, Scenery.TREE_STUMP_1358,  (byte) 2),
    /**
     * Dead tree 11 woodcutting node.
     */
    DEAD_TREE_11(Scenery.DEAD_TREE_1384, Scenery.TREE_STUMP_1359,  (byte) 2),
    /**
     * Dead tree 12 woodcutting node.
     */
    DEAD_TREE_12(Scenery.DEAD_TREE_5902, Scenery.TREE_STUMP_1347,  (byte) 2),
    /**
     * Dead tree 13 woodcutting node.
     */
    DEAD_TREE_13(Scenery.DEAD_TREE_5903, Scenery.TREE_STUMP_1353,  (byte) 2),
    /**
     * Dead tree 14 woodcutting node.
     */
    DEAD_TREE_14(Scenery.DEAD_TREE_5904, Scenery.TREE_STUMP_1353,  (byte) 2),
    /**
     * Dead tree 15 woodcutting node.
     */
    DEAD_TREE_15(Scenery.DEAD_TREE_32294,Scenery.TREE_STUMP_1353,  (byte) 2),
    /**
     * Dead tree 16 woodcutting node.
     */
    DEAD_TREE_16(Scenery.DEAD_TREE_37481,Scenery.TREE_STUMP_1347,  (byte) 2),
    /**
     * Dead tree 17 woodcutting node.
     */
    DEAD_TREE_17(Scenery.DEAD_TREE_37482,Scenery.TREE_STUMP_1351,  (byte) 2),
    /**
     * Dead tree 18 woodcutting node.
     */
    DEAD_TREE_18(Scenery.DEAD_TREE_37483,Scenery.TREE_STUMP_1358,  (byte) 2),
    /**
     * Dead tree 19 woodcutting node.
     */
    DEAD_TREE_19(Scenery.DYING_TREE_24168,Scenery.DYING_TREE_STUMP_24169, (byte) 2),

    /**
     * Evergreen 1 woodcutting node.
     */
    EVERGREEN_1(Scenery.EVERGREEN_1315,Scenery.TREE_STUMP_1342,(byte) 3),
    /**
     * Evergreen 2 woodcutting node.
     */
    EVERGREEN_2(Scenery.EVERGREEN_1316,Scenery.TREE_STUMP_1355,(byte) 3),
    /**
     * Evergreen 3 woodcutting node.
     */
    EVERGREEN_3(Scenery.EVERGREEN_1318,Scenery.TREE_STUMP_1355,(byte) 3),
    /**
     * Evergreen 4 woodcutting node.
     */
    EVERGREEN_4(Scenery.EVERGREEN_1319,Scenery.TREE_STUMP_1355,(byte) 3),

    /**
     * Jungle tree 1 woodcutting node.
     */
    JUNGLE_TREE_1(Scenery.JUNGLE_TREE_2887,0, (byte) 4),
    /**
     * Jungle tree 2 woodcutting node.
     */
    JUNGLE_TREE_2(Scenery.JUNGLE_TREE_2889,0, (byte) 4),
    /**
     * Jungle tree 3 woodcutting node.
     */
    JUNGLE_TREE_3(Scenery.JUNGLE_TREE_2890,0, (byte) 4),
    /**
     * Jungle tree 4 woodcutting node.
     */
    JUNGLE_TREE_4(Scenery.JUNGLE_TREE_4818,0, (byte) 4),
    /**
     * Jungle tree 5 woodcutting node.
     */
    JUNGLE_TREE_5(Scenery.JUNGLE_TREE_4820,0, (byte) 4),

    /**
     * Jungle bush 1 woodcutting node.
     */
    JUNGLE_BUSH_1(Scenery.JUNGLE_BUSH_2892,Scenery.SLASHED_BUSH_2894, (byte) 5),
    /**
     * Jungle bush 2 woodcutting node.
     */
    JUNGLE_BUSH_2(Scenery.JUNGLE_BUSH_2893,Scenery.SLASHED_BUSH_2895, (byte) 5),

    /**
     * Achey tree woodcutting node.
     */
    ACHEY_TREE(Scenery.ACHEY_TREE_2023,Scenery.ACHEY_TREE_STUMP_3371, (byte) 6),

    /**
     * Oak tree 1 woodcutting node.
     */
    OAK_TREE_1(Scenery.OAK_1281, Scenery.TREE_STUMP_1356, (byte) 7),
    /**
     * Oak tree 2 woodcutting node.
     */
    OAK_TREE_2(Scenery.OAK_3037, Scenery.TREE_STUMP_1357, (byte) 7),
    /**
     * Oak tree 3 woodcutting node.
     */
    OAK_TREE_3(Scenery.OAK_37479,Scenery.TREE_STUMP_1356, (byte) 7),
    /**
     * Oak tree 4 woodcutting node.
     */
    OAK_TREE_4(Scenery.OAK_8467, Scenery.TREE_STUMP_1356, (byte) 19, true),

    /**
     * Willow tree 1 woodcutting node.
     */
    WILLOW_TREE_1(Scenery.WILLOW_1308, Scenery.TREE_STUMP_7399, (byte) 8),
    /**
     * Willow tree 2 woodcutting node.
     */
    WILLOW_TREE_2(Scenery.WILLOW_5551, Scenery.TREE_STUMP_5554, (byte) 8),
    /**
     * Willow tree 3 woodcutting node.
     */
    WILLOW_TREE_3(Scenery.WILLOW_5552, Scenery.TREE_STUMP_5554, (byte) 8),
    /**
     * Willow tree 4 woodcutting node.
     */
    WILLOW_TREE_4(Scenery.WILLOW_5553, Scenery.TREE_STUMP_5554, (byte) 8),
    /**
     * Willow tree 5 woodcutting node.
     */
    WILLOW_TREE_5(Scenery.WILLOW_37480,Scenery.TREE_STUMP_7399, (byte) 8),
    /**
     * Willow tree 6 woodcutting node.
     */
    WILLOW_TREE_6(Scenery.WILLOW_TREE_8488,Scenery.TREE_STUMP_7399, (byte) 20, true),

    /**
     * Teak 1 woodcutting node.
     */
    TEAK_1(Scenery.TEAK_9036, Scenery.TREE_STUMP_9037, (byte) 9),
    /**
     * Teak 2 woodcutting node.
     */
    TEAK_2(Scenery.TEAK_15062,Scenery.TREE_STUMP_9037, (byte) 9),

    /**
     * Maple tree 1 woodcutting node.
     */
    MAPLE_TREE_1(Scenery.MAPLE_TREE_1307,Scenery.TREE_STUMP_7400, (byte) 10),
    /**
     * Maple tree 2 woodcutting node.
     */
    MAPLE_TREE_2(Scenery.MAPLE_TREE_4674,Scenery.TREE_STUMP_7400, (byte) 10),
    /**
     * Maple tree 3 woodcutting node.
     */
    MAPLE_TREE_3(Scenery.MAPLE_TREE_8444,Scenery.TREE_STUMP_7400, (byte) 21, true),

    /**
     * Hollow tree 1 woodcutting node.
     */
    HOLLOW_TREE_1(Scenery.HOLLOW_TREE_2289,Scenery.TREE_STUMP_2310, (byte) 11),
    /**
     * Hollow tree 2 woodcutting node.
     */
    HOLLOW_TREE_2(Scenery.HOLLOW_TREE_4060,Scenery.TREE_STUMP_4061, (byte) 11),

    /**
     * Mahogany woodcutting node.
     */
    MAHOGANY(Scenery.MAHOGANY_9034,Scenery.TREE_STUMP_9035, (byte) 12),

    /**
     * Swaying tree woodcutting node.
     */
    SWAYING_TREE(Scenery.SWAYING_TREE_4142,-1, (byte) 30),

    /**
     * Arctic pine woodcutting node.
     */
    ARCTIC_PINE(Scenery.ARCTIC_PINE_21273,Scenery.TREE_STUMP_21274, (byte) 13),

    /**
     * Eucalyptus 1 woodcutting node.
     */
    EUCALYPTUS_1(Scenery.EUCALYPTUS_TREE_28951,Scenery.EUCALYPTUS_STUMP_28954, (byte) 14),
    /**
     * Eucalyptus 2 woodcutting node.
     */
    EUCALYPTUS_2(Scenery.EUCALYPTUS_TREE_28952,Scenery.EUCALYPTUS_STUMP_28955, (byte) 14),
    /**
     * Eucalyptus 3 woodcutting node.
     */
    EUCALYPTUS_3(Scenery.EUCALYPTUS_TREE_28953,Scenery.EUCALYPTUS_STUMP_28956, (byte) 14),

    /**
     * Yew woodcutting node.
     */
    YEW(Scenery.YEW_1309,Scenery.TREE_STUMP_7402, (byte) 15),
    /**
     * Yew 1 woodcutting node.
     */
    YEW_1(Scenery.YEW_TREE_8513,Scenery.TREE_STUMP_7402,(byte) 22, true),

    /**
     * Magic tree 1 woodcutting node.
     */
    MAGIC_TREE_1(Scenery.MAGIC_TREE_1306,  Scenery.TREE_STUMP_7401, (byte) 16),
    /**
     * Magic tree 2 woodcutting node.
     */
    MAGIC_TREE_2(Scenery.MAGIC_TREE_37823,Scenery.TREE_STUMP_37824, (byte) 16),
    /**
     * Magic tree 3 woodcutting node.
     */
    MAGIC_TREE_3(Scenery.MAGIC_TREE_8409, Scenery.TREE_STUMP_37824, (byte) 23, true),

    /**
     * Cursed magic tree woodcutting node.
     */
    CURSED_MAGIC_TREE(Scenery.CURSED_MAGIC_TREE_37821,Scenery.TREE_STUMP_37822, (byte) 17),

    /**
     * Dramen tree woodcutting node.
     */
    DRAMEN_TREE(Scenery.DRAMEN_TREE_1292, -1, (byte) 18),

    /**
     * Windswept tree woodcutting node.
     */
    WINDSWEPT_TREE(Scenery.WINDSWEPT_TREE_18137, Scenery.TREE_STUMP_1353, (byte) 19),
    /**
     * Light jungle 1 woodcutting node.
     */
    LIGHT_JUNGLE_1(Scenery.LIGHT_JUNGLE_9010, Scenery.LIGHT_JUNGLE_9010, (byte) 31),
    /**
     * Light jungle 2 woodcutting node.
     */
    LIGHT_JUNGLE_2(Scenery.LIGHT_JUNGLE_9011, Scenery.LIGHT_JUNGLE_9010, (byte) 31),
    /**
     * Light jungle 3 woodcutting node.
     */
    LIGHT_JUNGLE_3(Scenery.LIGHT_JUNGLE_9012, Scenery.LIGHT_JUNGLE_9010, (byte) 31),
    /**
     * Light jungle 4 woodcutting node.
     */
    LIGHT_JUNGLE_4(Scenery.LIGHT_JUNGLE_9013, Scenery.LIGHT_JUNGLE_9010, (byte) 31),
    /**
     * Medium jungle 1 woodcutting node.
     */
    MEDIUM_JUNGLE_1(Scenery.MEDIUM_JUNGLE_9015, Scenery.MEDIUM_JUNGLE_9015, (byte) 32),
    /**
     * Medium jungle 2 woodcutting node.
     */
    MEDIUM_JUNGLE_2(Scenery.MEDIUM_JUNGLE_9016, Scenery.MEDIUM_JUNGLE_9015, (byte) 32),
    /**
     * Medium jungle 3 woodcutting node.
     */
    MEDIUM_JUNGLE_3(Scenery.MEDIUM_JUNGLE_9017, Scenery.MEDIUM_JUNGLE_9015, (byte) 32),
    /**
     * Medium jungle 4 woodcutting node.
     */
    MEDIUM_JUNGLE_4(Scenery.MEDIUM_JUNGLE_9018, Scenery.MEDIUM_JUNGLE_9015, (byte) 32),
    /**
     * Dense jungle 1 woodcutting node.
     */
    DENSE_JUNGLE_1(Scenery.DENSE_JUNGLE_9020, Scenery.DENSE_JUNGLE_9020, (byte) 33),
    /**
     * Dense jungle 2 woodcutting node.
     */
    DENSE_JUNGLE_2(Scenery.DENSE_JUNGLE_9021, Scenery.DENSE_JUNGLE_9020, (byte) 33),
    /**
     * Dense jungle 3 woodcutting node.
     */
    DENSE_JUNGLE_3(Scenery.DENSE_JUNGLE_9022, Scenery.DENSE_JUNGLE_9020, (byte) 33),
    /**
     * Dense jungle 4 woodcutting node.
     */
    DENSE_JUNGLE_4(Scenery.DENSE_JUNGLE_9023, Scenery.DENSE_JUNGLE_9020, (byte) 33);

    /**
     * The Full.
     */
    int full, /**
     * Empty woodcutting node.
     */
    empty, /**
     * Reward woodcutting node.
     */
    reward, /**
     * Respawn rate woodcutting node.
     */
    respawnRate, /**
     * Level woodcutting node.
     */
    level, /**
     * Reward amount woodcutting node.
     */
    rewardAmount;
    /**
     * The Experience.
     */
    double experience, /**
     * Rate woodcutting node.
     */
    rate;
    /**
     * The Identifier.
     */
    public byte identifier;
    /**
     * The Farming.
     */
    boolean farming;
    /**
     * The Base low.
     */
    public double baseLow = 2;
    /**
     * The Base high.
     */
    public double baseHigh = 6;
    /**
     * The Tier mod low.
     */
    public double tierModLow = 1;
    /**
     * The Tier mod high.
     */
    public double tierModHigh = 3;
    WoodcuttingNode(int full, int empty,byte identifier){
        this.full = full;
        this.empty = empty;
        this.identifier = identifier;
        this.farming = false;
        this.rewardAmount = 1;
        switch(identifier & 0xFF){
            case 1:
            case 2:
            case 3:
            case 4:
                reward = 1511;
                respawnRate = 50 | 100 << 16;
                rate = 0.05;
                experience = 25.0;
                level = 1;
                baseLow = 64;
                baseHigh = 200;
                tierModLow = 32;
                tierModHigh = 100;
                break;
            case 5:
                reward = 1511;
                respawnRate = 50 | 100 << 16;
                rate = 0.15;
                experience = 100;
                level = 1;
                this.rewardAmount = 2;
                baseLow = 64;
                baseHigh = 200;
                tierModLow = 32;
                tierModHigh = 100;
                break;
            case 6:
                reward = 2862;
                respawnRate = 50 | 100 << 16;
                rate = 0.05;
                experience = 25.0;
                level = 1;
                baseLow = 64;
                baseHigh = 200;
                tierModLow = 32;
                tierModHigh = 100;
                break;
            case 7:
                reward = 1521;
                respawnRate = 14 | 22 << 16;
                rate = 0.15;
                experience = 37.5;
                level = 15;
                rewardAmount = 10;
                baseLow = 32;
                baseHigh = 100;
                tierModLow = 16;
                tierModHigh = 50;
                break;
            case 8:
                reward = 1519;
                respawnRate = 14 | 22 << 16;
                rate = 0.3;
                experience = 67.8;
                level = 30;
                rewardAmount = 20;
                baseLow = 16;
                baseHigh = 50;
                tierModLow = 8;
                tierModHigh = 25;
                break;
            case 9:
                reward = 6333;
                respawnRate = 35 | 60 << 16;
                rate = 0.7;
                experience = 85.0;
                level = 35;
                rewardAmount = 25;
                baseLow = 15;
                baseHigh = 46;
                tierModLow = 8;
                tierModHigh = 23.5;
                break;
            case 10:
                reward = 1517;
                respawnRate = 58 | 100 << 16;
                rate = 0.65;
                experience = 100.0;
                level = 45;
                rewardAmount = 30;
                baseLow = 8;
                baseHigh = 25;
                tierModLow = 4;
                tierModHigh = 12.5;
                break;
            case 11:
                reward = 3239;
                respawnRate = 58 | 100 << 16;
                rate = 0.6;
                experience = 82.5;
                level = 45;
                rewardAmount = 30;
                baseLow = 18;
                baseHigh = 26;
                tierModLow = 10;
                tierModHigh = 14;
                break;
            case 12:
                reward = 6332;
                respawnRate = 62 | 115 << 16;
                rate = 0.7;
                experience = 125.0;
                level = 50;
                rewardAmount = 35;
                baseLow = 8;
                baseHigh = 25;
                tierModLow = 4;
                tierModHigh = 12.5;
                break;
            case 13:
                reward = 10810;
                respawnRate = 75 | 130 << 16;
                rate = 0.73;
                experience = 40.0;
                level = 54;
                rewardAmount = 35;
                baseLow = 6;
                baseHigh = 30;
                tierModLow = 3;
                tierModHigh = 13.5;
                break;
            case 14:
                reward = 12581;
                respawnRate = 80 | 140 << 16;
                rate = 0.77;
                experience = 165.0;
                level = 58;
                rewardAmount = 35;
                break;
            case 15:
                reward = 1515;
                respawnRate = 100 | 162 << 16;
                rate = 0.8;
                experience = 175.0;
                level = 60;
                rewardAmount = 40;
                baseLow = 4;
                baseHigh = 12.5;
                tierModLow = 2;
                tierModHigh = 6.25;
                break;
            case 16:
                reward = 1513;
                respawnRate = 200 | 317 << 16;
                rate = 0.9;
                experience = 250.0;
                level = 75;
                rewardAmount = 50;
                baseLow = 2;
                baseHigh = 6;
                tierModLow = 1;
                tierModHigh = 3;
                break;
            case 17:
                reward = 1513;
                respawnRate = 200 | 317 << 16;
                rate = 0.95;
                experience = 275.0;
                level = 82;
                rewardAmount = 50;
                break;
            case 18:
                reward = 771;
                respawnRate = -1;
                rate = 0.05;
                experience = 25.0;
                level = 36;
                rewardAmount = Integer.MAX_VALUE;
                baseLow = 255;
                baseHigh = 255;
                tierModLow = 0;
                tierModHigh = 0;
                break;
            case 30:
                reward = 3692;
                respawnRate = -1;
                rate = 0.05;
                experience = 1;
                level = 40;
                rewardAmount = Integer.MAX_VALUE;
                break;
        }
    }
    WoodcuttingNode(int full, int empty, byte identifier, boolean farming){
        this.full = full;
        this.empty = empty;
        this.identifier = identifier;
        this.farming = farming;
        switch(identifier & 0xFF){
            case 19:
                reward = 1521;
                respawnRate = 14 | 22 << 16;
                rate = 0.15;
                experience = 37.5;
                level = 15;
                rewardAmount = 10;
                baseLow = 32;
                baseHigh = 100;
                tierModLow = 16;
                tierModHigh = 50;
                break;
            case 20:
                reward = 1519;
                respawnRate = 14 | 22 << 16;
                rate = 0.3;
                experience = 67.8;
                level = 30;
                rewardAmount = 20;
                baseLow = 16;
                baseHigh = 50;
                tierModLow = 8;
                tierModHigh = 25;
                break;
            case 21:
                reward = 1517;
                respawnRate = 58 | 100 << 16;
                rate = 0.65;
                experience = 100.0;
                level = 45;
                rewardAmount = 30;
                baseLow = 8;
                baseHigh = 25;
                tierModLow = 4;
                tierModHigh = 12.5;
                break;
            case 22:
                reward = 1515;
                respawnRate = 100 | 162 << 16;
                rate = 0.8;
                experience = 175.0;
                level = 60;
                rewardAmount = 40;
                baseLow = 4;
                baseHigh = 12.5;
                tierModLow = 2;
                tierModHigh = 6.25;
                break;
            case 23:
                reward = 1513;
                respawnRate = 200 | 317 << 16;
                rate = 0.9;
                experience = 250.0;
                level = 75;
                rewardAmount = 50;
                baseLow = 2;
                baseHigh = 6;
                tierModLow = 1;
                tierModHigh = 3;
                break;
            case 31:
                reward = 6281;
                respawnRate = 200 | (317 << 16);
                rate = 0.2;
                experience = 32.0;
                level = 10;
                rewardAmount = 50;
                baseLow = 0.0;
                baseHigh = 9.5;
                tierModLow = 0.065;
                tierModHigh = 0.25;
                break;
            case 32:
                reward = 6283;
                respawnRate = 200 | (317 << 16);
                rate = 0.2;
                experience = 55.0;
                level = 20;
                rewardAmount = 50;
                baseLow = 0.0;
                baseHigh = 8.0;
                tierModLow = 0.065;
                tierModHigh = 0.25;
                break;
            case 33:
                reward = 6285;
                respawnRate = 200 | (317 << 16);
                rate = 0.2;
                experience = 80.0;
                level = 35;
                rewardAmount = 50;
                baseLow = 0.0;
                baseHigh = 6.0;
                tierModLow = 0.06;
                tierModHigh = 0.25;
                break;
        }
    }

    private static HashMap<Integer, WoodcuttingNode> NODE_MAP = new HashMap<>();
    private static HashMap<Integer, Integer> EMPTY_MAP = new HashMap<>();
    static{
        for(WoodcuttingNode node : WoodcuttingNode.values()){
            NODE_MAP.putIfAbsent(node.full,node);
            EMPTY_MAP.putIfAbsent(node.empty,node.full);
        }
    }

    /**
     * For id woodcutting node.
     *
     * @param id the id
     * @return the woodcutting node
     */
    public static WoodcuttingNode forId(int id){
        return NODE_MAP.get(id);
    }

    /**
     * Is empty boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public static boolean isEmpty(int id){
        return EMPTY_MAP.get(id) != null;
    }

    /**
     * Gets reward amount.
     *
     * @return the reward amount
     */
    public int getRewardAmount() {
        return rewardAmount;
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
     * Is farming boolean.
     *
     * @return the boolean
     */
    public boolean isFarming(){ return farming;}

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
