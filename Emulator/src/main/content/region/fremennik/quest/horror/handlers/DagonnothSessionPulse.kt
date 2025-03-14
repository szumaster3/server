package content.region.fremennik.quest.horror.handlers

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.system.task.Pulse

class DagonnothSessionPulse(
    val player: Player,
) : Pulse() {
    var counter = 0

    override fun pulse(): Boolean {
        when (counter++) {
            0 -> sendMessage(player, "A horror from the ocean depths...")
            3 -> {
                if (player.getExtension<Any?>(DagannothSession::class.java) != null) {
                    return true
                }
                DagannothSession.create(player).start().also { return true }
            }
        }
        return false
    }
}
