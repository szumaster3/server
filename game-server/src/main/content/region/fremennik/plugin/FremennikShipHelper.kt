package content.region.fremennik.plugin

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Components

/**
 * Represents all possible ship destinations and their properties.
 */
enum class Travel(val destination: String, val location: Location, val animation: Int, val component: Int? = null, val slot: Int = -1) {
    RELLEKKA_TO_MISC(      "Miscellania",       Location(2581, 3845, 0), 1372, Components.MISC_SHIPJOURNEY_224,     7),
    MISC_TO_RELLEKKA(      "Rellekka",          Location(2629, 3693, 0), 1373, Components.MISC_SHIPJOURNEY_224,     7),
    RELLEKKA_TO_JATIZSO(   "Jatizso",           Location(2421, 3781, 0), 5766, Components.MISC_SHIPJOURNEY_224,     7),
    JATIZSO_TO_RELLEKKA(   "Rellekka",          Location(2644, 3710, 0), 5767, Components.MISC_SHIPJOURNEY_224,     7),
    RELLEKKA_TO_NEITIZNOT( "Neitiznot",         Location(2310, 3782, 0), 5764, Components.MISC_SHIPJOURNEY_224,     7),
    NEITIZNOT_TO_RELLEKKA( "Rellekka",          Location(2644, 3710, 0), 5765, Components.MISC_SHIPJOURNEY_224,     7),
    WATERBIRTH_TO_RELLEKKA("Rellekka",          Location(2620, 3685, 0), 2345, Components.MISC_SHIPJOURNEY_224,     7),
    RELLEKKA_TO_WATERBIRTH("Waterbirth Island", Location(2544, 3759, 0), 2344, Components.MISC_SHIPJOURNEY_224,     7),
    PIRATES_COVE_TO_LUNAR( "Moonclan Island",   Location(2131, 3900, 2), 2344, Components.LUNAR_COVE_BOAT_MAP_431,  1),
    LUNAR_TO_PIRATES_COVE( "Pirates' Cove",     Location(2216, 3797, 2), 2345, Components.LUNAR_COVE_BOAT_MAP_431,  1),
    RELLEKKA_TO_ICEBERG(   "Iceberg",           Location(2659, 3988, 1), 4652, Components.LARRY_BOAT_505,           9),
    ICEBERG_TO_RELLEKKA(   "Rellekka",          Location(2707, 3735, 0), 4652, Components.LARRY_BOAT_505,           9),
}

/**
 * Utility for travel from Fremennik.
 */
object FremennikShipHelper {

    @JvmStatic
    fun sail(player: Player, travel: Travel) {
        val animDuration = animationDuration(Animation(travel.animation))
        lock(player, animDuration + 3)
        lockInteractions(player, animDuration)
        sendMessage(player, "You board the longship...")
        openOverlay(player, Components.FADE_TO_BLACK_115)
        travel.component?.let { openInterface(player, it) }
        if (travel.component != null && travel.slot >= 0) {
            animateInterface(player, travel.component, travel.slot, travel.animation)
        }
        teleport(player, travel.location, TeleportManager.TeleportType.INSTANT, 1)
        submitWorldPulse(object : Pulse(animDuration + 3) {
            override fun pulse(): Boolean {
                openOverlay(player, Components.FADE_FROM_BLACK_170)
                sendMessage(player, "The ship arrives at ${travel.destination}.")
                closeInterface(player)
                closeOverlay(player)
                unlock(player)
                return true
            }
        })
    }
}