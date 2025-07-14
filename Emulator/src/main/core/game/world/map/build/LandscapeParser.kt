package core.game.world.map.build

import core.cache.misc.buffer.ByteBufferUtils
import core.game.node.scenery.Scenery
import core.game.world.map.*
import java.nio.ByteBuffer

/**
 * A utility class used for parsing landscapes.
 */
object LandscapeParser {
    @JvmStatic
    fun parse(r: Region, mapscape: Array<Array<ByteArray>>, buffer: ByteBuffer, storeObjects: Boolean) {
        var objectId = -1
        while (true) {
            val offset = ByteBufferUtils.getBigSmart(buffer)
            if (offset == 0) break
            objectId += offset
            var location = 0
            while (true) {
                val locOffset = ByteBufferUtils.getSmart(buffer)
                if (locOffset == 0) break
                location += locOffset - 1

                val y = location and 0x3F
                val x = location shr 6 and 0x3F
                val config = buffer.get().toInt() and 0xFF
                val rotation = config and 0x3
                val type = config shr 2
                var z = location shr 12

                r.objectCount = r.objectCount + 1

                if ((x !in 0..63) || (y !in 0..63)) continue
                if ((mapscape[1][x][y].toInt() and 0x2) == 2) z--

                if (z in 0..3) {
                    val obj = Scenery(objectId, Location.create((r.x shl 6) + x, (r.y shl 6) + y, z), type, rotation)
                    flagScenery(r.planes[z], x, y, obj, landscape = true, storeObjects = storeObjects)
                }
            }
        }
    }

    @JvmStatic
    fun addScenery(object_: Scenery) {
        addScenery(object_, landscape = false)
    }

    @JvmStatic
    fun addScenery(object_: Scenery, landscape: Boolean) {
        val loc = object_.location
        flagScenery(RegionManager.getRegionPlane(loc), loc.localX, loc.localY, object_, landscape, storeObjects = false)
    }

    @JvmStatic
    fun flagScenery(
        plane: RegionPlane,
        localX: Int,
        localY: Int,
        object_: Scenery,
        landscape: Boolean,
        storeObjects: Boolean
    ) {
        Region.load(plane.region)
        val def = object_.definition
        object_.isActive = true
        val add = storeObjects || !landscape || def.getChildObject(null)!!.hasActions()
        if (add) {
            addPlaneObject(plane, object_, localX, localY, landscape, storeObjects)
        }

        if (!applyClippingFlagsFor(plane, localX, localY, object_)) return

        if (!storeObjects && !add && def.getChildObject(null)?.name != "null") {
            addPlaneObject(plane, object_, localX, localY, landscape, false)
        }
    }

    @JvmStatic
    fun applyClippingFlagsFor(plane: RegionPlane, localX: Int, localY: Int, object_: Scenery): Boolean {
        val def = object_.definition
        val (sizeX, sizeY) = if (object_.rotation % 2 == 0) def.sizeX to def.sizeY else def.sizeY to def.sizeX
        val type = object_.type

        return when {
            type == 22 -> {
                plane.flags.landscape!![localX][localY] = true
                if (def.interactive != 0 || def.solid == 1 || def.isBlocksLand) {
                    if (def.solid == 1) {
                        plane.flags.flagTileObject(localX, localY)
                        if (def.isProjectileClipped) {
                            plane.projectileFlags.flagTileObject(localX, localY)
                        }
                    }
                }
                true
            }

            type >= 9 -> {
                if (def.solid != 0) {
                    plane.flags.flagSolidObject(localX, localY, sizeX, sizeY, def.isProjectileClipped)
                    if (def.isProjectileClipped) {
                        plane.projectileFlags.flagSolidObject(localX, localY, sizeX, sizeY, true)
                    }
                }
                true
            }

            type in 0..3 -> {
                if (def.solid != 0) {
                    plane.flags.flagDoorObject(localX, localY, object_.rotation, type, def.isProjectileClipped)
                    if (def.isProjectileClipped) {
                        plane.projectileFlags.flagDoorObject(localX, localY, object_.rotation, type, true)
                    }
                }
                true
            }

            else -> false
        }
    }

    @JvmStatic
    private fun addPlaneObject(
        plane: RegionPlane,
        object_: Scenery,
        localX: Int,
        localY: Int,
        landscape: Boolean,
        storeAll: Boolean
    ) {
        if (landscape && !storeAll) {
            val current = plane.objects!![localX][localY]
            if (current != null && current.definition.getChildObject(null)
                    !!.hasOptions(!object_.definition.getChildObject(null)!!.hasOptions(false))
            ) {
                return
            }
        }
        plane.add(object_, localX, localY, landscape && !storeAll)
    }

    @JvmStatic
    fun removeScenery(object_: Scenery): Scenery? {
        if (!object_.isRenderable) return null

        val plane = RegionManager.getRegionPlane(object_.location)
        Region.load(plane.region)
        val (localX, localY) = object_.location.localX to object_.location.localY
        val current = plane.getChunkObject(localX, localY, object_.id)
        if (current == null || current.id != object_.id) return null

        current.isActive = false
        object_.isActive = false
        plane.remove(localX, localY, object_.id)

        val def = object_.definition
        val (sizeX, sizeY) = if (object_.rotation % 2 == 0) def.sizeX to def.sizeY else def.sizeY to def.sizeX
        val type = object_.type

        when {
            type == 22 -> {
                if (def.interactive != 0 || def.solid == 1 || def.isBlocksLand) {
                    if (def.solid == 1) {
                        plane.flags.unflagTileObject(localX, localY)
                        if (def.isProjectileClipped) {
                            plane.projectileFlags.unflagTileObject(localX, localY)
                        }
                    }
                }
            }

            type >= 9 -> {
                if (def.solid != 0) {
                    plane.flags.unflagSolidObject(localX, localY, sizeX, sizeY, def.isProjectileClipped)
                    if (def.isProjectileClipped) {
                        plane.projectileFlags.unflagSolidObject(localX, localY, sizeX, sizeY, true)
                    }
                }
            }

            type in 0..3 -> {
                if (def.solid != 0) {
                    plane.flags.unflagDoorObject(localX, localY, object_.rotation, type, def.isProjectileClipped)
                    if (def.isProjectileClipped) {
                        plane.projectileFlags.unflagDoorObject(localX, localY, object_.rotation, type, true)
                    }
                }
            }
        }
        return current
    }
}
