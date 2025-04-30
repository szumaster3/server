package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items

class KebabRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating a super kebab.
         */

        onUseWith(IntType.ITEM, kebabIDs, RED_HOT_SAUCE) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItemOrDrop(player, SUPER_KEBAB)
            }
            return@onUseWith true
        }

        /*
         * Handles creating an Ugthanki kebab by combining a pitta bread with Kebab mix.
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, PITTA_BREAD, KEBAB_MIX) { player, used, with ->
            fun process(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItemOrDrop(player, BOWL, 1)

                    // TODO find correct values.
                    if (RandomFunction.roll(50)) {
                        addItem(player, UGTHANKI_KEBAB_SMELL, 1, Container.INVENTORY)
                    } else {
                        addItem(player, UGTHANKI_KEBAB, 1, Container.INVENTORY)
                        rewardXP(player, Skills.COOKING, 40.0)
                    }

                    sendMessage(player, "You mix the ingredients to make ugthanki kebab.")
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
                withItems(UGTHANKI_KEBAB)
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
         * Handles creating Onion & tomato.
         */

        onUseWith(IntType.ITEM, CHOPPED_ONION, TOMATO) { player, used, with ->
            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to slice up the tomato.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, ONION_AND_TOMATO, 1, Container.INVENTORY)
                    sendMessage(player, "You added the tomato into the chopped onion.")
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
                withItems(ONION_AND_TOMATO)
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
         * Handles creating Onion & tomato.
         */

        onUseWith(IntType.ITEM, CHOPPED_TOMATO, ONION) { player, used, with ->
            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to slice up the onion.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, ONION_AND_TOMATO, 1, Container.INVENTORY)
                    sendMessage(player, "You added the onion into the chopped tomato.")
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
                withItems(ONION_AND_TOMATO)
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
         * Handles creating a Kebab mix by add Onion & tomato with Ugthanki meat.
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, ONION_AND_TOMATO, UGTHANKI_MEAT) { player, used, with ->
            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to slice up the meat.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, KEBAB_MIX, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the meat with onion and tomato.")
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
                withItems(KEBAB_MIX)
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
         * Handles creating an Ugthanki & onion.
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, CHOPPED_ONION, UGTHANKI_MEAT) { player, used, with ->
            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to slice up the meat.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, UGTHANKI_AND_ONION, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the meat with onion and tomato.")
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
                withItems(UGTHANKI_AND_ONION)
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
         * Handles creating an Ugthanki & Onion / Ugthanki & Tomato.
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, ugthankiIngredients, CHOPPED_UGTHANKI) { player, used, with ->
            val productID = if(used.id == ONION) UGTHANKI_AND_ONION else UGTHANKI_AND_TOMATO
            val ingredientName = used.name.lowercase()
            val secondIngredientName = with.name.lowercase()

            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to slice up the $ingredientName.")
                return@onUseWith true
            }

            fun process(): Boolean {
                val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
                if (success) {
                    addItem(player, productID, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the $ingredientName with $secondIngredientName.")
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
                withItems(productID)
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
        private const val UGTHANKI_MEAT = Items.UGTHANKI_MEAT_1861
        private const val CHOPPED_ONION = Items.CHOPPED_ONION_1871
        private const val CHOPPED_TOMATO = Items.CHOPPED_TOMATO_1869
        private const val TOMATO = Items.TOMATO_1982
        private const val KNIFE = Items.KNIFE_946
        private const val ONION = Items.ONION_1957
        private const val ONION_AND_TOMATO = Items.ONION_AND_TOMATO_1875
        private const val PITTA_BREAD = Items.PITTA_BREAD_1865
        private const val KEBAB_MIX = Items.KEBAB_MIX_1881
        private const val UGTHANKI_KEBAB = Items.UGTHANKI_KEBAB_1883
        private const val UGTHANKI_KEBAB_SMELL = Items.UGTHANKI_KEBAB_1885
        private const val UGTHANKI_AND_ONION = Items.UGTHANKI_AND_ONION_1877
        private const val UGTHANKI_AND_TOMATO = Items.UGTHANKI_AND_TOMATO_1879
        private const val BOWL = Items.BOWL_1923
        private const val CHOPPED_UGTHANKI = Items.CHOPPED_UGTHANKI_1873
        private const val RED_HOT_SAUCE = Items.RED_HOT_SAUCE_4610
        private const val SUPER_KEBAB = Items.SUPER_KEBAB_4608
        private val ugthankiIngredients = intArrayOf(ONION,TOMATO)
        private val kebabIDs = intArrayOf(Items.KEBAB_1971, Items.UGTHANKI_KEBAB_1883, Items.UGTHANKI_KEBAB_1885)
    }
}