package content.region.fremennik.plugin

import content.data.GameAttributes
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Components

/**
 * Travel destinations available in Fremennik.
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
        val delay = 3
        val animDuration = Animation(travel.animation).duration
        setAttribute(player, GameAttributes.ORIGINAL_LOCATION, player.location)

        lock(player, 2 * animDuration)
        lockInteractions(player, 2 * animDuration)

        openOverlay(player, Components.FADE_TO_BLACK_115)
        sendMessage(player, "You board the longship...")

        registerLogoutListener(player, "fremennik-travel") { p ->
            p.location = getAttribute(p, GameAttributes.ORIGINAL_LOCATION, player.location)
        }

        travel.component
            ?.takeIf { travel.slot >= 0 }
            ?.let {
                openInterface(player, it)
                animateInterface(player, it, travel.slot, travel.animation)
            }

        teleport(player, travel.location, TeleportManager.TeleportType.INSTANT, 1)

        queueScript(player, animDuration + delay, QueueStrength.SOFT) {
            openOverlay(player, Components.FADE_FROM_BLACK_170)
            sendMessage(player, "The ship arrives at ${travel.destination}.")
            clearLogoutListener(player, "fremennik-travel")
            closeInterface(player)
            closeOverlay(player)
            unlock(player)
            return@queueScript stopExecuting(player)
        }
    }
}