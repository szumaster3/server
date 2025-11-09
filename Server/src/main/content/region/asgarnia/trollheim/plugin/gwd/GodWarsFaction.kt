package content.region.asgarnia.trollheim.plugin.gwd

import content.data.God
import core.api.*
import core.game.node.entity.player.Player
import shared.consts.Components
import shared.consts.NPCs

/**
 * Represents a God Wars faction.
 * @property npcRange The npc id.
 * @property god The god associated with this faction.
 */
enum class GodWarsFaction(val npcRange: IntRange, private val god: God) {
    ARMADYL(NPCs.KREEARRA_6222..NPCs.AVIANSIE_6246, God.ARMADYL),
    BANDOS(NPCs.GENERAL_GRAARDOR_6260..NPCs.GOBLIN_6283, God.BANDOS),
    SARADOMIN(NPCs.COMMANDER_ZILYANA_6247..NPCs.KNIGHT_OF_SARADOMIN_6259, God.SARADOMIN),
    ZAMORAK(NPCs.KRIL_TSUTSAROTH_6203..NPCs.SPIRITUAL_MAGE_6221, God.ZAMORAK);

    companion object {
        /**
         * Gets the faction for npc id.
         * @param npcId the npc id
         * @return The [GodWarsFaction], or null if none match.
         */
        @JvmStatic
        fun forId(npcId: Int): GodWarsFaction? =
            values().firstOrNull { npcId in it.npcRange }

        /**
         * Increases (or decreases) the kill count of a player for a gwd faction.
         * @param player The player.
         * @param faction The [GodWarsFaction] to modify.
         * @param increase The amount.
         */
        @JvmStatic
        fun increaseKillCount(player: Player, faction: GodWarsFaction?, increase: Int) {
            if (faction == null) return

            val attribute = "gwd_kc_${faction.name.lowercase()}"
            val current = getAttribute(player, attribute, 0)
            val newAmount = (current + increase).coerceAtMost(4000)
            setAttribute(player, attribute, newAmount)

            val componentId = getAttribute(player, "gwd:overlay", Components.GODWARS_OVERLAY_601)
            val childBase = if (componentId == Components.GODWARS_OVERLAY_601 || componentId == 599) 6 else 7
            val displayValue = if (newAmount >= 4000) "Max" else newAmount.toString()
            sendString(player, displayValue, componentId, childBase + faction.ordinal)
        }

        /**
         * Counts how many protection items the player has for this faction.
         * @param player The player to check.
         * @return The number of protection items equipped.
         */
        @JvmStatic
        fun getProtectionItemAmount(player: Player, god: God): Int {
            var count = 0
            for (item in player.equipment.toArray()) {
                if (item != null && god.validItems.contains(item.id)) {
                    count++
                }
            }
            return count
        }
    }

    /**
     * Checks if the player has god item.
     * @param player The player to check.
     * @return true if the player has the item.
     */
    fun isProtected(player: Player): Boolean = hasGodItem(player, god)

    /**
     * Gets the [God] for [GodWarsFaction].
     * @return The [God] corresponding to this faction.
     */
    fun getGod(): God = god
}