package core.api

import core.game.node.entity.player.Player
import core.game.system.command.Command
import core.game.system.command.CommandMapping
import core.game.system.command.Privilege
import core.tools.Log
import core.tools.colorize

/**
 * Interface providing utility methods to define and manage player commands.
 */
interface Commands : ContentInterface {

    /**
     * Sends one or more rejection messages to the player and throws an IllegalStateException to stop further processing.
     *
     * @param player The player to send messages to.
     * @param message One or more messages to send as rejection notices.
     * @throws IllegalStateException Always thrown to halt command execution after rejection.
     */
    fun reject(
        player: Player,
        vararg message: String,
    ) {
        for (msg in message) {
            player.sendMessage(colorize("-->%R$msg"))
        }
        throw IllegalStateException()
    }

    /**
     * Sends a notification message to the player, optionally logging it to the console.
     *
     * @param player The player to notify.
     * @param message The message content.
     * @param logToConsole Whether to also log this message to the server console (default is false).
     */
    fun notify(
        player: Player,
        message: String,
        logToConsole: Boolean = false,
    ) {
        if (logToConsole) log(this::class.java, Log.DEBUG, message)
        player.sendMessage(colorize("-->$message"))
    }

    /**
     * Defines a new command with the given properties and registers it with the command system.
     *
     * @param name The command name (without prefix).
     * @param privilege The minimum privilege level required to use the command (default ADMIN).
     * @param usage Usage instructions shown to users.
     * @param description Short description of the command.
     * @param handle Lambda function invoked when the command is run, receives the player and command arguments.
     */
    fun define(
        name: String,
        privilege: Privilege = Privilege.ADMIN,
        usage: String = "",
        description: String = "",
        handle: (Player, Array<String>) -> Unit,
    ) {
        CommandMapping.register(
            Command(
                name = name,
                privilege = privilege,
                usage = usage,
                description = description,
                handle = handle,
            ),
        )
    }

    fun defineCommands()
}