package core.cache.def.impl

import core.api.log
import core.cache.Cache
import core.cache.Archive
import core.cache.util.ByteBufferExtensions
import core.tools.Log
import core.tools.StringUtils
import java.nio.ByteBuffer

class DataMap private constructor(val id: Int) {

    var keyType: Char = '?'
    var valueType: Char = '?'
    var defaultString: String? = null
    var defaultInt: Int = 0
    val dataStore: MutableMap<Int, Any> = mutableMapOf()

    fun getInt(key: Int): Int {
        if (!dataStore.containsKey(key)) {
            log(this::class.java, Log.ERR, "Invalid value passed for key: $key map: $id")
            return -1
        }
        return dataStore[key] as? Int ?: -1
    }

    fun getString(key: Int): String? {
        return dataStore[key] as? String
    }

    override fun toString(): String {
        return buildString {
            append("DataMapDefinition{")
            append("id=$id, ")
            append("keyType=$keyType, ")
            append("valueType=${when (valueType) {
                'K' -> "Normal"
                'J' -> "Struct Pointer"
                else -> "Unknown"
            }}, ")
            append("defaultString='$defaultString', ")
            append("defaultInt=$defaultInt, ")
            append("dataStore=$dataStore")
            append("}\n")
        }
    }

    companion object {
        private val definitions = mutableMapOf<Int, DataMap>()

        fun get(id: Int): DataMap {
            definitions[id]?.let { return it }

            val archive = id ushr 8
            val file = id and 0xFF

            val data = Cache.getData(Archive.JS5_CONFIG_ENUM, archive, file)
            val def = parse(id, data)
            definitions[id] = def
            return def
        }

        private fun parse(id: Int, data: ByteArray?): DataMap {
            val def = DataMap(id)

            data?.let {
                val buffer = ByteBuffer.wrap(it)

                while (true) {
                    val opcode = buffer.get().toInt() and 0xFF
                    if (opcode == 0) break

                    when (opcode) {
                        1 -> def.keyType = StringUtils.getFromByte(buffer.get())
                        2 -> def.valueType = StringUtils.getFromByte(buffer.get())
                        3 -> def.defaultString = ByteBufferExtensions.getString(buffer)
                        4 -> def.defaultInt = buffer.int
                        5, 6 -> {
                            val size = buffer.short.toInt() and 0xFFFF
                            repeat(size) {
                                val key = buffer.int
                                val value: Any = if (opcode == 5) {
                                    ByteBufferExtensions.getString(buffer)
                                } else {
                                    buffer.int
                                }
                                def.dataStore[key] = value
                            }
                        }
                    }
                }
            }

            return def
        }
    }
}
