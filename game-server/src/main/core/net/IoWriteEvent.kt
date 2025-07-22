package core.net

import java.nio.channels.CancelledKeyException

/**
 * Handles a writing event.
 *
 * @author Emperor
 */
abstract class IoWriteEvent(
    private val session: IoSession,
    private val context: Any
) : Runnable {

    override fun run() {
        try {
            write(session, context)
        } catch (t: Throwable) {
            if (t !is CancelledKeyException) {
                t.printStackTrace()
            }
            session.disconnect()
        }
    }

    /**
     * Write.
     *
     * @param session the session
     * @param context the context
     */
    abstract fun write(session: IoSession, context: Any)
}
