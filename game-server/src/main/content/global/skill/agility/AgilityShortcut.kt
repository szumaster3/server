package content.global.skill.agility

import core.api.getStatLevel
import core.api.inEquipment
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * Represents a base class for agility shortcuts in the game.
 *
 * @property ids Object ids associated with this shortcut (nullable).
 * @property level Required agility level.
 * @property experience Experience gained upon success.
 * @property canFail Whether this shortcut can fail.
 * @property failChance Chance of failure (0.0 to 1.0).
 * @property options Interaction options (nullable).
 *
 * @author Woah
 */
abstract class AgilityShortcut(
    val ids: IntArray?,
    val level: Int,
    val experience: Double,
    val canFail: Boolean = false,
    var failChance: Double = 0.0,
    vararg val options: String?
) : OptionHandler() {

    constructor(ids: IntArray?, level: Int, experience: Double, vararg options: String?) :
            this(ids, level, experience, false, 0.0, *options)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(this)
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (!checkRequirements(player)) return true
        run(player, node.asScenery(), option, checkFail(player, node.asScenery(), option))
        return true
    }

    /**
     * Executes the shortcut logic.
     *
     * @param player The player using the shortcut.
     * @param scenery The obstacle scenery.
     * @param option The interaction option.
     * @param failed Whether the attempt failed.
     */
    abstract fun run(player: Player, scenery: Scenery, option: String, failed: Boolean)

    /**
     * Checks if the player meets the agility and equipment requirements.
     *
     * @param player The player to check.
     * @return True if requirements are met, false otherwise.
     */
    open fun checkRequirements(player: Player): Boolean {
        if (inEquipment(player, Items.SLED_4084, 1)) {
            sendMessage(player, "You can't do that on a sled.")
            return false
        }
        if (getStatLevel(player, Skills.AGILITY) < level) {
            sendMessage(player, "You need an agility level of at least $level to negotiate this obstacle.")
            return false
        }
        return true
    }

    /**
     * Check if the shortcut attempt has failed based on chance.
     *
     * @param player The player attempting the shortcut.
     * @param scenery The obstacle scenery.
     * @param option The chosen interaction option.
     * @return True if the attempt failed, false otherwise.
     */
    private fun checkFail(player: Player, scenery: Scenery, option: String): Boolean {
        if (!canFail) return false
        return AgilityHandler.hasFailed(player, level, failChance)
    }

    /**
     * Registers this shortcut's handlers to its associated object definitions.
     *
     * @param shortcut The agility shortcut instance.
     */
    open fun configure(shortcut: AgilityShortcut) {
        shortcut.ids?.forEach { objectId ->
            val def = SceneryDefinition.forId(objectId)
            shortcut.options.forEach { opt ->
                if (opt != null) {
                    def.handlers["option:$opt"] = shortcut
                }
            }
        }
    }

    /**
     * Returns the mirrored direction for object orientation.
     *
     * @param direction The original direction.
     * @return The mirrored direction.
     */
    protected fun getObjectDirection(direction: Direction): Direction = when (direction) {
        Direction.NORTH -> Direction.EAST
        Direction.SOUTH -> Direction.WEST
        Direction.EAST -> Direction.NORTH
        Direction.WEST -> Direction.SOUTH
        Direction.NORTH_WEST -> Direction.NORTH_EAST
        Direction.NORTH_EAST -> Direction.SOUTH_EAST
        Direction.SOUTH_WEST -> Direction.NORTH_WEST
        Direction.SOUTH_EAST -> Direction.SOUTH_WEST
    }

    /**
     * Calculates destination location after moving through a pipe obstacle.
     *
     * @param player The player.
     * @param scenery The obstacle.
     * @param steps Number of tiles to move.
     * @return The target location.
     */
    fun pipeDestination(player: Player, scenery: Scenery, steps: Int): Location {
        player.faceLocation(scenery.location)
        var diffX = scenery.location.x - player.location.x
        diffX = diffX.coerceIn(-1, 1)
        var diffY = scenery.location.y - player.location.y
        diffY = diffY.coerceIn(-1, 1)
        return player.location.transform(diffX * steps, diffY * steps, 0)
    }

    /**
     * Calculates destination location based on step count and difference in position.
     *
     * @param player The player.
     * @param scenery The obstacle.
     * @param steps Number of tiles to move.
     * @return The target location.
     */
    fun agilityDestination(player: Player, scenery: Scenery, steps: Int): Location {
        player.faceLocation(scenery.location)
        val diffX = scenery.location.x - player.location.x
        val diffY = scenery.location.y - player.location.y
        return player.location.transform(diffX * steps, diffY * steps, 0)
    }
}
