package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class KebabRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating a super kebab.
         */

        onUseWith(IntType.ITEM, Items.RED_HOT_SAUCE_4610, Items.KEBAB_1971, Items.UGTHANKI_KEBAB_1883, Items.UGTHANKI_KEBAB_1885) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItemOrDrop(player, Items.SUPER_KEBAB_4608)
            }
            return@onUseWith true
        }

        /*
         * Handles creating an Ugthanki kebab by combining a pitta bread with Kebab mix.
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, Items.PITTA_BREAD_1865, Items.KEBAB_MIX_1881) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 40) {
                sendMessage(player, "You need a Cooking level of 40 to make that.")
                return@onUseWith false
            }

            fun makeKebab(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.BOWL_1923, 1, Container.INVENTORY)
                    addItem(player, Items.UGTHANKI_KEBAB_1885, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 40.0)
                    sendMessage(player, "You mix the ingredients to make ugthanki kebab.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeKebab()
            }

            sendSkillDialogue(player) {
                withItems(Items.UGTHANKI_KEBAB_1885)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeKebab()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating Onion & tomato.
         */

        onUseWith(IntType.ITEM, Items.CHOPPED_ONION_1871, Items.TOMATO_1982) { player, used, with ->
            if (!inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the tomato.")
                return@onUseWith false
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.ONION_AND_TOMATO_1875, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 1.0)
                    sendMessage(player, "You added the tomato into the chopped onion.")
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
                withItems(Items.ONION_AND_TOMATO_1875)
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
         * Handles creating a Kebab mix by combining an Onion & tomato and Ugthanki meat.
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, Items.ONION_AND_TOMATO_1875, Items.UGTHANKI_MEAT_1861) { player, used, with ->
            if (!inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the meat.")
                return@onUseWith false
            }

            fun mixMeat(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.KEBAB_MIX_1881, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 1.0)
                    sendMessage(player, "You mix the meat with onion and tomato.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith mixMeat()
            }

            sendSkillDialogue(player) {
                withItems(Items.KEBAB_MIX_1881)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) mixMeat()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }


        /*
         * Handles creating an Ugthanki & Onion / Ugthanki & Tomato.
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, Items.UGTHANKI_MEAT_1861, Items.ONION_1957, Items.TOMATO_1982) { player, used, with ->
            val product = if(with.id == Items.ONION_1957) Items.UGTHANKI_AND_ONION_1877 else Items.UGTHANKI_AND_TOMATO_1879

            if (!inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the ${with.name.lowercase()}.")
                return@onUseWith false
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, product, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 1.0)
                    sendMessage(player, "You mix the ${used.name.lowercase()} with ${with.name.lowercase()}.")
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
                withItems(product)
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