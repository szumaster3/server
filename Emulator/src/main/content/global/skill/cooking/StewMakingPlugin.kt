package content.global.skill.cooking

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Handles stew-related cooking recipes.
 */
class StewMakingPlugin : InteractionListener {

    override fun defineListeners() {

        /**
         * Registers a recipe handler.
         */
        fun registerRecipe(
            requiredLevel: Int,
            used: Int,
            with: Int,
            output: Int,
            ingredientName: (Item) -> String = { it.name.lowercase() },
            successMessage: (Item) -> String,
            onProcess: (Player, Item, Item) -> Boolean
        ) {
            onUseWith(IntType.ITEM, used, with) { player, usedNode, withNode ->
                if (!hasLevelDyn(player, Skills.COOKING, requiredLevel)) {
                    sendDialogue(player, "You need an Cooking level of at least $requiredLevel to make that.")
                    return@onUseWith true
                }

                val usedItem = usedNode.asItem()
                val withItem = withNode.asItem()
                val amountUsed = amountInInventory(player, usedNode.id)
                val amountWith = amountInInventory(player, withNode.id)

                fun process(): Boolean {
                    if (!onProcess(player, usedItem, withItem)) {
                        sendMessage(player, "You don't have the required ingredients.")
                        return false
                    }
                    sendMessage(player, successMessage(usedItem))
                    return true
                }

                if (minOf(amountUsed, amountWith) <= 1) {
                    process()
                    return@onUseWith true
                }

                sendSkillDialogue(player) {
                    withItems(output)
                    create { _, amount ->
                        runTask(player, 2, amount) {
                            if (amount > 0) process()
                        }
                    }
                    calculateMaxAmount { minOf(amountUsed, amountWith) }
                }
                return@onUseWith true
            }
        }

        /*
         * Handles creating incomplete stew.
         */
        listOf(POTATO, COOKED_MEAT).forEach { ingredient ->
            registerRecipe(
                requiredLevel = 25,
                used = ingredient,
                with = BOWL_OF_WATER,
                output = 0,
                ingredientName = { it.name.lowercase() },
                successMessage = { item -> "You cut up the ${item.name.lowercase()} and put it into the bowl." }
            ) { player, usedItem, withItem ->
                val stew = when (usedItem.id) {
                    POTATO -> POTATO_STEW
                    COOKED_MEAT -> MEAT_STEW
                    else -> return@registerRecipe false
                }
                if (!removeItem(player, usedItem, Container.INVENTORY) || !removeItem(player, withItem, Container.INVENTORY)) return@registerRecipe false
                addItem(player, stew, 1, Container.INVENTORY)
                return@registerRecipe true
            }
        }

        /*
         * Handles creating Uncooked stew.
         */

        STEW_INGREDIENTS.forEach { ingredient ->
            INCOMPLETE_STEW.forEach { incompleteStew ->
                registerRecipe(
                    requiredLevel = 25,
                    used = ingredient,
                    with = incompleteStew,
                    output = UNCOOKED_STEW,
                    ingredientName = { it.name.lowercase().replace("cooked", "").trim() },
                    successMessage = { item -> "You cut up the ${item.name.lowercase().replace("cooked", "").trim()} and put it into the stew." }
                ) { player, usedItem, withItem ->
                    if (!removeItem(player, usedItem, Container.INVENTORY) || !removeItem(player, withItem, Container.INVENTORY)) return@registerRecipe false
                    addItem(player, UNCOOKED_STEW, 1, Container.INVENTORY)
                    return@registerRecipe true
                }
            }
        }

        /*
         * Handles creating Uncooked curry.
         */

        registerRecipe(
            requiredLevel = 60,
            used = UNCOOKED_STEW,
            with = SPICE,
            output = UNCOOKED_CURRY,
            successMessage = { _ -> "You mix the spice with the stew." }
        ) { player, usedItem, withItem ->
            if (!removeItem(player, usedItem, Container.INVENTORY) || !removeItem(player, withItem, Container.INVENTORY)) return@registerRecipe false
            addItem(player, UNCOOKED_CURRY, 1, Container.INVENTORY)
            return@registerRecipe true
        }

        /*
         * Handles creating Uncooked curry.
         */

        onUseWith(IntType.ITEM, CURRY_LEAF, UNCOOKED_STEW) { player, usedNode, withNode ->
            if (!hasLevelDyn(player, Skills.COOKING, 60)) {
                sendDialogue(player, "You need an Cooking level of at least 60 to make that.")
                return@onUseWith true
            }

            val requiredAmount = 3
            val amount = amountInInventory(player, usedNode.id)

            if (amount < requiredAmount) {
                sendMessage(player, "You need ${requiredAmount - amount} curry leaves to mix with the stew.")
                return@onUseWith true
            }

            if (removeItem(player, Item(usedNode.id, requiredAmount), Container.INVENTORY) &&
                removeItem(player, Item(withNode.id, 1), Container.INVENTORY)) {
                addItem(player, UNCOOKED_CURRY, 1, Container.INVENTORY)
                sendMessage(player, "You mix the curry leaves with the stew.")
            }
            return@onUseWith true
        }
    }

    companion object {
        private const val BOWL_OF_WATER  = Items.BOWL_OF_WATER_1921
        private const val UNCOOKED_STEW  = Items.UNCOOKED_STEW_2001
        private const val UNCOOKED_CURRY = Items.UNCOOKED_CURRY_2009
        private const val SPICE          = Items.SPICE_2007
        private const val CURRY_LEAF     = Items.CURRY_LEAF_5970
        private const val POTATO         = Items.POTATO_1942
        private const val POTATO_STEW    = Items.INCOMPLETE_STEW_1997
        private const val MEAT_STEW      = Items.INCOMPLETE_STEW_1999
        private const val COOKED_MEAT    = Items.COOKED_MEAT_2142

        private val STEW_INGREDIENTS = intArrayOf(COOKED_MEAT, POTATO)
        private val INCOMPLETE_STEW  = intArrayOf(POTATO_STEW, MEAT_STEW)
    }
}
