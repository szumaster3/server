package content.global.skill.cooking.recipes

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import org.rs.consts.Items

class PizzaRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating a incomplete pizza.
         *
         * Requirements:
         *  - Cooking Level:    Level 35 Cooking, +0.0 XP (using Tomato and Pizza Base)
         *
         */

        onUseWith(IntType.ITEM, Items.PIZZA_BASE_2283, Items.TOMATO_1982) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 35)) {
                sendDialogue(player, "You need an Cooking level of at least 35 to make that.")
                return@onUseWith true
            }

            val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(
                player, with.asItem(), Container.INVENTORY
            )
            if (success) {
                addItem(player, Items.INCOMPLETE_PIZZA_2285, 1, Container.INVENTORY)
                sendMessage(player, "You add the tomato to the pizza base.")
            }
            return@onUseWith true
        }

        /*
         * Handles creating a uncooked pizza.
         *
         * Requirements:
         *  - Cooking Level:    Level 35 Cooking, +0.0 XP (using Cheese and Incomplete Pizza)
         *
         */

        onUseWith(IntType.ITEM, Items.CHEESE_1985, Items.INCOMPLETE_PIZZA_2285) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 35)) {
                sendDialogue(player, "You need an Cooking level of at least 35 to make that.")
                return@onUseWith true
            }

            val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
            if (success) {
                addItem(player, Items.UNCOOKED_PIZZA_2287, 1, Container.INVENTORY)
                sendMessage(player, "You add the cheese to the incomplete pizza.")
            }
            return@onUseWith true
        }


        /*
         * Handles combining a plain pizza with additional ingredients to create specialty pizzas.
         *
         * Requirements:
         *  - Meat Pizza:      Level 45 Cooking, +26.0 XP (using Cooked meat or Cooked chicken)
         *  - Anchovy Pizza:   Level 55 Cooking, +39.0 XP (using Anchovies)
         *  - Pineapple Pizza: Level 65 Cooking, +52.0 XP (using Pineapple chunks or Pineapple rings)
         *
         * Ticks: 3 (1.8 seconds)
         */

        val pizzaMap = mapOf(
            Items.COOKED_MEAT_2142 to Triple(Items.MEAT_PIZZA_2293, 45, 26.0),
            Items.COOKED_CHICKEN_2140 to Triple(Items.MEAT_PIZZA_2293, 45, 26.0),
            Items.ANCHOVIES_319 to Triple(Items.ANCHOVY_PIZZA_2297, 55, 39.0),
            Items.PINEAPPLE_CHUNKS_2116 to Triple(Items.PINEAPPLE_PIZZA_2301, 65, 52.0),
            Items.PINEAPPLE_RING_2118 to Triple(Items.PINEAPPLE_PIZZA_2301, 65, 52.0)
        )

        onUseWith(IntType.ITEM, pizzaIngredients, Items.PLAIN_PIZZA_2289) { player, used, with ->
            val (productID, requiredLevel, experience) = pizzaMap[used.id] ?: return@onUseWith false

            if (!hasLevelDyn(player, Skills.COOKING, requiredLevel)) {
                sendDialogue(player, "You need an Cooking level of at least $requiredLevel to make that.")
                return@onUseWith true
            }

            player.pulseManager.run(object : Pulse(1) {
                override fun pulse(): Boolean {
                    super.setDelay(3)
                    val amount = amountInInventory(player, used.id)
                    if (amount > 0) {
                        val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                        if (success) {
                            addItem(player, productID, 1, Container.INVENTORY)
                            rewardXP(player, Skills.COOKING, experience)
                            val ingredientName = used.name.lowercase()
                            sendMessage(player, "You add the $ingredientName to the pizza.")
                        }
                    }
                    return amount <= 0
                }
            })
            return@onUseWith true
        }
    }

    companion object {
        private val pizzaIngredients = intArrayOf(
            Items.COOKED_CHICKEN_2140,
            Items.COOKED_MEAT_2142,
            Items.PINEAPPLE_CHUNKS_2116,
            Items.PINEAPPLE_RING_2118,
            Items.ANCHOVIES_319
        )
    }
}