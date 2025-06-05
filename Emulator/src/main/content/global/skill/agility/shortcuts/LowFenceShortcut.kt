package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Represents the jump over low fence shortcut.
 */
@Initializable
class LowFenceShortcut : AgilityShortcut(intArrayOf(12776), 25, 0.0, "jump-over") {

    override fun run(player: Player, scenery: core.game.node.scenery.Scenery, option: String?, failed: Boolean) {
        val direction = if (player.location.x == 3473) Direction.EAST else Direction.WEST
        val destination = player.location.transform(direction, 1)

        ForceMovement.run(
            player,
            player.location,
            destination,
            Animation(Animations.JUMP_OVER_OBSTACLE_6132),
            ForceMovement.WALKING_SPEED
        )
    }

    override fun getDestination(node: Node, n: Node): Location {
        val player = node.asPlayer()
        val direction = if (player.location.x == 3473) Direction.EAST else Direction.WEST
        return player.location.transform(direction, 1)
    }
}
