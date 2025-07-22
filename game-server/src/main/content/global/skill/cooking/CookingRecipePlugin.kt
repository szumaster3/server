package content.global.skill.cooking

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import kotlin.math.min

/**
 * Handles cooking recipes processing logic.
 */
class CookingRecipePlugin : InteractionListener {

    override fun defineListeners() {
        for (recipe in CookingRecipe.values()) {
            onUseWith(IntType.ITEM, recipe.ingredientIds, recipe.secondaryId) { player, used, with ->
                if (!hasLevelDyn(player, Skills.COOKING, recipe.requiredLevel)) {
                    sendMessage(player, "You need a Cooking level of at least ${recipe.requiredLevel} to make this.")
                    return@onUseWith true
                }

                val (ingredient, secondary) = if (used.id in recipe.ingredientIds) used to with else with to used

                if (recipe.requiresKnife && !inInventory(player, Items.KNIFE_946)) {
                    sendMessage(player, "You need a knife to prepare ${getItemName(ingredient.id).lowercase()}.")
                    return@onUseWith true
                }

                val ingredientCount = amountInInventory(player, ingredient.id)
                val secondaryCount = amountInInventory(player, secondary.id)
                val maxAmount = min(ingredientCount, secondaryCount)

                if (maxAmount <= 0) {
                    sendMessage(player, "You don't have the required ingredients.")
                    return@onUseWith true
                }

                fun process(amount: Int) {
                    if (!removeItem(player, ingredient.id) || !removeItem(player, secondary.id)) {
                        sendMessage(player, "You don't have the required ingredients.")
                        return
                    }

                    recipe.productId?.let { addItem(player, it, amount) }
                    recipe.returnsContnainer?.let { addItemOrDrop(player, it, amount) }
                    recipe.xpReward?.let { rewardXP(player, Skills.COOKING, it * amount) }
                    recipe.animation?.let { animate(player, it) }
                    recipe.message?.let { sendMessage(player, it) }
                }

                if (maxAmount == 1) {
                    process(1)
                    return@onUseWith true
                }

                sendSkillDialogue(player) {
                    recipe.productId?.let { withItems(it) }

                    create { _, amount ->
                        runTask(player, 2, amount) {
                            process(amount)
                        }
                    }

                    calculateMaxAmount { maxAmount }
                }

                return@onUseWith true
            }
        }

        onUseWith(IntType.ITEM, Items.PITTA_BREAD_1865, Items.KEBAB_MIX_1881) { player, _, _ ->
            if (!hasLevelDyn(player, Skills.COOKING, 58)) {
                sendDialogue(player, "You need a Cooking level of at least 58 to make that.")
                return@onUseWith true
            }

            if (!inInventory(player, Items.PITTA_BREAD_1865) || !inInventory(player, Items.KEBAB_MIX_1881)) {
                sendMessage(player, "You don't have the required ingredients.")
                return@onUseWith true
            }

            removeItem(player, Items.PITTA_BREAD_1865)
            removeItem(player, Items.KEBAB_MIX_1881)
            addItem(player, Items.BOWL_1923)

            if (RandomFunction.roll(50)) {
                addItem(player, Items.UGTHANKI_KEBAB_1885)
                sendMessage(player, "Your kebab smells a bit off, but you keep it.")
            } else {
                rewardXP(player, Skills.COOKING, 40.0)
                addItem(player, Items.UGTHANKI_KEBAB_1883)
                sendMessage(player, "You make a delicious ugthanki kebab.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.CHEESE_1985, Items.POTATO_1942) { player, _, _ ->
            sendMessage(player, "You must add butter to the baked potato before adding toppings.")
            return@onUseWith true
        }
    }

    /**
     * Represents the cooking recipes.
     */
    enum class CookingRecipe(val ingredientIds: IntArray, val secondaryId: Int, val productId: Int? = null, val requiredLevel: Int = 1, val animation: Int? = null, val requiresKnife: Boolean = false, val message: String? = null, val xpReward: Double? = null, val returnsContnainer: Int? = null) {
        UNCOOKED_CAKE(intArrayOf(Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944), Items.CAKE_TIN_1887, Items.UNCOOKED_CAKE_1889, 40, message = "You mix the milk, flour, and egg together to make a raw cake mix.", returnsContnainer = Items.BUCKET_1925),
        CHOCOLATE_CAKE(intArrayOf(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_DUST_1975), Items.CAKE_1891, Items.CHOCOLATE_CAKE_1897, 50, message = "You add chocolate to the cake.", xpReward = 30.0),
        CALQUAT_KEG(intArrayOf(Items.CALQUAT_FRUIT_5980), Items.KNIFE_946, Items.CALQUAT_KEG_5769, requiresKnife = true, animation = Animations.CARVE_CALQUAT_KEG_2290, message = "You carve the calquat fruit."),
        SLICED_BANANA(intArrayOf(Items.BANANA_1963), Items.KNIFE_946, Items.SLICED_BANANA_3162, requiresKnife = true, animation = Animations.HUMAN_FRUIT_CUTTING_1192, message = "You slice the banana."),
        CHOCOLATE_DUST(intArrayOf(Items.CHOCOLATE_BAR_1973), Items.KNIFE_946, Items.CHOCOLATE_DUST_1975, requiresKnife = true, animation = Animations.CUTTING_CHOCOLATE_BAR_1989, message = "You cut the chocolate."),
        CHOPPED_TUNA(intArrayOf(Items.TUNA_361), Items.BOWL_1923, Items.CHOPPED_TUNA_7086, requiresKnife = true, animation = -1, message = "You chop the tuna into the bowl."),
        CHOPPED_ONION(intArrayOf(Items.ONION_1957), Items.BOWL_1923, Items.CHOPPED_ONION_1871, requiresKnife = true, animation = -1, message = "You cut the onion into the bowl."),
        CHOPPED_GARLIC(intArrayOf(Items.GARLIC_1550), Items.BOWL_1923, Items.CHOPPED_GARLIC_7074, requiresKnife = true, animation = -1, message = "You chop the garlic into the bowl."),
        CHOPPED_TOMATO(intArrayOf(Items.TOMATO_1982), Items.BOWL_1923, Items.CHOPPED_TOMATO_1869, requiresKnife = true, animation = -1, message = "You chop the tomato into the bowl."),
        CHOPPED_UGTHANKI(intArrayOf(Items.UGTHANKI_MEAT_1861), Items.BOWL_1923, Items.CHOPPED_UGTHANKI_1873, requiresKnife = true, animation = -1, message = "You chop the meat into the bowl."),
        SLICED_MUSHROOMS(intArrayOf(Items.MUSHROOM_6004), Items.BOWL_1923, Items.SLICED_MUSHROOMS_7080, requiresKnife = true, animation = -1, message = "You slice the mushrooms."),
        MINCED_MEAT(intArrayOf(Items.COOKED_MEAT_2142), Items.BOWL_1923, Items.MINCED_MEAT_7070, requiresKnife = true, animation = -1, message = "You chop the meat into the bowl."),
        SPICY_SAUCE(intArrayOf(Items.GNOME_SPICE_2169), Items.CHOPPED_GARLIC_7074, Items.SPICY_SAUCE_7072, 9, message = "You mix the ingredients to make spicy sauce.", xpReward = 25.0),
        UNCOOKED_EGG(intArrayOf(Items.EGG_1944), Items.BOWL_1923, Items.UNCOOKED_EGG_7076, message = "You prepare an uncooked egg."),
        ONION_AND_TOMATO_FROM_ONION_AND_TOMATO(intArrayOf(Items.CHOPPED_ONION_1871), Items.TOMATO_1982, Items.ONION_AND_TOMATO_1875, requiresKnife = true, message = "You cut the onion into the bowl."),
        ONION_AND_TOMATO_FROM_TOMATO_AND_ONION(intArrayOf(Items.CHOPPED_TOMATO_1869), Items.ONION_1957, Items.ONION_AND_TOMATO_1875, requiresKnife = true, message = "You cut the tomato into the bowl."),
        KEBAB_MIX(intArrayOf(Items.ONION_AND_TOMATO_1875), Items.UGTHANKI_MEAT_1861, Items.KEBAB_MIX_1881, requiresKnife = true, message = "You mix the ugthanki meat with the onion and tomato."),
        KEBAB_MIX_2(intArrayOf(Items.UGTHANKI_AND_ONION_1877), Items.TOMATO_1982, Items.KEBAB_MIX_1881, requiresKnife = true, message = "You mix the onion and tomato with the tomato."),
        UGTHANKI_AND_ONION(intArrayOf(Items.CHOPPED_ONION_1871), Items.UGTHANKI_MEAT_1861, Items.UGTHANKI_AND_ONION_1877, requiresKnife = true, message = "You mix the chopped onion with the ugthanki meat."),
        UGTHANKI_AND_TOMATO(intArrayOf(Items.CHOPPED_TOMATO_1869), Items.UGTHANKI_MEAT_1861, Items.UGTHANKI_AND_TOMATO_1879, requiresKnife = true, message = "You mix the chopped tomato with the ugthanki meat."),
        SUPER_KEBAB(intArrayOf(Items.KEBAB_1971, Items.UGTHANKI_KEBAB_1883, Items.UGTHANKI_KEBAB_1885), Items.RED_HOT_SAUCE_4610, Items.SUPER_KEBAB_4608, message = "You add red hot sauce to make a super kebab."),
        SPICY_SAUCE_PLUS_MINCED_MEAT(intArrayOf(Items.SPICY_SAUCE_7072), Items.MINCED_MEAT_7070, Items.CHILLI_CON_CARNE_7062, 9, message = "You mix the ingredients to make the topping.", xpReward = 25.0, returnsContnainer = Items.BOWL_1923),
        SPICY_SAUCE_PLUS_COOKED_MEAT(intArrayOf(Items.SPICY_SAUCE_7072), Items.COOKED_MEAT_2142, Items.CHILLI_CON_CARNE_7062, 9, requiresKnife = true, message = "You put the cut up meat into the bowl.", xpReward = 25.0, returnsContnainer = Items.BOWL_1923),
        CHOPPED_TUNA_PLUS_COOKED_SWEETCORN(intArrayOf(Items.CHOPPED_TUNA_7086), Items.COOKED_SWEETCORN_5988, Items.TUNA_AND_CORN_7068, 67, message = "You mix the ingredients to make the topping.", xpReward = 204.0),
        SCRAMBLED_EGG_PLUS_TOMATO(intArrayOf(Items.SCRAMBLED_EGG_7078), Items.TOMATO_1982, Items.EGG_AND_TOMATO_7064, 23, message = "You mix the scrambled egg with the tomato.", xpReward = 50.0),
        RAW_OOMLIE_PLUS_PALM_LEAF(intArrayOf(Items.RAW_OOMLIE_2337), Items.PALM_LEAF_2339, Items.WRAPPED_OOMLIE_2341, 50, message = "You wrap the raw oomlie in the palm leaf."),
        FRIED_MUSHROOMS_PLUS_FRIED_ONIONS(intArrayOf(Items.FRIED_MUSHROOMS_7082), Items.FRIED_ONIONS_7084, Items.MUSHROOM_AND_ONION_7066, 57, message = "You mix the fried onions and mushrooms.", xpReward = 120.0, returnsContnainer = Items.BOWL_1923),
        INCOMPLETE_PIZZA(intArrayOf(Items.PIZZA_BASE_2283), Items.TOMATO_1982, Items.INCOMPLETE_PIZZA_2285, 35, message = "You add the tomato to the pizza base."),
        UNCOOKED_PIZZA(intArrayOf(Items.CHEESE_1985), Items.INCOMPLETE_PIZZA_2285, Items.UNCOOKED_PIZZA_2287, 35, message = "You add the cheese to the incomplete pizza."),
        MEAT_PIZZA(intArrayOf(Items.COOKED_MEAT_2142, Items.COOKED_CHICKEN_2140), Items.PLAIN_PIZZA_2289, Items.MEAT_PIZZA_2293, 45, message = "You add the meat to the pizza.", xpReward = 26.0),
        ANCHOVY_PIZZA(intArrayOf(Items.ANCHOVIES_319), Items.PLAIN_PIZZA_2289, Items.ANCHOVY_PIZZA_2297, 55, message = "You add the anchovies to the pizza.", xpReward = 39.0),
        PINEAPPLE_PIZZA(intArrayOf(Items.PINEAPPLE_CHUNKS_2116, Items.PINEAPPLE_RING_2118), Items.PLAIN_PIZZA_2289, Items.PINEAPPLE_PIZZA_2301, 65, message = "You add the pineapple to the pizza.", xpReward = 52.0),
        PIE_SHELL(intArrayOf(Items.PASTRY_DOUGH_1953), Items.PIE_DISH_2313, Items.PIE_SHELL_2315, message = "You put the pastry dough into the pie dish to make a pie shell."),
        UNCOOKED_BERRY_PIE(intArrayOf(Items.REDBERRIES_1951), Items.PIE_SHELL_2315, Items.UNCOOKED_BERRY_PIE_2321, 10, message = "You fill the pie with redberries."),
        UNCOOKED_MEAT_PIE(intArrayOf(Items.COOKED_MEAT_2142, Items.COOKED_CHICKEN_2140), Items.PIE_SHELL_2315, Items.UNCOOKED_MEAT_PIE_2319, 20, message = "You fill the pie with cooked meat."),
        PART_MUD_PIE(intArrayOf(Items.COMPOST_6032), Items.PIE_SHELL_2315, Items.PART_MUD_PIE_7164, 29, message = "You fill the pie with compost."),
        UNCOOKED_APPLE_PIE(intArrayOf(Items.COOKING_APPLE_1955), Items.PIE_SHELL_2315, Items.UNCOOKED_APPLE_PIE_2317, 30, message = "You fill the pie with apple."),
        PART_GARDEN_PIE(intArrayOf(Items.TOMATO_1982), Items.PIE_SHELL_2315, Items.PART_GARDEN_PIE_7172, 34, message = "You fill the pie with tomato."),
        PART_FISH_PIE(intArrayOf(Items.TROUT_333), Items.PIE_SHELL_2315, Items.PART_FISH_PIE_7182, 47, message = "You fill the pie with trout."),
        PART_ADMIRAL_PIE(intArrayOf(Items.SALMON_329), Items.PIE_SHELL_2315, Items.PART_ADMIRAL_PIE_7192, 70, message = "You fill the pie with salmon."),
        PART_WILD_PIE(intArrayOf(Items.RAW_BEAR_MEAT_2136), Items.PIE_SHELL_2315, Items.PART_WILD_PIE_7202, 85, message = "You fill the pie with bear meat."),
        PART_SUMMER_PIE(intArrayOf(Items.STRAWBERRY_5504), Items.PIE_SHELL_2315, Items.PART_SUMMER_PIE_7212, 95, message = "You fill the pie with strawberry."),
        PART_MUD_PIE_SECOND(intArrayOf(Items.BUCKET_OF_WATER_1929), Items.PART_MUD_PIE_7164, Items.PART_MUD_PIE_7166, 29, message = "You add water to the mud pie."),
        PART_GARDEN_PIE_SECOND(intArrayOf(Items.ONION_1957), Items.PART_GARDEN_PIE_7172, Items.PART_GARDEN_PIE_7174, 34, message = "You add onion to the garden pie."),
        PART_FISH_PIE_SECOND(intArrayOf(Items.COD_339), Items.PART_FISH_PIE_7182, Items.PART_FISH_PIE_7184, 47, message = "You add cod to the fish pie."),
        PART_ADMIRAL_PIE_SECOND(intArrayOf(Items.TUNA_361), Items.PART_ADMIRAL_PIE_7192, Items.PART_ADMIRAL_PIE_7194, 70, message = "You add tuna to the admiral pie."),
        PART_WILD_PIE_SECOND(intArrayOf(Items.RAW_CHOMPY_2876), Items.PART_WILD_PIE_7202, Items.PART_WILD_PIE_7204, 85, message = "You add chompy to the wild pie."),
        PART_SUMMER_PIE_SECOND(intArrayOf(Items.WATERMELON_5982), Items.PART_SUMMER_PIE_7212, Items.PART_SUMMER_PIE_7214, 95, message = "You add watermelon to the summer pie."),
        RAW_MUD_PIE(intArrayOf(Items.CLAY_434), Items.PART_MUD_PIE_7166, Items.RAW_MUD_PIE_7168, 29, message = "You prepare a raw mud pie."),
        RAW_GARDEN_PIE_1(intArrayOf(Items.CABBAGE_1965), Items.PART_GARDEN_PIE_7174, Items.RAW_GARDEN_PIE_7176, 34, message = "You prepare a raw garden pie."),
        RAW_GARDEN_PIE_2(intArrayOf(Items.CABBAGE_1967), Items.PART_GARDEN_PIE_7174, Items.RAW_GARDEN_PIE_7176, 34, message = "You prepare a raw garden pie."),
        RAW_WILD_PIE(intArrayOf(Items.RAW_RABBIT_3226), Items.PART_WILD_PIE_7204, Items.RAW_WILD_PIE_7206, 85, message = "You prepare a raw wild pie."),
        RAW_SUMMER_PIE(intArrayOf(Items.COOKING_APPLE_1955), Items.PART_SUMMER_PIE_7214, Items.RAW_SUMMER_PIE_7216, 95, message = "You prepare a raw summer pie."),
        RAW_ADMIRAL_PIE(intArrayOf(Items.PART_ADMIRAL_PIE_7194), Items.POTATO_1942, Items.RAW_ADMIRAL_PIE_7196, 70, message = "You prepare an admiral pie."),
        RAW_FISH_PIE(intArrayOf(Items.PART_FISH_PIE_7184), Items.POTATO_1942, Items.RAW_FISH_PIE_7186, 47, message = "You prepare a fish pie."),
        POTATO_WITH_BUTTER(intArrayOf(Items.PAT_OF_BUTTER_6697), Items.BAKED_POTATO_6701, Items.POTATO_WITH_BUTTER_6703, 39, message = "You add the butter to the potato.", xpReward = 40.5),
        CHILLI_POTATO(intArrayOf(Items.CHILLI_CON_CARNE_7062), Items.POTATO_WITH_BUTTER_6703, Items.CHILLI_POTATO_7054, 41, message = "You add the chilli con carne to the potato.", xpReward = 10.0),
        EGG_POTATO(intArrayOf(Items.EGG_AND_TOMATO_7064), Items.POTATO_WITH_BUTTER_6703, Items.EGG_POTATO_7056, 51, message = "You add the egg and tomato to the potato.", xpReward = 10.0),
        MUSHROOM_POTATO(intArrayOf(Items.MUSHROOM_AND_ONION_7066), Items.POTATO_WITH_BUTTER_6703, Items.MUSHROOM_POTATO_7058, 64, message = "You add the mushrooms and onions to the potato.", xpReward = 10.0),
        POTATO_WITH_CHEESE(intArrayOf(Items.CHEESE_1985), Items.POTATO_WITH_BUTTER_6703, Items.POTATO_WITH_CHEESE_6705, 47, message = "You add the cheese to the potato.", xpReward = 10.0),
        TUNA_POTATO(intArrayOf(Items.TUNA_AND_CORN_7068), Items.POTATO_WITH_BUTTER_6703, Items.TUNA_POTATO_7060, 68, message = "You add the tuna and corn to the potato.", xpReward = 10.0),
        SKEWERED_BIRD_MEAT(intArrayOf(Items.RAW_BIRD_MEAT_9978, Items.IRON_SPIT_7225), Items.IRON_SPIT_7225, Items.SKEWERED_BIRD_MEAT_9984, 11),
        SKEWERED_RABBIT(intArrayOf(Items.RAW_RABBIT_3226, Items.IRON_SPIT_7225), Items.IRON_SPIT_7225, Items.SKEWERED_RABBIT_7224, 16),
        SKEWERED_BEAST(intArrayOf(Items.RAW_BEAST_MEAT_9986, Items.IRON_SPIT_7225), Items.IRON_SPIT_7225, Items.SKEWERED_BEAST_9992, 21),
        SKEWERED_CHOMPY(intArrayOf(Items.RAW_CHOMPY_2876, Items.IRON_SPIT_7225), Items.IRON_SPIT_7225, Items.SKEWERED_CHOMPY_7230, 30),
        SPIDER_ON_STICK(intArrayOf(Items.SPIDER_CARCASS_6291, Items.SKEWER_STICK_6305), Items.SKEWER_STICK_6305, Items.SPIDER_ON_STICK_6293),
        SPIDER_ON_SHAFT(intArrayOf(Items.SPIDER_CARCASS_6291, Items.ARROW_SHAFT_52), Items.ARROW_SHAFT_52, Items.SPIDER_ON_SHAFT_6295),
        PLACE_NETTLES_IN_WATER(intArrayOf(Items.NETTLES_4241, Items.BOWL_OF_WATER_1921), Items.BOWL_OF_WATER_1921, Items.NETTLE_WATER_4237),
        ADD_MILK_TO_BOWL_OF_NETTLE_TEA(intArrayOf(Items.BUCKET_OF_MILK_1927, Items.NETTLE_TEA_4239), Items.NETTLE_TEA_4239, Items.NETTLE_TEA_4240, 1, returnsContnainer = Items.BUCKET_1925),
        ADD_MILK_TO_PORCELAIN_CUP_OF_NETTLE_TEA(intArrayOf(Items.BUCKET_OF_MILK_1927, Items.PORCELAIN_CUP_4244), Items.PORCELAIN_CUP_4244, Items.CUP_OF_TEA_4246, 1, returnsContnainer = Items.BUCKET_1925),
        POUR_NETTLE_TEA_INTO_EMPTY_CUP(intArrayOf(Items.NETTLE_TEA_4239, Items.EMPTY_CUP_1980), Items.EMPTY_CUP_1980, Items.CUP_OF_TEA_4242),
        POUR_MILKY_NETTLE_TEA_INTO_EMPTY_CUP(intArrayOf(Items.NETTLE_TEA_4240, Items.EMPTY_CUP_1980), Items.EMPTY_CUP_1980, Items.CUP_OF_TEA_4243),
        POUR_NETTLE_TEA_INTO_EMPTY_PORCELAIN_CUP(intArrayOf(Items.NETTLE_TEA_4239, Items.PORCELAIN_CUP_4244), Items.PORCELAIN_CUP_4244, Items.CUP_OF_TEA_4245),
        POUR_MILKY_NETTLE_TEA_INTO_EMPTY_PORCELAIN_CUP(intArrayOf(Items.NETTLE_TEA_4240, Items.PORCELAIN_CUP_4244), Items.PORCELAIN_CUP_4244, Items.CUP_OF_TEA_4246);
    }
}
