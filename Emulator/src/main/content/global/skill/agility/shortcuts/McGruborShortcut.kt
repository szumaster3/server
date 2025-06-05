package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

@Initializable
class McGruborShortcut : AgilityShortcut(intArrayOf(51), 1, 0.0, "squeeze-through") {

    override fun run(player: Player, scenery: Scenery, option: String?, failed: Boolean) {
        val direction = if (player.location.x < 2662) Direction.EAST else Direction.WEST
        val destination = player.location.transform(direction, 1)

        ForceMovement.run(
            player,
            player.location,
            destination,
            Animation(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844),
            5
        )
    }

    override fun getDestination(node: Node, n: Node): Location {
        val player = node.asPlayer()
        val direction = if (player.location.x < 2662) Direction.EAST else Direction.WEST
        return player.location.transform(direction, 1)
    }
}