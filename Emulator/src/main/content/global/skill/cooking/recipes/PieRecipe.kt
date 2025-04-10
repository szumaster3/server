package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class PieRecipe : InteractionListener {

    private val firstPartPie = intArrayOf(
        Items.PART_MUD_PIE_7164,
        Items.PART_GARDEN_PIE_7172,
        Items.PART_FISH_PIE_7182,
        Items.PART_ADMIRAL_PIE_7192,
        Items.PART_WILD_PIE_7202,
        Items.PART_SUMMER_PIE_7212
    )
    private val secondPartPie = intArrayOf(
        Items.PART_MUD_PIE_7166,
        Items.PART_GARDEN_PIE_7174,
        Items.PART_WILD_PIE_7204,
        Items.PART_SUMMER_PIE_7214
    )

    override fun defineListeners() {

        /*
         * Handles creating a pie shell from pastry dough and a pie dish.
         */

        onUseWith(IntType.ITEM, Items.PASTRY_DOUGH_1953, Items.PIE_DISH_2313) { player, used, with ->
            val pieDish = with.asItem().slot
            if (removeItem(player, Item(used.id, 1))) {
                replaceSlot(player, pieDish, Item(Items.PIE_SHELL_2315, 1))
                sendMessage(player, "You put the pastry dough into the pie dish to make a pie shell.")
            }
            return@onUseWith true
        }

        /*
         * Handles combining a pie shell with ingredients to create first part pies.
         *
         * Requirements:
         * - Redberry pie:     Level 10 Cooking (using Items.REDBERRIES_1951)
         * - Meat pie:         Level 20 Cooking (using Items.COOKED_MEAT_2142 or Items.COOKED_CHICKEN_2140)
         * - Part mud pie      Level 29 Cooking (using Items.COMPOST_6032)
         * - Apple pie:        Level 30 Cooking (using Items.COOKING_APPLE_1955)
         * - Part garden pie:  Level 34 Cooking (using Items.TOMATO_1982)
         * - Part fish pie:    Level 47 Cooking (using Items.TROUT_333)
         * - Part admiral pie: Level 70 Cooking (using Items.SALMON_329)
         * - Part wild pie:    Level 85 Cooking (using Items.RAW_BEAR_MEAT_2136)
         * - Part summer pie   Level 95 Cooking (using Items.STRAWBERRY_5504)
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, Items.PIE_SHELL_2315, Items.REDBERRIES_1951, Items.COOKED_MEAT_2142, Items.COOKED_CHICKEN_2140, Items.COMPOST_6032, Items.COOKING_APPLE_1955, Items.TOMATO_1982, Items.TROUT_333, Items.SALMON_329, Items.RAW_BEAR_MEAT_2136, Items.STRAWBERRY_5504) { player, used, with ->
            val requirements = when (with.id) {
                Items.REDBERRIES_1951 -> 10
                Items.COOKED_MEAT_2142, Items.COOKED_CHICKEN_2140 -> 20
                Items.COMPOST_6032 -> 29
                Items.COOKING_APPLE_1955 -> 30
                Items.TOMATO_1982 -> 34
                Items.TROUT_333 -> 47
                Items.SALMON_329 -> 70
                Items.RAW_BEAR_MEAT_2136 -> 85
                Items.STRAWBERRY_5504 -> 95
                else -> return@onUseWith false
            }

            if (getStatLevel(player, Skills.COOKING) < requirements) {
                sendMessage(player, "You need a Cooking level of $requirements to make that.")
                return@onUseWith false
            }

            val products = when (with.id) {
                Items.REDBERRIES_1951 -> Items.UNCOOKED_BERRY_PIE_2321
                Items.COOKED_MEAT_2142, Items.COOKED_CHICKEN_2140 -> Items.UNCOOKED_MEAT_PIE_2319
                Items.COMPOST_6032 -> Items.PART_MUD_PIE_7164
                Items.COOKING_APPLE_1955 -> Items.UNCOOKED_APPLE_PIE_2317
                Items.TOMATO_1982 -> Items.PART_GARDEN_PIE_7172
                Items.TROUT_333 -> Items.PART_FISH_PIE_7182
                Items.SALMON_329 -> Items.PART_ADMIRAL_PIE_7192
                Items.RAW_BEAR_MEAT_2136 -> Items.PART_WILD_PIE_7202
                Items.STRAWBERRY_5504 -> Items.PART_SUMMER_PIE_7212
                else -> return@onUseWith false
            }

            val ingredient = with.name.lowercase()

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, products, 1, Container.INVENTORY)
                    sendMessage(player, "You fill the pie with $ingredient.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(products)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                            removeItem(player, Item(with.id, 1), Container.INVENTORY)
                        ) {
                            addItem(player, products, 1, Container.INVENTORY)
                            sendMessage(player, "You fill the pie with $ingredient.")
                        }
                    }
                }

                calculateMaxAmount {
                    min(amountInInventory(player, used.id), amountInInventory(player, with.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles combining a part pies with ingredients to create second part pies.
         *
         * Requirements:
         * - Part mud pie      Level 29 Cooking (using Items.BUCKET_OF_WATER_1929)
         * - Part garden pie:  Level 34 Cooking (using Items.ONION_1957)
         * - Part fish pie:    Level 47 Cooking (using Items.COD_339)
         * - Part admiral pie: Level 70 Cooking (using Items.TUNA_361)
         * - Part wild pie:    Level 85 Cooking (using Items.RAW_CHOMPY_2876)
         * - Part summer pie   Level 95 Cooking (using Items.WATERMELON_5982)
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, firstPartPie, Items.BUCKET_OF_WATER_1929, Items.ONION_1957, Items.COD_339, Items.TUNA_361, Items.RAW_CHOMPY_2876, Items.WATERMELON_5982) { player, used, with ->
            val requirements = when (used.id) {
                Items.PART_MUD_PIE_7164 -> 29
                Items.PART_GARDEN_PIE_7172 -> 34
                Items.PART_FISH_PIE_7182 -> 47
                Items.PART_ADMIRAL_PIE_7192 -> 70
                Items.PART_WILD_PIE_7202 -> 85
                Items.PART_SUMMER_PIE_7212 -> 95
                else -> return@onUseWith false
            }

            if (getStatLevel(player, Skills.COOKING) < requirements) {
                sendMessage(player, "You need a Cooking level of $requirements to make that.")
                return@onUseWith false
            }

            val product = when {
                used.id == Items.BUCKET_OF_WATER_1929 && with.id == Items.PART_MUD_PIE_7164 -> Items.PART_MUD_PIE_7166
                used.id == Items.ONION_1957 && with.id == Items.PART_GARDEN_PIE_7172 -> Items.PART_GARDEN_PIE_7174
                used.id == Items.COD_339 && with.id == Items.PART_FISH_PIE_7182 -> Items.PART_FISH_PIE_7184
                used.id == Items.TUNA_361 && with.id == Items.PART_ADMIRAL_PIE_7192 -> Items.PART_ADMIRAL_PIE_7194
                used.id == Items.RAW_CHOMPY_2876 && with.id == Items.PART_WILD_PIE_7202 -> Items.PART_WILD_PIE_7204
                used.id == Items.WATERMELON_5982 && with.id == Items.PART_SUMMER_PIE_7212 -> Items.PART_SUMMER_PIE_7214
                else -> return@onUseWith false
            }

            val ingredient = with.name.lowercase()

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    if (with.id == Items.BUCKET_OF_WATER_1929) {
                        addItem(player, Items.BUCKET_1925)
                    }
                    addItem(player, product, 1, Container.INVENTORY)
                    sendMessage(player, "You fill the pie with the $ingredient.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(product)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                            removeItem(player, Item(with.id, 1), Container.INVENTORY)
                        ) {
                            if (with.id == Items.BUCKET_OF_WATER_1929) {
                                addItem(player, Items.BUCKET_1925)
                            }
                            addItem(player, product, 1, Container.INVENTORY)
                            sendMessage(player, "You fill the pie with the $ingredient.")
                        }
                    }
                }

                calculateMaxAmount {
                    min(amountInInventory(player, used.id), amountInInventory(player, with.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles combining a second part pies with ingredients to create raw pies.
         *
         * Requirements:
         * - Raw mud pie      Level 29 Cooking (using Items.CLAY_434)
         * - Raw garden pie:  Level 34 Cooking (using Items.CABBAGE_1965 or Items.CABBAGE_1967)
         * - Raw wild pie:    Level 85 Cooking (using Items.RAW_RABBIT_3226)
         * - Raw summer pie   Level 95 Cooking (using Items.COOKING_APPLE_1955)
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, secondPartPie, Items.CLAY_434, Items.CABBAGE_1965 or Items.CABBAGE_1967, Items.RAW_RABBIT_3226, Items.COOKING_APPLE_1955) { player, used, with ->
            val requirements = when (used.id) {
                Items.PART_MUD_PIE_7166 -> 29
                Items.PART_GARDEN_PIE_7174 -> 34
                Items.PART_WILD_PIE_7204 -> 85
                Items.PART_SUMMER_PIE_7214 -> 95
                else -> return@onUseWith false
            }

            if (getStatLevel(player, Skills.COOKING) < requirements) {
                sendMessage(player, "You need a Cooking level of $requirements to make that.")
                return@onUseWith false
            }

            val product = when {
                used.id == Items.PART_MUD_PIE_7166 && with.id == Items.CLAY_434 -> Items.RAW_MUD_PIE_7168
                used.id == Items.PART_GARDEN_PIE_7174 && with.id == Items.CABBAGE_1965 || with.id == Items.CABBAGE_1967 -> Items.RAW_GARDEN_PIE_7176
                used.id == Items.PART_WILD_PIE_7204 && with.id == Items.RAW_RABBIT_3226 -> Items.RAW_WILD_PIE_7206
                used.id == Items.PART_SUMMER_PIE_7214 && with.id == Items.COOKING_APPLE_1955 -> Items.RAW_SUMMER_PIE_7216
                else -> return@onUseWith false
            }

            val productName = product.asItem().name.lowercase()

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, product, 1, Container.INVENTORY)
                    sendMessage(player, "You prepare an $productName.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(product)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                            removeItem(player, Item(with.id, 1), Container.INVENTORY)
                        ) {
                            addItem(player, product, 1, Container.INVENTORY)
                            sendMessage(player, "You prepare an $productName.")
                        }
                    }
                }

                calculateMaxAmount {
                    min(amountInInventory(player, used.id), amountInInventory(player, with.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating raw admiral pie.
         */

        onUseWith(IntType.ITEM, Items.PART_ADMIRAL_PIE_7194, Items.POTATO_1942) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 70) {
                sendMessage(player, "You need a Cooking level of 70 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    addItem(player, Items.RAW_ADMIRAL_PIE_7196, 1, Container.INVENTORY)
                    sendMessage(player, "You prepare a admiral pie.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.RAW_ADMIRAL_PIE_7196)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                            addItem(player, Items.RAW_ADMIRAL_PIE_7196, 1, Container.INVENTORY)
                            sendMessage(player, "You prepare a admiral pie.")
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
         * Handles creating a raw fish pie.
         */

        onUseWith(IntType.ITEM, Items.PART_FISH_PIE_7184, Items.POTATO_1942) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 47) {
                sendMessage(player, "You need a Cooking level of 47 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    addItem(player, Items.RAW_FISH_PIE_7186, 1, Container.INVENTORY)
                    sendMessage(player, "You prepare a fish pie.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.RAW_FISH_PIE_7186)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(
                                player,
                                Item(with.id, 1),
                                Container.INVENTORY
                            )
                        ) {
                            addItem(player, Items.RAW_FISH_PIE_7186, 1, Container.INVENTORY)
                            sendMessage(player, "You prepare a fish pie.")
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }
    }

}