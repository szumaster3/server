package core.game.world.update.flag.context

import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights

class ChatMessage(
    player: Player,
    text: String,
    effects: Int,
    numChars: Int,
) {
    var player: Player = player
        private set

    var text: String = text
        private set

    var effects = effects
        private set

    var numChars = numChars
        private set

    var chatIcon = Rights.getChatIcon(player)

    @JvmField
    var isQuickChat = false
}
