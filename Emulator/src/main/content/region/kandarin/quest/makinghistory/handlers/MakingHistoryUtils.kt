package content.region.kandarin.quest.makinghistory.handlers

import core.api.getStatLevel
import core.api.getVarbit
import core.api.quest.isQuestComplete
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.Quests

object MakingHistoryUtils {
    const val PROGRESS = 1383
    const val ERIN_PROGRESS = 1384
    const val DRON_PROGRESS = 1385
    const val DROALAK_PROGRESS = 1386
    const val MELINA_DISAPPEARED = 1387
    const val DROALAK_DISAPPEARED = 1388

    const val ATTRIBUTE_ERIN_PROGRESS = "/save:mh-erin"
    const val ATTRIBUTE_DRON_PROGRESS = "/save:mh-dron"
    const val ATTRIBUTE_DROALAK_PROGRESS = "/save:mh-droalak"

    fun checkRequirements(player: Player): Boolean {
        return getStatLevel(player, Skills.CRAFTING) >= 24 &&
            getStatLevel(player, Skills.MINING) >= 40 &&
            getStatLevel(player, Skills.SMITHING) >= 40 &&
            isQuestComplete(player, Quests.PRIEST_IN_PERIL)
    }

    fun checkProgress(player: Player) {
        if (getVarbit(player, ERIN_PROGRESS) == 4 &&
            getVarbit(player, DROALAK_PROGRESS) in 5..6 &&
            getVarbit(player, DRON_PROGRESS) == 4
        ) {
            setVarbit(player, PROGRESS, 3, true)
        }
    }
}
