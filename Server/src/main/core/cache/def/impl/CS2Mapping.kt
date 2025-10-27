package core.cache.def.impl

import core.cache.Cache
import core.cache.Archive
import core.game.world.GameWorld
import core.net.g1
import core.net.g2
import core.net.g4
import core.net.gjstr
import java.io.BufferedWriter
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Represents a mapping for a CS2 script.
 */
class CS2Mapping private constructor(val scriptId: Int) {

    /**
     * Type of values stored in the map (e.g., int or string).
     */
    var valueType: Int = 0

    /**
     * Type of keys or additional flags for the mapping.
     */
    var keyType: Int = 0

    /**
     * Default string value if a key is missing.
     */
    var defaultString: String? = null

    /**
     * Default integer value if a key is missing.
     */
    var defaultInt: Int = 0

    /**
     * Map of keys to their corresponding values.
     */
    var map: HashMap<Int, Any>? = null

    /**
     * Parallel array of values for fast indexed access.
     */
    var array: Array<Any?>? = null

    companion object {
        private val maps = HashMap<Int, CS2Mapping>()

        /**
         * Dump all mappings to file.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            GameWorld.prompt(false)
            BufferedWriter(Files.newBufferedWriter(Paths.get("./cs2.txt"))).use { bw ->
                for (i in 0 until 10000) {
                    val mapping = forId(i) ?: continue
                    val mappingMap = mapping.map ?: continue
                    bw.append("ScriptAPI - $i [")
                    for ((index, value) in mappingMap) {
                        bw.append("$value: $index ")
                    }
                    bw.append("]")
                    bw.newLine()
                }
            }
        }

        /**
         * Load mapping by script id.
         */
        fun forId(scriptId: Int): CS2Mapping? {
            maps[scriptId]?.let { return it }
            val mapping = CS2Mapping(scriptId)
            val bs = Cache.getData(Archive.JS5_CONFIG_ENUM, scriptId ushr 8, scriptId and 0xFF) ?: return null
            mapping.load(ByteBuffer.wrap(bs))
            maps[scriptId] = mapping
            return mapping
        }
    }

    /**
     * Read mapping data from buffer.
     */
    private fun load(buffer: ByteBuffer) {
        while (true) {
            when (val opcode = buffer.g1()) {
                0 -> return
                1 -> valueType = buffer.g1()
                2 -> keyType = buffer.g1()
                3 -> defaultString = buffer.gjstr()
                4 -> defaultInt = buffer.g4()
                5, 6 -> {
                    val size = buffer.g2()
                    map = HashMap(size)
                    array = arrayOfNulls(size)
                    for (i in 0 until size) {
                        val key = buffer.g4()
                        val value: Any = if (opcode == 5) buffer.gjstr() else buffer.g4()
                        array!![i] = value
                        map!![key] = value
                    }
                }
            }
        }
    }
}
