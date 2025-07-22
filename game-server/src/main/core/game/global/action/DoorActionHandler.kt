package core.game.global.action

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.scenery.Constructed
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.config.DoorConfigLoader.Companion.forId
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.path.Pathfinder
import org.rs.consts.Sounds
import java.awt.Point

/**
 * Handles door-related actions.
 */
object DoorActionHandler {
    private const val IN_USE_CHARGE = 88

    /**
     * Handles interaction with a door managing open & close states.
     *
     * @param player The player interacting with the door.
     * @param scenery The door object being interacted with.
     */
    @JvmStatic
    fun handleDoor(player: Player, scenery: Scenery) {
        val second = if ((scenery.id == 1530 || scenery.id == 1531)) null else getSecondDoor(scenery)
        var o: Scenery? = null
        if (scenery is Constructed && (scenery.replaced.also { o = it }) != null) {
            val d = forId(scenery.getId())
            if (d != null && d.isMetal) {
                playAudio(player, Sounds.IRON_DOOR_CLOSE_70)
            } else if (d != null && d.isFence) {
                playAudio(player, Sounds.GATE_CLOSE_66)
            } else {
                playAudio(player, 60)
            }
            SceneryBuilder.replace(scenery, o)
            if (second is Constructed && (second.replaced.also { o = it }) != null) {
                SceneryBuilder.replace(second, o)
                return
            }
            return
        }
        if (scenery.definition.hasAction("close")) {
            if (second != null) {
                sendMessage(player, "The doors appear to be stuck.")
                playAudio(player, Sounds.DOOR_CREAK_61)
                return
            }
            val d = forId(scenery.id)
            if (d != null && d.isMetal) {
                playAudio(player, Sounds.IRON_DOOR_OPEN_71)
            } else if (d != null && d.isFence) {
                playAudio(player, Sounds.GATE_OPEN_67)
            } else {
                playAudio(player, Sounds.DOOR_OPEN_62)
            }
            if (d == null) {
                sendMessage(player, "The door appears to be stuck.")
                playAudio(player, Sounds.DOOR_CREAK_61)
                return
            }
            val firstDir = (scenery.rotation + 3) % 4
            val p = getCloseRotation(scenery)
            val firstLoc = scenery.location.transform(p.x, p.y, 0)
            SceneryBuilder.replace(scenery, scenery.transform(d.replaceId, firstDir, firstLoc))
            return
        }
        val d = forId(scenery.id)
        if (d != null && d.questRequirement != "") {
            if (!hasRequirement(player, d.questRequirement)) return
        }
        if (d == null || d.isAutoWalk) {
            handleAutowalkDoor(player, scenery)
            return
        }
        if (d.isMetal) {
            playAudio(player, Sounds.IRON_DOOR_OPEN_71)
        } else if (d.isFence) {
            playAudio(player, Sounds.GATE_OPEN_67)
        } else {
            playAudio(player, Sounds.DOOR_OPEN_62)
        }
        if (second != null) {
            val s = forId(second.id)
            open(scenery, second, d.replaceId, s?.replaceId ?: second.id, true, 500, d.isFence)
            return
        }
        open(scenery, null, d.replaceId, -1, true, 500, d.isFence)
    }

    /**
     * Handles autowalk through a door for any entity.
     *
     * @param entity The entity walking through the door.
     * @param scenery The door object.
     * @param location The target location.
     * @return True if the pulse started, false if door is in use.
     */
    @JvmStatic
    @JvmOverloads
    fun handleAutowalkDoor(entity: Entity, scenery: Scenery, location: Location = getEndLocation(entity, scenery)): Boolean {
        if (scenery.charge == IN_USE_CHARGE) {
            return false
        }
        val second = if ((scenery.id == 3)) null else getSecondDoor(scenery)
        entity.lock(4)
        val loc = entity.location
        if (entity is Player) {
            val d = forId(scenery.id)
            if (d != null && d.isMetal) {
                playAudio(entity.asPlayer(), Sounds.IRON_DOOR_OPEN_71)
                playAudio(entity.asPlayer(), Sounds.IRON_DOOR_CLOSE_70, 60)
            } else if (d != null && d.isFence) {
                playAudio(entity.asPlayer(), Sounds.GATE_OPEN_67)
                playAudio(entity.asPlayer(), Sounds.GATE_CLOSE_66, 60)
            } else {
                playAudio(entity.asPlayer(), Sounds.DOOR_OPEN_62)
                playAudio(entity.asPlayer(), 60, 60)
            }
            entity.asPlayer().logoutListeners["autowalk"] = { player: Player ->
                player.location = loc
            }
        }
        submitWorldPulse(
            object : Pulse(1) {
                var opened: Boolean = false

                override fun pulse(): Boolean {
                    if (!opened) {
                        open(scenery, second, scenery.id, second?.id ?: -1, false, 2, false)
                        entity.walkingQueue.reset()
                        entity.walkingQueue.addPath(location.x, location.y)
                        opened = true

                        scenery.charge = IN_USE_CHARGE
                        if (second != null) {
                            second.charge = IN_USE_CHARGE
                        }
                        return false
                    }
                    if (entity is Player) {
                        val player = entity as? Player ?: return false
                        if (scenery.id == 2112 && withinDistance(player, Location(3046, 9756, 0), 10)) {
                            finishDiaryTask(player, DiaryType.FALADOR, 2, 6)
                        }

                        if (scenery.id == 35549 || scenery.id == 35551 && player.viewport.region!!.id == 13106) {
                            finishDiaryTask(player, DiaryType.LUMBRIDGE, 0, 4)
                        }

                        if (scenery.id == 2406 && withinDistance(player, Location.create(3202, 3169, 0))) {
                            finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 6)
                        }

                        entity.asPlayer().logoutListeners.remove("autowalk")
                    }

                    scenery.charge = 1000
                    if (second != null) {
                        second.charge = 1000
                    }
                    return true
                }
            },
        )
        return true
    }

    /**
     * Gets the end location on the other side of a door.
     *
     * @param entity The entity moving through the door.
     * @param scenery The door object.
     * @return The destination.
     */
    @JvmStatic
    fun getEndLocation(entity: Entity, scenery: Scenery): Location = getEndLocation(entity, scenery, false)

    /**
     * Gets the end location on the other side of a door.
     *
     * @param entity The entity moving through the door.
     * @param scenery The door object.
     * @param isAutoWalk *Optional* flag indicating if autowalk is used.
     * @return The destination.
     */
    @JvmStatic
    fun getEndLocation(entity: Entity, scenery: Scenery, isAutoWalk: Boolean?): Location {
        var l = scenery.location
        when (scenery.rotation) {
            0 -> if (entity.location.x >= l.x) {
                l = l.transform(-1, 0, 0)
            }

            1 -> if (entity.location.y <= l.y) {
                l = l.transform(0, 1, 0)
            }

            2 -> if (entity.location.x <= l.x) {
                l = l.transform(1, 0, 0)
            }

            else -> if (entity.location.y >= l.y) {
                l = l.transform(0, -1, 0)
            }
        }
        return l
    }

    /**
     * Gets the destination location.
     *
     * @param entity The entity moving through the door.
     * @param door The door object.
     * @return The final destination location.
     */
    @JvmStatic
    fun getDestination(entity: Entity, door: Scenery): Location {
        var l = door.location
        var rotation = door.rotation
        if (door is Constructed && door.getDefinition().hasAction("close")) {
            val o = door.replaced
            if (o != null) {
                l = o.location
                rotation = o.rotation
            }
        }
        if (door.type == 9) {
            when (rotation) {
                0, 2 -> {
                    if (entity.location.y < l.y || entity.location.x < l.x) {
                        return l.transform(0, -1, 0)
                    }
                    return l.transform(0, 1, 0)
                }

                1, 3 -> {
                    if (entity.location.x > l.x || entity.location.y > l.y) {
                        return l.transform(1, 0, 0)
                    }
                    return l.transform(-1, 0, 0)
                }
            }
        }
        when (rotation) {
            0 -> if (entity.location.x < l.x) {
                if (Pathfinder.find(entity, l.transform(-1, 0, 0)).isMoveNear) {
                    return l.transform(0, 0, 0)
                }
                return l.transform(-1, 0, 0)
            }

            1 -> if (entity.location.y > l.y) {
                if (Pathfinder.find(entity, l.transform(0, 1, 0)).isMoveNear) {
                    return l.transform(0, 0, 0)
                }
                return l.transform(0, 1, 0)
            }

            2 -> if (entity.location.x > l.x) {
                if (Pathfinder.find(entity, l.transform(1, 0, 0)).isMoveNear) {
                    return l.transform(0, 0, 0)
                }
                return l.transform(1, 0, 0)
            }

            3 -> if (entity.location.y < l.y) {
                if (Pathfinder.find(entity, l.transform(0, -1, 0)).isMoveNear) {
                    return l.transform(0, 0, 0)
                }
                return l.transform(0, -1, 0)
            }
        }
        return l
    }

    /**
     * Handles open interaction.
     *
     * @param scenery The primary door object to open.
     * @param second The second door object, if any.
     * @param replaceId The id of the replacement object for the primary door.
     * @param secondReplaceId The id of the replacement object for the second door.
     * @param clip Whether to apply clipping on the door.
     * @param restoreTicks Ticks after which the door should restore to its original state.
     * @param fence True if the door is a fence gate, which uses a different open method.
     */
    @JvmStatic
    fun open(scenery: Scenery, second: Scenery?, replaceId: Int, secondReplaceId: Int, clip: Boolean, restoreTicks: Int, fence: Boolean) {
        var `object` = scenery
        var second = second
        var replaceId = replaceId
        `object` = `object`.wrapper
        val mod = if (`object`.type == 9) -1 else 1
        var firstDir = (`object`.rotation + ((mod + 4) % 4)) % 4
        val p = getRotationPoint(`object`.rotation)
        var firstLoc = `object`.location.transform(p!!.x * mod, p.y * mod, 0)
        if (second == null) {
            if (replaceId == 4577) {
                replaceId = 4578
                firstDir = 3
                firstLoc = firstLoc.transform(0, 1, 0)
            }
            SceneryBuilder.replace(`object`, `object`.transform(replaceId, firstDir, firstLoc), restoreTicks, clip)
            return
        }
        second = second.wrapper
        if (fence) {
            openFence(`object`, second, replaceId, secondReplaceId, clip, restoreTicks)
            return
        }
        val offset =
            Direction.getDirection(second.location.x - `object`.location.x, second.location.y - `object`.location.y)
        var secondDir = (second.rotation + mod) % 4
        if (firstDir == 1 && offset == Direction.NORTH) {
            firstDir = 3
        } else if (firstDir == 2 && offset == Direction.EAST) {
            firstDir = 0
        } else if (firstDir == 3 && offset == Direction.SOUTH) {
            firstDir = 1
        } else if (firstDir == 0 && offset == Direction.WEST) {
            firstDir = 2
        }
        if (firstDir == secondDir) {
            secondDir = (secondDir + 2) % 4
        }
        val secondLoc = second.location.transform(p.x, p.y, 0)
        SceneryBuilder.replace(`object`, `object`.transform(replaceId, firstDir, firstLoc), restoreTicks, clip)
        SceneryBuilder.replace(second, second.transform(secondReplaceId, secondDir, secondLoc), restoreTicks, clip)
    }

    /**
     * Handles fence & gate interactions.
     *
     * @param scenery The primary fence door.
     * @param second The second fence door.
     * @param replaceId replacement id for the primary door.
     * @param secondReplaceId replacement id for the second door.
     * @param clip Whether clipping should be applied.
     * @param restoreTicks Ticks before restoring original state.
     */
    @JvmStatic
    private fun openFence(scenery: Scenery, second: Scenery, replaceId: Int, secondReplaceId: Int, clip: Boolean, restoreTicks: Int) {
        var replaceId = replaceId
        var secondReplaceId = secondReplaceId
        val offset =
            Direction.getDirection(second.location.x - scenery.location.x, second.location.y - scenery.location.y)
        var firstDir = (scenery.rotation + 3) % 4
        val p = getRotationPoint(scenery.rotation)
        var firstLoc: Location? = null
        var secondDir = (second.rotation + 3) % 4
        if (offset == Direction.WEST || offset == Direction.SOUTH) {
            firstLoc = second.location.transform(p!!.x, p.y, 0)
            val s = replaceId
            replaceId = secondReplaceId
            secondReplaceId = s
        } else {
            firstLoc = scenery.location.transform(p!!.x, p.y, 0)
        }
        if (scenery.rotation == 3 || scenery.rotation == 2) {
            firstDir = (firstDir + 2) % 4
            secondDir = (secondDir + 2) % 4
        }
        val secondLoc = firstLoc.transform(p.x, p.y, 0)
        if ((scenery.id == 36917 || scenery.id == 36919)) {
            when (scenery.direction) {
                Direction.SOUTH -> {
                    SceneryBuilder.replace(scenery, scenery.transform(36919, firstDir, firstLoc), restoreTicks, true)
                    SceneryBuilder.replace(second, second.transform(36917, secondDir, secondLoc), restoreTicks, true)
                }

                Direction.EAST -> {
                    SceneryBuilder.replace(scenery, scenery.transform(36917, firstDir, firstLoc), restoreTicks, true)
                    SceneryBuilder.replace(second, second.transform(36919, secondDir, secondLoc), restoreTicks, true)
                }

                else -> {
                    SceneryBuilder.replace(scenery, scenery.transform(36919, firstDir, firstLoc), restoreTicks, true)
                    SceneryBuilder.replace(second, second.transform(36917, secondDir, secondLoc), restoreTicks, true)
                }
            }
        } else {
            SceneryBuilder.replace(scenery, scenery.transform(replaceId, firstDir, firstLoc), restoreTicks, clip)
            SceneryBuilder.replace(second, second.transform(secondReplaceId, secondDir, secondLoc), restoreTicks, clip)
        }
    }

    /**
     * Handles autowalking through a fence gate.
     *
     * @param entity The entity walking through the fence gate.
     * @param scenery The primary fence door object.
     * @param replaceId Replacement id for the primary fence door.
     * @param secondReplaceId Replacement id for the second fence door.
     * @return True if autowalk was initiated, false otherwise.
     */
    @JvmStatic
    fun autowalkFence(entity: Entity, scenery: Scenery, replaceId: Int, secondReplaceId: Int): Boolean {
        val second = getSecondDoor(scenery)
        if (scenery.charge == IN_USE_CHARGE || second == null) {
            return false
        }
        entity.lock(4)
        val loc = entity.location
        if (entity is Player) {
            entity.asPlayer().logoutListeners["autowalk"] = { player: Player ->
                player.location = loc
            }
        }
        scenery.charge = IN_USE_CHARGE
        second.charge = IN_USE_CHARGE
        submitWorldPulse(
            object : Pulse(1) {
                var opened: Boolean = false

                override fun pulse(): Boolean {
                    if (!opened) {
                        openFence(scenery, second, replaceId, secondReplaceId, false, 2)
                        val l = getEndLocation(entity, scenery)
                        entity.walkingQueue.reset()
                        entity.walkingQueue.addPath(l.x, l.y)
                        opened = true
                        return false
                    }
                    if (entity is Player) {
                        entity.asPlayer().logoutListeners.remove("autowalk")
                    }
                    scenery.charge = 1000
                    if (second != null) {
                        second.charge = 1000
                    }
                    return true
                }
            },
        )
        return true
    }

    /**
     * Gets the point offset used to calculate the "close" rotation for doors.
     *
     * @param scenery The door object.
     * @return The point offset for close rotation.
     */
    @JvmStatic
    private fun getCloseRotation(scenery: Scenery): Point {
        when (scenery.rotation) {
            0 -> return Point(0, 1)
            1 -> return Point(1, 0)
            2 -> return Point(0, -1)
            3 -> return Point(-1, 0)
        }
        return Point(0, 0)
    }

    /**
     * Gets the point offset based on door rotation.
     *
     * @param rotation The door rotation.
     * @return The point offset for the given rotation.
     */
    @JvmStatic
    fun getRotationPoint(rotation: Int): Point? {
        when (rotation) {
            0 -> return Point(-1, 0)
            1 -> return Point(0, 1)
            2 -> return Point(1, 0)
            3 -> return Point(0, -1)
        }
        return null
    }

    /**
     * Gets the second door.
     *
     * @param scenery The primary door object.
     * @return The matching door.
     */
    @JvmStatic
    fun getSecondDoor(scenery: Scenery): Scenery? {
        val location = scenery.location
        val directions = arrayOf(
            location.transform(-1, 0, 0),
            location.transform(1, 0, 0),
            location.transform(0, -1, 0),
            location.transform(0, 1, 0),
        )

        for (dir in directions) {
            val foundObject = getObject(dir)
            if (foundObject != null && foundObject.name == scenery.name) {
                return foundObject
            }
        }
        return null
    }

    /**
     * Gets door rotations.
     *
     * @param scenery The primary door.
     * @param second The second door, or null if none.
     * @param rp Reference point for rotation calculation.
     * @return Array of two rotations for the doors.
     */
    @JvmStatic
    fun getRotation(scenery: Scenery, second: Scenery?, rp: Point): IntArray {
        if (second == null) {
            return intArrayOf((scenery.rotation + 1) % 4)
        }
        var rotations = intArrayOf(3, 1)
        val fl = scenery.location
        val sl = second.location
        if (fl.x > sl.x) {
            rotations = intArrayOf(2, 0)
        }
        if (fl.x < sl.x) {
            rotations = intArrayOf(0, 2)
        }
        if (fl.y > sl.y) {
            rotations = intArrayOf(1, 3)
        }
        if (rp.y > 0 || rp.x > 0) {
            rotations = intArrayOf(rotations[1], rotations[0])
        }
        return rotations
    }
}
