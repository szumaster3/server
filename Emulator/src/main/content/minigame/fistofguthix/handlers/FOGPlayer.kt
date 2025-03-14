package content.minigame.fistofguthix.handlers

import core.game.node.entity.player.Player

class FOGPlayer(
    val player: Player,
    val target: FOGPlayer,
) {
    var isHunted = false
    var charges = 0

    fun switchRoles() {
        isHunted = !isHunted
    }

    fun incrementCharges(increment: Int) {
        charges += increment
    }
}
