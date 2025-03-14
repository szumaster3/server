package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.hasLevelDyn
import core.api.queueScript
import core.api.sendDialogue
import core.api.stopExecuting
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery

class DraynorManorShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.BROKEN_RAILING_37703, IntType.SCENERY, "squeeze-through") { player, _ ->
            if (!hasLevelDyn(player, Skills.AGILITY, 28)) {
                sendDialogue(player, "You need an agility level of at least 28 to do this.")
                return@on true
            }
            handleShortcut(player)
            return@on true
        }
    }

    private fun handleShortcut(player: Player) {
        queueScript(player, 1, QueueStrength.SOFT) {
            val direction = if (player.location.x >= 3086) Direction.WEST else Direction.EAST
            AgilityHandler.forceWalk(
                player,
                -1,
                player.location,
                player.location.transform(direction, 1),
                Animation(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844),
                5,
                0.0,
                "You squeeze through the loose railing.",
            )
            return@queueScript stopExecuting(player)
        }
    }
}
