package content.global.skill.gather.woodcutting

import core.ServerConstants
import core.game.world.repository.Repository.players
import shared.consts.Scenery

/**
 * Represents the woodcutting node.
 */
enum class WoodcuttingNode {
    STANDARD_TREE_1(Scenery.TREE_1276, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_2(Scenery.TREE_1277, Scenery.TREE_STUMP_1343, 1.toByte()),
    STANDARD_TREE_3(Scenery.TREE_1278, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_4(Scenery.TREE_1279, Scenery.TREE_STUMP_1345, 1.toByte()),
    STANDARD_TREE_5(Scenery.TREE_1280, Scenery.TREE_STUMP_1343, 1.toByte()),
    STANDARD_TREE_6(Scenery.TREE_1330, Scenery.TREE_STUMP_1341, 1.toByte()),
    STANDARD_TREE_7(Scenery.TREE_1331, Scenery.TREE_STUMP_1341, 1.toByte()),
    STANDARD_TREE_8(Scenery.TREE_1332, Scenery.TREE_STUMP_1341, 1.toByte()),
    STANDARD_TREE_9(Scenery.TREE_2409, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_10(Scenery.TUTORIAL_TREE_3033, Scenery.TREE_STUMP_1345, 1.toByte()),
    STANDARD_TREE_11(Scenery.TREE_3034, Scenery.TREE_STUMP_1345, 1.toByte()),
    STANDARD_TREE_12(Scenery.TREE_3035, Scenery.TREE_STUMP_1347, 1.toByte()),
    STANDARD_TREE_13(Scenery.TREE_3036, Scenery.TREE_STUMP_1351, 1.toByte()),
    STANDARD_TREE_14(Scenery.TREE_3879, Scenery.TREE_STUMP_3880, 1.toByte()),
    STANDARD_TREE_15(Scenery.TREE_3881, Scenery.TREE_STUMP_3880, 1.toByte()),
    STANDARD_TREE_16(Scenery.TREE_3882, Scenery.TREE_STUMP_3880, 1.toByte()),
    STANDARD_TREE_17(Scenery.TREE_3883, Scenery.TREE_STUMP_3884, 1.toByte()),
    STANDARD_TREE_18(Scenery.TREE_10041, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_19(Scenery.TREE_14308, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_20(Scenery.TREE_14309, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_21(Scenery.TREE_16264, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_22(Scenery.TREE_16265, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_23(Scenery.TREE_30132, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_24(Scenery.TREE_30133, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_25(Scenery.TREE_37477, Scenery.TREE_STUMP_1342, 1.toByte()),
    STANDARD_TREE_26(Scenery.TREE_37478, Scenery.TREE_STUMP_37653, 1.toByte()),
    STANDARD_TREE_27(Scenery.TREE_37652, Scenery.TREE_STUMP_37653, 1.toByte()),
    DEAD_TREE_1(Scenery.DEAD_TREE_1282, Scenery.TREE_STUMP_1347, 2.toByte()),
    DEAD_TREE_2(Scenery.DEAD_TREE_1283, Scenery.TREE_STUMP_1347, 2.toByte()),
    DEAD_TREE_3(Scenery.DEAD_TREE_1284, Scenery.TREE_STUMP_1348, 2.toByte()),
    DEAD_TREE_4(Scenery.DEAD_TREE_1285, Scenery.TREE_STUMP_1349, 2.toByte()),
    DEAD_TREE_5(Scenery.DEAD_TREE_1286, Scenery.TREE_STUMP_1351, 2.toByte()),
    DEAD_TREE_6(Scenery.DEAD_TREE_1289, Scenery.TREE_STUMP_1353, 2.toByte()),
    DEAD_TREE_7(Scenery.DEAD_TREE_1290, Scenery.TREE_STUMP_1354, 2.toByte()),
    DEAD_TREE_8(Scenery.DEAD_TREE_1291, Scenery.TREE_STUMP_23054, 2.toByte()),
    DEAD_TREE_9(Scenery.DEAD_TREE_1365, Scenery.TREE_STUMP_1352, 2.toByte()),
    DEAD_TREE_10(Scenery.DEAD_TREE_1383, Scenery.TREE_STUMP_1358, 2.toByte()),
    DEAD_TREE_11(Scenery.DEAD_TREE_1384, Scenery.TREE_STUMP_1359, 2.toByte()),
    DEAD_TREE_12(Scenery.DEAD_TREE_5902, Scenery.TREE_STUMP_1347, 2.toByte()),
    DEAD_TREE_13(Scenery.DEAD_TREE_5903, Scenery.TREE_STUMP_1353, 2.toByte()),
    DEAD_TREE_14(Scenery.DEAD_TREE_5904, Scenery.TREE_STUMP_1353, 2.toByte()),
    DEAD_TREE_15(Scenery.DEAD_TREE_32294, Scenery.TREE_STUMP_1353, 2.toByte()),
    DEAD_TREE_16(Scenery.DEAD_TREE_37481, Scenery.TREE_STUMP_1347, 2.toByte()),
    DEAD_TREE_17(Scenery.DEAD_TREE_37482, Scenery.TREE_STUMP_1351, 2.toByte()),
    DEAD_TREE_18(Scenery.DEAD_TREE_37483, Scenery.TREE_STUMP_1358, 2.toByte()),
    DEAD_TREE_19(Scenery.DYING_TREE_24168, Scenery.DYING_TREE_STUMP_24169, 2.toByte()),
    EVERGREEN_1(Scenery.EVERGREEN_1315, Scenery.TREE_STUMP_1342, 3.toByte()),
    EVERGREEN_2(Scenery.EVERGREEN_1316, Scenery.TREE_STUMP_1355, 3.toByte()),
    EVERGREEN_3(Scenery.EVERGREEN_1318, Scenery.TREE_STUMP_1355, 3.toByte()),
    EVERGREEN_4(Scenery.EVERGREEN_1319, Scenery.TREE_STUMP_1355, 3.toByte()),
    JUNGLE_TREE_1(Scenery.JUNGLE_TREE_2887, 0, 4.toByte()),
    JUNGLE_TREE_2(Scenery.JUNGLE_TREE_2889, Scenery.JUNGLE_TREE_STUMP_4819, 4.toByte()),
    JUNGLE_TREE_3(Scenery.JUNGLE_TREE_2890, Scenery.JUNGLE_TREE_STUMP_4821, 4.toByte()),
    JUNGLE_TREE_4(Scenery.JUNGLE_TREE_4818, Scenery.JUNGLE_TREE_STUMP_4819, 4.toByte()),
    JUNGLE_TREE_5(Scenery.JUNGLE_TREE_4820, Scenery.JUNGLE_TREE_STUMP_4821, 4.toByte()),
    JUNGLE_BUSH_1(Scenery.JUNGLE_BUSH_2892, Scenery.SLASHED_BUSH_2894, 5.toByte()),
    JUNGLE_BUSH_2(Scenery.JUNGLE_BUSH_2893, Scenery.SLASHED_BUSH_2895, 5.toByte()),
    ACHEY_TREE(Scenery.ACHEY_TREE_2023, Scenery.ACHEY_TREE_STUMP_3371, 6.toByte()),
    OAK_TREE_1(Scenery.OAK_1281, Scenery.TREE_STUMP_1356, 7.toByte()),
    OAK_TREE_2(Scenery.OAK_3037, Scenery.TREE_STUMP_1357, 7.toByte()),
    OAK_TREE_3(Scenery.OAK_37479, Scenery.TREE_STUMP_1356, 7.toByte()),
    OAK_TREE_4(Scenery.OAK_8467, Scenery.TREE_STUMP_1356, 19.toByte(), true),
    WILLOW_TREE_1(Scenery.WILLOW_1308, Scenery.TREE_STUMP_7399, 8.toByte()),
    WILLOW_TREE_2(Scenery.WILLOW_5551, Scenery.TREE_STUMP_5554, 8.toByte()),
    WILLOW_TREE_3(Scenery.WILLOW_5552, Scenery.TREE_STUMP_5554, 8.toByte()),
    WILLOW_TREE_4(Scenery.WILLOW_5553, Scenery.TREE_STUMP_5554, 8.toByte()),
    WILLOW_TREE_5(Scenery.WILLOW_37480, Scenery.TREE_STUMP_7399, 8.toByte()),
    WILLOW_TREE_6(Scenery.WILLOW_TREE_8488, Scenery.TREE_STUMP_7399, 20.toByte(), true),
    TEAK_1(Scenery.TEAK_9036, Scenery.TREE_STUMP_9037, 9.toByte()),
    TEAK_2(Scenery.TEAK_15062, Scenery.TREE_STUMP_9037, 9.toByte()),
    MAPLE_TREE_1(Scenery.MAPLE_TREE_1307, Scenery.TREE_STUMP_7400, 10.toByte()),
    MAPLE_TREE_2(Scenery.MAPLE_TREE_4674, Scenery.TREE_STUMP_7400, 10.toByte()),
    MAPLE_TREE_3(Scenery.MAPLE_TREE_8444, Scenery.TREE_STUMP_7400, 21.toByte(), true),
    HOLLOW_TREE_1(Scenery.HOLLOW_TREE_2289, Scenery.TREE_STUMP_2310, 11.toByte()),
    HOLLOW_TREE_2(Scenery.HOLLOW_TREE_4060, Scenery.TREE_STUMP_4061, 11.toByte()),
    MAHOGANY(Scenery.MAHOGANY_9034, Scenery.TREE_STUMP_9035, 12.toByte()),
    SWAYING_TREE(Scenery.SWAYING_TREE_4142, -1, 30.toByte()),
    ARCTIC_PINE(Scenery.ARCTIC_PINE_21273, Scenery.TREE_STUMP_21274, 13.toByte()),
    EUCALYPTUS_1(Scenery.EUCALYPTUS_TREE_28951, Scenery.EUCALYPTUS_STUMP_28954, 14.toByte()),
    EUCALYPTUS_2(Scenery.EUCALYPTUS_TREE_28952, Scenery.EUCALYPTUS_STUMP_28955, 14.toByte()),
    EUCALYPTUS_3(Scenery.EUCALYPTUS_TREE_28953, Scenery.EUCALYPTUS_STUMP_28956, 14.toByte()),
    YEW(Scenery.YEW_1309, Scenery.TREE_STUMP_7402, 15.toByte()),
    YEW_1(Scenery.YEW_TREE_8513, Scenery.TREE_STUMP_7402, 22.toByte(), true),
    MAGIC_TREE_1(Scenery.MAGIC_TREE_1306, Scenery.TREE_STUMP_7401, 16.toByte()),
    MAGIC_TREE_2(Scenery.MAGIC_TREE_37823, Scenery.TREE_STUMP_37824, 16.toByte()),
    MAGIC_TREE_3(Scenery.MAGIC_TREE_8409, Scenery.TREE_STUMP_37824, 23.toByte(), true),
    CURSED_MAGIC_TREE(Scenery.CURSED_MAGIC_TREE_37821, Scenery.TREE_STUMP_37822, 17.toByte()),
    DRAMEN_TREE(Scenery.DRAMEN_TREE_1292, 771, 18.toByte()),
    WINDSWEPT_TREE(Scenery.WINDSWEPT_TREE_18137, Scenery.TREE_STUMP_1353, 19.toByte()),
    LIGHT_JUNGLE_1(Scenery.LIGHT_JUNGLE_9010, Scenery.LIGHT_JUNGLE_9010, 31.toByte()),
    LIGHT_JUNGLE_2(Scenery.LIGHT_JUNGLE_9011, Scenery.LIGHT_JUNGLE_9010, 31.toByte()),
    LIGHT_JUNGLE_3(Scenery.LIGHT_JUNGLE_9012, Scenery.LIGHT_JUNGLE_9010, 31.toByte()),
    LIGHT_JUNGLE_4(Scenery.LIGHT_JUNGLE_9013, Scenery.LIGHT_JUNGLE_9010, 31.toByte()),
    MEDIUM_JUNGLE_1(Scenery.MEDIUM_JUNGLE_9015, Scenery.MEDIUM_JUNGLE_9015, 32.toByte()),
    MEDIUM_JUNGLE_2(Scenery.MEDIUM_JUNGLE_9016, Scenery.MEDIUM_JUNGLE_9015, 32.toByte()),
    MEDIUM_JUNGLE_3(Scenery.MEDIUM_JUNGLE_9017, Scenery.MEDIUM_JUNGLE_9015, 32.toByte()),
    MEDIUM_JUNGLE_4(Scenery.MEDIUM_JUNGLE_9018, Scenery.MEDIUM_JUNGLE_9015, 32.toByte()),
    DENSE_JUNGLE_1(Scenery.DENSE_JUNGLE_9020, Scenery.DENSE_JUNGLE_9020, 33.toByte()),
    DENSE_JUNGLE_2(Scenery.DENSE_JUNGLE_9021, Scenery.DENSE_JUNGLE_9020, 33.toByte()),
    DENSE_JUNGLE_3(Scenery.DENSE_JUNGLE_9022, Scenery.DENSE_JUNGLE_9020, 33.toByte()),
    DENSE_JUNGLE_4(Scenery.DENSE_JUNGLE_9023, Scenery.DENSE_JUNGLE_9020, 33.toByte());

    /**
     * The Full.
     */
    var id: Int

    /**
     * Empty woodcutting node.
     */
    var emptyId: Int

    /**
     * Reward woodcutting node.
     */
    var reward: Int = 0

    /**
     * Respawn rate woodcutting node.
     */
    var respawnRate: Int = 0

    /**
     * Level woodcutting node.
     */
    var level: Int = 0

    /**
     * Reward amount woodcutting node.
     */
    var rewardAmount: Int = 0

    /**
     * The Experience.
     */
    var experience: Double = 0.0

    /**
     * Rate woodcutting node.
     */
    var rate: Double = 0.0

    /**
     * The Identifier.
     */
    var identifier: Byte

    /**
     * The Farming.
     */
    var isFarming: Boolean

    /**
     * The Base low.
     */
    var baseLow: Double = 2.0

    /**
     * The Base high.
     */
    var baseHigh: Double = 6.0

    /**
     * The Tier mod low.
     */
    var tierModLow: Double = 1.0

    /**
     * The Tier mod high.
     */
    var tierModHigh: Double = 3.0

    constructor(full: Int, empty: Int, identifier: Byte) {
        this.id = full
        this.emptyId = empty
        this.identifier = identifier
        this.isFarming = false
        this.rewardAmount = 1
        when (identifier.toInt() and 0xFF) {
            1, 2, 3, 4 -> {
                reward = 1511
                respawnRate = 50 or (100 shl 16)
                rate = 0.05
                experience = 25.0
                level = 1
                baseLow = 64.0
                baseHigh = 200.0
                tierModLow = 32.0
                tierModHigh = 100.0
            }

            5 -> {
                reward = 1511
                respawnRate = 50 or (100 shl 16)
                rate = 0.15
                experience = 100.0
                level = 1
                this.rewardAmount = 2
                baseLow = 64.0
                baseHigh = 200.0
                tierModLow = 32.0
                tierModHigh = 100.0
            }

            6 -> {
                reward = 2862
                respawnRate = 50 or (100 shl 16)
                rate = 0.05
                experience = 25.0
                level = 1
                baseLow = 64.0
                baseHigh = 200.0
                tierModLow = 32.0
                tierModHigh = 100.0
            }

            7 -> {
                reward = 1521
                respawnRate = 14 or (22 shl 16)
                rate = 0.15
                experience = 37.5
                level = 15
                rewardAmount = 10
                baseLow = 32.0
                baseHigh = 100.0
                tierModLow = 16.0
                tierModHigh = 50.0
            }

            8 -> {
                reward = 1519
                respawnRate = 14 or (22 shl 16)
                rate = 0.3
                experience = 67.8
                level = 30
                rewardAmount = 20
                baseLow = 16.0
                baseHigh = 50.0
                tierModLow = 8.0
                tierModHigh = 25.0
            }

            9 -> {
                reward = 6333
                respawnRate = 35 or (60 shl 16)
                rate = 0.7
                experience = 85.0
                level = 35
                rewardAmount = 25
                baseLow = 15.0
                baseHigh = 46.0
                tierModLow = 8.0
                tierModHigh = 23.5
            }

            10 -> {
                reward = 1517
                respawnRate = 58 or (100 shl 16)
                rate = 0.65
                experience = 100.0
                level = 45
                rewardAmount = 30
                baseLow = 8.0
                baseHigh = 25.0
                tierModLow = 4.0
                tierModHigh = 12.5
            }

            11 -> {
                reward = 3239
                respawnRate = 58 or (100 shl 16)
                rate = 0.6
                experience = 82.5
                level = 45
                rewardAmount = 30
                baseLow = 18.0
                baseHigh = 26.0
                tierModLow = 10.0
                tierModHigh = 14.0
            }

            12 -> {
                reward = 6332
                respawnRate = 62 or (115 shl 16)
                rate = 0.7
                experience = 125.0
                level = 50
                rewardAmount = 35
                baseLow = 8.0
                baseHigh = 25.0
                tierModLow = 4.0
                tierModHigh = 12.5
            }

            13 -> {
                reward = 10810
                respawnRate = 75 or (130 shl 16)
                rate = 0.73
                experience = 40.0
                level = 54
                rewardAmount = 35
                baseLow = 6.0
                baseHigh = 30.0
                tierModLow = 3.0
                tierModHigh = 13.5
            }

            14 -> {
                reward = 12581
                respawnRate = 80 or (140 shl 16)
                rate = 0.77
                experience = 165.0
                level = 58
                rewardAmount = 35
            }

            15 -> {
                reward = 1515
                respawnRate = 100 or (162 shl 16)
                rate = 0.8
                experience = 175.0
                level = 60
                rewardAmount = 40
                baseLow = 4.0
                baseHigh = 12.5
                tierModLow = 2.0
                tierModHigh = 6.25
            }

            16 -> {
                reward = 1513
                respawnRate = 200 or (317 shl 16)
                rate = 0.9
                experience = 250.0
                level = 75
                rewardAmount = 50
                baseLow = 2.0
                baseHigh = 6.0
                tierModLow = 1.0
                tierModHigh = 3.0
            }

            17 -> {
                reward = 1513
                respawnRate = 200 or (317 shl 16)
                rate = 0.95
                experience = 275.0
                level = 82
                rewardAmount = 50
            }

            18 -> {
                reward = 771
                respawnRate = -1
                rate = 0.05
                experience = 25.0
                level = 36
                rewardAmount = Int.MAX_VALUE
                baseLow = 255.0
                baseHigh = 255.0
                tierModLow = 0.0
                tierModHigh = 0.0
            }

            30 -> {
                reward = 3692
                respawnRate = -1
                rate = 0.05
                experience = 1.0
                level = 40
                rewardAmount = Int.MAX_VALUE
            }
        }
    }

    constructor(full: Int, empty: Int, identifier: Byte, farming: Boolean) {
        this.id = full
        this.emptyId = empty
        this.identifier = identifier
        this.isFarming = farming
        when (identifier.toInt() and 0xFF) {
            19 -> {
                reward = 1521
                respawnRate = 14 or (22 shl 16)
                rate = 0.15
                experience = 37.5
                level = 15
                rewardAmount = 10
                baseLow = 32.0
                baseHigh = 100.0
                tierModLow = 16.0
                tierModHigh = 50.0
            }

            20 -> {
                reward = 1519
                respawnRate = 14 or (22 shl 16)
                rate = 0.3
                experience = 67.8
                level = 30
                rewardAmount = 20
                baseLow = 16.0
                baseHigh = 50.0
                tierModLow = 8.0
                tierModHigh = 25.0
            }

            21 -> {
                reward = 1517
                respawnRate = 58 or (100 shl 16)
                rate = 0.65
                experience = 100.0
                level = 45
                rewardAmount = 30
                baseLow = 8.0
                baseHigh = 25.0
                tierModLow = 4.0
                tierModHigh = 12.5
            }

            22 -> {
                reward = 1515
                respawnRate = 100 or (162 shl 16)
                rate = 0.8
                experience = 175.0
                level = 60
                rewardAmount = 40
                baseLow = 4.0
                baseHigh = 12.5
                tierModLow = 2.0
                tierModHigh = 6.25
            }

            23 -> {
                reward = 1513
                respawnRate = 200 or (317 shl 16)
                rate = 0.9
                experience = 250.0
                level = 75
                rewardAmount = 50
                baseLow = 2.0
                baseHigh = 6.0
                tierModLow = 1.0
                tierModHigh = 3.0
            }

            31 -> {
                reward = 6281
                respawnRate = 200 or (317 shl 16)
                rate = 0.2
                experience = 32.0
                level = 10
                rewardAmount = 50
                baseLow = 0.0
                baseHigh = 9.5
                tierModLow = 0.065
                tierModHigh = 0.25
            }

            32 -> {
                reward = 6283
                respawnRate = 200 or (317 shl 16)
                rate = 0.2
                experience = 55.0
                level = 20
                rewardAmount = 50
                baseLow = 0.0
                baseHigh = 8.0
                tierModLow = 0.065
                tierModHigh = 0.25
            }

            33 -> {
                reward = 6285
                respawnRate = 200 or (317 shl 16)
                rate = 0.2
                experience = 80.0
                level = 35
                rewardAmount = 50
                baseLow = 0.0
                baseHigh = 6.0
                tierModLow = 0.06
                tierModHigh = 0.25
            }
        }
    }

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
        private val NODE_MAP = HashMap<Int, WoodcuttingNode>()
        private val EMPTY_MAP = HashMap<Int, Int?>()

        init {
            for (node in values()) {
                NODE_MAP.putIfAbsent(node.id, node)
                EMPTY_MAP.putIfAbsent(node.emptyId, node.id)
            }
        }

        /**
         * For id woodcutting node.
         *
         * @param id the id
         * @return the woodcutting node
         */
        fun forId(id: Int): WoodcuttingNode? {
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
