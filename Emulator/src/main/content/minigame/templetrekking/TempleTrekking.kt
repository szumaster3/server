package content.minigame.templetrekking

import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import org.rs.consts.Quests

object TempleTrekking {
    fun hasRequirements(player: Player?): Boolean {
        if (player != null) {
            if (!isQuestComplete(player, Quests.PRIEST_IN_PERIL)) return false
            if (!isQuestComplete(player, Quests.NATURE_SPIRIT)) return false
            if (!hasRequirement(player, Quests.IN_SEARCH_OF_THE_MYREQUE, false)) return false
            if (!hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE, false)) return false
            if (!hasRequirement(player, Quests.DARKNESS_OF_HALLOWVALE, false)) return false
            if (!hasRequirement(player, Quests.LEGACY_OF_SEERGAZE, false)) return false
        }
        return true
    }

    val tentacleCombatEventRegion = 10318
    val logout = "temple-trekking-logout"
    val baseLocation = "temple-trekking:base_location"
    val templeTrekkingCombatSession = "temple-trekking:combat_session"
}
