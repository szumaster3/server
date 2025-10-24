package core.cache.util

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object ByteBufferExtensions {
    @JvmStatic
    fun getString(buffer: ByteBuffer): String {
        val sb = StringBuilder()
        var b: Byte
        while (buffer.hasRemaining()) {
            b = buffer.get()
            if (b.toInt() == 0) break
            sb.append(b.toChar())
        }
        return sb.toString()
    }

    @JvmStatic
    fun putString(s: String, buffer: ByteBuffer) {
        buffer.put(s.toByteArray(StandardCharsets.UTF_8)).put(0.toByte())
    }

    @JvmStatic
    fun packGJString2(position: Int, buffer: ByteArray, string: String): Int {
        var offset = position
        for (ch in string) {
            val character = ch.code
            if (character > 127) {
                if (character > 2047) {
                    buffer[offset++] = ((character or 919275) shr 12).toByte()
                    buffer[offset++] = (128 or ((character shr 6) and 63)).toByte()
                    buffer[offset++] = (128 or (character and 63)).toByte()
                } else {
                    buffer[offset++] = ((character or 12309) shr 6).toByte()
                    buffer[offset++] = (128 or (character and 63)).toByte()
                }
            } else {
                buffer[offset++] = character.toByte()
            }
        }
        return offset - position
    }

    @JvmStatic
    fun getMedium(buffer: ByteBuffer): Int =
        ((buffer.get().toInt() and 0xFF) shl 16) + ((buffer.get().toInt() and 0xFF) shl 8) + (buffer.get()
            .toInt() and 0xFF)

    @JvmStatic
    fun getSmart(buffer: ByteBuffer): Int {
        val peek = buffer.get().toInt() and 0xFF
        return if (peek <= Byte.MAX_VALUE.toInt()) {
            peek
        } else {
            ((peek shl 8) or (buffer.get().toInt() and 0xFF)) - 32768
        }
    }

    @JvmStatic
    fun getBigSmart(buffer: ByteBuffer): Int {
        var value = 0
        var current = getSmart(buffer)
        while (current == 32767) {
            current = getSmart(buffer)
            value += 32767
        }
        value += current
        return value
    }
}