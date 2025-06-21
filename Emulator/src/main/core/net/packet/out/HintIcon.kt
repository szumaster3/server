package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the Hint icon outgoing packet.
 *
 * @author Emperor
 */
class HintIcon : OutgoingPacket<OutgoingContext.HintIcon> {
    override fun send(context: OutgoingContext.HintIcon) {
        val buffer = IoBuffer(217)
        buffer.put(context.slot shl 6 or context.targetType).put(context.arrowId)
        if (context.arrowId > 0) {
            if (context.targetType == 1 || context.targetType == 10) {
                buffer.putShort(context.index).putShort(0) // Skip 3 bytes
                    .put(0)
            } else if (context.location != null) {
                buffer.putShort(context.location.x).putShort(context.location.y).put(context.height)
            } else {
                buffer.putShort(0).putShort(0).put(0) // Skip all bytes.
            }
            buffer.putShort(context.modelId)
        }
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.session.write(buffer)
    }
}