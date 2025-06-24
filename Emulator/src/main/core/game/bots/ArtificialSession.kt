package core.game.bots

import core.net.IoSession
import java.nio.ByteBuffer

/**
 * Represents an artificial session for bots or simulated clients.
 */
class ArtificialSession private constructor() {

    private val session: IoSession = IoSession(null, null)

    /**
     * Returns the underlying IoSession instance.
     */
    val ioSession: IoSession
        get() = session // Artificial IoSession for bot usage;

    /**
     * Returns a dummy remote address for the artificial session.
     */
    fun getRemoteAddress(): String = "127.0.0.1"

    /**
     * Writes to the artificial session. This is a no-op.
     */
    fun write(context: Any?, instant: Boolean) {
    }

    /**
     * Queues a buffer to the artificial session. This is a no-op.
     */
    fun queue(buffer: ByteBuffer) {
    }

    /**
     * Flushes the artificial session. This is a no-op.
     */
    fun write() {
    }

    /**
     * Disconnects the artificial session. This is a no-op.
     */
    fun disconnect() {
    }

    companion object {
        private val SINGLETON = ArtificialSession()

        /**
         * Returns the singleton instance of the artificial session.
         */
        fun getSingleton(): ArtificialSession = SINGLETON
    }
}