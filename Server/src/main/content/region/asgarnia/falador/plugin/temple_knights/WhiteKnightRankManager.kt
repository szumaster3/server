package content.region.asgarnia.falador.plugin.temple_knights

import core.api.getAttribute
import core.api.sendDialogue
import core.api.setAttribute
import core.game.node.entity.player.Player

/**
 * Manages the player's White Knight rank and kill count.
 */
object WhiteKnightRankManager {

    private const val KILL_COUNT = "/save:white_knight_kills"

    /**
     * Gets the number of Black Knights killed by the player.
     */
    fun getKillCount(player: Player): Int {
        return getAttribute(player, KILL_COUNT, 0)
    }

    /**
     * Update the player's kill count.
     */
    private fun setKillCount(player: Player, kills: Int) {
        setAttribute(player, KILL_COUNT, kills)
    }

    /**
     * Adds a kills to the player's kill count.
     */
    fun addToKillCount(player: Player, kills: Int) {
        val previousRank = getRank(player)
        val totalKills = getKillCount(player) + kills
        setKillCount(player, totalKills)
        val newRank = getRank(player)

        if (newRank != previousRank) {
            sendDialogue(player, "Congratulations! You are now a White Knight ${newRank.name.replace("_", " ").capitalize()}")
        }
    }

    /**
     * Adds a single kill to the player's kill count.
     */
    fun addKill(player: Player) {
        val kills = getKillCount(player) + 1
        setKillCount(player, kills)
    }

    /**
     * Gets the current White Knight rank of the player based on kill count.
     */
    fun getRank(player: Player): WhiteKnightsRank {
        val kills = getKillCount(player)
        return WhiteKnightsRank.values().reversed().find { kills >= it.killCount } ?: WhiteKnightsRank.UNRANKED
    }

}