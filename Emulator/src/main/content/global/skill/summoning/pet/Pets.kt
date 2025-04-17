package content.global.skill.summoning.pet

import core.api.log
import core.game.node.entity.player.Player
import core.tools.Log
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Pets.
 */
enum class Pets(
    @JvmField val babyItemId: Int,
    @JvmField val grownItemId: Int,
    @JvmField val overgrownItemId: Int,
    @JvmField val babyNpcId: Int,
    val grownNpcId: Int,
    val overgrownNpcId: Int,
    @JvmField val growthRate: Double,
    @JvmField val summoningLevel: Int,
    @JvmField vararg val food: Int
) {
    CAT(
        babyItemId = Items.PET_KITTEN_1555,
        grownItemId = Items.PET_CAT_1561,
        overgrownItemId = Items.OVERGROWN_CAT_1567,
        babyNpcId = NPCs.KITTEN_761,
        grownNpcId = NPCs.CAT_768,
        overgrownNpcId = NPCs.OVERGROWN_CAT_774,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    CAT_1(
        babyItemId = Items.PET_KITTEN_1556,
        grownItemId = Items.PET_CAT_1562,
        overgrownItemId = Items.OVERGROWN_CAT_1568,
        babyNpcId = NPCs.KITTEN_762,
        grownNpcId = NPCs.CAT_769,
        overgrownNpcId = NPCs.OVERGROWN_CAT_775,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    CAT_2(
        babyItemId = Items.PET_KITTEN_1557,
        grownItemId = Items.PET_CAT_1563,
        overgrownItemId = Items.OVERGROWN_CAT_1569,
        babyNpcId = NPCs.KITTEN_763,
        grownNpcId = NPCs.CAT_770,
        overgrownNpcId = NPCs.OVERGROWN_CAT_776,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    CAT_3(
        babyItemId = Items.PET_KITTEN_1558,
        grownItemId = Items.PET_CAT_1564,
        overgrownItemId = Items.OVERGROWN_CAT_1570,
        babyNpcId = NPCs.KITTEN_764,
        grownNpcId = NPCs.CAT_771,
        overgrownNpcId = NPCs.OVERGROWN_CAT_777,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    CAT_4(
        babyItemId = Items.PET_KITTEN_1559,
        grownItemId = Items.PET_CAT_1565,
        overgrownItemId = Items.OVERGROWN_CAT_1571,
        babyNpcId = NPCs.KITTEN_765,
        grownNpcId = NPCs.CAT_772,
        overgrownNpcId = NPCs.OVERGROWN_CAT_778,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    CAT_5(
        babyItemId = Items.PET_KITTEN_1560,
        grownItemId = Items.PET_CAT_1566,
        overgrownItemId = Items.OVERGROWN_CAT_1572,
        babyNpcId = NPCs.KITTEN_766,
        grownNpcId = NPCs.CAT_773,
        overgrownNpcId = NPCs.OVERGROWN_CAT_779,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    HELLCAT(
        babyItemId = Items.HELL_KITTEN_7583,
        grownItemId = Items.HELL_CAT_7582,
        overgrownItemId = Items.OVERGROWN_HELLCAT_7581,
        babyNpcId = NPCs.HELL_KITTEN_3505,
        grownNpcId = NPCs.HELLCAT_3504,
        overgrownNpcId = NPCs.OVERGROWN_HELLCAT_3503,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    CAT_6(
        babyItemId = Items.PET_KITTEN_14089,
        grownItemId = Items.PET_CAT_14090,
        overgrownItemId = Items.OVERGROWN_CAT_14092,
        babyNpcId = NPCs.KITTEN_8217,
        grownNpcId = NPCs.CAT_8214,
        overgrownNpcId = NPCs.OVERGROWN_CAT_8216,
        growthRate = 0.0154320987654321,
        summoningLevel = 0,
        Items.ANCHOVIES_319, Items.BASS_365, Items.BUCKET_OF_MILK_1927, Items.COD_339, Items.HERRING_347, Items.LOBSTER_379, Items.MACKEREL_355, Items.MANTA_RAY_391, Items.MONKFISH_7946, Items.PIKE_351, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.SALMON_329, Items.SARDINE_325, Items.SEA_TURTLE_397, Items.SHARK_385, Items.SHRIMPS_315, Items.SWORDFISH_373, Items.TROUT_333, Items.TUNA_361,
    ),
    CLOCKWORK_CAT(
        babyItemId = Items.CLOCKWORK_CAT_7771,
        grownItemId = Items.CLOCKWORK_CAT_7772,
        overgrownItemId = -1,
        babyNpcId = NPCs.CLOCKWORK_CAT_3598,
        grownNpcId = -1,
        overgrownNpcId = -1,
        growthRate = 0.0,
        summoningLevel = 0
    ),
    BULLDOG(
        babyItemId = Items.BULLDOG_PUPPY_12522,
        grownItemId = Items.BULLDOG_12523,
        overgrownItemId = -1,
        babyNpcId = NPCs.BULLDOG_PUPPY_6969,
        grownNpcId = NPCs.BULLDOG_6968,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    BULLDOG_1(
        babyItemId = Items.BULLDOG_PUPPY_12720,
        grownItemId = Items.BULLDOG_12721,
        overgrownItemId = -1,
        babyNpcId = NPCs.BULLDOG_PUPPY_7259,
        grownNpcId = NPCs.BULLDOG_7257,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    BULLDOG_2(
        babyItemId = Items.BULLDOG_PUPPY_12722,
        grownItemId = Items.BULLDOG_12723,
        overgrownItemId = -1,
        babyNpcId = NPCs.BULLDOG_PUPPY_7260,
        grownNpcId = NPCs.BULLDOG_7258,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    DALMATIAN(
        babyItemId = Items.DALMATIAN_PUPPY_12518,
        grownItemId = Items.DALMATIAN_12519,
        overgrownItemId = -1,
        babyNpcId = NPCs.DALMATIAN_PUPPY_6964,
        grownNpcId = NPCs.DALMATIAN_6965,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    DALMATIAN_1(
        babyItemId = Items.DALMATIAN_PUPPY_12712,
        grownItemId = Items.DALMATIAN_12713,
        overgrownItemId = -1,
        babyNpcId = NPCs.DALMATIAN_PUPPY_7249,
        grownNpcId = NPCs.DALMATIAN_7250,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    DALMATIAN_2(
        babyItemId = Items.DALMATIAN_PUPPY_12714,
        grownItemId = Items.DALMATIAN_12715,
        overgrownItemId = -1,
        babyNpcId = NPCs.DALMATIAN_PUPPY_7251,
        grownNpcId = NPCs.DALMATIAN_7252,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    GREYHOUND(
        babyItemId = Items.GREYHOUND_PUPPY_12514,
        grownItemId = Items.GREYHOUND_12515,
        overgrownItemId = -1,
        babyNpcId = NPCs.GREYHOUND_PUPPY_6960,
        grownNpcId = NPCs.GREYHOUND_6961,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    GREYHOUND_1(
        babyItemId = Items.GREYHOUND_PUPPY_12704,
        grownItemId = Items.GREYHOUND_12705,
        overgrownItemId = -1,
        babyNpcId = NPCs.GREYHOUND_PUPPY_7241,
        grownNpcId = NPCs.GREYHOUND_7242,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    GREYHOUND_2(
        babyItemId = Items.GREYHOUND_PUPPY_12706,
        grownItemId = Items.GREYHOUND_12707,
        overgrownItemId = -1,
        babyNpcId = NPCs.GREYHOUND_PUPPY_7243,
        grownNpcId = NPCs.GREYHOUND_7244,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    LABRADOR(
        babyItemId = Items.LABRADOR_PUPPY_12516,
        grownItemId = Items.LABRADOR_12517,
        overgrownItemId = -1,
        babyNpcId = NPCs.LABRADOR_PUPPY_6962,
        grownNpcId = NPCs.LABRADOR_6963,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    LABRADOR_1(
        babyItemId = Items.LABRADOR_PUPPY_12708,
        grownItemId = Items.LABRADOR_12709,
        overgrownItemId = -1,
        babyNpcId = NPCs.LABRADOR_PUPPY_7245,
        grownNpcId = NPCs.LABRADOR_7246,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    LABRADOR_2(
        babyItemId = Items.LABRADOR_PUPPY_12710,
        grownItemId = Items.LABRADOR_12711,
        overgrownItemId = -1,
        babyNpcId = NPCs.LABRADOR_PUPPY_7247,
        grownNpcId = NPCs.LABRADOR_7248,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    SHEEPDOG(
        babyItemId = Items.SHEEPDOG_PUPPY_12520,
        grownItemId = Items.SHEEPDOG_12521,
        overgrownItemId = -1,
        babyNpcId = NPCs.SHEEPDOG_PUPPY_6966,
        grownNpcId = NPCs.SHEEPDOG_6967,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    SHEEPDOG_1(
        babyItemId = Items.SHEEPDOG_PUPPY_12716,
        grownItemId = Items.SHEEPDOG_12717,
        overgrownItemId = -1,
        babyNpcId = NPCs.SHEEPDOG_PUPPY_7253,
        grownNpcId = NPCs.SHEEPDOG_7254,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    SHEEPDOG_2(
        babyItemId = Items.SHEEPDOG_PUPPY_12718,
        grownItemId = Items.SHEEPDOG_12719,
        overgrownItemId = -1,
        babyNpcId = NPCs.SHEEPDOG_PUPPY_7255,
        grownNpcId = NPCs.SHEEPDOG_7256,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    TERRIER(
        babyItemId = Items.TERRIER_PUPPY_12512,
        grownItemId = Items.TERRIER_12513,
        overgrownItemId = -1,
        babyNpcId = NPCs.TERRIER_PUPPY_6958,
        grownNpcId = NPCs.TERRIER_6959,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    TERRIER_1(
        babyItemId = Items.TERRIER_PUPPY_12700,
        grownItemId = Items.TERRIER_12701,
        overgrownItemId = -1,
        babyNpcId = NPCs.TERRIER_PUPPY_7237,
        grownNpcId = NPCs.TERRIER_7238,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    TERRIER_2(
        babyItemId = Items.TERRIER_PUPPY_12702,
        grownItemId = Items.TERRIER_12703,
        overgrownItemId = -1,
        babyNpcId = NPCs.TERRIER_PUPPY_7239,
        grownNpcId = NPCs.TERRIER_7240,
        overgrownNpcId = -1,
        growthRate = 0.0033333333333333,
        summoningLevel = 4,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.BONES_526
    ),
    GECKO(
        babyItemId = Items.BABY_GECKO_12488,
        grownItemId = Items.GECKO_12489,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GECKO_6915,
        grownNpcId = NPCs.GECKO_6916,
        overgrownNpcId = -1,
        growthRate = 0.005,
        summoningLevel = 10,
        Items.FLIES_12125, Items.BEETLE_BITS_12127
    ),
    GECKO_1(
        babyItemId = Items.BABY_GECKO_12738,
        grownItemId = Items.GECKO_12742,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GECKO_7277,
        grownNpcId = NPCs.GECKO_7281,
        overgrownNpcId = -1,
        growthRate = 0.005,
        summoningLevel = 10,
        Items.FLIES_12125, Items.BEETLE_BITS_12127
    ),
    GECKO_2(
        babyItemId = Items.BABY_GECKO_12739,
        grownItemId = Items.GECKO_12743,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GECKO_7278,
        grownNpcId = NPCs.GECKO_7282,
        overgrownNpcId = -1,
        growthRate = 0.005,
        summoningLevel = 10,
        Items.FLIES_12125, Items.BEETLE_BITS_12127
    ),
    GECKO_3(
        babyItemId = Items.BABY_GECKO_12740,
        grownItemId = Items.GECKO_12744,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GECKO_7279,
        grownNpcId = NPCs.GECKO_7283,
        overgrownNpcId = -1,
        growthRate = 0.005,
        summoningLevel = 10,
        Items.FLIES_12125, Items.BEETLE_BITS_12127
    ),
    GECKO_4(
        babyItemId = Items.BABY_GECKO_12741,
        grownItemId = Items.GECKO_12745,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GECKO_7280,
        grownNpcId = NPCs.GECKO_7284,
        overgrownNpcId = -1,
        growthRate = 0.005,
        summoningLevel = 10,
        Items.FLIES_12125, Items.BEETLE_BITS_12127
    ),
    PLATYPUS(
        babyItemId = Items.BABY_PLATYPUS_12551,
        grownItemId = Items.PLATYPUS_12548,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_PLATYPUS_7018,
        grownNpcId = NPCs.PLATYPUS_7015,
        overgrownNpcId = -1,
        growthRate = 0.0046296296296296,
        summoningLevel = 10,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359,
    ),
    PLATYPUS_1(
        babyItemId = Items.BABY_PLATYPUS_12552,
        grownItemId = Items.PLATYPUS_12549,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_PLATYPUS_7019,
        grownNpcId = NPCs.PLATYPUS_7016,
        overgrownNpcId = -1,
        growthRate = 0.0046296296296296,
        summoningLevel = 10,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359,
    ),
    PLATYPUS_2(
        babyItemId = Items.BABY_PLATYPUS_12553,
        grownItemId = Items.PLATYPUS_12550,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_PLATYPUS_7020,
        grownNpcId = NPCs.PLATYPUS_7017,
        overgrownNpcId = -1,
        growthRate = 0.0046296296296296,
        summoningLevel = 10,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359,
    ),
    BROAV(
        babyItemId = Items.BROAV_14533,
        grownItemId = -1,
        overgrownItemId = -1,
        babyNpcId = NPCs.BROAV_8491,
        grownNpcId = -1,
        overgrownNpcId = -1,
        growthRate = 0.0,
        summoningLevel = 23,
        Items.MORT_MYRE_FUNGUS_2970
    ),
    PENGUIN(
        babyItemId = Items.BABY_PENGUIN_12481,
        grownItemId = Items.PENGUIN_12482,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_PENGUIN_6908,
        grownNpcId = NPCs.PENGUIN_6909,
        overgrownNpcId = -1,
        growthRate = 0.0046296296296296,
        summoningLevel = 30,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359,
    ),
    PENGUIN_1(
        babyItemId = Items.BABY_PENGUIN_12763,
        grownItemId = Items.PENGUIN_12762,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_PENGUIN_7313,
        grownNpcId = NPCs.PENGUIN_7314,
        overgrownNpcId = -1,
        growthRate = 0.0046296296296296,
        summoningLevel = 30,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359,
    ),
    PENGUIN_2(
        babyItemId = Items.BABY_PENGUIN_12765,
        grownItemId = Items.PENGUIN_12764,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_PENGUIN_7316,
        grownNpcId = NPCs.PENGUIN_7317,
        overgrownNpcId = -1,
        growthRate = 0.0046296296296296,
        summoningLevel = 30,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359,
    ),
    GIANT_CRAB(
        babyItemId = Items.BABY_GIANT_CRAB_12500,
        grownItemId = Items.GIANT_CRAB_12501,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GIANT_CRAB_6947,
        grownNpcId = NPCs.GIANT_CRAB_6948,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 40,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359,
    ),
    GIANT_CRAB_1(
        babyItemId = Items.BABY_GIANT_CRAB_12746,
        grownItemId = Items.GIANT_CRAB_12747,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GIANT_CRAB_7293,
        grownNpcId = NPCs.GIANT_CRAB_7294,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 40,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    GIANT_CRAB_2(
        babyItemId = Items.BABY_GIANT_CRAB_12748,
        grownItemId = Items.GIANT_CRAB_12749,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GIANT_CRAB_7295,
        grownNpcId = NPCs.GIANT_CRAB_7296,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 40,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    GIANT_CRAB_3(
        babyItemId = Items.BABY_GIANT_CRAB_12750,
        grownItemId = Items.GIANT_CRAB_12751,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GIANT_CRAB_7297,
        grownNpcId = NPCs.GIANT_CRAB_7298,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 40,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    GIANT_CRAB_4(
        babyItemId = Items.BABY_GIANT_CRAB_12752,
        grownItemId = Items.GIANT_CRAB_12753,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_GIANT_CRAB_7299,
        grownNpcId = NPCs.GIANT_CRAB_7300,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 40,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    RAVEN(
        babyItemId = Items.RAVEN_CHICK_12484,
        grownItemId = Items.RAVEN_12485,
        overgrownItemId = -1,
        babyNpcId = NPCs.RAVEN_CHICK_6911,
        grownNpcId = NPCs.RAVEN_6912,
        overgrownNpcId = -1,
        growthRate = 0.00698888,
        summoningLevel = 50,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    RAVEN_1(
        babyItemId = Items.RAVEN_CHICK_12724,
        grownItemId = Items.RAVEN_12725,
        overgrownItemId = -1,
        babyNpcId = NPCs.RAVEN_CHICK_7261,
        grownNpcId = NPCs.RAVEN_7262,
        overgrownNpcId = -1,
        growthRate = 0.00698888,
        summoningLevel = 50,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    RAVEN_2(
        babyItemId = Items.RAVEN_CHICK_12726,
        grownItemId = Items.RAVEN_12727,
        overgrownItemId = -1,
        babyNpcId = NPCs.RAVEN_CHICK_7263,
        grownNpcId = NPCs.RAVEN_7264,
        overgrownNpcId = -1,
        growthRate = 0.00698888,
        summoningLevel = 50,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    RAVEN_3(
        babyItemId = Items.RAVEN_CHICK_12728,
        grownItemId = Items.RAVEN_12729,
        overgrownItemId = -1,
        babyNpcId = NPCs.RAVEN_CHICK_7265,
        grownNpcId = NPCs.RAVEN_7266,
        overgrownNpcId = -1,
        growthRate = 0.00698888,
        summoningLevel = 50,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    RAVEN_4(
        babyItemId = Items.RAVEN_CHICK_12730,
        grownItemId = Items.RAVEN_12731,
        overgrownItemId = -1,
        babyNpcId = NPCs.RAVEN_CHICK_7267,
        grownNpcId = NPCs.RAVEN_7268,
        overgrownNpcId = -1,
        growthRate = 0.00698888,
        summoningLevel = 50,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    RAVEN_5(
        babyItemId = Items.RAVEN_CHICK_12732,
        grownItemId = Items.RAVEN_12733,
        overgrownItemId = -1,
        babyNpcId = NPCs.RAVEN_CHICK_7269,
        grownNpcId = NPCs.RAVEN_7270,
        overgrownNpcId = -1,
        growthRate = 0.00698888,
        summoningLevel = 50,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    SQUIRREL(
        babyItemId = Items.BABY_SQUIRREL_12490,
        grownItemId = Items.SQUIRREL_12491,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_SQUIRREL_6919,
        grownNpcId = NPCs.SQUIRREL_6920,
        overgrownNpcId = -1,
        growthRate = 0.0071225071225071,
        summoningLevel = 60,
        Items.NUTS_12130
    ),
    SQUIRREL_1(
        babyItemId = Items.BABY_SQUIRREL_12754,
        grownItemId = Items.SQUIRREL_12755,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_SQUIRREL_7301,
        grownNpcId = NPCs.SQUIRREL_7302,
        overgrownNpcId = -1,
        growthRate = 0.0071225071225071,
        summoningLevel = 60,
        Items.NUTS_12130
    ),
    SQUIRREL_2(
        babyItemId = Items.BABY_SQUIRREL_12756,
        grownItemId = Items.SQUIRREL_12757,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_SQUIRREL_7303,
        grownNpcId = NPCs.SQUIRREL_7304,
        overgrownNpcId = -1,
        growthRate = 0.0071225071225071,
        summoningLevel = 60,
        Items.NUTS_12130
    ),
    SQUIRREL_3(
        babyItemId = Items.BABY_SQUIRREL_12758,
        grownItemId = Items.SQUIRREL_12759,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_SQUIRREL_7305,
        grownNpcId = NPCs.SQUIRREL_7306,
        overgrownNpcId = -1,
        growthRate = 0.0071225071225071,
        summoningLevel = 60,
        Items.NUTS_12130
    ),
    SQUIRREL_4(
        babyItemId = Items.BABY_SQUIRREL_12760,
        grownItemId = Items.SQUIRREL_12761,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_SQUIRREL_7307,
        grownNpcId = NPCs.SQUIRREL_7308,
        overgrownNpcId = -1,
        growthRate = 0.0071225071225071,
        summoningLevel = 60,
        Items.NUTS_12130
    ),
    SARADOMIN_OWL(
        babyItemId = Items.SARADOMIN_CHICK_12503,
        grownItemId = Items.SARADOMIN_BIRD_12504,
        overgrownItemId = Items.SARADOMIN_OWL_12505,
        babyNpcId = NPCs.SARADOMIN_CHICK_6949,
        grownNpcId = NPCs.SARADOMIN_BIRD_6950,
        overgrownNpcId = NPCs.SARADOMIN_OWL_6951,
        growthRate = 0.0069444444444444,
        summoningLevel = 70,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    ZAMORAK_HAWK(
        babyItemId = Items.ZAMORAK_CHICK_12506,
        grownItemId = Items.ZAMORAK_BIRD_12507,
        overgrownItemId = Items.ZAMORAK_HAWK_12508,
        babyNpcId = NPCs.ZAMORAK_CHICK_6952,
        grownNpcId = NPCs.ZAMORAK_BIRD_6953,
        overgrownNpcId = NPCs.ZAMORAK_HAWK_6954,
        growthRate = 0.0069444444444444,
        summoningLevel = 70,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    GUTHIX_RAPTOR(
        babyItemId = Items.GUTHIX_CHICK_12509,
        grownItemId = Items.GUTHIX_BIRD_12510,
        overgrownItemId = Items.GUTHIX_RAPTOR_12511,
        babyNpcId = NPCs.GUTHIX_CHICK_6955,
        grownNpcId = NPCs.GUTHIX_BIRD_6956,
        overgrownNpcId = NPCs.GUTHIX_RAPTOR_6957,
        growthRate = 0.0069444444444444,
        summoningLevel = 70,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    EX_EX_PARROT(
        babyItemId = Items.EX_EX_PARROT_13335,
        grownItemId = -1,
        overgrownItemId = -1,
        babyNpcId = NPCs.EX_EX_PARROT_7844,
        grownNpcId = -1,
        overgrownNpcId = -1,
        growthRate = 0.0,
        summoningLevel = 71,
        Items.ROCK_FRAGMENTS_13379
    ),
    CUTE_PHOENIX_EGGLING(
        babyItemId = Items.PHOENIX_EGGLING_14627,
        grownItemId = -1,
        overgrownItemId = -1,
        babyNpcId = NPCs.PHOENIX_EGGLING_8578,
        grownNpcId = -1,
        overgrownNpcId = -1,
        growthRate = 0.0,
        summoningLevel = 72,
        Items.ASHES_592
    ),
    MEAN_PHOENIX_EGGLING(
        babyItemId = Items.PHOENIX_EGGLING_14626,
        grownItemId = -1,
        overgrownItemId = -1,
        babyNpcId = NPCs.PHOENIX_EGGLING_8577,
        grownNpcId = -1,
        overgrownNpcId = -1,
        growthRate = 0.0,
        summoningLevel = 72,
        Items.ASHES_592
    ),
    RACCOON(
        babyItemId = Items.BABY_RACCOON_12486,
        grownItemId = Items.RACCOON_12487,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_RACCOON_6913,
        grownNpcId = NPCs.RACCOON_6914,
        overgrownNpcId = -1,
        growthRate = 0.0029444444444444,
        summoningLevel = 80,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978,
    ),
    RACCOON_1(
        babyItemId = Items.BABY_RACCOON_12734,
        grownItemId = Items.RACCOON_12735,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_RACCOON_7271,
        grownNpcId = NPCs.RACCOON_7272,
        overgrownNpcId = -1,
        growthRate = 0.0029444444444444,
        summoningLevel = 80,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978,
    ),
    RACCOON_2(
        babyItemId = Items.BABY_RACCOON_12736,
        grownItemId = Items.RACCOON_12737,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_RACCOON_7273,
        grownNpcId = NPCs.RACCOON_7274,
        overgrownNpcId = -1,
        growthRate = 0.0029444444444444,
        summoningLevel = 80,
        Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359, Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978,
    ),
    VULTURE(
        babyItemId = Items.VULTURE_CHICK_12498,
        grownItemId = Items.VULTURE_12499,
        overgrownItemId = -1,
        babyNpcId = NPCs.VULTURE_CHICK_6945,
        grownNpcId = NPCs.VULTURE_6946,
        overgrownNpcId = -1,
        growthRate = 0.0078,
        summoningLevel = 85,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    VULTURE_1(
        babyItemId = Items.VULTURE_CHICK_12766,
        grownItemId = Items.VULTURE_12767,
        overgrownItemId = -1,
        babyNpcId = NPCs.VULTURE_CHICK_7319,
        grownNpcId = NPCs.VULTURE_7320,
        overgrownNpcId = -1,
        growthRate = 0.0078,
        summoningLevel = 85,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    VULTURE_2(
        babyItemId = Items.VULTURE_CHICK_12768,
        grownItemId = Items.VULTURE_12769,
        overgrownItemId = -1,
        babyNpcId = NPCs.VULTURE_CHICK_7321,
        grownNpcId = NPCs.VULTURE_7322,
        overgrownNpcId = -1,
        growthRate = 0.0078,
        summoningLevel = 85,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    VULTURE_3(
        babyItemId = Items.VULTURE_CHICK_12770,
        grownItemId = Items.VULTURE_12771,
        overgrownItemId = -1,
        babyNpcId = NPCs.VULTURE_CHICK_7323,
        grownNpcId = NPCs.VULTURE_7324,
        overgrownNpcId = -1,
        growthRate = 0.0078,
        summoningLevel = 85,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    VULTURE_4(
        babyItemId = Items.VULTURE_CHICK_12772,
        grownItemId = Items.VULTURE_12773,
        overgrownItemId = -1,
        babyNpcId = NPCs.VULTURE_CHICK_7325,
        grownNpcId = NPCs.VULTURE_7326,
        overgrownNpcId = -1,
        growthRate = 0.0078,
        summoningLevel = 85,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    VULTURE_5(
        babyItemId = Items.VULTURE_CHICK_12774,
        grownItemId = Items.VULTURE_12775,
        overgrownItemId = -1,
        babyNpcId = NPCs.VULTURE_CHICK_7327,
        grownNpcId = NPCs.VULTURE_7328,
        overgrownNpcId = -1,
        growthRate = 0.0078,
        summoningLevel = 85,
        Items.FISHING_BAIT_313, Items.GROUND_FISHING_BAIT_12129
    ),
    CHAMELEON(
        babyItemId = Items.BABY_CHAMELEON_12492,
        grownItemId = Items.CHAMELEON_12493,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_CHAMELEON_6922,
        grownNpcId = NPCs.CHAMELEON_6923,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 90,
        Items.FLIES_12125
    ),
    MONKEY(
        babyItemId = Items.BABY_MONKEY_12496,
        grownItemId = Items.MONKEY_12497,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_6942,
        grownNpcId = NPCs.MONKEY_6943,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_1(
        babyItemId = Items.BABY_MONKEY_12682,
        grownItemId = Items.MONKEY_12683,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7210,
        grownNpcId = NPCs.MONKEY_7211,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_2(
        babyItemId = Items.BABY_MONKEY_12684,
        grownItemId = Items.MONKEY_12685,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7212,
        grownNpcId = NPCs.MONKEY_7213,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_3(
        babyItemId = Items.BABY_MONKEY_12686,
        grownItemId = Items.MONKEY_12687,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7214,
        grownNpcId = NPCs.MONKEY_7215,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        1963
    ),
    MONKEY_4(
        babyItemId = Items.BABY_MONKEY_12688,
        grownItemId = Items.MONKEY_12689,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7216,
        grownNpcId = NPCs.MONKEY_7217,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_5(
        babyItemId = Items.BABY_MONKEY_12690,
        grownItemId = Items.MONKEY_12691,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7218,
        grownNpcId = NPCs.MONKEY_7219,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_6(
        babyItemId = Items.BABY_MONKEY_12692,
        grownItemId = Items.MONKEY_12693,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7220,
        grownNpcId = NPCs.MONKEY_7221,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_7(
        babyItemId = Items.BABY_MONKEY_12694,
        grownItemId = Items.MONKEY_12695,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7222,
        grownNpcId = NPCs.MONKEY_7223,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_8(
        babyItemId = Items.BABY_MONKEY_12696,
        grownItemId = Items.MONKEY_12697,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7224,
        grownNpcId = NPCs.MONKEY_7225,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    MONKEY_9(
        babyItemId = Items.BABY_MONKEY_12698,
        grownItemId = Items.MONKEY_12699,
        overgrownItemId = -1,
        babyNpcId = NPCs.BABY_MONKEY_7226,
        grownNpcId = NPCs.MONKEY_7227,
        overgrownNpcId = -1,
        growthRate = 0.0069444444444444,
        summoningLevel = 95,
        Items.BANANA_1963
    ),
    BABY_DRAGON(
        babyItemId = Items.HATCHLING_DRAGON_12469,
        grownItemId = Items.BABY_DRAGON_12470,
        overgrownItemId = -1,
        babyNpcId = NPCs.HATCHLING_DRAGON_6900,
        grownNpcId = NPCs.BABY_DRAGON_6901,
        overgrownNpcId = -1,
        growthRate = 0.0052,
        summoningLevel = 99,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    BABY_DRAGON_1(
        babyItemId = Items.HATCHLING_DRAGON_12471,
        grownItemId = Items.BABY_DRAGON_12472,
        overgrownItemId = -1,
        babyNpcId = NPCs.HATCHLING_DRAGON_6902,
        grownNpcId = NPCs.BABY_DRAGON_6903,
        overgrownNpcId = -1,
        growthRate = 0.0052,
        summoningLevel = 99,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    BABY_DRAGON_2(
        babyItemId = Items.HATCHLING_DRAGON_12473,
        grownItemId = Items.BABY_DRAGON_12474,
        overgrownItemId = -1,
        babyNpcId = NPCs.HATCHLING_DRAGON_6904,
        grownNpcId = NPCs.BABY_DRAGON_6905,
        overgrownNpcId = -1,
        growthRate = 0.0052,
        summoningLevel = 99,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    BABY_DRAGON_3(
        babyItemId = Items.HATCHLING_DRAGON_12475,
        grownItemId = Items.BABY_DRAGON_12476,
        overgrownItemId = -1,
        babyNpcId = NPCs.HATCHLING_DRAGON_6906,
        grownNpcId = NPCs.BABY_DRAGON_6907,
        overgrownNpcId = -1,
        growthRate = 0.0052,
        summoningLevel = 99,
        Items.RAW_BEEF_2132, Items.RAW_RAT_MEAT_2134, Items.RAW_BEAR_MEAT_2136, Items.RAW_CHICKEN_2138, Items.RAW_YAK_MEAT_10816, Items.RAW_BEAST_MEAT_9986, Items.RAW_BIRD_MEAT_9978, Items.RAW_ANCHOVIES_321, Items.RAW_BASS_363, Items.RAW_COD_341, Items.RAW_HERRING_345, Items.RAW_LOBSTER_377, Items.RAW_MACKEREL_353, Items.RAW_MANTA_RAY_389, Items.RAW_MONKFISH_7944, Items.RAW_PIKE_349, Items.RAW_SALMON_331, Items.RAW_SARDINE_327, Items.RAW_SEA_TURTLE_395, Items.RAW_SHARK_383, Items.RAW_SHRIMPS_317, Items.RAW_SWORDFISH_371, Items.RAW_TROUT_335, Items.RAW_TUNA_359
    ),
    EEK(
        babyItemId = Items.SPIDER_14073,
        grownItemId = -1,
        overgrownItemId = -1,
        babyNpcId = -1,
        grownNpcId = -1,
        overgrownNpcId = -1,
        growthRate = 0.0,
        summoningLevel = -1,
        -1
    );

    /**
     * Retrieves the NPC ID corresponding to the given pet item ID.
     *
     * @param itemId The item ID of the pet.
     * @return The NPC ID associated with the pet item, or -1 if no match is found.
     */
    fun getNpcId(itemId: Int): Int {
        if (itemId == babyItemId) {
            return babyNpcId
        }
        if (itemId == grownItemId) {
            return grownNpcId
        }
        if (itemId == overgrownItemId) {
            return overgrownNpcId
        }
        log(this.javaClass, Log.ERR, "Could not locate NPC ID for pet item $itemId")
        return -1
    }

    /**
     * Retrieves the next evolutionary stage's item ID for a given pet.
     *
     * @param itemId The current stage item ID.
     * @return The item ID of the next stage, or -1 if no further stage exists.
     */
    fun getNextStageItemId(itemId: Int): Int {
        if (itemId == babyItemId) {
            return grownItemId
        }
        if (itemId == grownItemId) {
            return overgrownItemId
        }
        return -1
    }

    /**
     * Determines whether the given item ID corresponds to a kitten.
     *
     * @param id The item ID to check.
     * @return `true` if the pet is a kitten, otherwise `false`.
     */
    fun isKitten(id: Int): Boolean {
        return when (this) {
            CAT, CAT_1, CAT_2, CAT_3, CAT_4, CAT_5, CAT_6, HELLCAT -> id == babyItemId
            else -> false
        }
    }

    companion object {
        private val babyPets: MutableMap<Int, Pets> = HashMap()
        private val grownPets: MutableMap<Int, Pets> = HashMap()
        private val overgrownPets: MutableMap<Int, Pets> = HashMap()

        init {
            for (pet in values()) {
                babyPets[pet.babyItemId] = pet
                if (pet.grownItemId > 0) {
                    grownPets[pet.grownItemId] = pet
                    if (pet.overgrownItemId > 0) {
                        overgrownPets[pet.overgrownItemId] = pet
                    }
                }
            }
        }

        /**
         * Retrieves the pet instance corresponding to the given item ID.
         *
         * @param itemId The item ID of the pet.
         * @return The corresponding `Pets` instance, or `null` if not found.
         */
        @JvmStatic
        fun forId(itemId: Int): Pets? {
            var pet = babyPets[itemId]
            if (pet == null) {
                pet = grownPets[itemId]
                if (pet == null) {
                    return overgrownPets[itemId]
                }
                return pet
            }
            return pet
        }

        /**
         * Checks whether the player has any pet in their inventory.
         *
         * @param player The player to check.
         * @return `true` if the player has a pet, otherwise `false`.
         */
        fun hasPet(player: Player): Boolean {
            for (itemId in babyPets.keys) {
                if (player.inventory.containsAtLeastOneItem(itemId)) {
                    return true
                }
            }
            for (itemId in grownPets.keys) {
                if (player.inventory.containsAtLeastOneItem(itemId)) {
                    return true
                }
            }
            for (itemId in overgrownPets.keys) {
                if (player.inventory.containsAtLeastOneItem(itemId)) {
                    return true
                }
            }
            return false
        }
    }
}