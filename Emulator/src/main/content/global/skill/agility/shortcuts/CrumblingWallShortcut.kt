package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable

@Initializable
class CrumblingWallShortcut : AgilityShortcut(intArrayOf(11844), 5, 0.0, "climb-over") {
    override fun run(
        player: Player,
        scenery: Scenery,
        option: String,
        failed: Boolean,
    ) {
        ForceMovement.run(
            player,
            if (player.location.x >= 2936) LOCATIONS[0] else LOCATIONS[1],
            if (player.location.x >= 2936) LOCATIONS[1] else LOCATIONS[0],
            Animation.create(839),
            10,
        )
    }

    companion object {
        private val LOCATIONS = arrayOf(Location(2936, 3355, 0), Location(2934, 3355, 0))
    }
}
