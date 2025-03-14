package content.region.desert.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations

class UllekListener : InteractionListener {
    override fun defineListeners() {
        on(org.rs.consts.Scenery.STAIRS_DOWN_28481, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(3448, 9252, 1)
            return@on true
        }

        on(org.rs.consts.Scenery.EXIT_28401, IntType.SCENERY, "leave through") { player, _ ->
            player.properties.teleportLocation = Location.create(3412, 2848, 1)
            return@on true
        }

        on(org.rs.consts.Scenery.FALLEN_PILLAR_28516, IntType.SCENERY, "climb") { player, _ ->
            player.properties.teleportLocation = Location.create(3419, 2801, 0)
            return@on true
        }

        on(org.rs.consts.Scenery.FALLEN_PILLAR_28515, IntType.SCENERY, "climb") { player, _ ->
            player.properties.teleportLocation = Location.create(3419, 2803, 1)
            return@on true
        }

        on(org.rs.consts.Scenery.REEDS_28474, IntType.SCENERY, "push through") { player, node ->
            animate(player, Animations.UNKONWN_7633)
            animateScenery(node as Scenery, 7634)
            queueScript(player, animationDuration(Animation(Animations.UNKONWN_7633)), QueueStrength.SOFT) {
                val newLoc = node.location.transform(Location.getDelta(player.location, node.location))
                player.walkingQueue.reset()
                player.walkingQueue.addPath(newLoc.x, newLoc.y)

                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }
}
