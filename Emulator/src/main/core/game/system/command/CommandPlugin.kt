package core.game.system.command

import core.game.node.entity.player.Player
import core.game.world.repository.Repository
import core.plugin.Plugin

abstract class CommandPlugin : Plugin<Any?> {
    abstract fun parse(
        player: Player?,
        name: String?,
        args: Array<String?>?,
    ): Boolean

    fun validate(player: Player?): Boolean {
        return true
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any {
        return Unit
    }

    fun link(vararg sets: CommandSet) {
        for (set in sets) {
            set.plugins.add(this)
        }
    }

    companion object {
        @JvmStatic
        fun toInteger(string: String): Int {
            return try {
                string.toInt()
            } catch (exception: NumberFormatException) {
                1
            }
        }

        fun getArgumentLine(args: Array<String?>): String {
            return getArgumentLine(args, 1, args.size)
        }

        fun getArgumentLine(
            args: Array<String?>,
            offset: Int,
            length: Int,
        ): String {
            val sb = StringBuilder()
            for (i in offset until length) {
                if (i != offset) {
                    sb.append(" ")
                }
                sb.append(args[i])
            }
            return sb.toString()
        }

        @JvmStatic
        fun getTarget(
            name: String?,
            load: Boolean,
        ): Player? {
            return Repository.getPlayerByName(name)
        }

        @JvmStatic
        fun getTarget(name: String?): Player? {
            return Repository.getPlayerByName(name)
        }
    }
}
