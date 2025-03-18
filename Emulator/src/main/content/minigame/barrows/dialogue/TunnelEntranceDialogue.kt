package content.minigame.barrows.dialogue

import core.api.getVarp
import core.api.sendPlainDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable

@Initializable
class TunnelEntranceDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var index = 0

    override fun open(vararg args: Any): Boolean {
        index = args[0] as Int
        sendPlainDialogue(player, false, "You find a hidden tunnel, do you want to enter?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (stage++ == 0) {
            options("Yeah, I'm fearless!", "No way, that looks scary!")
            return true
        }
        when (buttonId) {
            1 -> {
                var offsetX = 0
                var offsetY = 0
                val configValue = getVarp(player, 452)
                if (configValue and (1 shl 7) != 0) {
                    offsetX = 34
                    offsetY = 34
                } else if (configValue and (1 shl 6) != 0) {
                    offsetY = 34
                } else if (configValue and (1 shl 9) != 0) {
                    offsetX = 34
                }
                val x = 3534 + offsetX
                val y = 9677 + offsetY
                player.properties.teleportLocation = Location.create(x, y, 0)
                end()
                return true
            }

            2 -> {
                end()
                return true
            }
        }
        return false
    }

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("barrow_tunnel"))
}
