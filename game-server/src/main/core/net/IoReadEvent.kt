package core.net

import java.nio.ByteBuffer

/**
 * Handles a reading event.
 *
 * @author Emperor
 */
abstract class IoReadEvent(
    private val session: IoSession,
    buffer: ByteBuffer
) : Runnable {

    private var buffer: ByteBuffer = buffer

    /**
     * If the queued reading buffer was used (debugging purposes).
     */
    protected var usedQueuedBuffer: Boolean = false
        private set

    override fun run() {
        try {
            val queuedBuffer = session.getReadingQueue()
            if (queuedBuffer != null) {
                queuedBuffer.put(buffer)
                queuedBuffer.flip()
                buffer = queuedBuffer
                session.setReadingQueue(null)
                usedQueuedBuffer = true
            }
            read(session, buffer)
        } catch (t: Throwable) {
            t.printStackTrace()
            session.disconnect()
        }
    }

    /**
     * Queues the buffer until more data has been received.
     *
     * @param data The data that has been read already.
     */
    fun queueBuffer(vararg data: Int) {
        val queue = ByteBuffer.allocate(data.size + buffer.remaining() + 100_000)
        for (value in data) {
            queue.put(value.toByte())
        }
        queue.put(buffer)
        session.setReadingQueue(queue)
    }

    /**
     * Reads the data from the buffer.
     *
     * @param session The session.
     * @param buffer  The buffer to read from.
     */
    abstract fun read(session: IoSession, buffer: ByteBuffer)
}
