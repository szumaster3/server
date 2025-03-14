package content.global.travel.charter

import core.api.*
import core.api.ui.setMinimapState
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.tools.StringUtils
import org.rs.consts.Components

class CharterPulse(
    private val player: Player,
    private val charter: Charter,
) : Pulse(1) {
    private var counter = 0

    override fun pulse(): Boolean {
        when (counter++) {
            0 -> prepare()
            1 ->
                if (charter != Charter.PORT_SARIM_TO_CRANDOR) {
                    player.properties.teleportLocation = charter.location
                }

            else ->
                if (counter == charter.delay) {
                    arrive()
                    return true
                }
        }
        return false
    }

    private fun arrive() {
        unlock(player)
        setVarp(player, 75, 0)
        closeInterface(player)
        setMinimapState(player, 0)
        if (charter.destination != "Crandor") {
            sendDialogue(player, "The ship arrives at " + StringUtils.formatDisplayName(charter.destination) + ".")
            closeInterface(player)
        } else {
            openInterface(player, Components.SOULBANE_DARKNESS_317)
            setMinimapState(player, 2)
            openOverlay(player, Components.DRAGON_SLAYER_QIP_CR_JOURNEY_544)
            openInterface(player, Components.SOULBANE_DARKNESS_317)
        }

        if (charter == Charter.MUSA_POINT_TO_PORT_SARIM) {
            finishDiaryTask(player, DiaryType.KARAMJA, 0, 3)
        }
        if (charter == Charter.BRIMHAVEN_TO_ARDOUGNE) {
            finishDiaryTask(player, DiaryType.KARAMJA, 0, 4)
        }
        if (charter == Charter.CAIRN_ISLAND_TO_PORT_KHAZARD) {
            finishDiaryTask(player, DiaryType.KARAMJA, 1, 6)
        }
    }

    private fun prepare() {
        lock(player, charter.delay + 1)
        openInterface(player, Components.SHIP_MAP_299)
        setMinimapState(player, 2)
        setVarp(player, 75, charter.config)
    }
}
