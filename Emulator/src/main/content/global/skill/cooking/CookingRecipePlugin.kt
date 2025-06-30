package content.global.skill.cooking

import core.api.*
import core.api.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import kotlin.math.min

class CookingRecipePlugin : InteractionListener {

    companion object {

        /**
         * Handles cooking recipes processing logic.
         */
        @JvmStatic
        private fun handleCookingRecipe(player: Player, used: Item, with: Item, recipe: CookingRecipe): Boolean {
            if (!hasLevelDyn(player, Skills.COOKING, recipe.requiredLevel)) {
                sendDialogue(player, "You need a Cooking level of at least ${recipe.requiredLevel} to make that.")
                return true
            }

            val (ingredient, secondary) = if (used.id in recipe.ingredientIds) used to with else with to used

            if (recipe.requiresKnife && !inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the ${getItemName(ingredient.id).lowercase()}.")
                return true
            }

            val amountIngredient = amountInInventory(player, ingredient.id)
            val amountSecondary = amountInInventory(player, secondary.id)
            val maxAmount = min(amountIngredient, amountSecondary)

            if (maxAmount <= 0) {
                sendMessage(player, "You don't have the required ingredients.")
                return true
            }

            val process: () -> Boolean = {
                recipe.animation?.let { animate(player, it) }
                recipe.productId?.let { addItem(player, it, 1) }
                recipe.onProcess?.invoke(player)
                true
            }

            if (maxAmount == 1) {
                if (removeItem(player, ingredient.asItem()) && removeItem(player, secondary.asItem())) {
                    return process()
                }
                sendMessage(player, "You don't have the required ingredients.")
                return true
            }

            sendSkillDialogue(player) {
                recipe.productId?.let { withItems(it) }
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (!removeItem(player, ingredient.asItem()) || !removeItem(player, secondary.asItem())) {
                            sendMessage(player, "You don't have the required ingredients.")
                            return@runTask
                        }
                        process()
                    }
                }
                calculateMaxAmount { maxAmount }
            }
            return true
        }
    }

    override fun defineListeners() {

        /*
         * Handles a special-case recipe where the player attempts to
         *  use cheese on a "raw potato".
         */

        onUseWith(IntType.ITEM, Items.CHEESE_1985, Items.POTATO_1942) { player, _, _ ->
            sendMessage(player, "You must add butter to the baked potato before adding toppings.")
            return@onUseWith true
        }

        /*
         * Handles cooking recipes usage.
         */

        for (recipe in CookingRecipe.values()) {
            onUseWith(IntType.ITEM, recipe.ingredientIds, recipe.secondaryId) { player, used, with ->
                handleCookingRecipe(player, used.asItem(), with.asItem(), recipe)
                return@onUseWith true
            }
        }
    }

    /**
     * Represents the cooking recipes.
     */
    enum class CookingRecipe(val ingredientIds: IntArray, val secondaryId: Int, val productId: Int? = null, val requiredLevel: Int = 1, val animation: Int? = null, val requiresKnife: Boolean = false, val onProcess: ((Player) -> Unit)? = null) {
        // Cake recipes.
        UNCOOKED_CAKE(
            ingredientIds = intArrayOf(Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944),
            secondaryId = Items.CAKE_TIN_1887,
            productId = Items.UNCOOKED_CAKE_1889,
            requiredLevel = 40,
            onProcess = { player ->
                addItemOrDrop(player, Items.BUCKET_1925, 1)
                addItemOrDrop(player, Items.EMPTY_POT_1931, 1)
                sendMessage(player, "You mix the milk, flour, and egg together to make a raw cake mix.")
            }
        ),
        CHOCOLATE_CAKE(
            ingredientIds = intArrayOf(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_DUST_1975),
            secondaryId = Items.CAKE_1891,
            productId = Items.CHOCOLATE_CAKE_1897,
            requiredLevel = 50,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 30.0)
                sendMessage(player, "You add chocolate to the cake.")
            }
        ),
        // Chopping recipes.
        CALQUAT_KEG(
            ingredientIds = intArrayOf(Items.CALQUAT_FRUIT_5980),
            secondaryId = Items.KNIFE_946,
            productId = Items.CALQUAT_KEG_5769,
            requiresKnife = true,
            animation = Animations.CARVE_CALQUAT_KEG_2290,
            onProcess = { player -> sendMessage(player, "You carve the calquat fruit.") }
        ),
        SLICED_BANANA(
            ingredientIds = intArrayOf(Items.BANANA_1963),
            secondaryId = Items.KNIFE_946,
            productId = Items.SLICED_BANANA_3162,
            requiresKnife = true,
            animation = Animations.HUMAN_FRUIT_CUTTING_1192,
            onProcess = { player -> sendMessage(player, "You slice the banana.") }
        ),
        CHOCOLATE_DUST(
            ingredientIds = intArrayOf(Items.CHOCOLATE_BAR_1973),
            secondaryId = Items.KNIFE_946,
            productId = Items.CHOCOLATE_DUST_1975,
            requiresKnife = true,
            animation = Animations.CUTTING_CHOCOLATE_BAR_1989,
            onProcess = { player -> sendMessage(player, "You cut the chocolate.") }
        ),
        CHOPPED_TUNA(
            ingredientIds = intArrayOf(Items.TUNA_361),
            secondaryId = Items.BOWL_1923,
            productId = Items.CHOPPED_TUNA_7086,
            requiresKnife = true,
            animation = -1,
            onProcess = { player -> sendMessage(player, "You chop the tuna into the bowl.") }
        ),
        CHOPPED_ONION(
            ingredientIds = intArrayOf(Items.ONION_1957),
            secondaryId = Items.BOWL_1923,
            productId = Items.CHOPPED_ONION_1871,
            requiresKnife = true,
            animation = -1,
            onProcess = { player -> sendMessage(player, "You cut the onion into the bowl.") }
        ),
        CHOPPED_GARLIC(
            ingredientIds = intArrayOf(Items.GARLIC_1550),
            secondaryId = Items.BOWL_1923,
            productId = Items.CHOPPED_GARLIC_7074,
            requiresKnife = true,
            animation = -1,
            onProcess = { player -> sendMessage(player, "You chop the garlic into the bowl.") }
        ),
        CHOPPED_TOMATO(
            ingredientIds = intArrayOf(Items.TOMATO_1982),
            secondaryId = Items.BOWL_1923,
            productId = Items.CHOPPED_TOMATO_1869,
            requiresKnife = true,
            animation = -1,
            onProcess = { player -> sendMessage(player, "You chop the tomato into the bowl.") }
        ),
        CHOPPED_UGTHANKI(
            ingredientIds = intArrayOf(Items.UGTHANKI_MEAT_1861),
            secondaryId = Items.BOWL_1923,
            productId = Items.CHOPPED_UGTHANKI_1873,
            requiresKnife = true,
            animation = -1,
            onProcess = { player -> sendMessage(player, "You chop the meat into the bowl.") }
        ),
        SLICED_MUSHROOMS(
            ingredientIds = intArrayOf(Items.MUSHROOM_6004),
            secondaryId = Items.BOWL_1923,
            productId = Items.SLICED_MUSHROOMS_7080,
            requiresKnife = true,
            animation = -1,
            onProcess = { player -> sendMessage(player, "You slice the mushrooms.") }
        ),
        MINCED_MEAT(
            ingredientIds = intArrayOf(Items.COOKED_MEAT_2142),
            secondaryId = Items.BOWL_1923,
            productId = Items.MINCED_MEAT_7070,
            requiresKnife = true,
            animation = -1,
            onProcess = { player -> sendMessage(player, "You chop the meat into the bowl.") }
        ),
        SPICY_SAUCE(
            ingredientIds = intArrayOf(Items.GNOME_SPICE_2169),
            secondaryId = Items.CHOPPED_GARLIC_7074,
            productId = Items.SPICY_SAUCE_7072,
            requiredLevel = 9,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 25.0)
                sendMessage(player, "You mix the ingredients to make spicy sauce.")
            }
        ),
        UNCOOKED_EGG(
            ingredientIds = intArrayOf(Items.EGG_1944),
            secondaryId = Items.BOWL_1923,
            productId = Items.UNCOOKED_EGG_7076,
            onProcess = { player -> sendMessage(player, "You prepare an uncooked egg.") }
        ),
        // Kebab recipes.
        ONION_AND_TOMATO_FROM_ONION_AND_TOMATO(
            ingredientIds = intArrayOf(Items.CHOPPED_ONION_1871),
            secondaryId = Items.TOMATO_1982,
            productId = Items.ONION_AND_TOMATO_1875,
            requiresKnife = true,
            onProcess = { player -> sendMessage(player, "You cut the onion into the bowl.") }
        ),
        ONION_AND_TOMATO_FROM_TOMATO_AND_ONION(
            ingredientIds = intArrayOf(Items.CHOPPED_TOMATO_1869),
            secondaryId = Items.ONION_1957,
            productId = Items.ONION_AND_TOMATO_1875,
            requiresKnife = true,
            onProcess = { player -> sendMessage(player, "You cut the tomato into the bowl.") }
        ),
        KEBAB_MIX(
            ingredientIds = intArrayOf(Items.ONION_AND_TOMATO_1875),
            secondaryId = Items.UGTHANKI_MEAT_1861,
            productId = Items.KEBAB_MIX_1881,
            requiresKnife = true,
            onProcess = { player -> sendMessage(player, "You mix the ugthanki meat with the onion and tomato.") }
        ),
        KEBAB_MIX_2(
            ingredientIds = intArrayOf(Items.UGTHANKI_AND_ONION_1877),
            secondaryId = Items.TOMATO_1982,
            productId = Items.KEBAB_MIX_1881,
            requiresKnife = true,
            onProcess = { player -> sendMessage(player, "You mix the onion and tomato with the tomato.") }
        ),
        UGTHANKI_AND_ONION(
            ingredientIds = intArrayOf(Items.CHOPPED_ONION_1871),
            secondaryId = Items.UGTHANKI_MEAT_1861,
            productId = Items.UGTHANKI_AND_ONION_1877,
            requiresKnife = true,
            onProcess = { player -> sendMessage(player, "You mix the chopped onion with the ugthanki meat.") }
        ),
        UGTHANKI_AND_TOMATO(
            ingredientIds = intArrayOf(Items.CHOPPED_TOMATO_1869),
            secondaryId = Items.UGTHANKI_MEAT_1861,
            productId = Items.UGTHANKI_AND_TOMATO_1879,
            requiresKnife = true,
            onProcess = { player -> sendMessage(player, "You mix the chopped tomato with the ugthanki meat.") }
        ),
        SUPER_KEBAB(
            ingredientIds = intArrayOf(Items.KEBAB_1971, Items.UGTHANKI_KEBAB_1883, Items.UGTHANKI_KEBAB_1885),
            secondaryId = Items.RED_HOT_SAUCE_4610,
            productId = Items.SUPER_KEBAB_4608,
            onProcess = { player ->
                removeItem(player, Items.RED_HOT_SAUCE_4610, Container.INVENTORY)
                sendMessage(player, "You add red hot sauce to make a super kebab.")
                addItem(player, Items.SUPER_KEBAB_4608, 1, Container.INVENTORY)
            }
        ),
        UGTHANKI_KEBAB(
            ingredientIds = intArrayOf(Items.PITTA_BREAD_1865),
            secondaryId = Items.KEBAB_MIX_1881,
            requiredLevel = 58,
            onProcess = { player ->
                removeItem(player, Items.PITTA_BREAD_1865, Container.INVENTORY)
                removeItem(player, Items.KEBAB_MIX_1881, Container.INVENTORY)
                addItem(player, Items.BOWL_1923, 1, Container.INVENTORY)

                if (RandomFunction.roll(50)) {
                    addItem(player, Items.UGTHANKI_KEBAB_1885)
                    sendMessage(player, "Your kebab smells a bit off, but you keep it.")
                } else {
                    rewardXP(player, Skills.COOKING, 40.0)
                    addItem(player, Items.UGTHANKI_KEBAB_1883)
                    sendMessage(player, "You make a delicious ugthanki kebab.")
                }
            }
        ),
        // Topping recipes.
        SPICY_SAUCE_PLUS_MINCED_MEAT(
            ingredientIds = intArrayOf(Items.SPICY_SAUCE_7072),
            secondaryId = Items.MINCED_MEAT_7070,
            productId = Items.CHILLI_CON_CARNE_7062,
            requiredLevel = 9,
            onProcess = { player ->
                addItemOrDrop(player, Items.BOWL_1923, 1)
                rewardXP(player, Skills.COOKING, 25.0)
                sendMessage(player, "You mix the ingredients to make the topping.")
            }
        ),
        SPICY_SAUCE_PLUS_COOKED_MEAT(
            ingredientIds = intArrayOf(Items.SPICY_SAUCE_7072),
            secondaryId = Items.COOKED_MEAT_2142,
            productId = Items.CHILLI_CON_CARNE_7062,
            requiredLevel = 9,
            requiresKnife = true,
            onProcess = { player ->
                addItemOrDrop(player, Items.BOWL_1923, 1)
                rewardXP(player, Skills.COOKING, 25.0)
                sendMessage(player, "You put the cut up meat into the bowl.")
            }
        ),
        CHOPPED_TUNA_PLUS_COOKED_SWEETCORN(
            ingredientIds = intArrayOf(Items.CHOPPED_TUNA_7086),
            secondaryId = Items.COOKED_SWEETCORN_5988,
            productId = Items.TUNA_AND_CORN_7068,
            requiredLevel = 67,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 204.0)
                sendMessage(player, "You mix the ingredients to make the topping.")
            }
        ),
        SCRAMBLED_EGG_PLUS_TOMATO(
            ingredientIds = intArrayOf(Items.SCRAMBLED_EGG_7078),
            secondaryId = Items.TOMATO_1982,
            productId = Items.EGG_AND_TOMATO_7064,
            requiredLevel = 23,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 50.0)
                sendMessage(player, "You mix the scrambled egg with the tomato.")
            }
        ),
        RAW_OOMLIE_PLUS_PALM_LEAF(
            ingredientIds = intArrayOf(Items.RAW_OOMLIE_2337),
            secondaryId = Items.PALM_LEAF_2339,
            productId = Items.WRAPPED_OOMLIE_2341,
            requiredLevel = 50,
            onProcess = { player ->
                sendMessage(player, "You wrap the raw oomlie in the palm leaf.")
            }
        ),
        FRIED_MUSHROOMS_PLUS_FRIED_ONIONS(
            ingredientIds = intArrayOf(Items.FRIED_MUSHROOMS_7082),
            secondaryId = Items.FRIED_ONIONS_7084,
            productId = Items.MUSHROOM_AND_ONION_7066,
            requiredLevel = 57,
            onProcess = { player ->
                addItemOrDrop(player, Items.BOWL_1923, 1)
                rewardXP(player, Skills.COOKING, 120.0)
                sendMessage(player, "You mix the fried onions and mushrooms.")
            }
        ),
        // Pizza recipes.
        INCOMPLETE_PIZZA(
            ingredientIds = intArrayOf(Items.PIZZA_BASE_2283),
            secondaryId = Items.TOMATO_1982,
            productId = Items.INCOMPLETE_PIZZA_2285,
            requiredLevel = 35,
            onProcess = { player ->
                sendMessage(player, "You add the tomato to the pizza base.")
            }
        ),
        UNCOOKED_PIZZA(
            ingredientIds = intArrayOf(Items.CHEESE_1985),
            secondaryId = Items.INCOMPLETE_PIZZA_2285,
            productId = Items.UNCOOKED_PIZZA_2287,
            requiredLevel = 35,
            onProcess = { player ->
                sendMessage(player, "You add the cheese to the incomplete pizza.")
            }
        ),
        MEAT_PIZZA(
            ingredientIds = intArrayOf(Items.COOKED_MEAT_2142, Items.COOKED_CHICKEN_2140),
            secondaryId = Items.PLAIN_PIZZA_2289,
            productId = Items.MEAT_PIZZA_2293,
            requiredLevel = 45,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 26.0)
                sendMessage(player, "You add the meat to the pizza.")
            }
        ),
        ANCHOVY_PIZZA(
            ingredientIds = intArrayOf(Items.ANCHOVIES_319),
            secondaryId = Items.PLAIN_PIZZA_2289,
            productId = Items.ANCHOVY_PIZZA_2297,
            requiredLevel = 55,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 39.0)
                sendMessage(player, "You add the anchovies to the pizza.")
            }
        ),
        PINEAPPLE_PIZZA(
            ingredientIds = intArrayOf(Items.PINEAPPLE_CHUNKS_2116, Items.PINEAPPLE_RING_2118),
            secondaryId = Items.PLAIN_PIZZA_2289,
            productId = Items.PINEAPPLE_PIZZA_2301,
            requiredLevel = 65,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 52.0)
                sendMessage(player, "You add the pineapple to the pizza.")
            }
        ),
        // Pies.
        PIE_SHELL(
            ingredientIds = intArrayOf(Items.PASTRY_DOUGH_1953),
            secondaryId = Items.PIE_DISH_2313,
            productId = Items.PIE_SHELL_2315,
            onProcess = { player ->
                sendMessage(player, "You put the pastry dough into the pie dish to make a pie shell.")
            }
        ),
        UNCOOKED_BERRY_PIE(
            ingredientIds = intArrayOf(Items.REDBERRIES_1951),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.UNCOOKED_BERRY_PIE_2321,
            requiredLevel = 10,
            onProcess = { player -> sendMessage(player, "You fill the pie with redberries.") }
        ),
        UNCOOKED_MEAT_PIE(
            ingredientIds = intArrayOf(Items.COOKED_MEAT_2142, Items.COOKED_CHICKEN_2140),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.UNCOOKED_MEAT_PIE_2319,
            requiredLevel = 20,
            onProcess = { player -> sendMessage(player, "You fill the pie with cooked meat.") }
        ),
        PART_MUD_PIE(
            ingredientIds = intArrayOf(Items.COMPOST_6032),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.PART_MUD_PIE_7164,
            requiredLevel = 29,
            onProcess = { player -> sendMessage(player, "You fill the pie with compost.") }
        ),
        UNCOOKED_APPLE_PIE(
            ingredientIds = intArrayOf(Items.COOKING_APPLE_1955),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.UNCOOKED_APPLE_PIE_2317,
            requiredLevel = 30,
            onProcess = { player -> sendMessage(player, "You fill the pie with apple.") }
        ),
        PART_GARDEN_PIE(
            ingredientIds = intArrayOf(Items.TOMATO_1982),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.PART_GARDEN_PIE_7172,
            requiredLevel = 34,
            onProcess = { player -> sendMessage(player, "You fill the pie with tomato.") }
        ),
        PART_FISH_PIE(
            ingredientIds = intArrayOf(Items.TROUT_333),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.PART_FISH_PIE_7182,
            requiredLevel = 47,
            onProcess = { player -> sendMessage(player, "You fill the pie with trout.") }
        ),
        PART_ADMIRAL_PIE(
            ingredientIds = intArrayOf(Items.SALMON_329),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.PART_ADMIRAL_PIE_7192,
            requiredLevel = 70,
            onProcess = { player -> sendMessage(player, "You fill the pie with salmon.") }
        ),
        PART_WILD_PIE(
            ingredientIds = intArrayOf(Items.RAW_BEAR_MEAT_2136),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.PART_WILD_PIE_7202,
            requiredLevel = 85,
            onProcess = { player -> sendMessage(player, "You fill the pie with bear meat.") }
        ),
        PART_SUMMER_PIE(
            ingredientIds = intArrayOf(Items.STRAWBERRY_5504),
            secondaryId = Items.PIE_SHELL_2315,
            productId = Items.PART_SUMMER_PIE_7212,
            requiredLevel = 95,
            onProcess = { player -> sendMessage(player, "You fill the pie with strawberry.") }
        ),
        PART_MUD_PIE_SECOND(
            ingredientIds = intArrayOf(Items.BUCKET_OF_WATER_1929),
            secondaryId = Items.PART_MUD_PIE_7164,
            productId = Items.PART_MUD_PIE_7166,
            requiredLevel = 29,
            onProcess = { player -> sendMessage(player, "You add water to the mud pie.") }
        ),
        PART_GARDEN_PIE_SECOND(
            ingredientIds = intArrayOf(Items.ONION_1957),
            secondaryId = Items.PART_GARDEN_PIE_7172,
            productId = Items.PART_GARDEN_PIE_7174,
            requiredLevel = 34,
            onProcess = { player -> sendMessage(player, "You add onion to the garden pie.") }
        ),
        PART_FISH_PIE_SECOND(
            ingredientIds = intArrayOf(Items.COD_339),
            secondaryId = Items.PART_FISH_PIE_7182,
            productId = Items.PART_FISH_PIE_7184,
            requiredLevel = 47,
            onProcess = { player -> sendMessage(player, "You add cod to the fish pie.") }
        ),
        PART_ADMIRAL_PIE_SECOND(
            ingredientIds = intArrayOf(Items.TUNA_361),
            secondaryId = Items.PART_ADMIRAL_PIE_7192,
            productId = Items.PART_ADMIRAL_PIE_7194,
            requiredLevel = 70,
            onProcess = { player -> sendMessage(player, "You add tuna to the admiral pie.") }
        ),
        PART_WILD_PIE_SECOND(
            ingredientIds = intArrayOf(Items.RAW_CHOMPY_2876),
            secondaryId = Items.PART_WILD_PIE_7202,
            productId = Items.PART_WILD_PIE_7204,
            requiredLevel = 85,
            onProcess = { player -> sendMessage(player, "You add chompy to the wild pie.") }
        ),
        PART_SUMMER_PIE_SECOND(
            ingredientIds = intArrayOf(Items.WATERMELON_5982),
            secondaryId = Items.PART_SUMMER_PIE_7212,
            productId = Items.PART_SUMMER_PIE_7214,
            requiredLevel = 95,
            onProcess = { player -> sendMessage(player, "You add watermelon to the summer pie.") }
        ),
        RAW_MUD_PIE(
            ingredientIds = intArrayOf(Items.CLAY_434),
            secondaryId = Items.PART_MUD_PIE_7166,
            productId = Items.RAW_MUD_PIE_7168,
            requiredLevel = 29,
            onProcess = { player -> sendMessage(player, "You prepare a raw mud pie.") }
        ),
        RAW_GARDEN_PIE_1(
            ingredientIds = intArrayOf(Items.CABBAGE_1965),
            secondaryId = Items.PART_GARDEN_PIE_7174,
            productId = Items.RAW_GARDEN_PIE_7176,
            requiredLevel = 34,
            onProcess = { player -> sendMessage(player, "You prepare a raw garden pie.") }
        ),
        RAW_GARDEN_PIE_2(
            ingredientIds = intArrayOf(Items.CABBAGE_1967),
            secondaryId = Items.PART_GARDEN_PIE_7174,
            productId = Items.RAW_GARDEN_PIE_7176,
            requiredLevel = 34,
            onProcess = { player -> sendMessage(player, "You prepare a raw garden pie.") }
        ),
        RAW_WILD_PIE(
            ingredientIds = intArrayOf(Items.RAW_RABBIT_3226),
            secondaryId = Items.PART_WILD_PIE_7204,
            productId = Items.RAW_WILD_PIE_7206,
            requiredLevel = 85,
            onProcess = { player -> sendMessage(player, "You prepare a raw wild pie.") }
        ),
        RAW_SUMMER_PIE(
            ingredientIds = intArrayOf(Items.COOKING_APPLE_1955),
            secondaryId = Items.PART_SUMMER_PIE_7214,
            productId = Items.RAW_SUMMER_PIE_7216,
            requiredLevel = 95,
            onProcess = { player -> sendMessage(player, "You prepare a raw summer pie.") }
        ),
        RAW_ADMIRAL_PIE(
            ingredientIds = intArrayOf(Items.PART_ADMIRAL_PIE_7194),
            secondaryId = Items.POTATO_1942,
            productId = Items.RAW_ADMIRAL_PIE_7196,
            requiredLevel = 70,
            onProcess = { player -> sendMessage(player, "You prepare an admiral pie.") }
        ),
        RAW_FISH_PIE(
            ingredientIds = intArrayOf(Items.PART_FISH_PIE_7184),
            secondaryId = Items.POTATO_1942,
            productId = Items.RAW_FISH_PIE_7186,
            requiredLevel = 47,
            onProcess = { player -> sendMessage(player, "You prepare a fish pie.") }
        ),
        // Potato recipes.
        POTATO_WITH_BUTTER(
            ingredientIds = intArrayOf(Items.PAT_OF_BUTTER_6697),
            secondaryId = Items.BAKED_POTATO_6701,
            productId = Items.POTATO_WITH_BUTTER_6703,
            requiredLevel = 39,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 40.5)
                sendMessage(player, "You add the butter to the potato.")
            }
        ),
        CHILLI_POTATO(
            ingredientIds = intArrayOf(Items.CHILLI_CON_CARNE_7062),
            secondaryId = Items.POTATO_WITH_BUTTER_6703,
            productId = Items.CHILLI_POTATO_7054,
            requiredLevel = 41,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 10.0)
                sendMessage(player, "You add the chilli con carne to the potato.")
            }
        ),
        EGG_POTATO(
            ingredientIds = intArrayOf(Items.EGG_AND_TOMATO_7064),
            secondaryId = Items.POTATO_WITH_BUTTER_6703,
            productId = Items.EGG_POTATO_7056,
            requiredLevel = 51,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 10.0)
                sendMessage(player, "You add the egg and tomato to the potato.")
            }
        ),
        MUSHROOM_POTATO(
            ingredientIds = intArrayOf(Items.MUSHROOM_AND_ONION_7066),
            secondaryId = Items.POTATO_WITH_BUTTER_6703,
            productId = Items.MUSHROOM_POTATO_7058,
            requiredLevel = 64,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 10.0)
                sendMessage(player, "You add the mushrooms and onions to the potato.")
            }
        ),
        POTATO_WITH_CHEESE(
            ingredientIds = intArrayOf(Items.CHEESE_1985),
            secondaryId = Items.POTATO_WITH_BUTTER_6703,
            productId = Items.POTATO_WITH_CHEESE_6705,
            requiredLevel = 47,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 10.0)
                sendMessage(player, "You add the cheese to the potato.")
            }
        ),
        TUNA_POTATO(
            ingredientIds = intArrayOf(Items.TUNA_AND_CORN_7068),
            secondaryId = Items.POTATO_WITH_BUTTER_6703,
            productId = Items.TUNA_POTATO_7060,
            requiredLevel = 68,
            onProcess = { player ->
                rewardXP(player, Skills.COOKING, 10.0)
                sendMessage(player, "You add the tuna and corn to the potato.")
            }
        ),
        // Skewered recipes.
        SKEWERED_BIRD_MEAT(
            ingredientIds = intArrayOf(Items.RAW_BIRD_MEAT_9978, Items.IRON_SPIT_7225),
            secondaryId = Items.IRON_SPIT_7225,
            productId = Items.SKEWERED_BIRD_MEAT_9984,
            requiredLevel = 11
        ),
        SKEWERED_RABBIT(
            ingredientIds = intArrayOf(Items.RAW_RABBIT_3226, Items.IRON_SPIT_7225),
            secondaryId = Items.IRON_SPIT_7225,
            productId = Items.SKEWERED_RABBIT_7224,
            requiredLevel = 16
        ),
        SKEWERED_BEAST(
            ingredientIds = intArrayOf(Items.RAW_BEAST_MEAT_9986, Items.IRON_SPIT_7225),
            secondaryId = Items.IRON_SPIT_7225,
            productId = Items.SKEWERED_BEAST_9992,
            requiredLevel = 21
        ),
        SKEWERED_CHOMPY(
            ingredientIds = intArrayOf(Items.RAW_CHOMPY_2876, Items.IRON_SPIT_7225),
            secondaryId = Items.IRON_SPIT_7225,
            productId = Items.SKEWERED_CHOMPY_7230,
            requiredLevel = 30
        ),
        SPIDER_ON_STICK(
            ingredientIds = intArrayOf(Items.SPIDER_CARCASS_6291, Items.SKEWER_STICK_6305),
            secondaryId = Items.SKEWER_STICK_6305,
            productId = Items.SPIDER_ON_STICK_6293
        ),
        SPIDER_ON_SHAFT(
            ingredientIds = intArrayOf(Items.SPIDER_CARCASS_6291, Items.ARROW_SHAFT_52),
            secondaryId = Items.ARROW_SHAFT_52,
            productId = Items.SPIDER_ON_SHAFT_6295
        ),
        // Nettle tea recipes.
        PLACE_NETTLES_IN_WATER(
            ingredientIds = intArrayOf(Items.NETTLES_4241, Items.BOWL_OF_WATER_1921),
            secondaryId = Items.BOWL_OF_WATER_1921,
            productId = Items.NETTLE_WATER_4237,
        ),
        ADD_MILK_TO_BOWL_OF_NETTLE_TEA(
            ingredientIds = intArrayOf(Items.BUCKET_OF_MILK_1927, Items.NETTLE_TEA_4239),
            secondaryId = Items.NETTLE_TEA_4239,
            productId = Items.NETTLE_TEA_4240,
            requiredLevel = 1,
            onProcess = { player -> addItemOrDrop(player, Items.BUCKET_1925, 1) }
        ),
        ADD_MILK_TO_PORCELAIN_CUP_OF_NETTLE_TEA(
            ingredientIds = intArrayOf(Items.BUCKET_OF_MILK_1927, Items.PORCELAIN_CUP_4244),
            secondaryId = Items.PORCELAIN_CUP_4244,
            productId = Items.CUP_OF_TEA_4246,
            requiredLevel = 1,
            onProcess = { player -> addItemOrDrop(player, Items.BUCKET_1925, 1) }
        ),
        POUR_NETTLE_TEA_INTO_EMPTY_CUP(
            ingredientIds = intArrayOf(Items.NETTLE_TEA_4239, Items.EMPTY_CUP_1980),
            secondaryId = Items.EMPTY_CUP_1980,
            productId = Items.CUP_OF_TEA_4242
        ),
        POUR_MILKY_NETTLE_TEA_INTO_EMPTY_CUP(
            ingredientIds = intArrayOf(Items.NETTLE_TEA_4240, Items.EMPTY_CUP_1980),
            secondaryId = Items.EMPTY_CUP_1980,
            productId = Items.CUP_OF_TEA_4243
        ),
        POUR_NETTLE_TEA_INTO_EMPTY_PORCELAIN_CUP(
            ingredientIds = intArrayOf(Items.NETTLE_TEA_4239, Items.PORCELAIN_CUP_4244),
            secondaryId = Items.PORCELAIN_CUP_4244,
            productId = Items.CUP_OF_TEA_4245
        ),
        POUR_MILKY_NETTLE_TEA_INTO_EMPTY_PORCELAIN_CUP(
            ingredientIds = intArrayOf(Items.NETTLE_TEA_4240, Items.PORCELAIN_CUP_4244),
            secondaryId = Items.PORCELAIN_CUP_4244,
            productId = Items.CUP_OF_TEA_4246
        );
    }
}
