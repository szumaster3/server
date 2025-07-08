package core.net.packet

/**
 * Represents an outgoing packet.
 * @author Emperor
 * @param Context The context type.
 */
interface OutgoingPacket<Context> {
    /**
     * Send.
     *
     * @param context the context
     */
    fun send(context: Context)
}