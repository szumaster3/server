package content.global.skill.thieving.blackjack

import shared.consts.NPCs

enum class BlackjackDefinition(val npcIds: IntArray, val thievingLevel: Int, val xp: Double) {
    VILLAGERS(intArrayOf(NPCs.VILLAGER_1889, NPCs.VILLAGER_1890, NPCs.VILLAGER_1893, NPCs.VILLAGER_1894, NPCs.VILLAGER_1897, NPCs.VILLAGER_1898), 30, 65.0),
    BANDIT(intArrayOf(NPCs.BANDIT_1881, NPCs.BANDIT_6388), 45, 65.0),
    MENAPHITE_THUG(intArrayOf(NPCs.MENAPHITE_THUG_1905), 65, 137.5);

    companion object {
        fun forId(id: Int) =
            values().firstOrNull { id in it.npcIds }
    }
}