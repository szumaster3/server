package content.data

import org.rs.consts.Items

enum class MeatState {
    INEDIBLE_RAW,
    INEDIBLE_BURNT,
    INEDIBLE_SPECIAL,
    EDIBLE_COOKED,
}

enum class Meat(
    val id: Int,
    val state: MeatState,
) {
    ENCHANTED_BEEF(
        id = Items.ENCHANTED_BEEF_522,
        state = MeatState.INEDIBLE_SPECIAL,
    ),
    ENCHANTED_RAT_MEAT(
        id = Items.ENCHANTED_RAT_MEAT_523,
        state = MeatState.INEDIBLE_SPECIAL,
    ),
    ENCHANTED_BEAR_MEAT(
        id = Items.ENCHANTED_BEAR_MEAT_524,
        state = MeatState.INEDIBLE_SPECIAL,
    ),
    ENCHANTED_CHICKEN(
        id = Items.ENCHANTED_CHICKEN_525,
        state = MeatState.INEDIBLE_SPECIAL,
    ),
    RAW_UGTHANKI_MEAT(
        Items.RAW_UGTHANKI_MEAT_1859,
        state = MeatState.INEDIBLE_RAW,
    ),
    UGTHANKI_MEAT(
        id = Items.UGTHANKI_MEAT_1861,
        state = MeatState.EDIBLE_COOKED,
    ),
    RAW_BEEF(
        id = Items.RAW_BEEF_2132,
        state = MeatState.INEDIBLE_RAW,
    ),
    RAW_RAT_MEAT(
        id = Items.RAW_RAT_MEAT_2134,
        state = MeatState.INEDIBLE_RAW,
    ),
    RAW_BEAR_MEAT(
        id = Items.RAW_BEAR_MEAT_2136,
        state = MeatState.INEDIBLE_RAW,
    ),
    RAW_CHICKEN(
        id = Items.RAW_CHICKEN_2138,
        state = MeatState.INEDIBLE_RAW,
    ),
    COOKED_CHICKEN(
        id = Items.COOKED_CHICKEN_2140,
        state = MeatState.EDIBLE_COOKED,
    ),
    BURNT_CHICKEN(
        id = Items.BURNT_CHICKEN_2144,
        state = MeatState.INEDIBLE_BURNT,
    ),
    COOKED_MEAT(
        id = Items.COOKED_MEAT_2142,
        state = MeatState.EDIBLE_COOKED,
    ),
    BURNT_MEAT(
        id = Items.BURNT_MEAT_2146,
        state = MeatState.INEDIBLE_BURNT,
    ),
    THIN_SNAIL_MEAT(
        id = Items.THIN_SNAIL_MEAT_3369,
        state = MeatState.EDIBLE_COOKED,
    ),
    LEAN_SNAIL_MEAT(
        id = Items.LEAN_SNAIL_MEAT_3371,
        state = MeatState.EDIBLE_COOKED,
    ),
    FAT_SNAIL_MEAT(
        id = Items.FAT_SNAIL_MEAT_3373,
        state = MeatState.EDIBLE_COOKED,
    ),
    RAW_BEEF_UNDEAD(
        id = Items.RAW_BEEF_4287,
        state = MeatState.INEDIBLE_RAW,
    ),
    RAW_CHICKEN_UNDEAD(
        id = Items.RAW_CHICKEN_4289,
        state = MeatState.INEDIBLE_RAW,
    ),
    COOKED_CHICKEN_UNDEAD(
        id = Items.COOKED_CHICKEN_4291,
        state = MeatState.EDIBLE_COOKED,
    ),
    COOKED_MEAT_UNDEAD(
        id = Items.COOKED_MEAT_4293,
        state = MeatState.EDIBLE_COOKED,
    ),
    RAW_CRAB_MEAT(
        id = Items.CRAB_MEAT_7518,
        state = MeatState.INEDIBLE_RAW,
    ),
    BURNT_CRAB_MEAT(
        id = Items.BURNT_CRAB_MEAT_7520,
        state = MeatState.INEDIBLE_BURNT,
    ),
    COOKED_CRAB_MEAT_5(
        id = Items.COOKED_CRAB_MEAT_7521,
        state = MeatState.EDIBLE_COOKED,
    ),
    COOKED_CRAB_MEAT_4(
        id = Items.COOKED_CRAB_MEAT_7523,
        state = MeatState.EDIBLE_COOKED,
    ),
    COOKED_CRAB_MEAT_3(
        id = Items.COOKED_CRAB_MEAT_7524,
        state = MeatState.EDIBLE_COOKED,
    ),
    COOKED_CRAB_MEAT_2(
        id = Items.COOKED_CRAB_MEAT_7525,
        state = MeatState.EDIBLE_COOKED,
    ),
    COOKED_CRAB_MEAT_1(
        id = Items.COOKED_CRAB_MEAT_7526,
        state = MeatState.EDIBLE_COOKED,
    ),
    GROUND_CRAB_MEAT(
        id = Items.GROUND_CRAB_MEAT_7527,
        state = MeatState.INEDIBLE_RAW,
    ),
    LOCUST_MEAT(
        id = Items.LOCUST_MEAT_9052,
        state = MeatState.EDIBLE_COOKED,
    ),
    RAW_BIRD_MEAT(
        id = Items.RAW_BIRD_MEAT_9978,
        state = MeatState.INEDIBLE_RAW,
    ),
    ROAST_BIRD_MEAT(
        id = Items.ROAST_BIRD_MEAT_9980,
        state = MeatState.EDIBLE_COOKED,
    ),
    BURNT_BIRD_MEAT(
        id = Items.BURNT_BIRD_MEAT_9982,
        state = MeatState.INEDIBLE_BURNT,
    ),
    SKEWERED_BIRD_MEAT(
        id = Items.SKEWERED_BIRD_MEAT_9984,
        state = MeatState.INEDIBLE_RAW,
    ),
    RAW_BEAST_MEAT(
        id = Items.RAW_BEAST_MEAT_9986,
        state = MeatState.INEDIBLE_RAW,
    ),
    ROAST_BEAST_MEAT(
        id = Items.ROAST_BEAST_MEAT_9988,
        state = MeatState.EDIBLE_COOKED,
    ),
    BURNT_BEAST_MEAT(
        id = Items.BURNT_BEAST_MEAT_9990,
        state = MeatState.INEDIBLE_BURNT,
    ),
    RAW_YAK_MEAT(
        id = Items.RAW_YAK_MEAT_10816,
        state = MeatState.INEDIBLE_RAW,
    ),
    RAW_PAWYA_MEAT(
        id = Items.RAW_PAWYA_MEAT_12535,
        state = MeatState.INEDIBLE_RAW,
    ),
    ENCHANTED_PAWYA_MEAT(
        id = Items.ENCHANTED_PAWYA_MEAT_12546,
        state = MeatState.INEDIBLE_SPECIAL,
    ),
}
