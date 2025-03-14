package core.net;

import java.nio.ByteBuffer;

/**
 * The interface Event producer.
 */
public interface EventProducer {

    /**
     * Produce reader io read event.
     *
     * @param session the session
     * @param buffer  the buffer
     * @return the io read event
     */
    IoReadEvent produceReader(IoSession session, ByteBuffer buffer);

    /**
     * Produce writer io write event.
     *
     * @param session the session
     * @param context the context
     * @return the io write event
     */
    IoWriteEvent produceWriter(IoSession session, Object context);

}