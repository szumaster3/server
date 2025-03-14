package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.lock
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery

class LumberyardFenceShortcut : InteractionListener {
    private val squeezeUnderAnimation = Animation.create(Animations.LUMBER_YARD_ENTER_9221)
    private val brokenFence = Scenery.FENCE_31149

    override fun defineListeners() {
        on(brokenFence, IntType.SCENERY, "squeeze-under") { player, _ ->
            lock(player, 1)
            AgilityHandler.forceWalk(
                player,
                0,
                player.location,
                player.location.transform(if (player.location.x < 3296) Direction.EAST else Direction.WEST, 1),
                squeezeUnderAnimation,
                15,
                0.0,
                null,
            )
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(brokenFence), "squeeze-under") { player, node ->
            if (player.location.x > 3295) {
                return@setDest Location.create(3296, 3498, 0)
            } else {
                Location.create(
                    3295,
                    3498,
                    0,
                )
            }
        }
    }
}
