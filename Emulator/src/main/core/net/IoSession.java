package core.net;

import core.auth.AuthResponse;
import core.cache.crypto.ISAACPair;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.ClientInfo;
import core.game.world.repository.Repository;
import core.net.producer.HSEventProducer;
import core.net.producer.LoginEventProducer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a network session between a client and the server.
 * This session handles reading, writing, and managing client connections.
 */
public class IoSession {

    private static final EventProducer HANDSHAKE_PRODUCER = new HSEventProducer();

    private final SelectionKey key;
    private final ExecutorService service;
    private final String address;
    /**
     * The Isaac input opcode.
     */
    public int isaacInputOpcode = 0;
    /**
     * The Associated username.
     */
    public String associatedUsername;
    private EventProducer producer = HANDSHAKE_PRODUCER;
    private List<ByteBuffer> writingQueue = new ArrayList<>(20);
    private ByteBuffer readingQueue;
    private Lock writingLock = new ReentrantLock();
    private ISAACPair isaacPair;
    private int nameHash;
    private long serverKey;
    private int js5Encryption;
    private Object object;
    private boolean active = true;
    private long lastPing;
    private ClientInfo clientInfo;

    /**
     * Constructs a new IoSession for the given SelectionKey and ExecutorService.
     *
     * @param key     the SelectionKey associated with the client connection
     * @param service the ExecutorService to handle tasks for this session
     */
    public IoSession(SelectionKey key, ExecutorService service) {
        this.key = key;
        this.service = service;
        this.address = getRemoteAddress().replaceAll("/", "").split(":")[0];
    }

    /**
     * Writes the given context to the session.
     *
     * @param context the object to be written to the session
     * @throws IllegalStateException if the context is null
     */
    public void write(Object context) {
        write(context, false);
    }

    /**
     * Writes the given context to the session, with the option for immediate writing.
     *
     * @param context the object to be written to the session
     * @param instant if true, the writing happens immediately; otherwise, it is queued
     * @throws IllegalStateException if the context is null
     */
    public void write(Object context, boolean instant) {
        if (context == null) {
            throw new IllegalStateException("Invalid writing context!");
        }
        if (!(context instanceof AuthResponse) && producer instanceof LoginEventProducer) {
            return;
        }
        if (instant) {
            producer.produceWriter(this, context).run();
            return;
        }
        service.execute(producer.produceWriter(this, context));
    }

    /**
     * Adds a ByteBuffer to the writing queue for sending to the client.
     *
     * @param buffer the ByteBuffer to be queued
     */
    public void queue(ByteBuffer buffer) {
        try {
            writingLock.tryLock(1000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            writingLock.unlock();
        }
        writingQueue.add(buffer);
        writingLock.unlock();
        write();
    }

    /**
     * Writes all queued ByteBuffers to the client.
     * If no more buffers are available, the connection is set to non-writable.
     */
    public void write() {
        if (!key.isValid()) {
            disconnect();
            return;
        }
        try {
            writingLock.tryLock(1000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            writingLock.unlock();
            return;
        }
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            while (!writingQueue.isEmpty()) {
                ByteBuffer buffer = writingQueue.get(0);
                channel.write(buffer);
                if (buffer.hasRemaining()) {
                    key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                    break;
                }
                writingQueue.remove(0);
            }
        } catch (IOException e) {
            disconnect();
        }
        writingLock.unlock();
    }

    /**
     * Disconnects the session and closes the associated socket channel.
     */
    public void disconnect() {
        try {
            if (!active) {
                return;
            }
            active = false;
            key.cancel();
            SocketChannel channel = (SocketChannel) key.channel();
            channel.socket().close();
            if (getPlayer() != null) {
                try {
                    getPlayer().clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            object = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the address of the client.
     *
     * @return the address of the client
     */
    public String getAddress() {
        return address;
    }

    /**
     * Retrieves the remote address of the client.
     *
     * @return the remote address of the client
     * @throws IllegalStateException if an error occurs while retrieving the address
     */
    public String getRemoteAddress() {
        try {
            return ((SocketChannel) key.channel()).getRemoteAddress().toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Retrieves the event producer used by this session.
     *
     * @return the event producer
     */
    public EventProducer getProducer() {
        return producer;
    }

    /**
     * Sets the event producer for this session.
     *
     * @param producer the event producer to set
     */
    public void setProducer(EventProducer producer) {
        this.producer = producer;
    }

    /**
     * Retrieves the reading queue buffer for this session.
     *
     * @return the reading queue buffer
     */
    public ByteBuffer getReadingQueue() {
        synchronized (this) {
            return readingQueue;
        }
    }

    /**
     * Sets the reading queue buffer for this session.
     *
     * @param readingQueue the ByteBuffer to set as the reading queue
     */
    public void setReadingQueue(ByteBuffer readingQueue) {
        synchronized (this) {
            this.readingQueue = readingQueue;
        }
    }

    /**
     * Retrieves the writing lock for this session.
     *
     * @return the writing lock
     */
    public Lock getWritingLock() {
        return writingLock;
    }

    /**
     * Retrieves the selection key associated with this session.
     *
     * @return the selection key
     */
    public SelectionKey getKey() {
        return key;
    }

    /**
     * Checks whether the session is active.
     *
     * @return true if the session is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Retrieves the JS5 encryption value for this session.
     *
     * @return the JS5 encryption value
     */
    public int getJs5Encryption() {
        return js5Encryption;
    }

    /**
     * Sets the JS5 encryption value for this session.
     *
     * @param js5Encryption the JS5 encryption value to set
     */
    public void setJs5Encryption(int js5Encryption) {
        this.js5Encryption = js5Encryption;
    }

    /**
     * Retrieves the player associated with this session.
     *
     * @return the player, or null if no player is associated
     */
    public Player getPlayer() {
        if (object == null) {
            object = Repository.getPlayerByName(associatedUsername);
        }
        return object instanceof Player ? ((Player) object) : null;
    }

    /**
     * Retrieves the object associated with this session.
     *
     * @return the object associated with this session
     */
    public Object getObject() {
        return object;
    }

    /**
     * Sets the object associated with this session.
     *
     * @param object the object to associate with this session
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Retrieves the last ping timestamp for this session.
     *
     * @return the last ping timestamp
     */
    public long getLastPing() {
        return lastPing;
    }

    /**
     * Sets the last ping timestamp for this session.
     *
     * @param lastPing the last ping timestamp to set
     */
    public void setLastPing(long lastPing) {
        this.lastPing = lastPing;
    }

    /**
     * Retrieves the name hash for this session.
     *
     * @return the name hash
     */
    public int getNameHash() {
        return nameHash;
    }

    /**
     * Sets the name hash for this session.
     *
     * @param nameHash the name hash to set
     */
    public void setNameHash(int nameHash) {
        this.nameHash = nameHash;
    }

    /**
     * Retrieves the server key for this session.
     *
     * @return the server key
     */
    public long getServerKey() {
        return serverKey;
    }

    /**
     * Sets the server key for this session.
     *
     * @param serverKey the server key to set
     */
    public void setServerKey(long serverKey) {
        this.serverKey = serverKey;
    }

    /**
     * Gets isaac pair.
     *
     * @return the isaac pair
     */
    public ISAACPair getIsaacPair() {
        return isaacPair;
    }

    /**
     * Sets isaac pair.
     *
     * @param isaacPair the isaac pair
     */
    public void setIsaacPair(ISAACPair isaacPair) {
        this.isaacPair = isaacPair;
    }

    /**
     * Gets client info.
     *
     * @return the client info
     */
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    /**
     * Sets client info.
     *
     * @param clientInfo the client info
     */
    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

}