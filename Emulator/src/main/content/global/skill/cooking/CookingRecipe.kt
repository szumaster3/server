package content.global.skill.cooking

import shared.consts.Animations
import shared.consts.Items

/**
 * Cooking recipe definitions.
 */
enum class CookingRecipe(val ingredientID: Int, val secondaryID: Int, val productID: Int, val requiredLevel: Int = 1, val animation: Int? = null, val requiresKnife: Boolean = false, val message: String? = null, val xpReward: Double? = null, val returnsContainer: Int? = null) {
    CHOCOLATE_CAKE(Items.CHOCOLATE_BAR_1973, Items.CAKE_1891, Items.CHOCOLATE_CAKE_1897, 50, message = "You add chocolate to the cake.", xpReward = 30.0),
    CHOCOLATE_CAKE_ALT(Items.CHOCOLATE_DUST_1975, Items.CAKE_1891, Items.CHOCOLATE_CAKE_1897, 50, message = "You add chocolate to the cake.", xpReward = 30.0),
    CALQUAT_KEG(Items.CALQUAT_FRUIT_5980, Items.KNIFE_946, Items.CALQUAT_KEG_5769, requiresKnife = true, animation = Animations.CARVE_CALQUAT_KEG_2290, message = "You carve the calquat fruit."),
    CHOCOLATE_DUST(Items.CHOCOLATE_BAR_1973, Items.KNIFE_946, Items.CHOCOLATE_DUST_1975, requiresKnife = true, animation = Animations.CUTTING_CHOCOLATE_BAR_1989, message = "You cut the chocolate."),
    CHOPPED_TUNA(Items.TUNA_361, Items.BOWL_1923, Items.CHOPPED_TUNA_7086, requiresKnife = true, animation = -1, message = "You chop the tuna into the bowl."),
    CHOPPED_ONION(Items.ONION_1957, Items.BOWL_1923, Items.CHOPPED_ONION_1871, requiresKnife = true, animation = -1, message = "You cut the onion into the bowl."),
    CHOPPED_GARLIC(Items.GARLIC_1550, Items.BOWL_1923, Items.CHOPPED_GARLIC_7074, requiresKnife = true, animation = -1, message = "You chop the garlic into the bowl."),
    CHOPPED_TOMATO(Items.TOMATO_1982, Items.BOWL_1923, Items.CHOPPED_TOMATO_1869, requiresKnife = true, animation = -1, message = "You chop the tomato into the bowl."),
    CHOPPED_UGTHANKI(Items.UGTHANKI_MEAT_1861, Items.BOWL_1923, Items.CHOPPED_UGTHANKI_1873, requiresKnife = true, animation = -1, message = "You chop the meat into the bowl."),

    SLICED_MUSHROOMS(Items.MUSHROOM_6004, Items.BOWL_1923, Items.SLICED_MUSHROOMS_7080, requiresKnife = true, animation = -1, message = "You slice the mushrooms into the bowl."),
    MINCED_MEAT(Items.COOKED_MEAT_2142, Items.BOWL_1923, Items.MINCED_MEAT_7070, requiresKnife = true, animation = -1, message = "You chop the meat into the bowl."),
    SPICY_MINCED_MEAT(Items.COOKED_MEAT_2142, Items.SPICE_2007, Items.SPICY_MINCED_MEAT_9996, requiresKnife = true, animation = -1, message = "You chop the meat into the bowl."),
    SPICY_MINCED_MEAT_ALT(Items.COOKED_MEAT_2142, Items.GNOME_SPICE_2169, Items.SPICY_MINCED_MEAT_9996, requiresKnife = true, animation = -1, message = "You chop the meat into the bowl."),

    SPICY_SAUCE(Items.CHOPPED_GARLIC_7074, Items.GNOME_SPICE_2169, Items.SPICY_SAUCE_7072, 9, message = "You mix the ingredients to make spicy sauce.", xpReward = 25.0),
    SPICY_TOMATO(Items.CHOPPED_TOMATO_1869, Items.SPICE_2007, Items.SPICY_TOMATO_9994, message = "You mix the ingredients to make spicy tomatoes."),
    SPICY_TOMATO_ALT(Items.CHOPPED_TOMATO_1869, Items.GNOME_SPICE_2169, Items.SPICY_TOMATO_9994, message = "You mix the ingredients to make spicy tomatoes."),

    SWEETCORN(Items.COOKED_SWEETCORN_5988, Items.BOWL_1923, Items.SWEETCORN_7088, requiresKnife = true, requiredLevel = 67, animation = -1, message = "You cut the sweetcorn into the bowl."),
    UNCOOKED_EGG(Items.EGG_1944, Items.BOWL_1923, Items.UNCOOKED_EGG_7076, message = "You carefully break the egg into the bowl."),
    ONION_AND_TOMATO(Items.CHOPPED_ONION_1871, Items.TOMATO_1982, Items.ONION_AND_TOMATO_1875, requiresKnife = true, message = "You cut the onion into the bowl."),
    ONION_AND_TOMATO_ALT(Items.CHOPPED_TOMATO_1869, Items.ONION_1957, Items.ONION_AND_TOMATO_1875, requiresKnife = true, message = "You cut the tomato into the bowl."),
    KEBAB_MIX(Items.ONION_AND_TOMATO_1875, Items.UGTHANKI_MEAT_1861, Items.KEBAB_MIX_1881, requiresKnife = true, message = "You mix the ugthanki meat with the onion and tomato."),
    KEBAB_MIX_ALT(Items.UGTHANKI_AND_ONION_1877, Items.TOMATO_1982, Items.KEBAB_MIX_1881, requiresKnife = true, message = "You mix the onion and tomato with the tomato."),
    UGTHANKI_AND_ONION(Items.CHOPPED_ONION_1871, Items.UGTHANKI_MEAT_1861, Items.UGTHANKI_AND_ONION_1877, requiresKnife = true, message = "You mix the chopped onion with the ugthanki meat."),
    UGTHANKI_AND_TOMATO(Items.CHOPPED_TOMATO_1869, Items.UGTHANKI_MEAT_1861, Items.UGTHANKI_AND_TOMATO_1879, requiresKnife = true, message = "You mix the chopped tomato with the ugthanki meat."),
    SUPER_KEBAB(Items.KEBAB_1971, Items.RED_HOT_SAUCE_4610, Items.SUPER_KEBAB_4608, message = "You add red hot sauce to make a super kebab."),
    SUPER_KEBAB_ALT(Items.UGTHANKI_KEBAB_1883, Items.RED_HOT_SAUCE_4610, Items.SUPER_KEBAB_4608, message = "You add red hot sauce to make a super kebab."),
    SUPER_KEBAB_ALT_ALT(Items.UGTHANKI_KEBAB_1885, Items.RED_HOT_SAUCE_4610, Items.SUPER_KEBAB_4608, message = "You add red hot sauce to make a super kebab."),
    CHILLI_CON_CARNE(Items.SPICY_SAUCE_7072, Items.MINCED_MEAT_7070, Items.CHILLI_CON_CARNE_7062, 9, message = "You mix the ingredients to make the topping.", xpReward = 25.0, returnsContainer = Items.BOWL_1923),
    CHILLI_CON_CARNE_ALT(Items.SPICY_SAUCE_7072, Items.COOKED_MEAT_2142, Items.CHILLI_CON_CARNE_7062, 9, requiresKnife = true, message = "You put the cut up meat into the bowl.", xpReward = 25.0),
    TUNA_AND_CORN(Items.SWEETCORN_7088, Items.TUNA_361, Items.TUNA_AND_CORN_7068, 1, animation = -1, requiresKnife = false,  message = "You mix the tuna with the sweetcorn."),
    TUNA_AND_CORN_ALT(Items.CHOPPED_TUNA_7086, Items.COOKED_SWEETCORN_5988, Items.TUNA_AND_CORN_7068, requiredLevel = 67, message = "You mix the ingredients to make the topping.", xpReward = 204.0),
    EGG_AND_TOMATO(Items.SCRAMBLED_EGG_7078, Items.TOMATO_1982, Items.EGG_AND_TOMATO_7064, 23, message = "You mix the scrambled egg with the tomato.", xpReward = 50.0),
    EGG_AND_TOMATO_ALT(Items.SCRAMBLED_EGG_7078, Items.CHOPPED_TOMATO_1869, Items.EGG_AND_TOMATO_7064, requiredLevel = 23, animation = -1, message = "You mix the scrambled egg with the chopped tomatoes.", xpReward = 50.0, returnsContainer = Items.BOWL_1923),
    WRAPPED_OOMLIE(Items.RAW_OOMLIE_2337, Items.PALM_LEAF_2339, Items.WRAPPED_OOMLIE_2341, 50, message = "You wrap the raw oomlie in the palm leaf."),
    MUSHROOM_AND_ONION(Items.FRIED_MUSHROOMS_7082, Items.FRIED_ONIONS_7084, Items.MUSHROOM_AND_ONION_7066, 57, message = "You mix the fried onions and mushrooms.", xpReward = 120.0, returnsContainer = Items.BOWL_1923),

    INCOMPLETE_PIZZA(Items.PIZZA_BASE_2283, Items.TOMATO_1982, Items.INCOMPLETE_PIZZA_2285, 35, message = "You add the tomato to the pizza."),
    UNCOOKED_PIZZA(Items.CHEESE_1985, Items.INCOMPLETE_PIZZA_2285, Items.UNCOOKED_PIZZA_2287, 35, message = "You add the cheese to the pizza."),
    MEAT_PIZZA(Items.COOKED_MEAT_2142, Items.PLAIN_PIZZA_2289, Items.MEAT_PIZZA_2293, 45, message = "You add the meat to the pizza.", xpReward = 26.0),
    MEAT_PIZZA_ALT(Items.COOKED_CHICKEN_2140, Items.PLAIN_PIZZA_2289, Items.MEAT_PIZZA_2293, 45, message = "You add the meat to the pizza.", xpReward = 26.0),
    ANCHOVY_PIZZA(Items.ANCHOVIES_319, Items.PLAIN_PIZZA_2289, Items.ANCHOVY_PIZZA_2297, 55, message = "You add the anchovies to the pizza.", xpReward = 39.0),
    PINEAPPLE_PIZZA(Items.PINEAPPLE_CHUNKS_2116, Items.PLAIN_PIZZA_2289, Items.PINEAPPLE_PIZZA_2301, 65, message = "You add the pineapple to the pizza.", xpReward = 52.0),
    PINEAPPLE_PIZZA_ALT(Items.PINEAPPLE_RING_2118, Items.PLAIN_PIZZA_2289, Items.PINEAPPLE_PIZZA_2301, 65, message = "You add the pineapple to the pizza.", xpReward = 52.0),

    PIE_SHELL_FIRST_PART(Items.PASTRY_DOUGH_1953, Items.PIE_DISH_2313, Items.PIE_SHELL_2315, message = "You put the pastry dough into the pie dish to make a pie shell."),
    UNCOOKED_BERRY_PIE_FIRST_PART(Items.REDBERRIES_1951, Items.PIE_SHELL_2315, Items.UNCOOKED_BERRY_PIE_2321, 10, message = "You fill the pie with redberries."),
    UNCOOKED_MEAT_PIE_FIRST_PART(Items.COOKED_MEAT_2142, Items.PIE_SHELL_2315, Items.UNCOOKED_MEAT_PIE_2319, 20, message = "You fill the pie with cooked meat."),
    UNCOOKED_MEAT_PIE_ALT_FIRST_PART(Items.COOKED_CHICKEN_2140, Items.PIE_SHELL_2315, Items.UNCOOKED_MEAT_PIE_2319, 20, message = "You fill the pie with cooked meat."),
    MUD_PIE_FIRST_PART(Items.COMPOST_6032, Items.PIE_SHELL_2315, Items.PART_MUD_PIE_7164, 29, message = "You fill the pie with compost.", returnsContainer = Items.BUCKET_1925),
    UNCOOKED_APPLE_PIE(Items.COOKING_APPLE_1955, Items.PIE_SHELL_2315, Items.UNCOOKED_APPLE_PIE_2317, 30, message = "You fill the pie with apple slices."),
    GARDEN_PIE_FIRST_PART(Items.TOMATO_1982, Items.PIE_SHELL_2315, Items.PART_GARDEN_PIE_7172, 34, message = "You fill the pie with tomato."),
    FISH_PIE_FIRST_PART(Items.TROUT_333, Items.PIE_SHELL_2315, Items.PART_FISH_PIE_7182, 47, message = "You fill the pie with trout."),
    ADMIRAL_PIE_FIRST_PART(Items.SALMON_329, Items.PIE_SHELL_2315, Items.PART_ADMIRAL_PIE_7192, 70, message = "You fill the pie with salmon."),
    WILD_PIE_FIRST_PART(Items.RAW_BEAR_MEAT_2136, Items.PIE_SHELL_2315, Items.PART_WILD_PIE_7202, 85, message = "You fill the pie with bear meat."),
    SUMMER_PIE_FIRST_PART(Items.STRAWBERRY_5504, Items.PIE_SHELL_2315, Items.PART_SUMMER_PIE_7212, 95, message = "You fill the pie with strawberry."),

    PART_MUD_PIE_SECOND_PART(Items.BUCKET_OF_WATER_1929, Items.PART_MUD_PIE_7164, Items.PART_MUD_PIE_7166, 29, message = "You add water to the mud pie.", returnsContainer = Items.BUCKET_1925),
    PART_GARDEN_PIE_SECOND_PART(Items.ONION_1957, Items.PART_GARDEN_PIE_7172, Items.PART_GARDEN_PIE_7174, 34, message = "You add onion to the garden pie."),
    PART_FISH_PIE_SECOND_PART(Items.COD_339, Items.PART_FISH_PIE_7182, Items.PART_FISH_PIE_7184, 47, message = "You add cod to the fish pie."),
    PART_ADMIRAL_PIE_SECOND_PART(Items.TUNA_361, Items.PART_ADMIRAL_PIE_7192, Items.PART_ADMIRAL_PIE_7194, 70, message = "You add tuna to the admiral pie."),
    PART_WILD_PIE_SECOND_PART(Items.RAW_CHOMPY_2876, Items.PART_WILD_PIE_7202, Items.PART_WILD_PIE_7204, 85, message = "You add chompy to the wild pie."),
    PART_SUMMER_PIE_SECOND_PART(Items.WATERMELON_5982, Items.PART_SUMMER_PIE_7212, Items.PART_SUMMER_PIE_7214, 95, message = "You fill the pie with watermelon slices."),

    RAW_MUD_PIE_SECOND_PART(Items.CLAY_434, Items.PART_MUD_PIE_7166, Items.RAW_MUD_PIE_7168, 29, message = "You prepare a raw mud pie."),
    RAW_GARDEN_PIE_SECOND_PART(Items.CABBAGE_1965, Items.PART_GARDEN_PIE_7174, Items.RAW_GARDEN_PIE_7176, 34, message = "You prepare a raw garden pie."),
    RAW_GARDEN_PIE_ALT_SECOND_PART(Items.CABBAGE_1967, Items.PART_GARDEN_PIE_7174, Items.RAW_GARDEN_PIE_7176, 34, message = "You prepare a raw garden pie."),
    RAW_WILD_PIE_SECOND_PART(Items.RAW_RABBIT_3226, Items.PART_WILD_PIE_7204, Items.RAW_WILD_PIE_7206, 85, message = "You prepare a raw wild pie."),
    RAW_SUMMER_PIE_SECOND_PART(Items.COOKING_APPLE_1955, Items.PART_SUMMER_PIE_7214, Items.RAW_SUMMER_PIE_7216, 95, message = "You prepare a raw summer pie."),
    RAW_ADMIRAL_PIE_SECOND_PART(Items.PART_ADMIRAL_PIE_7194, Items.POTATO_1942, Items.RAW_ADMIRAL_PIE_7196, 70, message = "You prepare an admiral pie."),
    RAW_FISH_PIE_SECOND_PART(Items.PART_FISH_PIE_7184, Items.POTATO_1942, Items.RAW_FISH_PIE_7186, 47, message = "You prepare a fish pie."),

    POTATO_WITH_BUTTER(Items.PAT_OF_BUTTER_6697, Items.BAKED_POTATO_6701, Items.POTATO_WITH_BUTTER_6703, 39, message = "You add the butter to the potato.", xpReward = 40.5),
    POTATO_WITH_CHEESE(Items.POTATO_WITH_BUTTER_6703, Items.CHEESE_1985, Items.POTATO_WITH_CHEESE_6705, 47, message = "You add the topping to the potato.", xpReward = 40.5),
    CHILLI_POTATO(Items.CHILLI_CON_CARNE_7062, Items.POTATO_WITH_BUTTER_6703, Items.CHILLI_POTATO_7054, 41, message = "You add the topping to the potato.", xpReward = 10.0, returnsContainer = Items.BOWL_1923),
    EGG_POTATO(Items.EGG_AND_TOMATO_7064, Items.POTATO_WITH_BUTTER_6703, Items.EGG_POTATO_7056, 51, message = "You add the topping to the potato.", xpReward = 10.0, returnsContainer = Items.BOWL_1923),
    MUSHROOM_POTATO(Items.MUSHROOM_AND_ONION_7066, Items.POTATO_WITH_BUTTER_6703, Items.MUSHROOM_POTATO_7058, 64, message = "You add the topping to the potato.", xpReward = 10.0, returnsContainer = Items.BOWL_1923),
    TUNA_POTATO(Items.TUNA_AND_CORN_7068, Items.POTATO_WITH_BUTTER_6703, Items.TUNA_POTATO_7060, 68, message = "You add the topping to the potato.", xpReward = 10.0, returnsContainer = Items.BOWL_1923),

    SKEWERED_BIRD_MEAT(Items.RAW_BIRD_MEAT_9978, Items.IRON_SPIT_7225, Items.SKEWERED_BIRD_MEAT_9984, 11, message = "You skewer the bird meat."),
    SKEWERED_RABBIT(Items.RAW_RABBIT_3226,Items.IRON_SPIT_7225, Items.SKEWERED_RABBIT_7224, 16, message = "You skewer the rabbit meat."),
    SKEWERED_BEAST(Items.RAW_BEAST_MEAT_9986, Items.IRON_SPIT_7225, Items.SKEWERED_BEAST_9992, 21, message = "You skewer the beast meat."),
    SKEWERED_CHOMPY(Items.RAW_CHOMPY_2876, Items.IRON_SPIT_7225, Items.SKEWERED_CHOMPY_7230, 30, message = "You skewer the chompy bird meat."),

    SPIDER_ON_STICK(Items.SPIDER_CARCASS_6291, Items.SKEWER_STICK_6305, Items.SPIDER_ON_STICK_6293),
    SPIDER_ON_SHAFT(Items.SPIDER_CARCASS_6291, Items.ARROW_SHAFT_52, Items.SPIDER_ON_SHAFT_6295),

    INCOMPLETE_STEW_POTATO(Items.POTATO_1942, Items.BOWL_OF_WATER_1921, Items.INCOMPLETE_STEW_1999, 25, message = "You cut up the potato and put it into the bowl."),
    INCOMPLETE_STEW_MEAT(Items.COOKED_MEAT_2142, Items.BOWL_OF_WATER_1921, Items.INCOMPLETE_STEW_1997, 25, message = "You cut up the meat and put it into the bowl."),
    INCOMPLETE_STEW_MEAT_ALT(Items.COOKED_CHICKEN_2140, Items.BOWL_OF_WATER_1921, Items.INCOMPLETE_STEW_1997, 25, message = "You cut up the meat and put it into the bowl."),

    UNCOOKED_STEW_POTATO(Items.POTATO_1942, Items.INCOMPLETE_STEW_1999, Items.UNCOOKED_STEW_2001, 25, message = "You cut up the potato and put it into the stew."),
    UNCOOKED_STEW_MEAT(Items.COOKED_MEAT_2142, Items.INCOMPLETE_STEW_1997, Items.UNCOOKED_STEW_2001, 25, message = "You cut up the meat and put it into the stew."),
    UNCOOKED_STEW_MEAT_ALT(Items.COOKED_CHICKEN_2140, Items.INCOMPLETE_STEW_1997, Items.UNCOOKED_STEW_2001, 25, message = "You cut up the meat and put it into the stew."),
    UNCOOKED_STEW_SPICE(Items.UNCOOKED_STEW_2001, Items.SPICE_2007, Items.UNCOOKED_CURRY_2009,60, message = "You mix the spice with the stew."),

    NETTLE_WATER(Items.NETTLES_4241, Items.BOWL_OF_WATER_1921, Items.NETTLE_WATER_4237, message = "You place the nettles into the bowl of water."),
    NETTLE_TEA_MILKY(Items.NETTLE_TEA_4239, Items.BUCKET_OF_MILK_1927, Items.NETTLE_TEA_4240, 1, returnsContainer = Items.BUCKET_1925, message = "You add the milk to the nettle tea."),
    NETTLE_TEA_MILKY_ALT(Items.PORCELAIN_CUP_4244, Items.BUCKET_OF_MILK_1927, Items.CUP_OF_TEA_4246, 1, returnsContainer = Items.BUCKET_1925, message = "You add the milk to the tea."),
    NETTLE_TEA_CUP(Items.NETTLE_TEA_4239, Items.EMPTY_CUP_1980, Items.CUP_OF_TEA_4242, returnsContainer = Items.BOWL_1923, message = "You fill the cup with nettle tea."),
    NETTLE_TEA_CUP_ALT(Items.NETTLE_TEA_4240, Items.EMPTY_CUP_1980, Items.CUP_OF_TEA_4243, returnsContainer = Items.BOWL_1923, message = "You fill the cup with the tea."),
    NETTLE_TEA_PORCELAIN_CUP(Items.NETTLE_TEA_4239,  Items.PORCELAIN_CUP_4244, Items.CUP_OF_TEA_4245, returnsContainer = Items.BOWL_1923, message = "You pour the nettle tea into the porcelain cup."),
    NETTLE_TEA_MILKY_PORCELAIN_CUP(Items.NETTLE_TEA_4240, Items.PORCELAIN_CUP_4244, Items.CUP_OF_TEA_4246, returnsContainer = Items.BOWL_1923, message = "You pour the tea into the porcelain cup.");

    companion object {
        private val recipeMap: MutableMap<Int, CookingRecipe> = HashMap()

        val INGREDIENT_IDS = CookingRecipe.values().map { it.ingredientID }.toIntArray()
        val SECONDARY_IDS = CookingRecipe.values().map { it.secondaryID }.toIntArray()

        init {
            for (recipe in values()) {
                recipeMap[recipe.ingredientID] = recipe
            }
        }

        @JvmStatic
        fun forId(id: Int): CookingRecipe? = recipeMap[id]
    }
}