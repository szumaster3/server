package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Handles chopping-related cooking recipes.
 */
class ChoppingRecipePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cutting ingredients with a knife.
         */

        onUseWith(IntType.ITEM, CUTTING_INGREDIENTS, KNIFE) { player, used, _ ->
            val productID = when (used.id) {
                CALQUAT_FRUIT -> CALQUAT_KEG
                BANANA -> SLICED_BANANA
                else -> CHOCOLATE_DUST
            }
            val animation = when (used.id) {
                CALQUAT_FRUIT -> CALQUAT_CARVED_ANIMATION
                BANANA -> BANANA_SLICE_ANIMATION
                else -> CHOCOLATE_CUT_ANIMATION
            }

            queueScript(player, 1, QueueStrength.NORMAL) {
                if (amountInInventory(player, used.id) <= 0) {
                    return@queueScript stopExecuting(player)
                }
                if (!removeItem(player, used.asItem())) {
                    return@queueScript stopExecuting(player)
                }

                animate(player, animation)
                addItem(player, productID, 1, Container.INVENTORY)
                return@queueScript delayScript(player, 3)
            }

            return@onUseWith true
        }

        /*
         * Handles chopping ingredients into a bowl using a knife.
         */

        onUseWith(IntType.ITEM, CHOPPING_INGREDIENTS, EMPTY_BOWL) { player, used, with ->
            if (!inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the ${used.name.lowercase()}.")
                return@onUseWith true
            }

            val (productID, message) = when (used.id) {
                Items.TUNA_361 -> Items.CHOPPED_TUNA_7086 to "You chop the tuna into the bowl."
                Items.ONION_1957 -> Items.CHOPPED_ONION_1871 to "You chop the onion into small pieces."
                Items.GARLIC_1550 -> Items.CHOPPED_GARLIC_7074 to "You chop the garlic into the bowl."
                Items.TOMATO_1982 -> Items.CHOPPED_TOMATO_1869 to "You chop the tomato into the bowl."
                Items.UGTHANKI_MEAT_1861 -> Items.CHOPPED_UGTHANKI_1873 to "You chop the meat into the bowl."
                Items.MUSHROOM_6004 -> Items.SLICED_MUSHROOMS_7080 to "You slice the mushrooms."
                Items.COOKED_MEAT_2142 -> Items.MINCED_MEAT_7070 to "You chop the meat into the bowl."
                else -> return@onUseWith true
            }

            fun process(): Boolean {
                if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                    sendMessage(player, "You don't have the required ingredients.")
                    return false
                }
                animate(player, Animations.CUT_THING_WITH_KNIFE_IN_HAND_5756)
                addItem(player, productID, 1, Container.INVENTORY)
                sendMessage(player, message)
                return true
            }

            val baseAmount = amountInInventory(player, used.id)
            val withAmount = amountInInventory(player, with.id)

            if (baseAmount == 1 || withAmount == 1) {
                process()
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(productID)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) process()
                    }
                }
                calculateMaxAmount { minOf(baseAmount, withAmount) }
            }

            return@onUseWith true
        }

        /*
         * Handles creating uncooked egg.
         */

        onUseWith(IntType.ITEM, EGG, EMPTY_BOWL) { player, used, with ->
            if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                sendMessage(player, "You don't have the required ingredients.")
                return@onUseWith true
            }
            sendMessage(player, "You prepare an uncooked egg.")
            addItem(player, Items.UNCOOKED_EGG_7076)
            return@onUseWith true
        }

        /*
         * Handles creating spicy sauce from a chopped garlic and gnome spice.
         */

        onUseWith(IntType.ITEM, GNOME_SPICE, CHOPPED_GARLIC) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 9)) {
                sendDialogue(player, "You need an Cooking level of at least 9 to make that.")
                return@onUseWith true
            }
            fun process(): Boolean {
                if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                    sendMessage(player, "You don't have the required ingredients.")
                    return false
                }
                rewardXP(player, Skills.COOKING, 25.0)
                addItem(player, SPICY_SAUCE, 1, Container.INVENTORY)
                sendMessage(player, "You mix the ingredients to make spicy sauce.")
                return true
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                process()
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(SPICY_SAUCE)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) process()
                    }
                }
                calculateMaxAmount {
                    minOf(amountUsed, amountWith)
                }
            }

            return@onUseWith true
        }
    }

    companion object {
        private val CUTTING_INGREDIENTS = intArrayOf(
            Items.CALQUAT_FRUIT_5980,
            Items.CHOCOLATE_BAR_1973,
            Items.BANANA_1963
        )
        private val CHOPPING_INGREDIENTS = intArrayOf(
            Items.TUNA_361,
            Items.ONION_1957,
            Items.GARLIC_1550,
            Items.TOMATO_1982,
            Items.UGTHANKI_MEAT_1861,
            Items.MUSHROOM_6004,
            Items.COOKED_MEAT_2142
        )
        private const val CALQUAT_FRUIT             = Items.CALQUAT_FRUIT_5980
        private const val CALQUAT_KEG               = Items.CALQUAT_KEG_5769
        private const val BANANA                    = Items.BANANA_1963
        private const val SLICED_BANANA             = Items.SLICED_BANANA_3162
        private const val CHOCOLATE_BAR             = Items.CHOCOLATE_BAR_1973
        private const val CHOCOLATE_DUST            = Items.CHOCOLATE_DUST_1975
        private const val CHOPPED_GARLIC            = Items.CHOPPED_GARLIC_7074
        private const val GNOME_SPICE               = Items.GNOME_SPICE_2169
        private const val SPICY_SAUCE               = Items.SPICY_SAUCE_7072
        private const val EMPTY_BOWL                = Items.BOWL_1923
        private const val EGG                       = Items.EGG_1944
        private const val KNIFE                     = Items.KNIFE_946
        private const val CHOCOLATE_CUT_ANIMATION   = Animations.CUTTING_CHOCOLATE_BAR_1989
        private const val CALQUAT_CARVED_ANIMATION  = Animations.CARVE_CALQUAT_KEG_2290
        private const val BANANA_SLICE_ANIMATION    = Animations.HUMAN_FRUIT_CUTTING_1192
    }
}