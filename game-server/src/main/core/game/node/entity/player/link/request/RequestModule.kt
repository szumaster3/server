package core.game.node.entity.player.link.request

import core.game.node.entity.player.Player

/**
 * Represents a module for handling player-to-player requests.
 */
interface RequestModule {
    /**
     * Opens a request interface or interaction between two players.
     *
     * @param player The player initiating the request.
     * @param target The target player receiving the request.
     */
    fun open(
        player: Player?,
        target: Player?,
    )
}
