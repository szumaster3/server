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
import java.util.*

/**
 * Represents a clue scroll plugin.
 * @author Vexia
 */
abstract class ClueScroll(
    name: String?,
    val clueId: Int,
    val level: ClueLevel?,
    val interfaceId: Int,
    private val borders: Array<out ZoneBorders> = emptyArray()
) : MapZone(name, true), Plugin<Any> {

    /**
     * No-op event handler. Override if needed.
     */
    override fun fireEvent(identifier: String?, vararg args: Any?): Any? = null

    /**
     * Rewards the player with a casket.
     * @param player the player.
     * @param casket if we give a ckaset.
     */
    fun reward(player: Player, casket: Boolean) {
        val clue = player.inventory.getItem(Item(clueId)) ?: return
        nextStage(player, clue)
        if (casket) {
            player.inventory.replace(level?.let { Item(it.casketId, 1) }, clue.slot)
        } else {
            player.inventory.remove(clue)
        }
    }

    /**
     * Rewards the player.
     * @param player the player.
     */
    fun reward(player: Player) {
        reward(player, true)
    }

    /**
     * Increments the next stage of the clue.
     * @param player the player.
     * @param clue the clue.
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
     * Reads a clue scroll.
     * @param player the player.
     */
    open fun read(player: Player) {
        if (player.interfaceManager.isOpened()) {
            player.sendMessage("Please finish what you're doing first.")
            return
        }
        player.interfaceManager.open(Component(interfaceId))
    }

    /**
     * Registers a clue scroll into the repository.
     * @param clue the plugin.
     */
    fun register(clue: ClueScroll) {
        if (clue.clueId == 2681) return

        if (CLUE_SCROLLS.containsKey(clue.clueId)) {
            log(
                this::class.java,
                Log.ERR,
                "Error! Plugin already registered with clue id - ${clue.clueId}, trying to register ${clue::class.java.canonicalName} the real plugin using the id is ${CLUE_SCROLLS[clue.clueId]!!::class.java.canonicalName}!"
            )
            return
        }

        clue.level?.let { ORGANIZED.computeIfAbsent(it) { ArrayList(20) }.add(clue) }
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
     * Checks if the player has equipment.
     * @param player the player.
     * @param equipment the equipment.
     * @return true if so.
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
         * The wilderness cape ids.
         */
        val WILDERNESS_CAPES = (4315..4414).toIntArray()

        /**
         * The mapping of clue scrolls.
         */
        private val CLUE_SCROLLS: MutableMap<Int, ClueScroll> = HashMap()

        /**
         * A map of pre organized clue scrolls.
         */
        private val ORGANIZED: MutableMap<ClueLevel, MutableList<ClueScroll>> = EnumMap(ClueLevel::class.java)

        /**
         * Gets a clue item.
         * @param clueLevel the level.
         * @return the item.
         */
        @JvmStatic
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
         * Gets all registered clue scrolls ids.
         * @return A map of clue ids.
         */
        @JvmStatic
        fun getClueScrolls(): Map<Int, ClueScroll> = CLUE_SCROLLS
    }
}
