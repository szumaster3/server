package core.game.node.entity.state

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import org.json.simple.JSONObject

abstract class State(
    val player: Player? = null,
) {
    var pulse: Pulse? = null

    abstract fun save(root: JSONObject)

    abstract fun parse(_data: JSONObject)

    abstract fun newInstance(player: Player? = null): State

    abstract fun createPulse()

    fun init() {
        createPulse()
        pulse ?: return
        Pulser.submit(pulse!!)
    }
}
