package content.global.plugins.inter.with_item

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomUtils
import shared.consts.Animations
import shared.consts.Items

class FishingOffcutsPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cut open a leaping trout, leaping salmon, or leaping sturgeon using a knife.
         */

        onUseWith(
            IntType.ITEM,
            Items.KNIFE_946,
            Items.LEAPING_TROUT_11328,
            Items.LEAPING_SALMON_11330,
            Items.LEAPING_STURGEON_11332
        ) { player, _, fishItem ->

            val fishId = fishItem.id
            val level = getStatLevel(player, Skills.COOKING)

            if (level < 1) {
                sendDialogue(player, "You need a Cooking level to attempt cutting this fish.")
                return@onUseWith true
            }

            val slots = freeSlots(player)
            val hasOffcuts = inInventory(player, Items.FISH_OFFCUTS_11334)
            if (slots < 2 && (slots < 1 || !hasOffcuts)) {
                sendMessage(player, "You don't have enough space in your pack to attempt cutting open the fish.")
                return@onUseWith true
            }

            val anim = Animation(Animations.OFFCUTS_6702)

            queueScript(player, 1, QueueStrength.WEAK) {

                if (!clockReady(player, Clocks.SKILLING)) {
                    return@queueScript keepRunning(player)
                }

                if (amountInInventory(player, fishId) <= 0) {
                    return@queueScript clearScripts(player)
                }

                player.animate(anim)
                removeItem(player, fishId)

                val success = rollSuccess(fishId, level)
                if (success) {
                    val product = when (fishId) {
                        Items.LEAPING_TROUT_11328,
                        Items.LEAPING_SALMON_11330 -> Items.ROE_11324
                        Items.LEAPING_STURGEON_11332 -> Items.CAVIAR_11326
                        else -> -1
                    }

                    if (product != -1) addItem(player, product)
                    if (rollOffcuts(fishId)) addItem(player, Items.FISH_OFFCUTS_11334)

                    val xp = when (fishId) {
                        Items.LEAPING_TROUT_11328,
                        Items.LEAPING_SALMON_11330 -> 10.0
                        Items.LEAPING_STURGEON_11332 -> 15.0
                        else -> 0.0
                    }

                    rewardXP(player, Skills.COOKING, xp)
                    sendMessage(player, "You cut open the fish and extract some roe, but the rest is discarded.")
                } else {
                    sendMessage(player, "You fail to cut the fish properly and ruin it.")
                }

                delayClock(player, Clocks.SKILLING, 2)
                keepRunning(player)
            }

            return@onUseWith true
        }
    }


    private fun rollSuccess(fish: Int, level: Int): Boolean =
        when (fish) {
            Items.LEAPING_TROUT_11328 ->
                RandomUtils.randomDouble() < (level.coerceAtMost(99) / 150.0)

            Items.LEAPING_SALMON_11330,
            Items.LEAPING_STURGEON_11332 ->
                RandomUtils.randomDouble() < (level.coerceAtMost(80) / 80.0)

            else -> true
        }

    private fun rollOffcuts(fish: Int): Boolean =
        when (fish) {
            Items.LEAPING_TROUT_11328    -> RandomUtils.randomDouble() < 0.5
            Items.LEAPING_SALMON_11330   -> RandomUtils.randomDouble() < 0.75
            Items.LEAPING_STURGEON_11332 -> RandomUtils.randomDouble() < (5.0 / 6.0)
            else -> false
        }

}