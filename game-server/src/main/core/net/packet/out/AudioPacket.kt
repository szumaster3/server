package core.net.packet.out

import core.game.node.entity.player.link.audio.Audio
import core.game.world.map.Location
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Sends an audio packet.
 *
 * @author Vexia
 */
class AudioPacket : OutgoingPacket<OutgoingContext.Default> {

    companion object {
        /**
         * Write io buffer.
         *
         * @param buffer the buffer
         * @param audio  the audio
         * @param loc    the loc
         * @return the io buffer
         */
        @JvmStatic
        fun write(buffer: IoBuffer, audio: Audio, loc: Location?): IoBuffer {
            if (loc == null) {
                buffer.put(172)
                buffer.putShort(audio.id)
                buffer.put(audio.loops)
                buffer.putShort(audio.delay)
            } else {
                buffer.put(97)
                buffer.put((loc.chunkOffsetX shl 4) or loc.chunkOffsetY)
                buffer.putShort(audio.id)
                buffer.put((audio.radius shl 4) or (audio.loops and 7))
                buffer.put(audio.delay)
            }
            return buffer
        }
    }

    // 208 music effect
    // 4 music
    // 172 sound effect
    override fun send(context: OutgoingContext.Default) {
        val audio = context.objects[0] as Audio
        val loc = context.objects[1] as Location?
        val buffer = if (loc == null) {
            IoBuffer()
        } else {
            UpdateAreaPosition.getBuffer(context.player, loc.chunkBase)
        }
        write(buffer, audio, loc)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.session.write(buffer)
    }
}
