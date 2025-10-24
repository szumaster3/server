package core.cache.def.impl

import core.api.log
import core.cache.Cache
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.cache.util.ByteBufferExtensions
import core.net.g1
import core.net.g4
import core.net.gjstr
import core.tools.Log
import java.nio.ByteBuffer

/**
 * Represents a struct storing key-value pairs with mixed data types.
 */
class Struct(val id: Int) {
    val dataStore = mutableMapOf<Int, Any>()

    /**
     * Gets an integer value by key.
     *
     * @param key The key to lookup.
     * @return The integer value, or -1 if not found or invalid type.
     */
    fun getInt(key: Int): Int = dataStore[key] as? Int ?: run {
        log(javaClass, Log.ERR, "Invalid value passed for key: [$key] struct: [$id]")
        -1
    }

    /**
     * Gets a string value by key.
     *
     * @param key The key to lookup.
     * @return The string value, or null if not found or invalid type.
     */
    fun getString(key: Int): String? = dataStore[key] as? String

    override fun toString(): String = "Struct(id=$id, dataStore=$dataStore)"

    companion object {
        private val definitions = mutableMapOf<Int, Struct>()

        /**
         * Gets the [Struct] instance for the given id.
         * Loads and parses it from cache if not already loaded.
         *
         * @param id The struct id.
         * @return The [Struct] instance.
         */
        fun get(id: Int): Struct = definitions[id] ?: run {
            val data = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.STRUCT_TYPE, id)
            decode(id, data).also { definitions[id] = it }
        }

        /**
         * Parses raw byte data into a [Struct] instance.
         *
         * @param id The struct id.
         * @param data The raw byte data.
         * @return The parsed [Struct].
         */
        fun decode(id: Int, data: ByteArray?): Struct {
            val struct = Struct(id)
            data?.let {
                val buffer = ByteBuffer.wrap(it)
                while (true) {
                    val opcode = buffer.g1()
                    if (opcode == 0) break
                    if (opcode == 249) {
                        val size = buffer.g1()
                        repeat(size) {
                            val isString = buffer.g1() == 1
                            val key = ByteBufferExtensions.getMedium(buffer)
                            val value: Any = if (isString) buffer.gjstr() else buffer.g4()
                            struct.dataStore[key] = value
                        }
                    }
                }
            }
            return struct
        }
    }
}
