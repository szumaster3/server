package core.net

import java.nio.ByteBuffer

/**
 * Used for producing I/O events.
 *
 * @author Emperor
 */
interface EventProducer {

    /**
     * Produces a new read event.
     *
     * @param session The session.
     * @param buffer  The buffer to read.
     * @return The read event handler.
     */
    fun produceReader(session: IoSession, buffer: ByteBuffer): IoReadEvent

    /**
     * Produces a new writing event.
     *
     * @param session The session.
     * @param context The context.
     * @return The write event handler.
     */
    fun produceWriter(session: IoSession, context: Any): IoWriteEvent
}
