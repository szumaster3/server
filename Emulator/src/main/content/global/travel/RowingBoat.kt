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
    fun sail(
        player: Player,
        npc: NPC,
    ): Boolean {
        lock(player, 1000)
        lockInteractions(player, 1000)
        GameWorld.Pulser.submit(
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            if (npc.id == NPCs.KATHY_CORKAT_3831) {
                                sendPlainDialogue(player, true, "", "", "Kathy Corkat rows you up the river...", "")
                            } else {
                                sendPlainDialogue(
                                    player,
                                    true,
                                    "",
                                    "",
                                    "Kathy Corkat rows you down the river to the sea...",
                                    "",
                                )
                            }
                            openInterface(player, Components.FADE_TO_BLACK_120)
                        }

                        4 -> {
                            teleport(
                                player,
                                if (npc.id ==
                                    NPCs.KATHY_CORKAT_3831
                                ) {
                                    Location(2369, 3484, 0)
                                } else {
                                    Location(2357, 3641, 0)
                                },
                                TeleportManager.TeleportType.INSTANT,
                            )
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
            },
        )
        return true
    }
}
