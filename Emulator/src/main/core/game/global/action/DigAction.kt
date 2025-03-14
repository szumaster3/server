package core.game.global.action

import core.game.node.entity.player.Player

interface DigAction {
    fun run(player: Player?)
}
