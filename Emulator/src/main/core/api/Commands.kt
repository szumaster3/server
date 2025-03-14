package core.api

import core.game.node.entity.player.Player
import core.game.system.command.Command
import core.game.system.command.CommandMapping
import core.game.system.command.Privilege
import core.tools.Log
import core.tools.colorize

interface Commands : ContentInterface {
    fun reject(
        player: Player,
        vararg message: String,
    ) {
        for (msg in message) {
            player.sendMessage(colorize("-->%R$msg"))
        }
        throw IllegalStateException()
    }

    fun notify(
        player: Player,
        message: String,
        logToConsole: Boolean = false,
    ) {
        if (logToConsole) log(this::class.java, Log.DEBUG, message)
        player.sendMessage(colorize("-->$message"))
    }

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
