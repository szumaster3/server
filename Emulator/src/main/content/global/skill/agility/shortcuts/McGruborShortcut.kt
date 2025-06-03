package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery

class McGruborShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.LOOSE_RAILING_51, IntType.SCENERY, "squeeze-through") { player, _ ->
            val direction = if (player.location.x < 2662) Direction.EAST else Direction.WEST
            val destination = player.location.transform(direction, 1)

            AgilityHandler.forceWalk(
                player,
                -1,
                player.location,
                destination,
                Animation(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844),
                5,
                0.0,
                "You squeeze through the loose railing.",
                1
            )

            return@on true
        }
    }
}
