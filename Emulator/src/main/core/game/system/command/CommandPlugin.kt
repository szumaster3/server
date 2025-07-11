package core.game.system.command

import core.game.node.entity.player.Player
import core.game.world.repository.Repository
import core.plugin.Plugin

/**
 * Base class for command plugins that handle specific commands.
 */
abstract class CommandPlugin : Plugin<Any?> {

    /**
     * Parses and handles a command for a player.
     *
     * @param player The player issuing the command (nullable).
     * @param name The command name (nullable).
     * @param args The command arguments (nullable).
     * @return true if the command was handled, false otherwise.
     */
    abstract fun parse(player: Player?, name: String?, args: Array<String?>?): Boolean

    /**
     * Validates whether the command can be executed by the player.
     *
     * @param player The player to validate.
     * @return true if valid, false otherwise.
     */
    fun validate(player: Player?): Boolean = true

    /**
     * Fires an event for the plugin.
     *
     * @param identifier The event identifier.
     * @param args Additional arguments for the event.
     * @return Event result (default is Unit).
     */
    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any = Unit

    /**
     * Links this plugin to one or more command sets.
     *
     * @param sets The command sets to link.
     */
    fun link(vararg sets: CommandSet) {
        for (set in sets) {
            set.plugins.add(this)
        }
    }

    companion object {
        /**
         * Converts a string to an integer.
         *
         * @param string The string to convert.
         * @return The integer value or 1 on failure.
         */
        @JvmStatic
        fun toInteger(string: String): Int =
            try {
                string.toInt()
            } catch (exception: NumberFormatException) {
                1
            }

        /**
         * Concatenates command arguments into a single string starting from index 1.
         *
         * @param args The command arguments.
         * @return The concatenated string.
         */
        fun getArgumentLine(args: Array<String?>): String = getArgumentLine(args, 1, args.size)

        /**
         * Concatenates a slice of the command arguments into a string.
         *
         * @param args The command arguments.
         * @param offset The start index.
         * @param length The end index (exclusive).
         * @return The concatenated string.
         */
        fun getArgumentLine(args: Array<String?>, offset: Int, length: Int): String {
            val sb = StringBuilder()
            for (i in offset until length) {
                if (i != offset) {
                    sb.append(" ")
                }
                sb.append(args[i])
            }
            return sb.toString()
        }

        /**
         * Gets a player by name.
         *
         * @param name The player name.
         * @param load Whether to load the player if not found.
         * @return The player or null if not found.
         */
        @JvmStatic
        fun getTarget(name: String?, load: Boolean): Player? = Repository.getPlayerByName(name)

        /**
         * Gets a player by name.
         *
         * @param name The player name.
         * @return The player or null if not found.
         */
        @JvmStatic
        fun getTarget(name: String?): Player? = Repository.getPlayerByName(name)
    }
}
