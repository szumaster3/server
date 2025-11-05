package content.region.fremennik.rellekka.quest.viking.plugin

import content.data.GameAttributes
import content.region.fremennik.rellekka.quest.viking.npc.DraugenNPC
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
import core.tools.Vector3d
import shared.consts.Items
import kotlin.math.atan2

/**
 * Listener for the Hunters Talisman item used in the Fremennik Trials quest.
 */
class HunterTalismanPlugin : InteractionListener {

    private val TALISMAN = Items.HUNTERS_TALISMAN_3696

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

            val (lx, ly) = locationString.split(",").map { it.toInt() }
            val draugenLoc = Location(lx, ly)

            if (player.location?.withinDistance(draugenLoc, 5)!!) {
                sendDialogue(player, "The Draugen is nearby, be careful!")
                Pulser.submit(DraugenPulse(player))
            } else {
                val direction = draugenLoc.directionFrom(player)
                sendMessage(player, "The talisman pulls you to the $direction.")
            }
            return@on true
        }
    }

    /**
     * Pulse that delays the spawning of the Draugen NPC.
     */
    class DraugenPulse(private val player: Player) : Pulse() {
        private var count = 0

        override fun pulse(): Boolean {
            if (count++ == 3) {
                if (getAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_SPAWN, false)) return true
                sendMessage(player, "The Draugen is here! Beware!")
                DraugenNPC(player).init()
                setAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_DRAUGEN_SPAWN, true)
                return true
            }
            return false
        }
    }

    /**
     * List of possible Draugen spawn locations.
     */
    private val possibleLocations = listOf(
        Location(2625, 3608),
        Location(2602, 3628),
        Location(2668, 3714),
        Location(2711, 3602),
        Location(2664, 3592),
    )
}

/**
 * Calculates direction from an [Entity] to npc [Location].
 */
private fun Location.directionFrom(entity: Entity): String {
    val toTarget = Vector3d(this).sub(Vector3d(entity.location))
    val angle = Math.toDegrees(atan2(toTarget.y, toTarget.x))
    val normalized = (angle + 360) % 360

    val sectors = listOf(
        "east", "north-east", "north", "north-west",
        "west", "south-west", "south", "south-east"
    )
    val index = ((normalized + 22.5) / 45).toInt() % 8
    return sectors[index]
}