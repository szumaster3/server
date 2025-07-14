package core.game.world.map.build

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.music.MusicZone
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.scenery.Scenery
import core.game.world.map.*
import core.game.world.map.zone.RegionZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.impl.MultiwayCombatZone
import java.util.*

class DynamicRegion(
    private val regionCopyId: Int,
    x: Int,
    y: Int
) : Region(x, y) {

    companion object {
        private val RESERVED_AREAS = mutableListOf<ZoneBorders>()

        @JvmStatic
        fun create(regionId: Int): DynamicRegion {
            val x = (regionId shr 8) shl 6
            val y = (regionId and 0xFF) shl 6
            return create(ZoneBorders(x, y, x + SIZE, y + SIZE))[0]
        }

        @JvmStatic
        fun create(regionOne: Int, regionTwo: Int): DynamicRegion {
            val x = (regionOne shr 8) shl 6
            val y = (regionOne and 0xFF) shl 6
            val x1 = (regionTwo shr 8) shl 6
            val y1 = (regionTwo and 0xFF) shl 6
            return create(ZoneBorders(x, y, x1 + SIZE, y1 + SIZE))[0]
        }

        @JvmStatic
        fun create(copy: ZoneBorders): Array<DynamicRegion> {
            val baseX = copy.southWestX shr 6
            val baseY = copy.southWestY shr 6
            val border = findZoneBorders(
                (copy.northEastX - copy.southWestX) shr 3,
                (copy.northEastY - copy.southWestY) shr 3
            )
            RESERVED_AREAS.add(border)

            val baseLoc = Location.create(border.southWestX, border.southWestY, 0)
            val regions = mutableListOf<DynamicRegion>()

            for (x in (copy.southWestX shr 6) until (copy.northEastX shr 6)) {
                for (y in (copy.southWestY shr 6) until (copy.northEastY shr 6)) {
                    val regionId = (x shl 8) or y
                    val loc = baseLoc.transform((x - baseX) shl 6, (y - baseY) shl 6, 0)
                    val region = copy(regionId, loc)
                    region.borders = border

                    RegionManager.forId(region.id)?.let { original ->
                        for (z in 0 until 4) {
                            region.planes[z].players.addAll(original.planes[z].players)
                            region.planes[z].npcs.addAll(original.planes[z].npcs)
                        }
                    }
                    RegionManager.addRegion(region.id, region)
                    regions.add(region)
                }
            }

            regions.forEach { region ->
                for (z in 0 until 4) {
                    region.planes[z].players.forEach { it.updateSceneGraph(false) }
                }
            }

            return regions.toTypedArray()
        }

        @JvmStatic
        fun reserveArea(sizeX: Int, sizeY: Int): ZoneBorders {
            val borders = findZoneBorders(sizeX, sizeY)
            RESERVED_AREAS.add(borders)
            return borders
        }

        @JvmStatic
        fun findZoneBorders(sizeX: Int, sizeY: Int): ZoneBorders {
            var x = 0
            var y = 0
            var count = 0
            var width = (sizeX shr 3) shl 6
            var height = (sizeY shr 3) shl 6

            width = width.coerceAtLeast(64)
            height = height.coerceAtLeast(64)

            while (true) {
                val endX = x + width
                val endY = y + height

                val reserved = RESERVED_AREAS.any {
                    it.insideBorder(x, y) || it.insideBorder(endX, endY)
                            || it.insideBorder(x, endY) || it.insideBorder(endX, y)
                }

                if (!reserved) return ZoneBorders(x, y, endX, endY)

                if (++count % 15 == 0) {
                    y += 64
                    x = 0
                } else {
                    x += 64
                }
            }
        }

        @JvmStatic
        fun copy(regionId: Int, to: Location): DynamicRegion {
            val regionX = (regionId shr 8 and 0xFF) shl 6
            val regionY = (regionId and 0xFF) shl 6

            val region = DynamicRegion(regionId, to.regionX shr 3, to.regionY shr 3)
            val base = RegionManager.forId(regionId)
            Region.load(base)

            for (offsetX in 0 until 8) {
                for (offsetY in 0 until 8) {
                    val x = regionX + (offsetX shl 3)
                    val y = regionY + (offsetY shl 3)
                    for (plane in 0 until 4) {
                        val srcChunk = base?.planes?.get(plane)?.getRegionChunk(offsetX, offsetY)
                        val chunk = srcChunk?.copy(region.planes[plane])
                            ?: RegionChunk(Location.create(0, 0, 0), 0, region.planes[plane])
                        region.replaceChunk(plane, offsetX, offsetY, chunk, base!!)
                        chunk.rotation = 0
                        chunk.base = Location.create(x, y, plane)
                    }
                }
            }
            return region
        }
    }

    override val regionId: Int get() = regionCopyId
    val chunks = Array(4) { Array(SIZE shr 3) { arrayOfNulls<RegionChunk>(SIZE shr 3) } }
    var borders: ZoneBorders? = null
    var multicombat: Boolean = false
    var permanent: Boolean = false
    var linked: List<DynamicRegion>? = null
    var parentRegion: DynamicRegion? = null
    val npcs = ArrayList<NPC>(10)

    constructor(borders: ZoneBorders) : this(-1, borders.southWestX shr 6, borders.southWestY shr 6) {
        this.borders = borders
        this.updateAllPlanes = true
        RegionManager.addRegion(id, this)
    }

    fun link(vararg regions: DynamicRegion) {
        regions.forEach { it.parentRegion = this }
        linked = regions.toList()
        flagActive()
    }

    fun toggleMulticombat() {
        if (multicombat) {
            regionZones.removeIf { it.zone == MultiwayCombatZone.instance }
        } else {
            regionZones.add(RegionZone(MultiwayCombatZone.instance, borders))
        }
        multicombat = !multicombat
    }

    fun setMusicId(musicId: Int) {
        musicZones.add(MusicZone(musicId, borders))
    }

    fun rotate() {
        for (z in 0 until 4) {
            val old = chunks[z].copyOf()
            for (x in 0 until 8) {
                for (y in 0 until 8) {
                    val rotated = old[7 - y][x]
                    chunks[z][x][y] = rotated
                    rotated?.rotation = (rotated?.rotation ?: 0) + 1
                }
            }
        }
    }

    fun setChunk(z: Int, x: Int, y: Int, chunk: RegionChunk?) {
        chunks[z][x][y] = chunk
        planes[z].chunks[x][y] = chunk
        chunk?.currentBase = baseLocation.transform(x shl 3, y shl 3, 0)
    }

    fun replaceChunk(z: Int, x: Int, y: Int, chunk: RegionChunk?, fromRegion: Region) {
        Region.load(this)
        val plane = planes[z]
        chunks[z][x][y] = chunk
        plane.chunks[x][y] = chunk

        if (chunk == null) {
            for (i in (x shl 3) until ((x + 1) shl 3)) {
                for (j in (y shl 3) until ((y + 1) shl 3)) {
                    plane.flags.invalidateFlag(i, j)
                    plane.projectileFlags.invalidateFlag(i, j)
                    val obj = plane.objects[i][j]
                    if (obj != null) {
                        LandscapeParser.removeScenery(obj)
                    } else {
                        plane.add(null, i, j, true)
                    }
                }
            }
        } else {
            Region.load(fromRegion)
            val l = chunk.base
            val rp = fromRegion.planes[l.z]
            chunk.currentBase = baseLocation.transform(x shl 3, y shl 3, z)
            chunk.rebuildFlags(rp)
        }
    }

    override fun flagInactive(force: Boolean): Boolean {
        if (!permanent) {
            parentRegion?.takeIf { it.active }?.let {
                it.checkInactive()
                return false
            }
            linked?.forEach { if (!it.isInactive(false)) return false }

            if (!super.flagInactive(force)) return false

            planes.forEach { plane ->
                plane.npcs.forEach { it.clear() }
                chunks[plane.plane].forEach { row ->
                    row.forEach { chunk ->
                        chunk?.items?.forEach { GroundItemManager.getItems().remove(it) }
                    }
                }
            }

            RESERVED_AREAS.remove(borders)
            if (multicombat) toggleMulticombat()

            return linked?.all { it.flagInactive(force) } ?: true
        }
        return true
    }

    fun clear() {
        npcs.forEach { it.clear() }
        npcs.clear()
    }
}
