package core.game.node.entity.player.link

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository.send
import core.net.packet.out.HintIcon

/**
 * Handles hint icon operations for a player.
 *
 * Manages adding, removing, and tracking all active hint icons.
 *
 * @author Emperor
 */
class HintIconManager {

    /** Active hint icons for this player (max 8). */
    private val hintIcons: Array<OutgoingContext.HintIcon?> = arrayOfNulls(MAXIMUM_SIZE)

    /** Cached index of the next available slot. */
    private var cachedFreeSlot: Int = 0

    /**
     * Clears all active hint icons.
     */
    fun clear() {
        hintIcons.indices.filter { hintIcons[it] != null }.forEach { index ->
            hintIcons[index]?.let { removeHintIcon(it.player, index) }
        }
        cachedFreeSlot = 0
    }

    /**
     * Finds the first available icon slot.
     *
     * @return Free slot index, or -1 if full.
     */
    fun freeSlot(): Int {
        for (i in cachedFreeSlot until MAXIMUM_SIZE) {
            if (hintIcons[i] == null) {
                cachedFreeSlot = i
                return i
            }
        }
        for (i in 0 until cachedFreeSlot) {
            if (hintIcons[i] == null) {
                cachedFreeSlot = i
                return i
            }
        }
        return -1
    }

    /**
     * Gets the icon at a specific slot.
     *
     * @param slot The slot index.
     * @return The hint icon or null.
     */
    fun getIcon(slot: Int): OutgoingContext.HintIcon? = hintIcons.getOrNull(slot)

    companion object {
        /**
         * The maximum number of active hint icons.
         */
        const val MAXIMUM_SIZE = 8

        /**
         * The default arrow sprite id.
         */
        private const val DEFAULT_ARROW = 1

        /**
         * The default model id.
         */
        private const val DEFAULT_MODEL = -1

        /**
         * Model id for arrow-with-circle icon.
         */
        const val ARROW_CIRCLE_MODEL = 40497

        /**
         * Registers a height-based hint icon.
         *
         * @param player The player.
         * @param target The node to target.
         * @param height The vertical offset.
         * @return The slot id or -1 if unavailable.
         */
        fun registerHeightHintIcon(player: Player, target: Node, height: Int): Int =
            registerHintIcon(player, target, DEFAULT_ARROW, DEFAULT_MODEL, player.hintIconManager.freeSlot(), height)

        /**
         * Registers a new hint icon.
         *
         * Optional params allow full control over arrow, model, slot, and height.
         *
         * @param player The player receiving the icon.
         * @param target The target node.
         * @param arrowId The arrow sprite id.
         * @param modelId The model id.
         * @param slot The slot index (auto if unset).
         * @param height The height offset.
         * @param targetType Optional explicit target type.
         * @return The assigned slot or -1 if invalid.
         */
        fun registerHintIcon(player: Player, target: Node?, arrowId: Int = DEFAULT_ARROW, modelId: Int = DEFAULT_MODEL, slot: Int = player.hintIconManager.freeSlot(), height: Int = 0, targetType: Int? = null): Int {
            if (slot < 0 || target == null) return -1

            val manager = player.hintIconManager
            val resolvedTargetType = targetType ?: when (target) {
                is Player -> 10
                is Entity -> 1
                else -> 2
            }

            val icon = OutgoingContext.HintIcon(player, slot, arrowId, resolvedTargetType, target, modelId, height)
            send(HintIcon::class.java, icon)
            manager.hintIcons[slot] = icon
            return slot
        }

        /**
         * Removes the hint icon at a given slot.
         *
         * @param player The player.
         * @param slot The slot index.
         */
        fun removeHintIcon(player: Player, slot: Int) {
            if (slot < 0) return

            val manager = player.hintIconManager
            val icon = manager.hintIcons.getOrNull(slot) ?: return

            icon.targetType = 0
            send(HintIcon::class.java, icon)
            manager.hintIcons[slot] = null
        }
    }
}
