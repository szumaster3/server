package content.region.kandarin.witchaven.plugin

import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.Music
import shared.consts.Quests

/**
 * Represents a travel routes.
 */
enum class Travel(val destName: String, val destinationLoc: Location, val component: Int, val ticks: Int) {
    WITCHAVEN_TO_FISHING_PLATFORM("at the fishing platform", Location.create(2782, 3273, 0), 10, 10),
    FISHING_PLATFORM_TO_WITCHAVEN("at Witchaven", Location.create(2719, 3301, 0), 11, 12),
    FISHING_PLATFORM_TO_SMALL_ISLAND("on a small island", Location.create(2800, 3320, 0), 12, 12),
    SMALL_ISLAND_TO_FISHING_PLATFORM("at the fishing platform", Location.create(2782, 3273, 0), 13, 10),
}

/**
 * Handles travel between Witchaven and the Fishing Platform.
 */
object FishingPlatformBoat {

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
            private var counter = 0
            private val fadeDelay = 3
            private val teleportDelay = 4

            override fun pulse(): Boolean {
                counter++
                if (counter == fadeDelay) {
                    setMinimapState(player, 2)
                    openInterface(player, Components.SEASLUG_BOAT_TRAVEL_461)
                    setComponentVisibility(player, Components.SEASLUG_BOAT_TRAVEL_461, travel.component, false)
                    return false
                }
                if (counter == teleportDelay) {
                    teleport(player, travel.destinationLoc)
                    return false
                }
                if (counter >= travel.ticks) {
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
                return false
            }
        })
    }
}