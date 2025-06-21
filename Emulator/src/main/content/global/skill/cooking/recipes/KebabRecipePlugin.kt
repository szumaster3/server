package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import org.rs.consts.Items

/**
 * Handles kebab-related cooking recipes.
 */
class KebabRecipePlugin : InteractionListener {
    override fun defineListeners() {
        registerRecipe(CHOPPED_ONION, TOMATO, ONION_AND_TOMATO)
        registerRecipe(CHOPPED_TOMATO, ONION, ONION_AND_TOMATO)
        registerRecipe(ONION_AND_TOMATO, UGTHANKI_MEAT, KEBAB_MIX)
        registerRecipe(CHOPPED_ONION, UGTHANKI_MEAT, UGTHANKI_AND_ONION)

        /*
         * Handles creating a super kebab.
         */

        onUseWith(IntType.ITEM, KEBAB_IDS, RED_HOT_SAUCE) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, SUPER_KEBAB, 1)
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, PITTA_BREAD, KEBAB_MIX) { player, used, with ->
            fun process(): Boolean {
                if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                    sendMessage(player, "You don't have the required ingredients.")
                    return false
                }

                addItem(player, BOWL, 1, Container.INVENTORY)
                sendMessage(player, "You mix the ingredients to make ugthanki kebab.")

                if (RandomFunction.roll(50)) {
                    addItem(player, UGTHANKI_KEBAB_SMELL, 1, Container.INVENTORY)
                } else {
                    rewardXP(player, Skills.COOKING, 40.0)
                    addItem(player, UGTHANKI_KEBAB, 1, Container.INVENTORY)
                }
                return true
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)
            if (amountUsed == 1 || amountWith == 1) {
                process()
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(UGTHANKI_KEBAB)
                create { _, amount -> runTask(player, 2, amount) { process() } }
                calculateMaxAmount { minOf(amountUsed, amountWith) }
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, UGTHANKI_IDS, CHOPPED_UGTHANKI) { player, used, with ->
            val productID = if (used.id == ONION) UGTHANKI_AND_ONION else UGTHANKI_AND_TOMATO

            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to slice up the ${used.name.lowercase()}.")
                return@onUseWith true
            }

            fun process(): Boolean {
                if (!removeItem(player, used.asItem(), Container.INVENTORY) ||
                    !removeItem(player, with.asItem(), Container.INVENTORY)
                ) {
                    sendMessage(player, "You don't have the required ingredients.")
                    return false
                }

                addItem(player, productID, 1, Container.INVENTORY)
                sendMessage(player, "You mix the ${used.name.lowercase()} with the ${with.name.lowercase()}.")
                return true
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                process()
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(productID)
                create { _, amount -> runTask(player, 2, amount) { process() } }
                calculateMaxAmount { minOf(amountUsed, amountWith) }
            }
            return@onUseWith true
        }
    }

    private fun registerRecipe(baseID: Int, secondID: Int, productID: Int) {
        onUseWith(IntType.ITEM, baseID, secondID) { player, used, with ->
            if (!inInventory(player, KNIFE)) {
                val requiredItem = getItemName(if (used.id == baseID) baseID else secondID).lowercase()
                sendMessage(player, "You need a knife to slice up the $requiredItem.")
                return@onUseWith true
            }

            fun process(): Boolean {
                if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                    sendMessage(player, "You don't have the required ingredients.")
                    return false
                }

                val usedName = used.name.lowercase()
                val usedWith = with.name.lowercase()

                sendMessage(player, "You mix the $usedName with the $usedWith.")
                addItem(player, productID, 1)
                return true
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, with.id)

            if (amountUsed == 1 || amountWith == 1) {
                process()
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(productID)
                create { _, amount -> runTask(player, 2, amount) { process() } }
                calculateMaxAmount { minOf(amountUsed, amountWith) }
            }
            return@onUseWith true
        }
    }

    companion object {
        private const val UGTHANKI_MEAT        = Items.UGTHANKI_MEAT_1861
        private const val CHOPPED_ONION        = Items.CHOPPED_ONION_1871
        private const val CHOPPED_TOMATO       = Items.CHOPPED_TOMATO_1869
        private const val TOMATO               = Items.TOMATO_1982
        private const val KNIFE                = Items.KNIFE_946
        private const val ONION                = Items.ONION_1957
        private const val ONION_AND_TOMATO     = Items.ONION_AND_TOMATO_1875
        private const val PITTA_BREAD          = Items.PITTA_BREAD_1865
        private const val KEBAB_MIX            = Items.KEBAB_MIX_1881
        private const val UGTHANKI_KEBAB       = Items.UGTHANKI_KEBAB_1883
        private const val UGTHANKI_KEBAB_SMELL = Items.UGTHANKI_KEBAB_1885
        private const val UGTHANKI_AND_ONION   = Items.UGTHANKI_AND_ONION_1877
        private const val UGTHANKI_AND_TOMATO  = Items.UGTHANKI_AND_TOMATO_1879
        private const val BOWL                 = Items.BOWL_1923
        private const val CHOPPED_UGTHANKI     = Items.CHOPPED_UGTHANKI_1873
        private const val RED_HOT_SAUCE        = Items.RED_HOT_SAUCE_4610
        private const val SUPER_KEBAB          = Items.SUPER_KEBAB_4608
        private val UGTHANKI_IDS               = intArrayOf(ONION,TOMATO)
        private val KEBAB_IDS                  = intArrayOf(Items.KEBAB_1971, Items.UGTHANKI_KEBAB_1883, Items.UGTHANKI_KEBAB_1885)
    }
}