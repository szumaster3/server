package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

/**
 * Handles potato recipes.
 */
class PotatoRecipePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles attempting to add cheese to a baked potato without butter.
         */

        onUseWith(IntType.ITEM, CHEESE, POTATO) { player, _, _ ->
            sendMessage(player, "You must add butter to the baked potato before adding toppings.")
            return@onUseWith true
        }

        /*
         * Handles creating a potato with butter by combining a Baked potato with a pat of butter.
         *
         * Requirements:
         *  - Potato with butter: Level 39 Cooking, +40.5 XP (using Items.PAT_OF_BUTTER_6697)
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, PAT_OF_BUTTER, BAKED_POTATO) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 39)) {
                sendDialogue(player, "You need an Cooking level of at least 39 to make that.")
                return@onUseWith true
            }

            val usedAmount = amountInInventory(player, used.id)
            val withAmount = amountInInventory(player, with.id)
            val maxAmount = minOf(usedAmount, withAmount)

            fun process(): Boolean {
                if (!removeItem(player, used.asItem(), Container.INVENTORY) || !removeItem(player, with.asItem(), Container.INVENTORY)) {
                    sendMessage(player, "You don't have the required ingredients to make that.")
                    return false
                }
                addItem(player, POTATO_WITH_BUTTER, 1, Container.INVENTORY)
                rewardXP(player, Skills.COOKING, 40.5)
                sendMessage(player, "You add the butter to the potato.")
                return true
            }

            if (maxAmount == 1) {
                process()
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(POTATO_WITH_BUTTER)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) process()
                    }
                }
                calculateMaxAmount { maxAmount }
            }

            return@onUseWith true
        }

        /*
         * Handles combining a potato with butter and additional ingredients to create specialty potatoes.
         *
         * Requirements:
         *  - Chilli Potato:        Level 41 Cooking, +10.0 XP (using Items.CHILLI_CON_CARNE_7062)
         *  - Egg Potato:           Level 51 Cooking, +10.0 XP (using Items.EGG_AND_TOMATO_7064)
         *  - Mushroom Potato:      Level 64 Cooking, +10.0 XP (using Items.MUSHROOM_AND_ONION_7066)
         *  - Potato with Cheese:   Level 47 Cooking, +10.0 XP (using Items.CHEESE_1985)
         *  - Tuna Potato:          Level 68 Cooking, +10.0 XP (using Items.TUNA_AND_CORN_7068)
         *
         * Ticks: 2 (1.2 seconds)
         */

        val potatoRecipes = mapOf(
            CHILLI_CON_CARNE to Pair(CHILLI_POTATO, 41),
            EGG_AND_TOMATO to Pair(EGG_POTATO, 51),
            MUSHROOM_AND_ONION to Pair(MUSHROOM_POTATO, 64),
            CHEESE to Pair(POTATO_WITH_CHEESE, 47),
            TUNA_AND_CORN to Pair(TUNA_POTATO, 68)
        )

        potatoRecipes.forEach { (ingredientID, recipeData) ->
            val (productID, requiredLevel) = recipeData
            onUseWith(IntType.ITEM, ingredientID, POTATO_WITH_BUTTER) { player, used, with ->
                if (!hasLevelDyn(player, Skills.COOKING, requiredLevel)) {
                    sendDialogue(player, "You need an Cooking level of at least $requiredLevel to make that.")
                    return@onUseWith true
                }

                val amountUsed = amountInInventory(player, used.id)
                val amountWith = amountInInventory(player, with.id)
                val maxAmount = minOf(amountUsed, amountWith)
                val dropEmptyBowl = ingredientID != CHEESE

                fun process(): Boolean {
                    if (!removeItem(player, used.asItem(), Container.INVENTORY) || !removeItem(player, with.asItem(), Container.INVENTORY)) {
                        sendMessage(player, "You don't have the required ingredients to make that.")
                        return false
                    }
                    if (dropEmptyBowl) {
                        addItemOrDrop(player, EMPTY_BOWL, 1)
                    }
                    addItem(player, productID, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 10.0)
                    sendMessage(player, "You add the ${used.name.lowercase()} to the potato.")
                    return true
                }

                if (maxAmount == 1) {
                    process()
                    return@onUseWith true
                }

                sendSkillDialogue(player) {
                    withItems(productID)
                    create { _, amount ->
                        runTask(player, 2, amount) {
                            process()
                        }
                    }
                    calculateMaxAmount { maxAmount }
                }

                return@onUseWith true
            }
        }
    }

    companion object {
        private const val EMPTY_BOWL         = Items.BOWL_1923
        private const val POTATO             = Items.POTATO_1942
        private const val BAKED_POTATO       = Items.BAKED_POTATO_6701
        private const val PAT_OF_BUTTER      = Items.PAT_OF_BUTTER_6697
        private const val CHEESE             = Items.CHEESE_1985
        private const val POTATO_WITH_BUTTER = Items.POTATO_WITH_BUTTER_6703
        private const val CHILLI_POTATO      = Items.CHILLI_POTATO_7054
        private const val CHILLI_CON_CARNE   = Items.CHILLI_CON_CARNE_7062
        private const val EGG_POTATO         = Items.EGG_POTATO_7056
        private const val EGG_AND_TOMATO     = Items.EGG_AND_TOMATO_7064
        private const val MUSHROOM_POTATO    = Items.MUSHROOM_POTATO_7058
        private const val MUSHROOM_AND_ONION = Items.MUSHROOM_AND_ONION_7066
        private const val POTATO_WITH_CHEESE = Items.POTATO_WITH_CHEESE_6705
        private const val TUNA_AND_CORN      = Items.TUNA_AND_CORN_7068
        private const val TUNA_POTATO        = Items.TUNA_POTATO_7060

    }
}