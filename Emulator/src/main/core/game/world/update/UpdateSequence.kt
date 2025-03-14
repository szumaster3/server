package core.game.world.update

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.world.map.RegionManager
import core.game.world.repository.InitializingNodeList
import core.game.world.repository.Repository
import core.net.packet.PacketRepository
import core.net.packet.context.PlayerContext
import core.net.packet.out.ClearMinimapFlag
import core.integration.grafana.Grafana

class UpdateSequence {
    // List of players in the lobby.
    var lobbyList: List<Player>? = null

    // List of players currently being rendered.
    var playersList: List<Player>? = null

    // List of NPCs currently being rendered.
    var npcList: List<NPC>? = null

    fun start() {
        // Initialize player and NPC lists from the repository.
        lobbyList = Repository.lobbyPlayers
        playersList = rendererPlayers
        npcList = Repository.renderableNpcs

        // Send clear minimap flag to each lobby player.
        lobbyList!!.map { PacketRepository.send(ClearMinimapFlag::class.java, PlayerContext(it)) }

        // Measure and update NPC ticks.
        var npcTickStart = System.currentTimeMillis()
        npcList!!.forEach(NPC::tick)
        Grafana.npcTickTime = (System.currentTimeMillis() - npcTickStart).toInt()

        // Measure and update player ticks.
        var playerTickStart = System.currentTimeMillis()
        rendererPlayers.forEach(Player::tick)
        Grafana.playerTickTime = (System.currentTimeMillis() - playerTickStart).toInt()
    }

    fun run() {
        // Measure and update the rendering state of all players.
        var playerRenderStart = System.currentTimeMillis()
        rendererPlayers.forEach(Player::update)
        Grafana.playerRenderTime = (System.currentTimeMillis() - playerRenderStart).toInt()
    }

    fun end() {
        // Reset all players and NPCs
        playersList!!.forEach(Player::reset)
        npcList!!.forEach(NPC::reset)

        // Synchronize renderer players and pulse the region and ground item managers.
        rendererPlayers.sync()
        RegionManager.pulse()
        GroundItemManager.pulse()
    }

    fun terminate() {
        // Placeholder method for terminating the update sequence.
    }

    companion object {
        @JvmStatic
        val rendererPlayers = InitializingNodeList<Player>()
    }
}
