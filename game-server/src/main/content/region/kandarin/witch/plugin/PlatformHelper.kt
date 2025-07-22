package content.region.kandarin.witch.plugin

import core.api.*
import core.api.getQuestStage
import core.api.setMinimapState
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Music
import org.rs.consts.Quests

/**
 * Handles travel between Witchaven and the Fishing Platform.
 */
object PlatformHelper {

    /**
     * Pulse travel between Witchaven and the Fishing Platform.
     */
    @JvmStatic
    fun sail(player: Player, travel: Travel) {
        openInterface(player, Components.FADE_TO_BLACK_115)

        Travel.values().forEach {
            setComponentVisibility(player, Components.SEASLUG_BOAT_TRAVEL_461, it.component, true)
        }

        if (!player.musicPlayer.hasUnlocked(Music.THE_MOLLUSC_MENACE_200)) {
            player.musicPlayer.unlock(Music.THE_MOLLUSC_MENACE_200)
        }

        player.lock()
        submitWorldPulse(object : Pulse() {
            var counter = 0

            override fun pulse(): Boolean {
                when (counter++) {
                    3 -> {
                        setMinimapState(player, 2)
                        openInterface(player, Components.SEASLUG_BOAT_TRAVEL_461)
                        setComponentVisibility(player, Components.SEASLUG_BOAT_TRAVEL_461, travel.component, false)
                    }
                    4 -> teleport(player, travel.destinationLoc)
                    travel.ticks -> {
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        val message = if (getQuestStage(player, Quests.SEA_SLUG) > 50) {
                            "The boat arrives ${travel.destName}."
                        } else {
                            "You arrive ${travel.destName}."
                        }
                        sendDialogue(player, message)
                        setMinimapState(player, 0)
                        closeInterface(player)
                        closeOverlay(player)
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
    }

    /**
     * Represents a travel route.
     */
    enum class Travel(val destName: String, val destinationLoc: Location, val component: Int, val ticks: Int) {
        WITCHAVEN_TO_FISHING_PLATFORM("at the fishing platform", Location.create(2782, 3273, 0), 10, 10),
        FISHING_PLATFORM_TO_WITCHAVEN("at Witchaven", Location.create(2719, 3301, 0), 11, 12),
        FISHING_PLATFORM_TO_SMALL_ISLAND("on a small island", Location.create(2800, 3320, 0), 12, 12),
        SMALL_ISLAND_TO_FISHING_PLATFORM("at the fishing platform", Location.create(2782, 3273, 0), 13, 10),
    }
}
