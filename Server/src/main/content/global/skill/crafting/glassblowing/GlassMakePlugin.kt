package content.global.skill.crafting.glassblowing

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery
import kotlin.math.min

class GlassMakePlugin : InteractionListener {

    private val SODA_ASH = Items.SODA_ASH_1781
    private val BUCKET_OF_SAND = Items.BUCKET_OF_SAND_1783
    private val SANDBAG = Items.SANDBAG_9943
    private val MOLTEN_GLASS = Items.MOLTEN_GLASS_1775
    private val INPUTS = intArrayOf(SODA_ASH, BUCKET_OF_SAND, SANDBAG)
    private val FURNACES = intArrayOf(Scenery.FURNACE_4304, Scenery.FURNACE_6189, Scenery.LAVA_FORGE_9390, Scenery.FURNACE_11010, Scenery.FURNACE_11666, Scenery.FURNACE_12100, Scenery.FURNACE_12809, Scenery.FURNACE_18497, Scenery.FURNACE_26814, Scenery.FURNACE_30021, Scenery.FURNACE_30510, Scenery.FURNACE_36956, Scenery.FURNACE_37651)
    private val SAND_SOURCES = intArrayOf(BUCKET_OF_SAND, SANDBAG)

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, INPUTS, *FURNACES) { player, _, _ ->
            if (!inInventory(player, SODA_ASH, 1)) {
                sendMessage(player, "You need at least one heap of soda ash to do this.")
                return@onUseWith true
            }

            var hasSand = false
            for (sandId in SAND_SOURCES) {
                if (inInventory(player, sandId)) {
                    hasSand = true
                    break
                }
            }

            if (!hasSand) {
                sendMessage(player, "You need at least one bucket of sand or sandbag to do this.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(MOLTEN_GLASS)
                create { id, amount ->
                    submitIndividualPulse(
                        player,
                        GlassMakePulse(player, id, amount)
                    )
                }
                calculateMaxAmount { _ ->
                    min(amountInInventory(player, SODA_ASH), SAND_SOURCES.sumOf { amountInInventory(player, it) })
                }
            }

            return@onUseWith true
        }
    }
}

/**
 * Handles crafting molten glass.
 */
private class GlassMakePulse(private val player: Player, val product: Int, private var amount: Int, ) : Pulse() {

    private val SAND_SOURCES = intArrayOf(Items.BUCKET_OF_SAND_1783, Items.SANDBAG_9943)

    override fun pulse(): Boolean {
        if (amount < 1) return true

        if (!inInventory(player, Items.SODA_ASH_1781) || !anyInInventory(player, *SAND_SOURCES)) {
            return true
        }

        lock(player, 3)
        animate(player, Animations.USE_FURNACE_3243)
        sendMessage(player, "You heat the sand and soda ash in the furnace to make glass.")

        if (removeItem(player, Items.SODA_ASH_1781) && removeSand(player)) {
            addItem(player, Items.MOLTEN_GLASS_1775)
            rewardXP(player, Skills.CRAFTING, 20.0)
            player.dispatch(ResourceProducedEvent(product, amount, player))
        } else {
            return true
        }

        amount--
        delay = 3

        return false
    }

    /**
     * Removes one sand source from the inventory.
     */
    private fun removeSand(player: Player): Boolean {
        return when {
            inInventory(player, Items.BUCKET_OF_SAND_1783) -> {
                if (removeItem(player, Items.BUCKET_OF_SAND_1783)) {
                    addItem(player, Items.BUCKET_1925)
                    true
                } else false
            }
            inInventory(player, Items.SANDBAG_9943) -> {
                if (removeItem(player, Items.SANDBAG_9943)) {
                    addItem(player, Items.EMPTY_SACK_5418)
                    true
                } else false
            }
            else -> false
        }
    }
}