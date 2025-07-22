package core.game.system.command

import core.ServerConstants
import core.game.node.entity.player.Player
import core.game.world.GameWorld

/**
 * Represents a game command with associated metadata and handler logic.
 *
 * @property name The command name.
 * @property privilege The minimum privilege required to use the command.
 * @property usage The usage instructions for the command.
 * @property description The description of the command.
 * @property handle The function to execute when the command is invoked.
 */
class Command(
    val name: String,
    val privilege: Privilege,
    val usage: String = "UNDOCUMENTED",
    val description: String = "UNDOCUMENTED",
    val handle: (Player, Array<String>) -> Unit
) {

    /**
     * Attempts to execute the command for the given player with provided arguments.
     *
     * @param player The player issuing the command.
     * @param args The arguments passed to the command.
     */
    fun attemptHandling(player: Player, args: Array<String>?) {
        args ?: return
        if (player.rights.ordinal >= privilege.ordinal ||
            GameWorld.settings?.isDevMode == true ||
            ServerConstants.I_AM_A_CHEATER
        ) {
            handle(player, args)
        }
    }
}

/**
 * Manages registration and retrieval of game commands.
 */
object CommandMapping {
    private val mapping = hashMapOf<String, Command>()

    /**
     * Gets a command by its name or null if not found.
     */
    fun get(name: String): Command? = mapping[name]

    /**
     * Registers a new command.
     */
    fun register(command: Command) {
        mapping[command.name] = command
    }

    /**
     * Gets all registered commands as an array.
     */
    fun getCommands(): Array<Command> = mapping.values.toTypedArray()

    /**
     * Gets all registered command names as an array.
     */
    fun getNames(): Array<String> = mapping.keys.toTypedArray()

    /**
     * Computes pagination indices for commands available to a given rights level.
     *
     * @param rights The rights level of the querying player.
     * @return An array of indices representing page start positions.
     */
    fun getPageIndices(rights: Int): IntArray {
        val list = ArrayList<Int>()
        list.add(0)

        var lineCounter = 0
        for ((index, command) in getCommands().filter { it.privilege.ordinal <= rights }.withIndex()) {
            lineCounter += 2
            if (command.usage.isNotEmpty()) lineCounter++
            if (command.description.isNotEmpty()) lineCounter++

            if (lineCounter > 306) {
                list.add(index)
                lineCounter = 0
            }
        }

        return list.toIntArray()
    }
}