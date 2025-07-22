package core.api.utils

import core.api.sendMessage
import core.api.toIntArray
import core.game.node.entity.player.Player
import core.tools.RED
import core.tools.StringUtils.formatDisplayName
import org.rs.consts.NPCs

/**
 * Represents various bosses for kill tracking.
 *
 * @property npc The array of npc ids.
 * @property bossName The display name of the boss.
 */
enum class BossKillCounter(val npc: IntArray, val bossName: String) {
    KBD(intArrayOf(NPCs.KING_BLACK_DRAGON_50), "King Black Dragon"),
    BORK(intArrayOf(NPCs.BORK_7133, NPCs.BORK_7134), "Bork"),
    DAG_SUPREME(intArrayOf(NPCs.DAGANNOTH_SUPREME_2881), "Dagannoth Supreme"),
    DAG_PRIME(intArrayOf(NPCs.DAGANNOTH_PRIME_2882), "Dagannoth Prime"),
    DAG_REX(intArrayOf(NPCs.DAGANNOTH_REX_2883), "Dagannoth Rex"),
    CHAOS_ELE(intArrayOf(NPCs.CHAOS_ELEMENTAL_3200), "Chaos Elemental"),
    MOLE(intArrayOf(NPCs.GIANT_MOLE_3340), "Giant Mole"),
    SARADOMIN(intArrayOf(NPCs.COMMANDER_ZILYANA_6247), "Commander Zilyana"),
    ZAMORAK(intArrayOf(NPCs.KRIL_TSUTSAROTH_6203), "K'ril Tsutsaroth"),
    BANDOS(intArrayOf(NPCs.GENERAL_GRAARDOR_6260), "General Graardor"),
    ARMADYL(intArrayOf(NPCs.KREEARRA_6222), "Kree'arra"),
    JAD(intArrayOf(NPCs.TZTOK_JAD_2745), "Tz-Tok Jad"),
    KQ(intArrayOf(NPCs.KALPHITE_QUEEN_1160), "Kalphite Queen"),
    CORP(intArrayOf(NPCs.CORPOREAL_BEAST_8133), "Corporeal Beast"),
    TDS((NPCs.TORMENTED_DEMON_8349..NPCs.TORMENTED_DEMON_8366).toIntArray(), "Tormented demon"),
    PHOENIX(intArrayOf(NPCs.PHOENIX_8548, NPCs.PHOENIX_8549), "Phoenix")
    ;

    companion object {
        /**
         * Finds the [BossKillCounter] corresponding to a given NPC ID.
         *
         * @param npc The NPC ID to search for.
         * @return The matching [BossKillCounter] or `null` if no match is found.
         */
        fun forNPC(npc: Int): BossKillCounter? {
            for (kc in values()) {
                if (kc.npc.contains(npc)) {
                    return kc
                }
            }
            return null
        }

        /**
         * Adds one to the kill count of a boss for the specified player and sends a message.
         *
         * @param killer The player who killed the boss.
         * @param npcId The NPC ID of the boss killed.
         */
        @JvmStatic
        fun addToKillCount(killer: Player?, npcId: Int) {
            if (killer == null) return
            val boss = forNPC(npcId) ?: return

            val globalData = killer.getSavedData().globalData
            globalData.getBossCounters()[boss.ordinal]++
            val formattedName = formatDisplayName(boss.bossName)
            val currentKillCount = globalData.getBossCounters()[boss.ordinal]
            sendMessage(killer, "Your $formattedName killcount is now: $RED$currentKillCount</col>.")
        }

        /**
         * Increments the Barrows chest loot count for the player and sends a message.
         *
         * @param player The player whose Barrows chest count is to be incremented.
         */
        fun addToBarrowsCount(player: Player?) {
            if (player == null) return

            val globalData = player.getSavedData().globalData
            globalData.setBarrowsLoots(globalData.getBarrowsLoots() + 1)
            val barrowsLoots = globalData.getBarrowsLoots()
            sendMessage(player, "Your Barrows chest count is: $RED$barrowsLoots</col>.")
        }
    }
}
