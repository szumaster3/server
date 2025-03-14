package core.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.ExecutorService;

/**
 * The type Io event handler.
 */
public class IoEventHandler {

    /**
     * The Service.
     */
    protected final ExecutorService service;

    /**
     * Instantiates a new Io event handler.
     *
     * @param service the service
     */
    public IoEventHandler(ExecutorService service) {
        this.service = service;
    }

    /**
     * Connect.
     *
     * @param key the key
     * @throws IOException the io exception
     */
    public void connect(SelectionKey key) throws IOException {

    }

    /**
     * Accept.
     *
     * @param key      the key
     * @param selector the selector
     * @throws IOException the io exception
     */
    public void accept(SelectionKey key, Selector selector) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        sc.configureBlocking(false);
        sc.socket().setTcpNoDelay(true);
        sc.register(selector, SelectionKey.OP_READ);
    }

    /**
     * Read.
     *
     * @param key the key
     * @throws IOException the io exception
     */
    public void read(SelectionKey key) throws IOException {
        ReadableByteChannel channel = (ReadableByteChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(100_000);
        IoSession session = (IoSession) key.attachment();
        try {
            if (channel.read(buffer) == -1) {
                if (session != null) {
                    session.disconnect();
                }
                key.cancel();
                return;
            }
        } catch (IOException e) {
            if (e.getMessage().contains("reset by peer") && session != null) {
                session.disconnect();
            } else {
                key.cancel();
                return;
            }
        }
        buffer.flip();
        if (session == null) {
            key.attach(session = new IoSession(key, service));
        }
        service.execute(session.getProducer().produceReader(session, buffer));
    }

    /**
     * Write.
     *
     * @param key the key
     */
    public void write(SelectionKey key) {
        IoSession session = (IoSession) key.attachment();
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        session.write();
    }

    /**
     * Disconnect.
     *
     * @param key the key
     * @param t   the t
     */
    public void disconnect(SelectionKey key, Throwable t) {
        try {
            IoSession session = (IoSession) key.attachment();
            String cause = "" + t;
            if (t != null && !(t instanceof ClosedChannelException || cause.contains("De externe host") || cause.contains("De software op uw") || cause.contains("An established connection was aborted") || cause.contains("An existing connection") || cause.contains("AsynchronousClose"))) {
                t.printStackTrace();
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}