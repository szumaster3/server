package content.region.asgarnia.falador.plugin.wkrank

import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.player.Player

object WhiteKnightRankManager {

    private const val KILL_COUNT = "/save:white_knight_kills"

    /**
     * Returns the amount of black knights killed by the player.
     */
    fun getKillCount(player: Player): Int {
        return getAttribute(player, KILL_COUNT, 0)
    }

    /**
     * Sets the black knight kill count.
     */
    private fun setKillCount(player: Player, kills: Int) {
        setAttribute(player, KILL_COUNT, kills)
    }

    /**
     * Adds kills to player black knight kill count.
     */
    fun addKills(player: Player, kills: Int) {
        val totalKills = getKillCount(player) + kills
        setKillCount(player, totalKills)
    }

    /**
     * Adds kill to player black knight kill count.
     */
    fun addKill(player: Player) {
        val kills = getKillCount(player) + 1
        setKillCount(player, kills)
    }

    /**
     * Returns the current white knight rank based on kill count.
     */
    fun getRank(player: Player): WhiteKnightRank {
        val kills = getKillCount(player)
        return WhiteKnightRank.values().reversed().find { kills >= it.requiredKillCount } ?: WhiteKnightRank.UNRANKED
    }

}