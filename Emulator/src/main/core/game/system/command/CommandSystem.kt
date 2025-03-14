package core.game.system.command

import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.tools.colorize

class CommandSystem {
    fun parse(
        player: Player?,
        message: String,
    ): Boolean {
        player ?: return false
        val arguments = message.split(" ").toTypedArray()
        val command = CommandMapping.get(arguments[0])

        if (command == null && (player.rights != Rights.REGULAR_PLAYER)) {
            for (set in CommandSet.values()) {
                if (set.interpret(player, arguments[0], *arguments)) {
                    player.sendMessage("-->${arguments[0]}: Deprecated command")
                    return true
                }
            }
            player.sendMessage(colorize("-->%R${arguments[0]}: command not found."))
        } else {
            try {
                command?.attemptHandling(player, arguments)
            } catch (e: IllegalStateException) {
                return true
            }
        }
        return false
    }

    companion object {
        val commandSystem = CommandSystem()
    }
}
