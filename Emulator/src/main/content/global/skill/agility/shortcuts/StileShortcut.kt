package content.global.skill.agility.shortcuts

import core.api.animationCycles
import core.api.forceMove
import core.api.queueScript
import core.api.stopExecuting
import core.api.utils.Vector
import core.game.activity.ActivityManager
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Animations

class StileShortcut : InteractionListener {
    val ids = intArrayOf(993, 3730, 7527, 12982, 19222, 22302, 29460, 33842, 34776, 39508, 39509, 39510)
    private val FALCONRY_STILE = 19222

    override fun defineListeners() {
        on(ids, IntType.SCENERY, "climb-over") { p, n ->
            val direction = Vector.betweenLocs(p.location, n.location).toDirection()
            val startLoc = p.location.transform(direction, 1)
            val endLoc = p.location.transform(direction, 2)

            p.walkingQueue.reset()

            p.walkingQueue.addPath(startLoc.x, startLoc.y)
            forceMove(
                p,
                startLoc,
                endLoc,
                0,
                animationCycles(Animations.WALK_OVER_STILE_10980),
                direction,
                Animations.WALK_OVER_STILE_10980,
            )
            queueScript(p, 5, QueueStrength.SOFT) { _ ->
                val end = endLoc.transform(direction, 1)
                p.walkingQueue.reset()
                p.walkingQueue.addPath(end.x, end.y)

                if (n.id == FALCONRY_STILE) {
                    handleFalconry(p, endLoc)
                }
                return@queueScript stopExecuting(p)
            }
            return@on true
        }

        setDest(IntType.SCENERY, ids, "climb-over") { e, n ->
            return@setDest getInteractLocation(e.location, n.location, getOrientation(n.direction))
        }
    }

    companion object {
        fun getInteractLocation(
            pLoc: Location,
            sLoc: Location,
            orientation: Orientation,
        ): Location {
            return when (orientation) {
                Orientation.HORIZONTAL -> {
                    if (pLoc.x <= sLoc.x) {
                        sLoc.transform(-1, 0, 0)
                    } else {
                        sLoc.transform(2, 0, 0)
                    }
                }

                Orientation.VERTICAL -> {
                    if (pLoc.y <= sLoc.y) {
                        sLoc.transform(0, -1, 0)
                    } else {
                        sLoc.transform(0, 2, 0)
                    }
                }
            }
        }

        fun getOrientation(rotation: Direction): Orientation {
            return when (rotation) {
                Direction.EAST, Direction.WEST -> Orientation.HORIZONTAL
                else -> Orientation.VERTICAL
            }
        }

        fun handleFalconry(
            p: Player,
            endLoc: Location,
        ) {
            if (endLoc.y == 3619) {
                ActivityManager.start(p, "falconry", false)
            } else {
                ActivityManager.getActivity("falconry").leave(p, false)
            }
        }
    }

    enum class Orientation {
        HORIZONTAL,
        VERTICAL,
    }
}
