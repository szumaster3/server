package content.global.skill.crafting

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.tools.RandomUtils
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds
import kotlin.math.min

class StoneSplittingPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles stone pillar crafting.
         */

        onUseWith(IntType.ITEM, Items.STONE_SLAB_13245, Items.CHISEL_1755) { player, used, _ ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            if (!hasLevel(player, Skills.CRAFTING, 20)) return@onUseWith true
            if (!hasTool(player, Items.HAMMER_2347)) return@onUseWith true
            if (!inInventory(player, used.id)) return@onUseWith true

            runTask(player, 2) {
                delayClock(player, Clocks.SKILLING, 2)
                playAudio(player, Sounds.HAMMER_STONE_2100)
                animate(player, Animations.USE_HAMMER_CHISEL_11041)
                if (removeItem(player, used.id)) {
                    rewardXP(player, Skills.CRAFTING, 20.0)
                    addItem(player, Items.PILLAR_13246)
                    sendMessage(player, "You craft the stone into a pillar.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles granite cutting.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *GRANITE_IDS) { player, _, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            if (!hasTool(player, Items.CHISEL_1755)) return@onUseWith true

            setTitle(player, 2)
            sendOptions(player, "What would you like to do?", "Split the block into smaller pieces.", "Nothing.")

            addDialogueAction(player) { _, button ->
                if (button == 2) {
                    val amount = min(amountInInventory(player, with.id), amountInInventory(player, with.id))
                    submitIndividualPulse(player, GraniteCuttingPulse(player, Item(with.id), amount))
                } else closeDialogue(player)
            }
            return@onUseWith true
        }

        /*
         * Handles limestone cutting into bricks.
         */

        onUseWith(IntType.ITEM, Items.LIMESTONE_3211, Items.CHISEL_1755) { player, used, _ ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            if (!hasLevel(player, Skills.CRAFTING, 12) || !hasTool(player, Items.CHISEL_1755)) return@onUseWith true

            sendSkillDialogue(player) {
                withItems(Items.LIMESTONE_BRICK_3420)
                create { _, amount ->
                    delayClock(player, Clocks.SKILLING, 2)
                    player.pulseManager.run(object : Pulse(1, player) {
                        var remaining = amount

                        override fun pulse(): Boolean {
                            if (remaining <= 0 || !inInventory(player, used.id)) {
                                sendMessage(player, "You have ran out of limestone.")
                                stop()
                                return true
                            }

                            animate(player, Animations.HUMAN_CHISEL_LIMESTONE_4470)

                            if (removeItem(player, used.id)) {
                                val successProbability =
                                    BASE_SUCCESS_PROBABILITY + getStatLevel(player, Skills.CRAFTING) * SUCCESS_PER_LEVEL
                                if (RandomUtils.randomDouble() <= successProbability) {
                                    rewardXP(player, Skills.CRAFTING, 6.0)
                                    addItem(player, Items.LIMESTONE_BRICK_3420)
                                    sendMessage(
                                        player,
                                        "You successfully craft ${getItemName(Items.LIMESTONE_BRICK_3420)}."
                                    )
                                } else {
                                    rewardXP(player, Skills.CRAFTING, 1.5)
                                    addItem(player, Items.ROCK_968)
                                    sendMessage(player, "You fail to craft ${getItemName(Items.LIMESTONE_BRICK_3420)}.")
                                }
                            }

                            remaining--
                            return false
                        }
                    })
                }

                calculateMaxAmount { min(amountInInventory(player, used.id), amountInInventory(player, used.id)) }
            }

            return@onUseWith true
        }
    }

    /**
     * Checks if the player has the required crafting level.
     */
    private fun hasLevel(player: Player, skill: Int, level: Int): Boolean {
        if (getStatLevel(player, skill) < level) {
            sendMessage(player, "You need a Crafting level of at least $level.")
            return false
        }
        return true
    }

    /**
     * Checks if the player has the required tool.
     */
    private fun hasTool(player: Player, toolId: Int): Boolean {
        if (!inInventory(player, toolId)) {
            sendMessage(player, "You need ${getItemName(toolId)} to do that.")
            return false
        }
        return true
    }

    companion object {
        private const val MAXIMUM_SUCCESS_LEVEL = 40
        private const val BASE_SUCCESS_PROBABILITY = 0.75
        private const val MAXIMUM_SUCCESS_PROBABILITY = 1.0
        private val SPREAD_SUCCESS = MAXIMUM_SUCCESS_PROBABILITY - BASE_SUCCESS_PROBABILITY
        private val SUCCESS_PER_LEVEL = SPREAD_SUCCESS / MAXIMUM_SUCCESS_LEVEL
        private val GRANITE_IDS = intArrayOf(Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983)
    }

}

/**
 * Represents pulse used to split granite.
 */
private class GraniteCuttingPulse(player: Player, node: Item, var amount: Int) : SkillPulse<Item>(player, node) {

    private var ticks = 0
    init { resetAnimation = false }

    override fun checkRequirements(): Boolean {
        if (!inInventory(player, Items.CHISEL_1755)) return false
        if (!anyInInventory(player, *GRANITE)) {
            sendMessage(player, "You have ran out of granite.")
            return false
        }
        val requiredSlots = SLOTS.last
        if (freeSlots(player) < requiredSlots) {
            sendDialogue(player, "You'll need $requiredSlots empty inventory spaces to hold the granite.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0 || ticks < 1) {
            playAudio(player, Sounds.CHISEL_2586)
            animate(player, Animations.HUMAN_CHISEL_GRANITE_11146)
        }
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 2)

        if (amount < 1) return true

        if (removeItem(player, Item(node.id, 1))) {
            when (node.id) {
                Items.GRANITE_5KG_6983 -> {
                    addItem(player, Items.GRANITE_2KG_6981, 2)
                    addItem(player, Items.GRANITE_500G_6979, 2)
                }
                else -> addItem(player, Items.GRANITE_500G_6979, 4)
            }
        }
        amount--
        return amount < 1
    }

    companion object {
        private val GRANITE = intArrayOf(Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983)
        private val SLOTS = 1..4
    }
}