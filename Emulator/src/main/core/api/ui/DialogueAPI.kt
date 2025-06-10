package core.api.ui

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

private class DialogueAPI
