package core.game.world.map.build

import core.game.world.map.RegionManager
import kotlin.math.max

class RegionFlags(
    val plane: Int, private val baseX: Int, private val baseY: Int, private val projectile: Boolean = false
) {

    var isMembers: Boolean = false
    var landscape: Array<BooleanArray>? = null
    var clippingFlags: Array<IntArray>? = null
    var projectileFlags: Array<IntArray>? = null

    companion object {
        const val TILE_OBJECT = 0x40000
        const val EMPTY_TILE = 0
        const val SOLID_TILE = 0x200000
        const val OBJ_10_PROJECTILE = 0x20000
        const val OBJ_10 = 0x100
    }

    fun flagSolidTile(x: Int, y: Int) = flag(x, y, SOLID_TILE)
    fun flagEmptyTile(x: Int, y: Int) = flag(x, y, EMPTY_TILE)
    fun flagTileObject(x: Int, y: Int) = flag(x, y, TILE_OBJECT)
    fun unflagTileObject(x: Int, y: Int) = unflag(x, y, TILE_OBJECT)

    fun flagSolidObject(x: Int, y: Int, sizeX: Int, sizeY: Int, projectileClipped: Boolean) {
        var clipdata = OBJ_10
        if (projectileClipped) clipdata += OBJ_10_PROJECTILE
        for (i in x until x + sizeX) {
            for (j in y until y + sizeY) {
                flag(i, j, clipdata)
            }
        }
    }

    fun unflagSolidObject(x: Int, y: Int, sizeX: Int, sizeY: Int, projectileClipped: Boolean) {
        var clipdata = OBJ_10
        if (projectileClipped) clipdata += OBJ_10_PROJECTILE
        for (i in x until x + sizeX) {
            for (j in y until y + sizeY) {
                unflag(i, j, clipdata)
            }
        }
    }

    fun flag(x: Int, y: Int, clipdata: Int) {
        if (x in 0 until 64 && y in 0 until 64) {
            addFlag(x, y, clipdata)
        } else {
            RegionManager.addClippingFlag(plane, baseX + x, baseY + y, projectile, clipdata)
        }
    }

    fun unflag(x: Int, y: Int, clipdata: Int) {
        if (x in 0 until 64 && y in 0 until 64) {
            removeFlag(x, y, clipdata)
        } else {
            RegionManager.removeClippingFlag(plane, baseX + x, baseY + y, projectile, clipdata)
        }
    }

    fun getFlag(x: Int, y: Int): Int {
        val regionId = (baseX shr 6 shl 8) or (baseY shr 6)
        val index = (plane * 64 * 64) + (x * 64) + y
        return RegionManager.getFlags(regionId, projectile)[index]
    }

    fun addFlag(x: Int, y: Int, clipdata: Int) {
        val regionId = (baseX shr 6 shl 8) or (baseY shr 6)
        val index = (plane * 64 * 64) + (x * 64) + y
        val current = getFlag(x, y)
        RegionManager.getFlags(regionId, projectile)[index] = max(0, current) or clipdata
    }

    fun removeFlag(x: Int, y: Int, clipdata: Int) {
        val regionId = (baseX shr 6 shl 8) or (baseY shr 6)
        val index = (plane * 64 * 64) + (x * 64) + y
        val current = getFlag(x, y)
        if ((current and clipdata) == 0) return
        RegionManager.getFlags(regionId, projectile)[index] = max(0, current) and clipdata.inv()
    }

    fun clearFlag(x: Int, y: Int) {
        val regionId = (baseX shr 6 shl 8) or (baseY shr 6)
        val index = (plane * 64 * 64) + (x * 64) + y
        RegionManager.getFlags(regionId, projectile)[index] = 0
    }

    fun invalidateFlag(x: Int, y: Int) {
        val regionId = (baseX shr 6 shl 8) or (baseY shr 6)
        val index = (plane * 64 * 64) + (x * 64) + y
        RegionManager.getFlags(regionId, projectile)[index] = -1
    }

    fun flagDoorObject(x: Int, y: Int, rotation: Int, type: Int, projectileClipped: Boolean) {
        flagOrUnflagDoor(x, y, rotation, type, projectileClipped, ::flag)
    }

    fun unflagDoorObject(x: Int, y: Int, rotation: Int, type: Int, projectileClipped: Boolean) {
        flagOrUnflagDoor(x, y, rotation, type, projectileClipped, ::unflag)
    }

    private fun flagOrUnflagDoor(
        x: Int, y: Int, rotation: Int, type: Int, projectileClipped: Boolean, action: (Int, Int, Int) -> Unit
    ) {
        when (type) {
            0 -> when (rotation) {
                0 -> {
                    action(x, y, 0x80)
                    action(x - 1, y, 0x8)
                }

                1 -> {
                    action(x, y, 0x2)
                    action(x, y + 1, 0x20)
                }

                2 -> {
                    action(x, y, 0x8)
                    action(x + 1, y, 0x80)
                }

                3 -> {
                    action(x, y, 0x20)
                    action(x, y - 1, 0x2)
                }
            }

            1, 3 -> when (rotation) {
                0 -> {
                    action(x, y, 0x1)
                    action(x - 1, y + 1, 0x10)
                }

                1 -> {
                    action(x, y, 0x4)
                    action(x + 1, y + 1, 0x40)
                }

                2 -> {
                    action(x, y, 0x10)
                    action(x + 1, y - 1, 0x1)
                }

                3 -> {
                    action(x, y, 0x40)
                    action(x - 1, y - 1, 0x4)
                }
            }

            2 -> when (rotation) {
                0 -> {
                    action(x, y, 0x82)
                    action(x - 1, y, 0x8)
                    action(x, y + 1, 0x20)
                }

                1 -> {
                    action(x, y, 0xA)
                    action(x, y + 1, 0x20)
                    action(x + 1, y, 0x80)
                }

                2 -> {
                    action(x, y, 0x28)
                    action(x + 1, y, 0x80)
                    action(x, y - 1, 0x2)
                }

                3 -> {
                    action(x, y, 0xA0)
                    action(x, y - 1, 0x2)
                    action(x - 1, y, 0x8)
                }
            }
        }

        if (!projectileClipped) return

        when (type) {
            0 -> when (rotation) {
                0 -> {
                    action(x, y, 0x10000)
                    action(x - 1, y, 0x1000)
                }

                1 -> {
                    action(x, y, 0x400)
                    action(x, y + 1, 0x4000)
                }

                2 -> {
                    action(x, y, 0x1000)
                    action(x + 1, y, 0x10000)
                }

                3 -> {
                    action(x, y, 0x4000)
                    action(x, y - 1, 0x400)
                }
            }

            1, 3 -> when (rotation) {
                0 -> {
                    action(x, y, 0x200)
                    action(x - 1, y + 1, 0x2000)
                }

                1 -> {
                    action(x, y, 0x800)
                    action(x + 1, y + 1, 0x8000)
                }

                2 -> {
                    action(x, y, 0x2000)
                    action(x + 1, y - 1, 0x200)
                }

                3 -> {
                    action(x, y, 0x8000)
                    action(x - 1, y - 1, 0x800)
                }
            }

            2 -> when (rotation) {
                0 -> {
                    action(x, y, 0x10400)
                    action(x - 1, y, 0x1000)
                    action(x, y + 1, 0x4000)
                }

                1 -> {
                    action(x, y, 0x1400)
                    action(x, y + 1, 0x4000)
                    action(x + 1, y, 0x10000)
                }

                2 -> {
                    action(x, y, 0x5000)
                    action(x + 1, y, 0x10000)
                    action(x, y - 1, 0x400)
                }

                3 -> {
                    action(x, y, 0x14000)
                    action(x, y - 1, 0x400)
                    action(x - 1, y, 0x1000)
                }
            }
        }
    }
}