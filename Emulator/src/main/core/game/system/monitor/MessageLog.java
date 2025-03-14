package core.game.system.monitor;

import core.cache.misc.buffer.ByteBufferUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Message log.
 */
public class MessageLog {

    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<String> messages;

    private final int capacity;

    private boolean uniqueLogging;

    /**
     * Instantiates a new Message log.
     */
    public MessageLog() {
        this(-1, false);
    }

    /**
     * Instantiates a new Message log.
     *
     * @param capacity the capacity
     */
    public MessageLog(int capacity) {
        this(capacity, false);
    }

    /**
     * Instantiates a new Message log.
     *
     * @param capacity      the capacity
     * @param uniqueLogging the unique logging
     */
    public MessageLog(int capacity, boolean uniqueLogging) {
        this.capacity = capacity;
        this.messages = new ArrayList<>(20);
        this.uniqueLogging = uniqueLogging;
    }

    /**
     * Log.
     *
     * @param message   the message
     * @param timeStamp the time stamp
     */
    public void log(String message, boolean timeStamp) {
        if (messages.size() == capacity) {
            messages.remove(0);
        }
        if (uniqueLogging && messages.contains(message)) {
            return;
        }
        if (timeStamp) {
            StringBuilder sb = new StringBuilder(dateFormat.format(new Date()));
            message = sb.append(": ").append(message).toString();
        }
        messages.add(message);
    }

    /**
     * Write.
     *
     * @param fileName the file name
     */
    public void write(String fileName) {
        if (messages.isEmpty()) {
            return;
        }
        int size = messages.size();
        ByteBuffer buffer = ByteBuffer.allocate(size * 16055);
        buffer.putShort((short) size);
        for (String message : messages) {
            ByteBufferUtils.putString(message, buffer);
            buffer.put((byte) '\n');
        }
        buffer.putShort((short) -1);
        buffer.flip();
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            FileChannel channel = raf.getChannel();
            long pos = channel.size() - 2l;
            if (pos < 1) {
                pos = 0;
            }
            channel.write(buffer, pos);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messages.clear();
    }

    /**
     * Gets messages.
     *
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Sets messages.
     *
     * @param messages the messages
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

}