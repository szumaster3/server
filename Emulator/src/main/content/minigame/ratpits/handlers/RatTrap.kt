package content.minigame.ratpits.handlers

import core.api.animateScenery
import core.api.impact
import core.api.queueScript
import core.api.sendChat
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import org.rs.consts.Scenery

class RatTrap : InteractionListener {
    val RatTrapAnimFirst = 2897
    val RatLeverAnimFirst = 2899
    val RatBarnAnimFirst = 2901
    val RatBarrelAnimFirst = 2898
    val RatBarrelAnimSecond = 2898
    val RatLeverAnimSecond = 2899
    val RatBarnAnimSecond = 2901
    val RatBarrelAnimThird = 2898

    override fun defineListeners() {
        on(Scenery.RAT_TRAP_10290, IntType.SCENERY, "take") { player, node ->
            queueScript(player, 0, QueueStrength.SOFT) {
                impact(player, 3, ImpactHandler.HitsplatType.NORMAL)
                animateScenery(node.asScenery(), RatTrapAnimFirst)
                sendChat(player, "Ouch!")
                return@queueScript true
            }
            return@on true
        }
    }
}
