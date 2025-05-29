package content.global.skill.cooking.recipes

import core.api.*
import core.api.item.allInInventory
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class CakeRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating an Uncooked Cake by combining flour, milk, egg, and a cake tin.
         *
         * Requirements:
         *  - Level 40 Cooking
         *  - Items: Pot of flour, Bucket of milk, Egg, Cake tin
         *  - Produces: Uncooked cake, Empty bucket, and Empty pot
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, cakeIngredients, CAKE_TIN) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 40)) {
                sendDialogue(player, "You need an Cooking level of at least 40 to make that.")
                return@onUseWith true
            }

            if (anyInInventory(player, *cakeIngredients) && !allInInventory(player, *cakeIngredients)) {
                sendMessage(player, "You don't have the required items to make a cake.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(UNCOOKED_CAKE)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) return@runTask
                        if (player.inventory.remove(Item(POT_OF_FLOUR, 1)) &&
                            player.inventory.remove(Item(BUCKET_OF_MILK, 1)) &&
                            player.inventory.remove(Item(EGG, 1)) &&
                            player.inventory.remove(Item(CAKE_TIN, 1))
                        ) {

                            addItem(player, UNCOOKED_CAKE, 1, Container.INVENTORY)
                            addItemOrDrop(player, EMPTY_BUCKET, 1)
                            addItemOrDrop(player, EMPTY_POT, 1)

                            sendMessage(player, "You mix the milk, flour, and egg together to make a raw cake mix.")
                        }
                    }
                    calculateMaxAmount { _ ->
                        minOf(amountInInventory(player, with.id), amountInInventory(player, used.id))
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles creating a Chocolate Cake by combining a Cake with either a Chocolate bar or Chocolate dust.
         *
         * Requirements:
         *  - Level 50 Cooking
         *  - Items: Cake + (Chocolate bar or Chocolate dust)
         *  - Produces: Chocolate Cake
         *  - XP Gained: 30.0 Cooking XP
         */

        onUseWith(IntType.ITEM, chocolateIngredients, CAKE) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 50)) {
                sendDialogue(player, "You need an Cooking level of at least 50 to make that.")
                return@onUseWith true
            }
            val success = removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)
            if (success) {
                addItem(player, CHOCOLATE_CAKE, 1, Container.INVENTORY)
                rewardXP(player, Skills.COOKING, 30.0)
                sendMessage(player, "You add chocolate to the cake.")
            }
            return@onUseWith true
        }
    }

    companion object {
        private const val POT_OF_FLOUR = Items.POT_OF_FLOUR_1933
        private const val EGG = Items.EGG_1944
        private const val BUCKET_OF_MILK = Items.BUCKET_OF_MILK_1927
        private const val EMPTY_BUCKET = Items.BUCKET_1925
        private const val EMPTY_POT = Items.EMPTY_POT_1931
        private const val UNCOOKED_CAKE = Items.UNCOOKED_CAKE_1889
        private const val CAKE_TIN = Items.CAKE_TIN_1887
        private const val CAKE = Items.CAKE_1891
        private const val CHOCOLATE_CAKE = Items.CHOCOLATE_CAKE_1897
        private val chocolateIngredients = intArrayOf(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_DUST_1975)
        private val cakeIngredients = intArrayOf(Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944)

    }
}