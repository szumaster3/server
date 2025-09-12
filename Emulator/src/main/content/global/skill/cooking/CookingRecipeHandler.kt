package content.global.skill.cooking

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Items
import kotlin.math.min

/**
 * Handles cooking recipes.
 */
class CookingRecipeHandler : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles create single-ingredient recipes.
         */

        onUseWith(IntType.ITEM, CookingRecipe.INGREDIENT_IDS, *CookingRecipe.SECONDARY_IDS) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val recipe = CookingRecipe.values().find { r ->
                (r.ingredientID == used.id && r.secondaryID == with.id) ||
                        (r.ingredientID == with.id && r.secondaryID == used.id)
            }

            if (recipe == null) {
                sendMessage(player, "You do not have the required ingredients to make this.")
                return@onUseWith true
            }

            if (!hasLevelDyn(player, Skills.COOKING, recipe.requiredLevel)) {
                sendMessage(player, "You need a Cooking level of at least ${recipe.requiredLevel} to make this.")
                return@onUseWith true
            }

            if (!inInventory(player, recipe.ingredientID) || !inInventory(player, recipe.secondaryID)) {
                sendMessage(player, "You do not have the required ingredients to make this.")
                return@onUseWith true
            }

            if (recipe.requiresKnife && !inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to prepare this recipe.")
                return@onUseWith true
            }

            if(used.id == Items.CHEESE_1985 && with.id == Items.BAKED_POTATO_6701) {
                sendMessage(player, "You must add butter to the baked potato before adding toppings.")
                return@onUseWith true
            }

            val ingredientAmount = amountInInventory(player, recipe.ingredientID)
            val secondaryAmount = amountInInventory(player, recipe.secondaryID)
            val maxAmount = min(ingredientAmount, secondaryAmount)
            if (maxAmount <= 0) {
                sendMessage(player, "You do not have the required ingredients to make this.")
                return@onUseWith true
            }

            val handler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(recipe.productID)) {
                override fun create(amount: Int, index: Int) {
                    player.pulseManager.run(object : SkillPulse<Item?>(player, Item(recipe.productID)) {
                        private var remaining = amount
                        private var tick = 0

                        override fun checkRequirements(): Boolean =
                            inInventory(player, recipe.ingredientID) &&
                                    (!recipe.requiresKnife && inInventory(player, recipe.secondaryID) ||
                                            recipe.requiresKnife && inInventory(player, Items.KNIFE_946))

                        override fun animate() {
                            recipe.animation?.let { animate(player, it) }
                        }

                        override fun reward(): Boolean {
                            if (tick < 2) {
                                tick++
                                return false
                            }
                            processRecipe(player, recipe)
                            remaining--
                            tick = 0
                            delayClock(player, Clocks.SKILLING, 2)
                            return remaining <= 0
                        }
                    })
                }

                override fun getAll(index: Int): Int = maxAmount
            }

            if (maxAmount > 1) handler.open() else handler.create(0, 1)

            return@onUseWith true
        }

        /*
         * Handles create ugthanki kebab.
         */

        onUseWith(IntType.ITEM, Items.PITTA_BREAD_1865, Items.KEBAB_MIX_1881) { player, _, _ ->
            handleSpecialRecipe(player)
            return@onUseWith true
        }

        /*
         * Handles create a cake.
         */

        onUseWith(IntType.ITEM, Items.CAKE_TIN_1887, Items.EGG_1944, Items.BUCKET_OF_MILK_1927, Items.POT_OF_FLOUR_1933) { player, _, _ ->
            handleCakeRecipe(player)
            return@onUseWith true
        }

        /*
         * Handles create uncooked curry.
         */

        onUseWith(IntType.ITEM, Items.CURRY_LEAF_5970, Items.UNCOOKED_STEW_2001) { player, usedNode, withNode ->
            if (!hasLevelDyn(player, Skills.COOKING, 60)) {
                sendDialogue(player, "You need an Cooking level of at least 60 to make that.")
                return@onUseWith true
            }

            val requiredAmount = 3
            val amount = amountInInventory(player, usedNode.id)

            if (amount < requiredAmount) {
                sendMessage(player, "You need ${requiredAmount - amount} more curry leaves to mix with the stew.")
                return@onUseWith true
            }

            if (removeItem(player, Item(usedNode.id, requiredAmount), Container.INVENTORY) &&
                removeItem(player, Item(withNode.id, 1), Container.INVENTORY)) {
                addItem(player, Items.UNCOOKED_CURRY_2009, 1, Container.INVENTORY)
                sendMessage(player, "You mix the curry leaves with the stew.")
            }
            return@onUseWith true
        }
    }

    private fun processRecipe(player: Player, recipe: CookingRecipe) {
        if (!removeItem(player, recipe.ingredientID)) return
        if (!removeItem(player, recipe.secondaryID)) return
        recipe.returnsContainer?.let { addItemOrDrop(player, it, 1) }
        addItem(player, recipe.productID, 1)
        recipe.xpReward?.let { rewardXP(player, Skills.COOKING, it) }
        recipe.message?.let { sendMessage(player, it) }
    }

    /**
     * Handles a special ugthanki kebab recipe.
     *
     * @param player The player.
     */
    private fun handleSpecialRecipe(player: Player) {
        if (!hasLevelDyn(player, Skills.COOKING, 58)) {
            sendDialogue(player, "You need a Cooking level of at least 58 to make that.")
            return
        }
        if (!inInventory(player, Items.PITTA_BREAD_1865) || !inInventory(player, Items.KEBAB_MIX_1881)) {
            sendMessage(player, "You don't have the required ingredients.")
            return
        }
        if (freeSlots(player) < 1) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return
        }

        removeItem(player, Items.PITTA_BREAD_1865)
        removeItem(player, Items.KEBAB_MIX_1881)
        addItem(player, Items.BOWL_1923, 1)

        if (RandomFunction.roll(50)) {
            addItem(player, Items.UGTHANKI_KEBAB_1885)
            sendMessage(player, "Your kebab smells a bit off, but you keep it.")
        } else {
            rewardXP(player, Skills.COOKING, 40.0)
            addItem(player, Items.UGTHANKI_KEBAB_1883, 1)
            sendMessage(player, "You make a delicious ugthanki kebab.")
        }
    }

    /**
     * Handles creating the uncooked cake.
     *
     * @param player The player.
     */
    private fun handleCakeRecipe(player: Player) {
        val ingredients = intArrayOf(
            Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944, Items.CAKE_TIN_1887
        )
        if (!hasLevelDyn(player, Skills.COOKING, 40)) {
            sendDialogue(player, "You need a Cooking level of at least 40 to make this cake.")
            return
        }

        if (!anyInInventory(player, *ingredients)) return
        if (freeSlots(player) < 1) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return
        }

        ingredients.forEach { id ->
            val item = player.inventory.get(id)
            if (item != null && item.slot > -1) {
                when (id) {
                    Items.POT_OF_FLOUR_1933 -> player.inventory.replace(Item(Items.EMPTY_POT_1931, 1), item.slot)
                    Items.BUCKET_OF_MILK_1927 -> player.inventory.replace(Item(Items.BUCKET_1925, 1), item.slot)
                    Items.EGG_1944, Items.CAKE_TIN_1887 -> player.inventory.remove(Item(id, 1))
                }
            }
        }

        addItem(player, Items.UNCOOKED_CAKE_1889, 1)

        rewardXP(player, Skills.COOKING, 40.0)
        sendMessage(player, "You mix the milk, flour, and egg together to make a raw cake mix.")
        delayClock(player, Clocks.SKILLING, 2)
    }

}
