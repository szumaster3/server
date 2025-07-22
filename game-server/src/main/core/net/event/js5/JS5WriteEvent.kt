package core.net.event.js5

import core.cache.Cache.provider
import core.cache.Cache.versionTable
import core.net.*
import core.tools.Log
import core.tools.SystemLogger
import java.nio.ByteBuffer
import kotlin.math.min

class JS5WriteEvent(
    session: IoSession,
    context: Any,
) : IoWriteEvent(session, context) {

    override fun write(session: IoSession, context: Any) {
        val request = context as Triple<*, *, *>
        val index = request.first as Int
        val archive = request.second as Int
        val priority = request.third as Boolean

        val data = data(index, archive) ?: run {
            SystemLogger.processLogEntry(
                this::class.java,
                Log.WARN,
                "Unable to fulfill request $index $archive $priority."
            )
            return
        }

        val buffer = if (index == 255 && archive == 255) {
            serve255(data)
        } else {
            serve(index, archive, data, priority)
        }
        session.queue(buffer)
    }

    /**
     * @return data for an [index]'s [archive] file or [versionTable] when index and archive are both 255
     */
    private fun data(index: Int, archive: Int): ByteArray? =
        if (index == 255 && archive == 255) versionTable else provider.data(index, archive)

    /**
     * Prepares a buffer to send the version table data
     */
    private fun serve255(data: ByteArray): ByteBuffer {
        val buffer = ByteBuffer.allocate(8 + data.size)
        buffer.p1(255)
        buffer.p2(255)
        buffer.p1(0)
        buffer.p4(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }

    private fun serve(
        index: Int,
        archive: Int,
        data: ByteArray,
        prefetch: Boolean,
    ): ByteBuffer {
        val compression = data[0].toInt()
        val size = getInt(data[1], data[2], data[3], data[4]) + if (compression != 0) 8 else 4
        val buffer = ByteBuffer.allocate(size + 5 + size / 512 + 10)

        buffer.p1(index)
        buffer.p2(archive)
        buffer.p1(if (prefetch) compression or 0x80 else compression)

        var length = min(size, SPLIT - HEADER)
        buffer.put(data, OFFSET, length)
        var written = length

        while (written < size) {
            buffer.p1(SEPARATOR)
            length = if (size - written < SPLIT) size - written else SPLIT - 1
            buffer.put(data, written + OFFSET, length)
            written += length
        }
        buffer.flip()
        return buffer
    }

    companion object {
        private fun getInt(b1: Byte, b2: Byte, b3: Byte, b4: Byte): Int =
            (b1.toInt() shl 24) or
                    ((b2.toInt() and 0xff) shl 16) or
                    ((b3.toInt() and 0xff) shl 8) or
                    (b4.toInt() and 0xff)

        private const val SEPARATOR = 255
        private const val HEADER = 4
        private const val SPLIT = 512
        private const val OFFSET = 1
    }
}
