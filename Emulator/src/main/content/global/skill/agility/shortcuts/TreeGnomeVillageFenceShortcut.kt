package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Represents the squeeze through the loose railing shortcut.
 */
@Initializable
class TreeGnomeVillageFenceShortcut : AgilityShortcut(intArrayOf(2186), 0, 0.0, "squeeze-through") {

    private val SQUEEZE_ANIMATION = Animation.create(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844)

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val direction = if (player.location.y >= 3161) Direction.SOUTH else Direction.NORTH
        val start = player.location
        val destination = start.transform(direction, 1)

        AgilityHandler.forceWalk(
            player,
            -1,
            start,
            destination,
            SQUEEZE_ANIMATION,
            5,
            0.0,
            "You squeeze through the loose railing.",
        )
    }
}
