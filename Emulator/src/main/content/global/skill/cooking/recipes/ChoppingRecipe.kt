package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import kotlin.math.min

class ChoppingRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cutting ingredients with a knife.
         *
         * Products:
         *  - Calquat Keg
         *  - Chocolate Dust
         *
         * Ticks: 4 (2.4 seconds)
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.CALQUAT_FRUIT_5980, Items.CHOCOLATE_BAR_1973) { player, _, items ->
            val (base, product, animation) = when (items.id) {
                Items.CALQUAT_FRUIT_5980 -> Triple(Items.CALQUAT_FRUIT_5980, Items.CALQUAT_KEG_5769, Animations.CARVE_CALQUAT_KEG_2290)
                Items.CHOCOLATE_BAR_1973 -> Triple(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_DUST_1975, Animations.CUTTING_CHOCOLATE_BAR_1989)
                else -> return@onUseWith false
            }

            player.pulseManager.run(object : Pulse(1) {
                override fun pulse(): Boolean {
                    super.setDelay(4)
                    val amount = amountInInventory(player, base)
                    if (amount > 0) {
                        if (removeItem(player, Item(base, 1), Container.INVENTORY)) {
                            animate(player, animation)
                            addItem(player, product, 1, Container.INVENTORY)
                        }
                    }
                    return amount <= 0
                }
            })

            return@onUseWith true
        }

        /*
         * Handles chopping ingredients into a bowl using a knife.
         *
         * Products:
         *  - Chopped Tuna
         *  - Chopped Onion
         *  - Chopped Garlic
         *  - Chopped Tomato
         *  - Chopped Ugthanki
         *  - Sliced Mushrooms
         *  - Minced Meat
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, Items.BOWL_1923, Items.TUNA_361, Items.ONION_1957, Items.GARLIC_1550, Items.TOMATO_1982, Items.UGTHANKI_MEAT_1861, Items.MUSHROOM_6004, Items.COOKED_MEAT_2142) { player, used, ingredients ->
            if (!inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the ${ingredients.name.lowercase()}.")
                return@onUseWith false
            }

            val (product, message) = when (ingredients.id) {
                Items.TUNA_361 -> Items.CHOPPED_TUNA_7086 to "You chop the tuna into the bowl."
                Items.ONION_1957 -> Items.CHOPPED_ONION_1871 to "You chop the onion into small pieces."
                Items.GARLIC_1550 -> Items.CHOPPED_GARLIC_7074 to "You chop the garlic into the bowl."
                Items.TOMATO_1982 -> Items.CHOPPED_TOMATO_1869 to "You chop the tomato into the bowl."
                Items.UGTHANKI_MEAT_1861 -> Items.CHOPPED_UGTHANKI_1873 to "You chop the meat into the bowl."
                Items.MUSHROOM_6004 -> Items.SLICED_MUSHROOMS_7080 to "You slice the mushrooms."
                Items.COOKED_MEAT_2142 -> Items.MINCED_MEAT_7070 to "You chop the meat into the bowl."
                else -> return@onUseWith false
            }

            player.pulseManager.run(object : Pulse(1) {
                override fun pulse(): Boolean {
                    super.setDelay(2)
                    val amount = amountInInventory(player, ingredients.id)
                    if (amount > 0) {
                        if (removeItem(player, Item(ingredients.id, 1), Container.INVENTORY) && removeItem(player, Item(used.id, 1), Container.INVENTORY)) {
                            animate(player, Animations.CUT_THING_WITH_KNIFE_IN_HAND_5756)
                            addItem(player, product, 1, Container.INVENTORY)
                            rewardXP(player, Skills.COOKING, 1.0)
                            sendMessage(player, message)
                        }
                    }
                    return amount <= 0
                }
            })
            return@onUseWith true
        }

        /*
         * Handles creating spicy sauce from a chopped garlic and gnome spice.
         *
         * Product: Uncooked Egg.
         */

        onUseWith(IntType.ITEM, Items.BOWL_1923, Items.EGG_1944) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.UNCOOKED_EGG_7076)
                sendMessage(player, "You prepare an uncooked egg.")
            }
            return@onUseWith true
        }

        /*
         * Handles creating spicy sauce from a chopped garlic and gnome spice.
         */

        onUseWith(IntType.ITEM, Items.CHOPPED_GARLIC_7074, Items.GNOME_SPICE_2169) { player, used, with ->
            if (!hasLevelDyn(player, Skills.COOKING, 9)) {
                sendMessage(player, "You need a Cooking level of 9 to make that.")
                return@onUseWith true
            }

            fun makeDish(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(with.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.SPICY_SAUCE_7072, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 25.0)
                    sendMessage(player, "You mix the ingredients to make spicy sauce.")
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
                withItems(Items.SPICY_SAUCE_7072)
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