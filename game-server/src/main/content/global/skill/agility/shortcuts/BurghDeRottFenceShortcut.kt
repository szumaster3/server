package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Handles the low fence shortcut.
 */
@Initializable
class BurghDeRottFenceShortcut : AgilityShortcut(intArrayOf(12776), 25, 0.0, "jump-over") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val direction = if (player.location.x == 3473) Direction.EAST else Direction.WEST
        val destination = player.location.transform(direction, 1)
        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    destination,
                    Animation(Animations.JUMP_OVER_OBSTACLE_6132),
                    10,
                    0.0,
                    null
                )
                return true
            }
        })
    }
}
