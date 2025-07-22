package core.net.packet

import core.game.node.entity.player.Player

/**
 * Represents packet context.
 */
interface Context {
    val player: Player
}