package core.game.world.map.build

import core.game.world.map.Region
import java.nio.ByteBuffer

/**
 * A utility class used for parsing mapscapes.
 */
object MapscapeParser {

    /**
     * Parses the mapscape buffer.
     *
     * @param r The region.
     * @param mapscape The mapscape 3D array.
     * @param buffer The buffer.
     */
    @JvmStatic
    fun parse(r: Region, mapscape: Array<Array<ByteArray>>, buffer: ByteBuffer) {
        for (z in 0 until 4) {
            val landscape = r.planes[z].flags.landscape
            for (x in 0 until 64) {
                for (y in 0 until 64) {
                    while (true) {
                        val value = buffer.get().toInt() and 0xFF
                        when {
                            value == 0 -> break
                            value == 1 -> {
                                buffer.get()
                                break
                            }
                            value <= 49 -> {
                                val overlay = buffer.get().toInt() and 0xFF
                                if (overlay != 42 && overlay > 0) {
                                    landscape!![x][y] = true
                                }
                            }
                            value <= 81 -> {
                                mapscape[z][x][y] = (value - 49).toByte()
                            }
                            else -> {
                                val underlay = (value - 81) and 0xFF
                                if (underlay != 42 && underlay > 0) {
                                    landscape!![x][y] = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Clips the mapscape by setting solid tile flags.
     */
    @JvmStatic
    fun clipMapscape(r: Region, mapscape: Array<Array<ByteArray>>) {
        for (z in 0 until 4) {
            for (x in 0 until 64) {
                for (y in 0 until 64) {
                    r.planes[z].flags.flagEmptyTile(x, y)
                    if ((mapscape[z][x][y].toInt() and 0x1) == 1) {
                        var plane = z
                        if ((mapscape[1][x][y].toInt() and 0x2) == 2) {
                            plane--
                        }
                        if (plane >= 0) {
                            r.planes[plane].flags.flagSolidTile(x, y)
                        }
                    }
                }
            }
        }
    }
}
