package core.net;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * The type Server socket connection.
 */
public final class ServerSocketConnection {

    private final Selector selector;

    private final ServerSocketChannel channel;

    private final SocketChannel socket;

    /**
     * Instantiates a new Server socket connection.
     *
     * @param selector the selector
     * @param channel  the channel
     */
    public ServerSocketConnection(Selector selector, ServerSocketChannel channel) {
        this.selector = selector;
        this.channel = channel;
        this.socket = null;
    }

    /**
     * Instantiates a new Server socket connection.
     *
     * @param selector the selector
     * @param channel  the channel
     */
    public ServerSocketConnection(Selector selector, SocketChannel channel) {
        this.selector = selector;
        this.socket = channel;
        this.channel = null;
    }

    /**
     * Is client boolean.
     *
     * @return the boolean
     */
    public boolean isClient() {
        return socket != null;
    }

    /**
     * Gets selector.
     *
     * @return the selector
     */
    public Selector getSelector() {
        return selector;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public ServerSocketChannel getChannel() {
        return channel;
    }

    /**
     * Gets socket.
     *
     * @return the socket
     */
    public SocketChannel getSocket() {
        return socket;
    }

}