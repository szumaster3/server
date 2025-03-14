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

@Initializable
class AlkharidPitShortcut : AgilityShortcut(intArrayOf(9331, 9332), 38, 0.0, "climb") {
    companion object {
        private val ANIMATION = Animation(1148)
        private val SCALE = Animation(740)
    }

    override fun run(
        player: Player,
        scenery: Scenery,
        option: String,
        failed: Boolean,
    ) {
        val destination =
            when (scenery.id) {
                9331 -> Location.create(3303, 3315, 0)
                9332 -> Location.create(3307, 3315, 0)
                else -> return
            }

        val animation = if (scenery.id == 9331) ANIMATION else SCALE
        ForceMovement.run(player, player.location, destination, animation, animation, Direction.EAST, 13).apply {
            endAnimation = Animation.RESET
        }
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        return (n as? Scenery)?.takeIf { it.id == 9331 }?.location?.transform(1, 0, 0)
    }
}
