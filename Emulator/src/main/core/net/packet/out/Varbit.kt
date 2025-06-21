package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Sends a varbit update to the client.
 */
class Varbit : OutgoingPacket<OutgoingContext.Varbit> {
    override fun send(varbitContext: OutgoingContext.Varbit) {
        val buffer: IoBuffer
        if (varbitContext.value > 255) {
            buffer = IoBuffer(84)
            buffer.putLEInt((128 or varbitContext.value) and 255)
        } else {
            buffer = IoBuffer(37)
            buffer.put(128.toByte().toInt() or varbitContext.value)
        }
        buffer.putLEShort(varbitContext.varbitId)
        varbitContext.player.session.write(buffer)
    }
}
