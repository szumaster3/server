package content.global.travel.glider

import core.api.*
import core.api.ui.setMinimapState
import core.api.utils.PlayerCamera
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import org.rs.consts.Components

class GliderPulse(
    delay: Int,
    private val player: Player,
    private val glider: Glider,
) : Pulse(delay, player) {
    private var count: Int = 0

    init {
        lock(player, 100)
    }

    override fun pulse(): Boolean {
        val crash = glider == Glider.LEMANTO_ADRA
        if (count == 1) {
            setVarp(player, 153, glider.config)
            setMinimapState(player, 2)
        } else if (count == 2 && crash) {
            PacketRepository.send(
                CameraViewPacket::class.java,
                CameraContext(player, CameraContext.CameraType.SHAKE, 4, 4, 1200, 4, 4),
            )
            sendMessage(player, "The glider almost gets blown from its path as it withstands heavy winds.")
        }
        when (count) {
            3 -> {
                openOverlay(player, Components.FADE_TO_BLACK_115)
            }

            4 -> {
                unlock(player)
                teleport(player, glider.location)
            }

            5 -> {
                if (crash) {
                    PlayerCamera(player).reset()
                    sendMessage(player, "The glider becomes uncontrollable and crashes down...")
                }
                closeOverlay(player)
                closeInterface(player)
                setMinimapState(player, 0)
                setVarp(player, 153, 0)
                if (!crash && glider == Glider.GANDIUS) {
                    finishDiaryTask(player, DiaryType.KARAMJA, 1, 11)
                }
                return true
            }
        }
        count++
        return false
    }
}
