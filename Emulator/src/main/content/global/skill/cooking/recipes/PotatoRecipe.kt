package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class PotatoRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles attempting to add cheese to a baked potato without butter.
         */

        onUseWith(IntType.ITEM, Items.POTATO_1943, Items.CHEESE_1985) { player, _, _ ->
            sendMessage(player, "You must add butter to the baked potato before adding toppings.")
            return@onUseWith true
        }

        /*
         * Handles creating a potato with butter by combining a Baked potato with a pat of butter.
         *
         * Requirements:
         *  - Potato with butter: Level 39 Cooking, +40.5 XP (using Items.PAT_OF_BUTTER_6697)
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, Items.BAKED_POTATO_6701, Items.PAT_OF_BUTTER_6697) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 39)) {
                sendMessage(player, "You need a Cooking level of 39 to make that.")
                return@onUseWith true
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    addItem(player, Items.POTATO_WITH_BUTTER_6703, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 40.5)
                    sendMessage(player, "You add the butter to the potato.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.POTATO_WITH_BUTTER_6703)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                            addItem(player, Items.POTATO_WITH_BUTTER_6703, 1, Container.INVENTORY)
                            rewardXP(player, Skills.CRAFTING, 40.5)
                            sendMessage(player, "You add the butter to the potato.")
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
         * Handles combining a potato with butter and additional ingredients to create specialty potatoes.
         *
         * Requirements:
         *  - Chilli Potato:        Level 41 Cooking, +10.0 XP (using Items.CHILLI_CON_CARNE_7062)
         *  - Egg Potato:           Level 51 Cooking, +10.0 XP (using Items.EGG_AND_TOMATO_7064)
         *  - Mushroom Potato:      Level 64 Cooking, +10.0 XP (using Items.MUSHROOM_AND_ONION_7066)
         *  - Potato with Cheese:   Level 47 Cooking, +10.0 XP (using Items.CHEESE_1985)
         *  - Tuna Potato:          Level 68 Cooking, +10.0 XP (using Items.TUNA_AND_CORN_7068)
         *
         * Ticks: 2 (1.2 seconds)
         */

        val potatoRecipes = mapOf(
            Items.CHILLI_CON_CARNE_7062 to Pair(Items.CHILLI_POTATO_7054, 41),
            Items.EGG_AND_TOMATO_7064 to Pair(Items.EGG_POTATO_7056, 51),
            Items.MUSHROOM_AND_ONION_7066 to Pair(Items.MUSHROOM_POTATO_7058, 64),
            Items.CHEESE_1985 to Pair(Items.POTATO_WITH_CHEESE_6705, 47),
            Items.TUNA_AND_CORN_7068 to Pair(Items.TUNA_POTATO_7060, 68)
        )

        potatoRecipes.forEach { (ingredient, product) ->
            val (product, requirements) = product

            onUseWith(IntType.ITEM, Items.POTATO_WITH_BUTTER_6703, ingredient) { player, used, with ->
                if (!hasLevelDyn(player, Skills.COOKING, requirements)) {
                    sendMessage(player, "You need a Cooking level of $requirements to make that.")
                    return@onUseWith true
                }

                fun makeDish(): Boolean {
                    if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                        removeItem(player, Item(with.id, 1), Container.INVENTORY)
                    ) {
                        if (ingredient != Items.CHEESE_1985) {
                            addItem(player, Items.BOWL_1923, 1, Container.INVENTORY)
                        }
                        addItem(player, product, 1, Container.INVENTORY)
                        rewardXP(player, Skills.COOKING, 10.0)
                        val item = getItemName(with.id).lowercase()
                        sendMessage(player, "You add the $item to the potato.")
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
}