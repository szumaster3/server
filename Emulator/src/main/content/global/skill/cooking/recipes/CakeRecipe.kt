package content.global.skill.cooking.recipes

import core.api.*
import core.api.item.allInInventory
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

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

        onUseWith(IntType.ITEM, Items.CAKE_TIN_1887, Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 40) {
                sendMessage(player, "You need a Cooking level of 40 to make that.")
                return@onUseWith false
            }

            if (anyInInventory(player, Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944) && !allInInventory(player, Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944)) {
                sendMessage(player, "You don't have the required items to make a cake.")
                return@onUseWith false
            }

            sendSkillDialogue(player) {
                withItems(Items.UNCOOKED_CAKE_1889)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) return@runTask
                        if (player.inventory.remove(Item(Items.POT_OF_FLOUR_1933, 1)) &&
                            player.inventory.remove(Item(Items.BUCKET_OF_MILK_1927, 1)) &&
                            player.inventory.remove(Item(Items.EGG_1944, 1)) &&
                            player.inventory.remove(Item(Items.CAKE_TIN_1887, 1))
                        ) {

                            addItem(player, Items.UNCOOKED_CAKE_1889, 1, Container.INVENTORY)
                            addItem(player, Items.BUCKET_1925, 1, Container.INVENTORY)
                            addItem(player, Items.EMPTY_POT_1931, 1, Container.INVENTORY)

                            sendMessage(player, "You mix the milk, flour, and egg together to make a raw cake mix.")
                        }
                    }
                    calculateMaxAmount { _ ->
                        min(amountInInventory(player, with.id), amountInInventory(player, used.id))
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

        onUseWith(IntType.ITEM, Items.CAKE_1891, Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_DUST_1975) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 50) {
                sendMessage(player, "You need a Cooking level of 50 to make that.")
                return@onUseWith false
            }
            if(removeItem(player, Item(used.id, 1)) && removeItem(player, Item(with.id, 1))) {
                addItem(player, Items.CHOCOLATE_CAKE_1897, 1, Container.INVENTORY)
                rewardXP(player, Skills.COOKING, 30.0)
                sendMessage(player, "You add chocolate to the cake.")
            }
            return@onUseWith true
        }
    }
}