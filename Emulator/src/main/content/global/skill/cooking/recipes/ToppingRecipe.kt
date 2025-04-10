package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

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

        onUseWith(IntType.ITEM, Items.SPICY_SAUCE_7072, Items.MINCED_MEAT_7070, Items.COOKED_MEAT_2142) { player, used, ingredient ->
            if (!hasLevelDyn(player, Skills.COOKING, 9)) {
                sendMessage(player, "You need a Cooking level of 9 to make that.")
                return@onUseWith true
            }

            if (ingredient.id == Items.MINCED_MEAT_7070 && freeSlots(player) < 1) {
                sendMessage(player, "Not enough space in your inventory.")
                return@onUseWith false
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(ingredient.id, 1), Container.INVENTORY)
                ) {
                    if (ingredient.id == Items.MINCED_MEAT_7070) {
                        addItem(player, Items.BOWL_1923, 1)
                    }
                    rewardXP(player, Skills.COOKING, 25.0)
                    sendMessage(
                        player,
                        if (ingredient.id == Items.MINCED_MEAT_7070) "You mix the ingredients to make the topping." else "You put the cut up meat into the bowl."
                    )
                    addItem(player, Items.CHILLI_CON_CARNE_7062, 1, Container.INVENTORY)
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, ingredient.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeDish()
            }

            sendSkillDialogue(player) {
                withItems(Items.CHILLI_CON_CARNE_7062)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeDish()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
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

        onUseWith(IntType.ITEM, Items.COOKED_SWEETCORN_5988, Items.CHOPPED_TUNA_7086) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 67)) {
                sendMessage(player, "You need a Cooking level of 67 to make that.")
                return@onUseWith true
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    rewardXP(player, Skills.COOKING, 204.0)
                    addItem(player, Items.TUNA_AND_CORN_7068, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the ingredients to make the topping.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeDish()
            }

            sendSkillDialogue(player) {
                withItems(Items.TUNA_AND_CORN_7068)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeDish()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
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

        onUseWith(IntType.ITEM, Items.SCRAMBLED_EGG_7078, Items.TOMATO_1982) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 23)) {
                sendMessage(player, "You need a Cooking level of 23 to make that.")
                return@onUseWith true
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.EGG_AND_TOMATO_7064, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 50.0)
                    sendMessage(player, "You mix the scrambled egg with the tomato.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeDish()
            }

            sendSkillDialogue(player) {
                withItems(Items.EGG_AND_TOMATO_7064)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeDish()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
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

        onUseWith(IntType.ITEM, Items.RAW_OOMLIE_2337, Items.PALM_LEAF_2339) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 50)) {
                sendMessage(player, "You need a Cooking level of 50 to make that.")
                return@onUseWith true
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.WRAPPED_OOMLIE_2341, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 10.0)
                    sendMessage(player, "You wrap the raw oomlie in the palm leaf.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeDish()
            }

            sendSkillDialogue(player) {
                withItems(Items.WRAPPED_OOMLIE_2341)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeDish()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating Mushroom & onion.
         */

        onUseWith(IntType.ITEM, Items.FRIED_MUSHROOMS_7082, Items.FRIED_ONIONS_7084) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 57)) {
                sendMessage(player, "You need a Cooking level of 57 to make that.")
                return@onUseWith true
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.BOWL_1923, 1, Container.INVENTORY)
                    addItem(player, Items.MUSHROOM_AND_ONION_7066, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 120.0)
                    sendMessage(player, "You mix the fried onions and mushrooms.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeDish()
            }

            sendSkillDialogue(player) {
                withItems(Items.MUSHROOM_AND_ONION_7066)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeDish()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

    }
}