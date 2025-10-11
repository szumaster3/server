package content.global.skill.cooking

import core.game.node.item.Item
import shared.consts.Items

/**
 * Represents the cook data.
 */
enum class CookableItems(
    val cooked: Int,
    val raw: Int,
    val burnt: Int,
    val level: Int,
    val experience: Double,
    val low: Int,
    val high: Int,
    val lowRange: Int,
    val highRange: Int
) {
    /**
     * Chicken.
     */
    CHICKEN(Items.COOKED_CHICKEN_2140, Items.RAW_CHICKEN_2138, Items.BURNT_CHICKEN_2144, 1, 30.0, 128, 512, 128, 512),

    /**
     * Ugthanki meat.
     */
    UGTHANKI(Items.UGTHANKI_MEAT_1861, Items.RAW_UGTHANKI_MEAT_1859, Items.BURNT_MEAT_2146, 1, 40.0, 40, 252, 30, 253),

    /**
     * Turkey drumstick.
     */
    TURKEY_DRUMSTICK(Items.COOKED_TURKEY_DRUMSTICK_14543, Items.RAW_TURKEY_DRUMSTICK_14542, Items.BURNT_TURKEY_DRUMSTICK_14544, 1, 30.0, 128, 512, 128, 512),

    /**
     * Turkey.
     */
    TURKEY(Items.COOKED_TURKEY_14540, Items.RAW_TURKEY_14539, Items.BURNT_TURKEY_14541, 1, 30.0, 128, 512, 128, 512),

    /**
     * Rabbit.
     */
    RABBIT(Items.COOKED_RABBIT_3228, Items.RAW_RABBIT_3226, Items.BURNT_RABBIT_7222, 1, 30.0, 128, 512, 128, 512),

    /**
     * Crab.
     */
    CRAB(Items.COOKED_CRAB_MEAT_7521, Items.CRAB_MEAT_7518, Items.BURNT_CRAB_MEAT_7520, 21, 100.0, 57, 377, 57, 377),

    /**
     * Chompy.
     */
    CHOMPY(Items.COOKED_CHOMPY_2878, Items.RAW_CHOMPY_2876, Items.RUINED_CHOMPY_2880, 30, 100.0, 200, 255, 200, 255),

    /**
     * Jubbly.
     */
    JUBBLY(Items.COOKED_JUBBLY_7568, Items.RAW_JUBBLY_7566, Items.BURNT_JUBBLY_7570, 41, 160.0, 195, 250, 195, 250),

    /**
     * Crayfish.
     */
    CRAYFISH(Items.CRAYFISH_13433, Items.RAW_CRAYFISH_13435, Items.BURNT_CRAYFISH_13437, 1, 30.0, 128, 512, 128, 512),

    /**
     * Shrimp.
     */
    SHRIMP(Items.SHRIMPS_315, Items.RAW_SHRIMPS_317, Items.BURNT_SHRIMP_7954, 1, 30.0, 128, 512, 128, 512),

    /**
     * Karambwanji.
     */
    KARAMBWANJI(Items.KARAMBWANJI_3151, Items.RAW_KARAMBWANJI_3150, Items.ASHES_592, 1, 10.0, 200, 400, 200, 400),

    /**
     * Sardine.
     */
    SARDINE(Items.SARDINE_325, Items.RAW_SARDINE_327, Items.BURNT_FISH_369, 1, 40.0, 118, 492, 118, 492),

    /**
     * Anchovies.
     */
    ANCHOVIES(Items.ANCHOVIES_319, Items.RAW_ANCHOVIES_321, Items.BURNT_FISH_323, 1, 30.0, 128, 512, 128, 512),

    /**
     * Herring.
     */
    HERRING(Items.HERRING_347, Items.RAW_HERRING_345, Items.BURNT_FISH_357, 5, 50.0, 108, 472, 108, 472),

    /**
     * Mackerel.
     */
    MACKEREL(Items.MACKEREL_355, Items.RAW_MACKEREL_353, Items.BURNT_FISH_357, 10, 60.0, 98, 452, 98, 452),

    /**
     * Trout.
     */
    TROUT(Items.TROUT_333, Items.RAW_TROUT_335, Items.BURNT_FISH_343, 15, 70.0, 88, 432, 88, 432),

    /**
     * Cod.
     */
    COD(Items.COD_339, Items.RAW_COD_341, Items.BURNT_FISH_343, 18, 75.0, 83, 422, 88, 432),

    /**
     * Pike.
     */
    PIKE(Items.PIKE_351, Items.RAW_PIKE_349, Items.BURNT_FISH_343, 20, 80.0, 78, 412, 78, 412),

    /**
     * Salmon.
     */
    SALMON(Items.SALMON_329, Items.RAW_SALMON_331, Items.BURNT_FISH_343, 25, 90.0, 68, 392, 68, 392),

    /**
     * Slimy eel.
     */
    SLIMY_EEL(Items.COOKED_SLIMY_EEL_3381, Items.SLIMY_EEL_3379, Items.BURNT_EEL_3383, 28, 95.0, 63, 382, 63, 382),

    /**
     * Tuna.
     */
    TUNA(Items.TUNA_361, Items.RAW_TUNA_359, Items.BURNT_FISH_367, 30, 100.0, 58, 372, 58, 372),

    /**
     * Rainbow fish.
     */
    RAINBOW_FISH(Items.RAINBOW_FISH_10136, Items.RAW_RAINBOW_FISH_10138, Items.BURNT_RAINBOW_FISH_10140, 35, 110.0, 56, 370, 56, 370),

    /**
     * Cave eel.
     */
    CAVE_EEL(Items.CAVE_EEL_5003, Items.RAW_CAVE_EEL_5001, Items.BURNT_CAVE_EEL_5002, 38, 115.0, 38, 332, 38, 332),

    /**
     * Lobster.
     */
    LOBSTER(Items.LOBSTER_379, Items.RAW_LOBSTER_377, Items.BURNT_LOBSTER_381, 40, 120.0, 38, 332, 38, 332),

    /**
     * Giant carp (Fishing contest quest - Once the quest has been completed, this fish can no longer be obtained.).
     */
    GIANT_CARP(Items.GIANT_CARP_337, Items.RAW_GIANT_CARP_338, Items.BURNT_FISH_343, 10, 0.0, 68, 392, 68, 392),

    /**
     * Bass.
     */
    BASS(Items.BASS_365, Items.RAW_BASS_363, Items.BURNT_FISH_367, 43, 130.0, 33, 312, 33, 312),

    /**
     * Swordfish.
     */
    SWORDFISH(Items.SWORDFISH_373, Items.RAW_SWORDFISH_371, Items.BURNT_SWORDFISH_375, 45, 140.0, 18, 292, 30, 310),

    /**
     * Lava eel.
     */
    LAVA_EEL(Items.LAVA_EEL_2149, Items.RAW_LAVA_EEL_2148, Items.BURNT_EEL_3383, 53, 30.0, 256, 256, 256, 256),

    /**
     * Monkfish.
     */
    MONKFISH(Items.MONKFISH_7946, Items.RAW_MONKFISH_7944, Items.BURNT_MONKFISH_7948, 62, 150.0, 11, 275, 13, 280),

    /**
     * Shark.
     */
    SHARK(Items.SHARK_385, Items.RAW_SHARK_383, Items.BURNT_SHARK_387, 80, 210.0, 1, 202, 1, 232),

    /**
     * Sea turtle.
     */
    SEA_TURTLE(Items.SEA_TURTLE_397, Items.RAW_SEA_TURTLE_395, Items.BURNT_SEA_TURTLE_399, 82, 211.3, 1, 202, 1, 222),

    /**
     * Manta ray.
     */
    MANTA_RAY(Items.MANTA_RAY_391, Items.RAW_MANTA_RAY_389, Items.BURNT_MANTA_RAY_393, 91, 216.0, 1, 202, 1, 222),

    /**
     * Karambwan.
     */
    KARAMBWAN(Items.COOKED_KARAMBWAN_3144, Items.RAW_KARAMBWAN_3142, Items.POISON_KARAMBWAN_3146, 30, 190.0, 70, 255, 70, 255),

    /**
     * Thin snail.
     */
    THIN_SNAIL(Items.THIN_SNAIL_MEAT_3369, Items.THIN_SNAIL_3363, Items.BURNT_SNAIL_3375, 12, 70.0, 93, 444, 93, 444),

    /**
     * Lean snail.
     */
    LEAN_SNAIL(Items.LEAN_SNAIL_MEAT_3371, Items.LEAN_SNAIL_3365, Items.BURNT_SNAIL_3375, 17, 80.0, 85, 428, 93, 444),

    /**
     * Fat snail.
     */
    FAT_SNAIL(Items.FAT_SNAIL_MEAT_3373, Items.FAT_SNAIL_3367, Items.BURNT_SNAIL_3375, 22, 95.0, 73, 402, 73, 402),

    /**
     * Bread.
     */
    BREAD(Items.BREAD_2309, Items.BREAD_DOUGH_2307, Items.BURNT_BREAD_2311, 1, 40.0, 118, 492, 118, 492),

    /**
     * Pitta bread.
     */
    PITTA_BREAD(Items.PITTA_BREAD_1865, Items.PITTA_DOUGH_1863, Items.BURNT_PITTA_BREAD_1867, 58, 40.0, 118, 492, 118, 492),

    /**
     * Cake.
     */
    CAKE(Items.CAKE_1891, Items.UNCOOKED_CAKE_1889, Items.BURNT_CAKE_1903, 40, 180.0, 0, 0, 38, 332),

    /**
     * Beef.
     */
    BEEF(Items.COOKED_MEAT_2142, Items.RAW_BEEF_2132, Items.BURNT_MEAT_2146, 1, 30.0, 128, 512, 128, 512),

    /**
     * Rat meat.
     */
    RAT_MEAT(Items.COOKED_MEAT_2142, Items.RAW_RAT_MEAT_2134, Items.BURNT_MEAT_2146, 1, 30.0, 128, 512, 128, 512),

    /**
     * Bear meat.
     */
    BEAR_MEAT(Items.COOKED_MEAT_2142, Items.RAW_BEAR_MEAT_2136, Items.BURNT_MEAT_2146, 1, 30.0, 128, 512, 128, 512),

    /**
     * Yak meat.
     */
    YAK_MEAT(Items.COOKED_MEAT_2142, Items.RAW_YAK_MEAT_10816, Items.BURNT_MEAT_2146, 1, 30.0, 128, 512, 128, 512),

    /**
     * Skewer chompy.
     */
    SKEWER_CHOMPY(Items.COOKED_CHOMPY_2878, Items.SKEWERED_CHOMPY_7230, Items.RUINED_CHOMPY_2880, 30, 100.0, 200, 255, 200, 255),

    /**
     * Roasted chompy.
     */
    ROASTED_CHOMPY(Items.COOKED_CHOMPY_7228, Items.COOKED_CHOMPY_7229, Items.BURNT_CHOMPY_7226, 30, 100.0, 200, 255, 200, 255),

    /**
     * Skewer roast rabbit.
     */
    SKEWER_ROAST_RABBIT(Items.ROAST_RABBIT_7223, Items.SKEWERED_RABBIT_7224, Items.BURNT_RABBIT_7222, 16, 72.0, 160, 255, 160, 255),

    /**
     * Skewer roast bird.
     */
    SKEWER_ROAST_BIRD(Items.ROAST_BIRD_MEAT_9980, Items.SKEWERED_BIRD_MEAT_9984, Items.BURNT_BIRD_MEAT_9982, 11, 62.0, 155, 255, 155, 255),

    /**
     * Skewer roast beast.
     */
    SKEWER_ROAST_BEAST(Items.ROAST_BEAST_MEAT_9988, Items.SKEWERED_BEAST_9992, Items.BURNT_BEAST_MEAT_9990, 21, 82.5, 180, 255, 180, 255),

    /**
     * Redberry pie.
     */
    REDBERRY_PIE(Items.REDBERRY_PIE_2325, Items.UNCOOKED_BERRY_PIE_2321, Items.BURNT_PIE_2329, 10, 78.0, 0, 0, 98, 452),

    /**
     * Meat pie.
     */
    MEAT_PIE(Items.MEAT_PIE_2327, Items.UNCOOKED_MEAT_PIE_2319, Items.BURNT_PIE_2329, 20, 110.0, 0, 0, 78, 412),

    /**
     * Mud pie.
     */
    MUD_PIE(Items.MUD_PIE_7170, Items.RAW_MUD_PIE_7168, Items.BURNT_PIE_2329, 29, 128.0, 0, 0, 58, 372),

    /**
     * Apple pie.
     */
    APPLE_PIE(Items.APPLE_PIE_2323, Items.UNCOOKED_APPLE_PIE_2317, Items.BURNT_PIE_2329, 30, 130.0, 0, 0, 58, 372),

    /**
     * Garden pie.
     */
    GARDEN_PIE(Items.GARDEN_PIE_7178, Items.RAW_GARDEN_PIE_7176, Items.BURNT_PIE_2329, 34, 138.0, 0, 0, 48, 352),

    /**
     * Fish pie.
     */
    FISH_PIE(Items.FISH_PIE_7188, Items.RAW_FISH_PIE_7186, Items.BURNT_PIE_2329, 47, 164.0, 0, 0, 38, 332),

    /**
     * Admiral pie.
     */
    ADMIRAL_PIE(Items.ADMIRAL_PIE_7198, Items.RAW_ADMIRAL_PIE_7196, Items.BURNT_PIE_2329, 70, 210.0, 0, 0, 15, 270),

    /**
     * Wild pie.
     */
    WILD_PIE(Items.WILD_PIE_7208, Items.RAW_WILD_PIE_7206, Items.BURNT_PIE_2329, 85, 240.0, 0, 0, 1, 222),

    /**
     * Summer pie.
     */
    SUMMER_PIE(Items.SUMMER_PIE_7218, Items.RAW_SUMMER_PIE_7216, Items.BURNT_PIE_2329, 95, 260.0, 0, 0, 1, 212),

    /**
     * Pizza plain.
     */
    PIZZA_PLAIN(Items.PLAIN_PIZZA_2289, Items.UNCOOKED_PIZZA_2287, Items.BURNT_PIZZA_2305, 35, 143.0, 0, 0, 48, 352),

    /**
     * Bowl stew.
     */
    BOWL_STEW(Items.STEW_2003, Items.UNCOOKED_STEW_2001, Items.BURNT_STEW_2005, 25, 117.0, 68, 392, 68, 392),

    /**
     * Bowl curry.
     */
    BOWL_CURRY(Items.CURRY_2011, Items.UNCOOKED_CURRY_2009, Items.BURNT_CURRY_2013, 60, 280.0, 38, 332, 38, 332),

    /**
     * Bowl nettle.
     */
    BOWL_NETTLE(Items.NETTLE_TEA_4239, Items.NETTLE_WATER_4237, Items.BOWL_1923, 20, 52.0, 78, 412, 78, 412),

    /**
     * Bowl of hot water.
     */
    BOWL_OF_HOT_WATER(Items.BOWL_OF_HOT_WATER_4456, Items.BOWL_OF_WATER_1921, Items.BOWL_1923, 0, 0.0, 128, 512, 128, 512),

    /**
     * Bowl egg.
     */
    BOWL_EGG(Items.SCRAMBLED_EGG_7078, Items.UNCOOKED_EGG_7076, Items.BURNT_EGG_7090, 13, 50.0, 0, 0, 90, 438),

    /**
     * Bowl onion.
     */
    BOWL_ONION(Items.FRIED_ONIONS_7084, Items.CHOPPED_ONION_1871, Items.BURNT_ONION_7092, 43, 60.0, 36, 322, 36, 322),

    /**
     * Bowl mushroom.
     */
    BOWL_MUSHROOM(Items.FRIED_MUSHROOMS_7082, Items.SLICED_MUSHROOMS_7080, Items.BURNT_MUSHROOM_7094, 46, 60.0, 16, 282, 16, 282),

    /**
     * Baked potato.
     */
    BAKED_POTATO(Items.BAKED_POTATO_6701, Items.POTATO_1942, Items.BURNT_POTATO_6699, 7, 15.0, 0, 0, 108, 472),

    /**
     * Cup of hot water.
     */
    CUP_OF_HOT_WATER(Items.CUP_OF_HOT_WATER_4460, Items.CUP_OF_WATER_4458, Items.EMPTY_CUP_1980, 0, 0.0, 128, 512, 128, 512),

    /**
     * Sweetcorn.
     */
    SWEETCORN(Items.COOKED_SWEETCORN_5988, Items.SWEETCORN_5986, Items.BURNT_SWEETCORN_5990, 28, 104.0, 78, 412, 78, 412),

    /**
     * Barley malt.
     */
    BARLEY_MALT(Items.BARLEY_MALT_6008, Items.BARLEY_6006, Items.BARLEY_MALT_6008, 1, 1.0, 0, 0, 0, 0),

    /**
     * Raw oomlie.
     */
    RAW_OOMLIE(Items.RAW_OOMLIE_2337, 0, Items.BURNT_OOMLIE_2426, 50, 0.0, 0, 0, 0, 0),

    /**
     * Oomlie wrap.
     */
    OOMLIE_WRAP(Items.COOKED_OOMLIE_WRAP_2343, Items.WRAPPED_OOMLIE_2341, Items.BURNT_OOMLIE_WRAP_2345, 50, 30.0, 106, 450, 112, 476),

    /**
     * Seaweed.
     */
    SEAWEED(Items.SEAWEED_401, 0, Items.SODA_ASH_1781, 0, 0.0, 0, 0, 0, 0),

    /**
     * Sinew.
     */
    SINEW(Items.SINEW_9436, Items.RAW_BEEF_2132, Items.SINEW_9436, 0, 3.0, 0, 0, 0, 0),

    /**
     * Swamp paste.
     */
    SWAMP_PASTE(Items.SWAMP_PASTE_1941, Items.RAW_SWAMP_PASTE_1940, Items.SWAMP_PASTE_1941, 0, 2.0, 0, 0, 0, 0),

    /**
     * Swamp weed.
     */
    SWAMP_WEED(Items.SODA_ASH_1781, Items.SWAMP_WEED_10978, Items.SODA_ASH_1781, 0, 3.0, 0, 0, 0, 0);

    companion object {
        /**
         * Maps raw item IDs to [CookableItems].
         */
        val cookingMap = HashMap<Int, CookableItems>()

        /**
         * Maps cooked item IDs for intentional burns.
         */
        val intentionalBurnMap = HashMap<Int, CookableItems?>()

        /**
         * Cooking gauntlet bonuses: [successRate, levelReq].
         */
        val gauntletValues = HashMap<Int, IntArray>()

        /**
         * Lumbridge range bonuses: [successRate, levelReq].
         */
        val lumbridgeRangeValues = HashMap<Int, IntArray>()


        init {
            for (item in values()) {
                cookingMap.putIfAbsent(item.raw, item)
                intentionalBurnMap.putIfAbsent(item.cooked, item)
            }

            gauntletValues[Items.RAW_LOBSTER_377] = intArrayOf(55, 368)
            gauntletValues[Items.RAW_SWORDFISH_371] = intArrayOf(30, 310)
            gauntletValues[Items.RAW_MONKFISH_7944] = intArrayOf(24, 290)
            gauntletValues[Items.RAW_SHARK_383] = intArrayOf(15, 270)

            lumbridgeRangeValues[Items.BREAD_DOUGH_2307] = intArrayOf(128, 512)
            lumbridgeRangeValues[Items.RAW_BEEF_2132] = intArrayOf(138, 532)
            lumbridgeRangeValues[Items.RAW_RAT_MEAT_2134] = intArrayOf(138, 532)
            lumbridgeRangeValues[Items.RAW_BEAR_MEAT_2136] = intArrayOf(138, 532)
            lumbridgeRangeValues[Items.RAW_YAK_MEAT_10816] = intArrayOf(138, 532)
            lumbridgeRangeValues[Items.RAW_CHICKEN_2138] = intArrayOf(138, 532)
            lumbridgeRangeValues[Items.RAW_SHRIMPS_317] = intArrayOf(138, 532)
            lumbridgeRangeValues[Items.RAW_ANCHOVIES_321] = intArrayOf(138, 532)
            lumbridgeRangeValues[Items.RAW_SARDINE_327] = intArrayOf(128, 512)
            lumbridgeRangeValues[Items.RAW_HERRING_345] = intArrayOf(118, 492)
            lumbridgeRangeValues[Items.RAW_MACKEREL_353] = intArrayOf(108, 472)
            lumbridgeRangeValues[Items.UNCOOKED_BERRY_PIE_2321] = intArrayOf(108, 462)
            lumbridgeRangeValues[Items.THIN_SNAIL_3363] = intArrayOf(103, 464)
            lumbridgeRangeValues[Items.RAW_TROUT_335] = intArrayOf(98, 452)
            lumbridgeRangeValues[Items.LEAN_SNAIL_3365] = intArrayOf(95, 448)
            lumbridgeRangeValues[Items.RAW_COD_341] = intArrayOf(93, 442)
            lumbridgeRangeValues[Items.RAW_PIKE_349] = intArrayOf(88, 432)
            lumbridgeRangeValues[Items.UNCOOKED_MEAT_PIE_2319] = intArrayOf(88, 432)
            lumbridgeRangeValues[Items.FAT_SNAIL_3367] = intArrayOf(83, 422)
            lumbridgeRangeValues[Items.UNCOOKED_STEW_2001] = intArrayOf(78, 412)
            lumbridgeRangeValues[Items.RAW_SALMON_331] = intArrayOf(78, 402)
            lumbridgeRangeValues[Items.RAW_GIANT_CARP_338] = intArrayOf(78, 402)
        }

        /**
         * Gets [CookableItems] by raw item id.
         */
        @JvmStatic
        fun forId(id: Int): CookableItems? = cookingMap[id]

        /**
         * Gets burnt version of the item by raw id.
         */
        fun getBurnt(id: Int): Item = Item(cookingMap[id]!!.burnt)

        /**
         * Checks if item id is intentionally burnable.
         */
        fun intentionalBurn(id: Int): Boolean = intentionalBurnMap[id] != null

        /**
         * Gets intentionally burnt item by cooked id.
         */
        fun getIntentionalBurn(id: Int): Item = Item(intentionalBurnMap[id]!!.burnt)
    }
}
