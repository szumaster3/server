package core.net.packet.`in`

import core.api.removeAttribute
import core.api.sendMessage
import core.game.dialogue.InputType
import core.game.node.entity.player.Player

/**
 * Handles an incoming script execution request packet.
 * @author vddCore
 */
object RunScript {
    /**
     * Validates and processes player input.
     *
     * @param player The input provider.
     * @param value Raw input (Int or String).
     * @param script Function to execute with processed input.
     */
    fun processInput(
        player: Player,
        value: Any,
        script: ((Any) -> Boolean),
    ) {
        if (value is Int && value <= 0) return

        val type = player.getAttribute("input-type", InputType.NUMERIC)

        var input = value

        if (player.getAttribute("parseamount", false)) {
            input = value.toString().lowercase()

            if (!input.matches(Regex("^(\\d+)(k+|m+)?$"))) {
                sendMessage(player, "That doesn't look right. Please try again.")
                return
            }

            input = input.replace("k", "000").replace("m", "000000")
        }

        if (type == InputType.NUMERIC || type == InputType.AMOUNT) {
            input = input.toString().toIntOrNull() ?: input
        }

        try {
            script(input)
        } catch (_: NumberFormatException) {
            sendMessage(player, "That number's a bit large, don't you think?")
        } catch (_: ClassCastException) {
            sendMessage(player, "Something went wrong here. Try again.")
        } finally {
            removeAttribute(player, "runscript")
            removeAttribute(player, "input-type")
        }
    }
}
