package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

/**
 * Handles pie cooking recipes.
 */
class PieRecipePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating a pie shell from pastry dough and a pie dish.
         */

        onUseWith(IntType.ITEM, Items.PASTRY_DOUGH_1953, Items.PIE_DISH_2313) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                addItem(player, Items.PIE_SHELL_2315, 1)
                sendMessage(player, "You put the pastry dough into the pie dish to make a pie shell.")
            }
            return@onUseWith true
        }

        /*
         * Handles combining a pie shell with ingredients to create first part pies.
         */

        onUseWith(IntType.ITEM, PieShellRecipe.allIngredientIds, Items.PIE_SHELL_2315) { player, used, with ->
            val recipe = PieShellRecipe.byIngredientId[used.id] ?: return@onUseWith false
            handlePieRecipe(
                player, used.asItem(), with.asItem(), recipe.requiredLevel, recipe.productId,
                "You fill the pie with ${used.name.lowercase()}.",
                returnsBucket = (used.id == Items.COMPOST_6032)
            )
            return@onUseWith true
        }

        /*
         * Handles combining part pies with ingredients to create second part pies.
         */

        onUseWith(IntType.ITEM, PieSecondPartRecipe.byPair.keys.map { it.second }.toIntArray(), *PieSecondPartRecipe.firstParts) { player, used, with ->
            val recipe = PieSecondPartRecipe.byPair[with.id to used.id]
                ?: PieSecondPartRecipe.byPair[used.id to with.id] ?: return@onUseWith true
            handlePieRecipe(
                player, used.asItem(), with.asItem(), recipe.requiredLevel, recipe.productId,
                "You fill the pie with ${used.name.lowercase()}.",
                returnsBucket = recipe.returnsBucket
            )
            return@onUseWith true
        }

        /*
         * Handles combining second part pies with ingredients to create raw pies.
         */

        onUseWith(IntType.ITEM, RawPieRecipe.byPair.keys.map { it.second }.toIntArray(), *RawPieRecipe.secondParts) { player, used, with ->
            val recipe = RawPieRecipe.byPair[with.id to used.id]
                ?: RawPieRecipe.byPair[used.id to with.id] ?: return@onUseWith true
            handlePieRecipe(
                player, used.asItem(), with.asItem(), recipe.requiredLevel, recipe.productId,
                "You fill the pie with ${used.name.lowercase()}."
            )
            return@onUseWith true
        }

        /*
         * Handles creating raw admiral pie.
         */

        onUseWith(IntType.ITEM, Items.PART_ADMIRAL_PIE_7194, Items.POTATO_1942) { player, used, with ->
            handlePieRecipe(player, used.asItem(), with.asItem(), 70, Items.RAW_ADMIRAL_PIE_7196, "You prepare an admiral pie.")
            return@onUseWith true
        }

        /*
         * Handles creating raw fish pie.
         */

        onUseWith(IntType.ITEM, Items.PART_FISH_PIE_7184, Items.POTATO_1942) { player, used, with ->
            handlePieRecipe(player, used.asItem(), with.asItem(), 47, Items.RAW_FISH_PIE_7186, "You prepare a fish pie.")
            return@onUseWith true
        }
    }

    /**
     * Handle pie cooking steps.
     */
    private fun handlePieRecipe(player: Player, used: Item, with: Item, requiredLevel: Int, productId: Int, message: String, returnsBucket: Boolean = false): Boolean {
        if (!hasLevelDyn(player, Skills.COOKING, requiredLevel)) {
            sendDialogue(player, "You need a Cooking level of at least $requiredLevel to make that.")
            return true
        }

        val maxAmount = min(amountInInventory(player, used.id), amountInInventory(player, with.id))

        val process = {
            if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                sendMessage(player, "You don't have the required ingredients.")
                false
            } else {
                if (returnsBucket) addItemOrDrop(player, Items.BUCKET_1925, 1)
                addItem(player, productId, 1)
                sendMessage(player, message)
                true
            }
        }

        if (maxAmount == 1) {
            process()
        } else {
            sendSkillDialogue(player) {
                withItems(productId)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) process()
                    }
                }
                calculateMaxAmount { maxAmount }
            }
        }
        return true
    }

    enum class PieShellRecipe(val ingredientId: Int, val requiredLevel: Int, val productId: Int) {
        REDBERRY(Items.REDBERRIES_1951, 10, Items.UNCOOKED_BERRY_PIE_2321),
        MEAT_1(Items.COOKED_MEAT_2142, 20, Items.UNCOOKED_MEAT_PIE_2319),
        MEAT_2(Items.COOKED_CHICKEN_2140, 20, Items.UNCOOKED_MEAT_PIE_2319),
        MUD(Items.COMPOST_6032, 29, Items.PART_MUD_PIE_7164),
        APPLE(Items.COOKING_APPLE_1955, 30, Items.UNCOOKED_APPLE_PIE_2317),
        GARDEN(Items.TOMATO_1982, 34, Items.PART_GARDEN_PIE_7172),
        FISH(Items.TROUT_333, 47, Items.PART_FISH_PIE_7182),
        ADMIRAL(Items.SALMON_329, 70, Items.PART_ADMIRAL_PIE_7192),
        WILD(Items.RAW_BEAR_MEAT_2136, 85, Items.PART_WILD_PIE_7202),
        SUMMER(Items.STRAWBERRY_5504, 95, Items.PART_SUMMER_PIE_7212);

        companion object {
            val byIngredientId = values().associateBy { it.ingredientId }
            val allIngredientIds = values().map { it.ingredientId }.toIntArray()
            val allProductIds = values().map { it.productId }.toIntArray()
        }
    }

    enum class PieSecondPartRecipe(val firstPartId: Int, val ingredientId: Int, val requiredLevel: Int, val productId: Int, val returnsBucket: Boolean = false) {
        MUD(Items.PART_MUD_PIE_7164, Items.BUCKET_OF_WATER_1929, 29, Items.PART_MUD_PIE_7166, true),
        GARDEN(Items.PART_GARDEN_PIE_7172, Items.ONION_1957, 34, Items.PART_GARDEN_PIE_7174),
        FISH(Items.PART_FISH_PIE_7182, Items.COD_339, 47, Items.PART_FISH_PIE_7184),
        ADMIRAL(Items.PART_ADMIRAL_PIE_7192, Items.TUNA_361, 70, Items.PART_ADMIRAL_PIE_7194),
        WILD(Items.PART_WILD_PIE_7202, Items.RAW_CHOMPY_2876, 85, Items.PART_WILD_PIE_7204),
        SUMMER(Items.PART_SUMMER_PIE_7212, Items.WATERMELON_5982, 95, Items.PART_SUMMER_PIE_7214);

        companion object {
            val byPair = values().associateBy { it.firstPartId to it.ingredientId }
            val firstParts = values().map { it.firstPartId }.toIntArray()
        }
    }

    enum class RawPieRecipe(val secondPartId: Int, val ingredientId: Int, val requiredLevel: Int, val productId: Int) {
        MUD(Items.PART_MUD_PIE_7166, Items.CLAY_434, 29, Items.RAW_MUD_PIE_7168),
        GARDEN1(Items.PART_GARDEN_PIE_7174, Items.CABBAGE_1965, 34, Items.RAW_GARDEN_PIE_7176),
        GARDEN2(Items.PART_GARDEN_PIE_7174, Items.CABBAGE_1967, 34, Items.RAW_GARDEN_PIE_7176),
        WILD(Items.PART_WILD_PIE_7204, Items.RAW_RABBIT_3226, 85, Items.RAW_WILD_PIE_7206),
        SUMMER(Items.PART_SUMMER_PIE_7214, Items.COOKING_APPLE_1955, 95, Items.RAW_SUMMER_PIE_7216);

        companion object {
            val byPair = values().associateBy { it.secondPartId to it.ingredientId }
            val secondParts = values().map { it.secondPartId }.toIntArray()
        }
    }
}
