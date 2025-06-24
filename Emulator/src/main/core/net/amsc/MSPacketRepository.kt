package core.net.amsc

import core.game.node.entity.player.Player
import core.game.system.communication.ClanRank
import core.net.packet.IoBuffer
import core.net.packet.PacketHeader

/**
 * The Management server packet repository.
 *
 * @author Emperor
 */
object MSPacketRepository {

    @JvmStatic
    fun sendContactUpdate(username: String, contact: String, remove: Boolean, block: Boolean, rank: ClanRank?) {
        val buffer = IoBuffer(if (block) 5 else 4, PacketHeader.BYTE)
        buffer.putString(username)
        buffer.putString(contact)
        if (rank != null) {
            buffer.put(2)
            buffer.put(rank.ordinal)
        } else {
            buffer.put(if (remove) 1 else 0)
        }
        WorldCommunicator.getSession()?.write(buffer)
    }

    fun sendClanRename(player: Player, clanName: String) {
        val buffer = IoBuffer(7, PacketHeader.BYTE)
        buffer.putString(player.name)
        buffer.putString(clanName)
        WorldCommunicator.getSession()?.write(buffer)
    }

    fun setClanSetting(player: Player, type: Int, rank: ClanRank?) {
        if (!WorldCommunicator.isEnabled()) {
            return
        }
        val buffer = IoBuffer(8, PacketHeader.BYTE)
        buffer.putString(player.name)
        buffer.put(type)
        rank?.let { buffer.put(it.ordinal) }
        WorldCommunicator.getSession()?.write(buffer)
    }

    fun sendClanKick(username: String, name: String) {
        val buffer = IoBuffer(9, PacketHeader.BYTE)
        buffer.putString(username)
        buffer.putString(name)
        WorldCommunicator.getSession()?.write(buffer)
    }

    fun sendChatSetting(player: Player, publicSetting: Int, privateSetting: Int, tradeSetting: Int) {
        val buffer = IoBuffer(13, PacketHeader.BYTE)
        buffer.putString(player.name)
        buffer.put(publicSetting)
        buffer.put(privateSetting)
        buffer.put(tradeSetting)
        val session = WorldCommunicator.getSession()
        if (session != null) {
            session.write(buffer)
        } else {
            player.sendMessage("Privacy settings unavailable at the moment. Please try again later.")
        }
    }
}
