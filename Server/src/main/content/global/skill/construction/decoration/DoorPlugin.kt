package content.global.skill.construction.decoration

import content.global.skill.construction.BuildHotspot
import content.global.skill.construction.HousingStyle
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Direction
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.plugin.Plugin
import java.awt.Point

@Initializable
class DoorPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        val dungeonDoors = BuildHotspot.DUNGEON_DOOR_LEFT.decorations + BuildHotspot.DUNGEON_DOOR_RIGHT.decorations
        dungeonDoors.forEach { deco ->
            val def = SceneryDefinition.forId(deco.objectId)
            listOf("option:open", "option:pick-lock", "option:force").forEach { opt ->
                def.handlers[opt] = this
            }
        }
        HousingStyle.values().forEach { style ->
            listOf(style.doorId, style.secondDoorId).forEach {
                SceneryDefinition.forId(it).handlers["option:open"] = this
            }
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val door = node as? Scenery ?: return false

        if (!option.equals("open", ignoreCase = true)) return false

        runCatching {
            val secondDoor = getSecondDoor(door)
            val replaceId = getReplaceId(door)
            val secondReplaceId = secondDoor?.let { getReplaceId(it) } ?: -1
            openDoor(door, secondDoor, replaceId, secondReplaceId)
        }.onFailure { it.printStackTrace() }

        return true
    }

    private fun openDoor(door: Scenery, second: Scenery?, replaceId: Int, secondReplaceId: Int) {
        if (second == null) {
            val (offsetX, offsetY, newRot) = getOpenOffset(door)
            val newDoor = Scenery(replaceId, door.location.transform(offsetX, offsetY, 0), 0, newRot)
            SceneryBuilder.replace(door, newDoor)
            return
        }

        val p = DoorActionHandler.getRotationPoint(door.rotation) ?: Point(0, 0)

        var firstDir = (door.rotation + 1) % 4
        var secondDir = (second.rotation + 1) % 4

        val offsetDir = Direction.getDirection(second.location.x - door.location.x,
            second.location.y - door.location.y)

        firstDir = when {
            firstDir == 1 && offsetDir == Direction.NORTH -> 3
            firstDir == 2 && offsetDir == Direction.EAST  -> 0
            firstDir == 3 && offsetDir == Direction.SOUTH -> 1
            firstDir == 0 && offsetDir == Direction.WEST  -> 2
            else -> firstDir
        }

        if (firstDir == secondDir) secondDir = (secondDir + 2) % 4

        val firstLoc = door.location.transform(p.x, p.y, 0)
        val secondLoc = second.location.transform(p.x, p.y, 0)

        SceneryBuilder.replace(door, door.transform(replaceId, firstDir, firstLoc))
        SceneryBuilder.replace(second, second.transform(secondReplaceId, secondDir, secondLoc))
    }

    private fun getOpenOffset(door: Scenery): Triple<Int, Int, Int> {
        return when {
            door.location.chunkOffsetY == 0 || door.location.chunkOffsetY == 7 -> {
                val offsetY = if (door.location.chunkOffsetY == 7) 1 else -1
                val rot = if (door.location.chunkOffsetX == 3) 0 else 2
                Triple(0, offsetY, rot)
            }
            door.location.chunkOffsetX == 0 || door.location.chunkOffsetX == 7 -> {
                val offsetX = if (door.location.chunkOffsetX == 7) 1 else -1
                val rot = if (door.location.chunkOffsetY == 3) 3 else 1
                Triple(offsetX, 0, rot)
            }
            else -> Triple(0, 0, door.rotation)
        }
    }

    private fun getReplaceId(door: Scenery): Int = REPLACEMENT_MAP.getValue(door.id)

    private fun getSecondDoor(door: Scenery): Scenery? {
        val possibleIds = setOf(door.id, door.id + 1, door.id - 1)
        val directions = listOf(
            0  to 0,    1 to  0,   -1 to  0,
            0  to 1,    0 to -1,    1 to  1,
           -1  to 1,    1 to -1,   -1 to -1
        )

        for ((dx, dy) in directions) {
            val loc = door.location.transform(dx, dy, 0)
            val obj = RegionManager.getObject(loc.z, loc.x, loc.y)
            if (obj != null && obj != door && obj.id in possibleIds) return obj
        }
        return null
    }

    companion object {
        private val REPLACEMENT_MAP = mapOf(
            13100 to 13102,
            13101 to 13103,
            13006 to 13008,
            13007 to 13008,
            13015 to 13017,
            13016 to 13018,
            13094 to 13095,
            13096 to 13097,
            13109 to 13110,
            13107 to 13108,
            13118 to 13120,
            13119 to 13121
        ).withDefault { key -> key + 2 }
    }
}