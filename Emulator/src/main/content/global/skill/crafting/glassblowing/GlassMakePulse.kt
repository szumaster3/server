package content.global.skill.crafting.glassblowing

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Represents creating molten glass.
 */
class GlassMakePulse(
    private val player: Player,
    val product: Int,
    private var amount: Int,
) : Pulse() {
    companion object {
        private val SAND_SOURCES = intArrayOf(Items.BUCKET_OF_SAND_1783, Items.SANDBAG_9943)
    }

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
     * Removes one sand source from the player's inventory.
     *
     * @param player The player whose inventory to modify.
     * @return `true` if sand was successfully removed, `false` otherwise.
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
