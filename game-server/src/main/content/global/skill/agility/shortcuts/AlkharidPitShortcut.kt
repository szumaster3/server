package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Handles shortcut for climbing the pit in Al Kharid.
 */
@Initializable
class AlkharidPitShortcut : AgilityShortcut(intArrayOf(9331, 9332), 38, 0.0, "climb") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val destination = when (scenery.id) {
            9331 -> Location.create(3303, 3315, 0)
            9332 -> Location.create(3307, 3315, 0)
            else -> return
        }

        val animation = if (scenery.id == 9331) ANIMATION else SCALE

        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    destination,
                    animation,
                    13,
                    0.0,
                    null
                ).endAnimation = Animation.RESET
                return true
            }
        })
    }

    override fun getDestination(node: Node, n: Node): Location? =
        (n as? Scenery)?.takeIf { it.id == 9331 }?.location?.transform(1, 0, 0)

    companion object {
        private val ANIMATION = Animation(Animations.WALK_BACKWARDS_CLIMB_1148)
        private val SCALE = Animation(Animations.CLIMB_DOWN_B_740)
    }
}
