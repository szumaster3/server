package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.queueScript
import core.api.stopExecuting
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery

class McGruborShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.LOOSE_RAILING_51, IntType.SCENERY, "squeeze-through") { player, _ ->
            handleSqueezeThrough(player)
            return@on true
        }
    }

    private fun handleSqueezeThrough(player: Player) {
        queueScript(player, 1, QueueStrength.SOFT) {
            val newDirection =
                when {
                    player.location.x < 2662 -> Direction.EAST
                    player.location.x >= 2662 -> Direction.WEST
                    else -> Direction.EAST
                }
            AgilityHandler.forceWalk(
                player,
                -1,
                player.location,
                player.location.transform(newDirection, 1),
                Animation(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844),
                5,
                0.0,
                "You squeeze through the loose railing.",
            )
            return@queueScript stopExecuting(player)
        }
    }
}
