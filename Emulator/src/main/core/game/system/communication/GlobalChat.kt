package core.game.system.communication

import core.api.Commands
import core.api.getAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.game.system.command.Privilege
import core.game.world.repository.Repository
import core.tools.colorize

/**
 * Handles the global chat commands and message broadcasting.
 */
class GlobalChat : Commands {

    /**
     * Defines the commands related to global chat.
     */
    override fun defineCommands() {
        define(
            name = "muteglobal",
            privilege = Privilege.ADMIN,
            usage = "",
            description = "Toggles global chat on or off.",
        ) { player, _ ->
            val original = getAttribute(player, ATTR_GLOBAL_MUTE, false)
            setAttribute(player, ATTR_GLOBAL_MUTE, !original)
            sendMessage(player, "Global chat is now ${if (original) "ON" else "OFF"}.")
            return@define
        }
    }

    companion object {
        /**
         * Attribute key to track global chat mute status per player
         */
        val ATTR_GLOBAL_MUTE = "/save:globalmute"

        /**
         * Sends a global chat message to all players who are not muted.
         *
         * @param sender The username of the message sender.
         * @param message The message content.
         * @param rights The sender's rights level (used for icon display).
         */
        fun process(
            sender: String,
            message: String,
            rights: Int,
        ) {
            val msgSD = prepare(sender, message, false, rights)
            val msgHD = prepare(sender, message, true, rights)
            for (player in Repository.players.filter { !getAttribute(it, ATTR_GLOBAL_MUTE, false) }) {
                if (player.interfaceManager.isResizable) {
                    sendMessage(player, msgHD)
                } else {
                    sendMessage(player, msgSD)
                }
            }
        }

        /**
         * Prepares the formatted chat message depending on interface type and rights.
         *
         * @param sender The username of the sender.
         * @param message The chat message.
         * @param isResizable Whether the player's interface is resizable.
         * @param rights The sender's rights level.
         * @return The formatted and colorized message string.
         */
        private fun prepare(
            sender: String,
            message: String,
            isResizable: Boolean,
            rights: Int,
        ): String {
            val baseColor = if (isResizable) "%f1b04c" else "%7512ff"
            val bracketColor = if (isResizable) "%ffffff" else "%000000"
            return colorize(
                "$bracketColor[${baseColor}G$bracketColor] ${if (rights > 0) "<img=${rights - 1}>" else ""}$sender: ${baseColor}$message",
            )
        }
    }
}