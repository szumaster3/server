package core.game.node.entity.player.link.request

import core.game.node.entity.player.Player

interface RequestModule {
    fun open(
        player: Player?,
        target: Player?,
    )
}
