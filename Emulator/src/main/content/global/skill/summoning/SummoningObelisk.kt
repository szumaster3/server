package content.global.skill.summoning

import core.api.*
import core.api.quest.isQuestComplete
import core.game.event.SummoningPointsRechargeEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.Quests
import org.rs.consts.Sounds

class SummoningObelisk : InteractionListener {
    override fun defineListeners() {
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
}
