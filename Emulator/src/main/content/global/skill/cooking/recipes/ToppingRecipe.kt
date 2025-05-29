package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

class ToppingRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating Chilli con carne (both variants).
         *
         * Requirements:
         *  - Cooking Level 9
         *  - Items: Spicy Sauce and Minced Meat.
         *  - Result: Chilli con Carne (25 XP)
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, spicyIngredients, SPICY_SAUCE) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 9)) {
                sendDialogue(player, "You need an Cooking level of at least 9 to make that.")
                return@onUseWith true
            }

            if (used.id == MINCED_MEAT && freeSlots(player) < 1) {
                sendMessage(player, "Not enough space in your inventory.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    if (used.id == MINCED_MEAT) {
                        addItemOrDrop(player, EMPTY_BOWL, 1)
                    }
                    rewardXP(player, Skills.COOKING, 25.0)
                    sendMessage(player, if (used.id == MINCED_MEAT) "You mix the ingredients to make the topping." else "You put the cut up meat into the bowl.")
                    addItem(player, CHILLI_CON_CARNE, 1, Container.INVENTORY)
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
                withItems(CHILLI_CON_CARNE)
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
         * Handles creating Tuna and Corn topping.
         *
         * Requirements:
         *  - Cooking Level 67
         *  - Items: Cooked Sweetcorn and Chopped Tuna.
         *  - Result: Tuna and Corn topping (204 XP).
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, CHOPPED_TUNA, COOKED_SWEETCORN) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 67)) {
                sendDialogue(player, "You need an Cooking level of at least 67 to make that.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    rewardXP(player, Skills.COOKING, 204.0)
                    addItem(player, TUNA_AND_CORN, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the ingredients to make the topping.")
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
                withItems(TUNA_AND_CORN)
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
         * Handles creating an Egg and tomato by combining a tomato with scrambled eggs.
         *
         * Requirements:
         *  - Egg and tomato: Level 23 Cooking, +50.0 XP
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, TOMATO, SCRAMBLED_EGG) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 23)) {
                sendDialogue(player, "You need an Cooking level of at least 23 to make that.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, EGG_AND_TOMATO, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 50.0)
                    sendMessage(player, "You mix the scrambled egg with the tomato.")
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
                withItems(EGG_AND_TOMATO)
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
         * Handles creating a Wrapped oomlie by combining a raw oomlie and a palm leaf.
         *
         * Requirements:
         *  - Wrapped oomlie: Level 50 Cooking, +10.0 XP
         *
         * Ticks: 1 (0.6 seconds)
         */

        onUseWith(IntType.ITEM, RAW_OOMLIE, PALM_LEAF) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 50)) {
                sendDialogue(player, "You need an Cooking level of at least 50 to make that.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, WRAPPED_OOMLIE, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 10.0)
                    sendMessage(player, "You wrap the raw oomlie in the palm leaf.")
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
                withItems(WRAPPED_OOMLIE)
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
         * Handles creating Mushroom & Onion by combining fried mushrooms and fried onions.
         *
         * Product:
         *  - Mushroom and Onion
         *
         * Required Cooking Level: 57
         *
         * Ticks: 2 (1.2 seconds)
         * Rewards: 120 Cooking XP
         */

        onUseWith(IntType.ITEM, FRIED_MUSHROOMS, FRIED_ONIONS) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 57)) {
                sendDialogue(player, "You need an Cooking level of at least 57 to make that.")
                return@onUseWith true
            }

            fun process(): Boolean {
                if (removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)) {
                    addItemOrDrop(player, EMPTY_BOWL, 1)
                    addItem(player, MUSHROOM_AND_ONION, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 120.0)
                    sendMessage(player, "You mix the fried onions and mushrooms.")
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
                withItems(MUSHROOM_AND_ONION)
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

    }

    companion object {
        private const val EMPTY_BOWL = Items.BOWL_1923
        private const val FRIED_MUSHROOMS = Items.FRIED_MUSHROOMS_7082
        private const val FRIED_ONIONS = Items.FRIED_ONIONS_7084
        private const val MUSHROOM_AND_ONION = Items.MUSHROOM_AND_ONION_7066
        private const val WRAPPED_OOMLIE = Items.WRAPPED_OOMLIE_2341
        private const val RAW_OOMLIE = Items.RAW_OOMLIE_2337
        private const val PALM_LEAF = Items.PALM_LEAF_2339

        private const val EGG_AND_TOMATO = Items.EGG_AND_TOMATO_7064
        private const val SCRAMBLED_EGG = Items.SCRAMBLED_EGG_7078
        private const val TOMATO = Items.TOMATO_1982

        private const val COOKED_SWEETCORN = Items.COOKED_SWEETCORN_5988
        private const val CHOPPED_TUNA = Items.CHOPPED_TUNA_7086
        private const val TUNA_AND_CORN = Items.TUNA_AND_CORN_7068

        private const val SPICY_SAUCE = Items.SPICY_SAUCE_7072
        private const val MINCED_MEAT = Items.MINCED_MEAT_7070
        private const val COOKED_MEAT = Items.COOKED_MEAT_2142
        private const val CHILLI_CON_CARNE = Items.CHILLI_CON_CARNE_7062

        private val spicyIngredients = intArrayOf(MINCED_MEAT, COOKED_MEAT)
    }
}