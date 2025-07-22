package core.net

import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

/**
 * Represents a server socket and its selector.
 *
 * @author Emperor
 */
class ServerSocketConnection {

    val selector: Selector
    val channel: ServerSocketChannel?
    val socket: SocketChannel?

    /**
     * Constructor for server socket channel.
     */
    constructor(selector: Selector, channel: ServerSocketChannel) {
        this.selector = selector
        this.channel = channel
        this.socket = null
    }

    /**
     * Constructor for client socket channel.
     */
    constructor(selector: Selector, socket: SocketChannel) {
        this.selector = selector
        this.socket = socket
        this.channel = null
    }

    /**
     * Returns true if this connection represents a client socket.
     */
    val isClient: Boolean
        get() = socket != null
}
