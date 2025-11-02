package core.net.packet;

/**
 * Represents an outgoing packet.
 *
 * @param <T> The context type.
 * @author Emperor
 */
public interface OutgoingPacket<Context> {

    /**
     * Sends the packet.
     *
     * @param context The context.
     */
    public void send(Context context);

}