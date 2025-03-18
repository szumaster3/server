package content.global.travel

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.DialogueFile
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
    companion object {
        private const val RELLEKKA_BOAT = Scenery.BOAT_21176
        private const val ICEBERG_BOAT = Scenery.BOAT_21175
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(RELLEKKA_BOAT).handlers["option:travel"] = this
        SceneryDefinition.forId(ICEBERG_BOAT).handlers["option:travel"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (node.id) {
            RELLEKKA_BOAT -> handleEastCrabsBoat(player, option, node)
            ICEBERG_BOAT -> handleIcebergBoat(player, node)
        }
        return true
    }

    private fun handleEastCrabsBoat(
        player: Player,
        option: String,
        node: Node,
    ) {
        if (node.location != Location(2708, 3732)) {
            return
        }
        when (option.lowercase()) {
            "iceberg" -> sail(player, TravelDestination.RELLEKKA_TO_ICEBERG)
            "travel" -> startTravelDialogue(player)
        }
    }

    private fun handleIcebergBoat(
        player: Player,
        node: Node,
    ) {
        if (node.location == Location(2654, 3985, 1)) {
            sail(player, TravelDestination.ICEBERG_TO_RELLEKA)
        }
    }

    private fun startTravelDialogue(player: Player) {
        openDialogue(
            player,
            object : DialogueFile() {
                override fun handle(
                    componentID: Int,
                    buttonID: Int,
                ) {
                    when (stage) {
                        0 -> {
                            setTitle(player, 2)
                            sendDialogueOptions(
                                player,
                                "Where would you like to travel?",
                                "Iceberg",
                                "Stay here",
                            ).also { stage++ }
                        }

                        1 ->
                            when (buttonID) {
                                1 ->
                                    end().also {
                                        sail(player, TravelDestination.RELLEKKA_TO_ICEBERG)
                                    }
                                2 -> end()
                            }
                    }
                }
            },
        )
    }

    fun sail(
        player: Player,
        destination: TravelDestination,
    ) {
        if (!isDestinationValid(destination)) {
            return
        }

        lock(player, destination.animationDuration())
        lockInteractions(player, destination.shipAnim)

        sendMessage(player, "You board the longship...")
        openOverlay(player, Components.FADE_TO_BLACK_115)
        openInterface(player, Components.LARRY_BOAT_505)
        animateInterface(player, Components.LARRY_BOAT_505, 9, destination.shipAnim)

        teleport(player, destination.destinationLoc)

        submitWorldPulse(
            object : Pulse(destination.animationDuration() + 3) {
                override fun pulse(): Boolean {
                    completeJourney(player, destination)
                    return true
                }
            },
        )
    }

    private fun isDestinationValid(destination: TravelDestination): Boolean = TravelDestination.values().contains(destination)

    private fun completeJourney(
        player: Player,
        destination: TravelDestination,
    ) {
        sendMessage(player, "The ship arrives at ${destination.destName}.")
        closeInterface(player)
        closeOverlay(player)
        unlock(player)
    }

    private fun TravelDestination.animationDuration(): Int = animationDuration(getAnimation(this.shipAnim))
}

enum class TravelDestination(
    val destName: String,
    val destinationLoc: Location,
    val shipAnim: Int,
) {
    ICEBERG_TO_RELLEKA("Rellekka", Location.create(2707, 3735, 0), 4652),
    RELLEKKA_TO_ICEBERG("Iceberg", Location.create(2659, 3988, 1), 4652),
}
