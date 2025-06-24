package core.net.packet.out

import core.game.bots.AIPlayer
import core.game.node.entity.player.Player
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import core.tools.StringUtils.encryptPlayerChat
import core.tools.StringUtils.stringToLong
import java.util.*

/**
 * Handles communication message packet sending.
 *
 * @author Emperor
 */
class CommunicationMessage : OutgoingPacket<OutgoingContext.MessageContext> {
    override fun send(context: OutgoingContext.MessageContext) {
        val buffer = IoBuffer(context.opcode, PacketHeader.BYTE)
        val message = context.message
        val player = context.player
        val other = context.other
        when (context.opcode) {
            OutgoingContext.MessageContext.SEND_MESSAGE -> {
                val bytes = ByteArray(256)
                val length = encryptPlayerChat(bytes, 0, 0, message.length, message.toByteArray())
                buffer.putLong(stringToLong(other))
                buffer.put(message.length.toByte().toInt())
                buffer.putBytes(bytes, 0, length)
            }

            OutgoingContext.MessageContext.RECEIVE_MESSAGE -> {
                val bytes = ByteArray(256)
                bytes[0] = message.length.toByte()
                val length = 1 + encryptPlayerChat(bytes, 0, 1, message.length, message.toByteArray())
                player.setAttribute("replyTo", other)
                buffer.putLong(stringToLong(other)).putShort(Random().nextInt(0xFFFF)).putTri(getMessageIndex(player))
                    .put(context.chatIcon.toByte().toInt()).putBytes(bytes, 0, length)
            }

            OutgoingContext.MessageContext.CLAN_MESSAGE -> {
                val clan = player.communication.clan ?: return
                val bytes = ByteArray(256)
                bytes[0] = context.message.length.toByte()
                val length = 1 + encryptPlayerChat(bytes, 0, 1, message.length, message.toByteArray())
                buffer.putLong(stringToLong(other))
                buffer.put(0.toByte().toInt()) // it's just read does nothing
                buffer.putLong(stringToLong(clan.name))
                buffer.putShort(Random().nextInt(0xFFFF))
                buffer.putTri(getMessageIndex(player))
                buffer.put(context.chatIcon.toByte().toInt()) // rights
                buffer.putBytes(bytes, 0, length)
            }
        }
        if (player.isArtificial) {
            (player as AIPlayer).handleIncomingChat(context)
        }
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        player.session.write(buffer)
    }

    companion object {
        private fun getMessageIndex(p: Player): Int {
            val count = p.getAttribute("pm_index", 0) + 1
            p.setAttribute("pm_index", count)
            return count
        }
    }
}