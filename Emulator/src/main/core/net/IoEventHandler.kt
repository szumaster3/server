package core.net

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.*
import java.util.concurrent.ExecutorService

/**
 * Handles I/O events for non-blocking socket channels.
 */
open class IoEventHandler(protected val service: ExecutorService) {

    @Throws(IOException::class)
    open fun connect(key: SelectionKey) {
        // no-op by default
    }

    @Throws(IOException::class)
    open fun accept(key: SelectionKey, selector: Selector) {
        val sc = (key.channel() as ServerSocketChannel).accept()
        sc.configureBlocking(false)
        sc.socket().tcpNoDelay = true
        sc.register(selector, SelectionKey.OP_READ)
    }

    @Throws(IOException::class)
    open fun read(key: SelectionKey) {
        val channel = key.channel() as ReadableByteChannel
        val buffer = ByteBuffer.allocate(100_000)
        var session = key.attachment() as? IoSession
        try {
            if (channel.read(buffer) == -1) {
                session?.disconnect()
                key.cancel()
                return
            }
        } catch (e: IOException) {
            if (e.message?.contains("reset by peer") == true) {
                session?.disconnect()
            } else {
                key.cancel()
            }
            return
        }
        buffer.flip()
        if (session == null) {
            session = IoSession(key, service)
            key.attach(session)
        }
        service.execute(session.getProducer().produceReader(session, buffer))
    }

    open fun write(key: SelectionKey) {
        val session = key.attachment() as? IoSession ?: return
        key.interestOps(key.interestOps() and SelectionKey.OP_WRITE.inv())
        session.write()
    }

    open fun disconnect(key: SelectionKey, t: Throwable?) {
        try {
            val session = key.attachment() as? IoSession
            val cause = t?.toString() ?: ""
            if (t != null &&
                t !is ClosedChannelException &&
                !cause.contains("De externe host") &&
                !cause.contains("De software op uw") &&
                !cause.contains("An established connection was aborted") &&
                !cause.contains("An existing connection") &&
                !cause.contains("AsynchronousClose")
            ) {
                t.printStackTrace()
            }
            session?.disconnect()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}