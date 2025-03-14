package core.game.system.command.sets

import core.game.node.entity.player.Player
import core.game.system.command.Command
import core.game.system.command.CommandMapping
import core.game.system.command.Privilege
import core.plugin.Plugin
import core.tools.colorize

abstract class CommandSet(
    val defaultPrivilege: Privilege,
) : Plugin<Any?> {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        defineCommands()
        return this
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any {
        return Unit
    }

    abstract fun defineCommands()

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
    ) {
        player.sendMessage(colorize("-->$message"))
    }

    fun define(
        name: String,
        privilege: Privilege = defaultPrivilege,
        usage: String = "",
        description: String = "",
        handle: (Player, Array<String>) -> Unit,
    ) {
        CommandMapping.register(
            Command(name = name, privilege = privilege, usage = usage, description = description, handle = handle),
        )
    }
}
