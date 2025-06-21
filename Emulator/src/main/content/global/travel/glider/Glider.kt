package content.global.travel.glider

import core.api.*
import core.api.ui.setMinimapState
import core.api.utils.PlayerCamera
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.CameraViewPacket
import org.rs.consts.Components
import org.rs.consts.NPCs

/**
 * Represents the glider transportation.
 */
enum class Glider(val button: Int, val location: Location, val config: Int, val npc: Int) {
    CRASH_ISLAND(14, Location(2894, 2726, 0), 8, NPCs.CAPTAIN_ERRDO_3811), GANDIUS(15, Location(2972, 2969, 0), 8, NPCs.CAPTAIN_KLEMFOODLE_3812),
    TA_QUIR_PRIW(16, Location(2465, 3501, 3), 9, NPCs.CAPTAIN_DALBUR_3809), SINDARPOS(17, Location(2848, 3497, 0), 1, NPCs.CAPTAIN_BLEEMADGE_3810),
    LEMANTO_ADRA(18, Location(3321, 3427, 0), 3, NPCs.CAPTAIN_ERRDO_3811), KAR_HEWO(19, Location(3278, 3212, 0), 4, NPCs.CAPTAIN_KLEMFOODLE_3812),
    LEMANTOLLY_UNDRI(20, Location(2544, 2970, 0), 10, NPCs.GNORMADIUM_AVLAFRIM_1800),
    ;

    companion object {
        /**
         * Sends glider config to the player based on NPC.
         */
        fun sendConfig(npc: NPC, player: Player, ) {
            val g = forNpc(npc.id)
            if (g != null) {
                setVarp(player, 153, g.config)
            }
        }

        /**
         * Gets glider by npc id, or null.
         */
        fun forNpc(npcId: Int): Glider? {
            for (data in values()) {
                if (data.npc == npcId) {
                    return data
                }
            }
            return null
        }

        /**
         * Gets glider by button id, or null.
         */
        fun forId(id: Int): Glider? {
            for (data in values()) {
                if (data.button == id) {
                    return data
                }
            }
            return null
        }
    }
}


/**
 * Represents the glider pulse.
 */
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
                OutgoingContext.Camera(player, OutgoingContext.CameraType.SHAKE, 4, 4, 1200, 4, 4),
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