package core.worker

import core.Server
import core.ServerConstants
import core.ServerStore
import core.api.log
import core.api.submitWorldPulse
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.game.world.update.UpdateSequence
import core.integration.grafana.Grafana
import core.net.packet.PacketProcessor
import core.net.packet.PacketWriteQueue
import core.plugin.type.Managers
import core.tools.Log
import core.tools.NetworkReachability
import java.lang.Long.max
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * Responsible for executing the main server update loop.
 */
class MajorUpdateWorker {

    companion object {
        private const val ENABLE_DEBUG_LOGGING = false
        private const val CYCLE_TIME_NANOSECONDS = 600_000_000L
        private const val SPINWAIT_TIME_NANOSECONDS = 20_000_000L
        private const val ONE_MILLISECOND_IN_NANOSECONDS = 1_000_000L

        /**
         * Formats a Long value with commas for readability.
         */
        private fun Long.format(): String = "%,d".format(this)

        /**
         * Performs a spin-wait loop to maintain consistent tick timing.
         */
        private fun spinwait(deviation: Long, start: Long, end: Long): ElapsedCycle {
            val consumed = max(0, (end - start) + deviation)
            if (consumed >= CYCLE_TIME_NANOSECONDS) {
                return ElapsedCycle(start, end, 0, 0, 0, 0, 0, System.nanoTime())
            }

            val sleepTimeNanos = CYCLE_TIME_NANOSECONDS - consumed
            val toSleepMillis = if (sleepTimeNanos < SPINWAIT_TIME_NANOSECONDS) 0
            else (sleepTimeNanos - SPINWAIT_TIME_NANOSECONDS) / ONE_MILLISECOND_IN_NANOSECONDS

            val sleptNano = if (toSleepMillis > 0) {
                val sleepStart = System.nanoTime()
                Thread.sleep(toSleepMillis)
                val sleepEnd = System.nanoTime()
                max(0, sleepEnd - sleepStart)
            } else 0L

            val onSpinStart = System.nanoTime()
            val exit = end + sleepTimeNanos
            var spinCount = 0L

            while (true) {
                val now = System.nanoTime()
                if (now >= exit) {
                    return ElapsedCycle(
                        start, end, spinCount, now - exit, toSleepMillis, sleptNano,
                        max(0, now - onSpinStart), now
                    )
                }
                Thread.onSpinWait()
                spinCount++
            }
        }

        /**
         * Represents timing details of a single update cycle.
         */
        private data class ElapsedCycle(
            val startNs: Long,
            val endNs: Long,
            val spinwaitCount: Long,
            val deviationNs: Long,
            val requestedSleepMs: Long,
            val sleptNs: Long,
            val onSpinWaitTimeNs: Long,
            val exitNs: Long,
        ) {
            val elapsedNanoseconds: Long get() = max(0, endNs - startNs)
            val sleptNanoseconds: Long get() = max(0, sleptNs + onSpinWaitTimeNs)
            val totalNs: Long get() = max(0, exitNs - startNs)

            override fun toString(): String {
                return "ElapsedCycle(" +
                        "startNs=${startNs.format()}, " +
                        "endNs=${endNs.format()}, " +
                        "spinwaitCount=${spinwaitCount.format()}, " +
                        "deviationNs=${deviationNs.format()}, " +
                        "requestedSleepMs=${requestedSleepMs.format()}, " +
                        "sleptNs=${sleptNs.format()}, " +
                        "onSpinWaitTimeNs=${onSpinWaitTimeNs.format()}, " +
                        "exitNs=${exitNs.format()}, " +
                        "elapsedNanoseconds=${elapsedNanoseconds.format()}, " +
                        "sleptNanoseconds=${sleptNanoseconds.format()}, " +
                        "totalNs=${totalNs.format()}" +
                        ")"
            }
        }
    }

    /**
     * Check the worker loop is currently running.
     */
    var running: Boolean = false

    /**
     * Check the worker thread has started.
     */
    var started = false

    private var deviation: Long = 0

    /**
     * Sequence of updates executed each tick.
     */
    val sequence = UpdateSequence()

    /**
     * Date formatter for checking daily resets.
     */
    val sdf = SimpleDateFormat("HHmmss")

    /**
     * The main worker thread executing the update loop.
     */
    val worker = Thread {
        Thread.currentThread().name = "Major Update Worker"
        started = true
        var start = System.nanoTime()

        while (running) {
            Grafana.startTick()

            Server.heartbeat()

            if (Server.networkReachability == NetworkReachability.REACHABLE) {
                handleTickActions()
            } else {
                tickOffline()
            }

            /*
             * Disconnect inactive players.
             */

            for (player in Repository.players.filter { !it.isArtificial }) {
                if (System.currentTimeMillis() - player.session.lastPing > 20000L) {
                    player.session.lastPing = Long.MAX_VALUE
                    player.session.disconnect()
                }
                if (!player.isActive &&
                    !Repository.disconnectionQueue.contains(player.name) &&
                    player.getAttribute("logged-in-fully", false)
                ) {
                    player.session.disconnect()
                    log(
                        MajorUpdateWorker::class.java,
                        Log.WARN,
                        "Manually disconnecting ${player.name} because they were set as inactive without being disconnected. This is bad.",
                    )
                }
            }

            /*
             * Daily / weekly resets.
             */

            if (sdf.format(Date()).toInt() == 0) {
                if (GameWorld.checkDay() == 1) {
                    ServerStore.clearWeeklyEntries()
                }
                ServerStore.clearDailyEntries()
                if (ServerConstants.DAILY_RESTART) {
                    for (player in Repository.players.filter { !it.isArtificial }) {
                        player.packetDispatch.sendSystemUpdate(500)
                    }
                    ServerConstants.DAILY_RESTART = false
                    submitWorldPulse(object : Pulse(100) {
                        var counter = 0
                        override fun pulse(): Boolean {
                            counter++
                            for (player in Repository.players.filter { !it.isArtificial }) {
                                player.packetDispatch.sendSystemUpdate((5 - counter) * 100)
                            }
                            if (counter == 5) {
                                exitProcess(0)
                            }
                            return false
                        }
                    })
                }
            }

            val end = System.nanoTime()
            val elapsedCycle = spinwait(deviation, start, end)
            Grafana.totalTickTime = (elapsedCycle.totalNs / 1_000_000).toInt()

            if (ENABLE_DEBUG_LOGGING) {
                log(MajorUpdateWorker::class.java, Log.DEBUG, "Tick elapsed: $elapsedCycle")
            }

            deviation = elapsedCycle.deviationNs
            start = elapsedCycle.exitNs

            Grafana.endTick()
        }

        log(this::class.java, Log.FINE, "Update worker stopped.")
    }

    /**
     * Performs minimal ticking when offline (no network).
     */
    fun tickOffline() {
        Repository.disconnectionQueue.update()
        GameWorld.pulse()
    }

    /**
     * Handles a full server tick.
     *
     * Processes packets, updates the world, sequences, and managers.
     *
     * @param skipPulseUpdate If true, skips the world pulse update.
     */
    fun handleTickActions(skipPulseUpdate: Boolean = false) {
        try {
            val packetStart = System.currentTimeMillis()
            PacketProcessor.processQueue()
            Grafana.packetProcessTime = (System.currentTimeMillis() - packetStart).toInt()

            Repository.disconnectionQueue.update()
            if (!skipPulseUpdate) {
                GameWorld.Pulser.updateAll()
            }
            GameWorld.tickListeners.forEach { it.tick() }

            sequence.start()
            sequence.run()
            sequence.end()
            GameWorld.pulse()
            Managers.tick()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                PacketWriteQueue.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Starts the update worker thread.
     */
    fun start() {
        if (!started) {
            running = true
            worker.start()
        }
    }

    /**
     * Stops the update worker thread.
     */
    fun stop() {
        running = false
        worker.interrupt()
    }
}