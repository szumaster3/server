package core.game.system

import core.ServerConstants
import core.game.node.entity.player.Player
import core.game.system.SystemManager.flag
import core.game.system.SystemManager.terminator
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.majorUpdateWorker
import core.game.world.repository.Repository.players
import java.util.concurrent.Executors

class SystemUpdate : Pulse(DEFAULT_COUNTDOWN) {
    var isCreateBackup = false

    override fun pulse(): Boolean {
        if (delay >= BACKUP_TICK && isCreateBackup) {
            try {
                terminator.save(ServerConstants.DATA_PATH!!)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            delay = BACKUP_TICK - 1
            return false
        }
        flag(SystemState.TERMINATED)
        return true
    }

    fun notifyPlayers() {
        try {
            val time = delay + if (isCreateBackup) BACKUP_TICK else 0
            val it: Iterator<Player> = players.iterator()
            while (it.hasNext()) {
                val p = it.next()
                if (p != null) {
                    p.packetDispatch.sendSystemUpdate(time)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun schedule() {
        super.setTicksPassed(0)
        super.start()
        if (majorUpdateWorker.started) {
            notifyPlayers()
            Pulser.submit(this)
            return
        }
        Executors.newSingleThreadExecutor().submit {
            while (isRunning) {
                try {
                    Thread.sleep(600)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (update()) {
                    break
                }
            }
        }
    }

    fun setCountdown(ticks: Int) {
        var ticks = ticks
        if (isCreateBackup) {
            if (ticks < BACKUP_TICK) {
                ticks = BACKUP_TICK
            }
            ticks -= BACKUP_TICK
        }
        super.setDelay(ticks)
    }

    fun cancel() {
        super.stop()
        flag(SystemState.ACTIVE)
    }

    companion object {
        const val DEFAULT_COUNTDOWN = 100

        const val BACKUP_TICK = 10
    }
}
