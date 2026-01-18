package content.global.skill.gather.fishing

import core.api.asItem
import core.game.node.item.Item
import shared.consts.Items

/**
 * Represents a fish type in the game.
 *
 * @property id The item id of the fish.
 * @property requiredLevel The fishing level required.
 * @property xp The xp gained when caught.
 * @property lowChance The min success chance.
 * @property highChance The max success chance.
 */
enum class Fish(
    val id: Int,
    val requiredLevel: Int,
    val xp: Double,
    private val lowChance: Double,
    private val highChance: Double
) {
    SWAMP_WEED(Items.SWAMP_WEED_10978, 1, 1.0, 0.121, 0.16),
    CRAYFISH(Items.RAW_CRAYFISH_13435, 1, 10.0, 0.15, 0.5),
    SHRIMP(Items.RAW_SHRIMPS_317, 1, 10.0, 0.191, 0.5),
    SARDINE(Items.RAW_SARDINE_327, 5, 20.0, 0.148, 0.374),
    KARAMBWANJI(Items.RAW_KARAMBWANJI_3150, 5, 5.0, 0.4, 0.98),
    HERRING(Items.RAW_HERRING_345, 10, 30.0, 0.129, 0.504),
    ANCHOVY(Items.RAW_ANCHOVIES_321, 15, 40.0, 0.098, 0.5),
    MACKEREL(Items.RAW_MACKEREL_353, 16, 20.0, 0.055, 0.258),
    TROUT(Items.RAW_TROUT_335, 20, 50.0, 0.246, 0.468),
    COD(Items.RAW_COD_341, 23, 45.0, 0.063, 0.219),
    PIKE(Items.RAW_PIKE_349, 25, 60.0, 0.14, 0.379),
    SLIMY_EEL(Items.SLIMY_EEL_3379, 28, 65.0, 0.117, 0.216),
    SALMON(Items.RAW_SALMON_331, 30, 70.0, 0.156, 0.378),
    FROG_SPAWN(Items.FROG_SPAWN_5004, 33, 75.0, 0.164, 0.379),
    TUNA(Items.RAW_TUNA_359, 35, 80.0, 0.109, 0.205),
    RAINBOW_FISH(Items.RAW_RAINBOW_FISH_10138, 38, 80.0, 0.113, 0.254),
    CAVE_EEL(Items.RAW_CAVE_EEL_5001, 38, 80.0, 0.145, 0.316),
    LOBSTER(Items.RAW_LOBSTER_377, 40, 90.0, 0.16, 0.375),
    BASS(Items.RAW_BASS_363, 46, 100.0, 0.078, 0.16),
    SWORDFISH(Items.RAW_SWORDFISH_371, 50, 100.0, 0.105, 0.191),
    LAVA_EEL(Items.RAW_LAVA_EEL_2148, 53, 30.0, 0.227, 0.379),
    MONKFISH(Items.RAW_MONKFISH_7944, 62, 120.0, 0.293, 0.356),
    KARAMBWAN(Items.RAW_KARAMBWAN_3142, 65, 105.0, 0.414, 0.629),
    SHARK(Items.RAW_SHARK_383, 76, 110.0, 0.121, 0.16),
    SEA_TURTLE(Items.RAW_SEA_TURTLE_395, 79, 38.0, 0.0, 0.0),
    MANTA_RAY(Items.RAW_MANTA_RAY_389, 81, 46.0, 0.0, 0.0),
    SEAWEED(Items.SEAWEED_401, 16, 1.0, 0.63, 0.219),
    CASKET(Items.CASKET_405, 16, 10.0, 0.63, 0.219),
    OYSTER(Items.OYSTER_407, 16, 10.0, 0.63, 0.219),
    GIANT_CARP(Items.RAW_GIANT_CARP_338, 10, 0.0, 0.098, 0.5);

    val item: Item = id.asItem()

    private val successChances: DoubleArray = DoubleArray(99) { lvl ->
        (lvl.toDouble()) * ((highChance - lowChance) / 98.0) + lowChance
    }

    /**
     * Returns the success chance at the given [level].
     *
     * @param level the fishing level
     * @return chance of catching this fish at that level.
     */
    fun getSuccessChance(level: Int): Double = successChances[level.coerceIn(1..99) - 1]

    companion object {
        val fishMap: Map<Int, Fish> = values().associateBy { it.id }

        private val bigFishMap: Map<Fish, Int> = mapOf(
            BASS to Items.BIG_BASS_7989,
            SWORDFISH to Items.BIG_SWORDFISH_7991,
            SHARK to Items.BIG_SHARK_7993
        )

        /**
         * Returns the item id for the big fish variant.
         */
        fun getBigFish(fish: Fish): Int? = bigFishMap[fish]

        /**
         * Returns the [Fish] for a given Item instance.
         */
        fun forItem(item: Item): Fish? = fishMap[item.id]
    }
}