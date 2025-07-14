package core.game.world.map

import core.api.log
import core.cache.Cache
import core.cache.CacheIndex
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.music.MusicZone
import core.game.system.communication.CommunicationInfo
import core.game.system.config.XteaParser
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.build.DynamicRegion
import core.game.world.map.build.LandscapeParser
import core.game.world.map.build.MapscapeParser
import core.game.world.map.zone.RegionZone
import core.game.world.repository.Repository
import core.tools.Log
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

/**
 * Represents a region.
 *
 * @author Emperor
 */
open class Region(val x: Int, val y: Int) {

    companion object {
        /**
         * Size of the region.
         */
        const val SIZE = 64

        /**
         * Loads region data, optionally building the region.
         *
         * @param region The region to load.
         */
        @JvmStatic
        fun load(region: Region) {
            load(region, region.build)
        }

        /**
         * Loads region data with control over building.
         *
         * @param region The region to load.
         * @param build Whether to build the region after loading.
         */
        @JvmStatic
        fun load(region: Region, build: Boolean) {
            try {
                if (region.loaded && region.build == build) return

                region.build = build
                val isDynamic = region is DynamicRegion
                val regionId = if (isDynamic) region.regionId else region.id
                val regionX = (regionId shr 8) and 0xFF
                val regionY = regionId and 0xFF
                val regionName = "${regionX}_$regionY"

                val mapscapeId = Cache.getArchiveId(CacheIndex.LANDSCAPES, "m$regionName")
                if (mapscapeId < 0 && !isDynamic) {
                    region.loaded = true
                    return
                }

                val mapscapeData = Array(4) { Array(SIZE) { ByteArray(SIZE) } }

                for (plane in region.planes) {
                    plane.flags.landscape = Array(SIZE) { BooleanArray(SIZE) }
                    plane.flags.clippingFlags = Array(SIZE) { IntArray(SIZE) }
                    plane.projectileFlags.clippingFlags = Array(SIZE) { IntArray(SIZE) }
                }

                if (mapscapeId > -1) {
                    val mapscapeBytes = Cache.getData(CacheIndex.LANDSCAPES, "m$regionName") ?: return
                    val mapscapeBuffer = ByteBuffer.wrap(mapscapeBytes)
                    MapscapeParser.parse(region, mapscapeData, mapscapeBuffer)
                }

                region.hasFlags = isDynamic
                region.loaded = true

                val landscapeId = Cache.getArchiveId(CacheIndex.LANDSCAPES, "l$regionName")
                if (landscapeId > -1) {
                    val landscapeBytes = Cache.getData(
                        CacheIndex.LANDSCAPES, "l$regionName", XteaParser.getRegionXTEA(regionId)
                    ) ?: return

                    if (landscapeBytes.size < 4) return

                    region.hasFlags = true
                    try {
                        val landscapeBuffer = ByteBuffer.wrap(landscapeBytes)
                        LandscapeParser.parse(region, mapscapeData, landscapeBuffer, build)
                    } catch (t: Throwable) {
                        log(Region::class.java, Log.ERR, "Failed parsing region $regionId!")
                    }
                }

                MapscapeParser.clipMapscape(region, mapscapeData)

            } catch (e: Throwable) {
                log(Region::class.java, Log.ERR, "Exception while loading region: ${e.message}")
            }
        }

        /**
         * Attempts to unload a region, optionally forcing unload even if viewed.
         *
         * @param region The region to unload.
         * @param force Whether to force unload even if players are present.
         * @return True if unloaded successfully, false otherwise.
         */
        @JvmStatic
        fun unload(region: Region, force: Boolean = false): Boolean {
            if (!force && region.isViewed()) {
                log(CommunicationInfo::class.java, Log.ERR, "Players viewing region!")
                region.flagActive()
                return false
            }

            for (plane in region.planes) {
                if (!force && plane.players.isNotEmpty()) {
                    log(CommunicationInfo::class.java, Log.ERR, "Players still in region!")
                    region.flagActive()
                    return false
                }
            }

            for (plane in region.planes) {
                plane.clear()
                if (region !is DynamicRegion) {
                    plane.npcs.forEach { it.onRegionInactivity() }
                }
            }

            if (region.build) region.loaded = false
            region.activityPulse.stop()
            return true
        }
    }

    /**
     * Planes within the region (z-level layers).
     */
    val planes: Array<RegionPlane> = Array(4) { plane -> RegionPlane(this, plane) }

    /**
     * Pulse to handle region activity timeout and inactivity flagging.
     */
    private val activityPulse: Pulse = object : Pulse(50) {
        override fun pulse(): Boolean {
            flagInactive()
            return true
        }
    }.also { it.stop() }

    /**
     * Zones contained within the region.
     */
    val regionZones: MutableList<RegionZone> = ArrayList(20)

    /**
     * Music zones that affect the region's ambient music.
     */
    val musicZones: MutableList<MusicZone> = ArrayList(20)

    /**
     * Player tolerance timestamps used for inactivity checks.
     */
    private val tolerances: MutableMap<String, Long> = HashMap()

    /**
     * Current music id for the region, -1 if none.
     */
    var music: Int = -1

    /**
     * Whether the region is currently active (loaded and flagged).
     */
    var active: Boolean = false

    /**
     * Count of objects in the region.
     */
    var objectCount: Int = 0

    /**
     * Indicates if the region has map flags loaded.
     */
    var hasFlags: Boolean = false

    /**
     * Whether the region data has been fully loaded.
     */
    var loaded: Boolean = false

    /**
     * Number of players currently viewing the region.
     */
    var viewAmount: Int = 0

    /**
     * Whether the region is in build mode.
     */
    var build: Boolean = false

    /**
     * Whether all planes require updating.
     */
    var updateAllPlanes: Boolean = false

    /**
     * Base location coordinate of the region (lower-left corner).
     */
    val baseLocation: Location
        get() = Location.create(x shl 6, y shl 6, 0)

    /**
     * Adds a region zone to this region and updates zone monitors.
     *
     * @param zone The region zone to add.
     */
    fun add(zone: RegionZone) {
        regionZones.add(zone)
        planes.forEach { plane ->
            plane.npcs.forEach { it.zoneMonitor.updateLocation(it.location) }
            plane.players.forEach { it.zoneMonitor.updateLocation(it.location) }
        }
    }

    /**
     * Removes a region zone from this region and updates zone monitors.
     *
     * @param zone The region zone to remove.
     */
    fun remove(zone: RegionZone) {
        regionZones.remove(zone)
        planes.forEach { plane ->
            plane.npcs.forEach { it.zoneMonitor.updateLocation(it.location) }
            plane.players.forEach { it.zoneMonitor.updateLocation(it.location) }
        }
    }

    /**
     * Adds a player to the appropriate plane of this region.
     *
     * @param player The player to add.
     */
    fun add(player: Player) {
        planes[player.location.z].add(player)
        tolerances[player.username] = System.currentTimeMillis()
        flagActive()
    }

    /**
     * Adds an NPC to the appropriate plane of this region.
     *
     * @param npc The NPC to add.
     */
    fun add(npc: NPC) {
        planes[npc.location.z].add(npc)
    }

    /**
     * Removes an NPC from this region.
     *
     * @param npc The NPC to remove.
     */
    fun remove(npc: NPC) {
        val plane = npc.viewport.currentPlane
        if (plane != null && plane != planes[npc.location.z]) {
            plane.remove(npc)
        }
        planes[npc.location.z].remove(npc)
    }

    /**
     * Removes a player from this region.
     *
     * @param player The player to remove.
     */
    fun remove(player: Player) {
        player.viewport.currentPlane!!.remove(player)
        tolerances.remove(player.username)
        checkInactive()
    }

    /**
     * Checks if a player is tolerated (inactive tolerance period).
     *
     * @param player The player to check.
     * @return True if player is tolerated, false otherwise.
     */
    fun isTolerated(player: Player): Boolean {
        val lastTime = tolerances[player.username] ?: System.currentTimeMillis()
        val elapsed = System.currentTimeMillis() - lastTime
        return elapsed > TimeUnit.MINUTES.toMillis(10)
    }

    /**
     * Checks if the region is inactive and triggers inactivity pulse.
     *
     * @return True if inactive, false otherwise.
     */
    fun checkInactive(): Boolean = isInactive(true)

    /**
     * Determines if the region is inactive.
     *
     * @param runPulse Whether to run the inactivity pulse if inactive.
     * @return True if inactive, false otherwise.
     */
    fun isInactive(runPulse: Boolean): Boolean {
        if (isViewed()) return false
        if (planes.any { it.players.isNotEmpty() }) return false

        if (runPulse) {
            if (!activityPulse.isRunning) {
                activityPulse.restart()
                activityPulse.start()
                GameWorld.Pulser.submit(activityPulse)
            }
        }
        return true
    }
    /**
     * Checks if the inactivity pulse is currently running (pending removal).
     *
     * @return True if pending removal, false otherwise.
     */
    fun isPendingRemoval(): Boolean = activityPulse.isRunning

    /**
     * Flags the region as inactive, attempting to unload it.
     *
     * @param force Whether to force unload.
     * @return True if successfully unloaded, false otherwise.
     */
    fun flagActive() {
        activityPulse.stop()
        if (!active) {
            active = true
            load(this)
            for (plane in planes) {
                plane.npcs.filter { it.isActive }.forEach { Repository.addRenderableNPC(it) }
            }
        }
    }

    /**
     * Flags the region as inactive, attempting to unload it.
     *
     * @param force Whether to force unload.
     * @return True if successfully unloaded, false otherwise.
     */
    open fun flagInactive(force: Boolean = false): Boolean {
        val unloaded = unload(this, force)
        if (unloaded) active = false
        return unloaded
    }

    /**
     * Gets the region id from coordinates.
     */
    val id: Int get() = (x shl 8) or y

    /**
     * Gets the region id, overridable for dynamic regions.
     */
    open val regionId: Int get() = id

    /**
     * Checks if the region is currently viewed by players.
     *
     * @return True if viewed, false otherwise.
     */
    fun isViewed(): Boolean = synchronized(this) { viewAmount > 0 }

    /**
     * Increments the view count for the region.
     *
     * @return The new view count.
     */
    fun incrementViewAmount(): Int = synchronized(this) { ++viewAmount }

    /**
     * Decrements the view count for the region.
     *
     * @return The new view count.
     */
    fun decrementViewAmount(): Int = synchronized(this) {
        if (viewAmount < 1) {
            // log(this::class.java, Log.ERR, "View amount is ${viewAmount - 1}")
            viewAmount++
        }
        --viewAmount
    }
    /**
     * Sets the timeout ticks for the inactivity pulse.
     *
     * @param ticks Number of ticks before flagging inactive.
     */
    fun setRegionTimeOut(ticks: Int) {
        activityPulse.delay = ticks
    }
}
