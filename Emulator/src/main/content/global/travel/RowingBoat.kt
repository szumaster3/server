package content.global.travel

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.NPCs

object RowingBoat {
    @JvmStatic
    fun sail(player: Player, npc: NPC): Boolean {
        player.lock()

        val isToRiver = npc.id == NPCs.KATHY_CORKAT_3831
        val dialogue = if (isToRiver)
            "Kathy Corkat rows you up the river..."
        else
            "Kathy Corkat rows you down the river to the sea..."
        val destination = if (isToRiver)
            Location(2369, 3484, 0)
        else
            Location(2357, 3641, 0)

        GameWorld.Pulser.submit(object : Pulse() {
            private var counter = 0

            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> {
                        sendPlainDialogue(player, true, dialogue)
                        openInterface(player, Components.FADE_TO_BLACK_120)
                    }
                    4 -> {
                        teleport(player, destination, TeleportManager.TeleportType.INSTANT)
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                    }
                    8 -> {
                        unlock(player)
                        closeChatBox(player)
                        openInterface(player, Components.CHATDEFAULT_137)
                        return true
                    }
                }
                return false
            }
        })

        return true
    }
}