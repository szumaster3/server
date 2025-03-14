package core.game.world.repository

import core.api.log
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerParser
import core.game.system.task.TaskExecutor
import core.game.world.GameWorld
import core.tools.Log

class DisconnectionQueue {
    private val queue = HashMap<String, DisconnectionEntry?>()
    private val queueTimers = HashMap<String, Int>()

    fun update() {
        if (queue.isEmpty() || GameWorld.ticks % 3 != 0) {
            return
        }
        val entries = ArrayList(queue.entries)

        entries.forEach {
            if (finish(it.value, false)) {
                queue.remove(it.key)
            } else {
                queueTimers[it.key] = (queueTimers[it.key] ?: 0) + 3
                if ((queueTimers[it.key] ?: Int.MAX_VALUE) >= 1500) {
                    it.value?.player?.let { player ->
                        player.finishClear()
                        Repository.removePlayer(player)
                        remove(it.key)
                        log(
                            this::class.java,
                            Log.WARN,
                            "Force-clearing ${it.key} after 15 minutes of being in the disconnection queue!",
                        )
                    }
                }
            }
        }
    }

    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    private fun finish(
        entry: DisconnectionEntry?,
        force: Boolean,
    ): Boolean {
        val player = entry!!.player
        if (!force && !player.allowRemoval()) {
            return false
        }
        player.packetDispatch.sendLogout()
        player.finishClear()
        Repository.removePlayer(player)
        try {
            if (player.communication.clan != null) {
                player.communication.clan.leave(player, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (player.isArtificial) {
            return true
        }
        if (!force) {
            TaskExecutor.executeSQL {
                Thread.currentThread().name = "PlayerSave SQL"
                save(player, true)
            }
            log(this::class.java, Log.INFO, "Player cleared. Removed ${player.details.username}.")
            return true
        }
        save(player, false)
        log(this::class.java, Log.INFO, "Player cleared. Removed ${player.details.username}.")
        return true
    }

    operator fun get(name: String?): Player? {
        val entry = queue[name]
        return entry?.player
    }

    fun clear() {
        for (entry in queue.values.toTypedArray()) {
            finish(entry, true)
        }
        queue.clear()
    }

    @JvmOverloads
    fun add(
        player: Player,
        clear: Boolean = false,
    ) {
        if (queue[player.name] != null) return
        queue[player.name] = DisconnectionEntry(player, clear)
        log(this::class.java, Log.INFO, "Queueing ${player.name} for disconnection.")
    }

    operator fun contains(name: String?): Boolean {
        return queue.containsKey(name)
    }

    fun remove(name: String?) {
        queue.remove(name)
        queueTimers.remove(name)
    }

    internal data class DisconnectionEntry(
        val player: Player,
        var isClear: Boolean,
    )

    fun save(
        player: Player,
        sql: Boolean,
    ): Boolean {
        try {
            PlayerParser.saveImmediately(player)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return false
    }
}
