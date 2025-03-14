package core.net;

import core.net.amsc.MSEventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Nio reactor.
 */
public final class NioReactor implements Runnable {

    private final ExecutorService service;

    private ServerSocketConnection channel;

    private IoEventHandler eventHandler;

    private boolean running;

    private NioReactor(IoEventHandler eventHandler) {
        this.service = Executors.newSingleThreadScheduledExecutor();
        this.eventHandler = eventHandler;
    }

    /**
     * Configure nio reactor.
     *
     * @param port the port
     * @return the nio reactor
     * @throws IOException the io exception
     */
    public static NioReactor configure(int port) throws IOException {
        return configure(port, 1);
    }

    /**
     * Configure nio reactor.
     *
     * @param port     the port
     * @param poolSize the pool size
     * @return the nio reactor
     * @throws IOException the io exception
     */
    public static NioReactor configure(int port, int poolSize) throws IOException {
        NioReactor reactor = new NioReactor(new IoEventHandler(Executors.newFixedThreadPool(poolSize)));
        ServerSocketChannel channel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        reactor.channel = new ServerSocketConnection(selector, channel);
        return reactor;
    }

    /**
     * Connect nio reactor.
     *
     * @param address the address
     * @param port    the port
     * @return the nio reactor
     * @throws IOException the io exception
     */
    public static NioReactor connect(String address, int port) throws IOException {
        NioReactor reactor = new NioReactor(new MSEventHandler());
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().setKeepAlive(true);
        channel.socket().setTcpNoDelay(true);
        channel.connect(new InetSocketAddress(address, port));
        channel.register(selector, SelectionKey.OP_CONNECT);
        reactor.channel = new ServerSocketConnection(selector, channel);
        return reactor;
    }

    /**
     * Start.
     */
    public void start() {
        running = true;
        service.execute(this);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("NioReactor");
        while (running) {
            try {
                channel.getSelector().select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Iterator<SelectionKey> iterator = channel.getSelector().selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (!key.isValid() || !key.channel().isOpen()) {
                        key.cancel();
                        continue;
                    }
                    if (key.isConnectable()) {
                        eventHandler.connect(key);
                    }
                    if (key.isAcceptable()) {
                        eventHandler.accept(key, channel.getSelector());
                    }
                    if (key.isReadable()) {
                        eventHandler.read(key);
                    } else if (key.isWritable()) {
                        eventHandler.write(key);
                    }
                } catch (Throwable t) {
                    eventHandler.disconnect(key, t);
                }
            }
        }
    }

    /**
     * Terminate.
     */
    public void terminate() {
        running = false;
    }

}