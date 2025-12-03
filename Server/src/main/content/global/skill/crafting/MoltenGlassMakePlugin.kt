package content.global.skill.crafting

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Animations
import shared.consts.Items
import kotlin.math.min

class MoltenGlassMakePlugin : InteractionListener {

    companion object {
        const val SODA_ASH = Items.SODA_ASH_1781
        const val BUCKET_OF_SAND = Items.BUCKET_OF_SAND_1783
        const val SANDBAG = Items.SANDBAG_9943
        const val MOLTEN_GLASS = Items.MOLTEN_GLASS_1775
        val INPUTS = intArrayOf(SODA_ASH, BUCKET_OF_SAND, SANDBAG)
        val SAND_SOURCES = intArrayOf(BUCKET_OF_SAND, SANDBAG)
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, INPUTS, *CraftingObject.FURNACES) { player, _, _ ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val sodaAmount = amountInInventory(player, SODA_ASH)
            val sandAmount = SAND_SOURCES.sumOf { amountInInventory(player, it) }
            val maxAmount = min(sodaAmount, sandAmount)

            if (maxAmount <= 0) {
                sendMessage(player, "You need soda ash and sand to make molten glass.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(MOLTEN_GLASS)

                create { productId, amount ->
                    var remaining = amount

                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)
                        if (!inInventory(player, SODA_ASH) || !anyInInventory(player, *SAND_SOURCES)) return@queueScript stopExecuting(player)

                        animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                        sendMessage(player, "You heat the sand and soda ash in the furnace to make glass.")
                        delayClock(player, Clocks.SKILLING, 3)

                        removeItem(player, SODA_ASH)
                        when {
                            inInventory(player, BUCKET_OF_SAND) -> {
                                removeItem(player, BUCKET_OF_SAND)
                                addItem(player, Items.BUCKET_1925)
                            }
                            inInventory(player, SANDBAG) -> {
                                removeItem(player, SANDBAG)
                                addItem(player, Items.EMPTY_SACK_5418)
                            }
                        }

                        addItem(player, productId)
                        rewardXP(player, Skills.CRAFTING, 20.0)
                        player.dispatch(ResourceProducedEvent(productId, 1, player))

                        remaining--
                        if (remaining > 0) {
                            setCurrentScriptState(player, 0)
                            delayScript(player, 2)
                        } else stopExecuting(player)
                    }
                }

                calculateMaxAmount { maxAmount }
            }

            return@onUseWith true
        }
    }


    private fun handleMoltenGlassProcess(player: Player, productId: Int, amount: Int) {
        var remaining = amount

        queueScript(player, 0) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)
            if (remaining <= 0) return@queueScript stopExecuting(player)

            if (!inInventory(player, SODA_ASH) || !anyInInventory(player, *SAND_SOURCES)) {
                return@queueScript stopExecuting(player)
            }

            when (stage) {
                0 -> {
                    delayClock(player, Clocks.SKILLING, 3)
                    delayScript(player, 2)
                }

                else -> {
                    animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                    sendMessage(player, "You heat the sand and soda ash in the furnace to make glass.")

                    if (!removeItem(player, SODA_ASH) || !removeSand(player)) {
                        return@queueScript stopExecuting(player)
                    }

                    addItem(player, productId)
                    rewardXP(player, Skills.CRAFTING, 20.0)
                    player.dispatch(ResourceProducedEvent(productId, 1, player))

                    remaining--

                    if (remaining > 0) {
                        setCurrentScriptState(player, 0)
                        delayScript(player, 2)
                    } else {
                        stopExecuting(player)
                    }
                }
            }
        }
    }

    /**
     * Removes one sand source from the inventory.
     */
    private fun removeSand(player: Player): Boolean {
        return when {
            inInventory(player, BUCKET_OF_SAND) -> {
                if (removeItem(player, BUCKET_OF_SAND)) {
                    addItem(player, Items.BUCKET_1925)
                    true
                } else false
            }

            inInventory(player, SANDBAG) -> {
                if (removeItem(player, SANDBAG)) {
                    addItem(player, Items.EMPTY_SACK_5418)
                    true
                } else false
            }

            else -> false
        }
    }
}