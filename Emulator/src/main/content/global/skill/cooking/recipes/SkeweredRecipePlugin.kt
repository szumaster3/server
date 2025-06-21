package content.global.skill.cooking.recipes

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds
import kotlin.math.min

/**
 * Handles cooking recipes related to skewered foods.
 */
class SkeweredRecipePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating skewered food with a spit.
         */

        val spitRecipes = mapOf(
            Items.RAW_BIRD_MEAT_9978 to Pair(11, Items.SKEWERED_BIRD_MEAT_9984),
            Items.RAW_RABBIT_3226 to Pair(16, Items.SKEWERED_RABBIT_7224),
            Items.RAW_BEAST_MEAT_9986 to Pair(21, Items.SKEWERED_BEAST_9992),
            Items.RAW_CHOMPY_2876 to Pair(30, Items.SKEWERED_CHOMPY_7230)
        )

        onUseWith(IntType.ITEM, RAW_INGREDIENTS, IRON_SPIT) { player, used, with ->
            val (requiredLevel, productID) = spitRecipes[used.id] ?: return@onUseWith false
            if (!hasLevelDyn(player, Skills.COOKING, requiredLevel)) {
                sendDialogue(player, "You need a Cooking level of at least $requiredLevel to make that.")
                return@onUseWith true
            }

            val ingredientName = used.name.lowercase()
            val maxAmount = min(amountInInventory(player, used.id), amountInInventory(player, with.id))

            val process = {
                if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                    sendMessage(player, "You don't have the required ingredients to make that.")
                    false
                } else {
                    addItem(player, productID, 1)
                    sendMessage(player, "You pierce the $ingredientName with the iron spit.")
                    true
                }
            }

            handleMultiItemProcess(player, maxAmount, productID, process)
            true
        }

        /*
         * Handles creating a spider on a stick using a skewer stick and a spider carcass.
         */

        handleSpiderRecipe(SPIDER_CARCASS, SKEWER_STICK, SPIDER_ON_STICK, Sounds.TBCU_SPIDER_STICK_1280)

        /*
         * Handles creating a spider on a shaft using an arrow shaft and a spider carcass.
         */

        handleSpiderRecipe(SPIDER_CARCASS, ARROW_SHAFT, SPIDER_ON_SHAFT, Sounds.TBCU_SPIDER_1279)
    }

    private fun handleSpiderRecipe(ingredient: Int, tool: Int, result: Int, sound: Int) {
        onUseWith(IntType.ITEM, ingredient, tool) { player, used, with ->
            val maxAmount = min(amountInInventory(player, used.id), amountInInventory(player, with.id))

            val process = {
                if (!removeItem(player, used.asItem()) || !removeItem(player, with.asItem())) {
                    sendMessage(player, "You don't have the required ingredients to make that.")
                    false
                } else {
                    animate(player, PIERCE_ANIMATION)
                    playAudio(player, sound)
                    addItem(player, result, 1)
                    sendMessage(player, "You pierce the spider carcass with the ${getItemName(tool).lowercase()}.")
                    true
                }
            }

            handleMultiItemProcess(player, maxAmount, result, process)
            return@onUseWith true
        }
    }

    private fun handleMultiItemProcess(player: Player, maxAmount: Int, productID: Int, process: () -> Boolean) {
        if (maxAmount == 1) {
            process()
        } else {
            sendSkillDialogue(player) {
                withItems(productID)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) process()
                    }
                }
                calculateMaxAmount { maxAmount }
            }
        }
    }

    companion object {
        private const val ARROW_SHAFT      = Items.ARROW_SHAFT_52
        private const val SPIDER_CARCASS   = Items.SPIDER_CARCASS_6291
        private const val PIERCE_ANIMATION = Animations.CRAFT_ITEM_1309
        private const val SKEWER_STICK     = Items.SKEWER_STICK_6305
        private const val SPIDER_ON_SHAFT  = Items.SPIDER_ON_SHAFT_6295
        private const val SPIDER_ON_STICK  = Items.SPIDER_ON_STICK_6293
        private const val IRON_SPIT        = Items.IRON_SPIT_7225
        private val RAW_INGREDIENTS        = intArrayOf(Items.RAW_CHOMPY_2876, Items.RAW_RABBIT_3226, Items.RAW_BIRD_MEAT_9978, Items.RAW_BEAST_MEAT_9986)
    }
}
