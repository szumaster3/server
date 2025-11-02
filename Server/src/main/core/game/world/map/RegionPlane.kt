package core.game.world.map

import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.scenery.Scenery
import core.game.world.map.build.DynamicRegion
import core.game.world.map.build.RegionFlags
import core.game.world.update.flag.chunk.ItemUpdateFlag
import core.net.packet.PacketRepository.send
import core.net.packet.context.BuildItemContext
import core.net.packet.out.ClearGroundItem
import core.net.packet.out.ConstructGroundItem
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Represents one of the 4 planes of a region.
 *
 * @author Emperor
 */
class RegionPlane(
    /**
     * The region.
     */
    val region: Region,
    /**
     * The plane.
     */
    val plane: Int
) {

    /**
     * The region flags.
     */
    val flags: RegionFlags

    /**
     * The region projectile flags.
     */
    val projectileFlags: RegionFlags

    /**
     * The region chunks.
     */
    var chunks: Array<Array<RegionChunk?>>

    /**
     * The list of NPCs in this region.
     */
    internal val npcs: MutableList<NPC> = CopyOnWriteArrayList()

    /**
     * The list of players in this region.
     */
    internal val players: MutableList<Player> = CopyOnWriteArrayList()

    /**
     * The scenerys.
     */
    var objects: Array<Array<Scenery?>>?
        private set

    /**
     * Constructs a new `RegionPlane` `Object`.
     *
     * @param region The region.
     * @param plane  The plane.
     */
    init {
        val base = region.baseLocation
        this.flags = RegionFlags(plane, base.x, base.y, false)
        this.projectileFlags = RegionFlags(plane, base.x, base.y, true)
        this.objects = Array(REGION_SIZE) { arrayOfNulls(REGION_SIZE) }
        this.chunks = Array(CHUNK_SIZE) { arrayOfNulls(CHUNK_SIZE) }
    }

    /**
     * Called at the end of the update sequence, if the region is active.
     */
    fun pulse() {
        chunks?.forEach { regionChunks ->
            regionChunks?.forEach { chunk ->
                chunk?.resetFlags()
            }
        }
    }

    /**
     * Adds a scenery.
     *
     * @param object    The object to add.
     * @param x         The x-coordinate.
     * @param y         The y-coordinate.
     * @param landscape If this object is added through landscape parsing.
     */
    fun add(`object`: Scenery?, x: Int, y: Int, landscape: Boolean) {
        setChunkObject(x, y, `object`)
        if (landscape) {
            objects!![x][y] = `object`
        }
        if (`object` != null) {
            `object`.isRenderable = true
        }
    }

    /**
     * Gets the region chunk.
     *
     * @param chunkX The chunk base x-coordinate.
     * @param chunkY The chunk base y-coordinate.
     * @return The region chunk.
     */
    fun getRegionChunk(chunkX: Int, chunkY: Int): RegionChunk {
        val r = chunks[chunkX][chunkY]
        if (r != null) {
            return r
        }
        if (region.isBuild) {
            return BuildRegionChunk(
                region.baseLocation.transform(chunkX shl 3, chunkY shl 3, plane),
                0,
                this
            ).also { chunks[chunkX][chunkY] = it }
        }
        return RegionChunk(
            region.baseLocation.transform(chunkX shl 3, chunkY shl 3, plane),
            0,
            this
        ).also { chunks[chunkX][chunkY] = it }
    }

    fun setRegionChunk(chunkX: Int, chunkY: Int, chunk: RegionChunk) {
        chunks[chunkX][chunkY] = chunk
    }

    /**
     * Removes a scenery.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    @JvmOverloads
    fun remove(x: Int, y: Int, objectId: Int = -1) {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        val offsetX = x - chunkX * CHUNK_SIZE
        val offsetY = y - chunkY * CHUNK_SIZE
        val chunk = getRegionChunk(chunkX, chunkY)

        val oldObject: Scenery?
        if (chunk is BuildRegionChunk) {
            val index = chunk.getIndex(offsetX, offsetY, objectId)
            oldObject = chunk.getObjects(index)[offsetX][offsetY]
            val remove = Scenery(
                0, region.baseLocation.transform(
                    x, y,
                    plane
                ), 22, 0
            )
            remove.isRenderable = false

            if (oldObject != null) {
            }

            chunk.getObjects(index)[offsetX][offsetY] = remove
            return
        }

        oldObject = chunk.getObjects()[offsetX][offsetY]
        val remove = Scenery(
            0, region.baseLocation.transform(
                x, y,
                plane
            ), 22, 0
        )
        remove.isRenderable = false

        if (oldObject != null) {
        }

        chunk.getObjects()[offsetX][offsetY] = remove
    }

    /**
     * Sets an object on a chunk.
     *
     * @param x      The regional x-coordinate.
     * @param y      The regional y-coordinate.
     * @param object The object to set.
     */
    private fun setChunkObject(x: Int, y: Int, `object`: Scenery?) {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        val offsetX = x - chunkX * CHUNK_SIZE
        val offsetY = y - chunkY * CHUNK_SIZE
        val r = getRegionChunk(chunkX, chunkY)
        if (r is BuildRegionChunk) {
            r.store(`object`)
            return
        }
        r.getObjects()[offsetX][offsetY] = `object`
    }

    val objectList: List<Scenery?>
        get() {
            val list: ArrayList<Scenery?> = ArrayList<Scenery?>()
            for (x in 0 until REGION_SIZE) {
                for (y in 0 until REGION_SIZE) {
                    if (objects!![x][y] != null) list.add(objects!![x][y])
                }
            }
            return list
        }

    /**
     * Clears this region plane.
     */
    fun clear() {
        if (chunks != null) {
            for (c in chunks) {
                for (chunk in c) {
                    chunk?.clear()
                }
            }
        }
        if (region is DynamicRegion && objects != null) {
            for (x in 0 until REGION_SIZE) {
                for (y in 0 until REGION_SIZE) {
                    objects!![x][y] = null
                }
            }
            objects = null
        }
    }

    /**
     * Adds an NPC to this region.
     *
     * @param npc The NPC to add.
     */
    fun add(npc: NPC) {
        npcs.add(npc)
    }

    /**
     * Adds a player to this region.
     *
     * @param player The player.
     */
    fun add(player: Player) {
        players.add(player)
    }

    /**
     * Adds an item to this region.
     *
     * @param item The item.
     */
    fun add(item: GroundItem) {
        val l = item.location
        val c = getRegionChunk(l.localX / RegionChunk.SIZE, l.localY / RegionChunk.SIZE)
        if (!c.getItems().add(item)) {
            return
        }
        val g = item
        if (g.isPrivate) {
            if (g.dropper != null) {
                send(ConstructGroundItem::class.java, BuildItemContext(g.dropper, item))
            }
            return
        }
        c.flag(ItemUpdateFlag(g, ItemUpdateFlag.CONSTRUCT_TYPE))
    }

    /**
     * Removes an NPC from this region.
     *
     * @param npc The NPC.
     */
    fun remove(npc: NPC) {
        npcs.remove(npc)
    }

    /**
     * Removes a player from this region.
     *
     * @param player The player.
     */
    fun remove(player: Player) {
        players.remove(player)
    }

    /**
     * Removes an item from this region.
     *
     * @param item The ground item.
     */
    fun remove(item: GroundItem) {
        val l = item.location
        val c = getRegionChunk(l.localX / RegionChunk.SIZE, l.localY / RegionChunk.SIZE)
        if (!c.getItems().remove(item)) {
            return
        }
        if (item.isPrivate) {
            // https://runescape.wiki/w/Drops
            if (item.dropper != null && item.dropper.isPlaying && item.dropper.location.withinDistance(l)) {
                send(ClearGroundItem::class.java, BuildItemContext(item.dropper, item))
            }
            return
        }
        c.flag(ItemUpdateFlag(item, ItemUpdateFlag.REMOVE_TYPE))
    }

    /**
     * Gets the npcs.
     *
     * @return The npcs.
     */
    fun getNpcs(): List<NPC> {
        return npcs
    }

    val entities: List<Node>
        get() {
            val entities = ArrayList<Node>(npcs)
            objects?.forEach { row ->
                row?.filterNotNull()?.let { entities.addAll(it) }
            }
            return entities
        }

    /**
     * Gets the players.
     *
     * @return The players.
     */
    fun getPlayers(): List<Player> {
        return players
    }

    /**
     * Gets an object from a region chunk.
     *
     * @param x The region x-coordinate.
     * @param y The region y-coordinate.
     * @return The scenery.
     */
    fun getChunkObject(x: Int, y: Int): Scenery? {
        return getChunkObject(x, y, -1)
    }

    /**
     * Gets an object from a region chunk.
     *
     * @param x        The region x-coordinate.
     * @param y        The region y-coordinate.
     * @param objectId The object id.
     * @return The scenery.
     */
    fun getChunkObject(x: Int, y: Int, objectId: Int): Scenery? {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        val offsetX = x - chunkX * CHUNK_SIZE
        val offsetY = y - chunkY * CHUNK_SIZE
        val chunk = getRegionChunk(chunkX, chunkY) ?: return null

        if (chunk is BuildRegionChunk) {
            val brc = chunk
            return brc[offsetX, offsetY, brc.getIndex(offsetX, offsetY, objectId)]
        }

        val objects = chunk.getObjects() ?: return null

        if (offsetX !in objects.indices || offsetY !in objects[offsetX].indices) {
            return null
        }

        return objects[offsetX][offsetY]
    }

    /**
     * Gets an object from a region chunk.
     *
     * @param x The region x-coordinate.
     * @param y The region y-coordinate.
     * @return The scenery.
     */
    fun getChunkItems(x: Int, y: Int): List<GroundItem> {
        val chunkX = x / CHUNK_SIZE
        val chunkY = y / CHUNK_SIZE
        return getRegionChunk(chunkX, chunkY).getItems()
    }

    /**
     * Gets a ground item from this plane.
     *
     * @param itemId The item id.
     * @param l      The location.
     * @param player The player.
     * @return The item.
     */
    fun getItem(itemId: Int, l: Location, player: Player?): GroundItem? {
        var groundItem: GroundItem? = null
        for (item in getChunkItems(l.localX, l.localY)) {
            val g = item
            if (g.id == itemId && l == g.location && !g.isRemoved) {
                if (groundItem != null && (!g.isPrivate || player == null)) {
                    continue
                }
                if ((!g.isPrivate || player == null || g.droppedBy(player))) {
                    groundItem = g
                }
            }
        }
        return groundItem
    }

    companion object {
        /**
         * The region size.
         */
        const val REGION_SIZE: Int = 64

        /**
         * The amount of chunks in this plane.
         */
        const val CHUNK_SIZE: Int = REGION_SIZE shr 3

        /**
         * Represents a removed scenery.
         */
        val NULL_OBJECT: Scenery = Scenery(0, Location.create(0, 0, 0))
    }
}
