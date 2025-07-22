package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScroll
import content.global.activity.ttrail.TreasureTrailManager
import core.api.*
import core.game.global.action.DigAction
import core.game.global.action.DigSpadeHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery

/**
 * Represents a cryptic clue scroll.
 * @author szu
 */
abstract class CrypticScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    val clue: String?,
    val location: Location?,
    val obj: Int = 0,
    vararg borders: ZoneBorders?
) : ClueScroll(name, clueId, level, Components.TRAIL_MAP09_345, borders.filterNotNull().toTypedArray()) {

    companion object {
        /**
         * A key used for unlocking containers for easy-level clues.
         */
        private val KEY_EASY = Item(Items.KEY_2832)

        /**
         * A key used for unlocking containers for medium-level clues.
         */
        private val KEY_MEDIUM = Item(Items.KEY_2834)

        /**
         * A key used for unlocking containers for hard-level clues.
         */
        private val KEY_HARD = Item(Items.KEY_2840)
    }

    /**
     * Handles using an item with a node (e.g. using clue scroll on fire for burning clue).
     */
    override fun handleUseWith(player: Player, used: Item, with: Node): Boolean {
        return if (
            player.inventory.contains(clueId, 1) &&
            with.asScenery().id == Scenery.FIRE_2732 &&
            inBorders(player, 2968, 2974, 2970, 2976)
        ) {
            handleReward(player)
        } else {
            super.handleUseWith(player, used, with)
        }
    }

    /**
     * Handles interacting with the clue's target object (e.g., searching or talking).
     */
    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        val player = e as? Player ?: return super.interact(e, target, option)

        val checkInteraction = target.id == obj &&
            (option.name == "Search" || option.name == "Talk-to")

        if (!checkInteraction) return super.interact(player, target, option)

        if (!inInventory(player, clueId, 1) || target.location != location) {
            player.sendMessage("Nothing interesting happens.")
            return false
        }

        return handleReward(player)
    }

    /**
     * Reward the player by removing the clue scroll and giving the next item or casket.
     */
    private fun handleReward(player: Player): Boolean {
        val manager = TreasureTrailManager.getInstance(player)
        val clueScroll = getClueScrolls()[clueId] ?: return false

        if (!removeItem(player, clueScroll.clueId, Container.INVENTORY)) return false

        clueScroll.reward(player)

        if (manager.isCompleted) {
            sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
            manager.clearTrail()
        } else {
            val newClue = clueScroll.level?.let { getClue(it) }
            if (newClue != null) {
                sendItemDialogue(player, newClue, "You found another clue scroll.")
                addItem(player, newClue.id, 1)
            }
        }
        return true
    }

    /**
     * Registers the clue's dig location to be handled by the spade digging system.
     */
    override fun configure() {
        location?.let { DigSpadeHandler.register(it, CrypticDigAction()) }
        super.configure()
    }

    /**
     * Displays the clue text to the player on the scroll interface.
     */
    override fun read(player: Player) {
        for (i in 1..8) {
            sendString(player, "", interfaceId, i)
        }
        super.read(player)
        clue?.let { sendString(player, it.replace("<br>", "<br><br>"), interfaceId, 1) }
    }

    /**
     * Called when the player digs at the correct location to solve this clue.
     */
    fun dig(player: Player) {
        reward(player)
        sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
    }

    /**
     * Checks if the player is holding the clue scroll required for this step.
     */
    fun hasRequiredItems(player: Player): Boolean {
        return player.inventory.contains(clueId, 1)
    }

    /**
     * A dig action implementation for cryptic clue scrolls that require digging.
     */
    private inner class CrypticDigAction : DigAction {
        /**
         * Executes the dig action if the player holds the correct clue item.
         */
        override fun run(player: Player?) {
            if (!hasRequiredItems(player!!)) {
                sendMessage(player,"Nothing interesting happens.")
                return
            }
            dig(player)
        }
    }
}
