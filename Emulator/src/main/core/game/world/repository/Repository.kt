package core.game.world.repository

import content.region.wilderness.handlers.revs.RevenantsNPC
import core.ServerConstants
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.UpdateSequence
import java.util.concurrent.CopyOnWriteArrayList

object Repository {
    @JvmStatic
    val players = NodeList<Player>(ServerConstants.MAX_PLAYERS)
    val uid_map = HashMap<Int, Player>(ServerConstants.MAX_PLAYERS)

    @JvmStatic
    val npcs = NodeList<NPC>(ServerConstants.MAX_NPCS)

    val LOGGED_IN_PLAYERS: MutableList<String> = ArrayList(ServerConstants.MAX_PLAYERS)

    val RENDERABLE_NPCS: MutableList<NPC> = CopyOnWriteArrayList()

    @JvmStatic
    val playerNames: MutableMap<String, Player> = HashMap()

    @JvmStatic
    val lobbyPlayers: MutableList<Player> = ArrayList()

    @JvmStatic
    val disconnectionQueue = DisconnectionQueue()

    @JvmOverloads
    fun sendMarketUpdate(
        string: String,
        icon: Int = 12,
        color: String = "<col=CC6600>",
    ) {
        val players: Array<Any> = playerNames.values.toTypedArray()
        val size = players.size
        for (i in 0 until size) {
            val player = players[i] as Player
            player.sendMessage("<img=" + icon + ">" + color + "Market Update: " + string)
        }
    }

    @JvmStatic
    fun sendNews(
        string: String,
        icon: Int = 12,
        color: String = "CC6600",
    ) {
        val players: Array<Any> = playerNames.values.toTypedArray()
        val size = players.size
        for (i in 0 until size) {
            val player = players[i] as Player
            player.sendMessage("<img=$icon><col=$color>News: $string")
        }
    }

    @JvmStatic
    @Deprecated("Old and bad", ReplaceWith("sendNews()"), DeprecationLevel.WARNING)
    fun sendNews(string: String) {
        sendNews(string, 12)
    }

    @JvmStatic
    fun addRenderableNPC(npc: NPC) {
        if (npc is RevenantsNPC) return // hack to make sure we can update revenants every tick.
        if (!RENDERABLE_NPCS.contains(npc)) {
            RENDERABLE_NPCS.add(npc)
            npc.isRenderable = true
        }
    }

    @JvmStatic
    fun removeRenderableNPC(npc: NPC) {
        if (npc is RevenantsNPC) return // hack to make sure we can update revenants every tick.
        RENDERABLE_NPCS.remove(npc)
        npc.isRenderable = false
    }

    @JvmStatic
    fun findNPC(l: Location): NPC? {
        for (n in RegionManager.getRegionPlane(l).npcs) {
            if (n.location == l) {
                return n
            }
        }
        return null
    }

    @JvmStatic
    fun addPlayer(player: Player) {
        if (players.isNotEmpty()) {
            for (i in 1 until players.size) {
                players[i] ?: continue
                if (players[i].details.uid == player.details.uid) {
                    val oldPl = players[i]
                    players.remove(oldPl)
                    oldPl.clear()
                    break
                }
            }
        }
        players.add(player)
        uid_map[player.details.uid] = player
        playerNames[player.name] = player
    }

    @JvmStatic
    fun removePlayer(player: Player) {
        players.remove(player)
        uid_map.remove(player.details.uid)
        playerNames.remove(player.name)
        UpdateSequence.rendererPlayers.remove(player)
        player.session.disconnect()
    }

    @JvmStatic
    fun findNPC(npcId: Int): NPC? {
        for (npc in npcs) {
            if (npc == null) {
                continue
            }
            if (npc.id == npcId) {
                return npc
            }
        }
        return null
    }

    @JvmStatic
    fun getPlayerByName(name: String?): Player? {
        return if (name == null) {
            null
        } else {
            playerNames[name.lowercase().replace(" ".toRegex(), "_")]
        }
    }

    @JvmStatic
    fun getPlayerByUid(uid: Int): Player? {
        return uid_map[uid]
    }

    @JvmStatic
    val renderableNpcs: List<NPC>
        get() = RENDERABLE_NPCS
}
