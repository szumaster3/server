package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.hasLevelDyn
import core.api.queueScript
import core.api.sendMessage
import core.api.stopExecuting
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery

class LowFenceShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.LOW_FENCE_12776, IntType.SCENERY, "jump-over") { player, _ ->
            if (!hasLevelDyn(player, Skills.AGILITY, 25)) {
                sendMessage(player, "You need an agility level of at least 25 to do this.")
                return@on true
            }

            queueScript(player, 1, QueueStrength.SOFT) {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    player.location.transform(if (player.location.x == 3473) Direction.EAST else Direction.WEST, 1),
                    Animation(Animations.JUMP_OVER_OBSTACLE_6132),
                    10,
                    0.0,
                    null,
                )
                return@queueScript stopExecuting(player)
            }

            return@on true
        }
    }
}
