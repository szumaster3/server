package core.game.world.map.build

import core.game.world.map.RegionManager
import kotlin.math.max

/**
 * Holds a region's flags like clipping and members.
 * Supports local (0..63) and global flags.
 *
 * @author Emperor
 */
class RegionFlags(
    val plane: Int,
    val baseX: Int,
    val baseY: Int,
    private val projectile: Boolean = false
) {

    /**
     * True if region is members-only.
     */
    var members: Boolean = false

    /**
     * Optional landscape data for the region.
     */
    var landscape: Array<BooleanArray>? = null

    companion object {
        const val TILE_OBJECT = 0x40000
        const val EMPTY_TILE = 0
        const val SOLID_TILE = 0x200000
        const val OBJ_10_PROJECTILE = 0x20000
        const val OBJ_10 = 0x100
    }

    constructor(plane: Int, x: Int, y: Int) : this(plane, x, y, false)

    /**
     * Flag a solid tile.
     */
    fun flagSolidTile(x: Int, y: Int) = flag(x, y, SOLID_TILE)

    /**
     * Flag an empty tile.
     */
    fun flagEmptyTile(x: Int, y: Int) = flag(x, y, EMPTY_TILE)

    /**
     * Flag a tile object (type 22).
     */
    fun flagTileObject(x: Int, y: Int) = flag(x, y, TILE_OBJECT)

    /**
     * Unflag a tile object.
     */
    fun unflagTileObject(x: Int, y: Int) = unflag(x, y, TILE_OBJECT)

    /**
     * Flag a solid object.
     *
     * @param projectileClipped True if blocks projectiles.
     */
    fun flagSolidObject(x: Int, y: Int, sizeX: Int, sizeY: Int, projectileClipped: Boolean) {
        var clipdata = OBJ_10
        if (projectileClipped) clipdata += OBJ_10_PROJECTILE
        for (i in x until x + sizeX) {
            for (j in y until y + sizeY) {
                flag(i, j, clipdata)
            }
        }
    }

    /**
     * Unflag a solid object.
     */
    fun unflagSolidObject(x: Int, y: Int, sizeX: Int, sizeY: Int, projectileClipped: Boolean) {
        var clipdata = OBJ_10
        if (projectileClipped) clipdata += OBJ_10_PROJECTILE
        for (i in x until x + sizeX) {
            for (j in y until y + sizeY) {
                unflag(i, j, clipdata)
            }
        }
    }

    /**
     * Add a flag locally or globally if out of bounds.
     */
    fun flag(x: Int, y: Int, clipdata: Int) {
        if (x in 0..63 && y in 0..63) addFlag(x, y, clipdata)
        else RegionManager.addClippingFlag(plane, baseX + x, baseY + y, projectile, clipdata)
    }

    /**
     * Remove a flag locally or globally if out of bounds.
     */
    fun unflag(x: Int, y: Int, clipdata: Int) {
        if (x in 0..63 && y in 0..63) removeFlag(x, y, clipdata)
        else RegionManager.removeClippingFlag(plane, baseX + x, baseY + y, projectile, clipdata)
    }

    /**
     * Get internal indices for a tile.
     */
    private fun getFlagIndex(x: Int, y: Int): Pair<Int, Int> =
        Pair(((baseX shr 6) shl 8) or (baseY shr 6), (plane * 64 * 64) + (x * 64) + y)

    /**
     * Get flag value for a tile.
     */
    fun getFlag(x: Int, y: Int): Int {
        val (regionIndex, tileIndex) = getFlagIndex(x, y)
        return RegionManager.getFlags(regionIndex, projectile)[tileIndex]
    }

    /**
     * Add a flag locally.
     */
    fun addFlag(x: Int, y: Int, clipdata: Int) {
        val current = getFlag(x, y)
        val (regionIndex, tileIndex) = getFlagIndex(x, y)
        RegionManager.getFlags(regionIndex, projectile)[tileIndex] = max(0, current) or clipdata
    }

    /**
     * Remove a flag locally.
     */
    fun removeFlag(x: Int, y: Int, clipdata: Int) {
        var current = getFlag(x, y)
        val (regionIndex, tileIndex) = getFlagIndex(x, y)
        if ((current and clipdata) == 0) return
        current = max(0, current) and clipdata.inv()
        RegionManager.getFlags(regionIndex, projectile)[tileIndex] = current
    }

    /**
     * Clear a tile's flags.
     */
    fun clearFlag(x: Int, y: Int) {
        val (regionIndex, tileIndex) = getFlagIndex(x, y)
        RegionManager.getFlags(regionIndex, projectile)[tileIndex] = 0
    }

    /**
     * Invalidate a tile's flags.
     */
    fun invalidateFlag(x: Int, y: Int) {
        val (regionIndex, tileIndex) = getFlagIndex(x, y)
        RegionManager.getFlags(regionIndex, projectile)[tileIndex] = -1
    }

    /**
     * Flag a door object (0-3 type).
     */
    fun flagDoorObject(x: Int, y: Int, rotation: Int, type: Int, projectileClipped: Boolean) {
        when (type) {
            0 -> when (rotation) {
                0 -> { flag(x, y, 0x80); flag(x - 1, y, 0x8) }
                1 -> { flag(x, y, 0x2); flag(x, y + 1, 0x20) }
                2 -> { flag(x, y, 0x8); flag(x + 1, y, 0x80) }
                3 -> { flag(x, y, 0x20); flag(x, y - 1, 0x2) }
            }
            1, 3 -> when (rotation) {
                0 -> { flag(x, y, 0x1); flag(x - 1, y + 1, 0x10) }
                1 -> { flag(x, y, 0x4); flag(x + 1, y + 1, 0x40) }
                2 -> { flag(x, y, 0x10); flag(x + 1, y - 1, 0x1) }
                3 -> { flag(x, y, 0x40); flag(x - 1, y - 1, 0x4) }
            }
            2 -> when (rotation) {
                0 -> { flag(x, y, 0x82); flag(x - 1, y, 0x8); flag(x, y + 1, 0x20) }
                1 -> { flag(x, y, 0xA); flag(x, y + 1, 0x20); flag(x + 1, y, 0x80) }
                2 -> { flag(x, y, 0x28); flag(x + 1, y, 0x80); flag(x, y - 1, 0x2) }
                3 -> { flag(x, y, 0xA0); flag(x, y - 1, 0x2); flag(x - 1, y, 0x8) }
            }
        }

        if (projectileClipped) {
            when (type) {
                0 -> when (rotation) {
                    0 -> { flag(x, y, 0x10000); flag(x - 1, y, 0x1000) }
                    1 -> { flag(x, y, 0x400); flag(x, y + 1, 0x4000) }
                    2 -> { flag(x, y, 0x1000); flag(x + 1, y, 0x10000) }
                    3 -> { flag(x, y, 0x4000); flag(x, y - 1, 0x400) }
                }
                1, 3 -> when (rotation) {
                    0 -> { flag(x, y, 0x200); flag(x - 1, y + 1, 0x2000) }
                    1 -> { flag(x, y, 0x800); flag(x + 1, y + 1, 0x8000) }
                    2 -> { flag(x, y, 0x2000); flag(x + 1, y - 1, 0x200) }
                    3 -> { flag(x, y, 0x8000); flag(x - 1, y - 1, 0x800) }
                }
                2 -> when (rotation) {
                    0 -> { flag(x, y, 0x10400); flag(x - 1, y, 0x1000); flag(x, y + 1, 0x4000) }
                    1 -> { flag(x, y, 0x1400); flag(x, y + 1, 0x4000); flag(x + 1, y, 0x10000) }
                    2 -> { flag(x, y, 0x5000); flag(x + 1, y, 0x10000); flag(x, y - 1, 0x400) }
                    3 -> { flag(x, y, 0x14000); flag(x, y - 1, 0x400); flag(x - 1, y, 0x1000) }
                }
            }
        }
    }

    /**
     * Unflag a door object (0-3 type).
     */
    fun unflagDoorObject(x: Int, y: Int, rotation: Int, type: Int, projectileClipped: Boolean) {
        when (type) {
            0 -> when (rotation) {
                0 -> { unflag(x, y, 0x80); unflag(x - 1, y, 0x8) }
                1 -> { unflag(x, y, 0x2); unflag(x, y + 1, 0x20) }
                2 -> { unflag(x, y, 0x8); unflag(x + 1, y, 0x80) }
                3 -> { unflag(x, y, 0x20); unflag(x, y - 1, 0x2) }
            }
            1, 3 -> when (rotation) {
                0 -> { unflag(x, y, 0x1); unflag(x - 1, y + 1, 0x10) }
                1 -> { unflag(x, y, 0x4); unflag(x + 1, y + 1, 0x40) }
                2 -> { unflag(x, y, 0x10); unflag(x + 1, y - 1, 0x1) }
                3 -> { unflag(x, y, 0x40); unflag(x - 1, y - 1, 0x4) }
            }
            2 -> when (rotation) {
                0 -> { unflag(x, y, 0x82); unflag(x - 1, y, 0x8); unflag(x, y + 1, 0x20) }
                1 -> { unflag(x, y, 0xA); unflag(x, y + 1, 0x20); unflag(x + 1, y, 0x80) }
                2 -> { unflag(x, y, 0x28); unflag(x + 1, y, 0x80); unflag(x, y - 1, 0x2) }
                3 -> { unflag(x, y, 0xA0); unflag(x, y - 1, 0x2); unflag(x - 1, y, 0x8) }
            }
        }

        if (projectileClipped) {
            when (type) {
                0 -> when (rotation) {
                    0 -> { unflag(x, y, 0x10000); unflag(x - 1, y, 0x1000) }
                    1 -> { unflag(x, y, 0x400); unflag(x, y + 1, 0x4000) }
                    2 -> { unflag(x, y, 0x1000); unflag(x + 1, y, 0x10000) }
                    3 -> { unflag(x, y, 0x4000); unflag(x, y - 1, 0x400) }
                }
                1, 3 -> when (rotation) {
                    0 -> { unflag(x, y, 0x200); unflag(x - 1, y + 1, 0x2000) }
                    1 -> { unflag(x, y, 0x800); unflag(x + 1, y + 1, 0x8000) }
                    2 -> { unflag(x, y, 0x2000); unflag(x + 1, y - 1, 0x200) }
                    3 -> { unflag(x, y, 0x8000); unflag(x - 1, y - 1, 0x800) }
                }
                2 -> when (rotation) {
                    0 -> { unflag(x, y, 0x10400); unflag(x - 1, y, 0x1000); unflag(x, y + 1, 0x4000) }
                    1 -> { unflag(x, y, 0x1400); unflag(x, y + 1, 0x4000); unflag(x + 1, y, 0x10000) }
                    2 -> { unflag(x, y, 0x5000); unflag(x + 1, y, 0x10000); unflag(x, y - 1, 0x400) }
                    3 -> { unflag(x, y, 0x14000); unflag(x, y - 1, 0x400); unflag(x - 1, y, 0x1000) }
                }
            }
        }
    }
}
