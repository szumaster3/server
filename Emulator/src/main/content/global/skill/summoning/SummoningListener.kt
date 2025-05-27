package content.global.skill.summoning

import content.global.skill.summoning.familiar.npc.IbisNPC
import core.api.finishDiaryTask
import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Quests

class SummoningListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles summon the familiar from pouch item.
         */

        on(summoningPouchID, IntType.ITEM, "summon") { player, node ->
            if (!isQuestComplete(player, Quests.WOLF_WHISTLE) && player.getAttribute<Any?>("in-cutscene", null) == null) {
                sendMessage(player, "You have to complete Wolf Whistle before you can summon a familiar.")
                return@on true
            }

            player.familiarManager.summon(node.asItem(), false)

            val familiar = player.familiarManager.familiar
            val faladorZone = ZoneBorders(3011, 3222, 3017, 3229, 0).insideBorder(player) || ZoneBorders(3011, 3220, 3015, 3221, 0).insideBorder(player)
            if (player.familiarManager.hasFamiliar() && familiar is IbisNPC && faladorZone) {
                finishDiaryTask(player, DiaryType.FALADOR, 2, 9)
            }
            return@on true
        }
    }

    companion object {
        val summoningPouchID = SummoningPouch.values().map { it.pouchId }.toIntArray()
    }
}