package content.region.fremennik.handlers

import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Components

object WaterbirthTravel {
    @JvmStatic
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
        openInterface(player, Components.MISC_SHIPJOURNEY_224)
        animateInterface(player, Components.MISC_SHIPJOURNEY_224, 7, destination.shipAnim)

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

    private fun isDestinationValid(destination: TravelDestination): Boolean {
        return TravelDestination.values().contains(destination)
    }

    private fun completeJourney(
        player: Player,
        destination: TravelDestination,
    ) {
        sendMessage(player, "The ship arrives at ${destination.destName}.")
        closeInterface(player)
        closeOverlay(player)
        unlock(player)
    }

    private fun TravelDestination.animationDuration(): Int {
        return animationDuration(getAnimation(this.shipAnim))
    }
}

enum class TravelDestination(
    val destName: String,
    val destinationLoc: Location,
    val shipAnim: Int,
) {
    RELLEKA_TO_MISCELLANIA("Miscellania", Location.create(2581, 3845, 0), 1372),
    MISCELLANIA_TO_RELLEKKA("Rellekka", Location.create(2629, 3693, 0), 1373),
    RELLEKKA_TO_JATIZSO("Jatizso", Location.create(2421, 3781, 0), 5766),
    JATIZSO_TO_RELLEKKA("Rellekka", Location.create(2644, 3710, 0), 5767),
    RELLEKKA_TO_NEITIZNOT("Neitiznot", Location.create(2310, 3782, 0), 5764),
    NEITIZNOT_TO_RELLEKKA("Rellekka", Location.create(2644, 3710, 0), 5765),
    WATERBIRTH_TO_RELLEKKA("Rellekka", Location.create(2620, 3685, 0), 2345),
    RELLEKKA_TO_WATERBIRTH("Waterbirth Island", Location.create(2544, 3759, 0), 2344),
}
