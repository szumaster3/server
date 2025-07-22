package content.global.skill.summoning

import content.global.skill.summoning.familiar.npc.IbisNPC
import core.api.*
import core.game.event.SummoningPointsRechargeEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.*

class SummoningPlugin : InteractionListener {

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

        /*
         * Handles using pouches on obelisk ids.
         */

        onUseWith(IntType.SCENERY, SummoningPouch.POUCH_IDS, *obeliskID) { player, _, _ ->
            SummoningCreator.open(player, false)
            return@onUseWith true
        }

        /*
         * Handles infuse and renew summoning points.
         */

        on(IntType.SCENERY, "infuse-pouch", "renew-points") { player, node ->
            when (getUsedOption(player)) {
                "infuse-pouch" -> {
                    SummoningCreator.open(player, true)
                    return@on true
                }

                "renew-points" -> {
                    if (!isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                        sendMessage(player, "You need to complete Wolf Whistle quest to do this.")
                        return@on true
                    }
                    if (getDynLevel(player, Skills.SUMMONING) == getStatLevel(player, Skills.SUMMONING)) {
                        sendMessage(player, "You already have full summoning points.")
                        return@on true
                    }
                    visualize(player, Animations.USE_OBELISK_8502, Graphics.GREEN_FAMILIAR_GRAPHIC_1308)
                    playAudio(player, Sounds.DREADFOWL_BOOST_4214)
                    player.getSkills().setLevel(Skills.SUMMONING, getStatLevel(player, Skills.SUMMONING))
                    player.dispatch(SummoningPointsRechargeEvent(node))
                    sendMessage(player, "You renew your summoning points.")
                    return@on true
                }
            }
            return@on true
        }
    }

    companion object {
        val summoningPouchID = SummoningPouch.values().map { it.pouchId }.toIntArray()
        val obeliskID = intArrayOf(Scenery.OBELISK_28716, Scenery.OBELISK_28719, Scenery.OBELISK_28722, Scenery.OBELISK_28725, Scenery.OBELISK_28731, Scenery.OBELISK_28734)
    }
}