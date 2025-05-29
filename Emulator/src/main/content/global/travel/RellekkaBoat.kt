package content.global.travel

import core.api.*
import core.api.ui.closeDialogue
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Scenery

@Initializable
class RellekkaBoat : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(RELLEKKA_BOAT).handlers["option:travel"] = this
        SceneryDefinition.forId(ICEBERG_BOAT).handlers["option:travel"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val location = node.location

        when (node.id) {
            RELLEKKA_BOAT -> {
                if (location != Location(2708, 3732)) return true

                when (option.lowercase()) {
                    "iceberg" -> sail(player, TravelDestination.RELLEKKA_TO_ICEBERG)
                    "travel" -> {
                        setTitle(player, 2)
                        sendDialogueOptions(
                            player,
                            "Where would you like to travel?",
                            "Iceberg",
                            "Stay here",
                        )
                        addDialogueAction(player) { _, button ->
                            when (button) {
                                1 -> sail(player, TravelDestination.RELLEKKA_TO_ICEBERG)
                                else -> closeDialogue(player)
                            }
                        }
                    }
                }
            }

            ICEBERG_BOAT -> {
                if (location == Location(2654, 3985, 1)) {
                    sail(player, TravelDestination.ICEBERG_TO_RELLEKA)
                }
            }
        }

        return true
    }

    fun sail(player: Player, destination: TravelDestination, ) {
        val animDuration = animationDuration(getAnimation(destination.shipAnim))
        lock(player, animDuration)
        lockInteractions(player, animDuration)

        sendMessage(player, "You board the longship...")
        openOverlay(player, Components.FADE_TO_BLACK_115)
        openInterface(player, Components.LARRY_BOAT_505)
        animateInterface(player, Components.LARRY_BOAT_505, 9, destination.shipAnim)

        teleport(player, destination.destinationLoc)

        submitWorldPulse(
            object : Pulse(animDuration) {
                override fun pulse(): Boolean {
                    sendMessage(player, "The ship arrives at ${destination.destName}.")
                    closeInterface(player)
                    closeOverlay(player)
                    unlock(player)
                    return true
                }
            },
        )
    }

    companion object {
        private const val RELLEKKA_BOAT = Scenery.BOAT_21176
        private const val ICEBERG_BOAT = Scenery.BOAT_21175
    }

}

enum class TravelDestination(val destName: String, val destinationLoc: Location, val shipAnim: Int, ) {
    ICEBERG_TO_RELLEKA("Rellekka", Location.create(2707, 3735, 0), 4652),
    RELLEKKA_TO_ICEBERG("Iceberg", Location.create(2659, 3988, 1), 4652),
}
