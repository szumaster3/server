package core.game.system.timer.impl

import core.api.playAudio
import core.api.removeTimer
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag
import core.tools.colorize
import core.tools.secondsToTicks
import org.json.simple.JSONObject
import org.rs.consts.Sounds

class PoisonImmunity :
    PersistTimer(
        runInterval = 1,
        identifier = "poison:immunity",
        isSoft = true,
        flags = arrayOf(TimerFlag.ClearOnDeath),
    ) {
    var ticksRemaining = 0

    override fun save(
        root: JSONObject,
        entity: Entity,
    ) {
        root["ticksRemaining"] = ticksRemaining.toString()
    }

    override fun parse(
        root: JSONObject,
        entity: Entity,
    ) {
        ticksRemaining = root["ticksRemaining"].toString().toInt()
    }

    override fun onRegister(entity: Entity) {
        removeTimer<Poison>(entity)
    }

    override fun run(entity: Entity): Boolean {
        ticksRemaining--

        if (entity is Player && ticksRemaining == secondsToTicks(30)) {
            sendMessage(entity, colorize("%RYou have 30 seconds remaining on your poison immunity."))
            playAudio(entity, Sounds.CLOCK_TICK_1_3120, 0, 3)
        } else if (entity is Player && ticksRemaining == 0) {
            sendMessage(entity, colorize("%RYour poison immunity has expired."))
            playAudio(entity, Sounds.DRAGON_POTION_FINISHED_2607)
        }

        return ticksRemaining > 0
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val t = PoisonImmunity()
        t.ticksRemaining = args.getOrNull(0) as? Int ?: 100
        return t
    }
}
