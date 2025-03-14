package core.net;

import java.nio.channels.CancelledKeyException;

/**
 * The type Io write event.
 */
public abstract class IoWriteEvent implements Runnable {

    private final IoSession session;

    private final Object context;

    /**
     * Instantiates a new Io write event.
     *
     * @param session the session
     * @param context the context
     */
    public IoWriteEvent(IoSession session, Object context) {
        this.session = session;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            write(session, context);
        } catch (Throwable t) {
            if (!(t instanceof CancelledKeyException)) {
                t.printStackTrace();
            }
            session.disconnect();
        }
    }

    /**
     * Write.
     *
     * @param session the session
     * @param context the context
     */
    public abstract void write(IoSession session, Object context);

}