package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Handles the crumbling wall shortcut.
 */
@Initializable
class FaladorWallShortcut : AgilityShortcut(intArrayOf(11844), 5, 0.0, "climb-over") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val animation = Animation.create(Animations.CLIMB_OBJECT_839)
        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    if (player.location.x >= 2936) LOCATIONS[0] else LOCATIONS[1],
                    if (player.location.x >= 2936) LOCATIONS[1] else LOCATIONS[0],
                    animation,
                    10,
                    0.0,
                    null
                )
                return true
            }
        })
    }

    companion object {
        private val LOCATIONS = arrayOf(Location(2936, 3355, 0), Location(2934, 3355, 0))
    }
}
