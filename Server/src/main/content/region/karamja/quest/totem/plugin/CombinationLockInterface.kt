package content.region.karamja.quest.totem.plugin

import content.data.GameAttributes
import core.api.*
import core.game.interaction.InterfaceListener
import shared.consts.Components

/**
 * Represents the Combination lock interface.
 *
 * # Relations
 * - [Tribal Totem quest][content.region.karamja.quest.totem.TribalTotem]
 */
class CombinationLockInterface : InterfaceListener {

    companion object {
        val LETTERS = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        )
        val LETTERS_BACK = arrayOf(17, 19, 21, 23)
        val LETTERS_FORWARD = arrayOf(18, 20, 22, 24)
        val LETTERS_POSITIONS = arrayOf(13, 14, 15, 16)
        const val DOORLOCKINTERFACE = Components.COMBI_LOCK_369
    }

    override fun defineInterfaceListeners() {
        /*
         * Handles init the letter values when interface is opened.
         */

        onOpen(DOORLOCKINTERFACE) { player, _ ->
            for (i in 1..4) setAttribute(player, "tt-letter-$i", 0)
            return@onOpen true
        }

        /*
         * Handles cleanup when the interface is closed.
         */

        onClose(DOORLOCKINTERFACE) { player, _ ->
            for (i in 1..4) removeAttribute(player, "tt-letter-$i")
            return@onClose true
        }

        /*
         * Handle button presses.
         */

        on(DOORLOCKINTERFACE) { player, _, _, buttonID, _, _ ->
            val letterIndex = when (buttonID) {
                in LETTERS_BACK -> LETTERS_BACK.indexOf(buttonID)
                in LETTERS_FORWARD -> LETTERS_FORWARD.indexOf(buttonID)
                else -> -1
            }

            if (letterIndex != -1) {
                val letterKey = "tt-letter-${letterIndex + 1}"
                val currentLetter = player.getAttribute(letterKey, 0)

                val newLetter = if (buttonID in LETTERS_BACK) {
                    (currentLetter - 1 + 26) % 26
                } else {
                    (currentLetter + 1) % 26
                }

                setAttribute(player, letterKey, newLetter)
                sendString(player, LETTERS[newLetter], DOORLOCKINTERFACE, LETTERS_POSITIONS[letterIndex])
            }

            if (buttonID == 27) { // Enter button
                val enteredCode = (1..4).joinToString("") {
                    LETTERS[player.getAttribute("tt-letter-$it", 0)]
                }
                closeInterface(player)
                if (enteredCode == "KURT") {
                    setAttribute(player, GameAttributes.QUEST_TRIBAL_TOTEM_DOORS, true)
                    sendMessage(player, "The combination seems correct!")
                } else {
                    sendMessage(player, "This combination is incorrect.")
                }
            }

            return@on true
        }
    }
}
