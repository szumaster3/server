package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.forceMove
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.plugin.Initializable

/**
 * Handles the crumbling wall shortcut in Falador.
 */
@Initializable
class FaladorWallShortcut : AgilityShortcut(intArrayOf(11844), 5, 0.0, "climb-over") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val dir = Direction.getDirection(player.location, scenery.location)
        val destination = player.location.transform(dir, 2)
        player.lock(3)
        forceMove(
            player,
            player.location,
            destination,
            0,
            60,
            null,
            10980
        )
    }
}
