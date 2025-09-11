package core.game.node.entity.state

import com.google.gson.JsonObject
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser

abstract class State(val player: Player? = null) {
    var pulse: Pulse? = null

    abstract fun save(root: JsonObject)

    abstract fun parse(_data: JsonObject)

    abstract fun newInstance(player: Player? = null): State

    abstract fun createPulse()

    fun init() {
        createPulse()
        pulse ?: return
        Pulser.submit(pulse!!)
    }
}
