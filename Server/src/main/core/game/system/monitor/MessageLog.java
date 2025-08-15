package core.game.system.monitor;

import core.cache.util.ByteBufferExtensions;

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
 * Logs messages with optional timestamping and capacity limit.
 * Supports writing logs to a file in a custom binary format.
 */
public class MessageLog {

    private static final int MAX_MESSAGE_SIZE = 16055;

    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<String> messages;

    private final int capacity;

    private final boolean uniqueLogging;

    /**
     * Creates a MessageLog with unlimited capacity and no unique logging.
     */
    public MessageLog() {
        this(-1, false);
    }

    /**
     * Creates a MessageLog with specified capacity and no unique logging.
     *
     * @param capacity maximum number of stored messages; -1 means unlimited
     */
    public MessageLog(int capacity) {
        this(capacity, false);
    }

    /**
     * Creates a MessageLog with specified capacity and unique logging option.
     *
     * @param capacity      maximum number of stored messages; -1 means unlimited
     * @param uniqueLogging if true, duplicate messages are ignored
     */
    public MessageLog(int capacity, boolean uniqueLogging) {
        this.capacity = capacity;
        this.messages = new ArrayList<>();
        this.uniqueLogging = uniqueLogging;
    }

    /**
     * Adds a message to the log, optionally prepending a timestamp.
     * If capacity is exceeded, oldest message is removed.
     * If uniqueLogging is enabled, duplicate messages are ignored.
     *
     * @param message   the message to log
     * @param timeStamp whether to prepend a timestamp
     */
    public void log(String message, boolean timeStamp) {
        if (uniqueLogging && messages.contains(message)) {
            return;
        }

        if (capacity > 0 && messages.size() == capacity) {
            messages.remove(0);
        }

        if (timeStamp) {
            String timestamp = dateFormat.format(new Date());
            message = timestamp + ": " + message;
        }

        messages.add(message);
    }

    /**
     * Writes all stored messages to the specified file.
     * The messages are written with a custom binary format:
     * - First, a short with the number of messages
     * - Then each message as a string followed by a newline byte
     * - Finally, a short with value -1 to mark the end
     * <p>
     * Appends to the file just before the last two bytes (if file exists).
     * <p>
     * Clears the message list after writing.
     *
     * @param fileName path to the file where logs will be saved
     */
    public void write(String fileName) {
        if (messages.isEmpty()) {
            return;
        }

        int size = messages.size();
        ByteBuffer buffer = ByteBuffer.allocate(size * MAX_MESSAGE_SIZE);
        buffer.putShort((short) size);

        for (String message : messages) {
            ByteBufferExtensions.putString(message, buffer);
            buffer.put((byte) '\n');
        }

        buffer.putShort((short) -1);
        buffer.flip();

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
             FileChannel channel = raf.getChannel()) {

            long pos = channel.size() - 2L;
            if (pos < 0) {
                pos = 0;
            }
            channel.write(buffer, pos);

        } catch (IOException e) {
            e.printStackTrace();
        }

        messages.clear();
    }

    /**
     * Gets the list of stored messages.
     *
     * @return list of messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Replaces the current message list with the provided list.
     *
     * @param messages new list of messages
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * Gets the maximum capacity of stored messages.
     *
     * @return capacity or -1 if unlimited
     */
    public int getCapacity() {
        return capacity;
    }
}