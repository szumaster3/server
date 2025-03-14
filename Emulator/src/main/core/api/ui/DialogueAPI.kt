package core.api.ui

import core.game.component.Component
import core.game.dialogue.splitLines
import core.game.node.entity.player.Player

/**
 * Closes the current dialogue and chatbox for the player.
 *
 * @param player The player whose dialogue and chatbox are being closed.
 */
fun closeDialogue(player: Player) {
    player.dialogueInterpreter.close()
    player.interfaceManager.closeChatbox()
}

/**
 * Sends a tutorial dialogue to the player and prevents the dialogue window from being closed.
 *
 * @param player The player to whom the tutorial dialogue is sent.
 * @param message The set of messages to display as the tutorial dialogue.
 */
fun sendTutorialDialogue(
    player: Player,
    vararg message: String,
) {
    Component.setUnclosable(
        player,
        player.dialogueInterpreter.sendDialogues(*message),
    )
}

/**
 * Sends a dialogue message to the player with a specified delay in ticks.
 *
 * @param player The player to whom the dialogue message is sent.
 * @param message The dialogue message to display.
 * @param ticks The number of game ticks to wait before displaying the dialogue message.
 */
fun sendDialogueWithDelay(
    player: Player,
    message: String,
    ticks: Int,
) {
    player.dialogueInterpreter.sendDialogue(ticks, *splitLines(message))
}

private class DialogueAPI
