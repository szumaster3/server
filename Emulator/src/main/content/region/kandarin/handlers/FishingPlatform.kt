package content.region.kandarin.handlers

import core.api.*
import core.api.quest.getQuestStage
import core.api.ui.setMinimapState
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Music
import org.rs.consts.Quests

object FishingPlatform {
    @JvmStatic
    fun sail(
        player: Player,
        travel: Travel,
    ) {
        Travel.values().forEach {
            setComponentVisibility(player, Components.SEASLUG_BOAT_TRAVEL_461, it.component, true)
        }
        player.lock()
        submitWorldPulse(
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> if(!player.musicPlayer.hasUnlocked(Music.THE_MOLLUSC_MENACE_200)) {
                            player.musicPlayer.unlock(Music.THE_MOLLUSC_MENACE_200)
                        }
                        3 -> {
                            setMinimapState(player, 2)
                            openInterface(player, Components.SEASLUG_BOAT_TRAVEL_461)
                            setComponentVisibility(player, Components.SEASLUG_BOAT_TRAVEL_461, travel.component, false)
                        }
                        4 -> teleport(player, travel.destinationLoc)
                        travel.ticks -> {
                            openInterface(player, Components.FADE_FROM_BLACK_170)
                            if(getQuestStage(player, Quests.SEA_SLUG) > 50) {
                                sendDialogue(player, "The boat arrives ${travel.destName}.")
                            } else {
                                sendDialogue(player, "You arrive ${travel.destName}.")
                            }
                            setMinimapState(player, 0)
                            closeInterface(player)
                            closeOverlay(player)
                            player.unlock()
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    enum class Travel(
        val destName: String,
        val destinationLoc: Location,
        val component: Int,
        val ticks: Int,
    ) {
        WITCHAVEN_TO_FISHING_PLATFORM("at the fishing platform", Location.create(2782, 3273, 0), 10, 8),
        FISHING_PLATFORM_TO_WITCHAVEN("at Witchaven", Location.create(2719, 3301, 0), 11, 12),
        FISHING_PLATFORM_TO_SMALL_ISLAND("on a small island", Location.create(2800, 3320, 0), 12, 12),
        SMALL_ISLAND_TO_FISHING_PLATFORM("at the fishing platform", Location.create(2782, 3273, 0), 13, 10),
    }
}
