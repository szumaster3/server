package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

@Initializable
class TreeGnomeVillageFenceShortcut : AgilityShortcut(intArrayOf(2186), 0, 0.0, "squeeze-through") {

    private val SQUEEZE_ANIMATION = Animation.create(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844)
    private val FORCE_MOVE_ANIM = ForceMovement.WALK_ANIMATION

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val direction = if (player.location.y >= 3161) Direction.SOUTH else Direction.NORTH
        val start = player.location
        val destination = start.transform(direction, 1)

        player.locks.lockComponent(4)

        ForceMovement.run(
            player,
            start,
            destination,
            FORCE_MOVE_ANIM,
            SQUEEZE_ANIMATION,
            ForceMovement.direction(start, destination),
            ForceMovement.WALKING_SPEED,
            ForceMovement.WALKING_SPEED,
            false
        )

        Pulser.submit(object : Pulse(1, player) {
            var moved = false

            override fun pulse(): Boolean {
                if (!moved) {
                    if (player.location == destination) {
                        sendMessage(player, "You squeeze through the loose railing.")
                        unlock(player)
                        return true
                    }
                    return false
                }
                return true
            }
        })
    }

    override fun getDestination(node: Node, n: Node): Location? {
        val direction = if (n.location.y >= 3161) Direction.SOUTH else Direction.NORTH
        return n.location.transform(direction, 1)
    }
}
