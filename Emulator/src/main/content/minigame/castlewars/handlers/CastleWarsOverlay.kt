package content.minigame.castlewars.handlers

import core.api.setVarbit
import core.api.setVarp
import core.game.node.entity.player.Player

object CastleWarsOverlay {
    @JvmStatic
    fun sendLobbyUpdate(
        player: Player,
        bothTeamsHavePlayers: Boolean,
        gameStartMinutes: Int,
    ) {
        setVarp(player, 380, if (bothTeamsHavePlayers) gameStartMinutes else 0)
    }

    @JvmStatic
    fun sendGameUpdate(player: Player) {
        setVarbit(player, 143, 0)
        setVarbit(player, 145, 5)
        setVarbit(player, 153, 0)
        setVarbit(player, 155, 7)
    }
}
