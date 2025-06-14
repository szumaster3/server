package content.minigame.bountyhunter.plugin

import core.api.removeAttribute
import core.game.node.entity.player.Player
import core.game.world.GameWorld

class BountyEntry {
    var target: Player? = null

    var hunter: Player? = null

    fun update(player: Player) {
        var name: String? = "No one"
        if (target != null) {
            name = target!!.username
        }
        player.packetDispatch.sendString(name, 653, 7)
        updatePenalty(player, false)
    }

    fun updatePenalty(
        player: Player,
        unlock: Boolean,
    ) {
        var penalty = player.getAttribute("pickup_penalty", 0)
        var child = -1
        if (GameWorld.ticks > penalty) {
            removeAttribute(player, "pickup_penalty")
            player.packetDispatch.sendInterfaceConfig(653, 8, true)
        } else if (penalty != 0) {
            child = 8
            val seconds = Math.round((penalty - GameWorld.ticks) * 0.6).toInt()
            player.packetDispatch.sendString("$seconds Sec", 653, 10)
        }
        penalty = player.getAttribute("exit_penalty", 0)
        if (GameWorld.ticks > penalty) {
            removeAttribute(player, "exit_penalty")
            player.packetDispatch.sendInterfaceConfig(653, 11, true)
        } else if (penalty != 0) {
            child = 11
            val seconds = Math.round((penalty - GameWorld.ticks) * 0.6).toInt()
            player.packetDispatch.sendString("$seconds Sec", 653, 13)
        }
        if (unlock && child > -1) {
            player.packetDispatch.sendInterfaceConfig(653, child, false)
        }
    }
}
