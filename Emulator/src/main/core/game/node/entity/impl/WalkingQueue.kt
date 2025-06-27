package core.game.node.entity.impl

import core.api.hasTimerActive
import core.api.log
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.Point
import core.game.world.map.RegionManager.getRegionPlane
import core.game.world.map.RegionManager.move
import core.tools.Log
import java.util.*
import kotlin.math.abs
import kotlin.math.max

/**
 * Handles walking queue.
 */
class WalkingQueue(private val entity: Entity) {
    /**
     * The walking queue of points the entity will traverse.
     */
    val queue: Deque<Point> = ArrayDeque()

    /**
     * The direction of walking.
     */
    var walkDir: Int = -1
        private set

    /**
     * The direction of running.
     */
    var runDir: Int = -1
        private set

    /**
     * Whether the entity is currently running.
     */
    var isRunning: Boolean = false

    /**
     * Whether running is disabled.
     */
    var isRunDisabled: Boolean = false

    /**
     * The location of the entity's footprint.
     */
    var footPrint: Location

    /**
     * The list of ground items that represent the path for the player to follow.
     */
    var routeItems: ArrayList<GroundItem> = ArrayList()

    init {
        this.footPrint = entity.location
    }

    /**
     * Updates the movement of the entity based on the walking queue.
     */
    fun update() {
        val isPlayer = entity is Player
        this.walkDir = -1
        this.runDir = -1
        if (entity.location == null) {
            return
        }
        if (updateTeleport()) {
            return
        }
        if (isPlayer && updateRegion(entity.location, true)) {
            return
        }
        if (hasTimerActive(entity, "frozen")) return
        var point = queue.poll()
        val drawPath = entity.getAttribute("routedraw", false)
        if (point == null) {
            updateRunEnergy(false)
            if (isPlayer && drawPath) {
                for (item in routeItems) {
                    if (item != null) {
                        getRegionPlane(item.location).remove(item)
                    }
                }
                routeItems.clear()
            }
            return
        }
        if (isPlayer && (entity as Player).settings.runEnergy < 1.0) {
            isRunning = false
            entity.settings.isRunToggled = false
        }
        var runPoint: Point? = null
        if (point.direction == null) {
            point = queue.poll()
        }
        var walkDirection = -1
        var runDirection = -1
        if (isRunningBoth && (point == null || !point.isRunDisabled)) {
            runPoint = queue.poll()
        }
        if (point != null) {
            if (point.direction == null) {
                return
            }
            walkDirection = point.direction.ordinal
        }
        if (runPoint != null) {
            runDirection = runPoint.direction.ordinal
        }
        var diffX = 0
        var diffY = 0
        if (walkDirection != -1) {
            diffX = point!!.diffX
            diffY = point.diffY
        }
        if (runDirection != -1) {
            footPrint = entity.location.transform(diffX, diffY, 0)
            diffX += runPoint!!.diffX
            diffY += runPoint.diffY
            updateRunEnergy(true)
        } else {
            updateRunEnergy(false)
        }
        if (diffX != 0 || diffY != 0) {
            var walk = entity.location
            if (point != null) {
                walk = walk.transform(point.diffX, point.diffY, 0)
                if (!entity.zoneMonitor.move(entity.location, walk)) {
                    reset()

                    return
                }
            }
            var dest = entity.location.transform(diffX, diffY, 0)
            if (runPoint != null) {
                if (!entity.zoneMonitor.move(walk, dest)) {
                    dest = dest.transform(-runPoint.diffX, -runPoint.diffY, 0)
                    runPoint = null
                    runDirection = -1
                    reset()
                }
            }
            if (runPoint != null) {
                entity.direction = runPoint.direction
            } else if (point != null) {
                entity.direction = point.direction
            }
            footPrint = entity.location
            entity.location = dest
            move(entity)
        }
        this.walkDir = walkDirection
        this.runDir = runDirection
    }

    /**
     * Gets the rate at which energy is drained for running.
     *
     * @param player The player whose energy drain rate is being calculated.
     * @return The energy drain rate.
     */
    private fun getEnergyDrainRate(player: Player): Double {
        var rate = 0.55
        if (player.settings.weight > 0.0) {
            rate *= 1 + (player.settings.weight / 100)
        }
        if (hasTimerActive(player, "hamstrung")) {
            rate *= 4.0
        }
        return rate
    }

    /**
     * Gets the rate at which energy is restored for running, based on the agility.
     *
     * @param player The player whose energy restore rate is being calculated.
     * @return The energy restore rate.
     */
    private fun getEnergyRestore(player: Player): Double {
        val rate = 100 / ((175 - (player.getSkills().getLevel(Skills.AGILITY))) / 0.6)
        return rate
    }

    /**
     * Updates the run energy of the player, either decreasing or restoring it.
     *
     * @param decrease Whether to decrease (true) or restore (false) the energy.
     */
    fun updateRunEnergy(decrease: Boolean) {
        if (entity !is Player) {
            return
        }
        val p = entity
        if (!decrease && p.settings.runEnergy >= 100.0) {
            return
        }
        val drain = if (decrease) getEnergyDrainRate(p) else -getEnergyRestore(p)
        p.settings.updateRunEnergy(drain)
    }

    /**
     * Checks if the entity is in the process of teleporting.
     *
     * @return True if the entity is teleporting, otherwise false.
     */
    fun updateTeleport(): Boolean {
        if (entity.properties.teleportLocation != null) {
            reset(false)
            entity.location = entity.properties.teleportLocation
            entity.properties.teleportLocation = null
            if (entity is Player) {
                val p = entity
                var last = p.playerFlags.lastSceneGraph
                if (last == null) {
                    last = p.location
                }
                if ((last!!.regionX - entity.getLocation().regionX) >= 4 || (last.regionX - entity.getLocation().regionX) <= -4) {
                    p.playerFlags.isUpdateSceneGraph = true
                } else if ((last.regionY - entity.getLocation().regionY) >= 4 || (last.regionY - entity.getLocation().regionY) <= -4) {
                    p.playerFlags.isUpdateSceneGraph = true
                }
            }
            move(entity)
            footPrint = entity.location
            entity.properties.isTeleporting = true
            return true
        }
        return false
    }

    /**
     * Updates the region for the given location, checking whether a scene graph update is needed.
     *
     * @param location The location to check the region for.
     * @param move     Whether to move the entity to the new region.
     * @return True if a region update is needed, otherwise false.
     */
    fun updateRegion(location: Location, move: Boolean): Boolean {
        val p = entity as Player
        var lastRegion = p.playerFlags.lastSceneGraph
        if (lastRegion == null) {
            lastRegion = location
        }
        val rx = lastRegion.regionX
        val ry = lastRegion.regionY
        val cx = location.regionX
        val cy = location.regionY
        if ((rx - cx) >= 4) {
            p.playerFlags.isUpdateSceneGraph = true
        } else if ((rx - cx) <= -4) {
            p.playerFlags.isUpdateSceneGraph = true
        }
        if ((ry - cy) >= 4) {
            p.playerFlags.isUpdateSceneGraph = true
        } else if ((ry - cy) <= -4) {
            p.playerFlags.isUpdateSceneGraph = true
        }
        if (move && p.playerFlags.isUpdateSceneGraph) {
            move(entity)
            return true
        }
        return false
    }

    /**
     * Moves the entity back to its footprint location.
     */
    fun walkBack() {
        entity.pulseManager.clear()
        reset()
        addPath(footPrint.x, footPrint.y)
    }

    /**
     * Adds a path to the walking queue with the option to disable running.
     *
     * @param x           The x-coordinate to move to.
     * @param y           The y-coordinate to move to.
     * @param runDisabled Whether running should be disabled for this path.
     */
    @JvmOverloads
    fun addPath(x: Int, y: Int, runDisabled: Boolean = isRunDisabled) {
        val point = queue.peekLast() ?: return
        val drawRoute = entity.getAttribute("routedraw", false)
        if (drawRoute && entity is Player) {
            val p = entity
            val item = GroundItem(Item(13444), Location.create(x, y, p.location.z), p)
            routeItems.add(item)
            getRegionPlane(item.location).add(item)
        }
        var diffX = x - point.x
        var diffY = y - point.y
        val max = max(abs(diffX.toDouble()), abs(diffY.toDouble())).toInt()
        for (i in 0 until max) {
            if (diffX < 0) {
                diffX++
            } else if (diffX > 0) {
                diffX--
            }
            if (diffY < 0) {
                diffY++
            } else if (diffY > 0) {
                diffY--
            }
            addPoint(x - diffX, y - diffY, runDisabled)
        }
    }

    /**
     * Adds a point to the walking queue with the specified coordinates and run state.
     *
     * @param x           The x-coordinate of the point.
     * @param y           The y-coordinate of the point.
     * @param runDisabled Whether running should be disabled at this point.
     */
    fun addPoint(x: Int, y: Int, runDisabled: Boolean) {
        val point = queue.peekLast() ?: return
        val diffX = x - point.x
        val diffY = y - point.y
        val direction = Direction.getDirection(diffX, diffY)
        if (direction != null) {
            queue.add(Point(x, y, direction, diffX, diffY, runDisabled))
        }
    }

    val isRunningBoth: Boolean
        /**
         * Determines if the entity is running both walk and run directions.
         *
         * @return True if the entity is running both walk and run directions, otherwise false.
         */
        get() {
            if (isRunDisabled) return false
            if (entity is Player && entity.settings.isRunToggled) {
                return true
            }
            return isRunning
        }

    /**
     * Checks if the entity has a path in the walking queue.
     *
     * @return True if the walking queue has a path, otherwise false.
     */
    fun hasPath(): Boolean {
        return !queue.isEmpty()
    }

    val isMoving: Boolean
        /**
         * Checks if the entity is currently moving.
         *
         * @return True if the entity is moving, otherwise false.
         */
        get() = walkDir != -1 || runDir != -1

    /**
     * Resets the walking queue to the current location, with the option to keep running enabled.
     *
     * @param running Whether the running state should be kept enabled.
     */
    @JvmOverloads
    fun reset(running: Boolean = isRunning) {
        val loc = entity.location

        if (loc == null) {
            log(
                this.javaClass,
                Log.ERR,
                "The entity location provided was null." + "Are you sure anything down the stack trace isn't providing an NPC with a null location?"
            )
        }

        queue.clear()
        queue.add(Point(loc!!.x, loc.y))
        this.isRunning = running
    }
}
