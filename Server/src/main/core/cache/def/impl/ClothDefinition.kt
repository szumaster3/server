package core.cache.def.impl

import core.ServerConstants
import core.cache.Cache
import core.cache.Group
import core.cache.Archive
import core.net.g1
import core.net.g2
import core.net.g2s
import java.nio.ByteBuffer

/**
 * Cloth definition with models and color swaps.
 */
class ClothDefinition {
    var bodyPartId: Int = 0
    var bodyModelIds: IntArray? = null
    var notSelectable: Boolean = false
    var headModelIds: IntArray = intArrayOf(-1, -1, -1, -1, -1)
    private var originalColors: IntArray? = null
    private var modifiedColors: IntArray? = null
    private var originalTextureColors: IntArray? = null
    private var modifiedTextureColors: IntArray? = null

    companion object {
        /**
         * Load cloth definition by id from cache.
         */
        fun forId(clothId: Int): ClothDefinition {
            val def = ClothDefinition()
            val data = Cache.getData(Archive.JS5_CONFIG, Group.IDK_TYPE, clothId)
            data?.let { def.load(ByteBuffer.wrap(it)) }
            return def
        }

        /**
         * Load all cloths from cache.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                Cache.init(ServerConstants.CACHE_PATH)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            val length = Cache.getArchiveCapacity(Archive.JS5_CONFIG, Group.IDK_TYPE)
            for (i in 0 until length) forId(i)
        }
    }

    /**
     * Read cloth data from buffer.
     */
    fun load(buffer: ByteBuffer) {
        while (true) {
            when (val opcode = buffer.g1()) {
                0 -> return
                else -> decode(opcode, buffer)
            }
        }
    }

    /**
     * Decode opcode data.
     */
    private fun decode(opcode: Int, buffer: ByteBuffer) {
        when (opcode) {
            1 -> bodyPartId = buffer.g1()
            2 -> {
                val len = buffer.g1()
                bodyModelIds = IntArray(len) { buffer.g2() }
            }
            3 -> notSelectable = true
            40 -> {
                val len = buffer.g1()
                originalColors = IntArray(len) { buffer.g2s() }
                modifiedColors = IntArray(len) { buffer.g2s() }
            }
            41 -> {
                val len = buffer.g1()
                originalTextureColors = IntArray(len) { buffer.g2s() }
                modifiedTextureColors = IntArray(len) { buffer.g2s() }
            }
            in 60..69 -> headModelIds[opcode - 60] = buffer.g2()
        }
    }
}
