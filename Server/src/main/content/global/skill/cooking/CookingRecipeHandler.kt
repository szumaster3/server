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
import shared.consts.Animations
import shared.consts.Items
import kotlin.math.min

/**
 * Handles cooking recipes.
 */
class CookingRecipeHandler : InteractionListener {

    private val cakeIngredients = intArrayOf(Items.EGG_1944, Items.BUCKET_OF_MILK_1927, Items.POT_OF_FLOUR_1933)

    override fun defineListeners() {

        /*
         * Handles create single-ingredient recipes.
         */

        CookingRecipe.values().forEach { recipe ->
            onUseWith(IntType.ITEM, recipe.ingredientID, recipe.secondaryID) { player, used, with ->
                if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

                if (!hasLevelDyn(player, Skills.COOKING, recipe.requiredLevel)) {
                    sendMessage(player, "You need a Cooking level of at least ${recipe.requiredLevel} to make this.")
                    return@onUseWith true
                }

                if (recipe.requiresKnife && !inInventory(player, Items.KNIFE_946)) {
                    sendMessage(player, "You need a knife to prepare this.")
                    return@onUseWith true
                }

                if (recipe.ingredientID == Items.CHEESE_1985 && recipe.secondaryID == Items.BAKED_POTATO_6701) {
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

                if (maxAmount == 1) {
                    removeItem(player, recipe.ingredientID)
                    removeItem(player, recipe.secondaryID)
                    addItem(player, recipe.productID)
                    recipe.returnsContainer?.let { addItem(player, it, 1) }
                    recipe.xpReward?.let { rewardXP(player, Skills.COOKING, it) }
                    recipe.message?.let { sendMessage(player, it) }
                } else {
                    val handler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(recipe.productID)) {
                        override fun create(amount: Int, index: Int) {
                            player.pulseManager.run(object : SkillPulse<Item?>(player, Item(recipe.productID)) {
                                private var remaining = amount
                                private var tick = 0

                                override fun checkRequirements(): Boolean {
                                    val hasIngredient = inInventory(player, recipe.ingredientID)
                                    val hasSecondary = inInventory(player, recipe.secondaryID)
                                    val hasKnife = !recipe.requiresKnife || inInventory(player, Items.KNIFE_946)
                                    return hasIngredient && hasSecondary && hasKnife
                                }

                                override fun animate() {}

                                override fun reward(): Boolean {
                                    if (tick < 2) {
                                        tick++
                                        return false
                                    }

                                    removeItem(player, recipe.ingredientID)
                                    removeItem(player, recipe.secondaryID)
                                    addItem(player, recipe.productID)
                                    recipe.returnsContainer?.let { addItem(player, it, 1) }
                                    recipe.xpReward?.let { rewardXP(player, Skills.COOKING, it) }
                                    recipe.message?.let { sendMessage(player, it) }

                                    remaining--
                                    tick = 0
                                    delayClock(player, Clocks.SKILLING, 2)
                                    return remaining <= 0
                                }
                            })
                        }

                        override fun getAll(index: Int): Int {
                            val ingredientAmount = amountInInventory(player, recipe.ingredientID)
                            val secondaryAmount = amountInInventory(player, recipe.secondaryID)
                            return min(ingredientAmount, secondaryAmount)
                        }
                    }

                    handler.open()
                }

                return@onUseWith true
            }
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


        onUseWith(IntType.ITEM, Items.CAKE_TIN_1887, *cakeIngredients) { player, _, _ ->
            if (!hasLevelDyn(player, Skills.COOKING, 40)) {
                sendDialogue(player, "You need a Cooking level of at least 40 to make this cake.")
                return@onUseWith true
            }

            val missing = cakeIngredients.filter { !inInventory(player, it) }
            if (missing.isNotEmpty()) {
                sendMessage(player, "You don't have all the ingredients to make a cake.")
                return@onUseWith true
            }

            if (freeSlots(player) < 1) {
                sendMessage(player, "You don't have enough space in your inventory.")
                return@onUseWith true
            }

            cakeIngredients.forEach { id ->
                when (id) {
                    Items.POT_OF_FLOUR_1933 -> {
                        removeItem(player, id)
                        addItem(player, Items.EMPTY_POT_1931, 1)
                    }
                    Items.BUCKET_OF_MILK_1927 -> {
                        removeItem(player, id)
                        addItem(player, Items.BUCKET_1925, 1)
                    }
                    Items.EGG_1944 -> removeItem(player, id)
                    else -> removeItem(player, id)
                }
            }

            removeItem(player, Items.CAKE_TIN_1887)

            rewardXP(player, Skills.COOKING, 40.0)
            sendMessage(player, "You mix the milk, flour, and egg together to make a raw cake mix.")
            addItem(player, Items.UNCOOKED_CAKE_1889, 1)

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

        /*
         * Handles carve the calquat fruit.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.CALQUAT_FRUIT_5980) { player, _, with ->
            val usedItem = with.asItem()
            if (removeItem(player, usedItem, Container.INVENTORY)) {
                animate(player, Animations.CARVE_CALQUAT_KEG_2290)
                addItem(player, Items.CALQUAT_KEG_5769, 1, Container.INVENTORY)
                sendMessage(player, "You carve the calquat fruit into a keg.")
            }
            return@onUseWith true
        }

        /*
         * Handles cut the chocolate bar.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.CHOCOLATE_BAR_1973) { player, _, with ->
            val usedItem = with.asItem()
            if (removeItem(player, usedItem, Container.INVENTORY)) {
                animate(player, Animations.CUTTING_CHOCOLATE_BAR_1989)
                addItem(player, Items.CHOCOLATE_DUST_1975, 1, Container.INVENTORY)
                sendMessage(player, "You cut the chocolate bar into chocolate dust.")
            }

            return@onUseWith true
        }
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
}
