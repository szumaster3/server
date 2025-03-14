package content.global.skill.gathering.fishing

import core.api.asItem
import core.game.node.item.Item
import org.rs.consts.Items

enum class Fish(
    val id: Int,
    val level: Int,
    val experience: Double,
    val lowChance: Double,
    val highChance: Double,
) {
    SWAMP_WEED(
        id = Items.SWAMP_WEED_10978,
        level = 1,
        experience = 1.0,
        lowChance = 0.121,
        highChance = 0.16,
    ),
    CRAYFISH(
        id = Items.RAW_CRAYFISH_13435,
        level = 1,
        experience = 10.0,
        lowChance = 0.15,
        highChance = 0.5,
    ),
    SHRIMP(
        id = Items.RAW_SHRIMPS_317,
        level = 1,
        experience = 10.0,
        lowChance = 0.191,
        highChance = 0.5,
    ),
    SARDINE(
        id = Items.RAW_SARDINE_327,
        level = 5,
        experience = 20.0,
        lowChance = 0.148,
        highChance = 0.374,
    ),
    KARAMBWANJI(
        id = Items.RAW_KARAMBWANJI_3150,
        level = 5,
        experience = 5.0,
        lowChance = 0.4,
        highChance = 0.98,
    ),
    HERRING(
        id = Items.RAW_HERRING_345,
        level = 10,
        experience = 30.0,
        lowChance = 0.129,
        highChance = 0.504,
    ),
    ANCHOVIE(
        id = Items.RAW_ANCHOVIES_321,
        level = 15,
        experience = 40.0,
        lowChance = 0.098,
        highChance = 0.5,
    ),
    MACKEREL(
        id = Items.RAW_MACKEREL_353,
        level = 16,
        experience = 20.0,
        lowChance = 0.055,
        highChance = 0.258,
    ),
    TROUT(
        id = Items.RAW_TROUT_335,
        level = 20,
        experience = 50.0,
        lowChance = 0.246,
        highChance = 0.468,
    ),
    COD(
        id = Items.RAW_COD_341,
        level = 23,
        experience = 45.0,
        lowChance = 0.063,
        highChance = 0.219,
    ),
    PIKE(
        id = Items.RAW_PIKE_349,
        level = 25,
        experience = 60.0,
        lowChance = 0.14,
        highChance = 0.379,
    ),
    SLIMY_EEL(
        id = Items.SLIMY_EEL_3379,
        level = 28,
        experience = 65.0,
        lowChance = 0.117,
        highChance = 0.216,
    ),
    SALMON(
        id = Items.RAW_SALMON_331,
        level = 30,
        experience = 70.0,
        lowChance = 0.156,
        highChance = 0.378,
    ),
    FROG_SPAWN(
        id = Items.FROG_SPAWN_5004,
        level = 33,
        experience = 75.0,
        lowChance = 0.164,
        highChance = 0.379,
    ),
    TUNA(
        id = Items.RAW_TUNA_359,
        level = 35,
        experience = 80.0,
        lowChance = 0.109,
        highChance = 0.205,
    ),
    RAINBOW_FISH(
        id = Items.RAW_RAINBOW_FISH_10138,
        level = 38,
        experience = 80.0,
        lowChance = 0.113,
        highChance = 0.254,
    ),
    CAVE_EEL(
        id = Items.RAW_CAVE_EEL_5001,
        level = 38,
        experience = 80.0,
        lowChance = 0.145,
        highChance = 0.316,
    ),
    LOBSTER(
        id = Items.RAW_LOBSTER_377,
        level = 40,
        experience = 90.0,
        lowChance = 0.16,
        highChance = 0.375,
    ),
    BASS(
        id = Items.RAW_BASS_363,
        level = 46,
        experience = 100.0,
        lowChance = 0.078,
        highChance = 0.16,
    ),
    SWORDFISH(
        id = Items.RAW_SWORDFISH_371,
        level = 50,
        experience = 100.0,
        lowChance = 0.105,
        highChance = 0.191,
    ),
    LAVA_EEL(
        id = Items.RAW_LAVA_EEL_2148,
        level = 53,
        experience = 30.0,
        lowChance = 0.227,
        highChance = 0.379,
    ),
    MONKFISH(
        id = Items.RAW_MONKFISH_7944,
        level = 62,
        experience = 120.0,
        lowChance = 0.293,
        highChance = 0.356,
    ),
    KARAMBWAN(
        id = Items.RAW_KARAMBWAN_3142,
        level = 65,
        experience = 105.0,
        lowChance = 0.414,
        highChance = 0.629,
    ),
    SHARK(
        id = Items.RAW_SHARK_383,
        level = 76,
        experience = 110.0,
        lowChance = 0.121,
        highChance = 0.16,
    ),
    SEA_TURTLE(
        id = Items.RAW_SEA_TURTLE_395,
        level = 79,
        experience = 38.0,
        lowChance = 0.0,
        highChance = 0.0,
    ),
    MANTA_RAY(
        id = Items.RAW_MANTA_RAY_389,
        level = 81,
        experience = 46.0,
        lowChance = 0.0,
        highChance = 0.0,
    ),
    SEAWEED(
        id = Items.SEAWEED_401,
        level = 16,
        experience = 1.0,
        lowChance = 0.63,
        highChance = 0.219,
    ),
    CASKET(
        id = Items.CASKET_405,
        level = 16,
        experience = 10.0,
        lowChance = 0.63,
        highChance = 0.219,
    ),
    OYSTER(
        id = Items.OYSTER_407,
        level = 16,
        experience = 10.0,
        lowChance = 0.63,
        highChance = 0.219,
    ),
    ;

    companion object {
        @JvmStatic
        val fishMap: HashMap<Int, Fish> = HashMap()

        @JvmStatic
        val bigFishMap: HashMap<Fish, Int> = HashMap()

        init {
            for (fish in values()) {
                fishMap[fish.id] = fish
            }
            bigFishMap[BASS] = Items.BIG_BASS_7989
            bigFishMap[SWORDFISH] = Items.BIG_SWORDFISH_7991
            bigFishMap[SHARK] = Items.BIG_SHARK_7993
        }

        @JvmStatic
        fun getBigFish(fish: Fish): Int? {
            return bigFishMap[fish]
        }

        @JvmStatic
        fun forItem(item: Item): Fish? {
            return fishMap[item.id]
        }
    }

    fun getSuccessChance(level: Int): Double {
        return (level.toDouble() - 1.0) * ((highChance - lowChance) / (99.0 - 1.0)) + lowChance
    }

    fun getItem(): Item {
        return this.id.asItem()
    }
}
