package core.net.packet.out

import core.game.container.ContainerEvent
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader

/**
 * Represents the outgoing container packet.
 *
 * @author Emperor
 */
class ContainerPacket : OutgoingPacket<OutgoingContext.Container> {
    override fun send(context: OutgoingContext.Container) {
        var buffer: IoBuffer? = null
        if (context.clear) {
            buffer = IoBuffer(144)
            buffer.putIntB(context.interfaceId shl 16 or context.childId)
        } else {
            val slotBased = context.slots != null
            buffer = IoBuffer(if (slotBased) 22 else 105, PacketHeader.SHORT)
            buffer.putShort(context.interfaceId)
            buffer.putShort(context.childId)
            buffer.putShort(context.containerId)
            if (slotBased) {
                for (slot in context.slots!!) {
                    buffer.putSmart(slot)
                    val item = context.items!![slot]
                    if (item != null && item != ContainerEvent.NULL_ITEM) {
                        buffer.putShort(item.id + 1)
                        val amount = item.amount
                        if (amount < 0 || amount > 254) {
                            buffer.put(255).putInt(amount)
                        } else {
                            buffer.put(amount)
                        }
                    } else {
                        buffer.putShort(0)
                    }
                }
            } else {
                if (context.ids != null) {
                    buffer.p2(context.length)
                    for (i in 0 until context.length) {
                        buffer.putS(1)
                        buffer.p2(context.ids!![i] + 1)
                    }
                } else {
                    buffer.putShort(context.items!!.size)
                    for (item in context.items!!) if (item != null) {
                        val amount = item.amount
                        if (amount < 0 || amount > 254) {
                            buffer.putS(255).putInt(amount)
                        } else {
                            buffer.putS(amount)
                        }
                        buffer.putShort(item.id + 1)
                    } else {
                        buffer.putS(0).putShort(0)
                    }
                }
            }
        }
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.session.write(buffer)
    }
}