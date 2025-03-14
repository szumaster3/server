package core.game.world.map

import core.api.log
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.zone.ZoneBorders
import core.tools.Log
import core.tools.RandomFunction
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

object RegionManager {
    private val REGION_CACHE: MutableMap<Int, Region> = HashMap()

    @JvmStatic val CLIPPING_FLAGS = HashMap<Int, Array<Int>>()

    @JvmStatic val PROJECTILE_FLAGS = HashMap<Int, Array<Int>>()

    public val LOCK = ReentrantLock()

    @JvmStatic
    fun forId(regionId: Int): Region {
        if (LOCK.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            var region = REGION_CACHE[regionId]
            if (region == null) {
                region = Region((regionId shr 8) and 0xFF, regionId and 0xFF)
                REGION_CACHE[regionId] = region
            }
            LOCK.unlock()
            return REGION_CACHE[regionId]!!
        }
        log(this::class.java, Log.ERR, "UNABLE TO OBTAIN LOCK WHEN GETTING REGION BY ID. RETURNING BLANK REGION.")
        return Region(0, 0)
    }

    @JvmStatic
    fun pulse() {
        if (LOCK.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            for (r in REGION_CACHE.values) {
                if (r.isActive) {
                    for (p in r.planes) {
                        p.pulse()
                    }
                }
            }
            LOCK.unlock()
        }
    }

    @JvmStatic
    fun getClippingFlag(l: Location): Int {
        return getClippingFlag(l.z, l.x, l.y)
    }

    @JvmStatic
    fun getClippingFlag(
        z: Int,
        x: Int,
        y: Int,
    ): Int {
        val regionX = x shr 6
        val regionY = y shr 6
        val localX = x and 63
        val localY = y and 63
        return getClippingFlag(z, regionX, regionY, localX, localY)
    }

    @JvmStatic
    fun getClippingFlag(
        z: Int,
        regionX: Int,
        regionY: Int,
        localX: Int,
        localY: Int,
        projectile: Boolean = false,
    ): Int {
        val (region, index) = getFlagIndex(z, regionX, regionY, localX, localY)
        var flag = getFlags(region, projectile)[index]

        if (flag == -1) {
            val r = forId((regionX shr 8) or regionY)
            if (!r.isLoaded) {
                Region.load(r)
            }
            if (!r.isHasFlags) {
                return -1
            }
            flag = getFlags(region, projectile)[index]
        }

        return flag
    }

    private fun getFlagIndex(
        z: Int,
        regionX: Int,
        regionY: Int,
        localX: Int,
        localY: Int,
    ): Pair<Int, Int> {
        return Pair((regionX shl 8) or regionY, (z * 64 * 64) + (localX * 64) + localY)
    }

    @JvmStatic
    fun getFlags(
        regionX: Int,
        regionY: Int,
        projectile: Boolean,
    ): Array<Int> {
        val region = (regionX shl 8) or regionY
        return getFlags(region, projectile)
    }

    @JvmStatic
    fun getFlags(
        regionId: Int,
        projectile: Boolean,
    ): Array<Int> {
        return if (projectile) {
            PROJECTILE_FLAGS.getOrPut(regionId) { Array(16384) { 0 } }
        } else {
            CLIPPING_FLAGS.getOrPut(regionId) { Array(16384) { -1 } }
        }
    }

    @JvmStatic
    fun resetFlags(regionId: Int) {
        PROJECTILE_FLAGS.put(regionId, Array(16384) { 0 })
        CLIPPING_FLAGS.put(regionId, Array(16384) { -1 })
    }

    @JvmStatic
    fun getWaterClipFlag(
        z: Int,
        x: Int,
        y: Int,
    ): Int {
        val flag = getClippingFlag(z, x, y)
        return if (!isClipped(z, x, y)) {
            flag or 0x100
        } else {
            flag and 0x200000.inv()
        }
    }

    @JvmStatic
    fun isLandscape(l: Location): Boolean {
        return isLandscape(l.z, l.x, l.y)
    }

    @JvmStatic
    fun isLandscape(
        z: Int,
        x: Int,
        y: Int,
    ): Boolean {
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags || region.planes[z].flags.landscape == null) {
            return false
        }
        x -= x shr 6 shl 6
        y -= y shr 6 shl 6
        return region.planes[z].flags.landscape[x][y]
    }

    @JvmStatic
    fun addClippingFlag(
        z: Int,
        x: Int,
        y: Int,
        projectile: Boolean,
        flag: Int,
    ) {
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags) {
            return
        }
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        if (projectile) {
            region.planes[z].projectileFlags.flag(x, y, flag)
        } else {
            region.planes[z].flags.flag(x, y, flag)
        }
    }

    @JvmStatic
    fun removeClippingFlag(
        z: Int,
        x: Int,
        y: Int,
        projectile: Boolean,
        flag: Int,
    ) {
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags) {
            return
        }
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        if (projectile) {
            region.planes[z].projectileFlags.unflag(x, y, flag)
        } else {
            region.planes[z].flags.unflag(x, y, flag)
        }
    }

    @JvmStatic
    fun getProjectileFlag(
        z: Int,
        x: Int,
        y: Int,
    ): Int {
        val regionX = x shr 6
        val regionY = y shr 6
        val localX = x and 63
        val localY = y and 63
        return getClippingFlag(z, regionX, regionY, localX, localY, true)
    }

    @JvmStatic
    fun isTeleportPermitted(location: Location): Boolean {
        return isTeleportPermitted(location.z, location.x, location.y)
    }

    @JvmStatic
    fun isTeleportPermitted(
        z: Int,
        x: Int,
        y: Int,
    ): Boolean {
        if (!isLandscape(z, x, y)) {
            return false
        }
        val flag = getClippingFlag(z, x, y)
        return flag and 0x12c0102 == 0 || flag and 0x12c0108 == 0 || flag and 0x12c0120 == 0 || flag and 0x12c0180 == 0
    }

    @JvmStatic
    fun isClipped(location: Location): Boolean {
        return isClipped(location.z, location.x, location.y)
    }

    @JvmStatic
    fun isClipped(
        z: Int,
        x: Int,
        y: Int,
    ): Boolean {
        if (!isLandscape(z, x, y)) {
            return true
        }
        val flag = getClippingFlag(z, x, y)
        return flag and 0x12c0102 != 0 || flag and 0x12c0108 != 0 || flag and 0x12c0120 != 0 || flag and 0x12c0180 != 0
    }

    @JvmStatic
    fun getSpawnLocation(
        owner: Player?,
        node: Node?,
    ): Location? {
        if (owner == null || node == null) {
            return null
        }
        outer@ for (i in 0..7) {
            val dir = Direction.get(i)
            var stepX = dir.stepX
            var stepY = dir.stepY
            // For objects that are larger than 1,
            // the below corrects for the fact that their origin is on the SW tile.
            if (dir.stepX < 0) {
                stepX -= (node.size() - 1)
            }
            if (dir.stepY < 0) {
                stepY -= (node.size() - 1)
            }
            if (owner.size() > 1) {
                // e.g. if you used ::pnpc to morph yourself into a large NPC.
                if (dir.stepX > 0) {
                    stepX += (owner.size() - 1)
                }
                if (dir.stepY > 0) {
                    stepY += (owner.size() - 1)
                }
            }
            val l = owner.location.transform(stepX, stepY, 0)
            // Check if ALL target tiles are unclipped
            for (x in 0 until node.size()) {
                for (y in 0 until node.size()) {
                    if (isClipped(l.transform(x, y, 0))) {
                        continue@outer
                    }
                }
            }
            return l
        }
        return null
    }

    @JvmStatic
    fun getObject(l: Location): Scenery? {
        return getObject(l.z, l.x, l.y)
    }

    @JvmStatic
    fun getObject(
        z: Int,
        x: Int,
        y: Int,
    ): Scenery? {
        return getObject(z, x, y, -1)
    }

    @JvmStatic
    fun getObject(
        z: Int,
        x: Int,
        y: Int,
        objectId: Int,
    ): Scenery? {
        var x = x
        var y = y
        val regionId = ((x shr 6) shl 8) or (y shr 6)
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        val region = forId(regionId)
        Region.load(region)
        val scenery: Scenery? = region.planes[z].getChunkObject(x, y, objectId)
        return if (scenery != null && !scenery.isRenderable) {
            null
        } else {
            scenery
        }
    }

    @JvmStatic
    fun getRegionPlane(l: Location): RegionPlane {
        val regionId = ((l.x shr 6) shl 8) or (l.y shr 6)
        return forId(regionId).planes[l.z]
    }

    @JvmStatic
    fun getRegionChunk(l: Location): RegionChunk {
        val plane = getRegionPlane(l)
        return plane.getRegionChunk(l.localX / RegionChunk.SIZE, l.localY / RegionChunk.SIZE)
    }

    @JvmStatic
    fun move(entity: Entity) {
        val player = entity is Player
        val regionId = ((entity.location.regionX shr 3) shl 8) or (entity.location.regionY shr 3)
        val viewport = entity.viewport
        val current = forId(regionId)
        val z = entity.location.z
        val plane = current.planes[z]
        viewport.updateViewport(entity)
        if (plane == viewport.currentPlane) {
            entity.zoneMonitor.updateLocation(entity.walkingQueue.footPrint)
            return
        }
        viewport.remove(entity)
        if (player) {
            current.add(entity as Player)
        } else {
            current.add(entity as NPC)
        }
        viewport.region = current
        viewport.currentPlane = plane
        val view: MutableList<RegionPlane> = LinkedList()
        for (regionX in ((entity.location.regionX shr 3) - 1)..((entity.location.regionX shr 3) + 1)) {
            for (regionY in ((entity.location.regionY shr 3) - 1)..((entity.location.regionY shr 3) + 1)) {
                if (regionX < 0 || regionY < 0) {
                    continue
                }
                val region = forId((regionX shl 8) or regionY)
                val p = region.planes[z]
                if (player) {
                    region.incrementViewAmount()
                    region.flagActive()
                }
                view.add(p)
            }
        }
        viewport.viewingPlanes = view
        entity.zoneMonitor.updateLocation(entity.walkingQueue.footPrint)
    }

    @JvmStatic
    fun getLocalNpcs(n: Entity): List<NPC> {
        return getLocalNpcs(n, MapDistance.RENDERING.distance)
    }

    @JvmStatic
    fun getLocalEntitys(
        location: Location,
        distance: Int,
    ): List<Entity> {
        val entitys: MutableList<Entity> = ArrayList(20)
        entitys.addAll(getLocalNpcs(location, distance))
        entitys.addAll(getLocalPlayers(location, distance))
        return entitys
    }

    @JvmStatic
    fun getLocalEntitys(
        entity: Entity,
        distance: Int,
    ): List<Entity> {
        return getLocalEntitys(entity.location, distance)
    }

    @JvmStatic
    fun getLocalEntitys(entity: Entity): List<Entity> {
        return getLocalEntitys(entity.location, MapDistance.RENDERING.distance)
    }

    @JvmStatic
    fun getLocalNpcs(
        n: Entity,
        distance: Int,
    ): List<NPC> {
        val npcs: MutableList<NPC> = LinkedList()
        for (r in n.viewport.viewingPlanes) {
            for (npc in r.npcs) {
                if (npc.location.withinDistance(n.location, distance)) {
                    npcs.add(npc)
                }
            }
        }
        return npcs
    }

    @JvmStatic
    fun getLocalPlayers(n: Entity): List<Player> {
        return getLocalPlayers(n, MapDistance.RENDERING.distance)
    }

    @JvmStatic
    fun getLocalPlayers(
        n: Entity,
        distance: Int,
    ): List<Player> {
        val players: MutableList<Player> = LinkedList()
        for (r in n.viewport.viewingPlanes) {
            for (p in r.players) {
                if (p.location.withinDistance(n.location, distance)) {
                    players.add(p)
                }
            }
        }
        return players
    }

    @JvmStatic
    fun getSurroundingPlayers(
        n: Node,
        vararg ignore: Node,
    ): List<Player> {
        return getSurroundingPlayers(n, 9, *ignore)
    }

    @JvmStatic
    fun getSurroundingPlayers(
        n: Node,
        maximum: Int,
        vararg ignore: Node,
    ): List<Player> {
        val players = getLocalPlayers(n.location, 2)
        var count = 0
        val it = players.iterator()
        while (it.hasNext()) {
            val p = it.next()
            if (p.isInvisible()) {
                it.remove()
            }
            if (!p.location.withinMaxnormDistance(n.location, 1)) {
                it.remove()
                continue
            }
            if (++count >= maximum) {
                it.remove()
                continue
            }
            for (node in ignore) {
                if (p === node) {
                    count--
                    it.remove()
                    break
                }
            }
        }
        return players
    }

    @JvmStatic
    fun getSurroundingNPCs(
        n: Node,
        vararg ignore: Node,
    ): List<NPC> {
        return getSurroundingNPCs(n, 9, *ignore)
    }

    @JvmStatic
    fun getSurroundingNPCs(
        n: Node,
        maximum: Int,
        vararg ignore: Node,
    ): List<NPC> {
        val npcs = getLocalNpcs(n.location, 2)
        var count = 0
        val it = npcs.iterator()
        while (it.hasNext()) {
            val p = it.next()
            if (p.properties.teleportLocation != null &&
                !p.properties.teleportLocation.withinMaxnormDistance(
                    n.location,
                    1,
                )
            ) {
                it.remove()
                continue
            }
            if (p.getAttribute("state:death", false)) {
                it.remove()
                continue
            }
            if (p.isInvisible()) {
                it.remove()
                continue
            }
            if (!p.location.withinMaxnormDistance(n.location, 1)) {
                it.remove()
                continue
            }
            if (++count > maximum) {
                it.remove()
                continue
            }
            for (node in ignore) {
                if (p === node) {
                    count--
                    it.remove()
                    break
                }
            }
        }
        return npcs
    }

    @JvmStatic
    fun getTeleportLocation(
        location: Location,
        radius: Int,
    ): Location {
        var radius = radius
        var mod = radius shr 1
        if (mod == 0) {
            mod++
            radius--
        }
        return getTeleportLocation(location.transform(-mod, -mod, 0), mod + radius, mod + radius)
    }

    @JvmStatic
    fun getTeleportLocation(
        location: Location,
        areaX: Int,
        areaY: Int,
    ): Location {
        var destination = location
        var x: Int = RandomFunction.random(1 + areaX)
        var y: Int = RandomFunction.random(1 + areaY)
        var count = 0
        while (!isTeleportPermitted(location.transform(x, y, 0).also { destination = it })) {
            x = RandomFunction.random(1 + areaX)
            y = RandomFunction.random(1 + areaY)
            if (count++ >= areaX * 2) {
                // This would be able to keep looping for
                // several seconds otherwise (this actually happens).
                x = 0
                while (x < areaX + 1) {
                    y = 0
                    while (y < areaY + 1) {
                        if (isTeleportPermitted(location.transform(x, y, 0).also { destination = it })) {
                            return destination
                        }
                        y++
                    }
                    x++
                }
                break
            }
        }
        return destination
    }

    @JvmStatic
    fun getViewportPlayers(l: Location): List<Player> {
        var l = l
        val players: MutableList<Player> = LinkedList()
        l = l.chunkBase.transform(-8, -8, 0)
        val b = ZoneBorders(l.x, l.y, l.x + 24, l.y + 24)
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (player in forId(regionX shl 8 or regionY).planes[l.z].players) {
                    l = player.location
                    if (b.insideBorder(l.x, l.y)) {
                        players.add(player)
                    }
                }
            }
        }
        return players
    }

    @JvmStatic
    fun getRegionPlayers(regionId: Int): List<Player> {
        val r = forId(regionId)
        val players: MutableList<Player> = ArrayList(20)
        for (plane in r.planes) {
            players.addAll(plane.players)
        }
        return players
    }

    @JvmStatic
    fun getLocalPlayers(l: Location): List<Player> {
        return getLocalPlayers(l, MapDistance.RENDERING.distance)
    }

    @JvmStatic
    fun getLocalPlayers(
        l: Location,
        distance: Int,
    ): MutableList<Player> {
        val players: MutableList<Player> = LinkedList()
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (player in forId((regionX shl 8) or regionY).planes[l.z].players) {
                    if (player.location.withinDistance(l, distance)) {
                        players.add(player)
                    }
                }
            }
        }
        return players
    }

    @JvmStatic
    fun getLocalPlayersBoundingBox(
        l: Location,
        xdist: Int,
        ydist: Int,
    ): MutableList<Player> {
        val players: MutableList<Player> = LinkedList()
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (player in forId((regionX shl 8) or regionY).planes[l.z].players) {
                    if (player.location.x >= l.getX() - xdist &&
                        player.location.x <= l.getX() + xdist &&
                        player.location.y >= l.getY() - ydist &&
                        player.location.y <= l.getY() + ydist
                    ) {
                        players.add(player)
                    }
                }
            }
        }
        return players
    }

    @JvmStatic
    fun getLocalPlayersMaxNorm(
        l: Location,
        distance: Int,
    ): MutableList<Player> {
        val players: MutableList<Player> = LinkedList()
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (player in forId((regionX shl 8) or regionY).planes[l.z].players) {
                    if (player.location.withinMaxnormDistance(l, distance)) {
                        players.add(player)
                    }
                }
            }
        }
        return players
    }

    @JvmStatic
    fun getLocalNpcs(l: Location): List<NPC> {
        return getLocalNpcs(l, MapDistance.RENDERING.distance)
    }

    @JvmStatic
    fun getNpc(
        entity: Entity,
        id: Int,
    ): NPC? {
        return getNpc(entity, id, 16)
    }

    @JvmStatic
    fun getNpc(
        entity: Entity,
        id: Int,
        distance: Int,
    ): NPC? {
        return getNpc(entity.location, id, distance)
    }

    @JvmStatic
    fun getNpc(
        location: Location,
        id: Int,
        distance: Int,
    ): NPC? {
        val npcs: List<NPC> = getLocalNpcs(location, distance)
        for (n in npcs) {
            if (n.id == id) {
                return n
            }
        }
        return null
    }

    @JvmStatic
    fun getLocalNpcs(
        l: Location,
        distance: Int,
    ): MutableList<NPC> {
        val npcs: MutableList<NPC> = LinkedList()
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (n in forId(regionX shl 8 or regionY).planes[l.z].npcs) {
                    if (n.location.withinDistance(l, (n.size() shr 1) + distance)) {
                        npcs.add(n)
                    }
                }
            }
        }
        return npcs
    }

    @JvmStatic
    fun addRegion(
        id: Int,
        region: Region,
    ) {
        if (lock.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            REGION_CACHE[id] = region
            LOCK.unlock()
        }
    }

    @JvmStatic
    fun removeRegion(id: Int) {
        if (lock.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            val r = REGION_CACHE.remove(id)
            r?.flagInactive(true)
            LOCK.unlock()
        }
    }

    val regionCache: Map<Int, Region>
        @JvmStatic get() {
            return REGION_CACHE
        }

    val lock: ReentrantLock
        @JvmStatic get() = LOCK
}
