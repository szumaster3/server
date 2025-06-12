package content.region.fremennik.quests.viking.handlers

import content.data.GameAttributes
import core.api.getAttribute
import core.api.sendDialogue
import core.api.sendMessage
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import org.rs.consts.Items
import kotlin.math.abs
import kotlin.math.atan2

/**
 * Listener for the Hunter's Talisman item used in the [FremennikTrials][content.region.fremennik.quest.viking.FremennikTrials] quest.
 * Handles the "locate" interaction which guides the player towards the [DraugenNPC][content.region.fremennik.quest.viking.handlers.DraugenNPC].
 */
class HunterTalismanPlugin : InteractionListener {
    val TALISMAN = Items.HUNTERS_TALISMAN_3696

    override fun defineListeners() {
        /*
         * Handles option on the Hunter's Talisman.
         */

        on(TALISMAN, IntType.ITEM, "locate") { player, _ ->
            var locationString = getAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_LOCATION, "none")
            if (locationString == "none") {
                val newLoc = possibleLocations.random()
                setAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_LOCATION, "${newLoc.x},${newLoc.y}")
                locationString = "${newLoc.x},${newLoc.y}"
            }
            val locationComponents = locationString.split(",")
            val draugenLoc =
                Location(Integer.parseInt(locationComponents[0]), Integer.parseInt(locationComponents[1]))

            if (player.location?.withinDistance(draugenLoc, 5)!!) {
                sendDialogue(player, "The Draugen is nearby, be careful!")
                Pulser.submit(DraugenPulse(player))
            } else {
                val neededDirection = draugenLoc.getDirection(player as Entity)
                sendMessage(player, "The talisman pulls you to the $neededDirection.")
            }
            return@on true
        }
    }

    /**
     * Pulse that delays the spawning of the Draugen NPC.
     *
     * @param player The player using the talisman.
     */
    class DraugenPulse(
        val player: Player,
    ) : Pulse() {
        var count = 0

        override fun pulse(): Boolean {
            when (count++) {
                3 -> {
                    if (getAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_SPAWN, false)) return true
                    sendMessage(player, "The draugen is here! Beware!")
                    DraugenNPC(player).init()
                    setAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_SPAWN, true)
                    return true
                }
            }
            return false
        }
    }

    /**
     * List of possible locations where the Draugen may spawn.
     */
    private val possibleLocations =
        listOf(
            Location(2625, 3608),
            Location(2602, 3628),
            Location(2668, 3714),
            Location(2711, 3602),
            Location(2664, 3592),
        )

    /**
     * Determines the cardinal/intercardinal direction from an entity's location to this location.
     *
     * @param entity The entity from which to calculate the direction.
     * @return A string representing the direction to this location.
     */
    fun Location.getDirection(entity: Entity): String {
        val loc: Location = this
        val difX: Double = (loc.x - entity.location.x).toDouble()
        val difY: Double = (loc.y - entity.location.y).toDouble()
        val angle = Math.toDegrees(atan2(difX, difY))
        val NORTH = 0.toDouble()
        val SOUTH = 180.toDouble()
        val EAST = (-90).toDouble()
        val WEST = 90.toDouble()
        val NORTHEAST = (-135).toDouble()
        val NORTHWEST = 135.toDouble()
        val SOUTHEAST = (-45).toDouble()
        val SOUTHWEST = 45.toDouble()
        if (diff(angle, NORTH) < 3) {
            return "north"
        }
        if (diff(angle, SOUTH) < 3) {
            return "south"
        }
        if (diff(angle, EAST) < 3) {
            return "west"
        }
        if (diff(angle, WEST) < 3) {
            return "east"
        }
        if (diff(angle, SOUTHEAST) < 45) {
            return "north-west"
        }
        if (diff(angle, SOUTHWEST) < 45) {
            return "north-east"
        }
        if (diff(angle, NORTHEAST) < 45) {
            return "south-west"
        }
        if (diff(angle, NORTHWEST) < 45) {
            return "south-east"
        }
        return "Dunno. $angle"
    }

    /**
     * Calculates the absolute difference between two angles.
     *
     * @param x First angle.
     * @param y Second angle.
     * @return The absolute difference between the angles.
     */
    fun diff(
        x: Double,
        y: Double,
    ): Double = abs(x - y)
}
