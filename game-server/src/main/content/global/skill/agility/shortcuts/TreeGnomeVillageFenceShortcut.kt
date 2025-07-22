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

/**
 * Handles the gnome village shortcut.
 */
@Initializable
class TreeGnomeVillageFenceShortcut : AgilityShortcut(intArrayOf(2186), 0, 0.0, "squeeze-through") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val direction = if (player.location.y >= 3161) Direction.SOUTH else Direction.NORTH
        val start = player.location
        val destination = start.transform(direction, 1)
        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    start,
                    destination,
                    SQUEEZE_ANIMATION,
                    5,
                    0.0,
                    "You squeeze through the loose railing."
                )
                return true
            }
        })
    }

    companion object {
        private val SQUEEZE_ANIMATION = Animation.create(3844)
    }
}