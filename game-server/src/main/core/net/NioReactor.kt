package core.net

import core.net.amsc.MSEventHandler
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.channels.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A simple NIO-based reactor that handles non-blocking I/O operations
 * for both server and client socket channels.
 *
 * Used to accept, connect, read, and write events via a selector loop.
 */
class NioReactor private constructor(private val eventHandler: IoEventHandler) : Runnable {

    private val service: ExecutorService = Executors.newSingleThreadScheduledExecutor()
    private lateinit var channel: ServerSocketConnection
    @Volatile
    private var running = false

    companion object {
        @Throws(IOException::class)
        fun configure(port: Int): NioReactor = configure(port, 1)

        @Throws(IOException::class)
        fun configure(port: Int, poolSize: Int): NioReactor {
            val reactor = NioReactor(IoEventHandler(Executors.newFixedThreadPool(poolSize)))
            val channel = ServerSocketChannel.open()
            val selector = Selector.open()
            channel.bind(InetSocketAddress(port))
            channel.configureBlocking(false)
            channel.register(selector, SelectionKey.OP_ACCEPT)
            reactor.channel = ServerSocketConnection(selector, channel)
            return reactor
        }

        @Throws(IOException::class)
        fun connect(address: String, port: Int): NioReactor {
            val reactor = NioReactor(MSEventHandler())
            val selector = Selector.open()
            val channel = SocketChannel.open()
            channel.configureBlocking(false)
            channel.socket().keepAlive = true
            channel.socket().tcpNoDelay = true
            channel.connect(InetSocketAddress(address, port))
            channel.register(selector, SelectionKey.OP_CONNECT)
            reactor.channel = ServerSocketConnection(selector, channel)
            return reactor
        }
    }

    fun start() {
        running = true
        service.execute(this)
    }

    override fun run() {
        Thread.currentThread().name = "NioReactor"
        while (running) {
            try {
                channel.selector.select()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val iterator = channel.selector.selectedKeys().iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                iterator.remove()
                try {
                    if (!key.isValid || !key.channel().isOpen) {
                        key.cancel()
                        continue
                    }
                    when {
                        key.isConnectable -> eventHandler.connect(key)
                        key.isAcceptable -> eventHandler.accept(key, channel.selector)
                        key.isReadable -> eventHandler.read(key)
                        key.isWritable -> eventHandler.write(key)
                    }
                } catch (t: Throwable) {
                    eventHandler.disconnect(key, t)
                }
            }
        }
    }

    fun terminate() {
        running = false
    }
}
