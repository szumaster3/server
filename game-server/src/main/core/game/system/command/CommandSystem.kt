package core.game.system.command

import core.game.node.entity.player.Player
import core.tools.colorize

/**
 * The command system responsible for parsing and executing player commands.
 */
class CommandSystem {
    /**
     * Parses a command message from the player and attempts to execute it.
     *
     * @param player The player issuing the command.
     * @param message The command message as a string.
     * @return `true` if the command was successfully handled or recognized, `false` otherwise.
     */
    fun parse(player: Player?, message: String): Boolean {
        player ?: return false
        val arguments = message.split(" ").toTypedArray()
        val command = CommandMapping.get(arguments[0])

        if (command == null) {
            for (set in CommandSet.values()) {
                if (set.interpret(player, arguments[0], *arguments)) {
                    player.sendMessage(colorize("-->%Y${arguments[0]}: Deprecated command"))
                    return true
                }
            }
            player.sendMessage(colorize("-->%R${arguments[0]}: command not found"))
        } else {
            try {
                command.attemptHandling(player, arguments)
            } catch (e: IllegalStateException) {
                return true
            }
        }
        return false
    }

    companion object {
        /**
         * Singleton instance of the command system.
         */
        val commandSystem = CommandSystem()
    }
}