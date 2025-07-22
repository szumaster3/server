package core.net.amsc

import core.api.log
import core.net.IoEventHandler
import core.net.IoSession
import core.tools.Log
import java.io.IOException
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.concurrent.Executors

/**
 * Event handler for Management Server communication.
 */
class MSEventHandler : IoEventHandler(Executors.newSingleThreadExecutor()) {

    @Throws(IOException::class)
    override fun connect(key: SelectionKey) {
        val ch = key.channel() as SocketChannel
        try {
            if (ch.finishConnect()) {
                key.interestOps(key.interestOps() xor SelectionKey.OP_CONNECT)
                key.interestOps(key.interestOps() or SelectionKey.OP_READ)
                val session = IoSession(key, service)
                key.attach(session)
                WorldCommunicator.register(session)
                return
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        log(this::class.java, Log.ERR, "Failed connecting to Management Server!")
        WorldCommunicator.terminate()
    }

    @Throws(IOException::class)
    override fun accept(key: SelectionKey, selector: Selector) {
        super.write(key)
    }

    @Throws(IOException::class)
    override fun read(key: SelectionKey) {
        super.read(key)
    }

    override fun write(key: SelectionKey) {
        super.write(key)
    }

    override fun disconnect(key: SelectionKey, t: Throwable?) {
        super.disconnect(key, t)
        WorldCommunicator.terminate()
    }
}
