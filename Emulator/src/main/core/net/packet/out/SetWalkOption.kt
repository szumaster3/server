package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import core.net.packet.context.WalkOptionContext

/**
 * Handles the sending of the "Set walk-to option" packet.
 *
 * @author Emperor
 */
class SetWalkOption : OutgoingPacket<WalkOptionContext> {
    override fun send(context: WalkOptionContext) {
        val buffer = IoBuffer(SET_WALK_OPTION_OPCODE, PacketHeader.BYTE)
            .putString(context.option)
        context.player.session.write(buffer)
    }

    private companion object {
        private const val SET_WALK_OPTION_OPCODE = 10
    }
}