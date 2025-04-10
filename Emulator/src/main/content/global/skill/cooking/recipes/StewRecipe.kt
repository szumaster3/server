package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class StewRecipe : InteractionListener {

    private val incompleteStew = intArrayOf(Items.INCOMPLETE_STEW_1997, Items.INCOMPLETE_STEW_1999)

    override fun defineListeners() {

        /*
         * Handles creating incomplete stews.
         */

        onUseWith(IntType.ITEM, Items.BOWL_OF_WATER_1921, Items.POTATO_1942, Items.COOKED_MEAT_2142) { player, used, ingredient ->
            if (getStatLevel(player, Skills.COOKING) < 25) {
                sendMessage(player, "You need a Cooking level of 25 to make that.")
                return@onUseWith false
            }

            val stew = when (ingredient.id) {
                Items.POTATO_1942 -> Items.INCOMPLETE_STEW_1997
                Items.COOKED_MEAT_2142 -> Items.INCOMPLETE_STEW_1999
                else -> return@onUseWith false
            }

            fun makeIncompleteStew(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(ingredient.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, stew, 1, Container.INVENTORY)
                    sendMessage(player, "You cut up the ${ingredient.name.lowercase()} and put it into the bowl.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, ingredient.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeIncompleteStew()
            }

            sendSkillDialogue(player) {
                withItems(stew)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeIncompleteStew()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating an uncooked stew.
         */

        onUseWith(IntType.ITEM, incompleteStew, Items.COOKED_MEAT_2142, Items.POTATO_1942) { player, used, ingredient ->
            if (getStatLevel(player, Skills.COOKING) < 25) {
                sendMessage(player, "You need a Cooking level of 25 to make that.")
                return@onUseWith false
            }

            fun makeUncookedStew(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(ingredient.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.UNCOOKED_STEW_2001, 1, Container.INVENTORY)
                    sendMessage(
                        player,
                        "You cut up the ${
                            ingredient.name.lowercase().replace("cooked", "").trim()
                        } and put it into the stew."
                    )
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, ingredient.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeUncookedStew()
            }

            sendSkillDialogue(player) {
                withItems(Items.UNCOOKED_STEW_2001)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeUncookedStew()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating an uncooked curry using spice on stew.
         */

        onUseWith(IntType.ITEM, Items.SPICE_2007, Items.UNCOOKED_STEW_2001) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 60) {
                sendMessage(player, "You need a Cooking level of 60 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    addItem(player, Items.UNCOOKED_CURRY_2009, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the spice with the stew.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.UNCOOKED_CURRY_2009)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(
                                player,
                                Item(with.id, 1),
                                Container.INVENTORY
                            )
                        ) {
                            addItem(player, Items.UNCOOKED_CURRY_2009, 1, Container.INVENTORY)
                            sendMessage(player, "You mix the spice with the stew.")
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating an uncooked curry using curry leaves.
         */

        onUseWith(IntType.ITEM, Items.UNCOOKED_STEW_2001, Items.CURRY_LEAF_5970) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 60) {
                sendMessage(player, "You need a Cooking level of 60 to make that.")
                return@onUseWith false
            }

            val requiredCurryLeaves = 3

            if (amountInInventory(player, with.id) < requiredCurryLeaves) {
                sendMessage(player, "You need $requiredCurryLeaves curry leaves to mix with the stew.")
                return@onUseWith true
            }

            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                removeItem(player, Item(with.id, requiredCurryLeaves), Container.INVENTORY)) {
                addItem(player, Items.UNCOOKED_CURRY_2009, 1, Container.INVENTORY)
                sendMessage(player, "You mix the curry leaves with the stew.")
            }

            return@onUseWith true
        }

    }
}