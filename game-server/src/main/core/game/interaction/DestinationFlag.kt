package core.game.interaction

import core.game.global.action.DoorActionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import kotlin.math.abs

/**
 * Handles pathfinding destination logic for different interaction types.
 */
open class DestinationFlag {

    /**
     * Gets the default destination location for a mover interacting with a node.
     *
     * @param mover The moving entity (player or NPC).
     * @param node The target node.
     * @return The destination location.
     */
    open fun getDestination(mover: Entity, node: Node): Location {
        return node.location
    }

    /**
     * Checks whether traversal is possible from a given location in the given direction.
     *
     * @param l The location to check.
     * @param dir The direction of intended movement.
     * @return True if traversal is permitted, false otherwise.
     */
    open fun checkTraversal(l: Location, dir: Direction): Boolean {
        return Direction.get((dir.toInteger() + 2) % 4).canMove(l)
    }

    /**
     * Finds the closest traversable tile to the given suggestion near the node.
     *
     * @param mover The moving entity.
     * @param node The node the mover is approaching.
     * @param suggestion The preferred destination to test from.
     * @return The closest reachable tile near the node.
     */
    fun getClosestTo(mover: Entity, node: Node, suggestion: Location): Location {
        val nl = node.location
        val diffX = suggestion.x - nl.x
        val diffY = suggestion.y - nl.y
        var moveDir = Direction.NORTH
        if (diffX < 0) {
            moveDir = Direction.EAST
        } else if (diffX >= node.size()) {
            moveDir = Direction.WEST
        } else if (diffY >= node.size()) {
            moveDir = Direction.SOUTH
        }
        var distance = 9999.9
        var destination = suggestion
        var suggest = suggestion
        for (c in 0 until 4) {
            for (i in 0..node.size()) {
                for (j in 0 until if (i == 0) 1 else 2) {
                    val current = Direction.get((moveDir.toInteger() + if (j == 1) 3 else 1) % 4)
                    val loc = suggest.transform(current.stepX * i, current.stepY * i, 0)
                    if (moveDir.toInteger() % 2 == 0) {
                        if (loc.x < nl.x || loc.x > nl.x + node.size() - 1) continue
                    } else {
                        if (loc.y < nl.y || loc.y > nl.y + node.size() - 1) continue
                    }
                    if (checkTraversal(loc, moveDir)) {
                        val dist = mover.location.getDistance(loc)
                        if (dist < distance) {
                            distance = dist
                            destination = loc
                        }
                    }
                }
            }
            moveDir = Direction.get((moveDir.toInteger() + 1) % 4)
            val offsetX = abs(moveDir.stepY * (node.size() shr 1))
            val offsetY = abs(moveDir.stepX * (node.size() shr 1))
            suggest = if (moveDir.toInteger() < 2) {
                node.location.transform(-moveDir.stepX + offsetX, -moveDir.stepY + offsetY, 0)
            } else {
                node.location.transform(
                    -moveDir.stepX * node.size() + offsetX, -moveDir.stepY * node.size() + offsetY, 0
                )
            }
        }
        return destination
    }

    companion object {
        /**
         * Default destination: simply returns the node's location.
         */
        @JvmField
        val LOCATION = DestinationFlag()

        /**
         * Destination when moving to interact with an entity (e.g. player or NPC).
         */
        @JvmField
        val ENTITY = object : DestinationFlag() {
            override fun getDestination(mover: Entity, node: Node): Location {
                var l = getClosestTo(mover, node, node.location.transform(0, -1, 0))
                if (mover.size() > 1) {
                    if (l.x < node.location.x) {
                        l = l.transform(-(mover.size() - 1), 0, 0)
                    }
                    if (l.y < node.location.y) {
                        l = l.transform(0, -(mover.size() - 1), 0)
                    }
                }
                return l
            }
        }

        /**
         * Destination for following an entity, adjusting position based on their facing direction.
         */
        @JvmField
        val FOLLOW_ENTITY = object : DestinationFlag() {
            override fun getDestination(mover: Entity, node: Node): Location {
                val dir = node.direction
                var l = node.location.transform(-dir.stepX, -dir.stepY, 0)
                if (!checkTraversal(l, dir)) {
                    l = getClosestTo(mover, node, l)
                }
                if (mover.size() > 1) {
                    if (l.x < node.location.x) {
                        l = l.transform(-(mover.size() - 1), 0, 0)
                    }
                    if (l.y < node.location.y) {
                        l = l.transform(0, -(mover.size() - 1), 0)
                    }
                }
                return l
            }
        }

        /**
         * Placeholder for combat-related movement destinations.
         */
        @JvmField
        val COMBAT = object : DestinationFlag() {
            override fun getDestination(mover: Entity, node: Node): Location {
                return node.location
            }
        }

        /**
         * Destination when walking toward a ground item.
         */
        @JvmField
        val ITEM = object : DestinationFlag() {
            override fun getDestination(mover: Entity, node: Node): Location {
                return if (!RegionManager.isTeleportPermitted(node.location)) {
                    getClosestTo(mover, node, node.location.transform(1, 0, 0))
                } else {
                    node.location
                }
            }
        }

        /**
         * Destination when interacting with a game object (scenery).
         */
        @JvmField
        val OBJECT = object : DestinationFlag() {
            override fun getDestination(mover: Entity, node: Node): Location {
                val scenery = node as Scenery
                return when (scenery.type) {
                    in 0..3, 9 -> DoorActionHandler.getDestination(mover, scenery)
                    4, 5 -> scenery.location
                    else -> {
                        var sizeX = scenery.definition.sizeX
                        var sizeY = scenery.definition.sizeY
                        if (scenery.rotation % 2 != 0) {
                            val tmp = sizeX
                            sizeX = sizeY
                            sizeY = tmp
                        }
                        val dir = Direction.forWalkFlag(scenery.definition.walkingFlag, scenery.rotation)
                        val destination = if (dir != null) {
                            getDestination(mover, scenery, sizeX, sizeY, dir, 3)
                        } else {
                            getDestination(
                                mover,
                                scenery,
                                sizeX,
                                sizeY,
                                Direction.getLogicalDirection(scenery.location, mover.location),
                                0
                            )
                        }
                        return destination ?: scenery.location
                    }
                }
            }

            /**
             * Finds the closest traversable tile around an object, respecting size and direction.
             *
             * @param mover The moving entity.
             * @param scenery The object being interacted with.
             * @param sizeX Width of the object.
             * @param sizeY Height of the object.
             * @param startDir Starting direction to check.
             * @param count Rotation offset (used when direction is known).
             * @return The closest reachable tile near the object, or null if none found.
             */
            private fun getDestination(mover: Entity, scenery: Scenery, sizeX: Int, sizeY: Int, startDir: Direction, count: Int): Location? {
                var closest: Location? = null
                var distance = 9999.9
                val loc = scenery.location
                var dir = startDir
                for (i in count until 4) {
                    if (dir.toInteger() % 2 != 0) {
                        val x = if (dir.stepX > 0) dir.stepX * sizeX else dir.stepX
                        for (y in 0 until sizeY) {
                            val l = loc.transform(x, y, 0)
                            if (checkTraversal(l, dir)) {
                                val dist = mover.location.getDistance(l)
                                if (dist < distance) {
                                    distance = dist
                                    closest = l
                                }
                            }
                        }
                    } else {
                        val y = if (dir.stepY > 0) dir.stepY * sizeY else dir.stepY
                        for (x in 0 until sizeX) {
                            val l = loc.transform(x, y, 0)
                            if (checkTraversal(l, dir)) {
                                val dist = mover.location.getDistance(l)
                                if (dist < distance) {
                                    distance = dist
                                    closest = l
                                }
                            }
                        }
                    }
                    dir = Direction.get((dir.toInteger() + 1) % 4)
                }
                return closest
            }

            /**
             * Checks if the tile in the given direction is traversable and not blocked.
             */
            override fun checkTraversal(l: Location, dir: Direction): Boolean {
                return RegionManager.isTeleportPermitted(l) && dir.canMove(l)
            }
        }
    }
}