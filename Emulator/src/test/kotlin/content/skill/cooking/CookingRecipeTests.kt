package content.global.skill.cooking

import TestUtils
import core.api.addItem
import core.api.anyInInventory
import core.api.inInventory
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.rs.consts.Items

class CookingRecipeTests {

    init {
        TestUtils.preTestSetup()
    }

    @Test
    fun testCookingRecipes() {
        TestUtils.getMockPlayer("Cooking").use { player ->
            player.skills.setLevel(Skills.COOKING, 99)
            player.inventory.clear()

            CookingRecipePlugin.CookingRecipe.values().forEach { recipe ->
                recipe.ingredientIds.forEach { ingredientId ->
                    addItem(player, ingredientId, 5)
                }
                addItem(player, recipe.secondaryId, 5)

                if (recipe.requiresKnife) {
                    addItem(player, Items.KNIFE_946, 1)
                }

                player.skills.setLevel(Skills.COOKING, recipe.requiredLevel)

                val initialIngredientAmount = player.inventory.getAmount(recipe.ingredientIds.first())
                val initialSecondaryAmount = player.inventory.getAmount(recipe.secondaryId)

                val success = handleCooking(player, recipe)

                assertTrue(success, "Recipe ${recipe.name} should cook successfully")
                assertEquals(initialIngredientAmount - 1, player.inventory.getAmount(recipe.ingredientIds.first()), "Ingredient should decrease by 1")
                assertEquals(initialSecondaryAmount - 1, player.inventory.getAmount(recipe.secondaryId), "Secondary ingredient should decrease by 1")
                assertTrue(inInventory(player, recipe.productId), "Product should be added to inventory")

                val expectedProduct = recipe.productId
                val actualProduct = getProductFromIngredients(recipe.ingredientIds, recipe.secondaryId)
                assertEquals(expectedProduct, actualProduct, "Product should match the recipe ingredients")
                player.inventory.clear()
            }
        }
    }

    private fun handleCooking(player: Player, recipe: CookingRecipePlugin.CookingRecipe): Boolean {
        if (player.skills.getLevel(Skills.COOKING) < recipe.requiredLevel) return false

        if (recipe.requiresKnife && !inInventory(player, Items.KNIFE_946)) return false

        if (recipe.ingredientIds.none { inInventory(player, it) }) return false
        if (!anyInInventory(player, recipe.secondaryId)) return false

        val ingredientId = recipe.ingredientIds.first { inInventory(player, it) }
        if (!player.inventory.remove(Item(ingredientId, 1))) return false
        if (!player.inventory.remove(Item(recipe.secondaryId, 1))) return false

        addItem(player, recipe.productId, 1)
        recipe.onProcess?.invoke(player)

        return true
    }

    private fun getProductFromIngredients(ingredients: IntArray, secondary: Int): Int {
        return CookingRecipePlugin.CookingRecipe.values()
            .firstOrNull { recipe ->
                recipe.ingredientIds.sorted() == ingredients.sorted() &&
                        recipe.secondaryId == secondary
            }?.productId ?: -1
    }
}