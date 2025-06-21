package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import kotlin.math.min

/**
 * Handles recipes for adding toppings to food items.
 */
class ToppingRecipePlugin : InteractionListener {

    override fun defineListeners() {
        registerRecipe(SPICY_SAUCE, MINCED_MEAT, 9, 25.0, CHILLI_CON_CARNE, "You mix the ingredients to make the topping.")
        registerRecipe(SPICY_SAUCE, COOKED_MEAT, 9, 25.0, CHILLI_CON_CARNE, "You put the cut up meat into the bowl.")
        registerRecipe(CHOPPED_TUNA, COOKED_SWEETCORN, 67, 204.0, TUNA_AND_CORN, "You mix the ingredients to make the topping.")
        registerRecipe(SCRAMBLED_EGG, TOMATO, 23, 50.0, EGG_AND_TOMATO, "You mix the scrambled egg with the tomato.")
        registerRecipe(RAW_OOMLIE, PALM_LEAF, 50, 10.0, WRAPPED_OOMLIE, "You wrap the raw oomlie in the palm leaf.")
        registerRecipe(FRIED_MUSHROOMS, FRIED_ONIONS, 57, 120.0, MUSHROOM_AND_ONION, "You mix the fried onions and mushrooms.")
    }

    private fun registerRecipe(baseID: Int, secondID: Int, requiredLevel: Int, xp: Double, productID: Int, message: String) {
        onUseWith(IntType.ITEM, baseID, secondID) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, requiredLevel)) {
                sendDialogue(player, "You need a Cooking level of at least $requiredLevel to make that.")
                return@onUseWith true
            }

            if (productID == CHILLI_CON_CARNE && secondID == COOKED_MEAT && !inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the meat.")
                return@onUseWith true
            }

            fun process(): Boolean {
                if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                    sendMessage(player, "You don't have the required ingredients to make that.")
                    return false
                }
                if (productID == MUSHROOM_AND_ONION || productID == CHILLI_CON_CARNE) {
                    addItemOrDrop(player, EMPTY_BOWL, 1)
                }
                rewardXP(player, Skills.COOKING, xp)
                sendMessage(player, message)
                addItem(player, productID, 1, Container.INVENTORY)
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
                calculateMaxAmount { min(baseAmount, withAmount) }
            }

            return@onUseWith true
        }
    }

    companion object {
        private const val EMPTY_BOWL         = Items.BOWL_1923
        private const val FRIED_MUSHROOMS    = Items.FRIED_MUSHROOMS_7082
        private const val FRIED_ONIONS       = Items.FRIED_ONIONS_7084
        private const val MUSHROOM_AND_ONION = Items.MUSHROOM_AND_ONION_7066
        private const val WRAPPED_OOMLIE     = Items.WRAPPED_OOMLIE_2341
        private const val RAW_OOMLIE         = Items.RAW_OOMLIE_2337
        private const val PALM_LEAF          = Items.PALM_LEAF_2339
        private const val EGG_AND_TOMATO     = Items.EGG_AND_TOMATO_7064
        private const val SCRAMBLED_EGG      = Items.SCRAMBLED_EGG_7078
        private const val TOMATO             = Items.TOMATO_1982
        private const val COOKED_SWEETCORN   = Items.COOKED_SWEETCORN_5988
        private const val CHOPPED_TUNA       = Items.CHOPPED_TUNA_7086
        private const val TUNA_AND_CORN      = Items.TUNA_AND_CORN_7068
        private const val SPICY_SAUCE        = Items.SPICY_SAUCE_7072
        private const val MINCED_MEAT        = Items.MINCED_MEAT_7070
        private const val COOKED_MEAT        = Items.COOKED_MEAT_2142
        private const val CHILLI_CON_CARNE   = Items.CHILLI_CON_CARNE_7062
    }

}
