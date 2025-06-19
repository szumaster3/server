package content.region.fremennik.rellekka.plugin

import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Components

/**
 * Utilities for travel between Rellekka and Waterbirth island.
 */
object RellekkaShip {

    /**
     * Initiates the ship travel.
     */
    @JvmStatic
    fun sail(player: Player, travel: TravelDestination) {
        closeAllInterfaces(player)
        val anim = animationDuration(Animation(travel.animation))
        if (!TravelDestination.values().contains(travel)) {
            return
        }
        lock(player, anim)
        lockInteractions(player, anim)
        openOverlay(player, Components.FADE_TO_BLACK_115)
        sendMessage(player, "You board the longship...")
        openInterface(player, Components.MISC_SHIPJOURNEY_224)
        animateInterface(player, Components.MISC_SHIPJOURNEY_224, 7, travel.animation)
        player.teleporter.send(travel.location)
        submitWorldPulse(
            object : Pulse(anim) {
                override fun pulse(): Boolean {
                    player.unlock()
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                    sendMessage(player, "The ship arrives at ${travel.destination}.")
                    return true
                }
            },
        )
    }
}

/**
 * Represents travel destinations.
 */
enum class TravelDestination(val destination: String, val location: Location, val animation: Int) {
    RELLEKA_TO_MISCELLANIA("Miscellania", Location.create(2581, 3845, 0), 1372),
    MISCELLANIA_TO_RELLEKKA("Rellekka", Location.create(2629, 3693, 0), 1373),
    RELLEKKA_TO_JATIZSO("Jatizso", Location.create(2421, 3781, 0), 5766),
    JATIZSO_TO_RELLEKKA("Rellekka", Location.create(2644, 3710, 0), 5767),
    RELLEKKA_TO_NEITIZNOT("Neitiznot", Location.create(2310, 3782, 0), 5764),
    NEITIZNOT_TO_RELLEKKA("Rellekka", Location.create(2644, 3710, 0), 5765),
    WATERBIRTH_TO_RELLEKKA("Rellekka", Location.create(2620, 3685, 0), 2345),
    RELLEKKA_TO_WATERBIRTH("Waterbirth Island", Location.create(2544, 3759, 0), 2344),
}
