package core.game.system.timer.impl

import core.api.getWorldTicks
import core.api.hasTimerActive
import core.api.removeTimer
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag
import org.json.simple.JSONObject

class FrozenImmunity :
    PersistTimer(
        runInterval = 1,
        identifier = "frozen:immunity",
        flags = arrayOf(TimerFlag.ClearOnDeath),
    ) {
    var ticksRemaining = 0

    override fun save(
        root: JSONObject,
        entity: Entity,
    ) {
        root["ticksLeft"] = (nextExecution - getWorldTicks()).toString()
    }

    override fun parse(
        root: JSONObject,
        entity: Entity,
    ) {
        runInterval = root["ticksLeft"].toString().toInt()
    }

    override fun onRegister(entity: Entity) {
        if (hasTimerActive<Frozen>(entity)) {
            removeTimer<Frozen>(entity)
        }
        (entity as? Player)?.debug("Applied frozen immunity for $runInterval ticks.")
    }

    override fun run(entity: Entity): Boolean {
        (entity as? Player)?.debug("Removed frozen immunity")
        return false
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val inst = FrozenImmunity()
        inst.runInterval = args.getOrNull(0) as? Int ?: 7
        return inst
    }
}
