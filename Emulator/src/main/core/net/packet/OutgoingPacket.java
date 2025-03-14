package core.net.packet;

/**
 * The interface Outgoing packet.
 *
 * @param <Context> the type parameter
 */
public interface OutgoingPacket<Context> {

    /**
     * Send.
     *
     * @param context the context
     */
    public void send(Context context);

}