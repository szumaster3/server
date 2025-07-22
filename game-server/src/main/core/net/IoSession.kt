package core.net

import core.auth.AuthResponse
import core.cache.crypto.ISAACPair
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.ClientInfo
import core.game.world.repository.Repository
import core.net.producer.HSEventProducer
import core.net.producer.LoginEventProducer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Represents a network session between a client and the server.
 *
 * @author Emperor
 */
open class IoSession(
    private val key: SelectionKey?,
    private val service: ExecutorService?
) {

    /**
     * The handshake event producer.
     */
    private var producer: EventProducer = HSEventProducer()

    /**
     * The currently queued writing data.
     */
    private val writingQueue = ArrayList<ByteBuffer>(20)

    /**
     * The currently queued reading data.
     */
    private var readingQueue: ByteBuffer? = null

    /**
     * The writing lock.
     */
    private val writingLock: Lock = ReentrantLock()

    /**
     * The ISAAC cipher pair.
     */
    private var isaacPair: ISAACPair? = null

    /**
     * The name hash.
     */
    private var nameHash: Int = 0

    /**
     * The server key.
     */
    private var serverKey: Long = 0

    /**
     * The JS-5 encryption value.
     */
    private var js5Encryption: Int = 0

    /**
     * The object.
     */
    private var obj: Any? = null

    /**
     * If the session is active.
     */
    private var active: Boolean = true

    /**
     * The last ping time stamp.
     */
    private var lastPing: Long = 0

    /**
     * The address.
     */
    private val address: String = getRemoteAddress().replace("/", "").split(":")[0]

    /**
     * The client info.
     */
    private var clientInfo: ClientInfo? = null

    var associatedUsername: String? = null

    /**
     * Writes the given context to the session.
     *
     * @param context the object to be written to the session
     * @throws IllegalStateException if the context is null
     */
    fun write(context: Any) {
        write(context, false)
    }

    /**
     * Writes the given context to the session, with the option for immediate writing.
     *
     * @param context the object to be written to the session
     * @param instant if true, the writing happens immediately; otherwise, it is queued
     * @throws IllegalStateException if the context is null
     */
    open fun write(context: Any, instant: Boolean = false) {
        if (context !is AuthResponse && producer is LoginEventProducer) {
            return
        }
        if (instant) {
            producer.produceWriter(this, context).run()
        } else {
            service?.execute(producer.produceWriter(this, context))
        }
    }

    /**
     * Adds a ByteBuffer to the writing queue for sending to the client.
     *
     * @param buffer the ByteBuffer to be queued
     */
    open fun queue(buffer: ByteBuffer?) {
        try {
            if (writingLock.tryLock(1000L, TimeUnit.MILLISECONDS)) {
                writingQueue.add(buffer!!)
                writingLock.unlock()
                write()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            writingLock.unlock()
        }
    }

    /**
     * Writes all queued ByteBuffers to the client.
     * If no more buffers are available, the connection is set to non-writable.
     */
    open fun write() {
        if (!key!!.isValid) {
            disconnect()
            return
        }
        try {
            if (writingLock.tryLock(1000L, TimeUnit.MILLISECONDS)) {
                val channel = key.channel() as SocketChannel
                while (writingQueue.isNotEmpty()) {
                    val buffer = writingQueue[0]
                    channel.write(buffer)
                    if (buffer.hasRemaining()) {
                        key.interestOps(key.interestOps() or SelectionKey.OP_WRITE)
                        break
                    }
                    writingQueue.removeAt(0)
                }
            }
        } catch (e: IOException) {
            disconnect()
        } finally {
            writingLock.unlock()
        }
    }

    /**
     * Disconnects the session and closes the associated socket channel.
     */
    open fun disconnect() {
        try {
            if (!active) {
                return
            }
            active = false
            key!!.cancel()
            val channel = key.channel() as SocketChannel
            channel.socket().close()
            getPlayer()?.clear()
            obj = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Retrieves the address of the client.
     *
     * @return the address of the client
     */
    fun getAddress(): String {
        return address
    }

    /**
     * Retrieves the remote address of the client.
     *
     * @return the remote address of the client
     * @throws IllegalStateException if an error occurs while retrieving the address
     */
    open fun getRemoteAddress(): String {
        return try {
            (key!!.channel() as SocketChannel).remoteAddress.toString()
        } catch (e: Exception) {
            "0.0.0.0:0"
        }
    }

    /**
     * Retrieves the event producer used by this session.
     *
     * @return the event producer
     */
    fun getProducer(): EventProducer {
        return producer
    }

    /**
     * Sets the event producer for this session.
     *
     * @param producer the event producer to set
     */
    fun setProducer(producer: EventProducer) {
        this.producer = producer
    }

    /**
     * Retrieves the reading queue buffer for this session.
     *
     * @return the reading queue buffer
     */
    fun getReadingQueue(): ByteBuffer? {
        synchronized(this) {
            return readingQueue
        }
    }

    /**
     * Sets the reading queue buffer for this session.
     *
     * @param readingQueue the ByteBuffer to set as the reading queue
     */
    fun setReadingQueue(readingQueue: ByteBuffer?) {
        synchronized(this) {
            this.readingQueue = readingQueue
        }
    }

    /**
     * Retrieves the writing lock for this session.
     *
     * @return the writing lock
     */
    fun getWritingLock(): Lock {
        return writingLock
    }

    /**
     * Retrieves the selection key associated with this session.
     *
     * @return the selection key
     */
    fun getKey(): SelectionKey? {
        return key
    }

    /**
     * Checks whether the session is active.
     *
     * @return true if the session is active, false otherwise
     */
    fun isActive(): Boolean {
        return active
    }

    /**
     * Retrieves the JS5 encryption value for this session.
     *
     * @return the JS5 encryption value
     */
    fun getJs5Encryption(): Int {
        return js5Encryption
    }

    /**
     * Sets the JS5 encryption value for this session.
     *
     * @param js5Encryption the JS5 encryption value to set
     */
    fun setJs5Encryption(js5Encryption: Int) {
        this.js5Encryption = js5Encryption
    }

    /**
     * Retrieves the player associated with this session.
     *
     * @return the player, or null if no player is associated
     */
    fun getPlayer(): Player? {
        if (obj == null) {
            obj = Repository.getPlayerByName(associatedUsername)
        }
        return if (obj is Player) obj as Player else null
    }

    /**
     * Retrieves the object associated with this session.
     *
     * @return the object associated with this session
     */
    fun getObject(): Any? {
        return obj
    }

    /**
     * Sets the object associated with this session.
     *
     * @param obj the object to associate with this session
     */
    fun setObject(obj: Any?) {
        this.obj = obj
    }

    /**
     * Retrieves the last ping timestamp for this session.
     *
     * @return the last ping timestamp
     */
    fun getLastPing(): Long {
        return lastPing
    }

    /**
     * Sets the last ping timestamp for this session.
     *
     * @param lastPing the last ping timestamp to set
     */
    fun setLastPing(lastPing: Long) {
        this.lastPing = lastPing
    }

    /**
     * Retrieves the name hash for this session.
     *
     * @return the name hash
     */
    fun getNameHash(): Int {
        return nameHash
    }

    /**
     * Sets the name hash for this session.
     *
     * @param nameHash the name hash to set
     */
    fun setNameHash(nameHash: Int) {
        this.nameHash = nameHash
    }

    /**
     * Retrieves the server key for this session.
     *
     * @return the server key
     */
    fun getServerKey(): Long {
        return serverKey
    }

    /**
     * Sets the server key for this session.
     *
     * @param serverKey the server key to set
     */
    fun setServerKey(serverKey: Long) {
        this.serverKey = serverKey
    }

    /**
     * Gets isaac pair.
     *
     * @return the isaac pair
     */
    open fun getIsaacPair(): ISAACPair? {
        return isaacPair
    }

    /**
     * Sets isaac pair.
     *
     * @param isaacPair the isaac pair
     */
    fun setIsaacPair(isaacPair: ISAACPair) {
        this.isaacPair = isaacPair
    }

    /**
     * Gets client info.
     *
     * @return the client info
     */
    fun getClientInfo(): ClientInfo? {
        return clientInfo
    }

    /**
     * Sets client info.
     *
     * @param clientInfo the client info
     */
    fun setClientInfo(clientInfo: ClientInfo) {
        this.clientInfo = clientInfo
    }
}
