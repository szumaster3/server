package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class StewRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating incomplete stews by combining stew ingredients with a bowl of water.
         *
         * Products:
         *  - Potato Stew
         *  - Meat Stew
         *
         * Required Cooking Level: 25
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, stewIngredients, BOWL_OF_WATER) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 25)) {
                sendDialogue(player, "You need an Cooking level of at least 25 to make that.")
                return@onUseWith true
            }

            val stew = when (used.id) {
                POTATO -> POTATO_STEW
                COOKED_MEAT -> MEAT_STEW
                else -> return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(
                    player, with.asItem(), Container.INVENTORY
                )
                if (success) {
                    addItem(player, stew, 1, Container.INVENTORY)
                    val ingredientName = used.name.lowercase()
                    sendMessage(player, "You cut up the $ingredientName and put it into the bowl.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith process()
            }

            sendSkillDialogue(player) {
                withItems(stew)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        process()
                    }
                }
                calculateMaxAmount {
                    minOf(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating an uncooked stew by combining stew ingredients with incomplete stew.
         *
         * Product:
         *  - Uncooked Stew
         *
         * Required Cooking Level: 25
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, stewIngredients, *incompleteStew) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 25)) {
                sendDialogue(player, "You need an Cooking level of at least 25 to make that.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(
                    player, with.asItem(), Container.INVENTORY
                )
                if (success) {
                    addItem(player, UNCOOKED_STEW, 1, Container.INVENTORY)
                    val ingredientName = used.name.lowercase().replace("cooked", "").trim()
                    sendMessage(player, "You cut up the $ingredientName and put it into the stew.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                process()
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(UNCOOKED_STEW)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        process()
                    }
                }
                calculateMaxAmount {
                    minOf(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating an uncooked curry by adding spice to an uncooked stew.
         *
         * Product:
         *  - Uncooked Curry
         *
         * Required Cooking Level: 60
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, UNCOOKED_STEW, SPICE) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 60)) {
                sendDialogue(player, "You need an Cooking level of at least 60 to make that.")
                return@onUseWith true
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, UNCOOKED_CURRY, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the spice with the stew.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(UNCOOKED_CURRY)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(
                            player, with.asItem(), Container.INVENTORY
                        )
                        if (success) {
                            addItem(player, UNCOOKED_CURRY, 1, Container.INVENTORY)
                            sendMessage(player, "You mix the spice with the stew.")
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    minOf(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating an uncooked curry by adding curry leaves to an uncooked stew.
         *
         * Product:
         *  - Uncooked Curry
         *
         * Required Cooking Level: 60
         * Required Amount of Curry Leaves: 3
         *
         */

        onUseWith(IntType.ITEM, CURRY_LEAF, UNCOOKED_STEW) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 60)) {
                sendDialogue(player, "You need an Cooking level of at least 60 to make that.")
                return@onUseWith true
            }

            val requiredAmount = 3
            val amount = amountInInventory(player, used.id)

            if (amount < requiredAmount) {
                sendMessage(player, "You need ${requiredAmount - amount} curry leaves to mix with the stew.")
                return@onUseWith true
            }

            if (removeItem(player, Item(used.id, requiredAmount), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, UNCOOKED_CURRY, 1, Container.INVENTORY)
                sendMessage(player, "You mix the curry leaves with the stew.")
            }

            return@onUseWith true
        }

    }

    companion object {
        private const val BOWL_OF_WATER = Items.BOWL_OF_WATER_1921
        private const val UNCOOKED_STEW = Items.UNCOOKED_STEW_2001
        private const val UNCOOKED_CURRY = Items.UNCOOKED_CURRY_2009
        private const val SPICE = Items.SPICE_2007
        private const val CURRY_LEAF = Items.CURRY_LEAF_5970
        private const val POTATO = Items.POTATO_1942
        private const val POTATO_STEW = Items.INCOMPLETE_STEW_1997
        private const val MEAT_STEW = Items.INCOMPLETE_STEW_1999
        private const val COOKED_MEAT = Items.COOKED_MEAT_2142
        private val stewIngredients = intArrayOf(Items.COOKED_MEAT_2142, Items.POTATO_1942)
        private val incompleteStew = intArrayOf(Items.INCOMPLETE_STEW_1997, Items.INCOMPLETE_STEW_1999)
    }
}