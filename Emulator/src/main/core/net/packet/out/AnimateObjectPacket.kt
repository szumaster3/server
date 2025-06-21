package core.net.packet.out

import core.game.world.update.flag.context.Animation
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Represents the packet used to animate an object.
 *
 * @author Vexia (10/11/2013)
 */
class AnimateObjectPacket : OutgoingPacket<OutgoingContext.AnimateObject> {

    override fun send(context: OutgoingContext.AnimateObject) {
        val player = context.player
        val `object` = context.animation.getObject()
        val buffer =
            write(UpdateAreaPosition.getBuffer(player, `object`.location.chunkBase), context.animation)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        player.session.write(buffer)
    }

    companion object {
        /**
         * Write io buffer.
         *
         * @param buffer    the buffer
         * @param animation the animation
         * @return the io buffer
         */
        fun write(buffer: IoBuffer, animation: Animation): IoBuffer {
            val `object` = animation.getObject()
            val l = `object`.location
            buffer.put(20)
            buffer.putS((l.chunkOffsetX shl 4) or (l.chunkOffsetY and 0x7))
            buffer.putS((`object`.type shl 2) + (`object`.rotation and 0x3))
            buffer.putLEShort(animation.id)
            return buffer
        }
    }
}
