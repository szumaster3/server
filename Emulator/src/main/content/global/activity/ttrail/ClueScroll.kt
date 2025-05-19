package content.global.activity.ttrail

import core.api.log
import core.api.toIntArray
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin
import core.tools.Log
import core.tools.RandomFunction

/**
 * Represents a base class for all clue scroll in the [TreasureTrailPlugin] activity.
 *
 * @param name          The name of the clue zone.
 * @param clueId        The item id of clue scroll.
 * @param level         The difficulty level of the clue.
 * @param interfaceId   The interface id shown when reading the clue.
 * @param borders       (Optional) map zone borders for region-based clues.
 */
abstract class ClueScroll(
    name: String,
    val clueId: Int,
    val level: ClueLevel,
    val interfaceId: Int,
    private val borders: Array<out ZoneBorders> = emptyArray()
) : MapZone(name, true), Plugin<Any> {

    /**
     * No-op event handler. Override if needed.
     */
    override fun fireEvent(identifier: String?, vararg args: Any?): Any? = null

    /**
     * Rewards the player by progressing the clue trail and replacing the clue
     * with a casket or removing it if no casket is given.
     *
     * @param player The player to reward.
     * @param casket If true, replace the clue with a casket. Otherwise, remove the clue.
     */
    fun reward(player: Player, casket: Boolean) {
        val clue = player.inventory.getItem(Item(clueId)) ?: return
        nextStage(player, clue)
        if (casket) {
            player.inventory.replace(Item(level.casketId, 1), clue.slot)
        } else {
            player.inventory.remove(clue)
        }
    }

    /**
     * Rewards the player with a casket after clue completion.
     *
     * @param player The player to reward.
     */
    fun reward(player: Player) {
        reward(player, true)
    }

    /**
     * Advances the player to the next stage of the clue trail,
     * or clears the trail if the final stage is completed.
     *
     * @param player The player progressing through the trail.
     * @param clue The clue item being progressed.
     */
    private fun nextStage(player: Player, clue: Item) {
        val manager = TreasureTrailManager.getInstance(player)
        if (!manager.hasTrail() || clue.id != manager.clueId) {
            manager.startTrail(this)
        }
        val currentStage = manager.trailStage
        if (currentStage >= manager.trailLength) {
            manager.clearTrail()
        } else {
            manager.incrementStage()
        }
    }

    /**
     * Opens the clue interface for the player, if no other interface is already open.
     *
     * @param player The player reading the clue.
     */
    open fun read(player: Player) {
        if (player.interfaceManager.isOpened()) {
            player.sendMessage("Please finish what you're doing first.")
            return
        }
        player.interfaceManager.open(Component(interfaceId))
    }

    /**
     * Registers a clue plugin and its map zone.
     *
     * @param clue The clue plugin to register.
     */
    fun register(clue: ClueScroll) {
        if (clue.clueId == 2681) return

        if (CLUE_SCROLLS.containsKey(clue.clueId)) {
            log(this::class.java, Log.ERR, "Error! Plugin already registered with clue id - ${clue.clueId}, trying to register ${clue::class.java.canonicalName} the real plugin using the id is ${CLUE_SCROLLS[clue.clueId]!!::class.java.canonicalName}!")
            return
        }

        ORGANIZED.computeIfAbsent(clue.level) { ArrayList(20) }.add(clue)
        ZoneBuilder.configure(clue)
        CLUE_SCROLLS[clue.clueId] = clue
    }

    /**
     * Configures the clue zone by registering its map borders.
     */
    override fun configure() {
        borders.forEach { register(it) }
    }

    /**
     * Checks whether the player is wearing at least one item from each of the provided equipment sets.
     *
     * @param player The player to check.
     * @param equipment An array of equipment sets, where each set is an array of item IDs.
     * @return True if the player satisfies the requirement for all sets.
     */
    fun hasEquipment(player: Player, equipment: Array<IntArray>?): Boolean {
        if (equipment.isNullOrEmpty()) return true
        var hasAmt = 0
        for (equipSet in equipment) {
            if (equipSet.any { player.equipment.contains(it, 1) }) {
                hasAmt++
            }
        }
        return hasAmt == equipment.size
    }

    companion object {
        /**
         * All wilderness cape ids. Used in certain clue equipment checks.
         */
        val WILDERNESS_CAPES = (4315..4414).toIntArray()

        private val CLUE_SCROLLS: MutableMap<Int, ClueScroll> = HashMap()
        private val ORGANIZED: MutableMap<ClueLevel, MutableList<ClueScroll>> = HashMap()

        /**
         * Gets a random clue scroll item of the given difficulty level.
         *
         * @param clueLevel The level of clue to fetch.
         * @return A new clue scroll item, or null if none are registered.
         */
        fun getClue(clueLevel: ClueLevel): Item? {
            val clues = ORGANIZED[clueLevel]
            if (clues == null) {
                log(ClueScroll::class.java, Log.ERR, "Error! There are no clues for level $clueLevel!")
                return null
            }
            val clue = clues[RandomFunction.random(clues.size)]
            return Item(clue.clueId)
        }

        /**
         * Gets all registered clue scrolls mapped by their item ID.
         *
         * @return A map of clue ID to plugin instance.
         */
        fun getClueScrolls(): Map<Int, ClueScroll> = CLUE_SCROLLS
    }
}
